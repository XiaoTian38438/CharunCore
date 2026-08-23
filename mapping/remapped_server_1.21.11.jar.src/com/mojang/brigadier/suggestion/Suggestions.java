/*     */ package com.mojang.brigadier.suggestion;
/*     */ 
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Suggestions
/*     */ {
/*  17 */   private static final Suggestions EMPTY = new Suggestions(StringRange.at(0), new ArrayList<>());
/*     */   
/*     */   private final StringRange range;
/*     */   private final List<Suggestion> suggestions;
/*     */   
/*     */   public Suggestions(StringRange paramStringRange, List<Suggestion> paramList) {
/*  23 */     this.range = paramStringRange;
/*  24 */     this.suggestions = paramList;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/*  28 */     return this.range;
/*     */   }
/*     */   
/*     */   public List<Suggestion> getList() {
/*  32 */     return this.suggestions;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  36 */     return this.suggestions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if (this == paramObject) {
/*  42 */       return true;
/*     */     }
/*  44 */     if (!(paramObject instanceof Suggestions)) {
/*  45 */       return false;
/*     */     }
/*  47 */     Suggestions suggestions = (Suggestions)paramObject;
/*  48 */     return (Objects.equals(this.range, suggestions.range) && 
/*  49 */       Objects.equals(this.suggestions, suggestions.suggestions));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  54 */     return Objects.hash(new Object[] { this.range, this.suggestions });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  59 */     return "Suggestions{range=" + this.range + ", suggestions=" + this.suggestions + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Suggestions> empty() {
/*  66 */     return CompletableFuture.completedFuture(EMPTY);
/*     */   }
/*     */   
/*     */   public static Suggestions merge(String paramString, Collection<Suggestions> paramCollection) {
/*  70 */     if (paramCollection.isEmpty())
/*  71 */       return EMPTY; 
/*  72 */     if (paramCollection.size() == 1) {
/*  73 */       return paramCollection.iterator().next();
/*     */     }
/*     */     
/*  76 */     HashSet<Suggestion> hashSet = new HashSet();
/*  77 */     for (Suggestions suggestions : paramCollection) {
/*  78 */       hashSet.addAll(suggestions.getList());
/*     */     }
/*  80 */     return create(paramString, hashSet);
/*     */   }
/*     */   
/*     */   public static Suggestions create(String paramString, Collection<Suggestion> paramCollection) {
/*  84 */     if (paramCollection.isEmpty()) {
/*  85 */       return EMPTY;
/*     */     }
/*  87 */     int i = Integer.MAX_VALUE;
/*  88 */     int j = Integer.MIN_VALUE;
/*  89 */     for (Suggestion suggestion : paramCollection) {
/*  90 */       i = Math.min(suggestion.getRange().getStart(), i);
/*  91 */       j = Math.max(suggestion.getRange().getEnd(), j);
/*     */     } 
/*  93 */     StringRange stringRange = new StringRange(i, j);
/*  94 */     HashSet<Suggestion> hashSet = new HashSet();
/*  95 */     for (Suggestion suggestion : paramCollection) {
/*  96 */       hashSet.add(suggestion.expand(paramString, stringRange));
/*     */     }
/*  98 */     ArrayList<Suggestion> arrayList = new ArrayList<>(hashSet);
/*  99 */     arrayList.sort((paramSuggestion1, paramSuggestion2) -> paramSuggestion1.compareToIgnoreCase(paramSuggestion2));
/* 100 */     return new Suggestions(stringRange, arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\suggestion\Suggestions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */