package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0002\u0019\u001aB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rJ\u000e\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\n\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001b"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager;", "", "crypto", "Lorg/matrix/androidsdk/crypto/MXCrypto;", "(Lorg/matrix/androidsdk/crypto/MXCrypto;)V", "getCrypto", "()Lorg/matrix/androidsdk/crypto/MXCrypto;", "isEnabled", "", "()Z", "isStucked", "mListeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "newState", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "state", "getState", "()Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "setState", "(Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;)V", "addListener", "", "listener", "removeListener", "KeysBackupState", "KeysBackupStateListener", "matrix-sdk_debug"})
public final class KeysBackupStateManager {
    private final java.util.ArrayList<org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener> mListeners = null;
    @org.jetbrains.annotations.NotNull()
    private org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState state;
    @org.jetbrains.annotations.NotNull()
    private final org.matrix.androidsdk.crypto.MXCrypto crypto = null;
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState getState() {
        return null;
    }
    
    public final void setState(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState newState) {
    }
    
    public final boolean isEnabled() {
        return false;
    }
    
    public final boolean isStucked() {
        return false;
    }
    
    public final void addListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener listener) {
    }
    
    public final void removeListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener listener) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.MXCrypto getCrypto() {
        return null;
    }
    
    public KeysBackupStateManager(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.MXCrypto crypto) {
        super();
    }
    
    /**
     * * E2e keys backup states.
     *     *
     *     * <pre>
     *     *                               |
     *     *                               V        deleteKeyBackupVersion (on current backup)
     *     *  +---------------------->  UNKNOWN  <-------------
     *     *  |                            |
     *     *  |                            | checkAndStartKeysBackup (at startup or on new verified device or a new detected backup)
     *     *  |                            V
     *     *  |                     CHECKING BACKUP
     *     *  |                            |
     *     *  |    Network error           |
     *     *  +<----------+----------------+-------> DISABLED <----------------------+
     *     *  |           |                |            |                            |
     *     *  |           |                |            | createKeysBackupVersion    |
     *     *  |           V                |            V                            |
     *     *  +<---  WRONG VERSION         |         ENABLING                        |
     *     *      |       ^                |            |                            |
     *     *      |       |                V       ok   |     error                  |
     *     *      |       |     +------> READY <--------+----------------------------+
     *     *      V       |     |          |
     *     * NOT TRUSTED  |     |          | on new key
     *     *              |     |          V
     *     *              |     |     WILL BACK UP (waiting a random duration)
     *     *              |     |          |
     *     *              |     |          |
     *     *              |     | ok       V
     *     *              |     +----- BACKING UP
     *     *              |                |
     *     *              |      Error     |
     *     *              +<---------------+
     *     * </pre>
     */
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "", "(Ljava/lang/String;I)V", "Unknown", "CheckingBackUpOnHomeserver", "WrongBackUpVersion", "Disabled", "NotTrusted", "Enabling", "ReadyToBackUp", "WillBackUp", "BackingUp", "matrix-sdk_debug"})
    public static enum KeysBackupState {
        /*public static final*/ Unknown /* = new Unknown() */,
        /*public static final*/ CheckingBackUpOnHomeserver /* = new CheckingBackUpOnHomeserver() */,
        /*public static final*/ WrongBackUpVersion /* = new WrongBackUpVersion() */,
        /*public static final*/ Disabled /* = new Disabled() */,
        /*public static final*/ NotTrusted /* = new NotTrusted() */,
        /*public static final*/ Enabling /* = new Enabling() */,
        /*public static final*/ ReadyToBackUp /* = new ReadyToBackUp() */,
        /*public static final*/ WillBackUp /* = new WillBackUp() */,
        /*public static final*/ BackingUp /* = new BackingUp() */;
        
        KeysBackupState() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "", "onStateChange", "", "newState", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "matrix-sdk_debug"})
    public static abstract interface KeysBackupStateListener {
        
        public abstract void onStateChange(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState newState);
    }
}