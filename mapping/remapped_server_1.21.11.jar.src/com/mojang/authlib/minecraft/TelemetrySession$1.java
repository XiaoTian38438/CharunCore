/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ class null
/*    */   implements TelemetrySession
/*    */ {
/*    */   public boolean isEnabled() {
/*  7 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public TelemetryEvent createNewEvent(String paramString) {
/* 12 */     return TelemetryEvent.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\TelemetrySession$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */