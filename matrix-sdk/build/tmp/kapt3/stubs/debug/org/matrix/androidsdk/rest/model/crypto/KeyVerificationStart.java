package org.matrix.androidsdk.rest.model.crypto;

import java.lang.System;

/**
 * * Sent by Alice to initiate an interactive key verification.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "()V", "fromDevice", "", "hashes", "", "keyAgreementProtocols", "messageAuthenticationCodes", "method", "shortAuthenticationStrings", "transactionID", "isValid", "", "Companion", "matrix-sdk_debug"})
public final class KeyVerificationStart implements org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject {
    
    /**
     * * Alice’s device ID
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "from_device")
    public java.lang.String fromDevice;
    @org.jetbrains.annotations.Nullable()
    public java.lang.String method;
    
    /**
     * * String to identify the transaction.
     *     * This string must be unique for the pair of users performing verification for the duration that the transaction is valid.
     *     * Alice’s device should record this ID and use it in future messages in this transaction.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "transaction_id")
    public java.lang.String transactionID;
    
    /**
     * * An array of key agreement protocols that Alice’s client understands.
     *     * Must include “curve25519”.
     *     * Other methods may be defined in the future
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "key_agreement_protocols")
    public java.util.List<java.lang.String> keyAgreementProtocols;
    
    /**
     * * An array of hashes that Alice’s client understands.
     *     * Must include “sha256”.  Other methods may be defined in the future.
     */
    @org.jetbrains.annotations.Nullable()
    public java.util.List<java.lang.String> hashes;
    
    /**
     * * An array of message authentication codes that Alice’s client understands.
     *     * Must include “hkdf-hmac-sha256”.
     *     * Other methods may be defined in the future.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "message_authentication_codes")
    public java.util.List<java.lang.String> messageAuthenticationCodes;
    
    /**
     * * An array of short authentication string methods that Alice’s client (and Alice) understands.
     *     * Must include “decimal”.
     *     * This document also describes the “emoji” method.
     *     * Other methods may be defined in the future
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "short_authentication_string")
    public java.util.List<java.lang.String> shortAuthenticationStrings;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String VERIF_METHOD_SAS = "m.sas.v1";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAS_MODE_DECIMAL = "decimal";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAS_MODE_EMOJI = "emoji";
    public static final org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart.Companion Companion = null;
    
    public final boolean isValid() {
        return false;
    }
    
    public KeyVerificationStart() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart$Companion;", "", "()V", "SAS_MODE_DECIMAL", "", "SAS_MODE_EMOJI", "VERIF_METHOD_SAS", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}