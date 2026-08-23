/*    */ package net.minecraft.world.level.block;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Double<S>
/*    */   implements DoubleBlockCombiner.NeighborCombineResult<S>
/*    */ {
/*    */   private final S first;
/*    */   private final S second;
/*    */   
/*    */   public Double(S paramS1, S paramS2) {
/* 77 */     this.first = paramS1;
/* 78 */     this.second = paramS2;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> paramCombiner) {
/* 83 */     return paramCombiner.acceptDouble(this.first, this.second);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$NeighborCombineResult$Double.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */