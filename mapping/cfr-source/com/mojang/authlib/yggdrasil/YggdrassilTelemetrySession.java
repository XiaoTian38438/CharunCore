/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.collect.ImmutableList
 *  com.google.gson.JsonObject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib.yggdrasil;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.authlib.Environment;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.TelemetryEvent;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrassilTelemetryEvent;
import com.mojang.authlib.yggdrasil.request.TelemetryEventsRequest;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrassilTelemetrySession
implements TelemetrySession {
    private static final Logger LOGGER = LoggerFactory.getLogger(YggdrassilTelemetrySession.class);
    private static final String SOURCE = "minecraft.java";
    private final MinecraftClient minecraftClient;
    private final URL routeEvents;
    private final Executor ioExecutor;

    @VisibleForTesting
    YggdrassilTelemetrySession(MinecraftClient minecraftClient, Environment environment, Executor executor) {
        this.minecraftClient = minecraftClient;
        this.routeEvents = HttpAuthenticationService.constantURL(environment.servicesHost() + "/events");
        this.ioExecutor = executor;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public TelemetryEvent createNewEvent(String string) {
        return new YggdrassilTelemetryEvent(this, string);
    }

    void sendEvent(String string, JsonObject jsonObject) {
        Instant instant = Instant.now();
        TelemetryEventsRequest.Event event = new TelemetryEventsRequest.Event(SOURCE, string, instant, jsonObject);
        this.ioExecutor.execute(() -> {
            try {
                TelemetryEventsRequest telemetryEventsRequest = new TelemetryEventsRequest((List<TelemetryEventsRequest.Event>)ImmutableList.of((Object)event));
                this.minecraftClient.post(this.routeEvents, telemetryEventsRequest, Void.class);
            }
            catch (MinecraftClientException minecraftClientException) {
                LOGGER.debug("Failed to send telemetry event {}", (Object)event.name(), (Object)minecraftClientException);
            }
        });
    }
}

