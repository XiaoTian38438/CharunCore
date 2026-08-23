/*    */ package com.mojang.util;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UUIDTypeAdapter
/*    */   extends TypeAdapter<UUID>
/*    */ {
/*    */   public void write(JsonWriter paramJsonWriter, UUID paramUUID) throws IOException {
/* 13 */     paramJsonWriter.value(UndashedUuid.toString(paramUUID));
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID read(JsonReader paramJsonReader) throws IOException {
/* 18 */     return UndashedUuid.fromStringLenient(paramJsonReader.nextString());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojan\\util\UUIDTypeAdapter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */