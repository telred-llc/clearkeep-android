package org.matrix.androidsdk.rest.model.login;

import java.lang.System;

/**
 * * Class to define the authentication parameters for threePid type
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParamsThreePid;", "Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "type", "", "(Ljava/lang/String;)V", "threePidCredentials", "Lorg/matrix/androidsdk/rest/model/login/ThreePidCredentials;", "matrix-sdk_debug"})
public final class AuthParamsThreePid extends org.matrix.androidsdk.rest.model.login.AuthParams {
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "threepid_creds")
    public final org.matrix.androidsdk.rest.model.login.ThreePidCredentials threePidCredentials = null;
    
    public AuthParamsThreePid(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
        super(null);
    }
}