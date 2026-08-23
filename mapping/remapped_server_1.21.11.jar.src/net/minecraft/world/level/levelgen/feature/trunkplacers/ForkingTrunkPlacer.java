/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class ForkingTrunkPlacer extends TrunkPlacer {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, ForkingTrunkPlacer::new));
/*    */   } public static final MapCodec<ForkingTrunkPlacer> CODEC;
/*    */   public ForkingTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/* 22 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 27 */     return TrunkPlacerType.FORKING_TRUNK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 32 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.below(), paramTreeConfiguration);
/*    */     
/* 34 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = Lists.newArrayList();
/*    */     
/* 36 */     Direction direction1 = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 37 */     int i = paramInt - paramRandomSource.nextInt(4) - 1;
/* 38 */     int j = 3 - paramRandomSource.nextInt(3);
/*    */     
/* 40 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 41 */     int k = paramBlockPos.getX();
/* 42 */     int m = paramBlockPos.getZ();
/* 43 */     OptionalInt optionalInt = OptionalInt.empty();
/* 44 */     for (byte b = 0; b < paramInt; b++) {
/* 45 */       int n = paramBlockPos.getY() + b;
/* 46 */       if (b >= i && j > 0) {
/* 47 */         k += direction1.getStepX();
/* 48 */         m += direction1.getStepZ();
/* 49 */         j--;
/*    */       } 
/* 51 */       if (placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)mutableBlockPos.set(k, n, m), paramTreeConfiguration)) {
/* 52 */         optionalInt = OptionalInt.of(n + 1);
/*    */       }
/*    */     } 
/*    */     
/* 56 */     if (optionalInt.isPresent()) {
/* 57 */       arrayList.add(new FoliagePlacer.FoliageAttachment(new BlockPos(k, optionalInt.getAsInt(), m), 1, false));
/*    */     }
/*    */     
/* 60 */     k = paramBlockPos.getX();
/* 61 */     m = paramBlockPos.getZ();
/* 62 */     Direction direction2 = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 63 */     if (direction2 != direction1) {
/* 64 */       int n = i - paramRandomSource.nextInt(2) - 1;
/* 65 */       int i1 = 1 + paramRandomSource.nextInt(3);
/*    */       
/* 67 */       optionalInt = OptionalInt.empty();
/* 68 */       for (int i2 = n; i2 < paramInt && i1 > 0; i2++, i1--) {
/* 69 */         if (i2 >= 1) {
/*    */ 
/*    */           
/* 72 */           int i3 = paramBlockPos.getY() + i2;
/* 73 */           k += direction2.getStepX();
/* 74 */           m += direction2.getStepZ();
/* 75 */           if (placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)mutableBlockPos.set(k, i3, m), paramTreeConfiguration))
/* 76 */             optionalInt = OptionalInt.of(i3 + 1); 
/*    */         } 
/*    */       } 
/* 79 */       if (optionalInt.isPresent()) {
/* 80 */         arrayList.add(new FoliagePlacer.FoliageAttachment(new BlockPos(k, optionalInt.getAsInt(), m), 0, false));
/*    */       }
/*    */     } 
/*    */     
/* 84 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\ForkingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */