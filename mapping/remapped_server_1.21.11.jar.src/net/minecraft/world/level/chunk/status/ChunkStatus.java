/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ public class ChunkStatus
/*     */ {
/*     */   public static final int MAX_STRUCTURE_DISTANCE = 8;
/*  19 */   private static final EnumSet<Heightmap.Types> WORLDGEN_HEIGHTMAPS = EnumSet.of(Heightmap.Types.OCEAN_FLOOR_WG, Heightmap.Types.WORLD_SURFACE_WG);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   public static final EnumSet<Heightmap.Types> FINAL_HEIGHTMAPS = EnumSet.of(Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE, Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static final ChunkStatus EMPTY = register("empty", null, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  32 */   public static final ChunkStatus STRUCTURE_STARTS = register("structure_starts", EMPTY, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  33 */   public static final ChunkStatus STRUCTURE_REFERENCES = register("structure_references", STRUCTURE_STARTS, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  34 */   public static final ChunkStatus BIOMES = register("biomes", STRUCTURE_REFERENCES, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  35 */   public static final ChunkStatus NOISE = register("noise", BIOMES, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  36 */   public static final ChunkStatus SURFACE = register("surface", NOISE, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  37 */   public static final ChunkStatus CARVERS = register("carvers", SURFACE, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  38 */   public static final ChunkStatus FEATURES = register("features", CARVERS, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  39 */   public static final ChunkStatus INITIALIZE_LIGHT = register("initialize_light", FEATURES, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  40 */   public static final ChunkStatus LIGHT = register("light", INITIALIZE_LIGHT, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  41 */   public static final ChunkStatus SPAWN = register("spawn", LIGHT, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  42 */   public static final ChunkStatus FULL = register("full", SPAWN, FINAL_HEIGHTMAPS, ChunkType.LEVELCHUNK);
/*     */   
/*  44 */   public static final Codec<ChunkStatus> CODEC = BuiltInRegistries.CHUNK_STATUS.byNameCodec(); private final int index; private final ChunkStatus parent;
/*     */   
/*     */   private static ChunkStatus register(String paramString, ChunkStatus paramChunkStatus, EnumSet<Heightmap.Types> paramEnumSet, ChunkType paramChunkType) {
/*  47 */     return (ChunkStatus)Registry.register((Registry)BuiltInRegistries.CHUNK_STATUS, paramString, new ChunkStatus(paramChunkStatus, paramEnumSet, paramChunkType));
/*     */   }
/*     */   private final ChunkType chunkType; private final EnumSet<Heightmap.Types> heightmapsAfter;
/*     */   public static List<ChunkStatus> getStatusList() {
/*  51 */     ArrayList<ChunkStatus> arrayList = Lists.newArrayList();
/*  52 */     ChunkStatus chunkStatus = FULL;
/*  53 */     while (chunkStatus.getParent() != chunkStatus) {
/*  54 */       arrayList.add(chunkStatus);
/*  55 */       chunkStatus = chunkStatus.getParent();
/*     */     } 
/*  57 */     arrayList.add(chunkStatus);
/*  58 */     Collections.reverse(arrayList);
/*  59 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected ChunkStatus(ChunkStatus paramChunkStatus, EnumSet<Heightmap.Types> paramEnumSet, ChunkType paramChunkType) {
/*  69 */     this.parent = (paramChunkStatus == null) ? this : paramChunkStatus;
/*  70 */     this.chunkType = paramChunkType;
/*  71 */     this.heightmapsAfter = paramEnumSet;
/*  72 */     this.index = (paramChunkStatus == null) ? 0 : (paramChunkStatus.getIndex() + 1);
/*     */   }
/*     */   
/*     */   public int getIndex() {
/*  76 */     return this.index;
/*     */   }
/*     */   
/*     */   public ChunkStatus getParent() {
/*  80 */     return this.parent;
/*     */   }
/*     */   
/*     */   public ChunkType getChunkType() {
/*  84 */     return this.chunkType;
/*     */   }
/*     */   
/*     */   public static ChunkStatus byName(String paramString) {
/*  88 */     return (ChunkStatus)BuiltInRegistries.CHUNK_STATUS.getValue(Identifier.tryParse(paramString));
/*     */   }
/*     */   
/*     */   public EnumSet<Heightmap.Types> heightmapsAfter() {
/*  92 */     return this.heightmapsAfter;
/*     */   }
/*     */   
/*     */   public boolean isOrAfter(ChunkStatus paramChunkStatus) {
/*  96 */     return (getIndex() >= paramChunkStatus.getIndex());
/*     */   }
/*     */   
/*     */   public boolean isAfter(ChunkStatus paramChunkStatus) {
/* 100 */     return (getIndex() > paramChunkStatus.getIndex());
/*     */   }
/*     */   
/*     */   public boolean isOrBefore(ChunkStatus paramChunkStatus) {
/* 104 */     return (getIndex() <= paramChunkStatus.getIndex());
/*     */   }
/*     */   
/*     */   public boolean isBefore(ChunkStatus paramChunkStatus) {
/* 108 */     return (getIndex() < paramChunkStatus.getIndex());
/*     */   }
/*     */   
/*     */   public static ChunkStatus max(ChunkStatus paramChunkStatus1, ChunkStatus paramChunkStatus2) {
/* 112 */     return paramChunkStatus1.isAfter(paramChunkStatus2) ? paramChunkStatus1 : paramChunkStatus2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return getName();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 121 */     return BuiltInRegistries.CHUNK_STATUS.getKey(this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */