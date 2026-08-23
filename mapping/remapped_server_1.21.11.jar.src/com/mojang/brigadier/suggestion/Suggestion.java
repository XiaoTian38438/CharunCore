/*     */ package com.mojang.brigadier.suggestion;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Suggestion
/*     */   implements Comparable<Suggestion>
/*     */ {
/*     */   private final StringRange range;
/*     */   private final String text;
/*     */   private final Message tooltip;
/*     */   
/*     */   public Suggestion(StringRange paramStringRange, String paramString) {
/*  17 */     this(paramStringRange, paramString, null);
/*     */   }
/*     */   
/*     */   public Suggestion(StringRange paramStringRange, String paramString, Message paramMessage) {
/*  21 */     this.range = paramStringRange;
/*  22 */     this.text = paramString;
/*  23 */     this.tooltip = paramMessage;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/*  27 */     return this.range;
/*     */   }
/*     */   
/*     */   public String getText() {
/*  31 */     return this.text;
/*     */   }
/*     */   
/*     */   public Message getTooltip() {
/*  35 */     return this.tooltip;
/*     */   }
/*     */   
/*     */   public String apply(String paramString) {
/*  39 */     if (this.range.getStart() == 0 && this.range.getEnd() == paramString.length()) {
/*  40 */       return this.text;
/*     */     }
/*  42 */     StringBuilder stringBuilder = new StringBuilder();
/*  43 */     if (this.range.getStart() > 0) {
/*  44 */       stringBuilder.append(paramString.substring(0, this.range.getStart()));
/*     */     }
/*  46 */     stringBuilder.append(this.text);
/*  47 */     if (this.range.getEnd() < paramString.length()) {
/*  48 */       stringBuilder.append(paramString.substring(this.range.getEnd()));
/*     */     }
/*  50 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  55 */     if (this == paramObject) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (!(paramObject instanceof Suggestion)) {
/*  59 */       return false;
/*     */     }
/*  61 */     Suggestion suggestion = (Suggestion)paramObject;
/*  62 */     return (Objects.equals(this.range, suggestion.range) && Objects.equals(this.text, suggestion.text) && Objects.equals(this.tooltip, suggestion.tooltip));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  67 */     return Objects.hash(new Object[] { this.range, this.text, this.tooltip });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return "Suggestion{range=" + this.range + ", text='" + this.text + '\'' + ", tooltip='" + this.tooltip + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Suggestion paramSuggestion) {
/*  81 */     return this.text.compareTo(paramSuggestion.text);
/*     */   }
/*     */   
/*     */   public int compareToIgnoreCase(Suggestion paramSuggestion) {
/*  85 */     return this.text.compareToIgnoreCase(paramSuggestion.text);
/*     */   }
/*     */   
/*     */   public Suggestion expand(String paramString, StringRange paramStringRange) {
/*  89 */     if (paramStringRange.equals(this.range)) {
/*  90 */       return this;
/*     */     }
/*  92 */     StringBuilder stringBuilder = new StringBuilder();
/*  93 */     if (paramStringRange.getStart() < this.range.getStart()) {
/*  94 */       stringBuilder.append(paramString.substring(paramStringRange.getStart(), this.range.getStart()));
/*     */     }
/*  96 */     stringBuilder.append(this.text);
/*  97 */     if (paramStringRange.getEnd() > this.range.getEnd()) {
/*  98 */       stringBuilder.append(paramString.substring(this.range.getEnd(), paramStringRange.getEnd()));
/*     */     }
/* 100 */     return new Suggestion(paramStringRange, stringBuilder.toString(), this.tooltip);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\suggestion\Suggestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */