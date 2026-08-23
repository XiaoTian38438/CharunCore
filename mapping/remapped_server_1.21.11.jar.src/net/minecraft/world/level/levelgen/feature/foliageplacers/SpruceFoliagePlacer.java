/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class SpruceFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and((App)IntProvider.codec(0, 24).fieldOf("trunk_height").forGetter(())).apply((Applicative)paramInstance, SpruceFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SpruceFoliagePlacer> CODEC;
/*    */   private final IntProvider trunkHeight;
/*    */   
/*    */   public SpruceFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3) {
/* 19 */     super(paramIntProvider1, paramIntProvider2);
/* 20 */     this.trunkHeight = paramIntProvider3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 25 */     return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 30 */     BlockPos blockPos = paramFoliageAttachment.pos();
/*    */     
/* 32 */     int i = paramRandomSource.nextInt(2);
/* 33 */     int j = 1;
/* 34 */     boolean bool = false;
/*    */     
/* 36 */     for (int k = paramInt4; k >= -paramInt2; k--) {
/* 37 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, blockPos, i, k, paramFoliageAttachment.doubleTrunk());
/*    */       
/* 39 */       if (i >= j) {
/* 40 */         i = bool;
/* 41 */         bool = true;
/* 42 */         j = Math.min(j + 1, paramInt3 + paramFoliageAttachment.radiusOffset());
/*    */       } else {
/* 44 */         i++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 52 */     return Math.max(4, paramInt - this.trunkHeight.sample(paramRandomSource));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 57 */     return (paramInt1 == paramInt4 && paramInt3 == paramInt4 && paramInt4 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\SpruceFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */