/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaJungleFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and((App)Codec.intRange(0, 16).fieldOf("height").forGetter(())).apply((Applicative)paramInstance, MegaJungleFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<MegaJungleFoliagePlacer> CODEC;
/*    */   protected final int height;
/*    */   
/*    */   public MegaJungleFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, int paramInt) {
/* 20 */     super(paramIntProvider1, paramIntProvider2);
/* 21 */     this.height = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 26 */     return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 32 */     int i = paramFoliageAttachment.doubleTrunk() ? paramInt2 : (1 + paramRandomSource.nextInt(2));
/*    */     
/* 34 */     for (int j = paramInt4; j >= paramInt4 - i; j--) {
/* 35 */       int k = paramInt3 + paramFoliageAttachment.radiusOffset() + 1 - j;
/* 36 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFoliageAttachment.pos(), k, j, paramFoliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 42 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 47 */     if (paramInt1 + paramInt3 >= 7) {
/* 48 */       return true;
/*    */     }
/* 50 */     return (paramInt1 * paramInt1 + paramInt3 * paramInt3 > paramInt4 * paramInt4);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaJungleFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */