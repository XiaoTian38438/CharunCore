/*    */ package com.mojang.util;
/*    */ 
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.format.DateTimeParseException;
/*    */ 
/*    */ public class InstantTypeAdapter
/*    */   extends TypeAdapter<Instant> {
/* 14 */   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;
/*    */ 
/*    */   
/*    */   public void write(JsonWriter paramJsonWriter, Instant paramInstant) throws IOException {
/* 18 */     paramJsonWriter.value(FORMATTER.format(paramInstant));
/*    */   }
/*    */ 
/*    */   
/*    */   public Instant read(JsonReader paramJsonReader) throws IOException {
/*    */     try {
/* 24 */       return Instant.from(FORMATTER.parse(paramJsonReader.nextString()));
/* 25 */     } catch (DateTimeParseException dateTimeParseException) {
/* 26 */       throw new JsonParseException("Malformed ISO instant format");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojan\\util\InstantTypeAdapter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */