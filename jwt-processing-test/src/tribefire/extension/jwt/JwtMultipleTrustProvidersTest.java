// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.extension.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.security.reason.InvalidCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentialsResponse;
import com.braintribe.model.securityservice.AuthenticatedUser;
import com.braintribe.model.securityservice.credentials.JwtTokenCredentials;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.sun.net.httpserver.HttpServer;

import io.jsonwebtoken.Jwts;
import tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration;
import tribefire.extension.jwt.model.configuration.JwtTrustProvider;
import tribefire.extension.jwt.processing.JwtTokenCredentialsAuthenticationServiceProcessor;

/**
 * Multiple trust providers whose JWKS endpoints publish keys with the same key id (kid).
 */
public class JwtMultipleTrustProvidersTest {

	private static final String KID = "shared-kid";
	private static final String CLIENT_ID_1 = "bbfcd6ba-ef0c-4d83-bf83-0f0aed6ff94c";
	private static final String CLIENT_ID_2 = "fe3547d7-1e4b-46a3-a574-2dcfc3edd528";

	private HttpServer server;

	@Before
	public void startServer() throws Exception {
		server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.start();
	}

	@After
	public void stopServer() {
		server.stop(0);
	}

	@Test
	public void differentKeysTokenOfFirstProviderIsAccepted() throws Exception {
		KeyPair keyPair1 = generateKeyPair();
		KeyPair keyPair2 = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair1), "unique_name");
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair2), "oid");

		String token = token(keyPair1, CLIENT_ID_1);

		assertAuthenticatedAs(authenticate(token, provider1, provider2), "user-name");
	}

	@Test
	public void differentKeysTokenOfSecondProviderIsAccepted() throws Exception {
		KeyPair keyPair1 = generateKeyPair();
		KeyPair keyPair2 = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair1), "unique_name");
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair2), "oid");

		String token = token(keyPair2, CLIENT_ID_2);

		assertAuthenticatedAs(authenticate(token, provider1, provider2), "object-id");
	}

	@Test
	public void tokenMatchingNoKeyIsRejected() throws Exception {
		KeyPair keyPair1 = generateKeyPair();
		KeyPair keyPair2 = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair1), "unique_name");
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair2), "oid");

		String token = token(generateKeyPair(), CLIENT_ID_1);

		assertInvalidCredentials(authenticate(token, provider1, provider2));
	}

	@Test
	public void sameKeyWithIdenticalProvidersIsAccepted() throws Exception {
		KeyPair keyPair = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair), "unique_name");
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair), "unique_name");

		String token = token(keyPair, CLIENT_ID_1);

		assertAuthenticatedAs(authenticate(token, provider1, provider2), "user-name");
	}

	@Test
	public void sameKeyProviderIsSelectedByAppIdOfJwksUrl() throws Exception {
		KeyPair keyPair = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair) + "?appid=" + CLIENT_ID_1, "unique_name");
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair) + "?appid=" + CLIENT_ID_2, "oid");

		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_1), provider1, provider2), "user-name");
		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_2), provider1, provider2), "object-id");

		// order of the trust providers must not matter
		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_1), provider2, provider1), "user-name");
		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_2), provider2, provider1), "object-id");
	}

	@Test
	public void sameKeyProviderIsSelectedByConfiguredAudience() throws Exception {
		KeyPair keyPair = generateKeyPair();

		JwtTrustProvider provider1 = provider(publishJwks("/jwks1", keyPair), "unique_name");
		provider1.setAudiences(Set.of(CLIENT_ID_1));
		JwtTrustProvider provider2 = provider(publishJwks("/jwks2", keyPair), "oid");
		provider2.setAudiences(Set.of(CLIENT_ID_2));

		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_1), provider2, provider1), "user-name");
		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_2), provider1, provider2), "object-id");
	}

	@Test
	public void tokenWithAudienceNotConfiguredIsRejected() throws Exception {
		KeyPair keyPair = generateKeyPair();

		JwtTrustProvider provider = provider(publishJwks("/jwks1", keyPair), "unique_name");
		provider.setAudiences(Set.of(CLIENT_ID_1));

		assertInvalidCredentials(authenticate(token(keyPair, CLIENT_ID_2), provider));
	}

	@Test
	public void tokenWithAudienceNotMatchingAppIdIsStillAccepted() throws Exception {
		KeyPair keyPair = generateKeyPair();

		JwtTrustProvider provider = provider(publishJwks("/jwks1", keyPair) + "?appid=" + CLIENT_ID_1, "unique_name");

		assertAuthenticatedAs(authenticate(token(keyPair, CLIENT_ID_2), provider), "user-name");
	}

	private String publishJwks(String path, KeyPair keyPair) {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String jwks = "{\"keys\":[{\"kid\":\"" + KID + "\",\"kty\":\"RSA\",\"alg\":\"RS256\",\"use\":\"sig\",\"n\":\""
				+ base64Url(publicKey.getModulus()) + "\",\"e\":\"" + base64Url(publicKey.getPublicExponent()) + "\"}]}";

		server.createContext(path, exchange -> {
			byte[] body = jwks.getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders().set("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, body.length);
			exchange.getResponseBody().write(body);
			exchange.close();
		});

		return "http://127.0.0.1:" + server.getAddress().getPort() + path;
	}

	private static JwtTrustProvider provider(String jwksUrl, String usernameClaim) {
		JwtTrustProvider provider = JwtTrustProvider.T.create();
		provider.setJwksUrl(jwksUrl);
		provider.setUsernameClaim(usernameClaim);
		return provider;
	}

	private static String token(KeyPair keyPair, String audience) {
		return Jwts.builder() //
				.header().keyId(KID).and() //
				.audience().add(audience).and() //
				.claim("unique_name", "user-name") //
				.claim("oid", "object-id") //
				.signWith(keyPair.getPrivate()) //
				.compact();
	}

	private Maybe<? extends AuthenticateCredentialsResponse> authenticate(String token, JwtTrustProvider... providers) {
		JwtAuthenticationConfiguration configuration = JwtAuthenticationConfiguration.T.create();
		configuration.getTrustProviders().addAll(Arrays.asList(providers));

		JwtTokenCredentialsAuthenticationServiceProcessor processor = new JwtTokenCredentialsAuthenticationServiceProcessor();
		processor.setConfigurationSet(configuration);
		processor.setJsonMarshaller(new JsonStreamMarshaller());
		processor.setHttpClientProvider(new DefaultHttpClientProvider());
		processor.setModuleClassLoader(getClass().getClassLoader());

		JwtTokenCredentials credentials = JwtTokenCredentials.T.create();
		credentials.setToken(token);
		AuthenticateCredentials request = AuthenticateCredentials.T.create();
		request.setCredentials(credentials);

		return processor.processReasoned(null, request);
	}

	private static void assertAuthenticatedAs(Maybe<? extends AuthenticateCredentialsResponse> result, String userName) {
		assertThat(result.isSatisfied()).isTrue();
		AuthenticatedUser authenticated = (AuthenticatedUser) result.get();
		assertThat(authenticated.getUser().getName()).isEqualTo(userName);
	}

	private static void assertInvalidCredentials(Maybe<? extends AuthenticateCredentialsResponse> result) {
		assertThat(result.isUnsatisfied()).isTrue();
		assertThat(result.whyUnsatisfied() instanceof InvalidCredentials).isTrue();
	}

	private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		return generator.generateKeyPair();
	}

	private static String base64Url(BigInteger value) {
		byte[] bytes = value.toByteArray();
		if (bytes.length > 1 && bytes[0] == 0)
			bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
