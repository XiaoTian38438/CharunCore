/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class CherryFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and(paramInstance.group((App)IntProvider.codec(4, 16).fieldOf("height").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(()))).apply((Applicative)paramInstance, CherryFoliagePlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<CherryFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   private final float wideBottomLayerHoleChance;
/*    */   
/*    */   private final float cornerHoleChance;
/*    */   private final float hangingLeavesChance;
/*    */   private final float hangingLeavesExtensionChance;
/*    */   
/*    */   public CherryFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 28 */     super(paramIntProvider1, paramIntProvider2);
/* 29 */     this.height = paramIntProvider3;
/* 30 */     this.wideBottomLayerHoleChance = paramFloat1;
/* 31 */     this.cornerHoleChance = paramFloat2;
/* 32 */     this.hangingLeavesChance = paramFloat3;
/* 33 */     this.hangingLeavesExtensionChance = paramFloat4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 38 */     return FoliagePlacerType.CHERRY_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 64 */     boolean bool = paramFoliageAttachment.doubleTrunk();
/* 65 */     BlockPos blockPos = paramFoliageAttachment.pos().above(paramInt4);
/*    */     
/* 67 */     int i = paramInt3 + paramFoliageAttachment.radiusOffset() - 1;
/*    */     
/* 69 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i - 2, paramInt2 - 3, bool);
/* 70 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i - 1, paramInt2 - 4, bool);
/*    */     
/* 72 */     for (int j = paramInt2 - 5; j >= 0; j--) {
/* 73 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i, j, bool);
/*    */     }
/*    */     
/* 76 */     placeLeavesRowWithHangingLeavesBelow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i, -1, bool, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/* 77 */     placeLeavesRowWithHangingLeavesBelow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i - 1, -2, bool, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 82 */     return this.height.sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 87 */     if (paramInt2 == -1 && (paramInt1 == paramInt4 || paramInt3 == paramInt4) && paramRandomSource.nextFloat() < this.wideBottomLayerHoleChance) {
/* 88 */       return true;
/*    */     }
/*    */     
/* 91 */     boolean bool1 = (paramInt1 == paramInt4 && paramInt3 == paramInt4) ? true : false;
/* 92 */     boolean bool2 = (paramInt4 > 2) ? true : false;
/*    */     
/* 94 */     if (bool2)
/*    */     {
/* 96 */       return (bool1 || (paramInt1 + paramInt3 > paramInt4 * 2 - 2 && paramRandomSource.nextFloat() < this.cornerHoleChance));
/*    */     }
/* 98 */     return (bool1 && paramRandomSource.nextFloat() < this.cornerHoleChance);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\CherryFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */