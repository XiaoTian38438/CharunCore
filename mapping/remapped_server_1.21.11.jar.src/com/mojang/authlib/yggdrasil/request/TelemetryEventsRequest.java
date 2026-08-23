/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ public final class TelemetryEventsRequest extends Record { @SerializedName("events")
/*    */   private final List<Event> events; public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */   }
/*  9 */   public TelemetryEventsRequest(List<Event> paramList) { this.events = paramList; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*  9 */     //   #9	-> 0 } @SerializedName("events") public List<Event> events() { return this.events; } public static final class Event extends Record { @SerializedName("source") private final String source; @SerializedName("name")
/*    */     private final String name; @SerializedName("timestamp")
/*    */     private final long timestamp; @SerializedName("data")
/*    */     private final JsonObject data; @SerializedName("data")
/* 13 */     public JsonObject data() { return this.data; } @SerializedName("timestamp") public long timestamp() { return this.timestamp; } @SerializedName("name") public String name() { return this.name; } @SerializedName("source") public String source() { return this.source; } public final boolean equals(Object param1Object) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0 } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0 } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/authlib/yggdrasil/request/TelemetryEventsRequest$Event;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 13 */       //   #13	-> 0 } public Event(String param1String1, String param1String2, long param1Long, JsonObject param1JsonObject) { this.source = param1String1; this.name = param1String2; this.timestamp = param1Long; this.data = param1JsonObject; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Event(String param1String1, String param1String2, Instant param1Instant, JsonObject param1JsonObject) {
/* 24 */       this(param1String1, param1String2, param1Instant.getEpochSecond(), param1JsonObject);
/*    */     } }
/*    */    }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\request\TelemetryEventsRequest.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */