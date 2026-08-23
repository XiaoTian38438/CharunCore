/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaPineFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> foliagePlacerParts(paramInstance).and((App)IntProvider.codec(0, 24).fieldOf("crown_height").forGetter(())).apply((Applicative)paramInstance, MegaPineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<MegaPineFoliagePlacer> CODEC;
/*    */   private final IntProvider crownHeight;
/*    */   
/*    */   public MegaPineFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3) {
/* 21 */     super(paramIntProvider1, paramIntProvider2);
/* 22 */     this.crownHeight = paramIntProvider3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 27 */     return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 32 */     BlockPos blockPos = paramFoliageAttachment.pos();
/*    */     
/* 34 */     int i = 0;
/* 35 */     for (int j = blockPos.getY() - paramInt2 + paramInt4; j <= blockPos.getY() + paramInt4; j++) {
/* 36 */       int n, k = blockPos.getY() - j;
/* 37 */       int m = paramInt3 + paramFoliageAttachment.radiusOffset() + Mth.floor(k / paramInt2 * 3.5F);
/*    */       
/* 39 */       if (k > 0 && m == i && (j & 0x1) == 0) {
/* 40 */         n = m + 1;
/*    */       } else {
/* 42 */         n = m;
/*    */       } 
/*    */       
/* 45 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, new BlockPos(blockPos.getX(), j, blockPos.getZ()), n, 0, paramFoliageAttachment.doubleTrunk());
/* 46 */       i = m;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 52 */     return this.crownHeight.sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 57 */     if (paramInt1 + paramInt3 >= 7) {
/* 58 */       return true;
/*    */     }
/* 60 */     return (paramInt1 * paramInt1 + paramInt3 * paramInt3 > paramInt4 * paramInt4);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaPineFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */