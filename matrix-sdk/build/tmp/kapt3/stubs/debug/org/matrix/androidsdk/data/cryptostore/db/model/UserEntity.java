package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\b\u0016\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B+\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR \u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0017"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/UserEntity;", "Lio/realm/RealmObject;", "userId", "", "devices", "Lio/realm/RealmList;", "Lorg/matrix/androidsdk/data/cryptostore/db/model/DeviceInfoEntity;", "deviceTrackingStatus", "", "(Ljava/lang/String;Lio/realm/RealmList;I)V", "getDeviceTrackingStatus", "()I", "setDeviceTrackingStatus", "(I)V", "getDevices", "()Lio/realm/RealmList;", "setDevices", "(Lio/realm/RealmList;)V", "getUserId", "()Ljava/lang/String;", "setUserId", "(Ljava/lang/String;)V", "Companion", "matrix-sdk_debug"})
public class UserEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String userId;
    @org.jetbrains.annotations.NotNull()
    private io.realm.RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devices;
    private int deviceTrackingStatus;
    public static final org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void setUserId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.realm.RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> getDevices() {
        return null;
    }
    
    public final void setDevices(@org.jetbrains.annotations.NotNull()
    io.realm.RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> p0) {
    }
    
    public final int getDeviceTrackingStatus() {
        return 0;
    }
    
    public final void setDeviceTrackingStatus(int p0) {
    }
    
    public UserEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    io.realm.RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devices, int deviceTrackingStatus) {
        super();
    }
    
    public UserEntity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/UserEntity$Companion;", "", "()V", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}