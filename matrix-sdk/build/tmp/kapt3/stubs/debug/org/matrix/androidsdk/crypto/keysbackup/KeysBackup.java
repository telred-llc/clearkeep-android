package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

/**
 * * A KeysBackup class instance manage incremental backup of e2e keys (megolm keys)
 * * to the user's homeserver.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00d2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 o2\u00020\u0001:\u0001oB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u0015J\"\u0010)\u001a\u00020\'2\b\u0010*\u001a\u0004\u0018\u00010+2\u0010\u0010,\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bJ\b\u0010-\u001a\u00020\'H\u0003J\u0006\u0010.\u001a\u00020\u000fJ\u0006\u0010/\u001a\u00020\'J\u0012\u00100\u001a\u00020\'2\b\u00101\u001a\u0004\u0018\u00010\u0019H\u0002J\u001c\u00102\u001a\u00020\'2\u0006\u00103\u001a\u0002042\f\u0010,\u001a\b\u0012\u0004\u0012\u0002050\bJ*\u00106\u001a\u0004\u0018\u0001072\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020\u000b2\u0006\u0010;\u001a\u00020\u000b2\u0006\u0010<\u001a\u00020=H\u0007J\u001e\u0010>\u001a\u00020\'2\u0006\u0010?\u001a\u00020\u000b2\u000e\u0010,\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bJ\u0010\u0010@\u001a\u00020\'2\u0006\u0010A\u001a\u00020\u0019H\u0002J\u0010\u0010B\u001a\u0002092\u0006\u0010\u0004\u001a\u00020CH\u0007J\u0014\u0010D\u001a\u00020\'2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000f0\bJ\u000e\u0010E\u001a\u00020\'2\u0006\u0010*\u001a\u00020+J\u0016\u0010F\u001a\u00020\'2\u000e\u0010,\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\bJ2\u0010G\u001a\u00020\'2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\b\u0010;\u001a\u0004\u0018\u00010\u000b2\u0006\u0010?\u001a\u00020\u000b2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020H0\bH\u0002J\u001c\u0010I\u001a\u00020\'2\u0006\u0010J\u001a\u00020\u00192\f\u0010,\u001a\b\u0012\u0004\u0012\u00020L0KJ\u0010\u0010M\u001a\u00020L2\u0006\u0010J\u001a\u00020\u0019H\u0003J\u0012\u0010N\u001a\u0004\u0018\u00010O2\u0006\u0010P\u001a\u00020\u0019H\u0002J\u0006\u0010Q\u001a\u00020RJ\u0006\u0010S\u001a\u00020RJ\u001e\u0010T\u001a\u00020\'2\u0006\u0010?\u001a\u00020\u000b2\u000e\u0010,\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\bJ\u0018\u0010U\u001a\u00020\u000f2\u0006\u0010V\u001a\u00020\u000b2\u0006\u0010P\u001a\u00020\u0019H\u0003J\u0006\u0010W\u001a\u00020\'J!\u0010X\u001a\u00020\'2\b\u0010Y\u001a\u0004\u0018\u00010R2\b\u0010Z\u001a\u0004\u0018\u00010\u000bH\u0002\u00a2\u0006\u0002\u0010[J\u0012\u0010\\\u001a\u0004\u0018\u00010=2\u0006\u0010V\u001a\u00020\u000bH\u0007J\u0012\u0010]\u001a\u0004\u0018\u00010\u000b2\u0006\u0010V\u001a\u00020\u000bH\u0003J(\u0010^\u001a\u00020\'2\b\u0010_\u001a\u0004\u0018\u00010\u000b2\b\u0010*\u001a\u0004\u0018\u00010+2\f\u0010,\u001a\b\u0012\u0004\u0012\u0002040`J$\u0010a\u001a\u0004\u0018\u00010\u000b2\u0006\u0010_\u001a\u00020\u000b2\u0006\u0010P\u001a\u00020\u00192\b\u0010*\u001a\u0004\u0018\u00010+H\u0003J\u000e\u0010b\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u0015J\b\u0010c\u001a\u00020\'H\u0002J\b\u0010d\u001a\u00020\'H\u0002JB\u0010e\u001a\u00020\'2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010_\u001a\u00020\u000b2\b\u0010;\u001a\u0004\u0018\u00010\u000b2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\b\u0010f\u001a\u0004\u0018\u00010g2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020h0\bJB\u0010i\u001a\u00020\'2\u0006\u0010A\u001a\u00020\u00192\u0006\u0010V\u001a\u00020\u000b2\b\u0010;\u001a\u0004\u0018\u00010\u000b2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\b\u0010f\u001a\u0004\u0018\u00010g2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020h0\bJ\b\u0010j\u001a\u00020\u000bH\u0016J$\u0010k\u001a\u00020\'2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010l\u001a\u00020\u000f2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\t0\bJ$\u0010m\u001a\u00020\'2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010_\u001a\u00020\u000b2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\t0\bJ$\u0010n\u001a\u00020\'2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010V\u001a\u00020\u000b2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\t0\bR\u0018\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u001a\u001a\u0004\u0018\u00010\u00192\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001f\u001a\n !*\u0004\u0018\u00010 0 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\"\u001a\u00020#8F\u00a2\u0006\u0006\u001a\u0004\b$\u0010%\u00a8\u0006p"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup;", "", "mCrypto", "Lorg/matrix/androidsdk/crypto/MXCrypto;", "session", "Lorg/matrix/androidsdk/MXSession;", "(Lorg/matrix/androidsdk/crypto/MXCrypto;Lorg/matrix/androidsdk/MXSession;)V", "backupAllGroupSessionsCallback", "Lorg/matrix/androidsdk/rest/callback/ApiCallback;", "Ljava/lang/Void;", "currentBackupVersion", "", "getCurrentBackupVersion", "()Ljava/lang/String;", "isEnabled", "", "()Z", "isStucked", "mBackupKey", "Lorg/matrix/olm/OlmPkEncryption;", "mKeysBackupStateListener", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "mKeysBackupStateManager", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager;", "<set-?>", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersionResult;", "mKeysBackupVersion", "getMKeysBackupVersion", "()Lorg/matrix/androidsdk/rest/model/keys/KeysVersionResult;", "mRandom", "Ljava/util/Random;", "mRoomKeysRestClient", "Lorg/matrix/androidsdk/rest/client/RoomKeysRestClient;", "kotlin.jvm.PlatformType", "state", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "getState", "()Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "addListener", "", "listener", "backupAllGroupSessions", "progressListener", "Lorg/matrix/androidsdk/listeners/ProgressListener;", "callback", "backupKeys", "canRestoreKeys", "checkAndStartKeysBackup", "checkAndStartWithKeysBackupVersion", "keyBackupVersion", "createKeysBackupVersion", "keysBackupCreationInfo", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupCreationInfo;", "Lorg/matrix/androidsdk/rest/model/keys/KeysVersion;", "decryptKeyBackupData", "Lorg/matrix/androidsdk/crypto/MegolmSessionData;", "keyBackupData", "Lorg/matrix/androidsdk/rest/model/keys/KeyBackupData;", "sessionId", "roomId", "decryption", "Lorg/matrix/olm/OlmPkDecryption;", "deleteBackup", "version", "enableKeysBackup", "keysVersionResult", "encryptGroupSession", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "forceUsingLastVersion", "getBackupProgress", "getCurrentVersion", "getKeys", "Lorg/matrix/androidsdk/rest/model/keys/KeysBackupData;", "getKeysBackupTrust", "keysBackupVersion", "Lorg/matrix/androidsdk/rest/callback/SuccessCallback;", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrust;", "getKeysBackupTrustBg", "getMegolmBackupAuthData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "keysBackupData", "getTotalNumbersOfBackedUpKeys", "", "getTotalNumbersOfKeys", "getVersion", "isValidRecoveryKeyForKeysBackupVersion", "recoveryKey", "maybeBackupKeys", "onServerDataRetrieved", "count", "hash", "(Ljava/lang/Integer;Ljava/lang/String;)V", "pkDecryptionFromRecoveryKey", "pkPublicKeyFromRecoveryKey", "prepareKeysBackupVersion", "password", "Lorg/matrix/androidsdk/rest/callback/SuccessErrorCallback;", "recoveryKeyFromPassword", "removeListener", "resetBackupAllGroupSessionsListeners", "resetKeysBackupData", "restoreKeyBackupWithPassword", "stepProgressListener", "Lorg/matrix/androidsdk/listeners/StepProgressListener;", "Lorg/matrix/androidsdk/crypto/data/ImportRoomKeysResult;", "restoreKeysWithRecoveryKey", "toString", "trustKeysBackupVersion", "trust", "trustKeysBackupVersionWithPassphrase", "trustKeysBackupVersionWithRecoveryKey", "Companion", "matrix-sdk_debug"})
public final class KeysBackup {
    private final org.matrix.androidsdk.rest.client.RoomKeysRestClient mRoomKeysRestClient = null;
    private final org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager mKeysBackupStateManager = null;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.rest.model.keys.KeysVersionResult mKeysBackupVersion;
    private org.matrix.olm.OlmPkEncryption mBackupKey;
    private final java.util.Random mRandom = null;
    private org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> backupAllGroupSessionsCallback;
    private org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener mKeysBackupStateListener;
    private final org.matrix.androidsdk.crypto.MXCrypto mCrypto = null;
    private static final java.lang.String LOG_TAG = null;
    private static final int KEY_BACKUP_WAITING_TIME_TO_SEND_KEY_BACKUP_MILLIS = 10000;
    private static final int KEY_BACKUP_SEND_KEYS_MAX_COUNT = 100;
    public static final org.matrix.androidsdk.crypto.keysbackup.KeysBackup.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.rest.model.keys.KeysVersionResult getMKeysBackupVersion() {
        return null;
    }
    
    public final boolean isEnabled() {
        return false;
    }
    
    public final boolean isStucked() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState getState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCurrentBackupVersion() {
        return null;
    }
    
    public final void addListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener listener) {
    }
    
    public final void removeListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener listener) {
    }
    
    /**
     * * Set up the data required to create a new backup version.
     *     * The backup version will not be created and enabled until [createKeysBackupVersion]
     *     * is called.
     *     * The returned [MegolmBackupCreationInfo] object has a `recoveryKey` member with
     *     * the user-facing recovery key string.
     *     *
     *     * @param password an optional passphrase string that can be entered by the user
     *     * when restoring the backup as an alternative to entering the recovery key.
     *     * @param progressListener a progress listener, as generating private key from password may take a while
     *     * @param callback Asynchronous callback
     */
    public final void prepareKeysBackupVersion(@org.jetbrains.annotations.Nullable()
    java.lang.String password, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.ProgressListener progressListener, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.SuccessErrorCallback<org.matrix.androidsdk.crypto.keysbackup.MegolmBackupCreationInfo> callback) {
    }
    
    /**
     * * Create a new keys backup version and enable it, using the information return from [prepareKeysBackupVersion].
     *     *
     *     * @param keysBackupCreationInfo the info object from [prepareKeysBackupVersion].
     *     * @param callback               Asynchronous callback
     */
    public final void createKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.MegolmBackupCreationInfo keysBackupCreationInfo, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersion> callback) {
    }
    
    /**
     * * Delete a keys backup version. It will delete all backed up keys on the server, and the backup itself.
     *     * If we are backing up to this version. Backup will be stopped.
     *     *
     *     * @param version  the backup version to delete.
     *     * @param callback Asynchronous callback
     */
    public final void deleteBackup(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Ask if the backup on the server contains keys that we may do not have locally.
     *     * This should be called when entering in the state READY_TO_BACKUP
     */
    public final boolean canRestoreKeys() {
        return false;
    }
    
    /**
     * * Facility method to get the total number of locally stored keys
     */
    public final int getTotalNumbersOfKeys() {
        return 0;
    }
    
    /**
     * * Facility method to get the number of backed up keys
     */
    public final int getTotalNumbersOfBackedUpKeys() {
        return 0;
    }
    
    /**
     * * Start to back up keys immediately.
     *     *
     *     * @param progressListener the callback to follow the progress
     *     * @param callback the main callback
     */
    public final void backupAllGroupSessions(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.ProgressListener progressListener, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Check trust on a key backup version.
     *     *
     *     * @param keysBackupVersion the backup version to check.
     *     * @param callback block called when the operations completes.
     */
    public final void getKeysBackupTrust(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.SuccessCallback<org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrust> callback) {
    }
    
    /**
     * * Check trust on a key backup version.
     *     * This has to be called on background thread.
     *     *
     *     * @param keysBackupVersion the backup version to check.
     *     * @return a KeysBackupVersionTrust object
     */
    @android.support.annotation.WorkerThread()
    private final org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrust getKeysBackupTrustBg(org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion) {
        return null;
    }
    
    /**
     * * Set trust on a keys backup version.
     *     * It adds (or removes) the signature of the current device to the authentication part of the keys backup version.
     *     *
     *     * @param keysBackupVersion the backup version to check.
     *     * @param trust the trust to set to the keys backup.
     *     * @param callback block called when the operations completes.
     */
    public final void trustKeysBackupVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion, boolean trust, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Set trust on a keys backup version.
     *     *
     *     * @param keysBackupVersion the backup version to check.
     *     * @param recoveryKey the recovery key to challenge with the key backup public key.
     *     * @param callback block called when the operations completes.
     */
    public final void trustKeysBackupVersionWithRecoveryKey(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion, @org.jetbrains.annotations.NotNull()
    java.lang.String recoveryKey, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Set trust on a keys backup version.
     *     *
     *     * @param keysBackupVersion the backup version to check.
     *     * @param password the pass phrase to challenge with the keyBackupVersion public key.
     *     * @param callback block called when the operations completes.
     */
    public final void trustKeysBackupVersionWithPassphrase(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Void> callback) {
    }
    
    /**
     * * Get public key from a Recovery key
     *     *
     *     * @param recoveryKey the recovery key
     *     * @return the corresponding public key, from Olm
     */
    @android.support.annotation.WorkerThread()
    private final java.lang.String pkPublicKeyFromRecoveryKey(java.lang.String recoveryKey) {
        return null;
    }
    
    private final void resetBackupAllGroupSessionsListeners() {
    }
    
    /**
     * * Return the current progress of the backup
     */
    public final void getBackupProgress(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.listeners.ProgressListener progressListener) {
    }
    
    /**
     * * Restore a backup with a recovery key from a given backup version stored on the homeserver.
     *     *
     *     * @param keysVersionResult    the backup version to restore from.
     *     * @param recoveryKey          the recovery key to decrypt the retrieved backup.
     *     * @param roomId               the id of the room to get backup data from.
     *     * @param sessionId            the id of the session to restore.
     *     * @param stepProgressListener the step progress listener
     *     * @param callback             Callback. It provides the number of found keys and the number of successfully imported keys.
     */
    public final void restoreKeysWithRecoveryKey(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysVersionResult, @org.jetbrains.annotations.NotNull()
    java.lang.String recoveryKey, @org.jetbrains.annotations.Nullable()
    java.lang.String roomId, @org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.StepProgressListener stepProgressListener, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.crypto.data.ImportRoomKeysResult> callback) {
    }
    
    /**
     * * Restore a backup with a password from a given backup version stored on the homeserver.
     *     *
     *     * @param keysBackupVersion the backup version to restore from.
     *     * @param password the password to decrypt the retrieved backup.
     *     * @param roomId the id of the room to get backup data from.
     *     * @param sessionId the id of the session to restore.
     *     * @param stepProgressListener the step progress listener
     *     * @param callback Callback. It provides the number of found keys and the number of successfully imported keys.
     */
    public final void restoreKeyBackupWithPassword(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupVersion, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.Nullable()
    java.lang.String roomId, @org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.StepProgressListener stepProgressListener, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.crypto.data.ImportRoomKeysResult> callback) {
    }
    
    /**
     * * Same method as [RoomKeysRestClient.getRoomKey] except that it accepts nullable
     *     * parameters and always returns a KeysBackupData object through the Callback
     */
    private final void getKeys(java.lang.String sessionId, java.lang.String roomId, java.lang.String version, org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysBackupData> callback) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @android.support.annotation.WorkerThread()
    @android.support.annotation.VisibleForTesting()
    public final org.matrix.olm.OlmPkDecryption pkDecryptionFromRecoveryKey(@org.jetbrains.annotations.NotNull()
    java.lang.String recoveryKey) {
        return null;
    }
    
    /**
     * * Do a backup if there are new keys, with a delay
     */
    public final void maybeBackupKeys() {
    }
    
    /**
     * * Get information about a backup version defined on the homeserver.
     *     *
     *     * It can be different than mKeysBackupVersion.
     *     * @param version the backup version
     *     * @param callback
     */
    public final void getVersion(@org.jetbrains.annotations.NotNull()
    java.lang.String version, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> callback) {
    }
    
    /**
     * * Retrieve the current version of the backup from the home server
     *     *
     *     * It can be different than mKeysBackupVersion.
     *     * @param callback onSuccess(null) will be called if there is no backup on the server
     */
    public final void getCurrentVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<org.matrix.androidsdk.rest.model.keys.KeysVersionResult> callback) {
    }
    
    /**
     * * This method fetches the last backup version on the server, then compare to the currently backup version use.
     *     * If versions are not the same, the current backup is deleted (on server or locally), then the backup may be started again, using the last version.
     *     *
     *     * @param callback true if backup is already using the last version, and false if it is not the case
     */
    public final void forceUsingLastVersion(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.callback.ApiCallback<java.lang.Boolean> callback) {
    }
    
    /**
     * * Check the server for an active key backup.
     *     *
     *     * If one is present and has a valid signature from one of the user's verified
     *     * devices, start backing up to it.
     */
    public final void checkAndStartKeysBackup() {
    }
    
    private final void checkAndStartWithKeysBackupVersion(org.matrix.androidsdk.rest.model.keys.KeysVersionResult keyBackupVersion) {
    }
    
    /**
     * * Extract MegolmBackupAuthData data from a backup version.
     *     *
     *     * @param keysBackupData the key backup data
     *     *
     *     * @return the authentication if found and valid, null in other case
     */
    private final org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData getMegolmBackupAuthData(org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupData) {
        return null;
    }
    
    /**
     * * Compute the recovery key from a password and key backup version.
     *     *
     *     * @param password the password.
     *     * @param keysBackupData the backup and its auth data.
     *     *
     *     * @return the recovery key if successful, null in other cases
     */
    @android.support.annotation.WorkerThread()
    private final java.lang.String recoveryKeyFromPassword(java.lang.String password, org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupData, org.matrix.androidsdk.listeners.ProgressListener progressListener) {
        return null;
    }
    
    /**
     * * Check if a recovery key matches key backup authentication data.
     *     *
     *     * @param recoveryKey the recovery key to challenge.
     *     * @param keysBackupData the backup and its auth data.
     *     *
     *     * @return true if successful.
     */
    @android.support.annotation.WorkerThread()
    private final boolean isValidRecoveryKeyForKeysBackupVersion(java.lang.String recoveryKey, org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysBackupData) {
        return false;
    }
    
    /**
     * * Enable backing up of keys.
     *     * This method will update the state and will start sending keys in nominal case
     *     *
     *     * @param keysVersionResult backup information object as returned by [getCurrentVersion].
     */
    private final void enableKeysBackup(org.matrix.androidsdk.rest.model.keys.KeysVersionResult keysVersionResult) {
    }
    
    /**
     * * Update the DB with data fetch from the server
     */
    private final void onServerDataRetrieved(java.lang.Integer count, java.lang.String hash) {
    }
    
    /**
     * * Reset all local key backup data.
     *     *
     *     * Note: This method does not update the state
     */
    private final void resetKeysBackupData() {
    }
    
    /**
     * * Send a chunk of keys to backup
     */
    @android.support.annotation.UiThread()
    private final void backupKeys() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @android.support.annotation.WorkerThread()
    @android.support.annotation.VisibleForTesting()
    public final org.matrix.androidsdk.rest.model.keys.KeyBackupData encryptGroupSession(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2 session) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @android.support.annotation.WorkerThread()
    @android.support.annotation.VisibleForTesting()
    public final org.matrix.androidsdk.crypto.MegolmSessionData decryptKeyBackupData(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.keys.KeyBackupData keyBackupData, @org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    org.matrix.olm.OlmPkDecryption decryption) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public KeysBackup(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.MXCrypto mCrypto, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup$Companion;", "", "()V", "KEY_BACKUP_SEND_KEYS_MAX_COUNT", "", "KEY_BACKUP_WAITING_TIME_TO_SEND_KEY_BACKUP_MILLIS", "LOG_TAG", "", "kotlin.jvm.PlatformType", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}