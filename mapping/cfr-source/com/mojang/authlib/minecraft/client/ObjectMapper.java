/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonParseException
 */
package com.mojang.authlib.minecraft.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.util.ByteBufferTypeAdapter;
import com.mojang.util.InstantTypeAdapter;
import com.mojang.util.UUIDTypeAdapter;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ObjectMapper {
    private final Gson gson;

    public ObjectMapper(Gson gson) {
        this.gson = Objects.requireNonNull(gson);
    }

    public <T> T readValue(String string, Class<T> clazz) {
        try {
            return (T)this.gson.fromJson(string, clazz);
        }
        catch (JsonParseException jsonParseException) {
            throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to read value " + string, jsonParseException);
        }
    }

    public String writeValueAsString(Object object) {
        try {
            return this.gson.toJson(object);
        }
        catch (RuntimeException runtimeException) {
            throw new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Failed to write value", runtimeException);
        }
    }

    public static ObjectMapper create() {
        return new ObjectMapper(new GsonBuilder().registerTypeAdapter(UUID.class, (Object)new UUIDTypeAdapter()).registerTypeAdapter(Instant.class, (Object)new InstantTypeAdapter()).registerTypeHierarchyAdapter(ByteBuffer.class, (Object)new ByteBufferTypeAdapter().nullSafe()).registerTypeAdapter(PropertyMap.class, (Object)new PropertyMap.Serializer()).registerTypeAdapter(UUID.class, (Object)new UUIDTypeAdapter()).registerTypeAdapter(ProfileSearchResultsResponse.class, (Object)new ProfileSearchResultsResponse.Serializer()).create());
    }
}

