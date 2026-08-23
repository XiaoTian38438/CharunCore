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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InstantTypeAdapter
extends TypeAdapter<Instant> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        jsonWriter.value(FORMATTER.format(instant));
    }

    public Instant read(JsonReader jsonReader) throws IOException {
        try {
            return Instant.from(FORMATTER.parse(jsonReader.nextString()));
        }
        catch (DateTimeParseException dateTimeParseException) {
            throw new JsonParseException("Malformed ISO instant format");
        }
    }
}

