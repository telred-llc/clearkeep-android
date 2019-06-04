package org.matrix.androidsdk.crypto.keysbackup;

import java.lang.System;

/**
 * * A signature in a `KeysBackupVersionTrust` object.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrustSignature;", "", "()V", "device", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "getDevice", "()Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "setDevice", "(Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;)V", "deviceId", "", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "valid", "", "getValid", "()Z", "setValid", "(Z)V", "matrix-sdk_debug"})
public final class KeysBackupVersionTrustSignature {
    
    /**
     * * The id of the device that signed the backup version.
     */
    @org.jetbrains.annotations.Nullable()
    private java.lang.String deviceId;
    
    /**
     * * The device that signed the backup version.
     *     * Can be null if the device is not known.
     */
    @org.jetbrains.annotations.Nullable()
    private org.matrix.androidsdk.crypto.data.MXDeviceInfo device;
    
    /**
     * * Flag to indicate the signature from this device is valid.
     */
    private boolean valid;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDeviceId() {
        return null;
    }
    
    public final void setDeviceId(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.matrix.androidsdk.crypto.data.MXDeviceInfo getDevice() {
        return null;
    }
    
    public final void setDevice(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.crypto.data.MXDeviceInfo p0) {
    }
    
    public final boolean getValid() {
        return false;
    }
    
    public final void setValid(boolean p0) {
    }
    
    public KeysBackupVersionTrustSignature() {
        super();
    }
}