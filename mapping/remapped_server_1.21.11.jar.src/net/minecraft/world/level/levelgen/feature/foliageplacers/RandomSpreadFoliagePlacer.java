/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class RandomSpreadFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and(paramInstance.group((App)IntProvider.codec(1, 512).fieldOf("foliage_height").forGetter(()), (App)Codec.intRange(0, 256).fieldOf("leaf_placement_attempts").forGetter(()))).apply((Applicative)paramInstance, RandomSpreadFoliagePlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomSpreadFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider foliageHeight;
/*    */   
/*    */   private final int leafPlacementAttempts;
/*    */   
/*    */   public RandomSpreadFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3, int paramInt) {
/* 24 */     super(paramIntProvider1, paramIntProvider2);
/*    */     
/* 26 */     this.foliageHeight = paramIntProvider3;
/* 27 */     this.leafPlacementAttempts = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 32 */     return FoliagePlacerType.RANDOM_SPREAD_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 37 */     BlockPos blockPos = paramFoliageAttachment.pos();
/* 38 */     BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
/*    */     
/* 40 */     for (byte b = 0; b < this.leafPlacementAttempts; b++) {
/* 41 */       mutableBlockPos.setWithOffset((Vec3i)blockPos, paramRandomSource.nextInt(paramInt3) - paramRandomSource.nextInt(paramInt3), paramRandomSource.nextInt(paramInt2) - paramRandomSource.nextInt(paramInt2), paramRandomSource.nextInt(paramInt3) - paramRandomSource.nextInt(paramInt3));
/* 42 */       tryPlaceLeaf(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, (BlockPos)mutableBlockPos);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 48 */     return this.foliageHeight.sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\RandomSpreadFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */