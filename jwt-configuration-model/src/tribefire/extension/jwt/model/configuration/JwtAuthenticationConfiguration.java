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
package tribefire.extension.jwt.model.configuration;

import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface JwtAuthenticationConfiguration extends GenericEntity {

	EntityType<JwtAuthenticationConfiguration> T = EntityTypes.T(JwtAuthenticationConfiguration.class);

	String defaultRoles = "defaultRoles";
	String jwksUrl = "jwksUrl";
	String usernameClaim = "usernameClaim";
	String emailClaim = "emailClaim";
	String firstNameClaim = "firstNameClaim";
	String lastNameClaim = "lastNameClaim";
	String rolesClaim = "rolesClaim";
	String claimRolesAndPrefixes = "claimRolesAndPrefixes";
	String propertiesClaims = "propertiesClaims";
	String invalidateTokenCredentialsOnLogout = "invalidateTokenCredentialsOnLogout";
	String syncWithAuthAccess = "syncWithAuthAccess";

	@Initializer("true")
	boolean getEnabled();
	void setEnabled(boolean enabled);

	@Initializer("'auth'")
	String getAuthAccessId();
	void setAuthAccessId(String authAccessId);

	@Initializer("false")
	boolean getAcceptSslCertificates();
	void setAcceptSslCertificates(boolean acceptSslCertificates);

	@Initializer("600000L")
	long getKeyReloadIntervalInMs();
	void setKeyReloadIntervalInMs(long keyReloadIntervalInMs);

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

	@Name("Email Claim")
	@Description("The claim that contains the email address.")
	@Initializer("'upn'")
	String getEmailClaim();
	void setEmailClaim(String emailClaim);

	@Name("First Name Claim")
	@Description("The claim that contains the user's first name.")
	@Initializer("'given_name'")
	String getFirstNameClaim();
	void setFirstNameClaim(String firstNameClaim);

	@Name("Last Name Claim")
	@Description("The claim that contains the user's last/family name.")
	@Initializer("'family_name'")
	String getLastNameClaim();
	void setLastNameClaim(String lastNameClaim);

	@Name("Roles Claim")
	@Description("The claim that contains the user roles.")
	@Initializer("'roles'")
	String getRolesClaim();
	void setRolesClaim(String rolesClaim);

	@Name("Claim Roles and Prefixes")
	@Description("Maps claim names to prefixes applied to all claim values when deriving roles.")
	Map<String, String> getClaimRolesAndPrefixes();
	void setClaimRolesAndPrefixes(Map<String, String> claimRolesAndPrefixes);

	@Name("Properties Claims")
	@Description("Claims copied into the resulting authenticated-user properties.")
	Set<String> getPropertiesClaims();
	void setPropertiesClaims(Set<String> propertiesClaims);

	@Name("Invalidate JwtTokenCredentials on Logout")
	@Initializer("true")
	boolean getInvalidateTokenCredentialsOnLogout();
	void setInvalidateTokenCredentialsOnLogout(boolean invalidateTokenCredentialsOnLogout);

	@Name("Sync with Auth Access")
	@Initializer("false")
	boolean getSyncWithAuthAccess();
	void setSyncWithAuthAccess(boolean syncWithAuthAccess);
}
