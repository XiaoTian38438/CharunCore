/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package com.mojang.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.util.UndashedUuid;
import java.io.IOException;
import java.util.UUID;

public class UUIDTypeAdapter
extends TypeAdapter<UUID> {
    public void write(JsonWriter jsonWriter, UUID uUID) throws IOException {
        jsonWriter.value(UndashedUuid.toString(uUID));
    }

    public UUID read(JsonReader jsonReader) throws IOException {
        return UndashedUuid.fromStringLenient(jsonReader.nextString());
    }
}

