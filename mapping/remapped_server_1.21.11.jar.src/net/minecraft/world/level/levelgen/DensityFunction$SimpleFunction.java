/*    */ package net.minecraft.world.level.levelgen;
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
/*    */ 
/*    */ 
/*    */ public interface SimpleFunction
/*    */   extends DensityFunction
/*    */ {
/*    */   default void fillArray(double[] paramArrayOfdouble, DensityFunction.ContextProvider paramContextProvider) {
/* 76 */     paramContextProvider.fillAllDirectly(paramArrayOfdouble, this);
/*    */   }
/*    */ 
/*    */   
/*    */   default DensityFunction mapAll(DensityFunction.Visitor paramVisitor) {
/* 81 */     return paramVisitor.apply(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\DensityFunction$SimpleFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */