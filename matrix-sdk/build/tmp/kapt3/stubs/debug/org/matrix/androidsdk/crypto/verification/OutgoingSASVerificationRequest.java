package org.matrix.androidsdk.crypto.verification;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u001bB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001c"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest;", "Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction;", "transactionId", "", "otherUserId", "otherDeviceId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "uxState", "Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "getUxState", "()Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "onKeyVerificationKey", "", "session", "Lorg/matrix/androidsdk/MXSession;", "userId", "vKey", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationKey;", "onKeyVerificationMac", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;", "onVerificationAccept", "accept", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;", "onVerificationStart", "startReq", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;", "start", "State", "matrix-sdk_debug"})
public final class OutgoingSASVerificationRequest extends org.matrix.androidsdk.crypto.verification.SASVerificationTransaction {
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.verification.OutgoingSASVerificationRequest.State getUxState() {
        return null;
    }
    
    @java.lang.Override()
    public void onVerificationStart(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart startReq) {
    }
    
    public final void start(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session) {
    }
    
    @java.lang.Override()
    public void onVerificationAccept(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept accept) {
    }
    
    @java.lang.Override()
    public void onKeyVerificationKey(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationKey vKey) {
    }
    
    @java.lang.Override()
    public void onKeyVerificationMac(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac vKey) {
    }
    
    public OutgoingSASVerificationRequest(@org.jetbrains.annotations.NotNull()
    java.lang.String transactionId, @org.jetbrains.annotations.NotNull()
    java.lang.String otherUserId, @org.jetbrains.annotations.NotNull()
    java.lang.String otherDeviceId) {
        super(null, null, null, false);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "", "(Ljava/lang/String;I)V", "UNKNOWN", "WAIT_FOR_START", "WAIT_FOR_KEY_AGREEMENT", "SHOW_SAS", "WAIT_FOR_VERIFICATION", "VERIFIED", "CANCELLED_BY_ME", "CANCELLED_BY_OTHER", "matrix-sdk_debug"})
    public static enum State {
        /*public static final*/ UNKNOWN /* = new UNKNOWN() */,
        /*public static final*/ WAIT_FOR_START /* = new WAIT_FOR_START() */,
        /*public static final*/ WAIT_FOR_KEY_AGREEMENT /* = new WAIT_FOR_KEY_AGREEMENT() */,
        /*public static final*/ SHOW_SAS /* = new SHOW_SAS() */,
        /*public static final*/ WAIT_FOR_VERIFICATION /* = new WAIT_FOR_VERIFICATION() */,
        /*public static final*/ VERIFIED /* = new VERIFIED() */,
        /*public static final*/ CANCELLED_BY_ME /* = new CANCELLED_BY_ME() */,
        /*public static final*/ CANCELLED_BY_OTHER /* = new CANCELLED_BY_OTHER() */;
        
        State() {
        }
    }
}