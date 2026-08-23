/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringArgumentType
/*    */   implements ArgumentType<String>
/*    */ {
/*    */   private final StringType type;
/*    */   
/*    */   private StringArgumentType(StringType paramStringType) {
/* 17 */     this.type = paramStringType;
/*    */   }
/*    */   
/*    */   public static StringArgumentType word() {
/* 21 */     return new StringArgumentType(StringType.SINGLE_WORD);
/*    */   }
/*    */   
/*    */   public static StringArgumentType string() {
/* 25 */     return new StringArgumentType(StringType.QUOTABLE_PHRASE);
/*    */   }
/*    */   
/*    */   public static StringArgumentType greedyString() {
/* 29 */     return new StringArgumentType(StringType.GREEDY_PHRASE);
/*    */   }
/*    */   
/*    */   public static String getString(CommandContext<?> paramCommandContext, String paramString) {
/* 33 */     return (String)paramCommandContext.getArgument(paramString, String.class);
/*    */   }
/*    */   
/*    */   public StringType getType() {
/* 37 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 42 */     if (this.type == StringType.GREEDY_PHRASE) {
/* 43 */       String str = paramStringReader.getRemaining();
/* 44 */       paramStringReader.setCursor(paramStringReader.getTotalLength());
/* 45 */       return str;
/* 46 */     }  if (this.type == StringType.SINGLE_WORD) {
/* 47 */       return paramStringReader.readUnquotedString();
/*    */     }
/* 49 */     return paramStringReader.readString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "string()";
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 60 */     return this.type.getExamples();
/*    */   }
/*    */   
/*    */   public static String escapeIfRequired(String paramString) {
/* 64 */     for (char c : paramString.toCharArray()) {
/* 65 */       if (!StringReader.isAllowedInUnquotedString(c)) {
/* 66 */         return escape(paramString);
/*    */       }
/*    */     } 
/* 69 */     return paramString;
/*    */   }
/*    */   
/*    */   private static String escape(String paramString) {
/* 73 */     StringBuilder stringBuilder = new StringBuilder("\"");
/*    */     
/* 75 */     for (byte b = 0; b < paramString.length(); b++) {
/* 76 */       char c = paramString.charAt(b);
/* 77 */       if (c == '\\' || c == '"') {
/* 78 */         stringBuilder.append('\\');
/*    */       }
/* 80 */       stringBuilder.append(c);
/*    */     } 
/*    */     
/* 83 */     stringBuilder.append("\"");
/* 84 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public enum StringType {
/* 88 */     SINGLE_WORD((String)new String[] { "word", "words_with_underscores" }),
/* 89 */     QUOTABLE_PHRASE((String)new String[] { "\"quoted phrase\"", "word", "\"\"" }),
/* 90 */     GREEDY_PHRASE((String)new String[] { "word", "words with spaces", "\"and symbols\"" });
/*    */     
/*    */     private final Collection<String> examples;
/*    */     
/*    */     StringType(String... param1VarArgs) {
/* 95 */       this.examples = Arrays.asList(param1VarArgs);
/*    */     }
/*    */     
/*    */     public Collection<String> getExamples() {
/* 99 */       return this.examples;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\StringArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */