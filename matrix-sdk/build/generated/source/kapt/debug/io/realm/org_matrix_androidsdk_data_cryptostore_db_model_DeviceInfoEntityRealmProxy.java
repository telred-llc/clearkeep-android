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
import io.realm.internal.UncheckedRow;
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
public class org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface {

    static final class DeviceInfoEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long primaryKeyIndex;
        long deviceIdIndex;
        long identityKeyIndex;
        long deviceInfoDataIndex;

        DeviceInfoEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(4);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("DeviceInfoEntity");
            this.primaryKeyIndex = addColumnDetails("primaryKey", "primaryKey", objectSchemaInfo);
            this.deviceIdIndex = addColumnDetails("deviceId", "deviceId", objectSchemaInfo);
            this.identityKeyIndex = addColumnDetails("identityKey", "identityKey", objectSchemaInfo);
            this.deviceInfoDataIndex = addColumnDetails("deviceInfoData", "deviceInfoData", objectSchemaInfo);
            addBacklinkDetails(schemaInfo, "users", "UserEntity", "devices");
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        DeviceInfoEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new DeviceInfoEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final DeviceInfoEntityColumnInfo src = (DeviceInfoEntityColumnInfo) rawSrc;
            final DeviceInfoEntityColumnInfo dst = (DeviceInfoEntityColumnInfo) rawDst;
            dst.primaryKeyIndex = src.primaryKeyIndex;
            dst.deviceIdIndex = src.deviceIdIndex;
            dst.identityKeyIndex = src.identityKeyIndex;
            dst.deviceInfoDataIndex = src.deviceInfoDataIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private DeviceInfoEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> proxyState;
    private RealmResults<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity> usersBacklinks;

    org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (DeviceInfoEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$primaryKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.primaryKeyIndex);
    }

    @Override
    public void realmSet$primaryKey(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'primaryKey' cannot be changed after object was created.");
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
    public String realmGet$identityKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.identityKeyIndex);
    }

    @Override
    public void realmSet$identityKey(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.identityKeyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.identityKeyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.identityKeyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.identityKeyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$deviceInfoData() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.deviceInfoDataIndex);
    }

    @Override
    public void realmSet$deviceInfoData(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.deviceInfoDataIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.deviceInfoDataIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.deviceInfoDataIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.deviceInfoDataIndex, value);
    }

    @Override
    public RealmResults<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity> realmGet$users() {
        BaseRealm realm = proxyState.getRealm$realm();
        realm.checkIfValid();
        proxyState.getRow$realm().checkIfAttached();
        if (usersBacklinks == null) {
            usersBacklinks = RealmResults.createBacklinkResults(realm, proxyState.getRow$realm(), org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class, "devices");
        }
        return usersBacklinks;
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("DeviceInfoEntity", 4, 1);
        builder.addPersistedProperty("primaryKey", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("deviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("identityKey", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("deviceInfoData", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addComputedLinkProperty("users", "UserEntity", "devices");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static DeviceInfoEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new DeviceInfoEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "DeviceInfoEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "DeviceInfoEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
            DeviceInfoEntityColumnInfo columnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("primaryKey")) {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("primaryKey"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("primaryKey")) {
                if (json.isNull("primaryKey")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class, json.getString("primaryKey"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) obj;
        if (json.has("deviceId")) {
            if (json.isNull("deviceId")) {
                objProxy.realmSet$deviceId(null);
            } else {
                objProxy.realmSet$deviceId((String) json.getString("deviceId"));
            }
        }
        if (json.has("identityKey")) {
            if (json.isNull("identityKey")) {
                objProxy.realmSet$identityKey(null);
            } else {
                objProxy.realmSet$identityKey((String) json.getString("identityKey"));
            }
        }
        if (json.has("deviceInfoData")) {
            if (json.isNull("deviceInfoData")) {
                objProxy.realmSet$deviceInfoData(null);
            } else {
                objProxy.realmSet$deviceInfoData((String) json.getString("deviceInfoData"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("primaryKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$primaryKey((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$primaryKey(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("deviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceId(null);
                }
            } else if (name.equals("identityKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$identityKey((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$identityKey(null);
                }
            } else if (name.equals("deviceInfoData")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceInfoData((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceInfoData(null);
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

    private static org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity copyOrUpdate(Realm realm, DeviceInfoEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = table.findFirstString(pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$primaryKey());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity copy(Realm realm, DeviceInfoEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.deviceIdIndex, realmObjectSource.realmGet$deviceId());
        builder.addString(columnInfo.identityKeyIndex, realmObjectSource.realmGet$identityKey());
        builder.addString(columnInfo.deviceInfoDataIndex, realmObjectSource.realmGet$deviceInfoData());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long tableNativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo columnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        }
        String realmGet$identityKey = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$identityKey();
        if (realmGet$identityKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, realmGet$identityKey, false);
        }
        String realmGet$deviceInfoData = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceInfoData();
        if (realmGet$deviceInfoData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, realmGet$deviceInfoData, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long tableNativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo columnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            }
            String realmGet$identityKey = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$identityKey();
            if (realmGet$identityKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, realmGet$identityKey, false);
            }
            String realmGet$deviceInfoData = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceInfoData();
            if (realmGet$deviceInfoData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, realmGet$deviceInfoData, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long tableNativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo columnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
        }
        String realmGet$identityKey = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$identityKey();
        if (realmGet$identityKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, realmGet$identityKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, false);
        }
        String realmGet$deviceInfoData = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceInfoData();
        if (realmGet$deviceInfoData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, realmGet$deviceInfoData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long tableNativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo columnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
            }
            String realmGet$identityKey = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$identityKey();
            if (realmGet$identityKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, realmGet$identityKey, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.identityKeyIndex, rowIndex, false);
            }
            String realmGet$deviceInfoData = ((org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) object).realmGet$deviceInfoData();
            if (realmGet$deviceInfoData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, realmGet$deviceInfoData, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceInfoDataIndex, rowIndex, false);
            }
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$primaryKey(realmSource.realmGet$primaryKey());
        unmanagedCopy.realmSet$deviceId(realmSource.realmGet$deviceId());
        unmanagedCopy.realmSet$identityKey(realmSource.realmGet$identityKey());
        unmanagedCopy.realmSet$deviceInfoData(realmSource.realmGet$deviceInfoData());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity update(Realm realm, DeviceInfoEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.deviceIdIndex, realmObjectSource.realmGet$deviceId());
        builder.addString(columnInfo.identityKeyIndex, realmObjectSource.realmGet$identityKey());
        builder.addString(columnInfo.deviceInfoDataIndex, realmObjectSource.realmGet$deviceInfoData());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("DeviceInfoEntity = proxy[");
        stringBuilder.append("{primaryKey:");
        stringBuilder.append(realmGet$primaryKey());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceId:");
        stringBuilder.append(realmGet$deviceId() != null ? realmGet$deviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{identityKey:");
        stringBuilder.append(realmGet$identityKey() != null ? realmGet$identityKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceInfoData:");
        stringBuilder.append(realmGet$deviceInfoData() != null ? realmGet$deviceInfoData() : "null");
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
        org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy aDeviceInfoEntity = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aDeviceInfoEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aDeviceInfoEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aDeviceInfoEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
