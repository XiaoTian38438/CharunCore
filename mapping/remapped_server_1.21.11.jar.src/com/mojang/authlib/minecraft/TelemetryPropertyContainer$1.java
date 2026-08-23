/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements TelemetryPropertyContainer
/*    */ {
/*    */   public void addProperty(String paramString1, String paramString2) {
/* 21 */     object.addProperty(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, int paramInt) {
/* 26 */     object.addProperty(paramString, Integer.valueOf(paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, long paramLong) {
/* 31 */     object.addProperty(paramString, Long.valueOf(paramLong));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProperty(String paramString, boolean paramBoolean) {
/* 36 */     object.addProperty(paramString, Boolean.valueOf(paramBoolean));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addNullProperty(String paramString) {
/* 41 */     object.add(paramString, (JsonElement)JsonNull.INSTANCE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\TelemetryPropertyContainer$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */