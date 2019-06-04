package org.matrix.androidsdk.rest.model.keys;

import java.lang.System;

/**
 * * <pre>
 * *     Example:
 * *
 * *     {
 * *         "algorithm": "m.megolm_backup.v1.curve25519-aes-sha2",
 * *         "auth_data": {
 * *             "public_key": "abcdefg",
 * *             "signatures": {
 * *                 "something": {
 * *                     "ed25519:something": "hijklmnop"
 * *                 }
 * *             }
 * *         }
 * *     }
 * * </pre>
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\u0004\u0018\u00010\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/rest/model/keys/KeysAlgorithmAndData;", "", "()V", "algorithm", "", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "authData", "Lcom/google/gson/JsonElement;", "getAuthData", "()Lcom/google/gson/JsonElement;", "setAuthData", "(Lcom/google/gson/JsonElement;)V", "getAuthDataAsMegolmBackupAuthData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "kotlin.jvm.PlatformType", "matrix-sdk_debug"})
public class KeysAlgorithmAndData {
    
    /**
     * * The algorithm used for storing backups. Currently, only "m.megolm_backup.v1.curve25519-aes-sha2" is defined
     */
    @org.jetbrains.annotations.Nullable()
    private java.lang.String algorithm;
    
    /**
     * * algorithm-dependent data, for "m.megolm_backup.v1.curve25519-aes-sha2" see [org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData]
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "auth_data")
    private com.google.gson.JsonElement authData;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAlgorithm() {
        return null;
    }
    
    public final void setAlgorithm(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.gson.JsonElement getAuthData() {
        return null;
    }
    
    public final void setAuthData(@org.jetbrains.annotations.Nullable()
    com.google.gson.JsonElement p0) {
    }
    
    /**
     * * Facility method to convert authData to a MegolmBackupAuthData object
     */
    public final org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData getAuthDataAsMegolmBackupAuthData() {
        return null;
    }
    
    public KeysAlgorithmAndData() {
        super();
    }
}