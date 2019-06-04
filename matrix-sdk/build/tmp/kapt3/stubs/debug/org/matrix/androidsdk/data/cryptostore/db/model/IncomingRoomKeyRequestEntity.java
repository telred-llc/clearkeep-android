package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0018\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u00002\u00020\u0001BY\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\u0006\u0010\u001f\u001a\u00020 R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u0004\b\u0016\u0010\u000eR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e\u00a8\u0006!"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/IncomingRoomKeyRequestEntity;", "Lio/realm/RealmObject;", "requestId", "", "userId", "deviceId", "requestBodyAlgorithm", "requestBodyRoomId", "requestBodySenderKey", "requestBodySessionId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "getRequestBodyAlgorithm", "setRequestBodyAlgorithm", "getRequestBodyRoomId", "setRequestBodyRoomId", "getRequestBodySenderKey", "setRequestBodySenderKey", "getRequestBodySessionId", "setRequestBodySessionId", "getRequestId", "setRequestId", "getUserId", "setUserId", "putRequestBody", "", "requestBody", "Lorg/matrix/androidsdk/rest/model/crypto/RoomKeyRequestBody;", "toIncomingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequest;", "matrix-sdk_debug"})
public class IncomingRoomKeyRequestEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String userId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodyAlgorithm;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodyRoomId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodySenderKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodySessionId;
    
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.IncomingRoomKeyRequest toIncomingRoomKeyRequest() {
        return null;
    }
    
    public final void putRequestBody(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.crypto.RoomKeyRequestBody requestBody) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequestId() {
        return null;
    }
    
    public final void setRequestId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserId() {
        return null;
    }
    
    public final void setUserId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceId() {
        return null;
    }
    
    public final void setDeviceId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequestBodyAlgorithm() {
        return null;
    }
    
    public final void setRequestBodyAlgorithm(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequestBodyRoomId() {
        return null;
    }
    
    public final void setRequestBodyRoomId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequestBodySenderKey() {
        return null;
    }
    
    public final void setRequestBodySenderKey(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequestBodySessionId() {
        return null;
    }
    
    public final void setRequestBodySessionId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public IncomingRoomKeyRequestEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String requestId, @org.jetbrains.annotations.Nullable()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.lang.String deviceId, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodyAlgorithm, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodyRoomId, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodySenderKey, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodySessionId) {
        super();
    }
    
    public IncomingRoomKeyRequestEntity() {
        super();
    }
}