package org.matrix.androidsdk.crypto.util;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a\u0012\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\n\u001a\u0004\u0018\u00010\u0006\u001a\u0010\u0010\u000b\u001a\u00020\f2\b\u0010\n\u001a\u0004\u0018\u00010\u0006\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"CHAR_0", "", "CHAR_1", "RECOVERY_KEY_LENGTH", "", "computeRecoveryKey", "", "curve25519Key", "", "extractCurveKeyFromRecoveryKey", "recoveryKey", "isValidRecoveryKey", "", "matrix-sdk_debug"})
public final class RecoveryKeyKt {
    
    /**
     * * See https://github.com/uhoreg/matrix-doc/blob/e2e_backup/proposals/1219-storing-megolm-keys-serverside.md
     */
    private static final byte CHAR_0 = (byte)-117;
    private static final byte CHAR_1 = (byte)1;
    private static final int RECOVERY_KEY_LENGTH = 35;
    
    /**
     * * Tell if the format of the recovery key is correct
     * *
     * * @param recoveryKey
     * * @return true if the format of the recovery key is correct
     */
    public static final boolean isValidRecoveryKey(@org.jetbrains.annotations.Nullable()
    java.lang.String recoveryKey) {
        return false;
    }
    
    /**
     * * Compute recovery key from curve25519 key
     * *
     * * @param curve25519Key
     * * @return the recovery key
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String computeRecoveryKey(@org.jetbrains.annotations.NotNull()
    byte[] curve25519Key) {
        return null;
    }
    
    /**
     * * Please call [.isValidRecoveryKey] and ensure it returns true before calling this method
     * *
     * * @param recoveryKey the recovery key
     * * @return curveKey, or null in case of error
     */
    @org.jetbrains.annotations.Nullable()
    public static final byte[] extractCurveKeyFromRecoveryKey(@org.jetbrains.annotations.Nullable()
    java.lang.String recoveryKey) {
        return null;
    }
}