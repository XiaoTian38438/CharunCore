/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  javax.annotation.Nullable
 */
package com.mojang.authlib.yggdrasil;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.authlib.minecraft.TelemetryEvent;
import com.mojang.authlib.yggdrasil.YggdrassilTelemetrySession;
import javax.annotation.Nullable;

public class YggdrassilTelemetryEvent
implements TelemetryEvent {
    private final YggdrassilTelemetrySession service;
    private final String type;
    @Nullable
    private JsonObject data = new JsonObject();

    YggdrassilTelemetryEvent(YggdrassilTelemetrySession yggdrassilTelemetrySession, String string) {
        this.service = yggdrassilTelemetrySession;
        this.type = string;
    }

    private JsonObject data() {
        if (this.data == null) {
            throw new IllegalStateException("Event already sent");
        }
        return this.data;
    }

    @Override
    public void addProperty(String string, String string2) {
        this.data().addProperty(string, string2);
    }

    @Override
    public void addProperty(String string, int n) {
        this.data().addProperty(string, (Number)n);
    }

    @Override
    public void addProperty(String string, long l) {
        this.data().addProperty(string, (Number)l);
    }

    @Override
    public void addProperty(String string, boolean bl) {
        this.data().addProperty(string, Boolean.valueOf(bl));
    }

    @Override
    public void addNullProperty(String string) {
        this.data().add(string, (JsonElement)JsonNull.INSTANCE);
    }

    @Override
    public void send() {
        this.service.sendEvent(this.type, this.data);
        this.data = null;
    }
}

