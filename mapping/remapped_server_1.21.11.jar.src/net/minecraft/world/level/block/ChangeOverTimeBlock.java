/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ChangeOverTimeBlock<T extends Enum<T>>
/*    */ {
/*    */   public static final int SCAN_DISTANCE = 4;
/*    */   
/*    */   default void changeOverTime(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 22 */     float f = 0.05688889F;
/* 23 */     if (paramRandomSource.nextFloat() < 0.05688889F) {
/* 24 */       getNextState(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource).ifPresent(paramBlockState -> paramServerLevel.setBlockAndUpdate(paramBlockPos, paramBlockState));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Optional<BlockState> getNextState(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 35 */     int i = getAge().ordinal();
/* 36 */     byte b1 = 0;
/* 37 */     byte b2 = 0;
/* 38 */     for (BlockPos blockPos : BlockPos.withinManhattan(paramBlockPos, 4, 4, 4)) {
/* 39 */       int j = blockPos.distManhattan((Vec3i)paramBlockPos);
/* 40 */       if (j > 4) {
/*    */         break;
/*    */       }
/* 43 */       if (blockPos.equals(paramBlockPos)) {
/*    */         continue;
/*    */       }
/*    */       
/* 47 */       Block block = paramServerLevel.getBlockState(blockPos).getBlock(); if (block instanceof ChangeOverTimeBlock) { ChangeOverTimeBlock<Block> changeOverTimeBlock = (ChangeOverTimeBlock)block;
/*    */         
/* 49 */         block = changeOverTimeBlock.getAge();
/* 50 */         if (getAge().getClass() != block.getClass()) {
/*    */           continue;
/*    */         }
/* 53 */         int k = block.ordinal();
/* 54 */         if (k < i)
/* 55 */           return Optional.empty(); 
/* 56 */         if (k > i) {
/* 57 */           b2++; continue;
/*    */         } 
/* 59 */         b1++; }
/*    */     
/*    */     } 
/*    */ 
/*    */     
/* 64 */     float f1 = (b2 + 1) / (b2 + b1 + 1);
/* 65 */     float f2 = f1 * f1 * getChanceModifier();
/*    */     
/* 67 */     if (paramRandomSource.nextFloat() < f2) {
/* 68 */       return getNext(paramBlockState);
/*    */     }
/*    */     
/* 71 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   Optional<BlockState> getNext(BlockState paramBlockState);
/*    */   
/*    */   float getChanceModifier();
/*    */   
/*    */   T getAge();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ChangeOverTimeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */