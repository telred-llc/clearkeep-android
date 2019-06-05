package org.matrix.androidsdk.rest.model;

import java.lang.System;

/**
 * * https://matrix.org/docs/spec/client_server/r0.4.0.html#server-discovery
 * * <pre>
 * * {
 * *     "base_url": "https://vector.im"
 * * }
 * * </pre>
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/rest/model/WellKnownBaseConfig;", "", "()V", "baseURL", "", "matrix-sdk_debug"})
public final class WellKnownBaseConfig {
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "base_url")
    public java.lang.String baseURL;
    
    public WellKnownBaseConfig() {
        super();
    }
}