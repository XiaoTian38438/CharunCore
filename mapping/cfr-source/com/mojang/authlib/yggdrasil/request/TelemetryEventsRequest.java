/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.annotations.SerializedName
 */
package com.mojang.authlib.yggdrasil.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import java.util.List;

public record TelemetryEventsRequest(@SerializedName(value="events") List<Event> events) {

    public record Event(@SerializedName(value="source") String source, @SerializedName(value="name") String name, @SerializedName(value="timestamp") long timestamp, @SerializedName(value="data") JsonObject data) {
        public Event(String string, String string2, Instant instant, JsonObject jsonObject) {
            this(string, string2, instant.getEpochSecond(), jsonObject);
        }
    }
}

