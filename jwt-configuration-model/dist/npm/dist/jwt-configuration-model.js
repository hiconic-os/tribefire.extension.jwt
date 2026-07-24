import '@dev.hiconic/gm_root-model';
import '@dev.hiconic/gm_gm-core-api';

import {T, hc} from '@dev.hiconic/hc-js-base';

export const meta = {
	groupId: "tribefire.extension.jwt",
	artifactId: "jwt-configuration-model",
	version: "1.0.1",
}

function modelAssembler($, P, _) {
//JSE version=4.0
//BEGIN_TYPES
P.a=$.T("com.braintribe.model.meta.GmMetaModel");
P.b=$.T("com.braintribe.model.meta.GmEntityType");
P.c=$.T("com.braintribe.model.meta.GmProperty");
P.d=$.T("com.braintribe.model.meta.GmBooleanType");
P.e=$.T("com.braintribe.model.meta.GmStringType");
P.f=$.T("com.braintribe.model.meta.GmMapType");
P.g=$.T("com.braintribe.model.meta.GmSetType");
P.h=$.T("com.braintribe.model.meta.GmLongType");
//END_TYPES
P.i=$.P(P.a,'name');P.j=$.P(P.a,'types');P.k=$.P(P.a,'version');P.l=$.P(P.b,'globalId');P.m=$.P(P.b,'isAbstract');P.n=$.P(P.b,'properties');P.o=$.P(P.b,'superTypes');
P.p=$.P(P.b,'typeSignature');P.q=$.P(P.c,'declaringType');P.r=$.P(P.c,'globalId');P.s=$.P(P.c,'initializer');P.t=$.P(P.c,'name');P.u=$.P(P.c,'nullable');P.v=$.P(P.c,'type');
P.w=$.P(P.d,'typeSignature');P.x=$.P(P.e,'typeSignature');P.y=$.P(P.f,'globalId');P.z=$.P(P.f,'keyType');P.A=$.P(P.f,'typeSignature');P.B=$.P(P.f,'valueType');P.C=$.P(P.g,'elementType');
P.D=$.P(P.g,'globalId');P.E=$.P(P.g,'typeSignature');P.F=$.P(P.h,'typeSignature');
P.G=$.C(P.a);P.H=$.C(P.b);P.I=$.C(P.c);P.J=$.C(P.c);P.K=$.C(P.c);P.L=$.C(P.c);P.M=$.C(P.c);P.N=$.C(P.c);P.O=$.C(P.c);P.P=$.C(P.c);P.Q=$.C(P.c);P.R=$.C(P.c);P.S=$.C(P.c);
P.T=$.C(P.c);P.U=$.C(P.c);P.V=$.C(P.c);P.W=$.C(P.c);P.X=$.C(P.b);P.Y=$.C(P.d);P.Z=$.C(P.e);P.$=$.C(P.f);P.aa=$.C(P.g);P.ba=$.C(P.h);
_=P.G;
$.s(_,P.i,"tribefire.extension.jwt:jwt-configuration-model");
$.s(_,P.j,$.S([P.H]));
$.s(_,P.k,"1.0.1");
_=P.H;
$.s(_,P.l,"type:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration");
$.s(_,P.m,$.n);
$.s(_,P.n,$.L([P.I,P.J,P.K,P.L,P.M,P.N,P.O,P.P,P.Q,P.R,P.S,P.T,P.U,P.V,P.W]));
$.s(_,P.o,$.L([P.X]));
$.s(_,P.p,"tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration");
_=P.I;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/acceptSslCertificates");
$.s(_,P.s,$.n);
$.s(_,P.t,"acceptSslCertificates");
$.s(_,P.u,$.n);
$.s(_,P.v,P.Y);
_=P.J;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/authAccessId");
$.s(_,P.s,"auth");
$.s(_,P.t,"authAccessId");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.K;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/claimRolesAndPrefixes");
$.s(_,P.t,"claimRolesAndPrefixes");
$.s(_,P.u,$.y);
$.s(_,P.v,P.$);
_=P.L;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/defaultRoles");
$.s(_,P.t,"defaultRoles");
$.s(_,P.u,$.y);
$.s(_,P.v,P.aa);
_=P.M;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/emailClaim");
$.s(_,P.s,"upn");
$.s(_,P.t,"emailClaim");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.N;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/enabled");
$.s(_,P.s,$.y);
$.s(_,P.t,"enabled");
$.s(_,P.u,$.n);
$.s(_,P.v,P.Y);
_=P.O;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/firstNameClaim");
$.s(_,P.s,"given_name");
$.s(_,P.t,"firstNameClaim");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.P;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/invalidateTokenCredentialsOnLogout");
$.s(_,P.s,$.y);
$.s(_,P.t,"invalidateTokenCredentialsOnLogout");
$.s(_,P.u,$.n);
$.s(_,P.v,P.Y);
_=P.Q;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/jwksUrl");
$.s(_,P.t,"jwksUrl");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.R;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/keyReloadIntervalInMs");
$.s(_,P.s,$.l({l:600000,m:0,h:0}));
$.s(_,P.t,"keyReloadIntervalInMs");
$.s(_,P.u,$.n);
$.s(_,P.v,P.ba);
_=P.S;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/lastNameClaim");
$.s(_,P.s,"family_name");
$.s(_,P.t,"lastNameClaim");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.T;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/propertiesClaims");
$.s(_,P.t,"propertiesClaims");
$.s(_,P.u,$.y);
$.s(_,P.v,P.aa);
_=P.U;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/rolesClaim");
$.s(_,P.s,"roles");
$.s(_,P.t,"rolesClaim");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.V;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/syncWithAuthAccess");
$.s(_,P.s,$.n);
$.s(_,P.t,"syncWithAuthAccess");
$.s(_,P.u,$.n);
$.s(_,P.v,P.Y);
_=P.W;
$.s(_,P.q,P.H);
$.s(_,P.r,"property:tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration/usernameClaim");
$.s(_,P.s,"sub");
$.s(_,P.t,"usernameClaim");
$.s(_,P.u,$.y);
$.s(_,P.v,P.Z);
_=P.X;
$.s(_,P.m,$.n);
$.s(_,P.p,"com.braintribe.model.generic.GenericEntity");
_=P.Y;
$.s(_,P.w,"boolean");
_=P.Z;
$.s(_,P.x,"string");
_=P.$;
$.s(_,P.y,"type:map<string,string>");
$.s(_,P.z,P.Z);
$.s(_,P.A,"map<string,string>");
$.s(_,P.B,P.Z);
_=P.aa;
$.s(_,P.C,P.Z);
$.s(_,P.D,"type:set<string>");
$.s(_,P.E,"set<string>");
_=P.ba;
$.s(_,P.F,"long");
return P.G;
[5364];
}

hc.reflection.internal.ensureModel(modelAssembler)

export const JwtAuthenticationConfiguration = T.tribefire.extension.jwt.model.configuration.JwtAuthenticationConfiguration;
