/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.BitSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public final class BelowZeroRetrogen {
/*  28 */   private static final BitSet EMPTY = new BitSet(0); private static final Codec<BitSet> BITSET_CODEC;
/*     */   static {
/*  30 */     BITSET_CODEC = Codec.LONG_STREAM.xmap(paramLongStream -> BitSet.valueOf(paramLongStream.toArray()), paramBitSet -> LongStream.of(paramBitSet.toLongArray()));
/*  31 */     NON_EMPTY_CHUNK_STATUS = BuiltInRegistries.CHUNK_STATUS.byNameCodec().comapFlatMap(paramChunkStatus -> (paramChunkStatus == ChunkStatus.EMPTY) ? DataResult.error(()) : DataResult.success(paramChunkStatus), 
/*     */         
/*  33 */         Function.identity());
/*     */ 
/*     */     
/*  36 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)NON_EMPTY_CHUNK_STATUS.fieldOf("target_status").forGetter(BelowZeroRetrogen::targetStatus), (App)BITSET_CODEC.lenientOptionalFieldOf("missing_bedrock").forGetter(())).apply((Applicative)paramInstance, BelowZeroRetrogen::new));
/*     */   }
/*     */   
/*     */   private static final Codec<ChunkStatus> NON_EMPTY_CHUNK_STATUS;
/*     */   public static final Codec<BelowZeroRetrogen> CODEC;
/*  41 */   private static final Set<ResourceKey<Biome>> RETAINED_RETROGEN_BIOMES = Set.of(Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES, Biomes.DEEP_DARK);
/*  42 */   public static final LevelHeightAccessor UPGRADE_HEIGHT_ACCESSOR = new LevelHeightAccessor()
/*     */     {
/*     */       public int getHeight() {
/*  45 */         return 64;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getMinY() {
/*  50 */         return -64;
/*     */       }
/*     */     };
/*     */   
/*     */   private final ChunkStatus targetStatus;
/*     */   private final BitSet missingBedrock;
/*     */   
/*     */   private BelowZeroRetrogen(ChunkStatus paramChunkStatus, Optional<BitSet> paramOptional) {
/*  58 */     this.targetStatus = paramChunkStatus;
/*  59 */     this.missingBedrock = paramOptional.orElse(EMPTY);
/*     */   }
/*     */   
/*     */   public static void replaceOldBedrock(ProtoChunk paramProtoChunk) {
/*  63 */     byte b = 4;
/*  64 */     BlockPos.betweenClosed(0, 0, 0, 15, 4, 15).forEach(paramBlockPos -> {
/*     */           if (paramProtoChunk.getBlockState(paramBlockPos).is(Blocks.BEDROCK)) {
/*     */             paramProtoChunk.setBlockState(paramBlockPos, Blocks.DEEPSLATE.defaultBlockState());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void applyBedrockMask(ProtoChunk paramProtoChunk) {
/*  72 */     LevelHeightAccessor levelHeightAccessor = paramProtoChunk.getHeightAccessorForGeneration();
/*  73 */     int i = levelHeightAccessor.getMinY();
/*  74 */     int j = levelHeightAccessor.getMaxY();
/*     */     
/*  76 */     for (byte b = 0; b < 16; b++) {
/*  77 */       for (byte b1 = 0; b1 < 16; b1++) {
/*  78 */         if (hasBedrockHole(b, b1)) {
/*  79 */           BlockPos.betweenClosed(b, i, b1, b, j, b1).forEach(paramBlockPos -> paramProtoChunk.setBlockState(paramBlockPos, Blocks.AIR.defaultBlockState()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ChunkStatus targetStatus() {
/*  86 */     return this.targetStatus;
/*     */   }
/*     */   
/*     */   public boolean hasBedrockHoles() {
/*  90 */     return !this.missingBedrock.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean hasBedrockHole(int paramInt1, int paramInt2) {
/*  94 */     return this.missingBedrock.get((paramInt2 & 0xF) * 16 + (paramInt1 & 0xF));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BiomeResolver getBiomeResolver(BiomeResolver paramBiomeResolver, ChunkAccess paramChunkAccess) {
/*  99 */     if (!paramChunkAccess.isUpgrading()) {
/* 100 */       return paramBiomeResolver;
/*     */     }
/*     */     
/* 103 */     Objects.requireNonNull(RETAINED_RETROGEN_BIOMES); Predicate predicate = RETAINED_RETROGEN_BIOMES::contains;
/*     */     
/* 105 */     return (paramInt1, paramInt2, paramInt3, paramSampler) -> {
/*     */         Holder holder = paramBiomeResolver.getNoiseBiome(paramInt1, paramInt2, paramInt3, paramSampler);
/*     */         return holder.is(paramPredicate) ? holder : paramChunkAccess.getNoiseBiome(paramInt1, 0, paramInt3);
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\BelowZeroRetrogen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */