/*    */ package com.mojang.authlib.yggdrasil.response;public final class UserAttributesResponse extends Record { @SerializedName("privileges")
/*    */   @Nullable
/*    */   private final Privileges privileges; @SerializedName("profanityFilterPreferences")
/*    */   @Nullable
/*    */   private final ProfanityFilterPreferences profanityFilterPreferences;
/*    */   @SerializedName("banStatus")
/*    */   @Nullable
/*    */   private final BanStatus banStatus;
/*    */   
/* 10 */   public UserAttributesResponse(@Nullable Privileges paramPrivileges, @Nullable ProfanityFilterPreferences paramProfanityFilterPreferences, @Nullable BanStatus paramBanStatus) { this.privileges = paramPrivileges; this.profanityFilterPreferences = paramProfanityFilterPreferences; this.banStatus = paramBanStatus; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 10 */     //   #10	-> 0 } @SerializedName("privileges") @Nullable public Privileges privileges() { return this.privileges; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 10 */     //   #10	-> 0 } @SerializedName("profanityFilterPreferences") @Nullable public ProfanityFilterPreferences profanityFilterPreferences() { return this.profanityFilterPreferences; } @SerializedName("banStatus") @Nullable public BanStatus banStatus() { return this.banStatus; } public static final class Privileges extends Record { @SerializedName("onlineChat") @Nullable private final Privilege onlineChat; @SerializedName("multiplayerServer")
/*    */     @Nullable
/*    */     private final Privilege multiplayerServer; @SerializedName("multiplayerRealms")
/*    */     @Nullable
/*    */     private final Privilege multiplayerRealms; @SerializedName("telemetry")
/*    */     @Nullable
/*    */     private final Privilege telemetry; @SerializedName("optionalTelemetry")
/*    */     @Nullable
/* 18 */     private final Privilege optionalTelemetry; public Privileges(@Nullable Privilege param1Privilege1, @Nullable Privilege param1Privilege2, @Nullable Privilege param1Privilege3, @Nullable Privilege param1Privilege4, @Nullable Privilege param1Privilege5) { this.onlineChat = param1Privilege1; this.multiplayerServer = param1Privilege2; this.multiplayerRealms = param1Privilege3; this.telemetry = param1Privilege4; this.optionalTelemetry = param1Privilege5; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 18 */       //   #18	-> 0 } @SerializedName("onlineChat") @Nullable public Privilege onlineChat() { return this.onlineChat; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 18 */       //   #18	-> 0 } @SerializedName("multiplayerServer") @Nullable public Privilege multiplayerServer() { return this.multiplayerServer; } @SerializedName("multiplayerRealms") @Nullable public Privilege multiplayerRealms() { return this.multiplayerRealms; } @SerializedName("telemetry") @Nullable public Privilege telemetry() { return this.telemetry; } @SerializedName("optionalTelemetry") @Nullable public Privilege optionalTelemetry() { return this.optionalTelemetry; }
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
/*    */     public boolean getOnlineChat() {
/* 31 */       return (this.onlineChat != null && this.onlineChat.enabled);
/*    */     }
/*    */     
/*    */     public boolean getMultiplayerServer() {
/* 35 */       return (this.multiplayerServer != null && this.multiplayerServer.enabled);
/*    */     }
/*    */     
/*    */     public boolean getMultiplayerRealms() {
/* 39 */       return (this.multiplayerRealms != null && this.multiplayerRealms.enabled);
/*    */     }
/*    */     
/*    */     public boolean getTelemetry() {
/* 43 */       return (this.telemetry != null && this.telemetry.enabled);
/*    */     }
/*    */     
/*    */     public boolean getOptionalTelemetry() {
/* 47 */       return (this.optionalTelemetry != null && this.optionalTelemetry.enabled);
/*    */     } public static final class Privilege extends Record { @SerializedName("enabled")
/*    */       private final boolean enabled;
/* 50 */       public Privilege(boolean param2Boolean) { this.enabled = param2Boolean; } public final String toString() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;)Ljava/lang/String;
/*    */         //   6: areturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #50	-> 0 } public final int hashCode() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #50	-> 0 } public final boolean equals(Object param2Object) { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/* 50 */         //   #50	-> 0 } @SerializedName("enabled") public boolean enabled() { return this.enabled; } } } public static final class Privilege extends Record { @SerializedName("enabled") private final boolean enabled; public Privilege(boolean param1Boolean) { this.enabled = param1Boolean; } public final String toString() { // Byte code:
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
/*    */       //   #50	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges$Privilege;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 50 */       //   #50	-> 0 } @SerializedName("enabled") public boolean enabled() { return this.enabled; }
/*    */      }
/*    */ 
/*    */   
/*    */   public static final class ProfanityFilterPreferences extends Record { @SerializedName("profanityFilterOn")
/*    */     private final boolean enabled;
/*    */     
/* 57 */     public ProfanityFilterPreferences(boolean param1Boolean) { this.enabled = param1Boolean; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$ProfanityFilterPreferences;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$ProfanityFilterPreferences;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$ProfanityFilterPreferences;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 57 */       //   #57	-> 0 } @SerializedName("profanityFilterOn") public boolean enabled() { return this.enabled; }
/*    */      }
/*    */   
/*    */   public static final class BanStatus extends Record { @SerializedName("bannedScopes")
/*    */     private final Map<String, BannedScope> bannedScopes;
/*    */     
/* 63 */     public BanStatus(Map<String, BannedScope> param1Map) { this.bannedScopes = param1Map; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 63 */       //   #63	-> 0 } @SerializedName("bannedScopes") public Map<String, BannedScope> bannedScopes() { return this.bannedScopes; } public static final class BannedScope extends Record { @SerializedName("banId") private final UUID banId; @SerializedName("expires") @Nullable
/*    */       private final Instant expires; @SerializedName("reason")
/*    */       private final String reason; @SerializedName("reasonMessage")
/*    */       @Nullable
/* 67 */       private final String reasonMessage; public BannedScope(UUID param2UUID, @Nullable Instant param2Instant, String param2String1, @Nullable String param2String2) { this.banId = param2UUID; this.expires = param2Instant; this.reason = param2String1; this.reasonMessage = param2String2; } public final String toString() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)Ljava/lang/String;
/*    */         //   6: areturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #67	-> 0 } public final int hashCode() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #67	-> 0 } public final boolean equals(Object param2Object) { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/* 67 */         //   #67	-> 0 } @SerializedName("banId") public UUID banId() { return this.banId; } @SerializedName("expires") @Nullable public Instant expires() { return this.expires; } @SerializedName("reason") public String reason() { return this.reason; } @SerializedName("reasonMessage") @Nullable public String reasonMessage() { return this.reasonMessage; } } } public static final class BannedScope extends Record { @SerializedName("banId") private final UUID banId; @SerializedName("expires") @Nullable private final Instant expires; @SerializedName("reason") private final String reason; @SerializedName("reasonMessage") @Nullable private final String reasonMessage; public BannedScope(UUID param1UUID, @Nullable Instant param1Instant, String param1String1, @Nullable String param1String2) { this.banId = param1UUID; this.expires = param1Instant; this.reason = param1String1; this.reasonMessage = param1String2; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$BanStatus$BannedScope;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 67 */       //   #67	-> 0 } @SerializedName("banId") public UUID banId() { return this.banId; } @SerializedName("expires") @Nullable public Instant expires() { return this.expires; } @SerializedName("reason") public String reason() { return this.reason; } @SerializedName("reasonMessage") @Nullable public String reasonMessage() { return this.reasonMessage; }
/*    */      }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\response\UserAttributesResponse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */