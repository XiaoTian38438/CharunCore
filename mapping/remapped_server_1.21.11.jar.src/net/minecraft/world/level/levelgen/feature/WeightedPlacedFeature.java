/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class WeightedPlacedFeature {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(())).apply((Applicative)paramInstance, WeightedPlacedFeature::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<WeightedPlacedFeature> CODEC;
/*    */   public final Holder<PlacedFeature> feature;
/*    */   public final float chance;
/*    */   
/*    */   public WeightedPlacedFeature(Holder<PlacedFeature> paramHolder, float paramFloat) {
/* 22 */     this.feature = paramHolder;
/* 23 */     this.chance = paramFloat;
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 27 */     return ((PlacedFeature)this.feature.value()).place(paramWorldGenLevel, paramChunkGenerator, paramRandomSource, paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\WeightedPlacedFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */