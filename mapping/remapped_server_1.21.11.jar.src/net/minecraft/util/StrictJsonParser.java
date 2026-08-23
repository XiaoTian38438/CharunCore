/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import com.google.gson.Strictness;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.MalformedJsonException;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StringReader;
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
/*    */ public class StrictJsonParser
/*    */ {
/*    */   public static JsonElement parse(Reader paramReader) throws JsonIOException, JsonSyntaxException {
/*    */     try {
/* 29 */       JsonReader jsonReader = new JsonReader(paramReader);
/* 30 */       jsonReader.setStrictness(Strictness.STRICT);
/* 31 */       JsonElement jsonElement = JsonParser.parseReader(jsonReader);
/* 32 */       if (!jsonElement.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
/* 33 */         throw new JsonSyntaxException("Did not consume the entire document.");
/*    */       }
/* 35 */       return jsonElement;
/* 36 */     } catch (MalformedJsonException|NumberFormatException malformedJsonException) {
/* 37 */       throw new JsonSyntaxException(malformedJsonException);
/* 38 */     } catch (IOException iOException) {
/* 39 */       throw new JsonIOException(iOException);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static JsonElement parse(String paramString) throws JsonSyntaxException {
/* 47 */     return parse(new StringReader(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\StrictJsonParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */