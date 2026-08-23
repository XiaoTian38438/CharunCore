/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.authlib.minecraft;

import com.mojang.authlib.minecraft.TelemetryPropertyContainer;

public interface TelemetryEvent
extends TelemetryPropertyContainer {
    public static final TelemetryEvent EMPTY = new TelemetryEvent(){

        @Override
        public void addProperty(String string, String string2) {
        }

        @Override
        public void addProperty(String string, int n) {
        }

        @Override
        public void addProperty(String string, long l) {
        }

        @Override
        public void addProperty(String string, boolean bl) {
        }

        @Override
        public void addNullProperty(String string) {
        }

        @Override
        public void send() {
        }
    };

    public void send();
}

