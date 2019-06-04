package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

/**
 * * Data model for [org.matrix.androidsdk.rest.model.keys.KeysAlgorithmAndData.authData] in case
 * * of [org.matrix.androidsdk.crypto.MXCRYPTO_ALGORITHM_MEGOLM_BACKUP].
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\"\b\u0002\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000bJ#\u0010\u001c\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bH\u00c6\u0003JT\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\"\b\u0002\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\u0006H\u00d6\u0001J\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\bJ\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\"\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0010\n\u0002\u0010\u000e\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR \u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R4\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u0006%"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "", "publicKey", "", "privateKeySalt", "privateKeyIterations", "", "signatures", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/Map;)V", "getPrivateKeyIterations", "()Ljava/lang/Integer;", "setPrivateKeyIterations", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getPrivateKeySalt", "()Ljava/lang/String;", "setPrivateKeySalt", "(Ljava/lang/String;)V", "getPublicKey", "setPublicKey", "getSignatures", "()Ljava/util/Map;", "setSignatures", "(Ljava/util/Map;)V", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/Map;)Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "equals", "", "other", "hashCode", "signalableJSONDictionary", "toString", "matrix-sdk_debug"})
public final class MegolmBackupAuthData {
    
    /**
     * * The curve25519 public key used to encrypt the backups.
     */
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "public_key")
    private java.lang.String publicKey;
    
    /**
     * * In case of a backup created from a password, the salt associated with the backup
     *         * private key.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "private_key_salt")
    private java.lang.String privateKeySalt;
    
    /**
     * * In case of a backup created from a password, the number of key derivations.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "private_key_iterations")
    private java.lang.Integer privateKeyIterations;
    
    /**
     * * Signatures of the public key.
     *         * userId -> (deviceSignKeyId -> signature)
     */
    @org.jetbrains.annotations.Nullable()
    private java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> signatures;
    
    /**
     * * Same as the parent [MXJSONModel JSONDictionary] but return only
     *     * data that must be signed.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Object> signalableJSONDictionary() {
        return null;
    }
    
    /**
     * * The curve25519 public key used to encrypt the backups.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPublicKey() {
        return null;
    }
    
    /**
     * * The curve25519 public key used to encrypt the backups.
     */
    public final void setPublicKey(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    /**
     * * In case of a backup created from a password, the salt associated with the backup
     *         * private key.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPrivateKeySalt() {
        return null;
    }
    
    /**
     * * In case of a backup created from a password, the salt associated with the backup
     *         * private key.
     */
    public final void setPrivateKeySalt(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    /**
     * * In case of a backup created from a password, the number of key derivations.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPrivateKeyIterations() {
        return null;
    }
    
    /**
     * * In case of a backup created from a password, the number of key derivations.
     */
    public final void setPrivateKeyIterations(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    /**
     * * Signatures of the public key.
     *         * userId -> (deviceSignKeyId -> signature)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getSignatures() {
        return null;
    }
    
    /**
     * * Signatures of the public key.
     *         * userId -> (deviceSignKeyId -> signature)
     */
    public final void setSignatures(@org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> p0) {
    }
    
    public MegolmBackupAuthData(@org.jetbrains.annotations.NotNull()
    java.lang.String publicKey, @org.jetbrains.annotations.Nullable()
    java.lang.String privateKeySalt, @org.jetbrains.annotations.Nullable()
    java.lang.Integer privateKeyIterations, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> signatures) {
        super();
    }
    
    public MegolmBackupAuthData() {
        super();
    }
    
    /**
     * * The curve25519 public key used to encrypt the backups.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    /**
     * * In case of a backup created from a password, the salt associated with the backup
     *         * private key.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    /**
     * * In case of a backup created from a password, the number of key derivations.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }
    
    /**
     * * Signatures of the public key.
     *         * userId -> (deviceSignKeyId -> signature)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> component4() {
        return null;
    }
    
    /**
     * * Data model for [org.matrix.androidsdk.rest.model.keys.KeysAlgorithmAndData.authData] in case
     * * of [org.matrix.androidsdk.crypto.MXCRYPTO_ALGORITHM_MEGOLM_BACKUP].
     */
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String publicKey, @org.jetbrains.annotations.Nullable()
    java.lang.String privateKeySalt, @org.jetbrains.annotations.Nullable()
    java.lang.Integer privateKeyIterations, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>> signatures) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object p0) {
        return false;
    }
}