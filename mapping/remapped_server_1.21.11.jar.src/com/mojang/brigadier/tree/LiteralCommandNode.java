/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ public class LiteralCommandNode<S>
/*     */   extends CommandNode<S>
/*     */ {
/*     */   private final String literal;
/*     */   private final String literalLowerCase;
/*     */   
/*     */   public LiteralCommandNode(String paramString, Command<S> paramCommand, Predicate<S> paramPredicate, CommandNode<S> paramCommandNode, RedirectModifier<S> paramRedirectModifier, boolean paramBoolean) {
/*  28 */     super(paramCommand, paramPredicate, paramCommandNode, paramRedirectModifier, paramBoolean);
/*  29 */     this.literal = paramString;
/*  30 */     this.literalLowerCase = paramString.toLowerCase(Locale.ROOT);
/*     */   }
/*     */   
/*     */   public String getLiteral() {
/*  34 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  39 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) throws CommandSyntaxException {
/*  44 */     int i = paramStringReader.getCursor();
/*  45 */     int j = parse(paramStringReader);
/*  46 */     if (j > -1) {
/*  47 */       paramCommandContextBuilder.withNode(this, StringRange.between(i, j));
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(paramStringReader, this.literal);
/*     */   }
/*     */   
/*     */   private int parse(StringReader paramStringReader) {
/*  55 */     int i = paramStringReader.getCursor();
/*  56 */     if (paramStringReader.canRead(this.literal.length())) {
/*  57 */       int j = i + this.literal.length();
/*  58 */       if (paramStringReader.getString().substring(i, j).equals(this.literal)) {
/*  59 */         paramStringReader.setCursor(j);
/*  60 */         if (!paramStringReader.canRead() || paramStringReader.peek() == ' ') {
/*  61 */           return j;
/*     */         }
/*  63 */         paramStringReader.setCursor(i);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/*  72 */     if (this.literalLowerCase.startsWith(paramSuggestionsBuilder.getRemainingLowerCase())) {
/*  73 */       return paramSuggestionsBuilder.suggest(this.literal).buildFuture();
/*     */     }
/*  75 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidInput(String paramString) {
/*  81 */     return (parse(new StringReader(paramString)) > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  86 */     if (this == paramObject) return true; 
/*  87 */     if (!(paramObject instanceof LiteralCommandNode)) return false;
/*     */     
/*  89 */     LiteralCommandNode literalCommandNode = (LiteralCommandNode)paramObject;
/*     */     
/*  91 */     if (!this.literal.equals(literalCommandNode.literal)) return false; 
/*  92 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsageText() {
/*  97 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     int i = this.literal.hashCode();
/* 103 */     i = 31 * i + super.hashCode();
/* 104 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public LiteralArgumentBuilder<S> createBuilder() {
/* 109 */     LiteralArgumentBuilder<S> literalArgumentBuilder = LiteralArgumentBuilder.literal(this.literal);
/* 110 */     literalArgumentBuilder.requires(getRequirement());
/* 111 */     literalArgumentBuilder.forward(getRedirect(), getRedirectModifier(), isFork());
/* 112 */     if (getCommand() != null) {
/* 113 */       literalArgumentBuilder.executes(getCommand());
/*     */     }
/* 115 */     return literalArgumentBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSortedKey() {
/* 120 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 125 */     return Collections.singleton(this.literal);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return "<literal " + this.literal + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\tree\LiteralCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */