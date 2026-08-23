/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.QuartPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
/*    */ 
/*    */ public class TheEndBiomeSource extends BiomeSource {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)RegistryOps.retrieveElement(Biomes.THE_END), (App)RegistryOps.retrieveElement(Biomes.END_HIGHLANDS), (App)RegistryOps.retrieveElement(Biomes.END_MIDLANDS), (App)RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS), (App)RegistryOps.retrieveElement(Biomes.END_BARRENS)).apply((Applicative)paramInstance, paramInstance.stable(TheEndBiomeSource::new)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<TheEndBiomeSource> CODEC;
/*    */   
/*    */   private final Holder<Biome> end;
/*    */   
/*    */   private final Holder<Biome> highlands;
/*    */   
/*    */   private final Holder<Biome> midlands;
/*    */   private final Holder<Biome> islands;
/*    */   private final Holder<Biome> barrens;
/*    */   
/*    */   public static TheEndBiomeSource create(HolderGetter<Biome> paramHolderGetter) {
/* 31 */     return new TheEndBiomeSource((Holder<Biome>)paramHolderGetter
/* 32 */         .getOrThrow(Biomes.THE_END), (Holder<Biome>)paramHolderGetter
/* 33 */         .getOrThrow(Biomes.END_HIGHLANDS), (Holder<Biome>)paramHolderGetter
/* 34 */         .getOrThrow(Biomes.END_MIDLANDS), (Holder<Biome>)paramHolderGetter
/* 35 */         .getOrThrow(Biomes.SMALL_END_ISLANDS), (Holder<Biome>)paramHolderGetter
/* 36 */         .getOrThrow(Biomes.END_BARRENS));
/*    */   }
/*    */ 
/*    */   
/*    */   private TheEndBiomeSource(Holder<Biome> paramHolder1, Holder<Biome> paramHolder2, Holder<Biome> paramHolder3, Holder<Biome> paramHolder4, Holder<Biome> paramHolder5) {
/* 41 */     this.end = paramHolder1;
/* 42 */     this.highlands = paramHolder2;
/* 43 */     this.midlands = paramHolder3;
/* 44 */     this.islands = paramHolder4;
/* 45 */     this.barrens = paramHolder5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 50 */     return Stream.of((Holder<Biome>[])new Holder[] { this.end, this.highlands, this.midlands, this.islands, this.barrens });
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 55 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler) {
/* 60 */     int i = QuartPos.toBlock(paramInt1);
/* 61 */     int j = QuartPos.toBlock(paramInt2);
/* 62 */     int k = QuartPos.toBlock(paramInt3);
/*    */     
/* 64 */     int m = SectionPos.blockToSectionCoord(i);
/* 65 */     int n = SectionPos.blockToSectionCoord(k);
/*    */     
/* 67 */     if (m * m + n * n <= 4096L) {
/* 68 */       return this.end;
/*    */     }
/*    */     
/* 71 */     int i1 = (SectionPos.blockToSectionCoord(i) * 2 + 1) * 8;
/* 72 */     int i2 = (SectionPos.blockToSectionCoord(k) * 2 + 1) * 8;
/*    */     
/* 74 */     double d = paramSampler.erosion().compute((DensityFunction.FunctionContext)new DensityFunction.SinglePointContext(i1, j, i2));
/* 75 */     if (d > 0.25D) {
/* 76 */       return this.highlands;
/*    */     }
/*    */     
/* 79 */     if (d >= -0.0625D) {
/* 80 */       return this.midlands;
/*    */     }
/*    */     
/* 83 */     if (d < -0.21875D) {
/* 84 */       return this.islands;
/*    */     }
/*    */     
/* 87 */     return this.barrens;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\TheEndBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */