/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class GiantTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, GiantTrunkPlacer::new));
/*    */   } public static final MapCodec<GiantTrunkPlacer> CODEC;
/*    */   public GiantTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 20 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.GIANT_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 30 */     BlockPos blockPos = paramBlockPos.below();
/* 31 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, paramTreeConfiguration);
/* 32 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.east(), paramTreeConfiguration);
/* 33 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.south(), paramTreeConfiguration);
/* 34 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.south().east(), paramTreeConfiguration);
/*    */     
/* 36 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 38 */     for (byte b = 0; b < paramInt; b++) {
/* 39 */       placeLogIfFreeWithOffset(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, mutableBlockPos, paramTreeConfiguration, paramBlockPos, 0, b, 0);
/*    */       
/* 41 */       if (b < paramInt - 1) {
/* 42 */         placeLogIfFreeWithOffset(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, mutableBlockPos, paramTreeConfiguration, paramBlockPos, 1, b, 0);
/* 43 */         placeLogIfFreeWithOffset(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, mutableBlockPos, paramTreeConfiguration, paramBlockPos, 1, b, 1);
/* 44 */         placeLogIfFreeWithOffset(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, mutableBlockPos, paramTreeConfiguration, paramBlockPos, 0, b, 1);
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(paramBlockPos.above(paramInt), 0, true));
/*    */   }
/*    */   
/*    */   private void placeLogIfFreeWithOffset(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, TreeConfiguration paramTreeConfiguration, BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3) {
/* 52 */     paramMutableBlockPos.setWithOffset((Vec3i)paramBlockPos, paramInt1, paramInt2, paramInt3);
/* 53 */     placeLogIfFree(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramMutableBlockPos, paramTreeConfiguration);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\GiantTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */