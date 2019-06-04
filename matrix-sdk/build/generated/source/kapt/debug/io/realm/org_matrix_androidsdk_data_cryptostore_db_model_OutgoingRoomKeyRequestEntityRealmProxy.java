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
public class org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface {

    static final class OutgoingRoomKeyRequestEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long requestIdIndex;
        long cancellationTxnIdIndex;
        long recipientsDataIndex;
        long requestBodyAlgorithmIndex;
        long requestBodyRoomIdIndex;
        long requestBodySenderKeyIndex;
        long requestBodySessionIdIndex;
        long stateIndex;

        OutgoingRoomKeyRequestEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(8);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("OutgoingRoomKeyRequestEntity");
            this.requestIdIndex = addColumnDetails("requestId", "requestId", objectSchemaInfo);
            this.cancellationTxnIdIndex = addColumnDetails("cancellationTxnId", "cancellationTxnId", objectSchemaInfo);
            this.recipientsDataIndex = addColumnDetails("recipientsData", "recipientsData", objectSchemaInfo);
            this.requestBodyAlgorithmIndex = addColumnDetails("requestBodyAlgorithm", "requestBodyAlgorithm", objectSchemaInfo);
            this.requestBodyRoomIdIndex = addColumnDetails("requestBodyRoomId", "requestBodyRoomId", objectSchemaInfo);
            this.requestBodySenderKeyIndex = addColumnDetails("requestBodySenderKey", "requestBodySenderKey", objectSchemaInfo);
            this.requestBodySessionIdIndex = addColumnDetails("requestBodySessionId", "requestBodySessionId", objectSchemaInfo);
            this.stateIndex = addColumnDetails("state", "state", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OutgoingRoomKeyRequestEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new OutgoingRoomKeyRequestEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final OutgoingRoomKeyRequestEntityColumnInfo src = (OutgoingRoomKeyRequestEntityColumnInfo) rawSrc;
            final OutgoingRoomKeyRequestEntityColumnInfo dst = (OutgoingRoomKeyRequestEntityColumnInfo) rawDst;
            dst.requestIdIndex = src.requestIdIndex;
            dst.cancellationTxnIdIndex = src.cancellationTxnIdIndex;
            dst.recipientsDataIndex = src.recipientsDataIndex;
            dst.requestBodyAlgorithmIndex = src.requestBodyAlgorithmIndex;
            dst.requestBodyRoomIdIndex = src.requestBodyRoomIdIndex;
            dst.requestBodySenderKeyIndex = src.requestBodySenderKeyIndex;
            dst.requestBodySessionIdIndex = src.requestBodySessionIdIndex;
            dst.stateIndex = src.stateIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private OutgoingRoomKeyRequestEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity> proxyState;

    org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity>(this);
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
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'requestId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cancellationTxnId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cancellationTxnIdIndex);
    }

    @Override
    public void realmSet$cancellationTxnId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cancellationTxnIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.cancellationTxnIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cancellationTxnIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cancellationTxnIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$recipientsData() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.recipientsDataIndex);
    }

    @Override
    public void realmSet$recipientsData(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.recipientsDataIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.recipientsDataIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.recipientsDataIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.recipientsDataIndex, value);
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

    @Override
    @SuppressWarnings("cast")
    public int realmGet$state() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.stateIndex);
    }

    @Override
    public void realmSet$state(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.stateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.stateIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("OutgoingRoomKeyRequestEntity", 8, 0);
        builder.addPersistedProperty("requestId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("cancellationTxnId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("recipientsData", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodyAlgorithm", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodyRoomId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodySenderKey", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("requestBodySessionId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("state", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OutgoingRoomKeyRequestEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new OutgoingRoomKeyRequestEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "OutgoingRoomKeyRequestEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OutgoingRoomKeyRequestEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
            OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
            long pkColumnIndex = columnInfo.requestIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("requestId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("requestId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("requestId")) {
                if (json.isNull("requestId")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class, json.getString("requestId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'requestId'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) obj;
        if (json.has("cancellationTxnId")) {
            if (json.isNull("cancellationTxnId")) {
                objProxy.realmSet$cancellationTxnId(null);
            } else {
                objProxy.realmSet$cancellationTxnId((String) json.getString("cancellationTxnId"));
            }
        }
        if (json.has("recipientsData")) {
            if (json.isNull("recipientsData")) {
                objProxy.realmSet$recipientsData(null);
            } else {
                objProxy.realmSet$recipientsData((String) json.getString("recipientsData"));
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
        if (json.has("state")) {
            if (json.isNull("state")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'state' to null.");
            } else {
                objProxy.realmSet$state((int) json.getInt("state"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) obj;
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
                jsonHasPrimaryKey = true;
            } else if (name.equals("cancellationTxnId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cancellationTxnId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cancellationTxnId(null);
                }
            } else if (name.equals("recipientsData")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$recipientsData((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$recipientsData(null);
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
            } else if (name.equals("state")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$state((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'state' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'requestId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity copyOrUpdate(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
            long pkColumnIndex = columnInfo.requestIdIndex;
            String value = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
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
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity copy(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.requestIdIndex, realmObjectSource.realmGet$requestId());
        builder.addString(columnInfo.cancellationTxnIdIndex, realmObjectSource.realmGet$cancellationTxnId());
        builder.addString(columnInfo.recipientsDataIndex, realmObjectSource.realmGet$recipientsData());
        builder.addString(columnInfo.requestBodyAlgorithmIndex, realmObjectSource.realmGet$requestBodyAlgorithm());
        builder.addString(columnInfo.requestBodyRoomIdIndex, realmObjectSource.realmGet$requestBodyRoomId());
        builder.addString(columnInfo.requestBodySenderKeyIndex, realmObjectSource.realmGet$requestBodySenderKey());
        builder.addString(columnInfo.requestBodySessionIdIndex, realmObjectSource.realmGet$requestBodySessionId());
        builder.addInteger(columnInfo.stateIndex, realmObjectSource.realmGet$state());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long pkColumnIndex = columnInfo.requestIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
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
        String realmGet$cancellationTxnId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$cancellationTxnId();
        if (realmGet$cancellationTxnId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, realmGet$cancellationTxnId, false);
        }
        String realmGet$recipientsData = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$recipientsData();
        if (realmGet$recipientsData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, realmGet$recipientsData, false);
        }
        String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
        }
        String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
        }
        String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
        }
        String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.stateIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$state(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long pkColumnIndex = columnInfo.requestIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
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
            String realmGet$cancellationTxnId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$cancellationTxnId();
            if (realmGet$cancellationTxnId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, realmGet$cancellationTxnId, false);
            }
            String realmGet$recipientsData = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$recipientsData();
            if (realmGet$recipientsData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, realmGet$recipientsData, false);
            }
            String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
            if (realmGet$requestBodyAlgorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
            }
            String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
            if (realmGet$requestBodyRoomId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
            }
            String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
            if (realmGet$requestBodySenderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
            }
            String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
            if (realmGet$requestBodySessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.stateIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$state(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long pkColumnIndex = columnInfo.requestIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
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
        String realmGet$cancellationTxnId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$cancellationTxnId();
        if (realmGet$cancellationTxnId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, realmGet$cancellationTxnId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, false);
        }
        String realmGet$recipientsData = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$recipientsData();
        if (realmGet$recipientsData != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, realmGet$recipientsData, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, false);
        }
        String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, false);
        }
        String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, false);
        }
        String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, false);
        }
        String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.stateIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$state(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long tableNativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        long pkColumnIndex = columnInfo.requestIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestId();
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
            String realmGet$cancellationTxnId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$cancellationTxnId();
            if (realmGet$cancellationTxnId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, realmGet$cancellationTxnId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cancellationTxnIdIndex, rowIndex, false);
            }
            String realmGet$recipientsData = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$recipientsData();
            if (realmGet$recipientsData != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, realmGet$recipientsData, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.recipientsDataIndex, rowIndex, false);
            }
            String realmGet$requestBodyAlgorithm = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyAlgorithm();
            if (realmGet$requestBodyAlgorithm != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, realmGet$requestBodyAlgorithm, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyAlgorithmIndex, rowIndex, false);
            }
            String realmGet$requestBodyRoomId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodyRoomId();
            if (realmGet$requestBodyRoomId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, realmGet$requestBodyRoomId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodyRoomIdIndex, rowIndex, false);
            }
            String realmGet$requestBodySenderKey = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySenderKey();
            if (realmGet$requestBodySenderKey != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, realmGet$requestBodySenderKey, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySenderKeyIndex, rowIndex, false);
            }
            String realmGet$requestBodySessionId = ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$requestBodySessionId();
            if (realmGet$requestBodySessionId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, realmGet$requestBodySessionId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.requestBodySessionIdIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.stateIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) object).realmGet$state(), false);
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$requestId(realmSource.realmGet$requestId());
        unmanagedCopy.realmSet$cancellationTxnId(realmSource.realmGet$cancellationTxnId());
        unmanagedCopy.realmSet$recipientsData(realmSource.realmGet$recipientsData());
        unmanagedCopy.realmSet$requestBodyAlgorithm(realmSource.realmGet$requestBodyAlgorithm());
        unmanagedCopy.realmSet$requestBodyRoomId(realmSource.realmGet$requestBodyRoomId());
        unmanagedCopy.realmSet$requestBodySenderKey(realmSource.realmGet$requestBodySenderKey());
        unmanagedCopy.realmSet$requestBodySessionId(realmSource.realmGet$requestBodySessionId());
        unmanagedCopy.realmSet$state(realmSource.realmGet$state());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity update(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.requestIdIndex, realmObjectSource.realmGet$requestId());
        builder.addString(columnInfo.cancellationTxnIdIndex, realmObjectSource.realmGet$cancellationTxnId());
        builder.addString(columnInfo.recipientsDataIndex, realmObjectSource.realmGet$recipientsData());
        builder.addString(columnInfo.requestBodyAlgorithmIndex, realmObjectSource.realmGet$requestBodyAlgorithm());
        builder.addString(columnInfo.requestBodyRoomIdIndex, realmObjectSource.realmGet$requestBodyRoomId());
        builder.addString(columnInfo.requestBodySenderKeyIndex, realmObjectSource.realmGet$requestBodySenderKey());
        builder.addString(columnInfo.requestBodySessionIdIndex, realmObjectSource.realmGet$requestBodySessionId());
        builder.addInteger(columnInfo.stateIndex, realmObjectSource.realmGet$state());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("OutgoingRoomKeyRequestEntity = proxy[");
        stringBuilder.append("{requestId:");
        stringBuilder.append(realmGet$requestId() != null ? realmGet$requestId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cancellationTxnId:");
        stringBuilder.append(realmGet$cancellationTxnId() != null ? realmGet$cancellationTxnId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{recipientsData:");
        stringBuilder.append(realmGet$recipientsData() != null ? realmGet$recipientsData() : "null");
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
        stringBuilder.append(",");
        stringBuilder.append("{state:");
        stringBuilder.append(realmGet$state());
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
        org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy aOutgoingRoomKeyRequestEntity = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aOutgoingRoomKeyRequestEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aOutgoingRoomKeyRequestEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aOutgoingRoomKeyRequestEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
