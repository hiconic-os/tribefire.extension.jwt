// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// ============================================================================
package tribefire.extension.jwt.rx.wire.space;

import com.braintribe.model.securityservice.credentials.JwtTokenCredentials;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.braintribe.transport.http.HttpClientProvider;
import com.braintribe.transport.ssl.SslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.EasySslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.StrictSslSocketFactoryProvider;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import hiconic.rx.access.module.api.AccessContract;
import hiconic.rx.locking.api.LockingContract;
import hiconic.rx.module.api.wire.RxModuleContract;
import hiconic.rx.module.api.wire.RxPlatformContract;
import hiconic.rx.security.api.SecurityExtensionContract;
import tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration;
import tribefire.extension.jwt.processing.JwtTokenCredentialsAuthenticationServiceProcessor;

@Managed
public class JwtRxModuleSpace implements RxModuleContract {

	@Import private RxPlatformContract platform;
	@Import private SecurityExtensionContract security;
	@Import private AccessContract access;
	@Import private LockingContract locking;

	@Override
	public void onDeploy() {
		if (configuration().getEnabled())
			security.registerCredentialProcessor(JwtTokenCredentials.T, jwtCredentialsAuthenticator());
	}

	@Managed
	private JwtAuthenticationConfiguration configuration() {
		return platform.configuration().readConfig(JwtAuthenticationConfiguration.T).get();
	}

	@Managed
	private JwtTokenCredentialsAuthenticationServiceProcessor jwtCredentialsAuthenticator() {
		JwtAuthenticationConfiguration config = configuration();
		JwtTokenCredentialsAuthenticationServiceProcessor bean = new JwtTokenCredentialsAuthenticationServiceProcessor();
		bean.setConfiguration(config);
		bean.setJsonMarshaller(platform.marshalling().jsonMarshaller());
		bean.setHttpClientProvider(httpClientProvider());
		bean.setModuleClassLoader(JwtRxModuleSpace.class.getClassLoader());
		bean.setKeyMapReloadIntervalInMs(config.getKeyReloadIntervalInMs());
		bean.setAuthSessionProvider(() -> access.systemSessionFactory().newSession(config.getAuthAccessId()));
		bean.setLocking(locking.locking());
		return bean;
	}

	@Managed
	private HttpClientProvider httpClientProvider() {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(sslSocketFactoryProvider());
		return bean;
	}

	@Managed
	private SslSocketFactoryProvider sslSocketFactoryProvider() {
		return configuration().getAcceptSslCertificates()
				? new EasySslSocketFactoryProvider()
				: new StrictSslSocketFactoryProvider();
	}
}
