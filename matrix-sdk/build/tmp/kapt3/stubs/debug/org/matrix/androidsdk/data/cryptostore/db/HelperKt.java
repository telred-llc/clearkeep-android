package org.matrix.androidsdk.data.cryptostore.db;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000:\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u001d\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004\u001a5\u0010\u0005\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u0001H\u00010\n\u00a2\u0006\u0002\u0010\f\u001a8\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00010\u000e\"\b\b\u0000\u0010\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u000e0\n\u001a\"\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00100\n\u001a-\u0010\u0011\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002H\u00010\n\u00a2\u0006\u0002\u0010\u0012\u001a\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015\u001a\n\u0010\u0016\u001a\u00020\u0003*\u00020\u0003\u00a8\u0006\u0017"}, d2 = {"deserializeFromRealm", "T", "string", "", "(Ljava/lang/String;)Ljava/lang/Object;", "doRealmQueryAndCopy", "Lio/realm/RealmObject;", "realmConfiguration", "Lio/realm/RealmConfiguration;", "action", "Lkotlin/Function1;", "Lio/realm/Realm;", "(Lio/realm/RealmConfiguration;Lkotlin/jvm/functions/Function1;)Lio/realm/RealmObject;", "doRealmQueryAndCopyList", "", "doRealmTransaction", "", "doWithRealm", "(Lio/realm/RealmConfiguration;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "serializeForRealm", "o", "", "hash", "matrix-sdk_debug"})
public final class HelperKt {
    
    /**
     * * Compute a Hash of a String, using md5 algorithm
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String hash(@org.jetbrains.annotations.NotNull()
    java.lang.String $receiver) {
        return null;
    }
    
    /**
     * * Get realm, invoke the action, close realm, and return the result of the action
     */
    public static final <T extends java.lang.Object>T doWithRealm(@org.jetbrains.annotations.NotNull()
    io.realm.RealmConfiguration realmConfiguration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super io.realm.Realm, ? extends T> action) {
        return null;
    }
    
    /**
     * * Get realm, do the query, copy from realm, close realm, and return the copied result
     */
    @org.jetbrains.annotations.Nullable()
    public static final <T extends io.realm.RealmObject>T doRealmQueryAndCopy(@org.jetbrains.annotations.NotNull()
    io.realm.RealmConfiguration realmConfiguration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super io.realm.Realm, ? extends T> action) {
        return null;
    }
    
    /**
     * * Get realm, do the list query, copy from realm, close realm, and return the copied result
     */
    @org.jetbrains.annotations.NotNull()
    public static final <T extends io.realm.RealmObject>java.lang.Iterable<T> doRealmQueryAndCopyList(@org.jetbrains.annotations.NotNull()
    io.realm.RealmConfiguration realmConfiguration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super io.realm.Realm, ? extends java.lang.Iterable<? extends T>> action) {
        return null;
    }
    
    /**
     * * Get realm instance, invoke the action in a transaction and close realm
     */
    public static final void doRealmTransaction(@org.jetbrains.annotations.NotNull()
    io.realm.RealmConfiguration realmConfiguration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super io.realm.Realm, kotlin.Unit> action) {
    }
    
    /**
     * * Serialize any Serializable object, zip it and convert to Base64 String
     */
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.String serializeForRealm(@org.jetbrains.annotations.Nullable()
    java.lang.Object o) {
        return null;
    }
    
    /**
     * * Do the opposite of serializeForRealm.
     */
    @org.jetbrains.annotations.Nullable()
    public static final <T extends java.lang.Object>T deserializeFromRealm(@org.jetbrains.annotations.Nullable()
    java.lang.String string) {
        return null;
    }
}