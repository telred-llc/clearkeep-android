package org.matrix.androidsdk.rest.model.crypto;

import java.lang.System;

/**
 * * Sent by both devices to send the MAC of their device key to the other device.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "()V", "keys", "", "mac", "", "transactionID", "isValid", "", "Companion", "matrix-sdk_debug"})
public final class KeyVerificationMac implements org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject {
    
    /**
     * * the ID of the transaction that the message is part of
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "transaction_id")
    public java.lang.String transactionID;
    
    /**
     * * A map of key ID to the MAC of the key, as an unpadded base64 string, calculated using the MAC key
     */
    @org.jetbrains.annotations.Nullable()
    public java.util.Map<java.lang.String, java.lang.String> mac;
    
    /**
     * *  The MAC of the comma-separated, sorted list of key IDs given in the mac property,
     *     *  as an unpadded base64 string, calculated using the MAC key.
     *     *  For example, if the mac property gives MACs for the keys ed25519:ABCDEFG and ed25519:HIJKLMN, then this property will
     *     *  give the MAC of the string “ed25519:ABCDEFG,ed25519:HIJKLMN”.
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String keys;
    public static final org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac.Companion Companion = null;
    
    public final boolean isValid() {
        return false;
    }
    
    public KeyVerificationMac() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\t\u001a\u00020\u0006\u00a8\u0006\n"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;", "tid", "", "mac", "", "keys", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac create(@org.jetbrains.annotations.NotNull()
        java.lang.String tid, @org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.String> mac, @org.jetbrains.annotations.NotNull()
        java.lang.String keys) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}