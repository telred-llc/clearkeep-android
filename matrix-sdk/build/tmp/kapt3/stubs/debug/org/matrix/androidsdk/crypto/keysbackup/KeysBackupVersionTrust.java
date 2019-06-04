package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

/**
 * * Data model for response to [KeysBackup.getKeysBackupTrust()].
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrust;", "", "()V", "signatures", "", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrustSignature;", "getSignatures", "()Ljava/util/List;", "setSignatures", "(Ljava/util/List;)V", "usable", "", "getUsable", "()Z", "setUsable", "(Z)V", "matrix-sdk_debug"})
public final class KeysBackupVersionTrust {
    
    /**
     * * Flag to indicate if the backup is trusted.
     *     * true if there is a signature that is valid & from a trusted device.
     */
    private boolean usable;
    
    /**
     * * Signatures found in the backup version.
     */
    @org.jetbrains.annotations.NotNull()
    private java.util.List<org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrustSignature> signatures;
    
    public final boolean getUsable() {
        return false;
    }
    
    public final void setUsable(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrustSignature> getSignatures() {
        return null;
    }
    
    public final void setSignatures(@org.jetbrains.annotations.NotNull()
    java.util.List<org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrustSignature> p0) {
    }
    
    public KeysBackupVersionTrust() {
        super();
    }
}