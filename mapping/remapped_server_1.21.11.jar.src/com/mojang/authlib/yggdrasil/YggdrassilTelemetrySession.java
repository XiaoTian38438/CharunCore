/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.Environment;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*    */ import com.mojang.authlib.minecraft.TelemetryEvent;
/*    */ import com.mojang.authlib.minecraft.TelemetrySession;
/*    */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*    */ import com.mojang.authlib.yggdrasil.request.TelemetryEventsRequest;
/*    */ import java.net.URL;
/*    */ import java.time.Instant;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class YggdrassilTelemetrySession implements TelemetrySession {
/* 21 */   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrassilTelemetrySession.class);
/*    */   
/*    */   private static final String SOURCE = "minecraft.java";
/*    */   
/*    */   private final MinecraftClient minecraftClient;
/*    */   private final URL routeEvents;
/*    */   private final Executor ioExecutor;
/*    */   
/*    */   @VisibleForTesting
/*    */   YggdrassilTelemetrySession(MinecraftClient paramMinecraftClient, Environment paramEnvironment, Executor paramExecutor) {
/* 31 */     this.minecraftClient = paramMinecraftClient;
/* 32 */     this.routeEvents = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/events");
/* 33 */     this.ioExecutor = paramExecutor;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public TelemetryEvent createNewEvent(String paramString) {
/* 43 */     return new YggdrassilTelemetryEvent(this, paramString);
/*    */   }
/*    */   
/*    */   void sendEvent(String paramString, JsonObject paramJsonObject) {
/* 47 */     Instant instant = Instant.now();
/* 48 */     TelemetryEventsRequest.Event event = new TelemetryEventsRequest.Event("minecraft.java", paramString, instant, paramJsonObject);
/*    */     
/* 50 */     this.ioExecutor.execute(() -> {
/*    */           try {
/*    */             TelemetryEventsRequest telemetryEventsRequest = new TelemetryEventsRequest((List)ImmutableList.of(paramEvent));
/*    */             this.minecraftClient.post(this.routeEvents, telemetryEventsRequest, Void.class);
/* 54 */           } catch (MinecraftClientException minecraftClientException) {
/*    */             LOGGER.debug("Failed to send telemetry event {}", paramEvent.name(), minecraftClientException);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrassilTelemetrySession.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */