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

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface JwtAuthenticationConfigurationSet extends GenericEntity {

	EntityType<JwtAuthenticationConfigurationSet> T = EntityTypes.T(JwtAuthenticationConfigurationSet.class);

	@Initializer("'auth'")
	String getAuthAccessId();
	void setAuthAccessId(String authAccessId);

	@Initializer("600000L")
	long getKeyReloadIntervalInMs();
	void setKeyReloadIntervalInMs(long keyReloadIntervalInMs);

	Set<JwtAuthenticationConfiguration> getConfigurations();
	void setConfigurations(Set<JwtAuthenticationConfiguration> configurations);

	@Initializer("false")
	boolean getAcceptSslCertificates();
	void setAcceptSslCertificates(boolean acceptSslCertificates);

	default boolean enabled() {
		return getConfigurations().stream().filter(c -> c.getEnabled()).map(c -> c.getEnabled()).findFirst().orElse(false);
	}
}
