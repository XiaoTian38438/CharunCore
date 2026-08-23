/*    */ package com.mojang.brigadier.builder;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ 
/*    */ 
/*    */ public class RequiredArgumentBuilder<S, T>
/*    */   extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>>
/*    */ {
/*    */   private final String name;
/*    */   private final ArgumentType<T> type;
/* 14 */   private SuggestionProvider<S> suggestionsProvider = null;
/*    */   
/*    */   private RequiredArgumentBuilder(String paramString, ArgumentType<T> paramArgumentType) {
/* 17 */     this.name = paramString;
/* 18 */     this.type = paramArgumentType;
/*    */   }
/*    */   
/*    */   public static <S, T> RequiredArgumentBuilder<S, T> argument(String paramString, ArgumentType<T> paramArgumentType) {
/* 22 */     return new RequiredArgumentBuilder<>(paramString, paramArgumentType);
/*    */   }
/*    */   
/*    */   public RequiredArgumentBuilder<S, T> suggests(SuggestionProvider<S> paramSuggestionProvider) {
/* 26 */     this.suggestionsProvider = paramSuggestionProvider;
/* 27 */     return getThis();
/*    */   }
/*    */   
/*    */   public SuggestionProvider<S> getSuggestionsProvider() {
/* 31 */     return this.suggestionsProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RequiredArgumentBuilder<S, T> getThis() {
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public ArgumentType<T> getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   public ArgumentCommandNode<S, T> build() {
/* 48 */     ArgumentCommandNode<S, T> argumentCommandNode = new ArgumentCommandNode(getName(), getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());
/*    */     
/* 50 */     for (CommandNode<S> commandNode : getArguments()) {
/* 51 */       argumentCommandNode.addChild(commandNode);
/*    */     }
/*    */     
/* 54 */     return argumentCommandNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\builder\RequiredArgumentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */