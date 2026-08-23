/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package com.mojang.util;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

public class ByteBufferTypeAdapter
extends TypeAdapter<ByteBuffer> {
    public void write(JsonWriter jsonWriter, ByteBuffer byteBuffer) throws IOException {
        jsonWriter.value(Base64.getEncoder().encodeToString(byteBuffer.array()));
    }

    public ByteBuffer read(JsonReader jsonReader) throws IOException {
        try {
            return ByteBuffer.wrap(Base64.getDecoder().decode(jsonReader.nextString()));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new JsonParseException("Malformed base64 string", (Throwable)illegalArgumentException);
        }
    }
}

