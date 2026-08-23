/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public interface SignalGetter extends BlockGetter {
/*  12 */   public static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   default int getDirectSignal(BlockPos paramBlockPos, Direction paramDirection) {
/*  15 */     return getBlockState(paramBlockPos).getDirectSignal(this, paramBlockPos, paramDirection);
/*     */   }
/*     */   
/*     */   default int getDirectSignalTo(BlockPos paramBlockPos) {
/*  19 */     int i = 0;
/*  20 */     i = Math.max(i, getDirectSignal(paramBlockPos.below(), Direction.DOWN));
/*  21 */     if (i >= 15) {
/*  22 */       return i;
/*     */     }
/*  24 */     i = Math.max(i, getDirectSignal(paramBlockPos.above(), Direction.UP));
/*  25 */     if (i >= 15) {
/*  26 */       return i;
/*     */     }
/*  28 */     i = Math.max(i, getDirectSignal(paramBlockPos.north(), Direction.NORTH));
/*  29 */     if (i >= 15) {
/*  30 */       return i;
/*     */     }
/*  32 */     i = Math.max(i, getDirectSignal(paramBlockPos.south(), Direction.SOUTH));
/*  33 */     if (i >= 15) {
/*  34 */       return i;
/*     */     }
/*  36 */     i = Math.max(i, getDirectSignal(paramBlockPos.west(), Direction.WEST));
/*  37 */     if (i >= 15) {
/*  38 */       return i;
/*     */     }
/*  40 */     i = Math.max(i, getDirectSignal(paramBlockPos.east(), Direction.EAST));
/*  41 */     if (i >= 15) {
/*  42 */       return i;
/*     */     }
/*  44 */     return i;
/*     */   }
/*     */   
/*     */   default int getControlInputSignal(BlockPos paramBlockPos, Direction paramDirection, boolean paramBoolean) {
/*  48 */     BlockState blockState = getBlockState(paramBlockPos);
/*  49 */     if (paramBoolean) {
/*  50 */       return DiodeBlock.isDiode(blockState) ? getDirectSignal(paramBlockPos, paramDirection) : 0;
/*     */     }
/*     */     
/*  53 */     if (blockState.is(Blocks.REDSTONE_BLOCK)) {
/*  54 */       return 15;
/*     */     }
/*     */     
/*  57 */     if (blockState.is(Blocks.REDSTONE_WIRE)) {
/*  58 */       return ((Integer)blockState.getValue((Property)RedStoneWireBlock.POWER)).intValue();
/*     */     }
/*  60 */     if (blockState.isSignalSource()) {
/*  61 */       return getDirectSignal(paramBlockPos, paramDirection);
/*     */     }
/*  63 */     return 0;
/*     */   }
/*     */   
/*     */   default boolean hasSignal(BlockPos paramBlockPos, Direction paramDirection) {
/*  67 */     return (getSignal(paramBlockPos, paramDirection) > 0);
/*     */   }
/*     */   
/*     */   default int getSignal(BlockPos paramBlockPos, Direction paramDirection) {
/*  71 */     BlockState blockState = getBlockState(paramBlockPos);
/*     */     
/*  73 */     int i = blockState.getSignal(this, paramBlockPos, paramDirection);
/*  74 */     if (blockState.isRedstoneConductor(this, paramBlockPos)) {
/*  75 */       return Math.max(i, getDirectSignalTo(paramBlockPos));
/*     */     }
/*  77 */     return i;
/*     */   }
/*     */   
/*     */   default boolean hasNeighborSignal(BlockPos paramBlockPos) {
/*  81 */     if (getSignal(paramBlockPos.below(), Direction.DOWN) > 0) {
/*  82 */       return true;
/*     */     }
/*  84 */     if (getSignal(paramBlockPos.above(), Direction.UP) > 0) {
/*  85 */       return true;
/*     */     }
/*  87 */     if (getSignal(paramBlockPos.north(), Direction.NORTH) > 0) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (getSignal(paramBlockPos.south(), Direction.SOUTH) > 0) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (getSignal(paramBlockPos.west(), Direction.WEST) > 0) {
/*  94 */       return true;
/*     */     }
/*  96 */     return (getSignal(paramBlockPos.east(), Direction.EAST) > 0);
/*     */   }
/*     */   
/*     */   default int getBestNeighborSignal(BlockPos paramBlockPos) {
/* 100 */     int i = 0;
/*     */     
/* 102 */     for (Direction direction : DIRECTIONS) {
/* 103 */       int j = getSignal(paramBlockPos.relative(direction), direction);
/*     */       
/* 105 */       if (j >= 15) {
/* 106 */         return 15;
/*     */       }
/* 108 */       if (j > i) {
/* 109 */         i = j;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\SignalGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */