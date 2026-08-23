/*    */ package net.minecraft.world.level.redstone;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface NeighborUpdater {
/* 19 */   public static final Direction[] UPDATE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };
/*    */   
/*    */   void shapeUpdate(Direction paramDirection, BlockState paramBlockState, BlockPos paramBlockPos1, BlockPos paramBlockPos2, @UpdateFlags int paramInt1, int paramInt2);
/*    */   
/*    */   void neighborChanged(BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation);
/*    */   
/*    */   void neighborChanged(BlockState paramBlockState, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean);
/*    */   
/*    */   default void updateNeighborsAtExceptFromFacing(BlockPos paramBlockPos, Block paramBlock, Direction paramDirection, Orientation paramOrientation) {
/* 28 */     for (Direction direction : UPDATE_ORDER) {
/* 29 */       if (direction != paramDirection) {
/* 30 */         neighborChanged(paramBlockPos.relative(direction), paramBlock, null);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   static void executeShapeUpdate(LevelAccessor paramLevelAccessor, Direction paramDirection, BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockState paramBlockState, @UpdateFlags int paramInt1, int paramInt2) {
/* 36 */     BlockState blockState1 = paramLevelAccessor.getBlockState(paramBlockPos1);
/* 37 */     if ((paramInt1 & 0x80) != 0 && blockState1.is(Blocks.REDSTONE_WIRE)) {
/*    */       return;
/*    */     }
/* 40 */     BlockState blockState2 = blockState1.updateShape((LevelReader)paramLevelAccessor, (ScheduledTickAccess)paramLevelAccessor, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState, paramLevelAccessor.getRandom());
/* 41 */     Block.updateOrDestroy(blockState1, blockState2, paramLevelAccessor, paramBlockPos1, paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   static void executeUpdate(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*    */     try {
/* 46 */       paramBlockState.handleNeighborChanged(paramLevel, paramBlockPos, paramBlock, paramOrientation, paramBoolean);
/* 47 */     } catch (Throwable throwable) {
/* 48 */       CrashReport crashReport = CrashReport.forThrowable(throwable, "Exception while updating neighbours");
/* 49 */       CrashReportCategory crashReportCategory = crashReport.addCategory("Block being updated");
/*    */       
/* 51 */       crashReportCategory.setDetail("Source block type", () -> {
/*    */             try {
/*    */               return String.format(Locale.ROOT, "ID #%s (%s // %s)", new Object[] { BuiltInRegistries.BLOCK.getKey(paramBlock), paramBlock.getDescriptionId(), paramBlock.getClass().getCanonicalName() });
/* 54 */             } catch (Throwable throwable) {
/*    */               return "ID #" + String.valueOf(BuiltInRegistries.BLOCK.getKey(paramBlock));
/*    */             } 
/*    */           });
/*    */       
/* 59 */       CrashReportCategory.populateBlockDetails(crashReportCategory, (LevelHeightAccessor)paramLevel, paramBlockPos, paramBlockState);
/*    */       
/* 61 */       throw new ReportedException(crashReport);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\NeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */