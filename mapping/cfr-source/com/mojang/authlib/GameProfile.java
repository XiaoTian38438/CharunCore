/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.authlib;

import com.mojang.authlib.properties.PropertyMap;
import java.util.Objects;
import java.util.UUID;

public record GameProfile(UUID id, String name, PropertyMap properties) {
    public GameProfile {
        Objects.requireNonNull(uUID, "Profile ID must not be null");
        Objects.requireNonNull(string, "Profile name must not be null");
        Objects.requireNonNull(propertyMap, "Profile properties must not be null");
    }

    public GameProfile(UUID uUID, String string) {
        this(uUID, string, PropertyMap.EMPTY);
    }
}

