package org.matrix.androidsdk.listeners;

import java.lang.System;

/**
 * * Interface to send a progress info
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0006J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/listeners/StepProgressListener;", "", "onStepProgress", "", "step", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step;", "Step", "matrix-sdk_debug"})
public abstract interface StepProgressListener {
    
    /**
     * * @param step The current step, containing progress data if available. Else you should consider progress as indeterminate
     */
    public abstract void onStepProgress(@org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.listeners.StepProgressListener.Step step);
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/listeners/StepProgressListener$Step;", "", "()V", "ComputingKey", "DownloadingKey", "ImportingKey", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$ComputingKey;", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$DownloadingKey;", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$ImportingKey;", "matrix-sdk_debug"})
    public static abstract class Step {
        
        private Step() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$ComputingKey;", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step;", "progress", "", "total", "(II)V", "getProgress", "()I", "getTotal", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "matrix-sdk_debug"})
        public static final class ComputingKey extends org.matrix.androidsdk.listeners.StepProgressListener.Step {
            private final int progress = 0;
            private final int total = 0;
            
            public final int getProgress() {
                return 0;
            }
            
            public final int getTotal() {
                return 0;
            }
            
            public ComputingKey(int progress, int total) {
                super();
            }
            
            public final int component1() {
                return 0;
            }
            
            public final int component2() {
                return 0;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final org.matrix.androidsdk.listeners.StepProgressListener.Step.ComputingKey copy(int progress, int total) {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            @java.lang.Override()
            public java.lang.String toString() {
                return null;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object p0) {
                return false;
            }
        }
        
        @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$DownloadingKey;", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step;", "()V", "matrix-sdk_debug"})
        public static final class DownloadingKey extends org.matrix.androidsdk.listeners.StepProgressListener.Step {
            public static final org.matrix.androidsdk.listeners.StepProgressListener.Step.DownloadingKey INSTANCE = null;
            
            private DownloadingKey() {
                super();
            }
        }
        
        @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/listeners/StepProgressListener$Step$ImportingKey;", "Lorg/matrix/androidsdk/listeners/StepProgressListener$Step;", "progress", "", "total", "(II)V", "getProgress", "()I", "getTotal", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "matrix-sdk_debug"})
        public static final class ImportingKey extends org.matrix.androidsdk.listeners.StepProgressListener.Step {
            private final int progress = 0;
            private final int total = 0;
            
            public final int getProgress() {
                return 0;
            }
            
            public final int getTotal() {
                return 0;
            }
            
            public ImportingKey(int progress, int total) {
                super();
            }
            
            public final int component1() {
                return 0;
            }
            
            public final int component2() {
                return 0;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final org.matrix.androidsdk.listeners.StepProgressListener.Step.ImportingKey copy(int progress, int total) {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            @java.lang.Override()
            public java.lang.String toString() {
                return null;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object p0) {
                return false;
            }
        }
    }
}