/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class FancyFoliagePlacer extends BlobFoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> blobParts(paramInstance).apply((Applicative)paramInstance, FancyFoliagePlacer::new));
/*    */   } public static final MapCodec<FancyFoliagePlacer> CODEC;
/*    */   public FancyFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, int paramInt) {
/* 15 */     super(paramIntProvider1, paramIntProvider2, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 20 */     return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 25 */     for (int i = paramInt4; i >= paramInt4 - paramInt2; i--) {
/* 26 */       int j = paramInt3 + ((i == paramInt4 || i == paramInt4 - paramInt2) ? 0 : 1);
/* 27 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFoliageAttachment.pos(), j, i, paramFoliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 33 */     return (Mth.square(paramInt1 + 0.5F) + Mth.square(paramInt3 + 0.5F) > (paramInt4 * paramInt4));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FancyFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */