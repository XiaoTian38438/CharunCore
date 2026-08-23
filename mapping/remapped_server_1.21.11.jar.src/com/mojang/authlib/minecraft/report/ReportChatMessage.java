/*    */ package com.mojang.authlib.minecraft.report;
/*    */ public final class ReportChatMessage extends Record { @SerializedName("index")
/*    */   private final int index; @SerializedName("profileId")
/*    */   private final UUID profileId;
/*    */   @SerializedName("sessionId")
/*    */   private final UUID sessionId;
/*    */   @SerializedName("timestamp")
/*    */   private final Instant timestamp;
/*    */   
/* 10 */   public ReportChatMessage(int paramInt, UUID paramUUID1, UUID paramUUID2, Instant paramInstant, long paramLong, List<ByteBuffer> paramList, String paramString, ByteBuffer paramByteBuffer, boolean paramBoolean) { this.index = paramInt; this.profileId = paramUUID1; this.sessionId = paramUUID2; this.timestamp = paramInstant; this.salt = paramLong; this.lastSeen = paramList; this.message = paramString; this.signature = paramByteBuffer; this.messageReported = paramBoolean; } @SerializedName("salt") private final long salt; @SerializedName("lastSeen") private final List<ByteBuffer> lastSeen; @SerializedName("message") private final String message; @SerializedName("signature") private final ByteBuffer signature; @SerializedName("messageReported") private final boolean messageReported; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/minecraft/report/ReportChatMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0 } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/minecraft/report/ReportChatMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0 } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/minecraft/report/ReportChatMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 10 */     //   #10	-> 0 } @SerializedName("index") public int index() { return this.index; } @SerializedName("profileId") public UUID profileId() { return this.profileId; } @SerializedName("sessionId") public UUID sessionId() { return this.sessionId; } @SerializedName("timestamp") public Instant timestamp() { return this.timestamp; } @SerializedName("salt") public long salt() { return this.salt; } @SerializedName("lastSeen") public List<ByteBuffer> lastSeen() { return this.lastSeen; } @SerializedName("message") public String message() { return this.message; } @SerializedName("signature") public ByteBuffer signature() { return this.signature; } @SerializedName("messageReported") public boolean messageReported() { return this.messageReported; }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\report\ReportChatMessage.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */