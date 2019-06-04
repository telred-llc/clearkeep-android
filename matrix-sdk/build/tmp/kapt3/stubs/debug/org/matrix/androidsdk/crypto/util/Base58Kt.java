package org.matrix.androidsdk.crypto.util;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\u001a\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0001\u001a\u000e\u0010\b\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006\u001a\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"ALPHABET", "", "BASE", "Ljava/math/BigInteger;", "kotlin.jvm.PlatformType", "base58decode", "", "input", "base58encode", "decodeToBigInteger", "matrix-sdk_debug"})
public final class Base58Kt {
    
    /**
     * * Ref: https://github.com/bitcoin-labs/bitcoin-mobile-android/blob/master/src/bitcoinj/java/com/google/bitcoin/core/Base58.java
     * *
     * *
     * * A custom form of base58 is used to encode BitCoin addresses. Note that this is not the same base58 as used by
     * * Flickr, which you may see reference to around the internet.
     * *
     * * Satoshi says: why base-58 instead of standard base-64 encoding?
     * *
     * *  * Don't want 0OIl characters that look the same in some fonts and
     * * could be used to create visually identical looking account numbers.
     * *  * A string with non-alphanumeric characters is not as easily accepted as an account number.
     * *  * E-mail usually won't line-break if there's no punctuation to break at.
     * *  * Doubleclicking selects the whole number as one word if it's all alphanumeric.
     * *
     */
    private static final java.lang.String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final java.math.BigInteger BASE = null;
    
    /**
     * * Encode a byte array to a human readable string with base58 chars
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String base58encode(@org.jetbrains.annotations.NotNull()
    byte[] input) {
        return null;
    }
    
    /**
     * * Decode a base58 String to a byte array
     */
    @org.jetbrains.annotations.NotNull()
    public static final byte[] base58decode(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    private static final java.math.BigInteger decodeToBigInteger(java.lang.String input) {
        return null;
    }
}