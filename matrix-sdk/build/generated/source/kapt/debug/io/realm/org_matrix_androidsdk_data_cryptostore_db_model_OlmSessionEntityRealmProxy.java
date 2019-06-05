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
public class org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface {

    static final class OlmSessionEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long primaryKeyIndex;
        long sessionIdIndex;
        long deviceKeyIndex;
        long olmSessionDataIndex;
        long lastReceivedMessageTsIndex;

        OlmSessionEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("OlmSessionEntity");
            this.primaryKeyIndex = addColumnDetails("primaryKey", "primaryKey", objectSchemaInfo);
            this.sessionIdIndex = addColumnDetails("sessionId", "sessionId", objectSchemaInfo);
            this.deviceKeyIndex = addColumnDetails("deviceKey", "deviceKey", objectSchemaInfo);
            this.olmSessionDataIndex = addColumnDetails("olmSessionData", "olmSessionData", objectSchemaInfo);
            this.lastReceivedMessageTsIndex = addColumnDetails("lastReceivedMessageTs", "lastReceivedMessageTs", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OlmSessionEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new OlmSessionEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final OlmSessionEntityColumnInfo src = (OlmSessionEntityColumnInfo) rawSrc;
            final OlmSessionEntityColumnInfo dst = (OlmSessionEntityColumnInfo) rawDst;
            dst.primaryKeyIndex = src.primaryKeyIndex;
            dst.sessionIdIndex = src.sessionIdIndex;
            dst.deviceKeyIndex = src.deviceKeyIndex;
            dst.olmSessionDataIndex = src.olmSessionDataIndex;
            dst.lastReceivedMessageTsIndex = src.lastReceivedMessageTsIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private OlmSessionEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (OlmSessionEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity>(this);
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
    public String realmGet$sessionId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.sessionIdIndex);
    }

    @Override
    public void realmSet$sessionId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.sessionIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.sessionIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.sessionIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.sessionIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$deviceKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.deviceKeyIndex);
    }

    @Override
    public void realmSet$deviceKey(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.deviceKeyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.deviceKeyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.deviceKeyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.deviceKeyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$olmSessionData() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.olmSessionDataIndex);
    }

    @Override
    public void realmSet$olmSessionData(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.olmSessionDataIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.olmSessionDataIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.olmSessionDataIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.olmSessionDataIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$lastReceivedMessageTs() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.lastReceivedMessageTsIndex);
    }

    @Override
    public void realmSet$lastReceivedMessageTs(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.lastReceivedMessageTsIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.lastReceivedMessageTsIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("OlmSessionEntity", 5, 0);
        builder.addPersistedProperty("primaryKey", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("sessionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("deviceKey", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("olmSessionData", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("lastReceivedMessageTs", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OlmSessionEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new OlmSessionEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "OlmSessionEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OlmSessionEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
            OlmSessionEntityColumnInfo columnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("primaryKey")) {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("primaryKey"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("primaryKey")) {
                if (json.isNull("primaryKey")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class, json.getString("primaryKey"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) obj;
        if (json.has("sessionId")) {
            if (json.isNull("sessionId")) {
                objProxy.realmSet$sessionId(null);
            } else {
                objProxy.realmSet$sessionId((String) json.getString("sessionId"));
            }
        }
        if (json.has("deviceKey")) {
            if (json.isNull("deviceKey")) {
                objProxy.realmSet$deviceKey(null);
            } else {
                objProxy.realmSet$deviceKey((String) json.getString("deviceKey"));
            }
        }
        if (json.has("olmSessionData")) {
            if (json.isNull("olmSessionData")) {
                objProxy.realmSet$olmSessionData(null);
            } else {
                objProxy.realmSet$olmSessionData((String) json.getString("olmSessionData"));
            }
        }
        if (json.has("lastReceivedMessageTs")) {
            if (json.isNull("lastReceivedMessageTs")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'lastReceivedMessageTs' to null.");
            } else {
                objProxy.realmSet$lastReceivedMessageTs((long) json.getLong("lastReceivedMessageTs"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) obj;
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
            } else if (name.equals("sessionId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$sessionId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$sessionId(null);
                }
            } else if (name.equals("deviceKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceKey((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceKey(null);
                }
            } else if (name.equals("olmSessionData")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$olmSessionData((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$olmSessionData(null);
                }
            } else if (name.equals("lastReceivedMessageTs")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lastReceivedMessageTs((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'lastReceivedMessageTs' to null.");
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

    private static org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity copyOrUpdate(Realm realm, OlmSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = table.findFirstString(pkColumnIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$primaryKey());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity copy(Realm realm, OlmSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.sessionIdIndex, realmObjectSource.realmGet$sessionId());
        builder.addString(columnInfo.deviceKeyIndex, realmObjectSource.realmGet$deviceKey());
        builder.addString(columnInfo.olmSessionDataIndex, realmObjectSource.realmGet$olmSessionData());
        builder.addInteger(columnInfo.lastReceivedMessageTsIndex, realmObjectSource.realmGet$lastReceivedMessageTs());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo columnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
        }
        String realmGet$deviceKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$deviceKey();
        if (realmGet$deviceKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, realmGet$deviceKey, false);
        }
        String realmGet$olmSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$olmSessionData();
        if (realmGet$olmSessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, realmGet$olmSessionData, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.lastReceivedMessageTsIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$lastReceivedMessageTs(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo columnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$sessionId();
            if (realmGet$sessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
            }
            String realmGet$deviceKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$deviceKey();
            if (realmGet$deviceKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, realmGet$deviceKey, false);
            }
            String realmGet$olmSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$olmSessionData();
            if (realmGet$olmSessionData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, realmGet$olmSessionData, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.lastReceivedMessageTsIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$lastReceivedMessageTs(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo columnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, false);
        }
        String realmGet$deviceKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$deviceKey();
        if (realmGet$deviceKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, realmGet$deviceKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, false);
        }
        String realmGet$olmSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$olmSessionData();
        if (realmGet$olmSessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, realmGet$olmSessionData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.lastReceivedMessageTsIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$lastReceivedMessageTs(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo columnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, (String)primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$sessionId();
            if (realmGet$sessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, false);
            }
            String realmGet$deviceKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$deviceKey();
            if (realmGet$deviceKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, realmGet$deviceKey, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceKeyIndex, rowIndex, false);
            }
            String realmGet$olmSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$olmSessionData();
            if (realmGet$olmSessionData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, realmGet$olmSessionData, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.olmSessionDataIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.lastReceivedMessageTsIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) object).realmGet$lastReceivedMessageTs(), false);
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$primaryKey(realmSource.realmGet$primaryKey());
        unmanagedCopy.realmSet$sessionId(realmSource.realmGet$sessionId());
        unmanagedCopy.realmSet$deviceKey(realmSource.realmGet$deviceKey());
        unmanagedCopy.realmSet$olmSessionData(realmSource.realmGet$olmSessionData());
        unmanagedCopy.realmSet$lastReceivedMessageTs(realmSource.realmGet$lastReceivedMessageTs());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity update(Realm realm, OlmSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.sessionIdIndex, realmObjectSource.realmGet$sessionId());
        builder.addString(columnInfo.deviceKeyIndex, realmObjectSource.realmGet$deviceKey());
        builder.addString(columnInfo.olmSessionDataIndex, realmObjectSource.realmGet$olmSessionData());
        builder.addInteger(columnInfo.lastReceivedMessageTsIndex, realmObjectSource.realmGet$lastReceivedMessageTs());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("OlmSessionEntity = proxy[");
        stringBuilder.append("{primaryKey:");
        stringBuilder.append(realmGet$primaryKey());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sessionId:");
        stringBuilder.append(realmGet$sessionId() != null ? realmGet$sessionId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceKey:");
        stringBuilder.append(realmGet$deviceKey() != null ? realmGet$deviceKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{olmSessionData:");
        stringBuilder.append(realmGet$olmSessionData() != null ? realmGet$olmSessionData() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lastReceivedMessageTs:");
        stringBuilder.append(realmGet$lastReceivedMessageTs());
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
        org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy aOlmSessionEntity = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aOlmSessionEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aOlmSessionEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aOlmSessionEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
