/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ public class MultiNoiseBiomeSource extends BiomeSource {
/*  20 */   private static final MapCodec<Holder<Biome>> ENTRY_CODEC = Biome.CODEC.fieldOf("biome");
/*     */ 
/*     */   
/*  23 */   public static final MapCodec<Climate.ParameterList<Holder<Biome>>> DIRECT_CODEC = Climate.ParameterList.<T>codec((MapCodec)ENTRY_CODEC).fieldOf("biomes");
/*     */   
/*  25 */   private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
/*     */   public static final MapCodec<MultiNoiseBiomeSource> CODEC;
/*     */   private final Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters;
/*     */   
/*     */   static {
/*  30 */     CODEC = Codec.mapEither(DIRECT_CODEC, PRESET_CODEC).xmap(MultiNoiseBiomeSource::new, paramMultiNoiseBiomeSource -> paramMultiNoiseBiomeSource.parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MultiNoiseBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> paramEither) {
/*  38 */     this.parameters = paramEither;
/*     */   }
/*     */   
/*     */   public static MultiNoiseBiomeSource createFromList(Climate.ParameterList<Holder<Biome>> paramParameterList) {
/*  42 */     return new MultiNoiseBiomeSource(Either.left(paramParameterList));
/*     */   }
/*     */   
/*     */   public static MultiNoiseBiomeSource createFromPreset(Holder<MultiNoiseBiomeSourceParameterList> paramHolder) {
/*  46 */     return new MultiNoiseBiomeSource(Either.right(paramHolder));
/*     */   }
/*     */   
/*     */   private Climate.ParameterList<Holder<Biome>> parameters() {
/*  50 */     return (Climate.ParameterList<Holder<Biome>>)this.parameters.map(paramParameterList -> paramParameterList, paramHolder -> ((MultiNoiseBiomeSourceParameterList)paramHolder.value()).parameters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/*  58 */     return parameters().values().stream().map(Pair::getSecond);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends BiomeSource> codec() {
/*  63 */     return (MapCodec)CODEC;
/*     */   }
/*     */   
/*     */   public boolean stable(ResourceKey<MultiNoiseBiomeSourceParameterList> paramResourceKey) {
/*  67 */     Optional<Holder> optional = this.parameters.right();
/*  68 */     return (optional.isPresent() && ((Holder)optional.get()).is(paramResourceKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler) {
/*  73 */     return getNoiseBiome(paramSampler.sample(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Holder<Biome> getNoiseBiome(Climate.TargetPoint paramTargetPoint) {
/*  78 */     return parameters().findValue(paramTargetPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugInfo(List<String> paramList, BlockPos paramBlockPos, Climate.Sampler paramSampler) {
/*  83 */     int i = QuartPos.fromBlock(paramBlockPos.getX());
/*  84 */     int j = QuartPos.fromBlock(paramBlockPos.getY());
/*  85 */     int k = QuartPos.fromBlock(paramBlockPos.getZ());
/*  86 */     Climate.TargetPoint targetPoint = paramSampler.sample(i, j, k);
/*     */     
/*  88 */     float f1 = Climate.unquantizeCoord(targetPoint.continentalness());
/*  89 */     float f2 = Climate.unquantizeCoord(targetPoint.erosion());
/*  90 */     float f3 = Climate.unquantizeCoord(targetPoint.temperature());
/*  91 */     float f4 = Climate.unquantizeCoord(targetPoint.humidity());
/*  92 */     float f5 = Climate.unquantizeCoord(targetPoint.weirdness());
/*     */     
/*  94 */     double d = NoiseRouterData.peaksAndValleys(f5);
/*     */     
/*  96 */     OverworldBiomeBuilder overworldBiomeBuilder = new OverworldBiomeBuilder();
/*  97 */     paramList.add("Biome builder PV: " + 
/*  98 */         OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(d) + " C: " + overworldBiomeBuilder
/*  99 */         .getDebugStringForContinentalness(f1) + " E: " + overworldBiomeBuilder
/* 100 */         .getDebugStringForErosion(f2) + " T: " + overworldBiomeBuilder
/* 101 */         .getDebugStringForTemperature(f3) + " H: " + overworldBiomeBuilder
/* 102 */         .getDebugStringForHumidity(f4));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */