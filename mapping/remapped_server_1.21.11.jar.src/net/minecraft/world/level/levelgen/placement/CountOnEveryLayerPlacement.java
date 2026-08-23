/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ @Deprecated
/*    */ public class CountOnEveryLayerPlacement
/*    */   extends PlacementModifier {
/*    */   public static final MapCodec<CountOnEveryLayerPlacement> CODEC;
/*    */   private final IntProvider count;
/*    */   
/*    */   static {
/* 21 */     CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountOnEveryLayerPlacement::new, paramCountOnEveryLayerPlacement -> paramCountOnEveryLayerPlacement.count);
/*    */   }
/*    */ 
/*    */   
/*    */   private CountOnEveryLayerPlacement(IntProvider paramIntProvider) {
/* 26 */     this.count = paramIntProvider;
/*    */   }
/*    */   
/*    */   public static CountOnEveryLayerPlacement of(IntProvider paramIntProvider) {
/* 30 */     return new CountOnEveryLayerPlacement(paramIntProvider);
/*    */   }
/*    */   
/*    */   public static CountOnEveryLayerPlacement of(int paramInt) {
/* 34 */     return of((IntProvider)ConstantInt.of(paramInt));
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/*    */     boolean bool;
/* 39 */     Stream.Builder<?> builder = Stream.builder();
/*    */     
/* 41 */     byte b = 0;
/*    */     do {
/* 43 */       bool = false;
/*    */       
/* 45 */       for (byte b1 = 0; b1 < this.count.sample(paramRandomSource); b1++) {
/* 46 */         int i = paramRandomSource.nextInt(16) + paramBlockPos.getX();
/* 47 */         int j = paramRandomSource.nextInt(16) + paramBlockPos.getZ();
/* 48 */         int k = paramPlacementContext.getHeight(Heightmap.Types.MOTION_BLOCKING, i, j);
/* 49 */         int m = findOnGroundYPosition(paramPlacementContext, i, k, j, b);
/* 50 */         if (m != Integer.MAX_VALUE) {
/* 51 */           builder.add(new BlockPos(i, m, j));
/* 52 */           bool = true;
/*    */         } 
/*    */       } 
/* 55 */       b++;
/* 56 */     } while (bool);
/*    */     
/* 58 */     return (Stream)builder.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 63 */     return PlacementModifierType.COUNT_ON_EVERY_LAYER;
/*    */   }
/*    */ 
/*    */   
/*    */   private static int findOnGroundYPosition(PlacementContext paramPlacementContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 68 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(paramInt1, paramInt2, paramInt3);
/*    */     
/* 70 */     int i = 0;
/* 71 */     BlockState blockState = paramPlacementContext.getBlockState((BlockPos)mutableBlockPos);
/* 72 */     for (int j = paramInt2; j >= paramPlacementContext.getMinY() + 1; j--) {
/* 73 */       mutableBlockPos.setY(j - 1);
/* 74 */       BlockState blockState1 = paramPlacementContext.getBlockState((BlockPos)mutableBlockPos);
/* 75 */       if (!isEmpty(blockState1) && isEmpty(blockState) && !blockState1.is(Blocks.BEDROCK)) {
/* 76 */         if (i == paramInt4) {
/* 77 */           return mutableBlockPos.getY() + 1;
/*    */         }
/* 79 */         i++;
/*    */       } 
/* 81 */       blockState = blockState1;
/*    */     } 
/* 83 */     return Integer.MAX_VALUE;
/*    */   }
/*    */   
/*    */   private static boolean isEmpty(BlockState paramBlockState) {
/* 87 */     return (paramBlockState.isAir() || paramBlockState.is(Blocks.WATER) || paramBlockState.is(Blocks.LAVA));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\CountOnEveryLayerPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */