package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB=\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0019R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000b\"\u0004\b\u0015\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000b\"\u0004\b\u0017\u0010\r\u00a8\u0006\u001e"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/OlmSessionEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "sessionId", "deviceKey", "olmSessionData", "lastReceivedMessageTs", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", "getDeviceKey", "()Ljava/lang/String;", "setDeviceKey", "(Ljava/lang/String;)V", "getLastReceivedMessageTs", "()J", "setLastReceivedMessageTs", "(J)V", "getOlmSessionData", "setOlmSessionData", "getPrimaryKey", "setPrimaryKey", "getSessionId", "setSessionId", "getOlmSession", "Lorg/matrix/olm/OlmSession;", "putOlmSession", "", "olmSession", "Companion", "matrix-sdk_debug"})
public class OlmSessionEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.NotNull()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String primaryKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String sessionId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String olmSessionData;
    private long lastReceivedMessageTs;
    public static final org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.olm.OlmSession getOlmSession() {
        return null;
    }
    
    public final void putOlmSession(@org.jetbrains.annotations.Nullable()
    org.matrix.olm.OlmSession olmSession) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPrimaryKey() {
        return null;
    }
    
    public final void setPrimaryKey(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSessionId() {
        return null;
    }
    
    public final void setSessionId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceKey() {
        return null;
    }
    
    public final void setDeviceKey(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOlmSessionData() {
        return null;
    }
    
    public final void setOlmSessionData(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final long getLastReceivedMessageTs() {
        return 0L;
    }
    
    public final void setLastReceivedMessageTs(long p0) {
    }
    
    public OlmSessionEntity(@org.jetbrains.annotations.NotNull()
    java.lang.String primaryKey, @org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceKey, @org.jetbrains.annotations.Nullable()
    java.lang.String olmSessionData, long lastReceivedMessageTs) {
        super();
    }
    
    public OlmSessionEntity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/OlmSessionEntity$Companion;", "", "()V", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}