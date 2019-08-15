//package vmodev.clearkeep;
//
//import com.squareup.moshi.JsonAdapter;
//import com.squareup.moshi.JsonDataException;
//import com.squareup.moshi.JsonReader;
//import com.squareup.moshi.JsonWriter;
//import com.squareup.moshi.Moshi;
//import com.squareup.moshi.Types;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Type;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.CheckReturnValue;
//
//public final class RuntimeJsonAdapterFactory<T> implements JsonAdapter.Factory {
//    final Class<T> baseType;
//    final String labelKey;
//    final Class fallbackType;
//    final Map<String, Type> labelToType = new LinkedHashMap<>();
//
//    /**
//     * @param baseType The base type for which this factory will create adapters. Cannot be Object.
//     * @param labelKey The key in the JSON object whose value determines the type to which to map the
//     *                 JSON object.
//     */
//    @CheckReturnValue
//    public static <T> RuntimeJsonAdapterFactory<T> of(Class<T> baseType, String labelKey, Class<? extends T> fallbackType) {
//        if (baseType == null) throw new NullPointerException("baseType == null");
//        if (labelKey == null) throw new NullPointerException("labelKey == null");
//        if (baseType == Object.class) {
//            throw new IllegalArgumentException(
//                    "The base type must not be Object. Consider using a marker interface.");
//        }
//        return new RuntimeJsonAdapterFactory<>(baseType, labelKey, fallbackType);
//    }
//
//    RuntimeJsonAdapterFactory(Class<T> baseType, String labelKey, Class fallbackType) {
//        this.baseType = baseType;
//        this.labelKey = labelKey;
//        this.fallbackType = fallbackType;
//    }
//
//    /**
//     * Register the subtype that can be created based on the label. When an unknown type is found
//     * during encoding an {@linkplain IllegalArgumentException} will be thrown. When an unknown label
//     * is found during decoding a {@linkplain JsonDataException} will be thrown.
//     */
//    public RuntimeJsonAdapterFactory<T> registerSubtype(Class<? extends T> subtype, String label) {
//        if (subtype == null) throw new NullPointerException("subtype == null");
//        if (label == null) throw new NullPointerException("label == null");
//        if (labelToType.containsKey(label) || labelToType.containsValue(subtype)) {
//            throw new IllegalArgumentException("Subtypes and labels must be unique.");
//        }
//        labelToType.put(label, subtype);
//        return this;
//    }
//
//    @Override
//    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
//        if (Types.getRawType(type) != baseType || !annotations.isEmpty()) {
//            return null;
//        }
//        int size = labelToType.size();
//        Map<String, JsonAdapter<Object>> labelToAdapter = new LinkedHashMap<>(size);
//        Map<Type, String> typeToLabel = new LinkedHashMap<>(size);
//        for (Map.Entry<String, Type> entry : labelToType.entrySet()) {
//            String label = entry.getKey();
//            Type typeValue = entry.getValue();
//            typeToLabel.put(typeValue, label);
//            labelToAdapter.put(label, moshi.adapter(typeValue));
//        }
//
//        final JsonAdapter<Object> fallbackAdapter = moshi.adapter(fallbackType);
//        JsonAdapter<Object> objectJsonAdapter = moshi.adapter(Object.class);
//
//        return new RuntimeJsonAdapter(labelKey, labelToAdapter, typeToLabel,
//                objectJsonAdapter, fallbackAdapter).nullSafe();
//    }
//
//    static final class RuntimeJsonAdapter extends JsonAdapter<Object> {
//        final String labelKey;
//        final Map<String, JsonAdapter<Object>> labelToAdapter;
//        final Map<Type, String> typeToLabel;
//        final JsonAdapter<Object> objectJsonAdapter;
//        final JsonAdapter<Object> fallbackAdapter;
//
//        RuntimeJsonAdapter(String labelKey, Map<String, JsonAdapter<Object>> labelToAdapter,
//                           Map<Type, String> typeToLabel, JsonAdapter<Object> objectJsonAdapter,
//                           JsonAdapter<Object> fallbackAdapter) {
//            this.labelKey = labelKey;
//            this.labelToAdapter = labelToAdapter;
//            this.typeToLabel = typeToLabel;
//            this.objectJsonAdapter = objectJsonAdapter;
//            this.fallbackAdapter = fallbackAdapter;
//        }
//
//        @Override
//        public Object fromJson(JsonReader reader) throws IOException {
//            JsonReader.Token peekedToken = reader.peek();
//            if (peekedToken != JsonReader.Token.BEGIN_OBJECT) {
//                throw new JsonDataException("Expected BEGIN_OBJECT but was " + peekedToken
//                        + " at path " + reader.getPath());
//            }
//            Object jsonValue = reader.readJsonValue();
//            Map<String, Object> jsonObject = (Map<String, Object>) jsonValue;
//            Object label = jsonObject.get(labelKey);
//            if (!(label instanceof String)) {
//                return null;
//            }
//            JsonAdapter<Object> adapter = labelToAdapter.get(label);
//            if (adapter == null) {
//                return fallbackAdapter.fromJsonValue(jsonValue);
//            }
//            return adapter.fromJsonValue(jsonValue);
//        }
//
//        @Override
//        public void toJson(JsonWriter writer, Object value) throws IOException {
//            Class<?> type = value.getClass();
//            String label = typeToLabel.get(type);
//            if (label == null) {
//                throw new IllegalArgumentException("Expected one of "
//                        + typeToLabel.keySet()
//                        + " but found "
//                        + value
//                        + ", a "
//                        + value.getClass()
//                        + ". Register this subtype.");
//            }
//            JsonAdapter<Object> adapter = labelToAdapter.get(label);
//            Map<String, Object> jsonValue = (Map<String, Object>) adapter.toJsonValue(value);
//
//            Map<String, Object> valueWithLabel = new LinkedHashMap<>(1 + jsonValue.size());
//            valueWithLabel.put(labelKey, label);
//            valueWithLabel.putAll(jsonValue);
//            objectJsonAdapter.toJson(writer, valueWithLabel);
//        }
//
//        @Override
//        public String toString() {
//            return "RuntimeJsonAdapter(" + labelKey + ")";
//        }
//    }
//}
