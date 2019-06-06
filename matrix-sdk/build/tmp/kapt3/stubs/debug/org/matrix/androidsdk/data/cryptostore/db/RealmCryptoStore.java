package org.matrix.androidsdk.data.cryptostore.db;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010#\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b \u0018\u0000 k2\u00020\u0001:\u0001kB\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0012\u0010\u0013\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u0012\u0010\u0016\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\u0018\u001a\u00020\u0012H\u0016J\u0014\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\tH\u0016J\n\u0010\u001c\u001a\u0004\u0018\u00010\fH\u0016J\b\u0010\u001d\u001a\u00020\tH\u0016J\u001e\u0010\u001e\u001a\u0004\u0018\u00010\u000e2\b\u0010\u001f\u001a\u0004\u0018\u00010\t2\b\u0010 \u001a\u0004\u0018\u00010\tH\u0016J\u0018\u0010!\u001a\b\u0012\u0004\u0012\u00020\t0\"2\b\u0010 \u001a\u0004\u0018\u00010\tH\u0016J\u001a\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\t2\u0006\u0010&\u001a\u00020$H\u0016J\u0014\u0010\'\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020$0(H\u0016J\b\u0010)\u001a\u00020\u0003H\u0016J\u001e\u0010*\u001a\u0004\u0018\u00010\n2\b\u0010\u001f\u001a\u0004\u0018\u00010\t2\b\u0010+\u001a\u0004\u0018\u00010\tH\u0016J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00020\n0-H\u0016J(\u0010.\u001a\u0004\u0018\u00010\u00152\b\u0010%\u001a\u0004\u0018\u00010\t2\b\u0010/\u001a\u0004\u0018\u00010\t2\b\u00100\u001a\u0004\u0018\u00010\tH\u0016J\n\u00101\u001a\u0004\u0018\u00010\tH\u0016J\n\u00102\u001a\u0004\u0018\u000103H\u0016J\u0014\u00104\u001a\u0004\u0018\u00010\t2\b\u0010 \u001a\u0004\u0018\u00010\tH\u0016J\u0014\u00105\u001a\u0004\u0018\u0001062\b\u00107\u001a\u0004\u0018\u000106H\u0016J\u0014\u00108\u001a\u0004\u0018\u0001062\b\u00109\u001a\u0004\u0018\u00010:H\u0016J\u001a\u0010;\u001a\u0004\u0018\u0001062\u000e\u0010<\u001a\n\u0012\u0004\u0012\u00020=\u0018\u00010\"H\u0016J\u000e\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00150-H\u0016J\u0012\u0010?\u001a\u0004\u0018\u00010\t2\u0006\u0010@\u001a\u00020\tH\u0016J\u000e\u0010A\u001a\b\u0012\u0004\u0012\u00020\t0-H\u0016J\u001e\u0010B\u001a\u0004\u0018\u00010\u001a2\b\u0010/\u001a\u0004\u0018\u00010\t2\b\u0010%\u001a\u0004\u0018\u00010\tH\u0016J \u0010C\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001a\u0018\u00010(2\b\u0010%\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010D\u001a\u00020\u0003H\u0016J\u0010\u0010E\u001a\u00020$2\u0006\u0010F\u001a\u00020\u0003H\u0016J\u0016\u0010G\u001a\b\u0012\u0004\u0012\u00020\n0H2\u0006\u0010I\u001a\u00020$H\u0016J\u0018\u0010J\u001a\u00020\u00122\u0006\u0010K\u001a\u00020L2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010M\u001a\u00020\u0003H\u0016J\u0016\u0010N\u001a\u00020\u00122\f\u0010O\u001a\b\u0012\u0004\u0012\u00020\n0-H\u0016J\b\u0010P\u001a\u00020\u0012H\u0016J\u001c\u0010Q\u001a\u00020\u00122\b\u0010\u001f\u001a\u0004\u0018\u00010\t2\b\u0010+\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010R\u001a\u00020\u0012H\u0016J\u001e\u0010S\u001a\u00020\u00122\u0014\u0010T\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020$\u0018\u00010(H\u0016J\u0010\u0010U\u001a\u00020\u00122\u0006\u0010V\u001a\u00020\u0003H\u0016J\u0012\u0010W\u001a\u00020\u00122\b\u0010X\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010Y\u001a\u00020\u00122\b\u0010Z\u001a\u0004\u0018\u000103H\u0016J\u0016\u0010[\u001a\u00020\u00122\f\u0010\\\u001a\b\u0012\u0004\u0012\u00020\t0-H\u0016J\u0010\u0010]\u001a\u00020\u00122\u0006\u0010^\u001a\u00020\fH\u0016J\u0012\u0010_\u001a\u00020\u00122\b\u0010/\u001a\u0004\u0018\u00010\tH\u0016J\u0016\u0010`\u001a\u00020\u00122\f\u0010O\u001a\b\u0012\u0004\u0012\u00020\n0-H\u0016J\u0012\u0010a\u001a\u00020\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u0018\u0010b\u001a\u00020\u00122\u0006\u0010@\u001a\u00020\t2\u0006\u0010c\u001a\u00020\tH\u0016J\u001c\u0010d\u001a\u00020\u00122\b\u0010e\u001a\u0004\u0018\u00010\u000e2\b\u0010 \u001a\u0004\u0018\u00010\tH\u0016J\u001c\u0010f\u001a\u00020\u00122\b\u0010%\u001a\u0004\u0018\u00010\t2\b\u0010g\u001a\u0004\u0018\u00010\u001aH\u0016J(\u0010h\u001a\u00020\u00122\b\u0010%\u001a\u0004\u0018\u00010\t2\u0014\u0010i\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001a\u0018\u00010(H\u0016J\u0012\u0010j\u001a\u00020\u00122\b\u00107\u001a\u0004\u0018\u000106H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006l"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/RealmCryptoStore;", "Lorg/matrix/androidsdk/data/cryptostore/IMXCryptoStore;", "enableFileEncryption", "", "(Z)V", "credentials", "Lorg/matrix/androidsdk/rest/model/login/Credentials;", "inboundGroupSessionToRelease", "Ljava/util/HashMap;", "", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "olmAccount", "Lorg/matrix/olm/OlmAccount;", "olmSessionsToRelease", "Lorg/matrix/androidsdk/crypto/data/MXOlmSession;", "realmConfiguration", "Lio/realm/RealmConfiguration;", "close", "", "deleteIncomingRoomKeyRequest", "incomingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequest;", "deleteOutgoingRoomKeyRequest", "transactionId", "deleteStore", "deviceWithIdentityKey", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "identityKey", "getAccount", "getDeviceId", "getDeviceSession", "sessionId", "deviceKey", "getDeviceSessionIds", "", "getDeviceTrackingStatus", "", "userId", "defaultValue", "getDeviceTrackingStatuses", "", "getGlobalBlacklistUnverifiedDevices", "getInboundGroupSession", "senderKey", "getInboundGroupSessions", "", "getIncomingRoomKeyRequest", "deviceId", "requestId", "getKeyBackupVersion", "getKeysBackupData", "Lorg/matrix/androidsdk/data/cryptostore/db/model/KeysBackupDataEntity;", "getLastUsedSessionId", "getOrAddOutgoingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest;", "request", "getOutgoingRoomKeyRequest", "requestBody", "Lorg/matrix/androidsdk/rest/model/crypto/RoomKeyRequestBody;", "getOutgoingRoomKeyRequestByState", "states", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest$RequestState;", "getPendingIncomingRoomKeyRequests", "getRoomAlgorithm", "roomId", "getRoomsListBlacklistUnverifiedDevices", "getUserDevice", "getUserDevices", "hasData", "inboundGroupSessionsCount", "onlyBackedUp", "inboundGroupSessionsToBackup", "", "limit", "initWithCredentials", "context", "Landroid/content/Context;", "isCorrupted", "markBackupDoneForInboundGroupSessions", "sessions", "open", "removeInboundGroupSession", "resetBackupMarkers", "saveDeviceTrackingStatuses", "deviceTrackingStatuses", "setGlobalBlacklistUnverifiedDevices", "block", "setKeyBackupVersion", "keyBackupVersion", "setKeysBackupData", "keysBackupData", "setRoomsListBlacklistUnverifiedDevices", "roomIds", "storeAccount", "account", "storeDeviceId", "storeInboundGroupSessions", "storeIncomingRoomKeyRequest", "storeRoomAlgorithm", "algorithm", "storeSession", "session", "storeUserDevice", "deviceInfo", "storeUserDevices", "devices", "updateOutgoingRoomKeyRequest", "Companion", "matrix-sdk_debug"})
public final class RealmCryptoStore implements org.matrix.androidsdk.data.cryptostore.IMXCryptoStore {
    private org.matrix.olm.OlmAccount olmAccount;
    private final java.util.HashMap<java.lang.String, org.matrix.androidsdk.crypto.data.MXOlmSession> olmSessionsToRelease = null;
    private final java.util.HashMap<java.lang.String, org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2> inboundGroupSessionToRelease = null;
    private org.matrix.androidsdk.rest.model.login.Credentials credentials;
    private io.realm.RealmConfiguration realmConfiguration;
    private final boolean enableFileEncryption = false;
    private static final java.lang.String LOG_TAG = "RealmCryptoStore";
    public static final org.matrix.androidsdk.data.cryptostore.db.RealmCryptoStore.Companion Companion = null;
    
