/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsedArgument<S, T>
/*    */ {
/*    */   private final StringRange range;
/*    */   private final T result;
/*    */   
/*    */   public ParsedArgument(int paramInt1, int paramInt2, T paramT) {
/* 13 */     this.range = StringRange.between(paramInt1, paramInt2);
/* 14 */     this.result = paramT;
/*    */   }
/*    */   
/*    */   public StringRange getRange() {
/* 18 */     return this.range;
/*    */   }
/*    */   
/*    */   public T getResult() {
/* 22 */     return this.result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 27 */     if (this == paramObject) {
/* 28 */       return true;
/*    */     }
/* 30 */     if (!(paramObject instanceof ParsedArgument)) {
/* 31 */       return false;
/*    */     }
/* 33 */     ParsedArgument parsedArgument = (ParsedArgument)paramObject;
/* 34 */     return (Objects.equals(this.range, parsedArgument.range) && Objects.equals(this.result, parsedArgument.result));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 39 */     return Objects.hash(new Object[] { this.range, this.result });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\ParsedArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */