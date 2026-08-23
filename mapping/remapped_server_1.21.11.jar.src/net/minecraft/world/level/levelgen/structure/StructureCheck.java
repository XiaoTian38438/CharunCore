/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.CollectFields;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureCheck
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int NO_STRUCTURE = -1;
/*     */   
/*     */   private final ChunkScanAccess storageAccess;
/*     */   
/*     */   private final RegistryAccess registryAccess;
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final ResourceKey<Level> dimension;
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   private final RandomState randomState;
/*     */   private final LevelHeightAccessor heightAccessor;
/*     */   private final BiomeSource biomeSource;
/*     */   private final long seed;
/*     */   private final DataFixer fixerUpper;
/*  59 */   private final Long2ObjectMap<Object2IntMap<Structure>> loadedChunks = (Long2ObjectMap<Object2IntMap<Structure>>)new Long2ObjectOpenHashMap();
/*  60 */   private final Map<Structure, Long2BooleanMap> featureChecks = new HashMap<>();
/*     */   
/*     */   public StructureCheck(ChunkScanAccess paramChunkScanAccess, RegistryAccess paramRegistryAccess, StructureTemplateManager paramStructureTemplateManager, ResourceKey<Level> paramResourceKey, ChunkGenerator paramChunkGenerator, RandomState paramRandomState, LevelHeightAccessor paramLevelHeightAccessor, BiomeSource paramBiomeSource, long paramLong, DataFixer paramDataFixer) {
/*  63 */     this.storageAccess = paramChunkScanAccess;
/*  64 */     this.registryAccess = paramRegistryAccess;
/*  65 */     this.structureTemplateManager = paramStructureTemplateManager;
/*  66 */     this.dimension = paramResourceKey;
/*  67 */     this.chunkGenerator = paramChunkGenerator;
/*  68 */     this.randomState = paramRandomState;
/*  69 */     this.heightAccessor = paramLevelHeightAccessor;
/*  70 */     this.biomeSource = paramBiomeSource;
/*  71 */     this.seed = paramLong;
/*  72 */     this.fixerUpper = paramDataFixer;
/*     */   }
/*     */   
/*     */   public StructureCheckResult checkStart(ChunkPos paramChunkPos, Structure paramStructure, StructurePlacement paramStructurePlacement, boolean paramBoolean) {
/*  76 */     long l = paramChunkPos.toLong();
/*  77 */     Object2IntMap<Structure> object2IntMap = (Object2IntMap)this.loadedChunks.get(l);
/*  78 */     if (object2IntMap != null) {
/*  79 */       return checkStructureInfo(object2IntMap, paramStructure, paramBoolean);
/*     */     }
/*     */ 
/*     */     
/*  83 */     StructureCheckResult structureCheckResult = tryLoadFromStorage(paramChunkPos, paramStructure, paramBoolean, l);
/*  84 */     if (structureCheckResult != null)
/*     */     {
/*  86 */       return structureCheckResult;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (!paramStructurePlacement.applyAdditionalChunkRestrictions(paramChunkPos.x, paramChunkPos.z, this.seed)) {
/*  95 */       return StructureCheckResult.START_NOT_PRESENT;
/*     */     }
/*     */ 
/*     */     
/*  99 */     boolean bool = ((Long2BooleanMap)this.featureChecks.computeIfAbsent(paramStructure, paramStructure -> new Long2BooleanOpenHashMap())).computeIfAbsent(l, paramLong -> canCreateStructure(paramChunkPos, paramStructure));
/*     */ 
/*     */ 
/*     */     
/* 103 */     if (!bool)
/*     */     {
/* 105 */       return StructureCheckResult.START_NOT_PRESENT;
/*     */     }
/*     */ 
/*     */     
/* 109 */     return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */   }
/*     */   
/*     */   private boolean canCreateStructure(ChunkPos paramChunkPos, Structure paramStructure) {
/* 113 */     Objects.requireNonNull(paramStructure.biomes()); return paramStructure.findValidGenerationPoint(new Structure.GenerationContext(this.registryAccess, this.chunkGenerator, this.biomeSource, this.randomState, this.structureTemplateManager, this.seed, paramChunkPos, this.heightAccessor, paramStructure.biomes()::contains)).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private StructureCheckResult tryLoadFromStorage(ChunkPos paramChunkPos, Structure paramStructure, boolean paramBoolean, long paramLong) {
/*     */     CompoundTag compoundTag2;
/* 120 */     CollectFields collectFields = new CollectFields(new FieldSelector[] { new FieldSelector(IntTag.TYPE, "DataVersion"), new FieldSelector("Level", "Structures", CompoundTag.TYPE, "Starts"), new FieldSelector("structures", CompoundTag.TYPE, "starts") });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 127 */       this.storageAccess.scanChunk(paramChunkPos, (StreamTagVisitor)collectFields).join();
/* 128 */     } catch (Exception exception) {
/* 129 */       LOGGER.warn("Failed to read chunk {}", paramChunkPos, exception);
/* 130 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/* 132 */     Tag tag = collectFields.getResult();
/* 133 */     if (!(tag instanceof CompoundTag))
/*     */     {
/* 135 */       return null;
/*     */     }
/*     */     
/* 138 */     CompoundTag compoundTag1 = (CompoundTag)tag;
/* 139 */     int i = NbtUtils.getDataVersion(compoundTag1);
/*     */     
/* 141 */     if (i <= 1493)
/*     */     {
/* 143 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     }
/*     */     
/* 146 */     SimpleRegionStorage.injectDatafixingContext(compoundTag1, ChunkMap.getChunkDataFixContextTag(this.dimension, this.chunkGenerator.getTypeNameForDataFixer()));
/*     */ 
/*     */     
/*     */     try {
/* 150 */       compoundTag2 = DataFixTypes.CHUNK.updateToCurrentVersion(this.fixerUpper, compoundTag1, i);
/* 151 */     } catch (Exception exception) {
/* 152 */       LOGGER.warn("Failed to partially datafix chunk {}", paramChunkPos, exception);
/*     */       
/* 154 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/*     */     
/* 157 */     Object2IntMap<Structure> object2IntMap = loadStructures(compoundTag2);
/* 158 */     if (object2IntMap == null)
/*     */     {
/* 160 */       return null;
/*     */     }
/*     */     
/* 163 */     storeFullResults(paramLong, object2IntMap);
/* 164 */     return checkStructureInfo(object2IntMap, paramStructure, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object2IntMap<Structure> loadStructures(CompoundTag paramCompoundTag) {
/* 169 */     Optional<CompoundTag> optional = paramCompoundTag.getCompound("structures").flatMap(paramCompoundTag -> paramCompoundTag.getCompound("starts"));
/* 170 */     if (optional.isEmpty()) {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     CompoundTag compoundTag = optional.get();
/* 175 */     if (compoundTag.isEmpty()) {
/* 176 */       return Object2IntMaps.emptyMap();
/*     */     }
/*     */     
/* 179 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 180 */     Registry registry = this.registryAccess.lookupOrThrow(Registries.STRUCTURE);
/* 181 */     compoundTag.forEach((paramString, paramTag) -> {
/*     */           Identifier identifier = Identifier.tryParse(paramString);
/*     */ 
/*     */           
/*     */           if (identifier == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*     */           Structure structure = (Structure)paramRegistry.getValue(identifier);
/*     */           
/*     */           if (structure == null) {
/*     */             return;
/*     */           }
/*     */           
/*     */           paramTag.asCompound().ifPresent(());
/*     */         });
/*     */     
/* 199 */     return (Object2IntMap<Structure>)object2IntOpenHashMap;
/*     */   }
/*     */   
/*     */   private static Object2IntMap<Structure> deduplicateEmptyMap(Object2IntMap<Structure> paramObject2IntMap) {
/* 203 */     return paramObject2IntMap.isEmpty() ? Object2IntMaps.emptyMap() : paramObject2IntMap;
/*     */   }
/*     */   
/*     */   private StructureCheckResult checkStructureInfo(Object2IntMap<Structure> paramObject2IntMap, Structure paramStructure, boolean paramBoolean) {
/* 207 */     int i = paramObject2IntMap.getOrDefault(paramStructure, -1);
/*     */     
/* 209 */     return (i != -1 && (!paramBoolean || i == 0)) ? StructureCheckResult.START_PRESENT : StructureCheckResult.START_NOT_PRESENT;
/*     */   }
/*     */   
/*     */   public void onStructureLoad(ChunkPos paramChunkPos, Map<Structure, StructureStart> paramMap) {
/* 213 */     long l = paramChunkPos.toLong();
/*     */     
/* 215 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 216 */     paramMap.forEach((paramStructure, paramStructureStart) -> {
/*     */           if (paramStructureStart.isValid()) {
/*     */             paramObject2IntMap.put(paramStructure, paramStructureStart.getReferences());
/*     */           }
/*     */         });
/* 221 */     storeFullResults(l, (Object2IntMap<Structure>)object2IntOpenHashMap);
/*     */   }
/*     */   
/*     */   private void storeFullResults(long paramLong, Object2IntMap<Structure> paramObject2IntMap) {
/* 225 */     this.loadedChunks.put(paramLong, deduplicateEmptyMap(paramObject2IntMap));
/*     */ 
/*     */     
/* 228 */     this.featureChecks.values().forEach(paramLong2BooleanMap -> paramLong2BooleanMap.remove(paramLong));
/*     */   }
/*     */   
/*     */   public void incrementReference(ChunkPos paramChunkPos, Structure paramStructure) {
/* 232 */     this.loadedChunks.compute(paramChunkPos.toLong(), (paramLong, paramObject2IntMap) -> {
/*     */           Object2IntOpenHashMap object2IntOpenHashMap;
/*     */           if (paramObject2IntMap == null || paramObject2IntMap.isEmpty())
/*     */             object2IntOpenHashMap = new Object2IntOpenHashMap(); 
/*     */           object2IntOpenHashMap.computeInt(paramStructure, ());
/*     */           return (Object2IntMap)object2IntOpenHashMap;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructureCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */