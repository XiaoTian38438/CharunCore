/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public class CheckerboardColumnBiomeSource extends BiomeSource {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Biome.LIST_CODEC.fieldOf("biomes").forGetter(()), (App)Codec.intRange(0, 62).fieldOf("scale").orElse(Integer.valueOf(2)).forGetter(())).apply((Applicative)paramInstance, CheckerboardColumnBiomeSource::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<CheckerboardColumnBiomeSource> CODEC;
/*    */   private final HolderSet<Biome> allowedBiomes;
/*    */   private final int bitShift;
/*    */   private final int size;
/*    */   
/*    */   public CheckerboardColumnBiomeSource(HolderSet<Biome> paramHolderSet, int paramInt) {
/* 22 */     this.allowedBiomes = paramHolderSet;
/* 23 */     this.bitShift = paramInt + 2;
/* 24 */     this.size = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 29 */     return this.allowedBiomes.stream();
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 34 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler) {
/* 39 */     return this.allowedBiomes.get(Math.floorMod((paramInt1 >> this.bitShift) + (paramInt3 >> this.bitShift), this.allowedBiomes.size()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\CheckerboardColumnBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */