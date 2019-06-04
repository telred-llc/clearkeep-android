package org.matrix.androidsdk.rest.model.keys;

import java.lang.System;

/**
 * * Backup data for several keys in several rooms.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R*\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/model/keys/KeysBackupData;", "", "()V", "roomIdToRoomKeysBackupData", "", "", "Lorg/matrix/androidsdk/rest/model/keys/RoomKeysBackupData;", "getRoomIdToRoomKeysBackupData", "()Ljava/util/Map;", "setRoomIdToRoomKeysBackupData", "(Ljava/util/Map;)V", "matrix-sdk_debug"})
public final class KeysBackupData {
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "rooms")
    private java.util.Map<java.lang.String, org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData> roomIdToRoomKeysBackupData;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData> getRoomIdToRoomKeysBackupData() {
        return null;
    }
    
    public final void setRoomIdToRoomKeysBackupData(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData> p0) {
    }
    
    public KeysBackupData() {
        super();
    }
}