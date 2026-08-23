/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ public final class Event extends Record {
/*    */   @SerializedName("source")
/*    */   private final String source;
/*    */   @SerializedName("name")
/*    */   private final String name;
/*    */   @SerializedName("timestamp")
/*    */   private final long timestamp;
/*    */   @SerializedName("data")
/*    */   private final JsonObject data;
/*    */   
/*    */   @SerializedName("data")
/* 13 */   public JsonObject data() { return this.data; } @SerializedName("timestamp") public long timestamp() { return this.timestamp; } @SerializedName("name") public String name() { return this.name; } @SerializedName("source") public String source() { return this.source; } public Event(String paramString1, String paramString2, long paramLong, JsonObject paramJsonObject) { this.source = paramString1; this.name = paramString2; this.timestamp = paramLong; this.data = paramJsonObject; }
/*    */    public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */   }
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */   }
/*    */   public Event(String paramString1, String paramString2, Instant paramInstant, JsonObject paramJsonObject) {
/* 24 */     this(paramString1, paramString2, paramInstant.getEpochSecond(), paramJsonObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\request\TelemetryEventsRequest$Event.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */