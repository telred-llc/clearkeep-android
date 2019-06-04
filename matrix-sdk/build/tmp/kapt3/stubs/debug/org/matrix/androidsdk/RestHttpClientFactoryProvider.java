package org.matrix.androidsdk;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lorg/matrix/androidsdk/RestHttpClientFactoryProvider;", "", "()V", "Companion", "matrix-sdk_debug"})
public final class RestHttpClientFactoryProvider {
    @org.jetbrains.annotations.NotNull()
    private static org.matrix.androidsdk.RestClientHttpClientFactory defaultProvider;
    public static final org.matrix.androidsdk.RestHttpClientFactoryProvider.Companion Companion = null;
    
    public RestHttpClientFactoryProvider() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/RestHttpClientFactoryProvider$Companion;", "", "()V", "defaultProvider", "Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "getDefaultProvider", "()Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "setDefaultProvider", "(Lorg/matrix/androidsdk/RestClientHttpClientFactory;)V", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.RestClientHttpClientFactory getDefaultProvider() {
            return null;
        }
        
        public final void setDefaultProvider(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.RestClientHttpClientFactory p0) {
        }
        
        private Companion() {
            super();
        }
    }
}