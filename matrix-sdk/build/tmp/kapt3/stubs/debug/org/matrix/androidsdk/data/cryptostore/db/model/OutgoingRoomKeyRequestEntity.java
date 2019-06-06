package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0010 \n\u0002\u0010%\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u00002\u00020\u0001Bc\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u001c\u0010!\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030#\u0018\u00010\"H\u0002J\"\u0010$\u001a\u00020%2\u001a\u0010&\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030#\u0018\u00010\"J\u0010\u0010\'\u001a\u00020%2\b\u0010(\u001a\u0004\u0018\u00010)J\u0006\u0010*\u001a\u00020+R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u000e\"\u0004\b\u001a\u0010\u0010R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 \u00a8\u0006,"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/OutgoingRoomKeyRequestEntity;", "Lio/realm/RealmObject;", "requestId", "", "cancellationTxnId", "recipientsData", "requestBodyAlgorithm", "requestBodyRoomId", "requestBodySenderKey", "requestBodySessionId", "state", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getCancellationTxnId", "()Ljava/lang/String;", "setCancellationTxnId", "(Ljava/lang/String;)V", "getRecipientsData", "setRecipientsData", "getRequestBodyAlgorithm", "setRequestBodyAlgorithm", "getRequestBodyRoomId", "setRequestBodyRoomId", "getRequestBodySenderKey", "setRequestBodySenderKey", "getRequestBodySessionId", "setRequestBodySessionId", "getRequestId", "setRequestId", "getState", "()I", "setState", "(I)V", "getRecipients", "", "", "putRecipients", "", "recipients", "putRequestBody", "requestBody", "Lorg/matrix/androidsdk/rest/model/crypto/RoomKeyRequestBody;", "toOutgoingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest;", "matrix-sdk_debug"})
public class OutgoingRoomKeyRequestEntity extends io.realm.RealmObject {
    @org.jetbrains.annotations.Nullable()
    @io.realm.annotations.PrimaryKey()
    private java.lang.String requestId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String cancellationTxnId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String recipientsData;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodyAlgorithm;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodyRoomId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodySenderKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String requestBodySessionId;
    private int state;
    
    /**
     * * Convert to OutgoingRoomKeyRequest
     */
    @org.jetbrains.annotations.NotNull()
    public final org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest toOutgoingRoomKeyRequest() {
        return null;
    }
    
    private final java.util.List<java.util.Map<java.lang.String, java.lang.String>> getRecipients() {
        return null;
    }
    
    public final void putRecipients(@org.jetbrains.annotations.Nullable()
    java.util.List<? extends java.util.Map<java.lang.String, java.lang.String>> recipients) {
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
    public final java.lang.String getCancellationTxnId() {
        return null;
    }
    
    public final void setCancellationTxnId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRecipientsData() {
        return null;
    }
    
    public final void setRecipientsData(@org.jetbrains.annotations.Nullable()
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
    
    public final int getState() {
        return 0;
    }
    
    public final void setState(int p0) {
    }
    
    public OutgoingRoomKeyRequestEntity(@org.jetbrains.annotations.Nullable()
    java.lang.String requestId, @org.jetbrains.annotations.Nullable()
    java.lang.String cancellationTxnId, @org.jetbrains.annotations.Nullable()
    java.lang.String recipientsData, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodyAlgorithm, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodyRoomId, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodySenderKey, @org.jetbrains.annotations.Nullable()
    java.lang.String requestBodySessionId, int state) {
        super();
    }
    
    public OutgoingRoomKeyRequestEntity() {
        super();
    }
}