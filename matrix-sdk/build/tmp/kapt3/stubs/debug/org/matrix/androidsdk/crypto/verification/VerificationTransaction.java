package org.matrix.androidsdk.crypto.verification;

import java.lang.System;

/**
 * * Generic interactive key verification transaction
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001:\u0001$B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u001dH&J\u000e\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\fJ\u0018\u0010 \u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\"H&J\u000e\u0010#\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\tR \u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012\u00a8\u0006%"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "", "transactionId", "", "otherUserId", "otherDeviceId", "isIncoming", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "()Z", "listeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "getListeners", "()Ljava/util/ArrayList;", "setListeners", "(Ljava/util/ArrayList;)V", "getOtherDeviceId", "()Ljava/lang/String;", "setOtherDeviceId", "(Ljava/lang/String;)V", "getOtherUserId", "getTransactionId", "acceptToDeviceEvent", "", "session", "Lorg/matrix/androidsdk/MXSession;", "senderId", "event", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "addListener", "listener", "cancel", "code", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "removeListener", "Listener", "matrix-sdk_debug"})
public abstract class VerificationTransaction {
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener> listeners;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transactionId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String otherUserId = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String otherDeviceId;
    private final boolean isIncoming = false;
    
    @org.jetbrains.annotations.NotNull()
    protected final java.util.ArrayList<org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener> getListeners() {
        return null;
    }
    
    protected final void setListeners(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener> p0) {
    }
    
    public final void addListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener listener) {
    }
    
    public final void removeListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener listener) {
    }
    
    public abstract void acceptToDeviceEvent(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    java.lang.String senderId, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject event);
    
    public abstract void cancel(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.CancelCode code);
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransactionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOtherUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOtherDeviceId() {
        return null;
    }
    
    public final void setOtherDeviceId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final boolean isIncoming() {
        return false;
    }
    
    public VerificationTransaction(@org.jetbrains.annotations.NotNull()
    java.lang.String transactionId, @org.jetbrains.annotations.NotNull()
    java.lang.String otherUserId, @org.jetbrains.annotations.Nullable()
    java.lang.String otherDeviceId, boolean isIncoming) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "", "transactionUpdated", "", "tx", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "matrix-sdk_debug"})
    public static abstract interface Listener {
        
        public abstract void transactionUpdated(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.verification.VerificationTransaction tx);
    }
}