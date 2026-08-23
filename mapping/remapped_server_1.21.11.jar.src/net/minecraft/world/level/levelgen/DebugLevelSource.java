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
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ 
/*     */ public class DebugLevelSource extends ChunkGenerator {
/*     */   public static final MapCodec<DebugLevelSource> CODEC;
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)RegistryOps.retrieveElement(Biomes.PLAINS)).apply((Applicative)paramInstance, paramInstance.stable(DebugLevelSource::new)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     ALL_BLOCKS = (List<BlockState>)StreamSupport.stream(BuiltInRegistries.BLOCK.spliterator(), false).flatMap(paramBlock -> paramBlock.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toList());
/*  42 */   } private static final int BLOCK_MARGIN = 2; private static final List<BlockState> ALL_BLOCKS; private static final int GRID_WIDTH = Mth.ceil(Mth.sqrt(ALL_BLOCKS.size()));
/*  43 */   private static final int GRID_HEIGHT = Mth.ceil(ALL_BLOCKS.size() / GRID_WIDTH);
/*     */   
/*  45 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  46 */   protected static final BlockState BARRIER = Blocks.BARRIER.defaultBlockState();
/*     */   
/*     */   public static final int HEIGHT = 70;
/*     */   public static final int BARRIER_HEIGHT = 60;
/*     */   
/*     */   public DebugLevelSource(Holder.Reference<Biome> paramReference) {
/*  52 */     super((BiomeSource)new FixedBiomeSource((Holder)paramReference));
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends ChunkGenerator> codec() {
/*  57 */     return (MapCodec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess) {}
/*     */ 
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel paramWorldGenLevel, ChunkAccess paramChunkAccess, StructureManager paramStructureManager) {
/*  66 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/*  68 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/*  69 */     int i = chunkPos.x;
/*  70 */     int j = chunkPos.z;
/*     */     
/*  72 */     for (byte b = 0; b < 16; b++) {
/*  73 */       for (byte b1 = 0; b1 < 16; b1++) {
/*  74 */         int k = SectionPos.sectionToBlockCoord(i, b);
/*  75 */         int m = SectionPos.sectionToBlockCoord(j, b1);
/*  76 */         paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos.set(k, 60, m), BARRIER, 2);
/*  77 */         BlockState blockState = getBlockStateFor(k, m);
/*  78 */         paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos.set(k, 70, m), blockState, 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess) {
/*  85 */     return CompletableFuture.completedFuture(paramChunkAccess);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/*  90 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState) {
/*  95 */     return new NoiseColumn(0, new BlockState[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos) {}
/*     */ 
/*     */   
/*     */   public static BlockState getBlockStateFor(int paramInt1, int paramInt2) {
/* 103 */     BlockState blockState = AIR;
/*     */     
/* 105 */     if (paramInt1 > 0 && paramInt2 > 0 && paramInt1 % 2 != 0 && paramInt2 % 2 != 0) {
/* 106 */       paramInt1 /= 2;
/* 107 */       paramInt2 /= 2;
/*     */       
/* 109 */       if (paramInt1 <= GRID_WIDTH && paramInt2 <= GRID_HEIGHT) {
/* 110 */         int i = Mth.abs(paramInt1 * GRID_WIDTH + paramInt2);
/* 111 */         if (i < ALL_BLOCKS.size()) {
/* 112 */           blockState = ALL_BLOCKS.get(i);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     return blockState;
/*     */   }
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
/* 130 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGenDepth() {
/* 135 */     return 384;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 140 */     return 63;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\DebugLevelSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */