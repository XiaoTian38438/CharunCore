/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class DarkOakFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).apply((Applicative)paramInstance, DarkOakFoliagePlacer::new));
/*    */   } public static final MapCodec<DarkOakFoliagePlacer> CODEC;
/*    */   public DarkOakFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 15 */     super(paramIntProvider1, paramIntProvider2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 20 */     return FoliagePlacerType.DARK_OAK_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 25 */     BlockPos blockPos = paramFoliageAttachment.pos().above(paramInt4);
/* 26 */     boolean bool = paramFoliageAttachment.doubleTrunk();
/*    */     
/* 28 */     if (bool) {
/* 29 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + 2, -1, bool);
/* 30 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + 3, 0, bool);
/* 31 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + 2, 1, bool);
/* 32 */       if (paramRandomSource.nextBoolean()) {
/* 33 */         placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3, 2, bool);
/*    */       }
/*    */     } else {
/* 36 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + 2, -1, bool);
/* 37 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + 1, 0, bool);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 43 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocationSigned(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 48 */     if (paramInt2 == 0 && paramBoolean && (
/* 49 */       paramInt1 == -paramInt4 || paramInt1 >= paramInt4) && (paramInt3 == -paramInt4 || paramInt3 >= paramInt4)) {
/* 50 */       return true;
/*    */     }
/*    */     
/* 53 */     return super.shouldSkipLocationSigned(paramRandomSource, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 58 */     if (paramInt2 == -1 && !paramBoolean) {
/* 59 */       return (paramInt1 == paramInt4 && paramInt3 == paramInt4);
/*    */     }
/* 61 */     if (paramInt2 == 1) {
/* 62 */       return (paramInt1 + paramInt3 > paramInt4 * 2 - 2);
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\DarkOakFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */