/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.minecraft.TelemetryEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class YggdrassilTelemetryEvent implements TelemetryEvent {
/*    */   private final YggdrassilTelemetrySession service;
/*    */   private final String type;
/*    */   @Nullable
/* 13 */   private JsonObject data = new JsonObject();
/*    */ 
/*    */   
/*    */   YggdrassilTelemetryEvent(YggdrassilTelemetrySession paramYggdrassilTelemetrySession, String paramString) {
/* 17 */     this.service = paramYggdrassilTelemetrySession;
/* 18 */     this.type = paramString;
/*    */   }
/*    */   
/*    */   private JsonObject data() {
/* 22 */     if (this.data == null) {
/* 23 */       throw new IllegalStateException("Event already sent");
/*    */     }
/* 25 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString1, String paramString2) {
/* 30 */     data().addProperty(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, int paramInt) {
/* 35 */     data().addProperty(paramString, Integer.valueOf(paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, long paramLong) {
/* 40 */     data().addProperty(paramString, Long.valueOf(paramLong));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, boolean paramBoolean) {
/* 45 */     data().addProperty(paramString, Boolean.valueOf(paramBoolean));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addNullProperty(String paramString) {
/* 50 */     data().add(paramString, (JsonElement)JsonNull.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void send() {
/* 55 */     this.service.sendEvent(this.type, this.data);
/* 56 */     this.data = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrassilTelemetryEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */