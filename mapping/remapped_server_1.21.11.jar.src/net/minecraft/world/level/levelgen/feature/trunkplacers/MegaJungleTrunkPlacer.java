/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class MegaJungleTrunkPlacer extends GiantTrunkPlacer {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, MegaJungleTrunkPlacer::new));
/*    */   } public static final MapCodec<MegaJungleTrunkPlacer> CODEC;
/*    */   public MegaJungleTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 21 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 26 */     return TrunkPlacerType.MEGA_JUNGLE_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 31 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = Lists.newArrayList();
/* 32 */     arrayList.addAll(super.placeTrunk(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramInt, paramBlockPos, paramTreeConfiguration));
/*    */     
/*    */     int i;
/* 35 */     for (i = paramInt - 2 - paramRandomSource.nextInt(4); i > paramInt / 2; i -= 2 + paramRandomSource.nextInt(4)) {
/* 36 */       float f = paramRandomSource.nextFloat() * 6.2831855F;
/* 37 */       int j = 0;
/* 38 */       int k = 0;
/*    */       
/* 40 */       for (byte b = 0; b < 5; b++) {
/* 41 */         j = (int)(1.5F + Mth.cos(f) * b);
/* 42 */         k = (int)(1.5F + Mth.sin(f) * b);
/* 43 */         BlockPos blockPos = paramBlockPos.offset(j, i - 3 + b / 2, k);
/* 44 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, paramTreeConfiguration);
/*    */       } 
/*    */       
/* 47 */       arrayList.add(new FoliagePlacer.FoliageAttachment(paramBlockPos.offset(j, i, k), -2, false));
/*    */     } 
/*    */     
/* 50 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\MegaJungleTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */