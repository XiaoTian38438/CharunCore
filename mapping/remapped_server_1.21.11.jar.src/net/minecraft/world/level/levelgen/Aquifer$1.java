/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.BlockState;
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
/*    */ class null
/*    */   implements Aquifer
/*    */ {
/*    */   public BlockState computeSubstance(DensityFunction.FunctionContext paramFunctionContext, double paramDouble) {
/* 64 */     if (paramDouble > 0.0D) {
/* 65 */       return null;
/*    */     }
/* 67 */     return fluidRule.computeFluid(paramFunctionContext.blockX(), paramFunctionContext.blockY(), paramFunctionContext.blockZ()).at(paramFunctionContext.blockY());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldScheduleFluidUpdate() {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\Aquifer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */