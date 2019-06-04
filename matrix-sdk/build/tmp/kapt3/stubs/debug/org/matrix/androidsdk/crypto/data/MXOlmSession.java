package org.matrix.androidsdk.crypto.data;

import java.lang.System;

/**
 * * Encapsulate a OlmSession and a last received message Timestamp
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\u0006\u0010\u0015\u001a\u00020\u0016J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0019"}, d2 = {"Lorg/matrix/androidsdk/crypto/data/MXOlmSession;", "", "olmSession", "Lorg/matrix/olm/OlmSession;", "lastReceivedMessageTs", "", "(Lorg/matrix/olm/OlmSession;J)V", "getLastReceivedMessageTs", "()J", "setLastReceivedMessageTs", "(J)V", "getOlmSession", "()Lorg/matrix/olm/OlmSession;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "onMessageReceived", "", "toString", "", "matrix-sdk_debug"})
public final class MXOlmSession {
    @org.jetbrains.annotations.NotNull()
    private final org.matrix.olm.OlmSession olmSession = null;
    private long lastReceivedMessageTs;
    
    /**
     * * Notify that a message has been received on this olm session so that it updates `lastReceivedMessageTs`
     */
    public final void onMessageReceived() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.olm.OlmSession getOlmSession() {
        return null;
    }
    
    public final long getLastReceivedMessageTs() {
        return 0L;
    }
    
    public final void setLastReceivedMessageTs(long p0) {
    }
    
    public MXOlmSession(@org.jetbrains.annotations.NotNull()
    org.matrix.olm.OlmSession olmSession, long lastReceivedMessageTs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.olm.OlmSession component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    /**
     * * Encapsulate a OlmSession and a last received message Timestamp
     */
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.data.MXOlmSession copy(@org.jetbrains.annotations.NotNull()
    org.matrix.olm.OlmSession olmSession, long lastReceivedMessageTs) {
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