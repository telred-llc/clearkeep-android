package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB?\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0019R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000f\"\u0004\b\u0013\u0010\u0011R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/OlmInboundGroupSessionEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "sessionId", "senderKey", "olmInboundGroupSessionData", "backedUp", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getBackedUp", "()Z", "setBackedUp", "(Z)V", "getOlmInboundGroupSessionData", "()Ljava/lang/String;", "setOlmInboundGroupSessionData", "(Ljava/lang/String;)V", "getPrimaryKey", "setPrimaryKey", "getSenderKey", "setSenderKey", "getSessionId", "setSessionId", "getInboundGroupSession", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "putInboundGroupSession", "", "mxOlmInboundGroupSession2", "Companion", "matrix-sdk_debug"})
public class OlmInboundGroupSessionEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String primaryKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String sessionId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String senderKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String olmInboundGroupSessionData;
    private boolean backedUp;
    public static final org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2 getInboundGroupSession() {
        return null;
    }
    
    public final void putInboundGroupSession(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2 mxOlmInboundGroupSession2) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPrimaryKey() {
        return null;
    }
    
    public final void setPrimaryKey(@org.jetbrains.annotations.Nullable()
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
    public final java.lang.String getSenderKey() {
        return null;
    }
    
    public final void setSenderKey(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOlmInboundGroupSessionData() {
        return null;
    }
    
    public final void setOlmInboundGroupSessionData(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean getBackedUp() {
        return false;
    }
    
    public final void setBackedUp(boolean p0) {
    }
    
    public OlmInboundGroupSessionEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String primaryKey, @org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String senderKey, @org.jetbrains.annotations.Nullable()
    java.lang.String olmInboundGroupSessionData, boolean backedUp) {
        super();
    }
    
    public OlmInboundGroupSessionEntity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/OlmInboundGroupSessionEntity$Companion;", "", "()V", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}