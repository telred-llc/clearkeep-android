package org.matrix.androidsdk.data.cryptostore.db;

import java.lang.System;

/**
 * * This class migrate the legacy FileCryptoStore to the Realm DB
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0013\u0010\t\u001a\u00020\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0096\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0019\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u001a\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/CryptoFileStoreImporter;", "Lio/realm/Realm$Transaction;", "enableFileEncryption", "", "context", "Landroid/content/Context;", "credentials", "Lorg/matrix/androidsdk/rest/model/login/Credentials;", "(ZLandroid/content/Context;Lorg/matrix/androidsdk/rest/model/login/Credentials;)V", "equals", "other", "", "execute", "", "realm", "Lio/realm/Realm;", "hashCode", "", "importInboundGroupSessions", "fileCryptoStore", "Lorg/matrix/androidsdk/data/cryptostore/MXFileCryptoStore;", "importIncomingRoomKeyRequests", "importMetaData", "importOlmSessions", "importOutgoingRoomKeyRequests", "importRooms", "importUsers", "Companion", "matrix-sdk_debug"})
public final class CryptoFileStoreImporter implements io.realm.Realm.Transaction {
    private final boolean enableFileEncryption = false;
    private final android.content.Context context = null;
    private final org.matrix.androidsdk.rest.model.login.Credentials credentials = null;
    private static final java.lang.String LOG_TAG = "CryptoFileStoreImporter";
    public static final org.matrix.androidsdk.data.cryptostore.db.CryptoFileStoreImporter.Companion Companion = null;
    
    @java.lang.Override()
    public void execute(@org.jetbrains.annotations.NotNull()
    io.realm.Realm realm) {
    }
    
    private final void importMetaData(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importRooms(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importUsers(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importOutgoingRoomKeyRequests(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importIncomingRoomKeyRequests(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importOlmSessions(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    private final void importInboundGroupSessions(org.matrix.androidsdk.data.cryptostore.MXFileCryptoStore fileCryptoStore, io.realm.Realm realm) {
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    public CryptoFileStoreImporter(boolean enableFileEncryption, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    org.matrix.androidsdk.rest.model.login.Credentials credentials) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/CryptoFileStoreImporter$Companion;", "", "()V", "LOG_TAG", "", "matrix-sdk_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}