/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ public final class AbuseReportRequest extends Record { @SerializedName("version")
/*    */   private final int version;
/*    */   @SerializedName("id")
/*    */   private final UUID id;
/*    */   @SerializedName("report")
/*    */   private final AbuseReport report;
/*    */   
/*  9 */   public AbuseReportRequest(int paramInt, UUID paramUUID, AbuseReport paramAbuseReport, ClientInfo paramClientInfo, @Nullable ThirdPartyServerInfo paramThirdPartyServerInfo, @Nullable RealmInfo paramRealmInfo, String paramString) { this.version = paramInt; this.id = paramUUID; this.report = paramAbuseReport; this.clientInfo = paramClientInfo; this.thirdPartyServerInfo = paramThirdPartyServerInfo; this.realmInfo = paramRealmInfo; this.reportType = paramString; } @SerializedName("clientInfo") private final ClientInfo clientInfo; @SerializedName("thirdPartyServerInfo") @Nullable private final ThirdPartyServerInfo thirdPartyServerInfo; @SerializedName("realmInfo") @Nullable private final RealmInfo realmInfo; @SerializedName("reportType") private final String reportType; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0 } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  9 */     //   #9	-> 0 } @SerializedName("version") public int version() { return this.version; } @SerializedName("id") public UUID id() { return this.id; } @SerializedName("report") public AbuseReport report() { return this.report; } @SerializedName("clientInfo") public ClientInfo clientInfo() { return this.clientInfo; } @SerializedName("thirdPartyServerInfo") @Nullable public ThirdPartyServerInfo thirdPartyServerInfo() { return this.thirdPartyServerInfo; } @SerializedName("realmInfo") @Nullable public RealmInfo realmInfo() { return this.realmInfo; } @SerializedName("reportType") public String reportType() { return this.reportType; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class ClientInfo
/*    */     extends Record
/*    */   {
/*    */     @SerializedName("clientVersion")
/*    */     private final String clientVersion;
/*    */     
/*    */     @SerializedName("locale")
/*    */     private final String locale;
/*    */ 
/*    */     
/*    */     public ClientInfo(String param1String1, String param1String2)
/*    */     {
/* 25 */       this.clientVersion = param1String1; this.locale = param1String2; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ClientInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 25 */       //   #25	-> 0 } @SerializedName("clientVersion") public String clientVersion() { return this.clientVersion; } @SerializedName("locale") public String locale() { return this.locale; }
/*    */   
/*    */   }
/*    */   
/*    */   public static final class ThirdPartyServerInfo extends Record
/*    */   {
/*    */     @SerializedName("address")
/*    */     private final String address;
/*    */     
/* 34 */     public ThirdPartyServerInfo(String param1String) { this.address = param1String; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ThirdPartyServerInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ThirdPartyServerInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$ThirdPartyServerInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 34 */       //   #34	-> 0 } @SerializedName("address") public String address() { return this.address; }
/*    */      }
/*    */   public static final class RealmInfo extends Record { @SerializedName("realmId")
/*    */     private final String realmId; @SerializedName("slotId")
/*    */     private final int slotId;
/*    */     
/* 40 */     public RealmInfo(String param1String, int param1Int) { this.realmId = param1String; this.slotId = param1Int; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$RealmInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$RealmInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest$RealmInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 40 */       //   #40	-> 0 } @SerializedName("realmId") public String realmId() { return this.realmId; } @SerializedName("slotId") public int slotId() { return this.slotId; }
/*    */      }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\request\AbuseReportRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */