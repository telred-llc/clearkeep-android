package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface {

    static final class KeysBackupDataEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long primaryKeyIndex;
        long backupLastServerHashIndex;
        long backupLastServerNumberOfKeysIndex;

        KeysBackupDataEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("KeysBackupDataEntity");
            this.primaryKeyIndex = addColumnDetails("primaryKey", "primaryKey", objectSchemaInfo);
            this.backupLastServerHashIndex = addColumnDetails("backupLastServerHash", "backupLastServerHash", objectSchemaInfo);
            this.backupLastServerNumberOfKeysIndex = addColumnDetails("backupLastServerNumberOfKeys", "backupLastServerNumberOfKeys", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        KeysBackupDataEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new KeysBackupDataEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final KeysBackupDataEntityColumnInfo src = (KeysBackupDataEntityColumnInfo) rawSrc;
            final KeysBackupDataEntityColumnInfo dst = (KeysBackupDataEntityColumnInfo) rawDst;
            dst.primaryKeyIndex = src.primaryKeyIndex;
            dst.backupLastServerHashIndex = src.backupLastServerHashIndex;
            dst.backupLastServerNumberOfKeysIndex = src.backupLastServerNumberOfKeysIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private KeysBackupDataEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (KeysBackupDataEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$primaryKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.primaryKeyIndex);
    }

    @Override
    public void realmSet$primaryKey(int value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'primaryKey' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$backupLastServerHash() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.backupLastServerHashIndex);
    }

    @Override
    public void realmSet$backupLastServerHash(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.backupLastServerHashIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.backupLastServerHashIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.backupLastServerHashIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.backupLastServerHashIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public Integer realmGet$backupLastServerNumberOfKeys() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.backupLastServerNumberOfKeysIndex)) {
            return null;
        }
        return (int) proxyState.getRow$realm().getLong(columnInfo.backupLastServerNumberOfKeysIndex);
    }

    @Override
    public void realmSet$backupLastServerNumberOfKeys(Integer value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.backupLastServerNumberOfKeysIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setLong(columnInfo.backupLastServerNumberOfKeysIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.backupLastServerNumberOfKeysIndex);
            return;
        }
        proxyState.getRow$realm().setLong(columnInfo.backupLastServerNumberOfKeysIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("KeysBackupDataEntity", 3, 0);
        builder.addPersistedProperty("primaryKey", RealmFieldType.INTEGER, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("backupLastServerHash", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("backupLastServerNumberOfKeys", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static KeysBackupDataEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new KeysBackupDataEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "KeysBackupDataEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "KeysBackupDataEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
            KeysBackupDataEntityColumnInfo columnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("primaryKey")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("primaryKey"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("primaryKey")) {
                if (json.isNull("primaryKey")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class, json.getInt("primaryKey"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) obj;
        if (json.has("backupLastServerHash")) {
            if (json.isNull("backupLastServerHash")) {
                objProxy.realmSet$backupLastServerHash(null);
            } else {
                objProxy.realmSet$backupLastServerHash((String) json.getString("backupLastServerHash"));
            }
        }
        if (json.has("backupLastServerNumberOfKeys")) {
            if (json.isNull("backupLastServerNumberOfKeys")) {
                objProxy.realmSet$backupLastServerNumberOfKeys(null);
            } else {
                objProxy.realmSet$backupLastServerNumberOfKeys((int) json.getInt("backupLastServerNumberOfKeys"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("primaryKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$primaryKey((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'primaryKey' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("backupLastServerHash")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$backupLastServerHash((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$backupLastServerHash(null);
                }
            } else if (name.equals("backupLastServerNumberOfKeys")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$backupLastServerNumberOfKeys((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$backupLastServerNumberOfKeys(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
        }
        return realm.copyToRealm(obj);
    }

    private static org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity copyOrUpdate(Realm realm, KeysBackupDataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = table.findFirstLong(pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity copy(Realm realm, KeysBackupDataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.backupLastServerHashIndex, realmObjectSource.realmGet$backupLastServerHash());
        builder.addInteger(columnInfo.backupLastServerNumberOfKeysIndex, realmObjectSource.realmGet$backupLastServerNumberOfKeys());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long tableNativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo columnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$backupLastServerHash = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerHash();
        if (realmGet$backupLastServerHash != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, realmGet$backupLastServerHash, false);
        }
        Number realmGet$backupLastServerNumberOfKeys = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerNumberOfKeys();
        if (realmGet$backupLastServerNumberOfKeys != null) {
            Table.nativeSetLong(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, realmGet$backupLastServerNumberOfKeys.longValue(), false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long tableNativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo columnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$backupLastServerHash = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerHash();
            if (realmGet$backupLastServerHash != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, realmGet$backupLastServerHash, false);
            }
            Number realmGet$backupLastServerNumberOfKeys = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerNumberOfKeys();
            if (realmGet$backupLastServerNumberOfKeys != null) {
                Table.nativeSetLong(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, realmGet$backupLastServerNumberOfKeys.longValue(), false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long tableNativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo columnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
        }
        cache.put(object, rowIndex);
        String realmGet$backupLastServerHash = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerHash();
        if (realmGet$backupLastServerHash != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, realmGet$backupLastServerHash, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, false);
        }
        Number realmGet$backupLastServerNumberOfKeys = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerNumberOfKeys();
        if (realmGet$backupLastServerNumberOfKeys != null) {
            Table.nativeSetLong(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, realmGet$backupLastServerNumberOfKeys.longValue(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long tableNativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo columnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$primaryKey());
            }
            cache.put(object, rowIndex);
            String realmGet$backupLastServerHash = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerHash();
            if (realmGet$backupLastServerHash != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, realmGet$backupLastServerHash, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.backupLastServerHashIndex, rowIndex, false);
            }
            Number realmGet$backupLastServerNumberOfKeys = ((org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) object).realmGet$backupLastServerNumberOfKeys();
            if (realmGet$backupLastServerNumberOfKeys != null) {
                Table.nativeSetLong(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, realmGet$backupLastServerNumberOfKeys.longValue(), false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.backupLastServerNumberOfKeysIndex, rowIndex, false);
            }
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$primaryKey(realmSource.realmGet$primaryKey());
        unmanagedCopy.realmSet$backupLastServerHash(realmSource.realmGet$backupLastServerHash());
        unmanagedCopy.realmSet$backupLastServerNumberOfKeys(realmSource.realmGet$backupLastServerNumberOfKeys());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity update(Realm realm, KeysBackupDataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addInteger(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.backupLastServerHashIndex, realmObjectSource.realmGet$backupLastServerHash());
        builder.addInteger(columnInfo.backupLastServerNumberOfKeysIndex, realmObjectSource.realmGet$backupLastServerNumberOfKeys());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("KeysBackupDataEntity = proxy[");
        stringBuilder.append("{primaryKey:");
        stringBuilder.append(realmGet$primaryKey());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{backupLastServerHash:");
        stringBuilder.append(realmGet$backupLastServerHash() != null ? realmGet$backupLastServerHash() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{backupLastServerNumberOfKeys:");
        stringBuilder.append(realmGet$backupLastServerNumberOfKeys() != null ? realmGet$backupLastServerNumberOfKeys() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy aKeysBackupDataEntity = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aKeysBackupDataEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aKeysBackupDataEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aKeysBackupDataEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
