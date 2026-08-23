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
/*    */ public interface NeighborCombineResult<S>
/*    */ {
/*    */   <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> paramCombiner);
/*    */   
/*    */   public static final class Double<S>
/*    */     implements NeighborCombineResult<S>
/*    */   {
/*    */     private final S first;
/*    */     private final S second;
/*    */     
/*    */     public Double(S param2S1, S param2S2) {
/* 77 */       this.first = param2S1;
/* 78 */       this.second = param2S2;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> param2Combiner) {
/* 83 */       return param2Combiner.acceptDouble(this.first, this.second);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Single<S> implements NeighborCombineResult<S> {
/*    */     private final S single;
/*    */     
/*    */     public Single(S param2S) {
/* 91 */       this.single = param2S;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> param2Combiner) {
/* 96 */       return param2Combiner.acceptSingle(this.single);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$NeighborCombineResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */