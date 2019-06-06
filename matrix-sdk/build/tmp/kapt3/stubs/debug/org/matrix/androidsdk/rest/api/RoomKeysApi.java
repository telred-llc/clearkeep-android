package org.matrix.androidsdk.rest.api;

import java.lang.System;

/**
 * * Ref: https://github.com/uhoreg/matrix-doc/blob/e2e_backup/proposals/1219-storing-megolm-keys-serverside.md
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J,\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH\'J\"\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH\'J\u0018\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003H\'J\u0018\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J,\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH\'J\"\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH\'J\u0018\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J6\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001b\u001a\u00020\u0014H\'J,\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001d\u001a\u00020\u0016H\'J\"\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001f\u001a\u00020\u0018H\'J\"\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010!\u001a\u00020\"H\'\u00a8\u0006#"}, d2 = {"Lorg/matrix/androidsdk/rest/api/RoomKeysApi;", "", "createKeysBackupVersion", "Lretrofit2/Call;", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersion;", "createKeysBackupVersionBody", "Lorg/matrix/androidsdk/rest/model/keys/CreateKeysBackupVersionBody;", "deleteBackup", "Ljava/lang/Void;", "version", "", "deleteRoomSessionData", "roomId", "sessionId", "deleteRoomSessionsData", "deleteSessionsData", "getKeysBackupLastVersion", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersionResult;", "getKeysBackupVersion", "getRoomSessionData", "Lorg/matrix/androidsdk/rest/model/keys/KeyBackupData;", "getRoomSessionsData", "Lorg/matrix/androidsdk/rest/model/keys/RoomKeysBackupData;", "getSessionsData", "Lorg/matrix/androidsdk/rest/model/keys/KeysBackupData;", "storeRoomSessionData", "Lorg/matrix/androidsdk/rest/model/keys/BackupKeysResult;", "keyBackupData", "storeRoomSessionsData", "roomKeysBackupData", "storeSessionsData", "keysBackupData", "updateKeysBackupVersion", "keysBackupVersionBody", "Lorg/matrix/androidsdk/rest/model/keys/UpdateKeysBackupVersionBody;", "matrix-sdk_debug"})
public abstract interface RoomKeysApi {
    
    /**
     * * Create a new keys backup version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.POST(value = "room_keys/version")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.KeysVersion> createKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    org.matrix.androidsdk.rest.model.keys.CreateKeysBackupVersionBody createKeysBackupVersionBody);
    
    /**
     * * Get information about the last version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "room_keys/version")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> getKeysBackupLastVersion();
    
    /**
     * * Get information about the given version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "room_keys/version/{version}")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> getKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "version")
    java.lang.String version);
    
    /**
     * * Update information about the given version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.PUT(value = "room_keys/version/{version}")
    public abstract retrofit2.Call<java.lang.Void> updateKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "version")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    org.matrix.androidsdk.rest.model.keys.UpdateKeysBackupVersionBody keysBackupVersionBody);
    
    /**
     * * Store the key for the given session in the given room, using the given backup version.
     *     *
     *     *
     *     * If the server already has a backup in the backup version for the given session and room, then it will
     *     * keep the "better" one. To determine which one is "better", key backups are compared first by the is_verified
     *     * flag (true is better than false), then by the first_message_index (a lower number is better), and finally by
     *     * forwarded_count (a lower number is better).
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.PUT(value = "room_keys/keys/{roomId}/{sessionId}")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> storeRoomSessionData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "sessionId")
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    org.matrix.androidsdk.rest.model.keys.KeyBackupData keyBackupData);
    
    /**
     * * Store several keys for the given room, using the given backup version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.PUT(value = "room_keys/keys/{roomId}")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> storeRoomSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData roomKeysBackupData);
    
    /**
     * * Store several keys, using the given backup version.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.PUT(value = "room_keys/keys")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> storeSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    org.matrix.androidsdk.rest.model.keys.KeysBackupData keysBackupData);
    
    /**
     * * Retrieve the key for the given session in the given room from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "room_keys/keys/{roomId}/{sessionId}")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.KeyBackupData> getRoomSessionData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "sessionId")
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Retrieve all the keys for the given room from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "room_keys/keys/{roomId}")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData> getRoomSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Retrieve all the keys from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "room_keys/keys")
    public abstract retrofit2.Call<org.matrix.androidsdk.rest.model.keys.KeysBackupData> getSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Deletes keys from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.DELETE(value = "room_keys/keys/{roomId}/{sessionId}")
    public abstract retrofit2.Call<java.lang.Void> deleteRoomSessionData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "sessionId")
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Deletes keys from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.DELETE(value = "room_keys/keys/{roomId}")
    public abstract retrofit2.Call<java.lang.Void> deleteRoomSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "roomId")
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Deletes keys from the backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.DELETE(value = "room_keys/keys")
    public abstract retrofit2.Call<java.lang.Void> deleteSessionsData(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "version")
    java.lang.String version);
    
    /**
     * * Deletes a backup.
     */
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.DELETE(value = "room_keys/version/{version}")
    public abstract retrofit2.Call<java.lang.Void> deleteBackup(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "version")
    java.lang.String version);
}