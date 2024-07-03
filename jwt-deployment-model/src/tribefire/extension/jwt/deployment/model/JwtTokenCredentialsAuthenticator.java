// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.extension.jwt.deployment.model;

import java.util.Map;
import java.util.Set;

import com.braintribe.model.extensiondeployment.ServiceProcessor;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface JwtTokenCredentialsAuthenticator extends ServiceProcessor {

	EntityType<JwtTokenCredentialsAuthenticator> T = EntityTypes.T(JwtTokenCredentialsAuthenticator.class);

	String defaultRoles = "defaultRoles";
	String jwksUrl = "jwksUrl";
	String usernameClaim = "usernameClaim";
	String rolesClaim = "rolesClaim";
	String claimRolesAndPrefixes = "claimRolesAndPrefixes";
	String propertiesClaims = "propertiesClaims";
	String invalidateTokenCredentialsOnLogout = "invalidateTokenCredentialsOnLogout";

	@Name("Default Roles")
	@Description("A set of roles users should get.")
	Set<String> getDefaultRoles();
	void setDefaultRoles(Set<String> defaultRoles);

	@Name("JWKS URL")
	@Description("The URL where to download JWKS information.")
	String getJwksUrl();
	void setJwksUrl(String jwksUrl);

	@Name("Username Claim")
	@Description("The claim that contains the user name.")
	@Initializer("'sub'")
	String getUsernameClaim();
	void setUsernameClaim(String usernameClaim);

	@Name("Roles Claim")
	@Description("The claim that contains the user roles.")
	@Initializer("'roles'")
	String getRolesClaim();
	void setRolesClaim(String rolesClaim);

	@Description("A map that maps from a Claim property name to a prefix that should be applied to all values to deduce user roles")
	@Name("Claim Roles and Prefixes")
	Map<String, String> getClaimRolesAndPrefixes();
	void setClaimRolesAndPrefixes(Map<String, String> claimRolesAndPrefixes);

	@Name("Properties Claims")
	@Description("Claims that should be added to the properties (which eventually will be added to the user session).")
	Set<String> getPropertiesClaims();
	void setPropertiesClaims(Set<String> propertiesClaims);

	@Name("Invalidate JwtTokenCredentials on Logout")
	@Initializer("true")
	boolean getInvalidateTokenCredentialsOnLogout();
	void setInvalidateTokenCredentialsOnLogout(boolean invalidateTokenCredentialsOnLogout);
}
