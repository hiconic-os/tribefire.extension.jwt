// ************
// Types
// ************

import '@dev.hiconic/gm_root-model';
import '@dev.hiconic/gm_gm-core-api';

import { T } from '@dev.hiconic/hc-js-base';

export declare namespace meta {
	const groupId: string;
	const artifactId: string;
	const version: string;
}

export import JwtAuthenticationConfiguration = T.tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration;

declare module '@dev.hiconic/hc-js-base' {

	namespace T.tribefire.extension.jwt.model.configuration {

		const JwtAuthenticationConfiguration: hc.reflection.EntityType<JwtAuthenticationConfiguration>;
		type JwtAuthenticationConfiguration = T.com.braintribe.model.generic.GenericEntity &
		  Entity<"tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration", {
			acceptSslCertificates: P<boolean, { nullable: false }>;
			authAccessId: string;
			claimRolesAndPrefixes: map<string, string>;
			defaultRoles: set<string>;
			emailClaim: string;
			enabled: P<boolean, { nullable: false }>;
			firstNameClaim: string;
			invalidateTokenCredentialsOnLogout: P<boolean, { nullable: false }>;
			jwksUrl: string;
			keyReloadIntervalInMs: P<long, { nullable: false }>;
			lastNameClaim: string;
			propertiesClaims: set<string>;
			rolesClaim: string;
			syncWithAuthAccess: P<boolean, { nullable: false }>;
			usernameClaim: string;
		}>;

	}

}
