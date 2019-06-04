package org.matrix.androidsdk.listeners;

import java.lang.System;

/**
 * * Interface to send a progress info
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/listeners/ProgressListener;", "", "onProgress", "", "progress", "", "total", "matrix-sdk_debug"})
public abstract interface ProgressListener {
    
    /**
     * * @param progress from 0 to total by contract
     *     * @param total
     */
    public abstract void onProgress(int progress, int total);
}