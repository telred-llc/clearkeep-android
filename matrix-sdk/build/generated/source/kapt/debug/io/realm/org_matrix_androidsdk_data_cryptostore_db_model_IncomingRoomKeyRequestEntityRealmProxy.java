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
public class org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface {

    static final class IncomingRoomKeyRequestEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long requestIdIndex;
        long userIdIndex;
        long deviceIdIndex;
        long requestBodyAlgorithmIndex;
        long requestBodyRoomIdIndex;
        long requestBodySenderKeyIndex;
        long requestBodySessionIdIndex;

        IncomingRoomKeyRequestEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(7);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("IncomingRoomKeyRequestEntity");
            this.requestIdIndex = addColumnDetails("requestId", "requestId", objectSchemaInfo);
            this.userIdIndex = addColumnDetails("userId", "userId", objectSchemaInfo);
            this.deviceIdIndex = addColumnDetails("deviceId", "deviceId", objectSchemaInfo);
            this.requestBodyAlgorithmIndex = addColumnDetails("requestBodyAlgorithm", "requestBodyAlgorithm", objectSchemaInfo);
            this.requestBodyRoomIdIndex = addColumnDetails("requestBodyRoomId", "requestBodyRoomId", objectSchemaInfo);
            this.requestBodySenderKeyIndex = addColumnDetails("requestBodySenderKey", "requestBodySenderKey", objectSchemaInfo);
            this.requestBodySessionIdIndex = addColumnDetails("requestBodySessionId", "requestBodySessionId", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        IncomingRoomKeyRequestEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new IncomingRoomKeyRequestEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final IncomingRoomKeyRequestEntityColumnInfo src = (IncomingRoomKeyRequestEntityColumnInfo) rawSrc;
            final IncomingRoomKeyRequestEntityColumnInfo dst = (IncomingRoomKeyRequestEntityColumnInfo) rawDst;
            dst.requestIdIndex = src.requestIdIndex;
            dst.userIdIndex = src.userIdIndex;
            dst.deviceIdIndex = src.deviceIdIndex;
            dst.requestBodyAlgorithmIndex = src.requestBodyAlgorithmIndex;
            dst.requestBodyRoomIdIndex = src.requestBodyRoomIdIndex;
            dst.requestBodySenderKeyIndex = src.requestBodySenderKeyIndex;
            dst.requestBodySessionIdIndex = src.requestBodySessionIdIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private IncomingRoomKeyRequestEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$requestId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.requestIdIndex);
    }

    @Override
    public void realmSet$requestId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.requestIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.requestIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.requestIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.requestIdIndex, value);
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
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.userIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.userIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.userIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.userIdIndex, value);
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
    public String realmGet$requestBodyAlgorithm() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.requestBodyAlgorithmIndex);
    }

    @Override
    public void realmSet$requestBodyAlgorithm(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.requestBodyAlgorithmIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.requestBodyAlgorithmIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.requestBodyAlgorithmIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.requestBodyAlgorithmIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$requestBodyRoomId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.requestBodyRoomIdIndex);
    }

    @Override
    public void realmSet$requestBodyRoomId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.requestBodyRoomIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.requestBodyRoomIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.requestBodyRoomIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.requestBodyRoomIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$requestBodySenderKey() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.requestBodySenderKeyIndex);
    }

    @Override
    public void realmSet$requestBodySenderKey(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.requestBodySenderKeyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.requestBodySenderKeyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.requestBodySenderKeyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.requestBodySenderKeyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$requestBodySessionId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.requestBodySessionIdIndex);
    }

    @Override
    public void realmSet$requestBodySessionId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.requestBodySessionIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.requestBodySessionIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.requestBodySessionIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.requestBodySessionIdIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("IncomingRoomKeyRequestEntity", 7, 0);
        builder.addPersistedProperty("requestId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("userId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("deviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodyAlgorithm", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodyRoomId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodySenderKey", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodySessionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static IncomingRoomKeyRequestEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new IncomingRoomKeyRequestEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "IncomingRoomKeyRequestEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "IncomingRoomKeyRequestEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity obj = realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class, true, excludeFields);

        final org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) obj;
        if (json.has("requestId")) {
            if (json.isNull("requestId")) {
                objProxy.realmSet$requestId(null);
            } else {
                objProxy.realmSet$requestId((String) json.getString("requestId"));
            }
        }
        if (json.has("userId")) {
            if (json.isNull("userId")) {
                objProxy.realmSet$userId(null);
            } else {
                objProxy.realmSet$userId((String) json.getString("userId"));
            }
        }
        if (json.has("deviceId")) {
            if (json.isNull("deviceId")) {
                objProxy.realmSet$deviceId(null);
            } else {
                objProxy.realmSet$deviceId((String) json.getString("deviceId"));
            }
        }
        if (json.has("requestBodyAlgorithm")) {
            if (json.isNull("requestBodyAlgorithm")) {
                objProxy.realmSet$requestBodyAlgorithm(null);
            } else {
                objProxy.realmSet$requestBodyAlgorithm((String) json.getString("requestBodyAlgorithm"));
            }
        }
        if (json.has("requestBodyRoomId")) {
            if (json.isNull("requestBodyRoomId")) {
                objProxy.realmSet$requestBodyRoomId(null);
            } else {
                objProxy.realmSet$requestBodyRoomId((String) json.getString("requestBodyRoomId"));
            }
        }
        if (json.has("requestBodySenderKey")) {
            if (json.isNull("requestBodySenderKey")) {
                objProxy.realmSet$requestBodySenderKey(null);
            } else {
                objProxy.realmSet$requestBodySenderKey((String) json.getString("requestBodySenderKey"));
            }
        }
        if (json.has("requestBodySessionId")) {
            if (json.isNull("requestBodySessionId")) {
                objProxy.realmSet$requestBodySessionId(null);
            } else {
                objProxy.realmSet$requestBodySessionId((String) json.getString("requestBodySessionId"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("requestId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$requestId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$requestId(null);
                }
            } else if (name.equals("userId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$userId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$userId(null);
                }
            } else if (name.equals("deviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$deviceId(null);
                }
            } else if (name.equals("requestBodyAlgorithm")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$requestBodyAlgorithm((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$requestBodyAlgorithm(null);
                }
            } else if (name.equals("requestBodyRoomId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$requestBodyRoomId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$requestBodyRoomId(null);
                }
            } else if (name.equals("requestBodySenderKey")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$requestBodySenderKey((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$requestBodySenderKey(null);
                }
            } else if (name.equals("requestBodySessionId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$requestBodySessionId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$requestBodySessionId(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity copyOrUpdate(Realm realm, IncomingRoomKeyRequestEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity copy(Realm realm, IncomingRoomKeyRequestEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.requestIdIndex, realmObjectSource.realmGet$requestId());
        builder.addString(columnInfo.userIdIndex, realmObjectSource.realmGet$userId());
        builder.addString(columnInfo.deviceIdIndex, realmObjectSource.realmGet$deviceId());
        builder.addString(columnInfo.requestBodyAlgorithmIndex, realmObjectSource.realmGet$requestBodyAlgorithm());
        builder.addString(columnInfo.requestBodyRoomIdIndex, realmObjectSource.realmGet$requestBodyRoomId());
        builder.addString(columnInfo.requestBodySenderKeyIndex, realmObjectSource.realmGet$requestBodySenderKey());
        builder.addString(columnInfo.requestBodySessionIdIndex, realmObjectSource.realmGet$requestBodySessionId());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$requestId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
        if (realmGet$requestId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestIdIndex, rowIndex, realmGet$requestId, false);
        }
        String realmGet$userId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
        }
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        }
        String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
        }
        String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
        }
        String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
        }
        String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$requestId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
            if (realmGet$requestId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestIdIndex, rowIndex, realmGet$requestId, false);
            }
            String realmGet$userId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$userId();
            if (realmGet$userId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
            }
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            }
            String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
            if (realmGet$requestBodyAlgorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
            }
            String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
            if (realmGet$requestBodyRoomId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
            }
            String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
            if (realmGet$requestBodySenderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
            }
            String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
            if (realmGet$requestBodySessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$requestId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
        if (realmGet$requestId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestIdIndex, rowIndex, realmGet$requestId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestIdIndex, rowIndex, false);
        }
        String realmGet$userId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.userIdIndex, rowIndex, false);
        }
        String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
        }
        String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, false);
        }
        String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, false);
        }
        String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, false);
        }
        String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$requestId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
            if (realmGet$requestId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestIdIndex, rowIndex, realmGet$requestId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestIdIndex, rowIndex, false);
            }
            String realmGet$userId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$userId();
            if (realmGet$userId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.userIdIndex, rowIndex, realmGet$userId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.userIdIndex, rowIndex, false);
            }
            String realmGet$deviceId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$deviceId();
            if (realmGet$deviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, realmGet$deviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.deviceIdIndex, rowIndex, false);
            }
            String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
            if (realmGet$requestBodyAlgorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, false);
            }
            String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
            if (realmGet$requestBodyRoomId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, false);
            }
            String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
            if (realmGet$requestBodySenderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, false);
            }
            String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
            if (realmGet$requestBodySessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, false);
            }
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$requestId(realmSource.realmGet$requestId());
        unmanagedCopy.realmSet$userId(realmSource.realmGet$userId());
        unmanagedCopy.realmSet$deviceId(realmSource.realmGet$deviceId());
        unmanagedCopy.realmSet$requestBodyAlgorithm(realmSource.realmGet$requestBodyAlgorithm());
        unmanagedCopy.realmSet$requestBodyRoomId(realmSource.realmGet$requestBodyRoomId());
        unmanagedCopy.realmSet$requestBodySenderKey(realmSource.realmGet$requestBodySenderKey());
        unmanagedCopy.realmSet$requestBodySessionId(realmSource.realmGet$requestBodySessionId());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("IncomingRoomKeyRequestEntity = proxy[");
        stringBuilder.append("{requestId:");
        stringBuilder.append(realmGet$requestId() != null ? realmGet$requestId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{userId:");
        stringBuilder.append(realmGet$userId() != null ? realmGet$userId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceId:");
        stringBuilder.append(realmGet$deviceId() != null ? realmGet$deviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{requestBodyAlgorithm:");
        stringBuilder.append(realmGet$requestBodyAlgorithm() != null ? realmGet$requestBodyAlgorithm() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{requestBodyRoomId:");
        stringBuilder.append(realmGet$requestBodyRoomId() != null ? realmGet$requestBodyRoomId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{requestBodySenderKey:");
        stringBuilder.append(realmGet$requestBodySenderKey() != null ? realmGet$requestBodySenderKey() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{requestBodySessionId:");
        stringBuilder.append(realmGet$requestBodySessionId() != null ? realmGet$requestBodySessionId() : "null");
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
        org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy aIncomingRoomKeyRequestEntity = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aIncomingRoomKeyRequestEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aIncomingRoomKeyRequestEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aIncomingRoomKeyRequestEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
