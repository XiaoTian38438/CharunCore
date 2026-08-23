/*    */ package com.mojang.brigadier.suggestion;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuggestionsBuilder
/*    */ {
/*    */   private final String input;
/*    */   private final String inputLowerCase;
/*    */   private final int start;
/*    */   private final String remaining;
/*    */   private final String remainingLowerCase;
/* 20 */   private final List<Suggestion> result = new ArrayList<>();
/*    */   
/*    */   public SuggestionsBuilder(String paramString1, String paramString2, int paramInt) {
/* 23 */     this.input = paramString1;
/* 24 */     this.inputLowerCase = paramString2;
/* 25 */     this.start = paramInt;
/* 26 */     this.remaining = paramString1.substring(paramInt);
/* 27 */     this.remainingLowerCase = paramString2.substring(paramInt);
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder(String paramString, int paramInt) {
/* 31 */     this(paramString, paramString.toLowerCase(Locale.ROOT), paramInt);
/*    */   }
/*    */   
/*    */   public String getInput() {
/* 35 */     return this.input;
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 39 */     return this.start;
/*    */   }
/*    */   
/*    */   public String getRemaining() {
/* 43 */     return this.remaining;
/*    */   }
/*    */   
/*    */   public String getRemainingLowerCase() {
/* 47 */     return this.remainingLowerCase;
/*    */   }
/*    */   
/*    */   public Suggestions build() {
/* 51 */     return Suggestions.create(this.input, this.result);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Suggestions> buildFuture() {
/* 55 */     return CompletableFuture.completedFuture(build());
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(String paramString) {
/* 59 */     if (paramString.equals(this.remaining)) {
/* 60 */       return this;
/*    */     }
/* 62 */     this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), paramString));
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(String paramString, Message paramMessage) {
/* 67 */     if (paramString.equals(this.remaining)) {
/* 68 */       return this;
/*    */     }
/* 70 */     this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), paramString, paramMessage));
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(int paramInt) {
/* 75 */     this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), paramInt));
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(int paramInt, Message paramMessage) {
/* 80 */     this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), paramInt, paramMessage));
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder add(SuggestionsBuilder paramSuggestionsBuilder) {
/* 85 */     this.result.addAll(paramSuggestionsBuilder.result);
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder createOffset(int paramInt) {
/* 90 */     return new SuggestionsBuilder(this.input, this.inputLowerCase, paramInt);
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder restart() {
/* 94 */     return createOffset(this.start);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\suggestion\SuggestionsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */