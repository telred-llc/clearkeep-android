package org.matrix.androidsdk.crypto;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0004"}, d2 = {"MXCRYPTO_ALGORITHM_MEGOLM", "", "MXCRYPTO_ALGORITHM_MEGOLM_BACKUP", "MXCRYPTO_ALGORITHM_OLM", "matrix-sdk_debug"})
public final class CryptoConstantsKt {
    
    /**
     * * Matrix algorithm value for olm.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MXCRYPTO_ALGORITHM_OLM = "m.olm.v1.curve25519-aes-sha2";
    
    /**
     * * Matrix algorithm value for megolm.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MXCRYPTO_ALGORITHM_MEGOLM = "m.megolm.v1.aes-sha2";
    
    /**
     * * Matrix algorithm value for megolm keys backup.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MXCRYPTO_ALGORITHM_MEGOLM_BACKUP = "m.megolm_backup.v1.curve25519-aes-sha2";
}