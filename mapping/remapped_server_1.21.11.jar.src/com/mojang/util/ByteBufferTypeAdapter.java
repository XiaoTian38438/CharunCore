/*    */ package com.mojang.util;
/*    */ 
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Base64;
/*    */ 
/*    */ public class ByteBufferTypeAdapter
/*    */   extends TypeAdapter<ByteBuffer>
/*    */ {
/*    */   public void write(JsonWriter paramJsonWriter, ByteBuffer paramByteBuffer) throws IOException {
/* 15 */     paramJsonWriter.value(Base64.getEncoder().encodeToString(paramByteBuffer.array()));
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuffer read(JsonReader paramJsonReader) throws IOException {
/*    */     try {
/* 21 */       return ByteBuffer.wrap(Base64.getDecoder().decode(paramJsonReader.nextString()));
/* 22 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 23 */       throw new JsonParseException("Malformed base64 string", illegalArgumentException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojan\\util\ByteBufferTypeAdapter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */