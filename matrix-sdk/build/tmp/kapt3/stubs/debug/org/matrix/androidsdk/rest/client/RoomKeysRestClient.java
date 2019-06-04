package org.matrix.androidsdk.rest.client;

import java.lang.System;

/**
 * * Class used to make requests to the RoomKeys API.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J4\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ$\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ,\u0010\u0014\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\u001c\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001a0\u000fJ\u001c\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000fJ\u001c\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ,\u0010\u001e\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ$\u0010\u001f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\u001c\u0010 \u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00130\u000fJ\u0014\u0010!\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\"0\u000fJ\u001c\u0010#\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\"0\u000fJ,\u0010$\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fJ$\u0010%\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00160\u000fJ$\u0010&\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\'\u001a\u00020(2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000f\u00a8\u0006)"}, d2 = {"Lorg/matrix/androidsdk/rest/client/RoomKeysRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/rest/api/RoomKeysApi;", "hsConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "(Lorg/matrix/androidsdk/HomeServerConnectionConfig;)V", "backupKey", "", "roomId", "", "sessionId", "version", "keyBackupData", "Lorg/matrix/androidsdk/rest/model/keys/KeyBackupData;", "callback", "Lorg/matrix/androidsdk/rest/callback/ApiCallback;", "Lorg/matrix/androidsdk/rest/model/keys/BackupKeysResult;", "backupKeys", "keysBackupData", "Lorg/matrix/androidsdk/rest/model/keys/KeysBackupData;", "backupRoomKeys", "roomKeysBackupData", "Lorg/matrix/androidsdk/rest/model/keys/RoomKeysBackupData;", "createKeysBackupVersion", "createKeysBackupVersionBody", "Lorg/matrix/androidsdk/rest/model/keys/CreateKeysBackupVersionBody;", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersion;", "deleteBackup", "Ljava/lang/Void;", "deleteKeys", "deleteRoomKey", "deleteRoomKeys", "getKeys", "getKeysBackupLastVersion", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersionResult;", "getKeysBackupVersion", "getRoomKey", "getRoomKeys", "updateKeysBackupVersion", "updateKeysBackupVersionBody", "Lorg/matrix/androidsdk/rest/model/keys/UpdateKeysBackupVersionBody;", "matrix-sdk_debug"})
public final class RoomKeysRestClient extends org.matrix.androidsdk.RestClient<org.matrix.androidsdk.rest.api.RoomKeysApi> {
    
    /**
     * * Get the key backup last version
     *     * If not supported by the server, an error is returned: {"errcode":"M_NOT_FOUND","error":"No backup found"}
     *     *
     *     * @param callback the callback
     */
    public final void getKeysBackupLastVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> callback) {
    }
    
    /**
     * * Get a key backup specific version
     *     * If not supported by the server, an error is returned: {"errcode":"M_NOT_FOUND","error":"No backup found"}
     *     *
     *     * @param version  version
     *     * @param callback the callback
     */
    public final void getKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> callback) {
    }
    
    /**
     * * Create a keys backup version
     *     *
     *     * @param createKeysBackupVersionBody the body
     *     * @param callback                    the callback
     */
    public final void createKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.CreateKeysBackupVersionBody createKeysBackupVersionBody, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersion> callback) {
    }
    
    /**
     * * Update a keys backup version
     *     *
     *     * @param version                     version
     *     * @param updateKeysBackupVersionBody the body
     *     * @param callback                    the callback
     */
    public final void updateKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.UpdateKeysBackupVersionBody updateKeysBackupVersionBody, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Send room session data for the given room, session, and version
     *     *
     *     * @param roomId        the room id
     *     * @param sessionId     the session id
     *     * @param version       the version of the backup
     *     * @param keyBackupData the data to send
     *     * @param callback      the callback
     */
    public final void backupKey(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeyBackupData keyBackupData, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * Send room session data for the given room, and version
     *     *
     *     * @param roomId             the room id
     *     * @param version            the version of the backup
     *     * @param roomKeysBackupData the data to send
     *     * @param callback           the callback
     */
    public final void backupRoomKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData roomKeysBackupData, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * Send room session data
     *     *
     *     * @param version        the version of the backup
     *     * @param keysBackupData the data to send
     *     * @param callback       the callback
     */
    public final void backupKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysBackupData keysBackupData, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * Retrieve the key for the given session in the given room from the backup.
     *     *
     *     * @param roomId    the room id
     *     * @param sessionId the session id
     *     * @param version   the version of the backup, or empty String to retrieve the last version
     *     * @param callback  the callback
     */
    public final void getRoomKey(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeyBackupData> callback) {
    }
    
    /**
     * * Retrieve all the keys for the given room from the backup.
     *     *
     *     * @param roomId   the room id
     *     * @param version  the version of the backup, or empty String to retrieve the last version
     *     * @param callback the callback
     */
    public final void getRoomKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.RoomKeysBackupData> callback) {
    }
    
    /**
     * * Retrieve the complete sessions data for the given backup version
     *     *
     *     * @param version  the version of the backup, or empty String to retrieve the last version
     *     * @param callback the callback
     */
    public final void getKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysBackupData> callback) {
    }
    
    /**
     * * @param roomId
     *     * @param sessionId
     *     * @param version
     *     * @param callback
     */
    public final void deleteRoomKey(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * @param roomId
     *     * @param version
     *     * @param callback
     */
    public final void deleteRoomKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * @param version
     *     * @param callback
     */
    public final void deleteKeys(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.BackupKeysResult> callback) {
    }
    
    /**
     * * @param version
     *     * @param callback
     */
    public final void deleteBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    public RoomKeysRestClient(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.HomeServerConnectionConfig hsConfig) {
        super(null, null, null);
    }
}