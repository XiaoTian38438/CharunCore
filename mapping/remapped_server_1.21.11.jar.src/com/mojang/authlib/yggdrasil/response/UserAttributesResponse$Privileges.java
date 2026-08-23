/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ public final class Privileges extends Record { @SerializedName("onlineChat")
/*    */   @Nullable
/*    */   private final Privilege onlineChat;
/*    */   @SerializedName("multiplayerServer")
/*    */   @Nullable
/*    */   private final Privilege multiplayerServer;
/*    */   @SerializedName("multiplayerRealms")
/*    */   @Nullable
/*    */   private final Privilege multiplayerRealms;
/*    */   @SerializedName("telemetry")
/*    */   @Nullable
/*    */   private final Privilege telemetry;
/*    */   @SerializedName("optionalTelemetry")
/*    */   @Nullable
/*    */   private final Privilege optionalTelemetry;
/*    */   
/* 18 */   public Privileges(@Nullable Privilege paramPrivilege1, @Nullable Privilege paramPrivilege2, @Nullable Privilege paramPrivilege3, @Nullable Privilege paramPrivilege4, @Nullable Privilege paramPrivilege5) { this.onlineChat = paramPrivilege1; this.multiplayerServer = paramPrivilege2; this.multiplayerRealms = paramPrivilege3; this.telemetry = paramPrivilege4; this.optionalTelemetry = paramPrivilege5; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 18 */     //   #18	-> 0 } @SerializedName("onlineChat") @Nullable public Privilege onlineChat() { return this.onlineChat; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 18 */     //   #18	-> 0 } @SerializedName("multiplayerServer") @Nullable public Privilege multiplayerServer() { return this.multiplayerServer; } @SerializedName("multiplayerRealms") @Nullable public Privilege multiplayerRealms() { return this.multiplayerRealms; } @SerializedName("telemetry") @Nullable public Privilege telemetry() { return this.telemetry; } @SerializedName("optionalTelemetry") @Nullable public Privilege optionalTelemetry() { return this.optionalTelemetry; }
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
/*    */   public boolean getOnlineChat() {
/* 31 */     return (this.onlineChat != null && this.onlineChat.enabled);
/*    */   }
/*    */   
/*    */   public boolean getMultiplayerServer() {
/* 35 */     return (this.multiplayerServer != null && this.multiplayerServer.enabled);
/*    */   }
/*    */   
/*    */   public boolean getMultiplayerRealms() {
/* 39 */     return (this.multiplayerRealms != null && this.multiplayerRealms.enabled);
/*    */   }
/*    */   
/*    */   public boolean getTelemetry() {
/* 43 */     return (this.telemetry != null && this.telemetry.enabled);
/*    */   }
/*    */   
/*    */   public boolean getOptionalTelemetry() {
/* 47 */     return (this.optionalTelemetry != null && this.optionalTelemetry.enabled);
/*    */   } public static final class Privilege extends Record { @SerializedName("enabled")
/*    */     private final boolean enabled;
/* 50 */     public Privilege(boolean param2Boolean) { this.enabled = param2Boolean; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0 } public final boolean equals(Object param2Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 50 */       //   #50	-> 0 } @SerializedName("enabled") public boolean enabled() { return this.enabled; }
/*    */      }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\UserAttributesResponse$Privileges.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */