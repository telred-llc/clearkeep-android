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
public class org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface {

    static final class OlmInboundGroupSessionEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long primaryKeyIndex;
        long sessionIdIndex;
        long senderKeyIndex;
        long olmInboundGroupSessionDataIndex;
        long backedUpIndex;

        OlmInboundGroupSessionEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("OlmInboundGroupSessionEntity");
            this.primaryKeyIndex = addColumnDetails("primaryKey", "primaryKey", objectSchemaInfo);
            this.sessionIdIndex = addColumnDetails("sessionId", "sessionId", objectSchemaInfo);
            this.senderKeyIndex = addColumnDetails("senderKey", "senderKey", objectSchemaInfo);
            this.olmInboundGroupSessionDataIndex = addColumnDetails("olmInboundGroupSessionData", "olmInboundGroupSessionData", objectSchemaInfo);
            this.backedUpIndex = addColumnDetails("backedUp", "backedUp", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OlmInboundGroupSessionEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new OlmInboundGroupSessionEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final OlmInboundGroupSessionEntityColumnInfo src = (OlmInboundGroupSessionEntityColumnInfo) rawSrc;
            final OlmInboundGroupSessionEntityColumnInfo dst = (OlmInboundGroupSessionEntityColumnInfo) rawDst;
            dst.primaryKeyIndex = src.primaryKeyIndex;
            dst.sessionIdIndex = src.sessionIdIndex;
            dst.senderKeyIndex = src.senderKeyIndex;
            dst.olmInboundGroupSessionDataIndex = src.olmInboundGroupSessionDataIndex;
            dst.backedUpIndex = src.backedUpIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private OlmInboundGroupSessionEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (OlmInboundGroupSessionEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity>(this);
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
    public String realmGet$senderKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderKeyIndex);
    }

    @Override
    public void realmSet$senderKey(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderKeyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderKeyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderKeyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderKeyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$olmInboundGroupSessionData() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.olmInboundGroupSessionDataIndex);
    }

    @Override
    public void realmSet$olmInboundGroupSessionData(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.olmInboundGroupSessionDataIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.olmInboundGroupSessionDataIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.olmInboundGroupSessionDataIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.olmInboundGroupSessionDataIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$backedUp() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.backedUpIndex);
    }

    @Override
    public void realmSet$backedUp(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.backedUpIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.backedUpIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("OlmInboundGroupSessionEntity", 5, 0);
        builder.addPersistedProperty("primaryKey", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("sessionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderKey", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("olmInboundGroupSessionData", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("backedUp", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OlmInboundGroupSessionEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new OlmInboundGroupSessionEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "OlmInboundGroupSessionEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OlmInboundGroupSessionEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
            OlmInboundGroupSessionEntityColumnInfo columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("primaryKey")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("primaryKey"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("primaryKey")) {
                if (json.isNull("primaryKey")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class, json.getString("primaryKey"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) obj;
        if (json.has("sessionId")) {
            if (json.isNull("sessionId")) {
                objProxy.realmSet$sessionId(null);
            } else {
                objProxy.realmSet$sessionId((String) json.getString("sessionId"));
            }
        }
        if (json.has("senderKey")) {
            if (json.isNull("senderKey")) {
                objProxy.realmSet$senderKey(null);
            } else {
                objProxy.realmSet$senderKey((String) json.getString("senderKey"));
            }
        }
        if (json.has("olmInboundGroupSessionData")) {
            if (json.isNull("olmInboundGroupSessionData")) {
                objProxy.realmSet$olmInboundGroupSessionData(null);
            } else {
                objProxy.realmSet$olmInboundGroupSessionData((String) json.getString("olmInboundGroupSessionData"));
            }
        }
        if (json.has("backedUp")) {
            if (json.isNull("backedUp")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'backedUp' to null.");
            } else {
                objProxy.realmSet$backedUp((boolean) json.getBoolean("backedUp"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) obj;
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
            } else if (name.equals("senderKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderKey((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderKey(null);
                }
            } else if (name.equals("olmInboundGroupSessionData")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$olmInboundGroupSessionData((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$olmInboundGroupSessionData(null);
                }
            } else if (name.equals("backedUp")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$backedUp((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'backedUp' to null.");
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

    private static org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity copyOrUpdate(Realm realm, OlmInboundGroupSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
            long pkColumnIndex = columnInfo.primaryKeyIndex;
            String value = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
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
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity copy(Realm realm, OlmInboundGroupSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.sessionIdIndex, realmObjectSource.realmGet$sessionId());
        builder.addString(columnInfo.senderKeyIndex, realmObjectSource.realmGet$senderKey());
        builder.addString(columnInfo.olmInboundGroupSessionDataIndex, realmObjectSource.realmGet$olmInboundGroupSessionData());
        builder.addBoolean(columnInfo.backedUpIndex, realmObjectSource.realmGet$backedUp());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
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
        String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
        }
        String realmGet$senderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$senderKey();
        if (realmGet$senderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, realmGet$senderKey, false);
        }
        String realmGet$olmInboundGroupSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$olmInboundGroupSessionData();
        if (realmGet$olmInboundGroupSessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, realmGet$olmInboundGroupSessionData, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.backedUpIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$backedUp(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
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
            String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$sessionId();
            if (realmGet$sessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
            }
            String realmGet$senderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$senderKey();
            if (realmGet$senderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, realmGet$senderKey, false);
            }
            String realmGet$olmInboundGroupSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$olmInboundGroupSessionData();
            if (realmGet$olmInboundGroupSessionData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, realmGet$olmInboundGroupSessionData, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.backedUpIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$backedUp(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
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
        String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, false);
        }
        String realmGet$senderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$senderKey();
        if (realmGet$senderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, realmGet$senderKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, false);
        }
        String realmGet$olmInboundGroupSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$olmInboundGroupSessionData();
        if (realmGet$olmInboundGroupSessionData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, realmGet$olmInboundGroupSessionData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.backedUpIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$backedUp(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long tableNativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        long pkColumnIndex = columnInfo.primaryKeyIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$primaryKey();
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
            String realmGet$sessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$sessionId();
            if (realmGet$sessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, realmGet$sessionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.sessionIdIndex, rowIndex, false);
            }
            String realmGet$senderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$senderKey();
            if (realmGet$senderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, realmGet$senderKey, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderKeyIndex, rowIndex, false);
            }
            String realmGet$olmInboundGroupSessionData = ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$olmInboundGroupSessionData();
            if (realmGet$olmInboundGroupSessionData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, realmGet$olmInboundGroupSessionData, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.olmInboundGroupSessionDataIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.backedUpIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) object).realmGet$backedUp(), false);
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$primaryKey(realmSource.realmGet$primaryKey());
        unmanagedCopy.realmSet$sessionId(realmSource.realmGet$sessionId());
        unmanagedCopy.realmSet$senderKey(realmSource.realmGet$senderKey());
        unmanagedCopy.realmSet$olmInboundGroupSessionData(realmSource.realmGet$olmInboundGroupSessionData());
        unmanagedCopy.realmSet$backedUp(realmSource.realmGet$backedUp());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity update(Realm realm, OlmInboundGroupSessionEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.primaryKeyIndex, realmObjectSource.realmGet$primaryKey());
        builder.addString(columnInfo.sessionIdIndex, realmObjectSource.realmGet$sessionId());
        builder.addString(columnInfo.senderKeyIndex, realmObjectSource.realmGet$senderKey());
        builder.addString(columnInfo.olmInboundGroupSessionDataIndex, realmObjectSource.realmGet$olmInboundGroupSessionData());
        builder.addBoolean(columnInfo.backedUpIndex, realmObjectSource.realmGet$backedUp());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("OlmInboundGroupSessionEntity = proxy[");
        stringBuilder.append("{primaryKey:");
        stringBuilder.append(realmGet$primaryKey() != null ? realmGet$primaryKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sessionId:");
        stringBuilder.append(realmGet$sessionId() != null ? realmGet$sessionId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderKey:");
        stringBuilder.append(realmGet$senderKey() != null ? realmGet$senderKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{olmInboundGroupSessionData:");
        stringBuilder.append(realmGet$olmInboundGroupSessionData() != null ? realmGet$olmInboundGroupSessionData() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{backedUp:");
        stringBuilder.append(realmGet$backedUp());
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
        org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy aOlmInboundGroupSessionEntity = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aOlmInboundGroupSessionEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aOlmInboundGroupSessionEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aOlmInboundGroupSessionEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
