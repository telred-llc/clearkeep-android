package org.matrix.androidsdk.data.cryptostore.db;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/RealmCryptoStoreMigration;", "Lio/realm/RealmMigration;", "()V", "CRYPTO_STORE_SCHEMA_VERSION", "", "LOG_TAG", "", "migrate", "", "realm", "Lio/realm/DynamicRealm;", "oldVersion", "newVersion", "matrix-sdk_debug"})
public final class RealmCryptoStoreMigration implements io.realm.RealmMigration {
    private static final java.lang.String LOG_TAG = "RealmCryptoStoreMigration";
    public static final long CRYPTO_STORE_SCHEMA_VERSION = 2L;
    public static final org.matrix.androidsdk.data.cryptostore.db.RealmCryptoStoreMigration INSTANCE = null;
    
    @java.lang.Override()
    public void migrate(@org.jetbrains.annotations.NotNull()
    io.realm.DynamicRealm realm, long oldVersion, long newVersion) {
    }
    
    private RealmCryptoStoreMigration() {
        super();
    }
}