/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoolArgumentType
/*    */   implements ArgumentType<Boolean>
/*    */ {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "true", "false" });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BoolArgumentType bool() {
/* 23 */     return new BoolArgumentType();
/*    */   }
/*    */   
/*    */   public static boolean getBool(CommandContext<?> paramCommandContext, String paramString) {
/* 27 */     return ((Boolean)paramCommandContext.getArgument(paramString, Boolean.class)).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 32 */     return Boolean.valueOf(paramStringReader.readBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/* 37 */     if ("true".startsWith(paramSuggestionsBuilder.getRemainingLowerCase())) {
/* 38 */       paramSuggestionsBuilder.suggest("true");
/*    */     }
/* 40 */     if ("false".startsWith(paramSuggestionsBuilder.getRemainingLowerCase())) {
/* 41 */       paramSuggestionsBuilder.suggest("false");
/*    */     }
/* 43 */     return paramSuggestionsBuilder.buildFuture();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 48 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\BoolArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */