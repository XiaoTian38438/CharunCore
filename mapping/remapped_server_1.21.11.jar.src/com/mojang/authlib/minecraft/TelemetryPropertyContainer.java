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
/*    */ public interface TelemetryPropertyContainer
/*    */ {
/*    */   void addProperty(String paramString1, String paramString2);
/*    */   
/*    */   void addProperty(String paramString, int paramInt);
/*    */   
/*    */   static TelemetryPropertyContainer forJsonObject(final JsonObject object) {
/* 18 */     return new TelemetryPropertyContainer()
/*    */       {
/*    */         public void addProperty(String param1String1, String param1String2) {
/* 21 */           object.addProperty(param1String1, param1String2);
/*    */         }
/*    */ 
/*    */         
/*    */         public void addProperty(String param1String, int param1Int) {
/* 26 */           object.addProperty(param1String, Integer.valueOf(param1Int));
/*    */         }
/*    */ 
/*    */         
/*    */         public void addProperty(String param1String, long param1Long) {
/* 31 */           object.addProperty(param1String, Long.valueOf(param1Long));
/*    */         }
/*    */ 
/*    */         
/*    */         public void addProperty(String param1String, boolean param1Boolean) {
/* 36 */           object.addProperty(param1String, Boolean.valueOf(param1Boolean));
/*    */         }
/*    */ 
/*    */         
/*    */         public void addNullProperty(String param1String) {
/* 41 */           object.add(param1String, (JsonElement)JsonNull.INSTANCE);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   void addProperty(String paramString, long paramLong);
/*    */   
/*    */   void addProperty(String paramString, boolean paramBoolean);
/*    */   
/*    */   void addNullProperty(String paramString);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\TelemetryPropertyContainer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */