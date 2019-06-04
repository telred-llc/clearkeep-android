package org.matrix.androidsdk.login;

import java.lang.System;

/**
 * * This class help to find WellKnown data of a Homeserver and provide action that must be done depending on well-known request result
 * * and its data validation
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u0013\u0014B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0002J\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u001e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery;", "", "()V", "wellKnownRestClient", "Lorg/matrix/androidsdk/rest/client/WellKnownRestClient;", "findClientConfig", "", "domain", "", "callback", "Lorg/matrix/androidsdk/rest/callback/ApiCallback;", "Lorg/matrix/androidsdk/login/AutoDiscovery$DiscoveredClientConfig;", "isValidURL", "", "url", "validateHomeServerAndProceed", "wellKnown", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "validateIdentityServerAndFinish", "Action", "DiscoveredClientConfig", "matrix-sdk_debug"})
public final class AutoDiscovery {
    private final org.matrix.androidsdk.rest.client.WellKnownRestClient wellKnownRestClient = null;
    
    /**
     * * Find client config
     *     *
     *     * - Do the .well-known request
     *     * - validate homeserver url and identity server url if provide in .well-known result
     *     * - return action and .well-known data
     *     *
     *     * @param domain: homeserver domain, deduced from mx userId (ex: "matrix.org" from userId "@user:matrix.org")
     *     * @param callback to get the result
     */
    public final void findClientConfig(@org.jetbrains.annotations.NotNull()
    java.lang.String domain, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig> callback) {
    }
    
    private final void validateHomeServerAndProceed(org.matrix.androidsdk.rest.model.WellKnown wellKnown, org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig> callback) {
    }
    
    private final void validateIdentityServerAndFinish(org.matrix.androidsdk.rest.model.WellKnown wellKnown, org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig> callback) {
    }
    
    private final boolean isValidURL(java.lang.String url) {
        return false;
    }
    
    public AutoDiscovery() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u001f\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery$DiscoveredClientConfig;", "", "action", "Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "wellKnown", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "(Lorg/matrix/androidsdk/login/AutoDiscovery$Action;Lorg/matrix/androidsdk/rest/model/WellKnown;)V", "getAction", "()Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "getWellKnown", "()Lorg/matrix/androidsdk/rest/model/WellKnown;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "matrix-sdk_debug"})
    public static final class DiscoveredClientConfig {
        @org.jetbrains.annotations.NotNull()
        private final org.matrix.androidsdk.login.AutoDiscovery.Action action = null;
        @org.jetbrains.annotations.Nullable()
        private final org.matrix.androidsdk.rest.model.WellKnown wellKnown = null;
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.login.AutoDiscovery.Action getAction() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final org.matrix.androidsdk.rest.model.WellKnown getWellKnown() {
            return null;
        }
        
        public DiscoveredClientConfig(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.login.AutoDiscovery.Action action, @org.jetbrains.annotations.Nullable()
        org.matrix.androidsdk.rest.model.WellKnown wellKnown) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.login.AutoDiscovery.Action component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final org.matrix.androidsdk.rest.model.WellKnown component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig copy(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.login.AutoDiscovery.Action action, @org.jetbrains.annotations.Nullable()
        org.matrix.androidsdk.rest.model.WellKnown wellKnown) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public java.lang.String toString() {
            return null;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object p0) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "", "(Ljava/lang/String;I)V", "PROMPT", "IGNORE", "FAIL_PROMPT", "FAIL_ERROR", "matrix-sdk_debug"})
    public static enum Action {
        /*public static final*/ PROMPT /* = new PROMPT() */,
        /*public static final*/ IGNORE /* = new IGNORE() */,
        /*public static final*/ FAIL_PROMPT /* = new FAIL_PROMPT() */,
        /*public static final*/ FAIL_ERROR /* = new FAIL_ERROR() */;
        
        Action() {
        }
    }
}