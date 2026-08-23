/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class AcaciaFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).apply((Applicative)paramInstance, AcaciaFoliagePlacer::new));
/*    */   } public static final MapCodec<AcaciaFoliagePlacer> CODEC;
/*    */   public AcaciaFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 15 */     super(paramIntProvider1, paramIntProvider2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 20 */     return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 25 */     boolean bool = paramFoliageAttachment.doubleTrunk();
/* 26 */     BlockPos blockPos = paramFoliageAttachment.pos().above(paramInt4);
/*    */     
/* 28 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + paramFoliageAttachment.radiusOffset(), -1 - paramInt2, bool);
/* 29 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 - 1, -paramInt2, bool);
/* 30 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, paramInt3 + paramFoliageAttachment.radiusOffset() - 1, 0, bool);
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 35 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 40 */     if (paramInt2 == 0)
/*    */     {
/* 42 */       return ((paramInt1 > 1 || paramInt3 > 1) && paramInt1 != 0 && paramInt3 != 0);
/*    */     }
/* 44 */     return (paramInt1 == paramInt4 && paramInt3 == paramInt4 && paramInt4 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\AcaciaFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */