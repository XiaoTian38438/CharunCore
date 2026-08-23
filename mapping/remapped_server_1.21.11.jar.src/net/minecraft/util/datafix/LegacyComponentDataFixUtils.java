/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LegacyComponentDataFixUtils
/*    */ {
/* 23 */   private static final String EMPTY_CONTENTS = createTextComponentJson("");
/*    */   
/*    */   public static <T> Dynamic<T> createPlainTextComponent(DynamicOps<T> paramDynamicOps, String paramString) {
/* 26 */     String str = createTextComponentJson(paramString);
/* 27 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createString(str));
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> createEmptyComponent(DynamicOps<T> paramDynamicOps) {
/* 31 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createString(EMPTY_CONTENTS));
/*    */   }
/*    */   
/*    */   public static String createTextComponentJson(String paramString) {
/* 35 */     JsonObject jsonObject = new JsonObject();
/* 36 */     jsonObject.addProperty("text", paramString);
/* 37 */     return GsonHelper.toStableString((JsonElement)jsonObject);
/*    */   }
/*    */   
/*    */   public static String createTranslatableComponentJson(String paramString) {
/* 41 */     JsonObject jsonObject = new JsonObject();
/* 42 */     jsonObject.addProperty("translate", paramString);
/* 43 */     return GsonHelper.toStableString((JsonElement)jsonObject);
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> createTranslatableComponent(DynamicOps<T> paramDynamicOps, String paramString) {
/* 47 */     String str = createTranslatableComponentJson(paramString);
/* 48 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createString(str));
/*    */   }
/*    */   
/*    */   public static String rewriteFromLenient(String paramString) {
/* 52 */     if (paramString.isEmpty() || paramString.equals("null")) {
/* 53 */       return EMPTY_CONTENTS;
/*    */     }
/*    */     
/* 56 */     char c1 = paramString.charAt(0);
/* 57 */     char c2 = paramString.charAt(paramString.length() - 1);
/* 58 */     if ((c1 == '"' && c2 == '"') || (c1 == '{' && c2 == '}') || (c1 == '[' && c2 == ']')) {
/*    */       try {
/* 60 */         JsonElement jsonElement = LenientJsonParser.parse(paramString);
/* 61 */         if (jsonElement.isJsonPrimitive()) {
/* 62 */           return createTextComponentJson(jsonElement.getAsString());
/*    */         }
/* 64 */         return GsonHelper.toStableString(jsonElement);
/* 65 */       } catch (JsonParseException jsonParseException) {}
/*    */     }
/*    */ 
/*    */     
/* 69 */     return createTextComponentJson(paramString);
/*    */   }
/*    */   
/*    */   public static boolean isStrictlyValidJson(Dynamic<?> paramDynamic) {
/* 73 */     return paramDynamic.asString().result()
/* 74 */       .filter(paramString -> {
/*    */           try {
/*    */             StrictJsonParser.parse(paramString);
/*    */             return true;
/* 78 */           } catch (JsonParseException jsonParseException) {
/*    */             
/*    */             return false;
/*    */           } 
/* 82 */         }).isPresent();
/*    */   }
/*    */   
/*    */   public static Optional<String> extractTranslationString(String paramString) {
/*    */     try {
/* 87 */       JsonElement jsonElement = LenientJsonParser.parse(paramString);
/* 88 */       if (jsonElement.isJsonObject()) {
/* 89 */         JsonObject jsonObject = jsonElement.getAsJsonObject();
/* 90 */         JsonElement jsonElement1 = jsonObject.get("translate");
/* 91 */         if (jsonElement1 != null && jsonElement1.isJsonPrimitive()) {
/* 92 */           return Optional.of(jsonElement1.getAsString());
/*    */         }
/*    */       } 
/* 95 */     } catch (JsonParseException jsonParseException) {}
/*    */ 
/*    */     
/* 98 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\LegacyComponentDataFixUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */