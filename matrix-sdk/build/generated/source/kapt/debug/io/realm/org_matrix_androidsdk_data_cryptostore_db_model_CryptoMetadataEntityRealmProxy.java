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
public class org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface {

    static final class CryptoMetadataEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long userIdIndex;
        long deviceIdIndex;
        long olmAccountDataIndex;
        long deviceSyncTokenIndex;
        long globalBlacklistUnverifiedDevicesIndex;
        long backupVersionIndex;

        CryptoMetadataEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("CryptoMetadataEntity");
            this.userIdIndex = addColumnDetails("userId", "userId", objectSchemaInfo);
            this.deviceIdIndex = addColumnDetails("deviceId", "deviceId", objectSchemaInfo);
            this.olmAccountDataIndex = addColumnDetails("olmAccountData", "olmAccountData", objectSchemaInfo);
            this.deviceSyncTokenIndex = addColumnDetails("deviceSyncToken", "deviceSyncToken", objectSchemaInfo);
            this.globalBlacklistUnverifiedDevicesIndex = addColumnDetails("globalBlacklistUnverifiedDevices", "globalBlacklistUnverifiedDevices", objectSchemaInfo);
            this.backupVersionIndex = addColumnDetails("backupVersion", "backupVersion", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CryptoMetadataEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CryptoMetadataEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CryptoMetadataEntityColumnInfo src = (CryptoMetadataEntityColumnInfo) rawSrc;
            final CryptoMetadataEntityColumnInfo dst = (CryptoMetadataEntityColumnInfo) rawDst;
            dst.userIdIndex = src.userIdIndex;
            dst.deviceIdIndex = src.deviceIdIndex;
            dst.olmAccountDataIndex = src.olmAccountDataIndex;
            dst.deviceSyncTokenIndex = src.deviceSyncTokenIndex;
            dst.globalBlacklistUnverifiedDevicesIndex = src.globalBlacklistUnverifiedDevicesIndex;
            dst.backupVersionIndex = src.backupVersionIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CryptoMetadataEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CryptoMetadataEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$userId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.userIdIndex);
    }

    @Override
    public void realmSet$userId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'userId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$deviceId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.deviceIdIndex);
    }

    @Override
    public void realmSet$deviceId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.deviceIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.deviceIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.deviceIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.deviceIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$olmAccountData() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.olmAccountDataIndex);
    }

    @Override
    public void realmSet$olmAccountData(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.olmAccountDataIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.olmAccountDataIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.olmAccountDataIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.olmAccountDataIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$deviceSyncToken() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.deviceSyncTokenIndex);
    }

    @Override
    public void realmSet$deviceSyncToken(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.deviceSyncTokenIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.deviceSyncTokenIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.deviceSyncTokenIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.deviceSyncTokenIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$globalBlacklistUnverifiedDevices() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.globalBlacklistUnverifiedDevicesIndex);
    }

    @Override
    public void realmSet$globalBlacklistUnverifiedDevices(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.globalBlacklistUnverifiedDevicesIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.globalBlacklistUnverifiedDevicesIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$backupVersion() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.backupVersionIndex);
    }

    @Override
    public void realmSet$backupVersion(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.backupVersionIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.backupVersionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.backupVersionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.backupVersionIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("CryptoMetadataEntity", 6, 0);
        builder.addPersistedProperty("userId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("deviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("olmAccountData", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("deviceSyncToken", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("globalBlacklistUnverifiedDevices", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("backupVersion", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CryptoMetadataEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CryptoMetadataEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "CryptoMetadataEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CryptoMetadataEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
            CryptoMetadataEntityColumnInfo columnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
            long pkColumnIndex = columnInfo.userIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("userId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("userId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("userId")) {
                if (json.isNull("userId")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class, json.getString("userId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) obj;
        if (json.has("deviceId")) {
            if (json.isNull("deviceId")) {
                objProxy.realmSet$deviceId(null);
            } else {
                objProxy.realmSet$deviceId((String) json.getString("deviceId"));
            }
        }
        if (json.has("olmAccountData")) {
            if (json.isNull("olmAccountData")) {
                objProxy.realmSet$olmAccountData(null);
            } else {
                objProxy.realmSet$olmAccountData((String) json.getString("olmAccountData"));
            }
        }
        if (json.has("deviceSyncToken")) {
            if (json.isNull("deviceSyncToken")) {
                objProxy.realmSet$deviceSyncToken(null);
            } else {
                objProxy.realmSet$deviceSyncToken((String) json.getString("deviceSyncToken"));
            }
        }
        if (json.has("globalBlacklistUnverifiedDevices")) {
            if (json.isNull("globalBlacklistUnverifiedDevices")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'globalBlacklistUnverifiedDevices' to null.");
            } else {
                objProxy.realmSet$globalBlacklistUnverifiedDevices((boolean) json.getBoolean("globalBlacklistUnverifiedDevices"));
            }
        }
        if (json.has("backupVersion")) {
            if (json.isNull("backupVersion")) {
                objProxy.realmSet$backupVersion(null);
            } else {
                objProxy.realmSet$backupVersion((String) json.getString("backupVersion"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("userId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$userId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$userId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("deviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceId(null);
                }
            } else if (name.equals("olmAccountData")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$olmAccountData((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$olmAccountData(null);
                }
            } else if (name.equals("deviceSyncToken")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceSyncToken((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceSyncToken(null);
                }
            } else if (name.equals("globalBlacklistUnverifiedDevices")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$globalBlacklistUnverifiedDevices((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'globalBlacklistUnverifiedDevices' to null.");
                }
            } else if (name.equals("backupVersion")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$backupVersion((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$backupVersion(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity copyOrUpdate(Realm realm, CryptoMetadataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
            long pkColumnIndex = columnInfo.userIdIndex;
            String value = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$userId();
            long rowIndex = Table.NO_MATCH;
            if (value == null) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, value);
            }
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity copy(Realm realm, CryptoMetadataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.userIdIndex, realmObjectSource.realmGet$userId());
        builder.addString(columnInfo.deviceIdIndex, realmObjectSource.realmGet$deviceId());
        builder.addString(columnInfo.olmAccountDataIndex, realmObjectSource.realmGet$olmAccountData());
        builder.addString(columnInfo.deviceSyncTokenIndex, realmObjectSource.realmGet$deviceSyncToken());
        builder.addBoolean(columnInfo.globalBlacklistUnverifiedDevicesIndex, realmObjectSource.realmGet$globalBlacklistUnverifiedDevices());
        builder.addString(columnInfo.backupVersionIndex, realmObjectSource.realmGet$backupVersion());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo columnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$userId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        }
        String realmGet$olmAccountData = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$olmAccountData();
        if (realmGet$olmAccountData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, realmGet$olmAccountData, false);
        }
        String realmGet$deviceSyncToken = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceSyncToken();
        if (realmGet$deviceSyncToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, realmGet$deviceSyncToken, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.globalBlacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$globalBlacklistUnverifiedDevices(), false);
        String realmGet$backupVersion = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$backupVersion();
        if (realmGet$backupVersion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, realmGet$backupVersion, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo columnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$userId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            }
            String realmGet$olmAccountData = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$olmAccountData();
            if (realmGet$olmAccountData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, realmGet$olmAccountData, false);
            }
            String realmGet$deviceSyncToken = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceSyncToken();
            if (realmGet$deviceSyncToken != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, realmGet$deviceSyncToken, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.globalBlacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$globalBlacklistUnverifiedDevices(), false);
            String realmGet$backupVersion = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$backupVersion();
            if (realmGet$backupVersion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, realmGet$backupVersion, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo columnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$userId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
        }
        String realmGet$olmAccountData = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$olmAccountData();
        if (realmGet$olmAccountData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, realmGet$olmAccountData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, false);
        }
        String realmGet$deviceSyncToken = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceSyncToken();
        if (realmGet$deviceSyncToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, realmGet$deviceSyncToken, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.globalBlacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$globalBlacklistUnverifiedDevices(), false);
        String realmGet$backupVersion = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$backupVersion();
        if (realmGet$backupVersion != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, realmGet$backupVersion, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo columnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$userId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
            }
            String realmGet$olmAccountData = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$olmAccountData();
            if (realmGet$olmAccountData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, realmGet$olmAccountData, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.olmAccountDataIndex, rowIndex, false);
            }
            String realmGet$deviceSyncToken = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$deviceSyncToken();
            if (realmGet$deviceSyncToken != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, realmGet$deviceSyncToken, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceSyncTokenIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.globalBlacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$globalBlacklistUnverifiedDevices(), false);
            String realmGet$backupVersion = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) object).realmGet$backupVersion();
            if (realmGet$backupVersion != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, realmGet$backupVersion, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.backupVersionIndex, rowIndex, false);
            }
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$userId(realmSource.realmGet$userId());
        unmanagedCopy.realmSet$deviceId(realmSource.realmGet$deviceId());
        unmanagedCopy.realmSet$olmAccountData(realmSource.realmGet$olmAccountData());
        unmanagedCopy.realmSet$deviceSyncToken(realmSource.realmGet$deviceSyncToken());
        unmanagedCopy.realmSet$globalBlacklistUnverifiedDevices(realmSource.realmGet$globalBlacklistUnverifiedDevices());
        unmanagedCopy.realmSet$backupVersion(realmSource.realmGet$backupVersion());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity update(Realm realm, CryptoMetadataEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.userIdIndex, realmObjectSource.realmGet$userId());
        builder.addString(columnInfo.deviceIdIndex, realmObjectSource.realmGet$deviceId());
        builder.addString(columnInfo.olmAccountDataIndex, realmObjectSource.realmGet$olmAccountData());
        builder.addString(columnInfo.deviceSyncTokenIndex, realmObjectSource.realmGet$deviceSyncToken());
        builder.addBoolean(columnInfo.globalBlacklistUnverifiedDevicesIndex, realmObjectSource.realmGet$globalBlacklistUnverifiedDevices());
        builder.addString(columnInfo.backupVersionIndex, realmObjectSource.realmGet$backupVersion());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("CryptoMetadataEntity = proxy[");
        stringBuilder.append("{userId:");
        stringBuilder.append(realmGet$userId() != null ? realmGet$userId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceId:");
        stringBuilder.append(realmGet$deviceId() != null ? realmGet$deviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{olmAccountData:");
        stringBuilder.append(realmGet$olmAccountData() != null ? realmGet$olmAccountData() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceSyncToken:");
        stringBuilder.append(realmGet$deviceSyncToken() != null ? realmGet$deviceSyncToken() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{globalBlacklistUnverifiedDevices:");
        stringBuilder.append(realmGet$globalBlacklistUnverifiedDevices());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{backupVersion:");
        stringBuilder.append(realmGet$backupVersion() != null ? realmGet$backupVersion() : "null");
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
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy aCryptoMetadataEntity = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCryptoMetadataEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCryptoMetadataEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCryptoMetadataEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
