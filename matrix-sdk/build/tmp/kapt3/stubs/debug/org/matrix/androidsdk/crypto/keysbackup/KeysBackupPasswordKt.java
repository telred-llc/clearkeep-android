package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00012\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0003\u001a\u001a\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\u001a\b\u0010\r\u001a\u00020\u0006H\u0002\u001a,\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"DEFAULT_ITERATION", "", "SALT_LENGTH", "deriveKey", "", "password", "", "salt", "iterations", "progressListener", "Lorg/matrix/androidsdk/listeners/ProgressListener;", "generatePrivateKeyWithPassword", "Lorg/matrix/androidsdk/crypto/keysbackup/GeneratePrivateKeyResult;", "generateSalt", "retrievePrivateKeyWithPassword", "matrix-sdk_debug"})
public final class KeysBackupPasswordKt {
    private static final int SALT_LENGTH = 32;
    private static final int DEFAULT_ITERATION = 500000;
    
    /**
     * * Compute a private key from a password.
     * *
     * * @param password the password to use.
     * *
     * * @return a {privateKey, salt, iterations} tuple.
     */
    @org.jetbrains.annotations.NotNull()
    @android.support.annotation.WorkerThread()
    public static final org.matrix.androidsdk.crypto.keysbackup.GeneratePrivateKeyResult generatePrivateKeyWithPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.ProgressListener progressListener) {
        return null;
    }
    
    /**
     * * Retrieve a private key from {password, salt, iterations}
     * *
     * * @param password the password used to generated the private key.
     * * @param salt the salt.
     * * @param iterations number of key derivations.
     * * @param progressListener the progress listener
     * *
     * * @return a private key.
     */
    @org.jetbrains.annotations.NotNull()
    @android.support.annotation.WorkerThread()
    public static final byte[] retrievePrivateKeyWithPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String salt, int iterations, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.listeners.ProgressListener progressListener) {
        return null;
    }
    
    /**
     * * Compute a private key by deriving a password and a salt strings.
     * *
     * * @param password the password.
     * * @param salt the salt.
     * * @param iterations number of derivations.
     * * @param progressListener a listener to follow progress.
     * *
     * * @return a private key.
     */
    @android.support.annotation.WorkerThread()
    private static final byte[] deriveKey(java.lang.String password, java.lang.String salt, int iterations, org.matrix.androidsdk.listeners.ProgressListener progressListener) {
        return null;
    }
    
    /**
     * * Generate a 32 chars salt
     */
    private static final java.lang.String generateSalt() {
        return null;
    }
}