/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PointedDripstoneBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DripstoneThickness;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DripstoneUtils
/*     */ {
/*     */   protected static double getDripstoneHeight(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/*  31 */     if (paramDouble1 < paramDouble4) {
/*  32 */       paramDouble1 = paramDouble4;
/*     */     }
/*     */ 
/*     */     
/*  36 */     double d1 = 0.384D;
/*  37 */     double d2 = paramDouble1 / paramDouble2 * 0.384D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     double d3 = 0.75D * Math.pow(d2, 1.3333333333333333D);
/*  43 */     double d4 = Math.pow(d2, 0.6666666666666666D);
/*  44 */     double d5 = 0.3333333333333333D * Math.log(d2);
/*  45 */     double d6 = paramDouble3 * (d3 - d4 - d5);
/*     */     
/*  47 */     d6 = Math.max(d6, 0.0D);
/*  48 */     return d6 / 0.384D * paramDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, int paramInt) {
/*  61 */     if (isEmptyOrWaterOrLava((LevelAccessor)paramWorldGenLevel, paramBlockPos)) {
/*  62 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  66 */     float f1 = 6.0F;
/*  67 */     float f2 = 6.0F / paramInt; float f3;
/*  68 */     for (f3 = 0.0F; f3 < 6.2831855F; f3 += f2) {
/*  69 */       int i = (int)(Mth.cos(f3) * paramInt);
/*  70 */       int j = (int)(Mth.sin(f3) * paramInt);
/*  71 */       if (isEmptyOrWaterOrLava((LevelAccessor)paramWorldGenLevel, paramBlockPos.offset(i, 0, j))) {
/*  72 */         return false;
/*     */       }
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyOrWater(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/*  79 */     return paramLevelAccessor.isStateAtPosition(paramBlockPos, DripstoneUtils::isEmptyOrWater);
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyOrWaterOrLava(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/*  83 */     return paramLevelAccessor.isStateAtPosition(paramBlockPos, DripstoneUtils::isEmptyOrWaterOrLava);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void buildBaseToTipColumn(Direction paramDirection, int paramInt, boolean paramBoolean, Consumer<BlockState> paramConsumer) {
/*  92 */     if (paramInt >= 3) {
/*  93 */       paramConsumer.accept(createPointedDripstone(paramDirection, DripstoneThickness.BASE));
/*  94 */       for (byte b = 0; b < paramInt - 3; b++) {
/*  95 */         paramConsumer.accept(createPointedDripstone(paramDirection, DripstoneThickness.MIDDLE));
/*     */       }
/*     */     } 
/*  98 */     if (paramInt >= 2) {
/*  99 */       paramConsumer.accept(createPointedDripstone(paramDirection, DripstoneThickness.FRUSTUM));
/*     */     }
/* 101 */     if (paramInt >= 1) {
/* 102 */       paramConsumer.accept(createPointedDripstone(paramDirection, paramBoolean ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void growPointedDripstone(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, Direction paramDirection, int paramInt, boolean paramBoolean) {
/* 107 */     if (!isDripstoneBase(paramLevelAccessor.getBlockState(paramBlockPos.relative(paramDirection.getOpposite())))) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 112 */     buildBaseToTipColumn(paramDirection, paramInt, paramBoolean, paramBlockState -> {
/*     */           if (paramBlockState.is(Blocks.POINTED_DRIPSTONE)) {
/*     */             paramBlockState = (BlockState)paramBlockState.setValue((Property)PointedDripstoneBlock.WATERLOGGED, Boolean.valueOf(paramLevelAccessor.isWaterAt((BlockPos)paramMutableBlockPos)));
/*     */           }
/*     */           paramLevelAccessor.setBlock((BlockPos)paramMutableBlockPos, paramBlockState, 2);
/*     */           paramMutableBlockPos.move(paramDirection);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static boolean placeDripstoneBlockIfPossible(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 122 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 123 */     if (blockState.is(BlockTags.DRIPSTONE_REPLACEABLE)) {
/* 124 */       paramLevelAccessor.setBlock(paramBlockPos, Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 2);
/* 125 */       return true;
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockState createPointedDripstone(Direction paramDirection, DripstoneThickness paramDripstoneThickness) {
/* 131 */     return (BlockState)((BlockState)Blocks.POINTED_DRIPSTONE.defaultBlockState()
/* 132 */       .setValue((Property)PointedDripstoneBlock.TIP_DIRECTION, (Comparable)paramDirection))
/* 133 */       .setValue((Property)PointedDripstoneBlock.THICKNESS, (Comparable)paramDripstoneThickness);
/*     */   }
/*     */   
/*     */   public static boolean isDripstoneBaseOrLava(BlockState paramBlockState) {
/* 137 */     return (isDripstoneBase(paramBlockState) || paramBlockState.is(Blocks.LAVA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDripstoneBase(BlockState paramBlockState) {
/* 144 */     return (paramBlockState.is(Blocks.DRIPSTONE_BLOCK) || paramBlockState.is(BlockTags.DRIPSTONE_REPLACEABLE));
/*     */   }
/*     */   
/*     */   public static boolean isEmptyOrWater(BlockState paramBlockState) {
/* 148 */     return (paramBlockState.isAir() || paramBlockState.is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public static boolean isNeitherEmptyNorWater(BlockState paramBlockState) {
/* 152 */     return (!paramBlockState.isAir() && !paramBlockState.is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public static boolean isEmptyOrWaterOrLava(BlockState paramBlockState) {
/* 156 */     return (paramBlockState.isAir() || paramBlockState.is(Blocks.WATER) || paramBlockState.is(Blocks.LAVA));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\DripstoneUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */