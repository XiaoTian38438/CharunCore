/*   */ package com.mojang.authlib.minecraft;
/*   */ 
/*   */ public interface TelemetryEvent extends TelemetryPropertyContainer {
/* 4 */   public static final TelemetryEvent EMPTY = new TelemetryEvent() {
/*   */       public void addProperty(String param1String1, String param1String2) {}
/*   */       
/*   */       public void addProperty(String param1String, int param1Int) {}
/*   */       
/*   */       public void addProperty(String param1String, long param1Long) {}
/*   */       
/*   */       public void addProperty(String param1String, boolean param1Boolean) {}
/*   */       
/*   */       public void addNullProperty(String param1String) {}
/*   */       
/*   */       public void send() {}
/*   */     };
/*   */   
/*   */   void send();
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\TelemetryEvent.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */