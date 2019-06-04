package org.matrix.androidsdk.data.cryptostore.db.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\b\u0016\u0018\u00002\u00020\u0001B\'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0007R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001e\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/data/cryptostore/db/model/KeysBackupDataEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "backupLastServerHash", "", "backupLastServerNumberOfKeys", "(ILjava/lang/String;Ljava/lang/Integer;)V", "getBackupLastServerHash", "()Ljava/lang/String;", "setBackupLastServerHash", "(Ljava/lang/String;)V", "getBackupLastServerNumberOfKeys", "()Ljava/lang/Integer;", "setBackupLastServerNumberOfKeys", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getPrimaryKey", "()I", "setPrimaryKey", "(I)V", "matrix-sdk_debug"})
public class KeysBackupDataEntity extends io.realm.RealmObject {
    @io.realm.annotations.PrimaryKey()
    private int primaryKey;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String backupLastServerHash;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer backupLastServerNumberOfKeys;
    
    public final int getPrimaryKey() {
        return 0;
    }
    
    public final void setPrimaryKey(int p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBackupLastServerHash() {
        return null;
    }
    
    public final void setBackupLastServerHash(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getBackupLastServerNumberOfKeys() {
        return null;
    }
    
    public final void setBackupLastServerNumberOfKeys(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    public KeysBackupDataEntity(int primaryKey, @org.jetbrains.annotations.Nullable()
    java.lang.String backupLastServerHash, @org.jetbrains.annotations.Nullable()
    java.lang.Integer backupLastServerNumberOfKeys) {
        super();
    }
    
    public KeysBackupDataEntity() {
        super();
    }
}