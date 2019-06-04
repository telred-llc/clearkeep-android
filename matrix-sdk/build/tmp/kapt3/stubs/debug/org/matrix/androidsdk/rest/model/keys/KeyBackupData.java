package org.matrix.androidsdk.rest.model.keys;

import java.lang.System;

/**
 * * Backup data for one key.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0011\"\u0004\b\u0012\u0010\u0013R \u0010\u0014\u001a\u0004\u0018\u00010\u00158\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001a"}, d2 = {"Lorg/matrix/androidsdk/rest/model/keys/KeyBackupData;", "", "()V", "firstMessageIndex", "", "getFirstMessageIndex", "()J", "setFirstMessageIndex", "(J)V", "forwardedCount", "", "getForwardedCount", "()I", "setForwardedCount", "(I)V", "isVerified", "", "()Z", "setVerified", "(Z)V", "sessionData", "Lcom/google/gson/JsonElement;", "getSessionData", "()Lcom/google/gson/JsonElement;", "setSessionData", "(Lcom/google/gson/JsonElement;)V", "matrix-sdk_debug"})
public final class KeyBackupData {
    
    /**
     * * Required. The index of the first message in the session that the key can decrypt.
     */
    @com.google.gson.annotations.SerializedName(value = "first_message_index")
    private long firstMessageIndex;
    
    /**
     * * Required. The number of times this key has been forwarded.
     */
    @com.google.gson.annotations.SerializedName(value = "forwarded_count")
    private int forwardedCount;
    
    /**
     * * Whether the device backing up the key has verified the device that the key is from.
     */
    @com.google.gson.annotations.SerializedName(value = "is_verified")
    private boolean isVerified;
    
    /**
     * * Algorithm-dependent data.
     */
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "session_data")
    private com.google.gson.JsonElement sessionData;
    
    public final long getFirstMessageIndex() {
        return 0L;
    }
    
    public final void setFirstMessageIndex(long p0) {
    }
    
    public final int getForwardedCount() {
        return 0;
    }
    
    public final void setForwardedCount(int p0) {
    }
    
    public final boolean isVerified() {
        return false;
    }
    
    public final void setVerified(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.gson.JsonElement getSessionData() {
        return null;
    }
    
    public final void setSessionData(@org.jetbrains.annotations.Nullable()
    com.google.gson.JsonElement p0) {
    }
    
    public KeyBackupData() {
        super();
    }
}