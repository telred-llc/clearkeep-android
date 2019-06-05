package org.matrix.androidsdk;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "", "testInterceptor", "Lokhttp3/Interceptor;", "(Lokhttp3/Interceptor;)V", "createHttpClient", "Lokhttp3/OkHttpClient;", "hsConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "endPoint", "", "authenticationInterceptor", "Companion", "matrix-sdk_debug"})
public final class RestClientHttpClientFactory {
    private final okhttp3.Interceptor testInterceptor = null;
    public static final long READ_TIMEOUT_MS = 60000L;
    public static final long WRITE_TIMEOUT_MS = 60000L;
    public static final org.matrix.androidsdk.RestClientHttpClientFactory.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient createHttpClient(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.HomeServerConnectionConfig hsConfig, @org.jetbrains.annotations.NotNull()
    java.lang.String endPoint, @org.jetbrains.annotations.NotNull()
    okhttp3.Interceptor authenticationInterceptor) {
        return null;
    }
    
    public RestClientHttpClientFactory(@org.jetbrains.annotations.Nullable()
    okhttp3.Interceptor testInterceptor) {
        super();
    }
    
    public RestClientHttpClientFactory() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/RestClientHttpClientFactory$Companion;", "", "()V", "READ_TIMEOUT_MS", "", "WRITE_TIMEOUT_MS", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}