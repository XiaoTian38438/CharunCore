/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class PineFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and((App)IntProvider.codec(0, 24).fieldOf("height").forGetter(())).apply((Applicative)paramInstance, PineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<PineFoliagePlacer> CODEC;
/*    */   private final IntProvider height;
/*    */   
/*    */   public PineFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3) {
/* 18 */     super(paramIntProvider1, paramIntProvider2);
/* 19 */     this.height = paramIntProvider3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 24 */     return FoliagePlacerType.PINE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 29 */     byte b = 0;
/*    */     
/* 31 */     for (int i = paramInt4; i >= paramInt4 - paramInt2; i--) {
/* 32 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFoliageAttachment.pos(), b, i, paramFoliageAttachment.doubleTrunk());
/*    */       
/* 34 */       if (b >= 1 && i == paramInt4 - paramInt2 + 1) {
/* 35 */         b--;
/* 36 */       } else if (b < paramInt3 + paramFoliageAttachment.radiusOffset()) {
/* 37 */         b++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageRadius(RandomSource paramRandomSource, int paramInt) {
/* 44 */     return super.foliageRadius(paramRandomSource, paramInt) + paramRandomSource.nextInt(Math.max(paramInt + 1, 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 49 */     return this.height.sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 54 */     return (paramInt1 == paramInt4 && paramInt3 == paramInt4 && paramInt4 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\PineFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */