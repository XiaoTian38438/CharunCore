/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class StraightTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, StraightTrunkPlacer::new));
/*    */   } public static final MapCodec<StraightTrunkPlacer> CODEC;
/*    */   public StraightTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 20 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 30 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.below(), paramTreeConfiguration);
/*    */     
/* 32 */     for (byte b = 0; b < paramInt; b++) {
/* 33 */       placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.above(b), paramTreeConfiguration);
/*    */     }
/* 35 */     return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(paramBlockPos.above(paramInt), 0, false));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\StraightTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */