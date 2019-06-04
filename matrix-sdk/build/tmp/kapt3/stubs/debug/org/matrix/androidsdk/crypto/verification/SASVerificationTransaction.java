package org.matrix.androidsdk.crypto.verification;

import java.lang.System;

/**
 * * Represents an ongoing short code interactive key verification between two devices.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\b&\u0018\u0000 j2\u00020\u0001:\u0002jkB\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ \u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u00032\u0006\u0010?\u001a\u00020@H\u0016J\u0018\u0010A\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010B\u001a\u00020\u0010H\u0016J\b\u0010C\u001a\u00020;H\u0004J\u000e\u0010D\u001a\u00020\u00032\u0006\u0010E\u001a\u00020#J\u0014\u0010F\u001a\b\u0012\u0004\u0012\u00020H0G2\u0006\u0010E\u001a\u00020#J\u0006\u0010I\u001a\u00020\u001cJ\u0010\u0010J\u001a\u0004\u0018\u00010\u00032\u0006\u0010K\u001a\u00020\u0003J\u0012\u0010L\u001a\u0004\u0018\u00010\u00032\u0006\u0010M\u001a\u00020\u0003H\u0004J\u001a\u0010N\u001a\u0004\u0018\u00010\u00032\u0006\u0010O\u001a\u00020\u00032\u0006\u0010P\u001a\u00020\u0003H\u0004J \u0010Q\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010R\u001a\u00020\u00032\u0006\u0010S\u001a\u00020TH&J\u0018\u0010U\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010S\u001a\u00020\u0016H&J\u0018\u0010V\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010W\u001a\u00020\nH&J\u0018\u0010X\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010(\u001a\u00020)H&J\b\u0010Y\u001a\u00020;H\u0002J@\u0010Z\u001a\u00020;2\u0006\u0010[\u001a\u00020\u00032\u0006\u0010\\\u001a\u00020]2\u0006\u0010<\u001a\u00020=2\u0006\u0010^\u001a\u00020/2\u0006\u0010_\u001a\u00020\u00102\u000e\u0010`\u001a\n\u0012\u0004\u0012\u00020;\u0018\u00010aH\u0004J<\u0010b\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010c\u001a\u00020\u00032\u0006\u0010R\u001a\u00020\u00032\f\u0010d\u001a\b\u0012\u0004\u0012\u00020;0a2\f\u0010e\u001a\b\u0012\u0004\u0012\u00020;0aH\u0002J\u0006\u0010f\u001a\u00020\u0007J\u0006\u0010g\u001a\u00020\u0007J\u000e\u0010h\u001a\u00020;2\u0006\u0010<\u001a\u00020=J\u0010\u0010i\u001a\u00020;2\u0006\u0010<\u001a\u00020=H\u0004R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001c\u0010\"\u001a\u0004\u0018\u00010#X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010\'R\u001c\u0010(\u001a\u0004\u0018\u00010)X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R+\u00100\u001a\u00020/2\u0006\u0010.\u001a\u00020/8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b5\u00106\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001c\u00107\u001a\u0004\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0018\"\u0004\b9\u0010\u001a\u00a8\u0006l"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "transactionId", "", "otherUserId", "otherDevice", "isIncoming", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "accepted", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;", "getAccepted", "()Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;", "setAccepted", "(Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationAccept;)V", "cancelledReason", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "getCancelledReason", "()Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "setCancelledReason", "(Lorg/matrix/androidsdk/crypto/verification/CancelCode;)V", "myMac", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;", "getMyMac", "()Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;", "setMyMac", "(Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationMac;)V", "olmSas", "Lorg/matrix/olm/OlmSAS;", "otherKey", "getOtherKey", "()Ljava/lang/String;", "setOtherKey", "(Ljava/lang/String;)V", "shortCodeBytes", "", "getShortCodeBytes", "()[B", "setShortCodeBytes", "([B)V", "startReq", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;", "getStartReq", "()Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;", "setStartReq", "(Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationStart;)V", "<set-?>", "Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "state", "getState", "()Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "setState", "(Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;)V", "state$delegate", "Lkotlin/properties/ReadWriteProperty;", "theirMac", "getTheirMac", "setTheirMac", "acceptToDeviceEvent", "", "session", "Lorg/matrix/androidsdk/MXSession;", "senderId", "event", "Lorg/matrix/androidsdk/rest/model/crypto/SendToDeviceObject;", "cancel", "code", "finalize", "getDecimalCodeRepresentation", "byteArray", "getEmojiCodeRepresentation", "", "Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji$EmojiRepresentation;", "getSAS", "getShortCodeRepresentation", "shortAuthenticationStringMode", "hashUsingAgreedHashMethod", "toHash", "macUsingAgreedMethod", "message", "info", "onKeyVerificationKey", "userId", "vKey", "Lorg/matrix/androidsdk/rest/model/crypto/KeyVerificationKey;", "onKeyVerificationMac", "onVerificationAccept", "accept", "onVerificationStart", "releaseSAS", "sendToOther", "type", "keyToDevice", "", "nextState", "onErrorReason", "onDone", "Lkotlin/Function0;", "setDeviceVerified", "deviceId", "success", "error", "supportsDecimal", "supportsEmoji", "userHasVerifiedShortCode", "verifyMacs", "Companion", "SASVerificationTxState", "matrix-sdk_debug"})
public abstract class SASVerificationTransaction extends org.matrix.androidsdk.crypto.verification.VerificationTransaction {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.properties.ReadWriteProperty state$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.crypto.verification.CancelCode cancelledReason;
    private org.matrix.olm.OlmSAS olmSas;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart startReq;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept accepted;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String otherKey;
    @org.jetbrains.annotations.Nullable()
    private byte[] shortCodeBytes;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac myMac;
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac theirMac;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String LOG_TAG = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAS_MAC_SHA256_LONGKDF = "hmac-sha256";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAS_MAC_SHA256 = "hkdf-hmac-sha256";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> KNOWN_AGREEMENT_PROTOCOLS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> KNOWN_HASHES = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> KNOWN_MACS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> KNOWN_SHORT_CODES = null;
    public static final org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState getState() {
        return null;
    }
    
    public final void setState(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.verification.CancelCode getCancelledReason() {
        return null;
    }
    
    public final void setCancelledReason(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.verification.CancelCode p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart getStartReq() {
        return null;
    }
    
    public final void setStartReq(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept getAccepted() {
        return null;
    }
    
    public final void setAccepted(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOtherKey() {
        return null;
    }
    
    public final void setOtherKey(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final byte[] getShortCodeBytes() {
        return null;
    }
    
    public final void setShortCodeBytes(@org.jetbrains.annotations.Nullable()
    byte[] p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac getMyMac() {
        return null;
    }
    
    public final void setMyMac(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac getTheirMac() {
        return null;
    }
    
    public final void setTheirMac(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.olm.OlmSAS getSAS() {
        return null;
    }
    
    protected final void finalize() {
    }
    
    private final void releaseSAS() {
    }
    
    /**
     * * To be called by the client when the user has verified that
     *     * both short codes do match
     */
    public final void userHasVerifiedShortCode(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session) {
    }
    
    @java.lang.Override()
    public void acceptToDeviceEvent(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    java.lang.String senderId, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.SendToDeviceObject event) {
    }
    
    public abstract void onVerificationStart(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationStart startReq);
    
    public abstract void onVerificationAccept(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationAccept accept);
    
    public abstract void onKeyVerificationKey(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationKey vKey);
    
    public abstract void onKeyVerificationMac(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.crypto.KeyVerificationMac vKey);
    
    protected final void verifyMacs(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session) {
    }
    
    private final void setDeviceVerified(org.matrix.androidsdk.MXSession session, java.lang.String deviceId, java.lang.String userId, kotlin.jvm.functions.Function0<kotlin.Unit> success, kotlin.jvm.functions.Function0<kotlin.Unit> error) {
    }
    
    @java.lang.Override()
    public void cancel(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.CancelCode code) {
    }
    
    protected final void sendToOther(@org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.Object keyToDevice, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.MXSession session, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState nextState, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.crypto.verification.CancelCode onErrorReason, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDone) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getShortCodeRepresentation(@org.jetbrains.annotations.NotNull()
    java.lang.String shortAuthenticationStringMode) {
        return null;
    }
    
    public final boolean supportsEmoji() {
        return false;
    }
    
    public final boolean supportsDecimal() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String hashUsingAgreedHashMethod(@org.jetbrains.annotations.NotNull()
    java.lang.String toHash) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String macUsingAgreedMethod(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.lang.String info) {
        return null;
    }
    
    /**
     * * decimal: generate five bytes by using HKDF.
     *     * Take the first 13 bits and convert it to a decimal number (which will be a number between 0 and 8191 inclusive),
     *     * and add 1000 (resulting in a number between 1000 and 9191 inclusive).
     *     * Do the same with the second 13 bits, and the third 13 bits, giving three 4-digit numbers.
     *     * In other words, if the five bytes are B0, B1, B2, B3, and B4, then the first number is (B0 << 5 | B1 >> 3) + 1000,
     *     * the second number is ((B1 & 0x7) << 10 | B2 << 2 | B3 >> 6) + 1000, and the third number is ((B3 & 0x3f) << 7 | B4 >> 1) + 1000.
     *     * (This method of converting 13 bits at a time is used to avoid requiring 32-bit clients to do big-number arithmetic,
     *     * and adding 1000 to the number avoids having clients to worry about properly zero-padding the number when displaying to the user.)
     *     * The three 4-digit numbers are displayed to the user either with dashes (or another appropriate separator) separating the three numbers,
     *     * or with the three numbers on separate lines.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDecimalCodeRepresentation(@org.jetbrains.annotations.NotNull()
    byte[] byteArray) {
        return null;
    }
    
    /**
     * * emoji: generate six bytes by using HKDF.
     *     * Split the first 42 bits into 7 groups of 6 bits, as one would do when creating a base64 encoding.
     *     * For each group of 6 bits, look up the emoji from Appendix A corresponding
     *     * to that number 7 emoji are selected from a list of 64 emoji (see Appendix A)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<org.matrix.androidsdk.crypto.verification.VerificationEmoji.EmojiRepresentation> getEmojiCodeRepresentation(@org.jetbrains.annotations.NotNull()
    byte[] byteArray) {
        return null;
    }
    
    public SASVerificationTransaction(@org.jetbrains.annotations.NotNull()
    java.lang.String transactionId, @org.jetbrains.annotations.NotNull()
    java.lang.String otherUserId, @org.jetbrains.annotations.Nullable()
    java.lang.String otherDevice, boolean isIncoming) {
        super(null, null, null, false);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0014\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014\u00a8\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "", "(Ljava/lang/String;I)V", "None", "SendingStart", "Started", "OnStarted", "SendingAccept", "Accepted", "OnAccepted", "SendingKey", "KeySent", "OnKeyReceived", "ShortCodeReady", "ShortCodeAccepted", "SendingMac", "MacSent", "Verifying", "Verified", "Cancelled", "OnCancelled", "matrix-sdk_debug"})
    public static enum SASVerificationTxState {
        /*public static final*/ None /* = new None() */,
        /*public static final*/ SendingStart /* = new SendingStart() */,
        /*public static final*/ Started /* = new Started() */,
        /*public static final*/ OnStarted /* = new OnStarted() */,
        /*public static final*/ SendingAccept /* = new SendingAccept() */,
        /*public static final*/ Accepted /* = new Accepted() */,
        /*public static final*/ OnAccepted /* = new OnAccepted() */,
        /*public static final*/ SendingKey /* = new SendingKey() */,
        /*public static final*/ KeySent /* = new KeySent() */,
        /*public static final*/ OnKeyReceived /* = new OnKeyReceived() */,
        /*public static final*/ ShortCodeReady /* = new ShortCodeReady() */,
        /*public static final*/ ShortCodeAccepted /* = new ShortCodeAccepted() */,
        /*public static final*/ SendingMac /* = new SendingMac() */,
        /*public static final*/ MacSent /* = new MacSent() */,
        /*public static final*/ Verifying /* = new Verifying() */,
        /*public static final*/ Verified /* = new Verified() */,
        /*public static final*/ Cancelled /* = new Cancelled() */,
        /*public static final*/ OnCancelled /* = new OnCancelled() */;
        
        SASVerificationTxState() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$Companion;", "", "()V", "KNOWN_AGREEMENT_PROTOCOLS", "", "", "getKNOWN_AGREEMENT_PROTOCOLS", "()Ljava/util/List;", "KNOWN_HASHES", "getKNOWN_HASHES", "KNOWN_MACS", "getKNOWN_MACS", "KNOWN_SHORT_CODES", "getKNOWN_SHORT_CODES", "LOG_TAG", "getLOG_TAG", "()Ljava/lang/String;", "SAS_MAC_SHA256", "SAS_MAC_SHA256_LONGKDF", "matrix-sdk_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getLOG_TAG() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getKNOWN_AGREEMENT_PROTOCOLS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getKNOWN_HASHES() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getKNOWN_MACS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.String> getKNOWN_SHORT_CODES() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}