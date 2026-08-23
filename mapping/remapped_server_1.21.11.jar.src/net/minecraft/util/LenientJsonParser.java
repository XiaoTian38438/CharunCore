/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LenientJsonParser
/*    */ {
/*    */   public static JsonElement parse(Reader paramReader) throws JsonIOException, JsonSyntaxException {
/* 18 */     return JsonParser.parseReader(paramReader);
/*    */   }
/*    */   
/*    */   public static JsonElement parse(String paramString) throws JsonSyntaxException {
/* 22 */     return JsonParser.parseString(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\LenientJsonParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */