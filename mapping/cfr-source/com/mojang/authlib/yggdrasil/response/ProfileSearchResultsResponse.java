/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParseException
 *  com.google.gson.reflect.TypeToken
 */
package com.mojang.authlib.yggdrasil.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.yggdrasil.response.NameAndId;
import java.lang.reflect.Type;
import java.util.List;

public record ProfileSearchResultsResponse(List<NameAndId> profiles) {
    public static final Type LIST_TYPE = TypeToken.getParameterized(List.class, (Type[])new Type[]{NameAndId.class}).getType();

    public static class Serializer
    implements JsonDeserializer<ProfileSearchResultsResponse> {
        public ProfileSearchResultsResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new ProfileSearchResultsResponse((List)jsonDeserializationContext.deserialize(jsonElement, LIST_TYPE));
        }
    }
}

