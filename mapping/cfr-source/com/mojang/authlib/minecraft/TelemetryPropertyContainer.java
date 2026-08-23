/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 */
package com.mojang.authlib.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public interface TelemetryPropertyContainer {
    public void addProperty(String var1, String var2);

    public void addProperty(String var1, int var2);

    public void addProperty(String var1, long var2);

    public void addProperty(String var1, boolean var2);

    public void addNullProperty(String var1);

    public static TelemetryPropertyContainer forJsonObject(final JsonObject jsonObject) {
        return new TelemetryPropertyContainer(){

            @Override
            public void addProperty(String string, String string2) {
                jsonObject.addProperty(string, string2);
            }

            @Override
            public void addProperty(String string, int n) {
                jsonObject.addProperty(string, (Number)n);
            }

            @Override
            public void addProperty(String string, long l) {
                jsonObject.addProperty(string, (Number)l);
            }

            @Override
            public void addProperty(String string, boolean bl) {
                jsonObject.addProperty(string, Boolean.valueOf(bl));
            }

            @Override
            public void addNullProperty(String string) {
                jsonObject.add(string, (JsonElement)JsonNull.INSTANCE);
            }
        };
    }
}

