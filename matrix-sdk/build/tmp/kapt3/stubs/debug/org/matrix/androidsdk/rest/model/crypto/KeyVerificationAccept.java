package org.matrix.androidsdk.rest.model.crypto;

import java.lang.System;

/**
 * * Sent by Bob to accept a verification from a previously sent m.key.verification.start message.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "()V", "commitment", "", "hash", "keyAgreementProtocol", "messageAuthenticationCode", "shortAuthenticationStrings", "", "transactionID", "isValid", "", "Companion", "matrix-sdk_debug"})
public final class KeyVerificationAccept implements org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject {
    
    /**
     * * string to identify the transaction.
     *     * This string must be unique for the pair of users performing verification for the duration that the transaction is valid.
     *     * Alice’s device should record this ID and use it in future messages in this transaction.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "transaction_id")
    public java.lang.String transactionID;
    
    /**
     * * The key agreement protocol that Bob’s device has selected to use, out of the list proposed by Alice’s device
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "key_agreement_protocol")
    public java.lang.String keyAgreementProtocol;
    
    /**
     * * The hash algorithm that Bob’s device has selected to use, out of the list proposed by Alice’s device
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String hash;
    
    /**
     * * The message authentication code that Bob’s device has selected to use, out of the list proposed by Alice’s device
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "message_authentication_code")
    public java.lang.String messageAuthenticationCode;
    
    /**
     * * An array of short authentication string methods that Bob’s client (and Bob) understands.  Must be a subset of the list proposed by Alice’s device
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "short_authentication_string")
    public java.util.List<java.lang.String> shortAuthenticationStrings;
    
    /**
     * * The hash (encoded as unpadded base64) of the concatenation of the device’s ephemeral public key (QB, encoded as unpadded base64)
     *     *  and the canonical JSON representation of the m.key.verification.start message.
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String commitment;
    public static final org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept.Companion Companion = null;
    
    public final boolean isValid() {
        return false;
    }
    
    public KeyVerificationAccept() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\f\u00a8\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;", "tid", "", "keyAgreementProtocol", "hash", "commitment", "messageAuthenticationCode", "shortAuthenticationStrings", "", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept create(@org.jetbrains.annotations.NotNull()
        java.lang.String tid, @org.jetbrains.annotations.NotNull()
        java.lang.String keyAgreementProtocol, @org.jetbrains.annotations.NotNull()
        java.lang.String hash, @org.jetbrains.annotations.NotNull()
        java.lang.String commitment, @org.jetbrains.annotations.NotNull()
        java.lang.String messageAuthenticationCode, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.String> shortAuthenticationStrings) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}