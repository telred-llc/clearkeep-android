package org.matrix.androidsdk.rest.model.crypto;

import java.lang.System;

/**
 * * Sent by both devices to send their ephemeral Curve25519 public key to the other device.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0006\u001a\u00020\u0007R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationKey;", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "()V", "key", "", "transactionID", "isValid", "", "Companion", "matrix-sdk_debug"})
public final class KeyVerificationKey implements org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject {
    
    /**
     * * the ID of the transaction that the message is part of
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "transaction_id")
    public java.lang.String transactionID;
    
    /**
     * * The device’s ephemeral public key, as an unpadded base64 string
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String key;
    public static final org.matrix.androidsdk.rest.model.crypto.KeyVerificationKey.Companion Companion = null;
    
    public final boolean isValid() {
        return false;
    }
    
    public KeyVerificationKey() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006\u00a8\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationKey$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationKey;", "tid", "", "key", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationKey create(@org.jetbrains.annotations.NotNull()
        java.lang.String tid, @org.jetbrains.annotations.NotNull()
        java.lang.String key) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}