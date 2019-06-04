package io.realm;


import android.util.JsonReader;
import io.realm.ImportFlag;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class RealmCryptoStoreModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>(9);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
        modelClasses.add(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        Map<Class<? extends RealmModel>, OsObjectSchemaInfo> infoMap = new HashMap<Class<? extends RealmModel>, OsObjectSchemaInfo>(9);
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class, io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.getExpectedObjectSchemaInfo());
        return infoMap;
    }

    @Override
    public ColumnInfo createColumnInfo(Class<? extends RealmModel> clazz, OsSchemaInfo schemaInfo) {
        checkClass(clazz);

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            return io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createColumnInfo(schemaInfo);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public String getSimpleClassNameImpl(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            return "KeysBackupDataEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            return "IncomingRoomKeyRequestEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            return "CryptoMetadataEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            return "CryptoRoomEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            return "DeviceInfoEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            return "OlmSessionEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            return "UserEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            return "OlmInboundGroupSessionEntity";
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            return "OutgoingRoomKeyRequestEntity";
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy());
            }
            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
                return clazz.cast(new io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy());
            }
            throw getMissingProxyClassException(clazz);
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.KeysBackupDataEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.IncomingRoomKeyRequestEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.CryptoMetadataEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.CryptoRoomEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.DeviceInfoEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.OlmSessionEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.UserEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.UserEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.OlmInboundGroupSessionEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) obj, update, cache, flags));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.OutgoingRoomKeyRequestEntityColumnInfo columnInfo = (org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class);
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.copyOrUpdate(realm, columnInfo, (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) obj, update, cache, flags));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) object, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) obj, cache);
        } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) object, cache);
            } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
                io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
                    io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.KeysBackupDataEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.IncomingRoomKeyRequestEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.CryptoMetadataEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_CryptoRoomEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.CryptoRoomEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_DeviceInfoEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.DeviceInfoEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmSessionEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.OlmSessionEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.UserEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_UserEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.UserEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.OlmInboundGroupSessionEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity.class)) {
            return clazz.cast(io.realm.org_matrix_androidsdk_data_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createDetachedCopy((org.matrix.androidsdk.data.cryptostore.db.model.OutgoingRoomKeyRequestEntity) realmObject, 0, maxDepth, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

}
