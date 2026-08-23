/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ParsedArgument;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentCommandNode<S, T>
/*     */   extends CommandNode<S>
/*     */ {
/*     */   private static final String USAGE_ARGUMENT_OPEN = "<";
/*     */   private static final String USAGE_ARGUMENT_CLOSE = ">";
/*     */   private final String name;
/*     */   private final ArgumentType<T> type;
/*     */   private final SuggestionProvider<S> customSuggestions;
/*     */   
/*     */   public ArgumentCommandNode(String paramString, ArgumentType<T> paramArgumentType, Command<S> paramCommand, Predicate<S> paramPredicate, CommandNode<S> paramCommandNode, RedirectModifier<S> paramRedirectModifier, boolean paramBoolean, SuggestionProvider<S> paramSuggestionProvider) {
/*  32 */     super(paramCommand, paramPredicate, paramCommandNode, paramRedirectModifier, paramBoolean);
/*  33 */     this.name = paramString;
/*  34 */     this.type = paramArgumentType;
/*  35 */     this.customSuggestions = paramSuggestionProvider;
/*     */   }
/*     */   
/*     */   public ArgumentType<T> getType() {
/*  39 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  44 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsageText() {
/*  49 */     return "<" + this.name + ">";
/*     */   }
/*     */   
/*     */   public SuggestionProvider<S> getCustomSuggestions() {
/*  53 */     return this.customSuggestions;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) throws CommandSyntaxException {
/*  58 */     int i = paramStringReader.getCursor();
/*  59 */     Object object = this.type.parse(paramStringReader, paramCommandContextBuilder.getSource());
/*  60 */     ParsedArgument parsedArgument = new ParsedArgument(i, paramStringReader.getCursor(), object);
/*     */     
/*  62 */     paramCommandContextBuilder.withArgument(this.name, parsedArgument);
/*  63 */     paramCommandContextBuilder.withNode(this, parsedArgument.getRange());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) throws CommandSyntaxException {
/*  68 */     if (this.customSuggestions == null) {
/*  69 */       return this.type.listSuggestions(paramCommandContext, paramSuggestionsBuilder);
/*     */     }
/*  71 */     return this.customSuggestions.getSuggestions(paramCommandContext, paramSuggestionsBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequiredArgumentBuilder<S, T> createBuilder() {
/*  77 */     RequiredArgumentBuilder<S, T> requiredArgumentBuilder = RequiredArgumentBuilder.argument(this.name, this.type);
/*  78 */     requiredArgumentBuilder.requires(getRequirement());
/*  79 */     requiredArgumentBuilder.forward(getRedirect(), getRedirectModifier(), isFork());
/*  80 */     requiredArgumentBuilder.suggests(this.customSuggestions);
/*  81 */     if (getCommand() != null) {
/*  82 */       requiredArgumentBuilder.executes(getCommand());
/*     */     }
/*  84 */     return requiredArgumentBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidInput(String paramString) {
/*     */     try {
/*  90 */       StringReader stringReader = new StringReader(paramString);
/*  91 */       this.type.parse(stringReader);
/*  92 */       return (!stringReader.canRead() || stringReader.peek() == ' ');
/*  93 */     } catch (CommandSyntaxException commandSyntaxException) {
/*  94 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 100 */     if (this == paramObject) return true; 
/* 101 */     if (!(paramObject instanceof ArgumentCommandNode)) return false;
/*     */     
/* 103 */     ArgumentCommandNode argumentCommandNode = (ArgumentCommandNode)paramObject;
/*     */     
/* 105 */     if (!this.name.equals(argumentCommandNode.name)) return false; 
/* 106 */     if (!this.type.equals(argumentCommandNode.type)) return false; 
/* 107 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     int i = this.name.hashCode();
/* 113 */     i = 31 * i + this.type.hashCode();
/* 114 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSortedKey() {
/* 119 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 124 */     return this.type.getExamples();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return "<argument " + this.name + ":" + this.type + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\tree\ArgumentCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */