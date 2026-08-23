/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 24 */   private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;
/*    */   
/*    */   public static Builder light() {
/* 27 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder setComposite(MinMaxBounds.Ints paramInts) {
/* 31 */     this.composite = paramInts;
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public LightPredicate build() {
/* 36 */     return new LightPredicate(this.composite);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\LightPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */