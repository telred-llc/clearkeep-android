package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001BK\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\nJ\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cJ\u0010\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001cR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e\u00a8\u0006 "}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/CryptoMetadataEntity;", "Lio/realm/RealmObject;", "userId", "", "deviceId", "olmAccountData", "deviceSyncToken", "globalBlacklistUnverifiedDevices", "", "backupVersion", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", "getBackupVersion", "()Ljava/lang/String;", "setBackupVersion", "(Ljava/lang/String;)V", "getDeviceId", "setDeviceId", "getDeviceSyncToken", "setDeviceSyncToken", "getGlobalBlacklistUnverifiedDevices", "()Z", "setGlobalBlacklistUnverifiedDevices", "(Z)V", "getOlmAccountData", "setOlmAccountData", "getUserId", "setUserId", "getOlmAccount", "Lorg/matrix/olm/OlmAccount;", "putOlmAccount", "", "olmAccount", "matrix-sdk_debug"})
public class CryptoMetadataEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String userId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String olmAccountData;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceSyncToken;
    private boolean globalBlacklistUnverifiedDevices;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String backupVersion;
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.olm.OlmAccount getOlmAccount() {
        return null;
    }
    
    public final void putOlmAccount(@org.jetbrains.annotations.Nullable()
    org.matrix.olm.OlmAccount olmAccount) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void setUserId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceId() {
        return null;
    }
    
    public final void setDeviceId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOlmAccountData() {
        return null;
    }
    
    public final void setOlmAccountData(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceSyncToken() {
        return null;
    }
    
    public final void setDeviceSyncToken(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean getGlobalBlacklistUnverifiedDevices() {
        return false;
    }
    
    public final void setGlobalBlacklistUnverifiedDevices(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBackupVersion() {
        return null;
    }
    
    public final void setBackupVersion(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public CryptoMetadataEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceId, @org.jetbrains.annotations.Nullable()
    java.lang.String olmAccountData, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceSyncToken, boolean globalBlacklistUnverifiedDevices, @org.jetbrains.annotations.Nullable()
    java.lang.String backupVersion) {
        super();
    }
    
    public CryptoMetadataEntity() {
        super();
    }
}