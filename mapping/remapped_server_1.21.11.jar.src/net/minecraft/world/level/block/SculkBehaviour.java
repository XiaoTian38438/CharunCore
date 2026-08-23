/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public interface SculkBehaviour
/*    */ {
/*    */   default byte getSculkSpreadDelay() {
/* 15 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default void onDischarged(LevelAccessor paramLevelAccessor, BlockState paramBlockState, BlockPos paramBlockPos, RandomSource paramRandomSource) {}
/*    */   
/*    */   default boolean depositCharge(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   default boolean attemptSpreadVein(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, Collection<Direction> paramCollection, boolean paramBoolean) {
/* 26 */     return (((MultifaceSpreadeableBlock)Blocks.SCULK_VEIN).getSpreader().spreadAll(paramBlockState, paramLevelAccessor, paramBlockPos, paramBoolean) > 0L);
/*    */   }
/*    */   
/*    */   default boolean canChangeBlockStateOnSpread() {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   default int updateDecayDelay(int paramInt) {
/* 34 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final SculkBehaviour DEFAULT = new SculkBehaviour()
/*    */     {
/*    */       public boolean attemptSpreadVein(LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, BlockState param1BlockState, Collection<Direction> param1Collection, boolean param1Boolean) {
/* 42 */         if (param1Collection == null) {
/* 43 */           return (((SculkVeinBlock)Blocks.SCULK_VEIN).getSameSpaceSpreader().spreadAll(param1LevelAccessor.getBlockState(param1BlockPos), param1LevelAccessor, param1BlockPos, param1Boolean) > 0L);
/*    */         }
/* 45 */         if (!param1Collection.isEmpty()) {
/* 46 */           if (param1BlockState.isAir() || param1BlockState.getFluidState().is((Fluid)Fluids.WATER)) {
/* 47 */             return SculkVeinBlock.regrow(param1LevelAccessor, param1BlockPos, param1BlockState, param1Collection);
/*    */           }
/* 49 */           return false;
/*    */         } 
/* 51 */         return super.attemptSpreadVein(param1LevelAccessor, param1BlockPos, param1BlockState, param1Collection, param1Boolean);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public int attemptUseCharge(SculkSpreader.ChargeCursor param1ChargeCursor, LevelAccessor param1LevelAccessor, BlockPos param1BlockPos, RandomSource param1RandomSource, SculkSpreader param1SculkSpreader, boolean param1Boolean) {
/* 57 */         return (param1ChargeCursor.getDecayDelay() > 0) ? param1ChargeCursor.getCharge() : 0;
/*    */       }
/*    */ 
/*    */       
/*    */       public int updateDecayDelay(int param1Int) {
/* 62 */         return Math.max(param1Int - 1, 0);
/*    */       }
/*    */     };
/*    */   
/*    */   int attemptUseCharge(SculkSpreader.ChargeCursor paramChargeCursor, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, SculkSpreader paramSculkSpreader, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkBehaviour.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */