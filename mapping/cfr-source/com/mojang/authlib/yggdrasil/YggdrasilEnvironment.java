/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum YggdrasilEnvironment {
    PROD("https://sessionserver.mojang.com", "https://api.minecraftservices.com", "https://api.mojang.com"),
    STAGING("https://yggdrasil-auth-session-staging.mojang.zone", "https://api-staging.minecraftservices.com", "https://api-staging.mojang.com");

    private final Environment environment;

    private YggdrasilEnvironment(String string2, String string3, String string4) {
        this.environment = new Environment(string2, string3, string4, this.name());
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public static Optional<Environment> fromString(@Nullable String string) {
        return Stream.of(YggdrasilEnvironment.values()).filter(yggdrasilEnvironment -> string != null && string.equalsIgnoreCase(yggdrasilEnvironment.name())).findFirst().map(YggdrasilEnvironment::getEnvironment);
    }
}

