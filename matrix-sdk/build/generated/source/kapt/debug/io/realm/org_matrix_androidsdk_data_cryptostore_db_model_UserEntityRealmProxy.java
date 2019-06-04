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
public class org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy extends org.matrix.androidsdk.data.cryptostore.db.model.UserEntity
    implements RealmObjectProxy, org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface {

    static final class UserEntityColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long userIdIndex;
        long devicesIndex;
        long deviceTrackingStatusIndex;

        UserEntityColumnInfo(OsSchemaInfo schemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("UserEntity");
            this.userIdIndex = addColumnDetails("userId", "userId", objectSchemaInfo);
            this.devicesIndex = addColumnDetails("devices", "devices", objectSchemaInfo);
            this.deviceTrackingStatusIndex = addColumnDetails("deviceTrackingStatus", "deviceTrackingStatus", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        UserEntityColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new UserEntityColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final UserEntityColumnInfo src = (UserEntityColumnInfo) rawSrc;
            final UserEntityColumnInfo dst = (UserEntityColumnInfo) rawDst;
            dst.userIdIndex = src.userIdIndex;
            dst.devicesIndex = src.devicesIndex;
            dst.deviceTrackingStatusIndex = src.deviceTrackingStatusIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private UserEntityColumnInfo columnInfo;
    private ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity> proxyState;
    private RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesRealmList;

    org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (UserEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<org.matrix.androidsdk.data.cryptostore.db.model.UserEntity>(this);
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
    public RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> realmGet$devices() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (devicesRealmList != null) {
            return devicesRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.devicesIndex);
            devicesRealmList = new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class, osList, proxyState.getRealm$realm());
            return devicesRealmList;
        }
    }

    @Override
    public void realmSet$devices(RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("devices")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> original = value;
                value = new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>();
                for (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.devicesIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.setRow(i, ((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        } else {
            osList.removeAll();
            if (value == null) {
                return;
            }
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$deviceTrackingStatus() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.deviceTrackingStatusIndex);
    }

    @Override
    public void realmSet$deviceTrackingStatus(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.deviceTrackingStatusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.deviceTrackingStatusIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("UserEntity", 3, 0);
        builder.addPersistedProperty("userId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("devices", RealmFieldType.LIST, "DeviceInfoEntity");
        builder.addPersistedProperty("deviceTrackingStatus", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static UserEntityColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new UserEntityColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "UserEntity";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "UserEntity";
    }

    @SuppressWarnings("cast")
    public static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        org.matrix.androidsdk.data.cryptostore.db.model.UserEntity obj = null;
        if (update) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
            UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
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
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("devices")) {
                excludeFields.add("devices");
            }
            if (json.has("userId")) {
                if (json.isNull("userId")) {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy) realm.createObjectInternal(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class, json.getString("userId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
            }
        }

        final org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) obj;
        if (json.has("devices")) {
            if (json.isNull("devices")) {
                objProxy.realmSet$devices(null);
            } else {
                objProxy.realmGet$devices().clear();
                JSONArray array = json.getJSONArray("devices");
                for (int i = 0; i < array.length(); i++) {
                    org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity item = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$devices().add(item);
                }
            }
        }
        if (json.has("deviceTrackingStatus")) {
            if (json.isNull("deviceTrackingStatus")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'deviceTrackingStatus' to null.");
            } else {
                objProxy.realmSet$deviceTrackingStatus((int) json.getInt("deviceTrackingStatus"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final org.matrix.androidsdk.data.cryptostore.db.model.UserEntity obj = new org.matrix.androidsdk.data.cryptostore.db.model.UserEntity();
        final org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface objProxy = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) obj;
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
            } else if (name.equals("devices")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$devices(null);
                } else {
                    objProxy.realmSet$devices(new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity item = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$devices().add(item);
                    }
                    reader.endArray();
                }
            } else if (name.equals("deviceTrackingStatus")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$deviceTrackingStatus((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'deviceTrackingStatus' to null.");
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

    private static org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class), false, Collections.<String>emptyList());
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy obj = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity copyOrUpdate(Realm realm, UserEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) cachedRealmObject;
        }

        org.matrix.androidsdk.data.cryptostore.db.model.UserEntity realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
            long pkColumnIndex = columnInfo.userIdIndex;
            String value = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$userId();
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
                    realmObject = new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity copy(Realm realm, UserEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) cachedRealmObject;
        }

        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) newObject;

        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.userIdIndex, realmObjectSource.realmGet$userId());
        builder.addInteger(columnInfo.deviceTrackingStatusIndex, realmObjectSource.realmGet$deviceTrackingStatus());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = realmObjectSource.realmGet$devices();
        if (devicesList != null) {
            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesRealmList = realmObjectCopy.realmGet$devices();
            devicesRealmList.clear();
            for (int i = 0; i < devicesList.size(); i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem = devicesList.get(i);
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity cachedevices = (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cache.get(devicesItem);
                if (cachedevices != null) {
                    devicesRealmList.add(cachedevices);
                } else {
                    devicesRealmList.add(org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class), devicesItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long tableNativePtr = table.getNativePtr();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$userId();
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

        RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$devices();
        if (devicesList != null) {
            OsList devicesOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.devicesIndex);
            for (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem : devicesList) {
                Long cacheItemIndexdevices = cache.get(devicesItem);
                if (cacheItemIndexdevices == null) {
                    cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, devicesItem, cache);
                }
                devicesOsList.addRow(cacheItemIndexdevices);
            }
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.deviceTrackingStatusIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$deviceTrackingStatus(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long tableNativePtr = table.getNativePtr();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.UserEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$userId();
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

            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$devices();
            if (devicesList != null) {
                OsList devicesOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.devicesIndex);
                for (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem : devicesList) {
                    Long cacheItemIndexdevices = cache.get(devicesItem);
                    if (cacheItemIndexdevices == null) {
                        cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, devicesItem, cache);
                    }
                    devicesOsList.addRow(cacheItemIndexdevices);
                }
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.deviceTrackingStatusIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$deviceTrackingStatus(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long tableNativePtr = table.getNativePtr();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$userId();
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

        OsList devicesOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.devicesIndex);
        RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$devices();
        if (devicesList != null && devicesList.size() == devicesOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = devicesList.size();
            for (int i = 0; i < objects; i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem = devicesList.get(i);
                Long cacheItemIndexdevices = cache.get(devicesItem);
                if (cacheItemIndexdevices == null) {
                    cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, devicesItem, cache);
                }
                devicesOsList.setRow(i, cacheItemIndexdevices);
            }
        } else {
            devicesOsList.removeAll();
            if (devicesList != null) {
                for (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem : devicesList) {
                    Long cacheItemIndexdevices = cache.get(devicesItem);
                    if (cacheItemIndexdevices == null) {
                        cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, devicesItem, cache);
                    }
                    devicesOsList.addRow(cacheItemIndexdevices);
                }
            }
        }

        Table.nativeSetLong(tableNativePtr, columnInfo.deviceTrackingStatusIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$deviceTrackingStatus(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long tableNativePtr = table.getNativePtr();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        long pkColumnIndex = columnInfo.userIdIndex;
        org.matrix.androidsdk.data.cryptostore.db.model.UserEntity object = null;
        while (objects.hasNext()) {
            object = (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$userId();
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

            OsList devicesOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.devicesIndex);
            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$devices();
            if (devicesList != null && devicesList.size() == devicesOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = devicesList.size();
                for (int i = 0; i < objectCount; i++) {
                    org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem = devicesList.get(i);
                    Long cacheItemIndexdevices = cache.get(devicesItem);
                    if (cacheItemIndexdevices == null) {
                        cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, devicesItem, cache);
                    }
                    devicesOsList.setRow(i, cacheItemIndexdevices);
                }
            } else {
                devicesOsList.removeAll();
                if (devicesList != null) {
                    for (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem : devicesList) {
                        Long cacheItemIndexdevices = cache.get(devicesItem);
                        if (cacheItemIndexdevices == null) {
                            cacheItemIndexdevices = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, devicesItem, cache);
                        }
                        devicesOsList.addRow(cacheItemIndexdevices);
                    }
                }
            }

            Table.nativeSetLong(tableNativePtr, columnInfo.deviceTrackingStatusIndex, rowIndex, ((org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) object).realmGet$deviceTrackingStatus(), false);
        }
    }

    public static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity createDetachedCopy(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        org.matrix.androidsdk.data.cryptostore.db.model.UserEntity unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new org.matrix.androidsdk.data.cryptostore.db.model.UserEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) cachedObject.object;
            }
            unmanagedObject = (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface unmanagedCopy = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) unmanagedObject;
        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface realmSource = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$userId(realmSource.realmGet$userId());

        // Deep copy of devices
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$devices(null);
        } else {
            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> manageddevicesList = realmSource.realmGet$devices();
            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> unmanageddevicesList = new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>();
            unmanagedCopy.realmSet$devices(unmanageddevicesList);
            int nextDepth = currentDepth + 1;
            int size = manageddevicesList.size();
            for (int i = 0; i < size; i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity item = org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createDetachedCopy(manageddevicesList.get(i), nextDepth, maxDepth, cache);
                unmanageddevicesList.add(item);
            }
        }
        unmanagedCopy.realmSet$deviceTrackingStatus(realmSource.realmGet$deviceTrackingStatus());

        return unmanagedObject;
    }

    static org.matrix.androidsdk.data.cryptostore.db.model.UserEntity update(Realm realm, UserEntityColumnInfo columnInfo, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity realmObject, org.matrix.androidsdk.data.cryptostore.db.model.UserEntity newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface realmObjectTarget = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) realmObject;
        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface realmObjectSource = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxyInterface) newObject;
        Table table = realm.getTable(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.userIdIndex, realmObjectSource.realmGet$userId());

        RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesList = realmObjectSource.realmGet$devices();
        if (devicesList != null) {
            RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity> devicesManagedCopy = new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>();
            for (int i = 0; i < devicesList.size(); i++) {
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity devicesItem = devicesList.get(i);
                org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity cachedevices = (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) cache.get(devicesItem);
                if (cachedevices != null) {
                    devicesManagedCopy.add(cachedevices);
                } else {
                    devicesManagedCopy.add(org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class), devicesItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.devicesIndex, devicesManagedCopy);
        } else {
            builder.addObjectList(columnInfo.devicesIndex, new RealmList<org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity>());
        }
        builder.addInteger(columnInfo.deviceTrackingStatusIndex, realmObjectSource.realmGet$deviceTrackingStatus());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("UserEntity = proxy[");
        stringBuilder.append("{userId:");
        stringBuilder.append(realmGet$userId() != null ? realmGet$userId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{devices:");
        stringBuilder.append("RealmList<DeviceInfoEntity>[").append(realmGet$devices().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceTrackingStatus:");
        stringBuilder.append(realmGet$deviceTrackingStatus());
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
        org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy aUserEntity = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aUserEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aUserEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aUserEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
