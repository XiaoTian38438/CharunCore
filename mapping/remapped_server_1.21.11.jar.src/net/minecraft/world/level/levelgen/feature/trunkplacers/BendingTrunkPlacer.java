/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class BendingTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).and(paramInstance.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", Integer.valueOf(1)).forGetter(()), (App)IntProvider.codec(1, 64).fieldOf("bend_length").forGetter(()))).apply((Applicative)paramInstance, BendingTrunkPlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BendingTrunkPlacer> CODEC;
/*    */   
/*    */   private final int minHeightForLeaves;
/*    */   
/*    */   private final IntProvider bendLength;
/*    */   
/*    */   public BendingTrunkPlacer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntProvider paramIntProvider) {
/* 32 */     super(paramInt1, paramInt2, paramInt3);
/*    */     
/* 34 */     this.minHeightForLeaves = paramInt4;
/* 35 */     this.bendLength = paramIntProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 40 */     return TrunkPlacerType.BENDING_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 45 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 46 */     int i = paramInt - 1;
/* 47 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 48 */     BlockPos blockPos = mutableBlockPos.below();
/*    */     
/* 50 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, paramTreeConfiguration);
/* 51 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = Lists.newArrayList();
/*    */     int j;
/* 53 */     for (j = 0; j <= i; j++) {
/*    */       
/* 55 */       if (j + 1 >= i + paramRandomSource.nextInt(2)) {
/* 56 */         mutableBlockPos.move(direction);
/*    */       }
/*    */       
/* 59 */       if (TreeFeature.validTreePos(paramLevelSimulatedReader, (BlockPos)mutableBlockPos)) {
/* 60 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)mutableBlockPos, paramTreeConfiguration);
/*    */       }
/*    */       
/* 63 */       if (j >= this.minHeightForLeaves) {
/* 64 */         arrayList.add(new FoliagePlacer.FoliageAttachment(mutableBlockPos.immutable(), 0, false));
/*    */       }
/*    */       
/* 67 */       mutableBlockPos.move(Direction.UP);
/*    */     } 
/*    */ 
/*    */     
/* 71 */     j = this.bendLength.sample(paramRandomSource);
/* 72 */     for (byte b = 0; b <= j; b++) {
/* 73 */       if (TreeFeature.validTreePos(paramLevelSimulatedReader, (BlockPos)mutableBlockPos)) {
/* 74 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)mutableBlockPos, paramTreeConfiguration);
/*    */       }
/*    */       
/* 77 */       arrayList.add(new FoliagePlacer.FoliageAttachment(mutableBlockPos.immutable(), 0, false));
/* 78 */       mutableBlockPos.move(direction);
/*    */     } 
/*    */     
/* 81 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\BendingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */