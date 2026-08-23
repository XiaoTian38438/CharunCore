/*    */ package com.mojang.brigadier.suggestion;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntegerSuggestion
/*    */   extends Suggestion
/*    */ {
/*    */   private int value;
/*    */   
/*    */   public IntegerSuggestion(StringRange paramStringRange, int paramInt) {
/* 15 */     this(paramStringRange, paramInt, (Message)null);
/*    */   }
/*    */   
/*    */   public IntegerSuggestion(StringRange paramStringRange, int paramInt, Message paramMessage) {
/* 19 */     super(paramStringRange, Integer.toString(paramInt), paramMessage);
/* 20 */     this.value = paramInt;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 24 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 29 */     if (this == paramObject) {
/* 30 */       return true;
/*    */     }
/* 32 */     if (!(paramObject instanceof IntegerSuggestion)) {
/* 33 */       return false;
/*    */     }
/* 35 */     IntegerSuggestion integerSuggestion = (IntegerSuggestion)paramObject;
/* 36 */     return (this.value == integerSuggestion.value && super.equals(paramObject));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 41 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), Integer.valueOf(this.value) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "IntegerSuggestion{value=" + this.value + ", range=" + 
/*    */       
/* 48 */       getRange() + ", text='" + 
/* 49 */       getText() + '\'' + ", tooltip='" + 
/* 50 */       getTooltip() + '\'' + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(Suggestion paramSuggestion) {
/* 56 */     if (paramSuggestion instanceof IntegerSuggestion) {
/* 57 */       return Integer.compare(this.value, ((IntegerSuggestion)paramSuggestion).value);
/*    */     }
/* 59 */     return super.compareTo(paramSuggestion);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareToIgnoreCase(Suggestion paramSuggestion) {
/* 64 */     return compareTo(paramSuggestion);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\suggestion\IntegerSuggestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */