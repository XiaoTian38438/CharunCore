/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib;

import com.mojang.authlib.Environment;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentParser {
    @Nullable
    private static String environmentOverride;
    private static final String PROP_PREFIX = "minecraft.api.";
    private static final Logger LOGGER;
    public static final String PROP_ENV = "minecraft.api.env";
    public static final String PROP_SESSION_HOST = "minecraft.api.session.host";
    public static final String PROP_SERVICES_HOST = "minecraft.api.services.host";
    public static final String PROP_PROFILES_HOST = "minecraft.api.profiles.host";

    public static void setEnvironmentOverride(@Nullable String string) {
        environmentOverride = string;
    }

    public static Optional<Environment> getEnvironmentFromProperties() {
        String string = environmentOverride != null ? environmentOverride : System.getProperty(PROP_ENV);
        Optional<Environment> optional = YggdrasilEnvironment.fromString(string);
        return optional.isPresent() ? optional : EnvironmentParser.fromHostNames();
    }

    private static Optional<Environment> fromHostNames() {
        String string = System.getProperty(PROP_SESSION_HOST);
        String string2 = System.getProperty(PROP_SERVICES_HOST);
        String string3 = System.getProperty(PROP_PROFILES_HOST);
        if (string2 != null && string != null && string3 != null) {
            return Optional.of(new Environment(string, string2, string3, "properties"));
        }
        if (string2 != null || string != null || string3 != null) {
            LOGGER.info("Ignoring hosts properties. All need to be set: {}", List.of(PROP_SERVICES_HOST, PROP_SESSION_HOST, PROP_PROFILES_HOST));
        }
        return Optional.empty();
    }

    static {
        LOGGER = LoggerFactory.getLogger(EnvironmentParser.class);
    }
}

