// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// ============================================================================
package tribefire.extension.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;

import org.junit.Test;

import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.securityservice.AuthenticateCredentials;
import com.braintribe.model.securityservice.AuthenticateCredentialsResponse;
import com.braintribe.model.securityservice.AuthenticatedUser;
import com.braintribe.model.securityservice.credentials.JwtTokenCredentials;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.sun.net.httpserver.HttpServer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration;
import tribefire.extension.jwt.processing.JwtTokenCredentialsAuthenticationServiceProcessor;

public class JwtAuthenticationProcessorTest {

	@Test
	@SuppressWarnings("deprecation")
	public void authenticatesLocallySignedTokenFromLocalJwks() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		String jwks = "{\"keys\":[{\"kid\":\"test-key\",\"kty\":\"RSA\",\"alg\":\"RS256\",\"use\":\"sig\",\"n\":\""
				+ base64Url(publicKey.getModulus()) + "\",\"e\":\"" + base64Url(publicKey.getPublicExponent()) + "\"}]}";

		HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/jwks", exchange -> {
			byte[] body = jwks.getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders().set("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, body.length);
			exchange.getResponseBody().write(body);
			exchange.close();
		});
		server.start();

		try {
			String token = Jwts.builder()
					.setHeaderParam("kid", "test-key")
					.claim("upn", "jwt-user")
					.claim("email", "jwt-user@example.org")
					.claim("roles", List.of("clerk", "report-analyst"))
					.signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
					.compact();

			JwtAuthenticationConfiguration configuration = JwtAuthenticationConfiguration.T.create();
			configuration.setJwksUrl("http://127.0.0.1:" + server.getAddress().getPort() + "/jwks");
			configuration.setUsernameClaim("upn");
			configuration.setEmailClaim("email");
			configuration.setRolesClaim("roles");

			JwtTokenCredentialsAuthenticationServiceProcessor processor = new JwtTokenCredentialsAuthenticationServiceProcessor();
			processor.setConfiguration(configuration);
			processor.setJsonMarshaller(new JsonStreamMarshaller());
			processor.setHttpClientProvider(new DefaultHttpClientProvider());
			processor.setModuleClassLoader(getClass().getClassLoader());

			JwtTokenCredentials credentials = JwtTokenCredentials.T.create();
			credentials.setToken(token);
			AuthenticateCredentials request = AuthenticateCredentials.T.create();
			request.setCredentials(credentials);

			Maybe<? extends AuthenticateCredentialsResponse> result = processor.processReasoned(null, request);
			assertThat(result.isSatisfied()).isTrue();

			AuthenticatedUser authenticated = (AuthenticatedUser) result.get();
			assertThat(authenticated.getUser().getName()).isEqualTo("jwt-user");
			assertThat(authenticated.getUser().getEmail()).isEqualTo("jwt-user@example.org");
			assertThat(authenticated.getUser().getRoles()).extracting(role -> role.getName())
					.containsExactlyInAnyOrder("clerk", "report-analyst");
		} finally {
			server.stop(0);
		}
	}

	private static String base64Url(BigInteger value) {
		byte[] bytes = value.toByteArray();
		if (bytes.length > 1 && bytes[0] == 0)
			bytes = java.util.Arrays.copyOfRange(bytes, 1, bytes.length);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
