/*    */ package net.minecraft.util.eventlog;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.Strictness;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.Closeable;
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface JsonEventLogReader<T> extends Closeable {
/*    */   static <T> JsonEventLogReader<T> create(final Codec<T> codec, Reader paramReader) {
/* 19 */     final JsonReader jsonReader = new JsonReader(paramReader);
/* 20 */     jsonReader.setStrictness(Strictness.LENIENT);
/* 21 */     return new JsonEventLogReader<T>()
/*    */       {
/*    */         public T next() throws IOException {
/*    */           try {
/* 25 */             if (!jsonReader.hasNext()) {
/* 26 */               return null;
/*    */             }
/* 28 */             JsonElement jsonElement = JsonParser.parseReader(jsonReader);
/* 29 */             return (T)codec.parse((DynamicOps)JsonOps.INSTANCE, jsonElement).getOrThrow(IOException::new);
/* 30 */           } catch (JsonParseException jsonParseException) {
/* 31 */             throw new IOException(jsonParseException);
/* 32 */           } catch (EOFException eOFException) {
/*    */             
/* 34 */             return null;
/*    */           } 
/*    */         }
/*    */ 
/*    */         
/*    */         public void close() throws IOException {
/* 40 */           jsonReader.close();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   T next() throws IOException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\eventlog\JsonEventLogReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */