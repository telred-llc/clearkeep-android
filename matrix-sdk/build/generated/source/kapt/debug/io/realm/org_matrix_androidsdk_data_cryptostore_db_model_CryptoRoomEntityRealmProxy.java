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
public class org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface {

    static final class CryptoRoomEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long roomIdIndex;
        long algorithmIndex;
        long blacklistUnverifiedDevicesIndex;

        CryptoRoomEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("CryptoRoomEntity");
            this.roomIdIndex = addColumnDetails("roomId", "roomId", objectSchemaInfo);
            this.algorithmIndex = addColumnDetails("algorithm", "algorithm", objectSchemaInfo);
            this.blacklistUnverifiedDevicesIndex = addColumnDetails("blacklistUnverifiedDevices", "blacklistUnverifiedDevices", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CryptoRoomEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CryptoRoomEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CryptoRoomEntityColumnInfo src = (CryptoRoomEntityColumnInfo) rawSrc;
            final CryptoRoomEntityColumnInfo dst = (CryptoRoomEntityColumnInfo) rawDst;
            dst.roomIdIndex = src.roomIdIndex;
            dst.algorithmIndex = src.algorithmIndex;
            dst.blacklistUnverifiedDevicesIndex = src.blacklistUnverifiedDevicesIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CryptoRoomEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CryptoRoomEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$roomId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.roomIdIndex);
    }

    @Override
    public void realmSet$roomId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'roomId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$algorithm() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.algorithmIndex);
    }

    @Override
    public void realmSet$algorithm(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.algorithmIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.algorithmIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.algorithmIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.algorithmIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$blacklistUnverifiedDevices() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.blacklistUnverifiedDevicesIndex);
    }

    @Override
    public void realmSet$blacklistUnverifiedDevices(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.blacklistUnverifiedDevicesIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.blacklistUnverifiedDevicesIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("CryptoRoomEntity", 3, 0);
        builder.addPersistedProperty("roomId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("algorithm", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("blacklistUnverifiedDevices", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CryptoRoomEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CryptoRoomEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "CryptoRoomEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CryptoRoomEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
            CryptoRoomEntityColumnInfo columnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
            long pkColumnIndex = columnInfo.roomIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("roomId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("roomId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("roomId")) {
                if (json.isNull("roomId")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class, json.getString("roomId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'roomId'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) obj;
        if (json.has("algorithm")) {
            if (json.isNull("algorithm")) {
                objProxy.realmSet$algorithm(null);
            } else {
                objProxy.realmSet$algorithm((String) json.getString("algorithm"));
            }
        }
        if (json.has("blacklistUnverifiedDevices")) {
            if (json.isNull("blacklistUnverifiedDevices")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'blacklistUnverifiedDevices' to null.");
            } else {
                objProxy.realmSet$blacklistUnverifiedDevices((boolean) json.getBoolean("blacklistUnverifiedDevices"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("roomId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$roomId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$roomId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("algorithm")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$algorithm((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$algorithm(null);
                }
            } else if (name.equals("blacklistUnverifiedDevices")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$blacklistUnverifiedDevices((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'blacklistUnverifiedDevices' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'roomId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity copyOrUpdate(Realm realm, CryptoRoomEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
            long pkColumnIndex = columnInfo.roomIdIndex;
            String value = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$roomId();
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
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity copy(Realm realm, CryptoRoomEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.roomIdIndex, realmObjectSource.realmGet$roomId());
        builder.addString(columnInfo.algorithmIndex, realmObjectSource.realmGet$algorithm());
        builder.addBoolean(columnInfo.blacklistUnverifiedDevicesIndex, realmObjectSource.realmGet$blacklistUnverifiedDevices());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo columnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long pkColumnIndex = columnInfo.roomIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$roomId();
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
        String realmGet$algorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$algorithm();
        if (realmGet$algorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.algorithmIndex, rowIndex, realmGet$algorithm, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.blacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$blacklistUnverifiedDevices(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo columnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long pkColumnIndex = columnInfo.roomIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$roomId();
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
            String realmGet$algorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$algorithm();
            if (realmGet$algorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.algorithmIndex, rowIndex, realmGet$algorithm, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.blacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$blacklistUnverifiedDevices(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo columnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long pkColumnIndex = columnInfo.roomIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$roomId();
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
        String realmGet$algorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$algorithm();
        if (realmGet$algorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.algorithmIndex, rowIndex, realmGet$algorithm, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.algorithmIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.blacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$blacklistUnverifiedDevices(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long tableNativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo columnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        long pkColumnIndex = columnInfo.roomIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$roomId();
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
            String realmGet$algorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$algorithm();
            if (realmGet$algorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.algorithmIndex, rowIndex, realmGet$algorithm, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.algorithmIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.blacklistUnverifiedDevicesIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) object).realmGet$blacklistUnverifiedDevices(), false);
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$roomId(realmSource.realmGet$roomId());
        unmanagedCopy.realmSet$algorithm(realmSource.realmGet$algorithm());
        unmanagedCopy.realmSet$blacklistUnverifiedDevices(realmSource.realmGet$blacklistUnverifiedDevices());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity update(Realm realm, CryptoRoomEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.roomIdIndex, realmObjectSource.realmGet$roomId());
        builder.addString(columnInfo.algorithmIndex, realmObjectSource.realmGet$algorithm());
        builder.addBoolean(columnInfo.blacklistUnverifiedDevicesIndex, realmObjectSource.realmGet$blacklistUnverifiedDevices());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("CryptoRoomEntity = proxy[");
        stringBuilder.append("{roomId:");
        stringBuilder.append(realmGet$roomId() != null ? realmGet$roomId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{algorithm:");
        stringBuilder.append(realmGet$algorithm() != null ? realmGet$algorithm() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{blacklistUnverifiedDevices:");
        stringBuilder.append(realmGet$blacklistUnverifiedDevices());
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
        org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy aCryptoRoomEntity = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCryptoRoomEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCryptoRoomEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCryptoRoomEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
