/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class DarkOakTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, DarkOakTrunkPlacer::new));
/*    */   } public static final MapCodec<DarkOakTrunkPlacer> CODEC;
/*    */   public DarkOakTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 22 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 27 */     return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 32 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = Lists.newArrayList();
/*    */     
/* 34 */     BlockPos blockPos = paramBlockPos.below();
/* 35 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, paramTreeConfiguration);
/* 36 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.east(), paramTreeConfiguration);
/* 37 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.south(), paramTreeConfiguration);
/* 38 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos.south().east(), paramTreeConfiguration);
/*    */     
/* 40 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 41 */     int i = paramInt - paramRandomSource.nextInt(4);
/* 42 */     int j = 2 - paramRandomSource.nextInt(3);
/*    */     
/* 44 */     int k = paramBlockPos.getX();
/* 45 */     int m = paramBlockPos.getY();
/* 46 */     int n = paramBlockPos.getZ();
/*    */     
/* 48 */     int i1 = k;
/* 49 */     int i2 = n;
/* 50 */     int i3 = m + paramInt - 1;
/*    */     
/*    */     byte b;
/* 53 */     for (b = 0; b < paramInt; b++) {
/* 54 */       if (b >= i && j > 0) {
/* 55 */         i1 += direction.getStepX();
/* 56 */         i2 += direction.getStepZ();
/* 57 */         j--;
/*    */       } 
/*    */       
/* 60 */       int i4 = m + b;
/* 61 */       BlockPos blockPos1 = new BlockPos(i1, i4, i2);
/* 62 */       if (TreeFeature.isAirOrLeaves(paramLevelSimulatedReader, blockPos1)) {
/* 63 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1, paramTreeConfiguration);
/* 64 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1.east(), paramTreeConfiguration);
/* 65 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1.south(), paramTreeConfiguration);
/* 66 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1.east().south(), paramTreeConfiguration);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     arrayList.add(new FoliagePlacer.FoliageAttachment(new BlockPos(i1, i3, i2), 0, true));
/*    */ 
/*    */     
/* 73 */     for (b = -1; b <= 2; b++) {
/* 74 */       for (byte b1 = -1; b1 <= 2; b1++) {
/* 75 */         if (b < 0 || b > 1 || b1 < 0 || b1 > 1)
/*    */         {
/*    */           
/* 78 */           if (paramRandomSource.nextInt(3) <= 0) {
/*    */ 
/*    */             
/* 81 */             int i4 = paramRandomSource.nextInt(3) + 2;
/* 82 */             for (byte b2 = 0; b2 < i4; b2++) {
/* 83 */               placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, new BlockPos(k + b, i3 - b2 - 1, n + b1), paramTreeConfiguration);
/*    */             }
/*    */             
/* 86 */             arrayList.add(new FoliagePlacer.FoliageAttachment(new BlockPos(k + b, i3, n + b1), 0, false));
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 90 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\DarkOakTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */