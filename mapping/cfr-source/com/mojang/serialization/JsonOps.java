/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  javax.annotation.Nullable
 */
package com.mojang.serialization;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

public class JsonOps
implements DynamicOps<JsonElement> {
    public static final JsonOps INSTANCE = new JsonOps(false);
    public static final JsonOps COMPRESSED = new JsonOps(true);
    private final boolean compressed;

    protected JsonOps(boolean bl) {
        this.compressed = bl;
    }

    @Override
    public JsonElement empty() {
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonElement emptyMap() {
        return new JsonObject();
    }

    @Override
    public JsonElement emptyList() {
        return new JsonArray();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject) {
            return this.convertMap(dynamicOps, jsonElement);
        }
        if (jsonElement instanceof JsonArray) {
            return this.convertList(dynamicOps, jsonElement);
        }
        if (jsonElement instanceof JsonNull) {
            return dynamicOps.empty();
        }
        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        if (jsonPrimitive.isString()) {
            return dynamicOps.createString(jsonPrimitive.getAsString());
        }
        if (jsonPrimitive.isBoolean()) {
            return dynamicOps.createBoolean(jsonPrimitive.getAsBoolean());
        }
        BigDecimal bigDecimal = jsonPrimitive.getAsBigDecimal();
        try {
            long l = bigDecimal.longValueExact();
            if ((long)((byte)l) == l) {
                return dynamicOps.createByte((byte)l);
            }
            if ((long)((short)l) == l) {
                return dynamicOps.createShort((short)l);
            }
            if ((long)((int)l) == l) {
                return dynamicOps.createInt((int)l);
            }
            return dynamicOps.createLong(l);
        }
        catch (ArithmeticException arithmeticException) {
            double d = bigDecimal.doubleValue();
            if ((double)((float)d) == d) {
                return dynamicOps.createFloat((float)d);
            }
            return dynamicOps.createDouble(d);
        }
    }

    @Override
    public DataResult<Number> getNumberValue(JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive) {
            if (jsonElement.getAsJsonPrimitive().isNumber()) {
                return DataResult.success(jsonElement.getAsNumber());
            }
            if (this.compressed && jsonElement.getAsJsonPrimitive().isString()) {
                try {
                    return DataResult.success(Integer.parseInt(jsonElement.getAsString()));
                }
                catch (NumberFormatException numberFormatException) {
                    return DataResult.error(() -> "Not a number: " + String.valueOf(numberFormatException) + " " + String.valueOf(jsonElement));
                }
            }
        }
        return DataResult.error(() -> "Not a number: " + String.valueOf(jsonElement));
    }

    @Override
    public JsonElement createNumeric(Number number) {
        return new JsonPrimitive(number);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive && jsonElement.getAsJsonPrimitive().isBoolean()) {
            return DataResult.success(jsonElement.getAsBoolean());
        }
        return DataResult.error(() -> "Not a boolean: " + String.valueOf(jsonElement));
    }

    @Override
    public JsonElement createBoolean(boolean bl) {
        return new JsonPrimitive(Boolean.valueOf(bl));
    }

    @Override
    public DataResult<String> getStringValue(JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive && (jsonElement.getAsJsonPrimitive().isString() || jsonElement.getAsJsonPrimitive().isNumber() && this.compressed)) {
            return DataResult.success(jsonElement.getAsString());
        }
        return DataResult.error(() -> "Not a string: " + String.valueOf(jsonElement));
    }

    @Override
    public JsonElement createString(String string) {
        return new JsonPrimitive(string);
    }

    @Override
    public DataResult<JsonElement> mergeToList(JsonElement jsonElement, JsonElement jsonElement2) {
        if (!(jsonElement instanceof JsonArray) && jsonElement != this.empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(jsonElement), jsonElement);
        }
        JsonArray jsonArray = new JsonArray();
        if (jsonElement != this.empty()) {
            jsonArray.addAll(jsonElement.getAsJsonArray());
        }
        jsonArray.add(jsonElement2);
        return DataResult.success(jsonArray);
    }

    @Override
    public DataResult<JsonElement> mergeToList(JsonElement jsonElement, List<JsonElement> list) {
        if (!(jsonElement instanceof JsonArray) && jsonElement != this.empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + String.valueOf(jsonElement), jsonElement);
        }
        if (list.isEmpty()) {
            if (jsonElement == this.empty()) {
                return DataResult.success(this.emptyList());
            }
            return DataResult.success(jsonElement);
        }
        JsonArray jsonArray = new JsonArray();
        if (jsonElement != this.empty()) {
            jsonArray.addAll(jsonElement.getAsJsonArray());
        }
        list.forEach(arg_0 -> ((JsonArray)jsonArray).add(arg_0));
        return DataResult.success(jsonArray);
    }

    @Override
    public DataResult<JsonElement> mergeToMap(JsonElement jsonElement, JsonElement jsonElement2, JsonElement jsonElement3) {
        if (!(jsonElement instanceof JsonObject) && jsonElement != this.empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(jsonElement), jsonElement);
        }
        if (!(jsonElement2 instanceof JsonPrimitive) || !jsonElement2.getAsJsonPrimitive().isString() && !this.compressed) {
            return DataResult.error(() -> "key is not a string: " + String.valueOf(jsonElement2), jsonElement);
        }
        JsonObject jsonObject = new JsonObject();
        if (jsonElement != this.empty()) {
            jsonElement.getAsJsonObject().entrySet().forEach(entry -> jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue()));
        }
        jsonObject.add(jsonElement2.getAsString(), jsonElement3);
        return DataResult.success(jsonObject);
    }

    @Override
    public DataResult<JsonElement> mergeToMap(JsonElement jsonElement, MapLike<JsonElement> mapLike) {
        if (!(jsonElement instanceof JsonObject) && jsonElement != this.empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(jsonElement), jsonElement);
        }
        Iterator iterator = mapLike.entries().iterator();
        if (!iterator.hasNext()) {
            if (jsonElement == this.empty()) {
                return DataResult.success(this.emptyMap());
            }
            return DataResult.success(jsonElement);
        }
        JsonObject jsonObject = new JsonObject();
        if (jsonElement != this.empty()) {
            jsonElement.getAsJsonObject().entrySet().forEach(entry -> jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue()));
        }
        ArrayList arrayList = Lists.newArrayList();
        iterator.forEachRemaining(pair -> {
            JsonElement jsonElement = (JsonElement)pair.getFirst();
            if (!(jsonElement instanceof JsonPrimitive) || !jsonElement.getAsJsonPrimitive().isString() && !this.compressed) {
                arrayList.add(jsonElement);
                return;
            }
            jsonObject.add(jsonElement.getAsString(), (JsonElement)pair.getSecond());
        });
        if (!arrayList.isEmpty()) {
            return DataResult.error(() -> "some keys are not strings: " + String.valueOf(arrayList), jsonObject);
        }
        return DataResult.success(jsonObject);
    }

    @Override
    public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(JsonElement jsonElement) {
        if (!(jsonElement instanceof JsonObject)) {
            return DataResult.error(() -> "Not a JSON object: " + String.valueOf(jsonElement));
        }
        return DataResult.success(jsonElement.getAsJsonObject().entrySet().stream().map(entry -> Pair.of(new JsonPrimitive((String)entry.getKey()), entry.getValue() instanceof JsonNull ? null : (JsonElement)entry.getValue())));
    }

    @Override
    public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(JsonElement jsonElement) {
        if (!(jsonElement instanceof JsonObject)) {
            return DataResult.error(() -> "Not a JSON object: " + String.valueOf(jsonElement));
        }
        return DataResult.success(biConsumer -> {
            for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
                biConsumer.accept(this.createString((String)entry.getKey()), entry.getValue() instanceof JsonNull ? null : (JsonElement)entry.getValue());
            }
        });
    }

    @Override
    public DataResult<MapLike<JsonElement>> getMap(JsonElement jsonElement) {
        if (!(jsonElement instanceof JsonObject)) {
            return DataResult.error(() -> "Not a JSON object: " + String.valueOf(jsonElement));
        }
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        return DataResult.success(new MapLike<JsonElement>(){

            @Override
            @Nullable
            public JsonElement get(JsonElement jsonElement) {
                JsonElement jsonElement2 = jsonObject.get(jsonElement.getAsString());
                if (jsonElement2 instanceof JsonNull) {
                    return null;
                }
                return jsonElement2;
            }

            @Override
            @Nullable
            public JsonElement get(String string) {
                JsonElement jsonElement = jsonObject.get(string);
                if (jsonElement instanceof JsonNull) {
                    return null;
                }
                return jsonElement;
            }

            @Override
            public Stream<Pair<JsonElement, JsonElement>> entries() {
                return jsonObject.entrySet().stream().map(entry -> Pair.of(new JsonPrimitive((String)entry.getKey()), (JsonElement)entry.getValue()));
            }

            public String toString() {
                return "MapLike[" + String.valueOf(jsonObject) + "]";
            }
        });
    }

    @Override
    public JsonElement createMap(Stream<Pair<JsonElement, JsonElement>> stream) {
        JsonObject jsonObject = new JsonObject();
        stream.forEach(pair -> jsonObject.add(((JsonElement)pair.getFirst()).getAsString(), (JsonElement)pair.getSecond()));
        return jsonObject;
    }

    @Override
    public DataResult<Stream<JsonElement>> getStream(JsonElement jsonElement2) {
        if (jsonElement2 instanceof JsonArray) {
            return DataResult.success(StreamSupport.stream(jsonElement2.getAsJsonArray().spliterator(), false).map(jsonElement -> jsonElement instanceof JsonNull ? null : jsonElement));
        }
        return DataResult.error(() -> "Not a json array: " + String.valueOf(jsonElement2));
    }

    @Override
    public DataResult<Consumer<Consumer<JsonElement>>> getList(JsonElement jsonElement) {
        if (jsonElement instanceof JsonArray) {
            return DataResult.success(consumer -> {
                for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                    consumer.accept(jsonElement2 instanceof JsonNull ? null : jsonElement2);
                }
            });
        }
        return DataResult.error(() -> "Not a json array: " + String.valueOf(jsonElement));
    }

    @Override
    public JsonElement createList(Stream<JsonElement> stream) {
        JsonArray jsonArray = new JsonArray();
        stream.forEach(arg_0 -> ((JsonArray)jsonArray).add(arg_0));
        return jsonArray;
    }

    @Override
    public JsonElement remove(JsonElement jsonElement, String string) {
        if (jsonElement instanceof JsonObject) {
            JsonObject jsonObject = new JsonObject();
            jsonElement.getAsJsonObject().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), string)).forEach(entry -> jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue()));
            return jsonObject;
        }
        return jsonElement;
    }

    public String toString() {
        return "JSON";
    }

    @Override
    public ListBuilder<JsonElement> listBuilder() {
        return new ArrayBuilder();
    }

    @Override
    public boolean compressMaps() {
        return this.compressed;
    }

    @Override
    public RecordBuilder<JsonElement> mapBuilder() {
        return new JsonRecordBuilder();
    }

    private static final class ArrayBuilder
    implements ListBuilder<JsonElement> {
        private DataResult<JsonArray> builder = DataResult.success(new JsonArray(), Lifecycle.stable());

        private ArrayBuilder() {
        }

        @Override
        public DynamicOps<JsonElement> ops() {
            return INSTANCE;
        }

        @Override
        public ListBuilder<JsonElement> add(JsonElement jsonElement) {
            this.builder = this.builder.map(jsonArray -> {
                jsonArray.add(jsonElement);
                return jsonArray;
            });
            return this;
        }

        @Override
        public ListBuilder<JsonElement> add(DataResult<JsonElement> dataResult) {
            this.builder = this.builder.apply2stable((jsonArray, jsonElement) -> {
                jsonArray.add(jsonElement);
                return jsonArray;
            }, dataResult);
            return this;
        }

        @Override
        public ListBuilder<JsonElement> withErrorsFrom(DataResult<?> dataResult) {
            this.builder = this.builder.flatMap(jsonArray -> dataResult.map(object -> jsonArray));
            return this;
        }

        @Override
        public ListBuilder<JsonElement> mapError(UnaryOperator<String> unaryOperator) {
            this.builder = this.builder.mapError(unaryOperator);
            return this;
        }

        @Override
        public DataResult<JsonElement> build(JsonElement jsonElement) {
            DataResult<JsonElement> dataResult = this.builder.flatMap(jsonArray -> {
                if (!(jsonElement instanceof JsonArray) && jsonElement != this.ops().empty()) {
                    return DataResult.error(() -> "Cannot append a list to not a list: " + String.valueOf(jsonElement), jsonElement);
                }
                JsonArray jsonArray2 = new JsonArray();
                if (jsonElement != this.ops().empty()) {
                    jsonArray2.addAll(jsonElement.getAsJsonArray());
                }
                jsonArray2.addAll(jsonArray);
                return DataResult.success(jsonArray2, Lifecycle.stable());
            });
            this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
            return dataResult;
        }
    }

    private class JsonRecordBuilder
    extends RecordBuilder.AbstractStringBuilder<JsonElement, JsonObject> {
        protected JsonRecordBuilder() {
            super(JsonOps.this);
        }

        @Override
        protected JsonObject initBuilder() {
            return new JsonObject();
        }

        @Override
        protected JsonObject append(String string, JsonElement jsonElement, JsonObject jsonObject) {
            jsonObject.add(string, jsonElement);
            return jsonObject;
        }

        @Override
        protected DataResult<JsonElement> build(JsonObject jsonObject, JsonElement jsonElement) {
            if (jsonElement == null || jsonElement instanceof JsonNull) {
                return DataResult.success(jsonObject);
            }
            if (jsonElement instanceof JsonObject) {
                JsonObject jsonObject2 = new JsonObject();
                for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
                    jsonObject2.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
                for (Map.Entry entry : jsonObject.entrySet()) {
                    jsonObject2.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
                return DataResult.success(jsonObject2);
            }
            return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(jsonElement), jsonElement);
        }
    }
}

