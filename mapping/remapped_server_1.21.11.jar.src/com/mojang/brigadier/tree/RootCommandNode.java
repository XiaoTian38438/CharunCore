/*    */ package com.mojang.brigadier.tree;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RootCommandNode<S>
/*    */   extends CommandNode<S>
/*    */ {
/*    */   public RootCommandNode() {
/* 20 */     super(null, paramObject -> true, null, paramCommandContext -> Collections.singleton(paramCommandContext.getSource()), false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsageText() {
/* 30 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) throws CommandSyntaxException {}
/*    */ 
/*    */   
/*    */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/* 39 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidInput(String paramString) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 49 */     if (this == paramObject) return true; 
/* 50 */     if (!(paramObject instanceof RootCommandNode)) return false; 
/* 51 */     return super.equals(paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public ArgumentBuilder<S, ?> createBuilder() {
/* 56 */     throw new IllegalStateException("Cannot convert root into a builder");
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getSortedKey() {
/* 61 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 66 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "<root>";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\tree\RootCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */