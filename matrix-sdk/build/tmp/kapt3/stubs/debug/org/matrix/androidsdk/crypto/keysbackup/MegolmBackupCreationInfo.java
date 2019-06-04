package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

/**
 * * Data retrieved from Olm library. algorithm and authData will be send to the homeserver, and recoveryKey will be displayed to the user
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\b\u00a8\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupCreationInfo;", "", "()V", "algorithm", "", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "authData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "getAuthData", "()Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "setAuthData", "(Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;)V", "recoveryKey", "getRecoveryKey", "setRecoveryKey", "matrix-sdk_debug"})
public final class MegolmBackupCreationInfo {
    
    /**
     * * The algorithm used for storing backups [org.matrix.androidsdk.crypto.MXCRYPTO_ALGORITHM_MEGOLM_BACKUP].
     */
    @org.jetbrains.annotations.NotNull()
    private java.lang.String algorithm;
    
    /**
     * * Authentication data.
     */
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData authData;
    
    /**
     * * The Base58 recovery key.
     */
    @org.jetbrains.annotations.NotNull()
    private java.lang.String recoveryKey;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAlgorithm() {
        return null;
    }
    
    public final void setAlgorithm(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData getAuthData() {
        return null;
    }
    
    public final void setAuthData(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRecoveryKey() {
        return null;
    }
    
    public final void setRecoveryKey(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public MegolmBackupCreationInfo() {
        super();
    }
}