/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ 
/*     */ public class FlatLevelSource extends ChunkGenerator {
/*     */   static {
/*  32 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)FlatLevelGeneratorSettings.CODEC.fieldOf("settings").forGetter(FlatLevelSource::settings)).apply((Applicative)paramInstance, paramInstance.stable(FlatLevelSource::new)));
/*     */   }
/*     */   
/*     */   public static final MapCodec<FlatLevelSource> CODEC;
/*     */   private final FlatLevelGeneratorSettings settings;
/*     */   
/*     */   public FlatLevelSource(FlatLevelGeneratorSettings paramFlatLevelGeneratorSettings) {
/*  39 */     super((BiomeSource)new FixedBiomeSource(paramFlatLevelGeneratorSettings.getBiome()), Util.memoize(paramFlatLevelGeneratorSettings::adjustGenerationSettings));
/*  40 */     this.settings = paramFlatLevelGeneratorSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> paramHolderLookup, RandomState paramRandomState, long paramLong) {
/*  51 */     Stream stream = this.settings.structureOverrides().map(HolderSet::stream).orElseGet(() -> paramHolderLookup.listElements().map(()));
/*  52 */     return ChunkGeneratorStructureState.createForFlat(paramRandomState, paramLong, this.biomeSource, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends ChunkGenerator> codec() {
/*  57 */     return (MapCodec)CODEC;
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings settings() {
/*  61 */     return this.settings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess) {}
/*     */ 
/*     */   
/*     */   public int getSpawnHeight(LevelHeightAccessor paramLevelHeightAccessor) {
/*  70 */     return paramLevelHeightAccessor.getMinY() + Math.min(paramLevelHeightAccessor.getHeight(), this.settings.getLayers().size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/*  77 */     List<BlockState> list = this.settings.getLayers();
/*     */     
/*  79 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  80 */     Heightmap heightmap1 = paramChunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/*  81 */     Heightmap heightmap2 = paramChunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/*  83 */     for (byte b = 0; b < Math.min(paramChunkAccess.getHeight(), list.size()); b++) {
/*  84 */       BlockState blockState = list.get(b);
/*  85 */       if (blockState != null) {
/*     */ 
/*     */         
/*  88 */         int i = paramChunkAccess.getMinY() + b;
/*     */         
/*  90 */         for (byte b1 = 0; b1 < 16; b1++) {
/*  91 */           for (byte b2 = 0; b2 < 16; b2++) {
/*  92 */             paramChunkAccess.setBlockState((BlockPos)mutableBlockPos.set(b1, i, b2), blockState);
/*  93 */             heightmap1.update(b1, i, b2, blockState);
/*  94 */             heightmap2.update(b1, i, b2, blockState);
/*     */           } 
/*     */         } 
/*     */       } 
/*  98 */     }  return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 103 */     List<BlockState> list = this.settings.getLayers();
/* 104 */     for (int i = Math.min(list.size() - 1, paramLevelHeightAccessor.getMaxY()); i >= 0; i--) {
/* 105 */       BlockState blockState = list.get(i);
/* 106 */       if (blockState != null)
/*     */       {
/*     */         
/* 109 */         if (paramTypes.isOpaque().test(blockState))
/* 110 */           return paramLevelHeightAccessor.getMinY() + i + 1; 
/*     */       }
/*     */     } 
/* 113 */     return paramLevelHeightAccessor.getMinY();
/*     */   }
/*     */ 
/*     */   
/*     */   public NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/* 118 */     return new NoiseColumn(paramLevelHeightAccessor.getMinY(), (BlockState[])this.settings.getLayers().stream().limit(paramLevelHeightAccessor.getHeight()).map(paramBlockState -> (paramBlockState == null) ? Blocks.AIR.defaultBlockState() : paramBlockState).toArray(paramInt -> new BlockState[paramInt]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyCarvers(WorldGenRegion paramWorldGenRegion, long paramLong, RandomState paramRandomState, BiomeManager paramBiomeManager, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion paramWorldGenRegion) {}
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 135 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGenDepth() {
/* 140 */     return 384;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 146 */     return -63;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\FlatLevelSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */