/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ForwardingMultimap
 *  com.google.common.collect.ImmutableMultimap
 *  com.google.common.collect.ImmutableMultimap$Builder
 *  com.google.common.collect.Multimap
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package com.mojang.authlib.properties;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Type;
import java.util.Map;

public class PropertyMap
extends ForwardingMultimap<String, Property> {
    public static final PropertyMap EMPTY = new PropertyMap((Multimap<String, Property>)ImmutableMultimap.of());
    private final Multimap<String, Property> properties;

    public PropertyMap(Multimap<String, Property> multimap) {
        this.properties = ImmutableMultimap.copyOf(multimap);
    }

    protected Multimap<String, Property> delegate() {
        return this.properties;
    }

    public static class Serializer
    implements JsonSerializer<PropertyMap>,
    JsonDeserializer<PropertyMap> {
        public PropertyMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            ImmutableMultimap.Builder builder;
            block5: {
                block4: {
                    builder = ImmutableMultimap.builder();
                    if (!(jsonElement instanceof JsonObject)) break block4;
                    JsonObject jsonObject = (JsonObject)jsonElement;
                    for (Map.Entry entry : jsonObject.entrySet()) {
                        if (!(entry.getValue() instanceof JsonArray)) continue;
                        for (JsonElement jsonElement2 : (JsonArray)entry.getValue()) {
                            builder.put((Object)((String)entry.getKey()), (Object)new Property((String)entry.getKey(), jsonElement2.getAsString()));
                        }
                    }
                    break block5;
                }
                if (!(jsonElement instanceof JsonArray)) break block5;
                JsonArray jsonArray = (JsonArray)jsonElement;
                for (JsonElement jsonElement3 : jsonArray) {
                    if (!(jsonElement3 instanceof JsonObject)) continue;
                    JsonObject jsonObject = (JsonObject)jsonElement3;
                    String string = jsonObject.getAsJsonPrimitive("name").getAsString();
                    String string2 = jsonObject.getAsJsonPrimitive("value").getAsString();
                    if (jsonObject.has("signature")) {
                        builder.put((Object)string, (Object)new Property(string, string2, jsonObject.getAsJsonPrimitive("signature").getAsString()));
                        continue;
                    }
                    builder.put((Object)string, (Object)new Property(string, string2));
                }
            }
            return new PropertyMap((Multimap<String, Property>)builder.build());
        }

        public JsonElement serialize(PropertyMap propertyMap, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray jsonArray = new JsonArray();
            for (Property property : propertyMap.values()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", property.name());
                jsonObject.addProperty("value", property.value());
                String string = property.signature();
                if (string != null) {
                    jsonObject.addProperty("signature", string);
                }
                jsonArray.add((JsonElement)jsonObject);
            }
            return jsonArray;
        }
    }
}

