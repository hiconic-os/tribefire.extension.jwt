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
package tribefire.extension.jwt.model;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface JwksKey extends GenericEntity {

	EntityType<JwksKey> T = EntityTypes.T(JwksKey.class);

	String alg = "alg";
	String e = "e";
	String kid = "kid";
	String kty = "kty";
	String n = "n";
	String use = "use";

	String getAlg();
	void setAlg(String alg);

	String getE();
	void setE(String e);

	String getKid();
	void setKid(String kid);

	String getKty();
	void setKty(String kty);

	String getN();
	void setN(String n);

	String getUse();
	void setUse(String use);

	@Override
	default String asString() {
		StringBuilder sb = new StringBuilder();
		sb.append("alg: " + getAlg());
		sb.append(", e: " + getE());
		sb.append(", kid: " + getKid());
		sb.append(", kty: " + getKty());
		sb.append(", n: " + getN());
		sb.append(", use: " + getUse());
		return sb.toString();
	}
}
