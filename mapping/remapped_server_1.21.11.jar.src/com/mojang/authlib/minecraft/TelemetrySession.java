/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ public interface TelemetrySession {
/*  4 */   public static final TelemetrySession DISABLED = new TelemetrySession()
/*    */     {
/*    */       public boolean isEnabled() {
/*  7 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public TelemetryEvent createNewEvent(String param1String) {
/* 12 */         return TelemetryEvent.EMPTY;
/*    */       }
/*    */     };
/*    */   
/*    */   boolean isEnabled();
/*    */   
/*    */   TelemetryEvent createNewEvent(String paramString);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\TelemetrySession.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */