package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\b\u0010\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b\u00a8\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/CryptoRoomEntity;", "Lio/realm/RealmObject;", "roomId", "", "algorithm", "blacklistUnverifiedDevices", "", "(Ljava/lang/String;Ljava/lang/String;Z)V", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "getBlacklistUnverifiedDevices", "()Z", "setBlacklistUnverifiedDevices", "(Z)V", "getRoomId", "setRoomId", "Companion", "matrix-sdk_debug"})
public class CryptoRoomEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String roomId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String algorithm;
    private boolean blacklistUnverifiedDevices;
    public static final org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRoomId() {
        return null;
    }
    
    public final void setRoomId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAlgorithm() {
        return null;
    }
    
    public final void setAlgorithm(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean getBlacklistUnverifiedDevices() {
        return false;
    }
    
    public final void setBlacklistUnverifiedDevices(boolean p0) {
    }
    
    public CryptoRoomEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String roomId, @org.jetbrains.annotations.Nullable()
    java.lang.String algorithm, boolean blacklistUnverifiedDevices) {
        super();
    }
    
    public CryptoRoomEntity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/CryptoRoomEntity$Companion;", "", "()V", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}