    @java.lang.Override()
    public void initWithCredentials(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.login.Credentials credentials) {
    }
    
    @java.lang.Override()
    public boolean isCorrupted() {
        return false;
    }
    
    @java.lang.Override()
    public boolean hasData() {
        return false;
    }
    
    @java.lang.Override()
    public void deleteStore() {
    }
    
    @java.lang.Override()
    public void open() {
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    @java.lang.Override()
    public void storeDeviceId(@org.jetbrains.annotations.Nullable()
    java.lang.String deviceId) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getDeviceId() {
        return null;
    }
    
    @java.lang.Override()
    public void storeAccount(@org.jetbrains.annotations.NotNull()
    org.matrix.olm.OlmAccount account) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.olm.OlmAccount getAccount() {
        return null;
    }
    
    @java.lang.Override()
    public void storeUserDevice(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.data.MXDeviceInfo deviceInfo) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.data.MXDeviceInfo getUserDevice(@org.jetbrains.annotations.Nullable()
    java.lang.String deviceId, @org.jetbrains.annotations.Nullable()
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.data.MXDeviceInfo deviceWithIdentityKey(@org.jetbrains.annotations.Nullable()
    java.lang.String identityKey) {
        return null;
    }
    
    @java.lang.Override()
    public void storeUserDevices(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, org.matrix.androidsdk.crypto.data.MXDeviceInfo> devices) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.util.Map<java.lang.String, org.matrix.androidsdk.crypto.data.MXDeviceInfo> getUserDevices(@org.jetbrains.annotations.Nullable()
    java.lang.String userId) {
        return null;
    }
    
    @java.lang.Override()
    public void storeRoomAlgorithm(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId, @org.jetbrains.annotations.NotNull()
    java.lang.String algorithm) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.String getRoomAlgorithm(@org.jetbrains.annotations.NotNull()
    java.lang.String roomId) {
        return null;
    }
    
    @java.lang.Override()
    public void storeSession(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.data.MXOlmSession session, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceKey) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.data.MXOlmSession getDeviceSession(@org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceKey) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.String getLastUsedSessionId(@org.jetbrains.annotations.Nullable()
    java.lang.String deviceKey) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.Set<java.lang.String> getDeviceSessionIds(@org.jetbrains.annotations.Nullable()
    java.lang.String deviceKey) {
        return null;
    }
    
    @java.lang.Override()
    public void storeInboundGroupSessions(@org.jetbrains.annotations.NotNull()
    java.util.List<org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2> sessions) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2 getInboundGroupSession(@org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String senderKey) {
        return null;
    }
    
    /**
     * * Note: the result will be only use to export all the keys and not to use the MXOlmInboundGroupSession2,
     *     * so there is no need to use or update `inboundGroupSessionToRelease` for native memory management
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2> getInboundGroupSessions() {
        return null;
    }
    
    @java.lang.Override()
    public void removeInboundGroupSession(@org.jetbrains.annotations.Nullable()
    java.lang.String sessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String senderKey) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.String getKeyBackupVersion() {
        return null;
    }
    
    @java.lang.Override()
    public void setKeyBackupVersion(@org.jetbrains.annotations.Nullable()
    java.lang.String keyBackupVersion) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity getKeysBackupData() {
        return null;
    }
    
    @java.lang.Override()
    public void setKeysBackupData(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity keysBackupData) {
    }
    
    @java.lang.Override()
    public void resetBackupMarkers() {
    }
    
    @java.lang.Override()
    public void markBackupDoneForInboundGroupSessions(@org.jetbrains.annotations.NotNull()
    java.util.List<org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2> sessions) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2> inboundGroupSessionsToBackup(int limit) {
        return null;
    }
    
    @java.lang.Override()
    public int inboundGroupSessionsCount(boolean onlyBackedUp) {
        return 0;
    }
    
    @java.lang.Override()
    public void setGlobalBlacklistUnverifiedDevices(boolean block) {
    }
    
    @java.lang.Override()
    public boolean getGlobalBlacklistUnverifiedDevices() {
        return false;
    }
    
    @java.lang.Override()
    public void setRoomsListBlacklistUnverifiedDevices(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> roomIds) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<java.lang.String> getRoomsListBlacklistUnverifiedDevices() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.Map<java.lang.String, java.lang.Integer> getDeviceTrackingStatuses() {
        return null;
    }
    
    @java.lang.Override()
    public void saveDeviceTrackingStatuses(@org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.Integer> deviceTrackingStatuses) {
    }
    
    @java.lang.Override()
    public int getDeviceTrackingStatus(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, int defaultValue) {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest getOutgoingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.RoomKeyRequestBody requestBody) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest getOrAddOutgoingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest request) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest getOutgoingRoomKeyRequestByState(@org.jetbrains.annotations.Nullable()
    java.util.Set<org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState> states) {
        return null;
    }
    
    @java.lang.Override()
    public void updateOutgoingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest request) {
    }
    
    @java.lang.Override()
    public void deleteOutgoingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    java.lang.String transactionId) {
    }
    
    @java.lang.Override()
    public void storeIncomingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.IncomingRoomKeyRequest incomingRoomKeyRequest) {
    }
    
    @java.lang.Override()
    public void deleteIncomingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.IncomingRoomKeyRequest incomingRoomKeyRequest) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public org.matrix.androidsdk.crypto.IncomingRoomKeyRequest getIncomingRoomKeyRequest(@org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceId, @org.jetbrains.annotations.Nullable()
    java.lang.String requestId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<org.matrix.androidsdk.crypto.IncomingRoomKeyRequest> getPendingIncomingRoomKeyRequests() {
        return null;
    }
    
    public RealmCryptoStore(boolean enableFileEncryption) {
        super();
    }
    
    public RealmCryptoStore() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/RealmCryptoStore$Companion;", "", "()V", "LOG_TAG", "", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}