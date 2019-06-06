package org.matrix.androidsdk.rest.model.crypto;

import java.lang.System;

/**
 * * To device event sent by either party to cancel a key verification.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationCancel;", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "()V", "code", "", "reason", "transactionID", "isValid", "", "Companion", "matrix-sdk_debug"})
public final class KeyVerificationCancel implements org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject {
    
    /**
     * * the transaction ID of the verification to cancel
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "transaction_id")
    public java.lang.String transactionID;
    
    /**
     * * machine-readable reason for cancelling, see #CancelCode
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String code;
    
    /**
     * * human-readable reason for cancelling.  This should only be used if the receiving client does not understand the code given.
     */
    @org.jetbrains.annotations.Nullable()
    public java.lang.String reason;
    public static final org.matrix.androidsdk.rest.model.crypto.KeyVerificationCancel.Companion Companion = null;
    
    public final boolean isValid() {
        return false;
    }
    
    public KeyVerificationCancel() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationCancel$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationCancel;", "tid", "", "cancelCode", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationCancel create(@org.jetbrains.annotations.NotNull()
        java.lang.String tid, @org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.verification.CancelCode cancelCode) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}