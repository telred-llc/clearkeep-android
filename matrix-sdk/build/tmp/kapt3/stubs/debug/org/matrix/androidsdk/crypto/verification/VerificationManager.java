package org.matrix.androidsdk.crypto.verification;

import java.lang.System;

/**
 * * Manages all current verifications transactions with short codes.
 * * Short codes interactive verification is a more user friendly way of verifying devices
 * * that is still maintaining a good level of security (alternative to the 43-character strings compare method).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 92\u00020\u0001:\u00029:B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0007J\u0010\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u000fH\u0002J \u0010\u0017\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eJ\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eJH\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u001f2\u0018\u0010 \u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\"\u0012\u0004\u0012\u00020\u00130!2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00130%H\u0002J\u0018\u0010&\u001a\u00020\u000e2\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eH\u0002J\u0010\u0010\'\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u000fH\u0002J\u0010\u0010(\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u000fH\u0002J\u0018\u0010)\u001a\u0004\u0018\u00010\u000f2\u0006\u0010*\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u000eJ\u0018\u0010,\u001a\n\u0012\u0004\u0012\u00020\u000f\u0018\u00010-2\u0006\u0010*\u001a\u00020\u000eH\u0002J\u0016\u0010.\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eJ\u0010\u0010/\u001a\u00020\u00132\u0006\u00100\u001a\u000201H\u0002J\u0010\u00102\u001a\u00020\u00132\u0006\u00100\u001a\u000201H\u0002J\u0010\u00103\u001a\u00020\u00132\u0006\u00100\u001a\u000201H\u0002J\u0010\u00104\u001a\u00020\u00132\u0006\u00100\u001a\u000201H\u0002J\u0010\u00105\u001a\u00020\u00132\u0006\u00100\u001a\u000201H\u0002J\u000e\u00106\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0007J\u0018\u00107\u001a\u00020\u00132\u0006\u0010*\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u000eH\u0002J\u0010\u00108\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u000fH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\f\u001a\u001a\u0012\u0004\u0012\u00020\u000e\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "session", "Lorg/matrix/androidsdk/MXSession;", "(Lorg/matrix/androidsdk/MXSession;)V", "listeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/verification/VerificationManager$VerificationManagerListener;", "getSession", "()Lorg/matrix/androidsdk/MXSession;", "sessionListener", "Lorg/matrix/androidsdk/listeners/MXEventListener;", "txMap", "Ljava/util/HashMap;", "", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "uiHandler", "Landroid/os/Handler;", "addListener", "", "listener", "addTransaction", "tx", "beginKeyVerification", "method", "userId", "deviceID", "beginKeyVerificationSAS", "checkKeysAreDownloaded", "otherUserId", "startReq", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;", "success", "Lkotlin/Function1;", "Lorg/matrix/androidsdk/crypto/data/MXUsersDevicesMap;", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "error", "Lkotlin/Function0;", "createUniqueIDForTransaction", "dispatchTxAdded", "dispatchTxUpdated", "getExistingTransaction", "otherUser", "tid", "getExistingTransactionsForUser", "", "markedLocallyAsManuallyVerified", "onAcceptReceived", "event", "Lorg/matrix/androidsdk/rest/model/Event;", "onCancelReceived", "onKeyReceived", "onMacReceived", "onStartRequestReceived", "removeListener", "removeTransaction", "transactionUpdated", "Companion", "VerificationManagerListener", "matrix-sdk_debug"})
public final class VerificationManager implements org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener {
    private final android.os.Handler uiHandler = null;
    private final java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, org.matrix.androidsdk.crypto.verification.VerificationTransaction>> txMap = null;
    private final org.matrix.androidsdk.listeners.MXEventListener sessionListener = null;
    private java.util.ArrayList<org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener> listeners;
    @org.jetbrains.annotations.NotNull()
    private final org.matrix.androidsdk.MXSession session = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String LOG_TAG = null;
    public static final org.matrix.androidsdk.crypto.verification.VerificationManager.Companion Companion = null;
    
    public final void addListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener listener) {
    }
    
    public final void removeListener(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener listener) {
    }
    
    private final void dispatchTxAdded(org.matrix.androidsdk.crypto.verification.VerificationTransaction tx) {
    }
    
    private final void dispatchTxUpdated(org.matrix.androidsdk.crypto.verification.VerificationTransaction tx) {
    }
    
    public final void markedLocallyAsManuallyVerified(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceID) {
    }
    
    private final void onStartRequestReceived(org.matrix.androidsdk.rest.model.Event event) {
    }
    
    private final void checkKeysAreDownloaded(org.matrix.androidsdk.MXSession session, java.lang.String otherUserId, org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart startReq, kotlin.jvm.functions.Function1<? super org.matrix.androidsdk.crypto.data.MXUsersDevicesMap<org.matrix.androidsdk.crypto.data.MXDeviceInfo>, kotlin.Unit> success, kotlin.jvm.functions.Function0<kotlin.Unit> error) {
    }
    
    private final void onCancelReceived(org.matrix.androidsdk.rest.model.Event event) {
    }
    
    private final void onAcceptReceived(org.matrix.androidsdk.rest.model.Event event) {
    }
    
    private final void onKeyReceived(org.matrix.androidsdk.rest.model.Event event) {
    }
    
    private final void onMacReceived(org.matrix.androidsdk.rest.model.Event event) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.verification.VerificationTransaction getExistingTransaction(@org.jetbrains.annotations.NotNull()
    java.lang.String otherUser, @org.jetbrains.annotations.NotNull()
    java.lang.String tid) {
        return null;
    }
    
    private final java.util.Collection<org.matrix.androidsdk.crypto.verification.VerificationTransaction> getExistingTransactionsForUser(java.lang.String otherUser) {
        return null;
    }
    
    private final void removeTransaction(java.lang.String otherUser, java.lang.String tid) {
    }
    
    private final void addTransaction(org.matrix.androidsdk.crypto.verification.VerificationTransaction tx) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String beginKeyVerificationSAS(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceID) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String beginKeyVerification(@org.jetbrains.annotations.NotNull()
    java.lang.String method, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceID) {
        return null;
    }
    
    /**
     * * This string must be unique for the pair of users performing verification for the duration that the transaction is valid
     */
    private final java.lang.String createUniqueIDForTransaction(java.lang.String userId, java.lang.String deviceID) {
        return null;
    }
    
    @java.lang.Override()
    public void transactionUpdated(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.VerificationTransaction tx) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.MXSession getSession() {
        return null;
    }
    
    public VerificationManager(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager$VerificationManagerListener;", "", "markedAsManuallyVerified", "", "userId", "", "deviceId", "transactionCreated", "tx", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "transactionUpdated", "matrix-sdk_debug"})
    public static abstract interface VerificationManagerListener {
        
        public abstract void transactionCreated(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.verification.VerificationTransaction tx);
        
        public abstract void transactionUpdated(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.verification.VerificationTransaction tx);
        
        public abstract void markedAsManuallyVerified(@org.jetbrains.annotations.NotNull()
        java.lang.String userId, @org.jetbrains.annotations.NotNull()
        java.lang.String deviceId);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J.\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager$Companion;", "", "()V", "LOG_TAG", "", "getLOG_TAG", "()Ljava/lang/String;", "cancelTransaction", "", "session", "Lorg/matrix/androidsdk/MXSession;", "transactionId", "userId", "userDevice", "code", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getLOG_TAG() {
            return null;
        }
        
        public final void cancelTransaction(@org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
        java.lang.String transactionId, @org.jetbrains.annotations.NotNull()
        java.lang.String userId, @org.jetbrains.annotations.NotNull()
        java.lang.String userDevice, @org.jetbrains.annotations.NotNull()
        org.matrix.androidsdk.crypto.verification.CancelCode code) {
        }
        
        private Companion() {
            super();
        }
    }
}