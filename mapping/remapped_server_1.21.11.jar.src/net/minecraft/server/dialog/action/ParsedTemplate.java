/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.functions.StringTemplate;
/*    */ 
/*    */ public class ParsedTemplate {
/*    */   public static final Codec<ParsedTemplate> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = Codec.STRING.comapFlatMap(ParsedTemplate::parse, paramParsedTemplate -> paramParsedTemplate.raw);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 16 */     VARIABLE_CODEC = Codec.STRING.validate(paramString -> StringTemplate.isValidVariableName(paramString) ? DataResult.success(paramString) : DataResult.error(()));
/*    */   }
/*    */   public static final Codec<String> VARIABLE_CODEC; private final String raw;
/*    */   private final StringTemplate parsed;
/*    */   
/*    */   private ParsedTemplate(String paramString, StringTemplate paramStringTemplate) {
/* 22 */     this.raw = paramString;
/* 23 */     this.parsed = paramStringTemplate;
/*    */   }
/*    */   
/*    */   private static DataResult<ParsedTemplate> parse(String paramString) {
/*    */     StringTemplate stringTemplate;
/*    */     try {
/* 29 */       stringTemplate = StringTemplate.fromString(paramString);
/* 30 */     } catch (Exception exception) {
/* 31 */       return DataResult.error(() -> "Failed to parse template " + paramString + ": " + paramException.getMessage());
/*    */     } 
/*    */     
/* 34 */     return DataResult.success(new ParsedTemplate(paramString, stringTemplate));
/*    */   }
/*    */   
/*    */   public String instantiate(Map<String, String> paramMap) {
/* 38 */     List list = this.parsed.variables().stream().map(paramString -> (String)paramMap.getOrDefault(paramString, "")).toList();
/* 39 */     return this.parsed.substitute(list);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\action\ParsedTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */