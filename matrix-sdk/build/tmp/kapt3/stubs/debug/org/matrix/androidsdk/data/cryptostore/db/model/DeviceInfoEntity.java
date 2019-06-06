package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB3\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0010\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0018R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\t\"\u0004\b\u000f\u0010\u000bR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000bR\u001e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u00138\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006\u001d"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/DeviceInfoEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "deviceId", "identityKey", "deviceInfoData", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "getDeviceInfoData", "setDeviceInfoData", "getIdentityKey", "setIdentityKey", "getPrimaryKey", "setPrimaryKey", "users", "Lio/realm/RealmResults;", "Lorg/matrix/androidsdk/data/cryptostore/db/model/UserEntity;", "getUsers", "()Lio/realm/RealmResults;", "getDeviceInfo", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "putDeviceInfo", "", "deviceInfo", "Companion", "matrix-sdk_debug"})
public class DeviceInfoEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.LinkingObjects(value = "devices")
    private final io.realm.RealmResults<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity> users = null;
    @org.jetbrains.annotations.NotNull()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String primaryKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String identityKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceInfoData;
    public static final org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.data.MXDeviceInfo getDeviceInfo() {
        return null;
    }
    
    public final void putDeviceInfo(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.data.MXDeviceInfo deviceInfo) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final io.realm.RealmResults<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity> getUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPrimaryKey() {
        return null;
    }
    
    public final void setPrimaryKey(@org.jetbrains.annotations.NotNull()
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
    public final java.lang.String getIdentityKey() {
        return null;
    }
    
    public final void setIdentityKey(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceInfoData() {
        return null;
    }
    
    public final void setDeviceInfoData(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public DeviceInfoEntity(@org.jetbrains.annotations.NotNull()
    java.lang.String primaryKey, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceId, @org.jetbrains.annotations.Nullable()
    java.lang.String identityKey, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceInfoData) {
        super();
    }
    
    public DeviceInfoEntity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/DeviceInfoEntity$Companion;", "", "()V", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}