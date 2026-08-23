/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ArgumentType<T>
/*    */ {
/*    */   T parse(StringReader paramStringReader) throws CommandSyntaxException;
/*    */   
/*    */   default <S> T parse(StringReader paramStringReader, S paramS) throws CommandSyntaxException {
/* 20 */     return parse(paramStringReader);
/*    */   }
/*    */   
/*    */   default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/* 24 */     return Suggestions.empty();
/*    */   }
/*    */   
/*    */   default Collection<String> getExamples() {
/* 28 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\ArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */