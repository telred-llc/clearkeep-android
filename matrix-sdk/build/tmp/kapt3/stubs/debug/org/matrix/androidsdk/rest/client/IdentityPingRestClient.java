package org.matrix.androidsdk.rest.client;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0014\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a8\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/client/IdentityPingRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/rest/api/IdentityPingApi;", "hsConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "(Lorg/matrix/androidsdk/HomeServerConnectionConfig;)V", "ping", "", "callback", "Lorg/matrix/androidsdk/rest/callback/ApiCallback;", "Lorg/json/JSONObject;", "matrix-sdk_debug"})
public final class IdentityPingRestClient extends org.matrix.androidsdk.RestClient<org.matrix.androidsdk.rest.api.IdentityPingApi> {
    
    public final void ping(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.json.JSONObject> callback) {
    }
    
    public IdentityPingRestClient(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.HomeServerConnectionConfig hsConfig) {
        super(null, null, null);
    }
}