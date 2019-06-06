package org.matrix.androidsdk.rest.model.login;

import java.lang.System;

/**
 * * Class to define the authentication parameters for "m.login.email.identity" type
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParamsEmailIdentity;", "Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "()V", "threePidCredentials", "Lorg/matrix/androidsdk/rest/model/login/ThreePidCredentials;", "matrix-sdk_debug"})
public final class AuthParamsEmailIdentity extends org.matrix.androidsdk.rest.model.login.AuthParams {
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "threepid_creds")
    public org.matrix.androidsdk.rest.model.login.ThreePidCredentials threePidCredentials;
    
    public AuthParamsEmailIdentity() {
        super(null);
    }
}