/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BushFoliagePlacer extends BlobFoliagePlacer {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> blobParts(paramInstance).apply((Applicative)paramInstance, BushFoliagePlacer::new));
/*    */   } public static final MapCodec<BushFoliagePlacer> CODEC;
/*    */   public BushFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, int paramInt) {
/* 14 */     super(paramIntProvider1, paramIntProvider2, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 19 */     return FoliagePlacerType.BUSH_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 24 */     for (int i = paramInt4; i >= paramInt4 - paramInt2; i--) {
/* 25 */       int j = paramInt3 + paramFoliageAttachment.radiusOffset() - 1 - i;
/* 26 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFoliageAttachment.pos(), j, i, paramFoliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 32 */     return (paramInt1 == paramInt4 && paramInt3 == paramInt4 && paramRandomSource.nextInt(2) == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BushFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */