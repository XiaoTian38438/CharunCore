/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.BitSet;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public final class RewriteResult<A, B> extends Record {
/*    */   private final View<A, B> view;
/*    */   private final BitSet recData;
/*    */   
/* 11 */   public RewriteResult(View<A, B> paramView, BitSet paramBitSet) { this.view = paramView; this.recData = paramBitSet; } public View<A, B> view() { return this.view; } public BitSet recData() { return this.recData; }
/*    */    public static <A, B> RewriteResult<A, B> create(View<A, B> paramView, BitSet paramBitSet) {
/* 13 */     return new RewriteResult<>(paramView, paramBitSet);
/*    */   }
/*    */   
/*    */   public static <A> RewriteResult<A, A> nop(Type<A> paramType) {
/* 17 */     return new RewriteResult<>(View.nopView(paramType), new BitSet());
/*    */   }
/*    */   
/*    */   public <C> RewriteResult<C, B> compose(RewriteResult<C, A> paramRewriteResult) {
/*    */     BitSet bitSet;
/* 22 */     if (this.view.type() instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType && paramRewriteResult.view.type() instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType) {
/*    */       
/* 24 */       bitSet = (BitSet)this.recData.clone();
/* 25 */       bitSet.or(paramRewriteResult.recData);
/*    */     } else {
/* 27 */       bitSet = this.recData;
/*    */     } 
/* 29 */     return create(this.view.compose(paramRewriteResult.view), bitSet);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return "RR[" + String.valueOf(this.view) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 39 */     if (this == paramObject) {
/* 40 */       return true;
/*    */     }
/* 42 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 43 */       return false;
/*    */     }
/* 45 */     RewriteResult rewriteResult = (RewriteResult)paramObject;
/* 46 */     return Objects.equals(this.view, rewriteResult.view);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return this.view.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\RewriteResult.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */