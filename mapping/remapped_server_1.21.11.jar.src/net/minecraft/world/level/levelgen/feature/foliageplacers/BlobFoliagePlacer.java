/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BlobFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<BlobFoliagePlacer> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> blobParts(paramInstance).apply((Applicative)paramInstance, BlobFoliagePlacer::new));
/*    */   } protected final int height;
/*    */   protected static <P extends BlobFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer> blobParts(RecordCodecBuilder.Instance<P> paramInstance) {
/* 16 */     return foliagePlacerParts((RecordCodecBuilder.Instance)paramInstance).and(
/* 17 */         (App)Codec.intRange(0, 16).fieldOf("height").forGetter(paramBlobFoliagePlacer -> Integer.valueOf(paramBlobFoliagePlacer.height)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlobFoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2, int paramInt) {
/* 24 */     super(paramIntProvider1, paramIntProvider2);
/* 25 */     this.height = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 30 */     return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliagePlacer.FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliagePlacer.FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4) {
/* 35 */     for (int i = paramInt4; i >= paramInt4 - paramInt2; i--) {
/* 36 */       int j = Math.max(paramInt3 + paramFoliageAttachment.radiusOffset() - 1 - i / 2, 0);
/* 37 */       placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFoliageAttachment.pos(), j, i, paramFoliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration) {
/* 43 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 48 */     return (paramInt1 == paramInt4 && paramInt3 == paramInt4 && (paramRandomSource.nextInt(2) == 0 || paramInt2 == 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BlobFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */