/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.LegacyTagFixer;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ 
/*     */ public class LegacyStructureDataHandler implements LegacyTagFixer {
/*     */   public static final int LAST_MONOLYTH_STRUCTURE_DATA_VERSION = 1493;
/*     */   
/*     */   static {
/*  33 */     CURRENT_TO_LEGACY_MAP = (Map<String, String>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("Village", "Village");
/*     */           
/*     */           paramHashMap.put("Mineshaft", "Mineshaft");
/*     */           
/*     */           paramHashMap.put("Mansion", "Mansion");
/*     */           paramHashMap.put("Igloo", "Temple");
/*     */           paramHashMap.put("Desert_Pyramid", "Temple");
/*     */           paramHashMap.put("Jungle_Pyramid", "Temple");
/*     */           paramHashMap.put("Swamp_Hut", "Temple");
/*     */           paramHashMap.put("Stronghold", "Stronghold");
/*     */           paramHashMap.put("Monument", "Monument");
/*     */           paramHashMap.put("Fortress", "Fortress");
/*     */           paramHashMap.put("EndCity", "EndCity");
/*     */         });
/*  48 */     LEGACY_TO_CURRENT_MAP = (Map<String, String>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("Iglu", "Igloo");
/*     */           paramHashMap.put("TeDP", "Desert_Pyramid");
/*     */           paramHashMap.put("TeJP", "Jungle_Pyramid");
/*     */           paramHashMap.put("TeSH", "Swamp_Hut");
/*     */         });
/*     */   }
/*     */   private static final Map<String, String> CURRENT_TO_LEGACY_MAP; private static final Map<String, String> LEGACY_TO_CURRENT_MAP;
/*  56 */   private static final Set<String> OLD_STRUCTURE_REGISTRY_KEYS = Set.of(new String[] { "pillager_outpost", "mineshaft", "mansion", "jungle_pyramid", "desert_pyramid", "igloo", "ruined_portal", "shipwreck", "swamp_hut", "stronghold", "monument", "ocean_ruin", "fortress", "endcity", "buried_treasure", "village", "nether_fossil", "bastion_remnant" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean hasLegacyData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final Map<String, Long2ObjectMap<CompoundTag>> dataMap = Maps.newHashMap();
/*  79 */   private final Map<String, StructureFeatureIndexSavedData> indexMap = Maps.newHashMap();
/*     */   private final DimensionDataStorage dimensionDataStorage;
/*     */   private final List<String> legacyKeys;
/*     */   private final List<String> currentKeys;
/*     */   private final DataFixer dataFixer;
/*     */   private boolean cachesInitialized;
/*     */   
/*     */   public LegacyStructureDataHandler(DimensionDataStorage paramDimensionDataStorage, List<String> paramList1, List<String> paramList2, DataFixer paramDataFixer) {
/*  87 */     this.dimensionDataStorage = paramDimensionDataStorage;
/*  88 */     this.legacyKeys = paramList1;
/*  89 */     this.currentKeys = paramList2;
/*  90 */     this.dataFixer = paramDataFixer;
/*     */ 
/*     */     
/*  93 */     int i = 0;
/*  94 */     for (String str : this.currentKeys) {
/*  95 */       i |= (this.dataMap.get(str) != null) ? 1 : 0;
/*     */     }
/*  97 */     this.hasLegacyData = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markChunkDone(ChunkPos paramChunkPos) {
/* 102 */     long l = paramChunkPos.toLong();
/* 103 */     for (String str : this.legacyKeys) {
/* 104 */       StructureFeatureIndexSavedData structureFeatureIndexSavedData = this.indexMap.get(str);
/* 105 */       if (structureFeatureIndexSavedData != null && structureFeatureIndexSavedData.hasUnhandledIndex(l)) {
/* 106 */         structureFeatureIndexSavedData.removeIndex(l);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int targetDataVersion() {
/* 113 */     return 1493;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag applyFix(CompoundTag paramCompoundTag) {
/* 118 */     if (!this.cachesInitialized && this.dimensionDataStorage != null) {
/* 119 */       populateCaches(this.dimensionDataStorage);
/*     */     }
/* 121 */     int i = NbtUtils.getDataVersion(paramCompoundTag);
/*     */     
/* 123 */     if (i < 1493) {
/* 124 */       paramCompoundTag = DataFixTypes.CHUNK.update(this.dataFixer, paramCompoundTag, i, 1493);
/*     */       
/* 126 */       if (((Boolean)paramCompoundTag.getCompound("Level").flatMap(paramCompoundTag -> paramCompoundTag.getBoolean("hasLegacyStructureData")).orElse(Boolean.valueOf(false))).booleanValue()) {
/* 127 */         paramCompoundTag = updateFromLegacy(paramCompoundTag);
/*     */       }
/*     */     } 
/* 130 */     return paramCompoundTag;
/*     */   }
/*     */   
/*     */   private CompoundTag updateFromLegacy(CompoundTag paramCompoundTag) {
/* 134 */     CompoundTag compoundTag1 = paramCompoundTag.getCompoundOrEmpty("Level");
/*     */     
/* 136 */     ChunkPos chunkPos = new ChunkPos(compoundTag1.getIntOr("xPos", 0), compoundTag1.getIntOr("zPos", 0));
/*     */     
/* 138 */     if (isUnhandledStructureStart(chunkPos.x, chunkPos.z)) {
/* 139 */       paramCompoundTag = updateStructureStart(paramCompoundTag, chunkPos);
/*     */     }
/*     */     
/* 142 */     CompoundTag compoundTag2 = compoundTag1.getCompoundOrEmpty("Structures");
/* 143 */     CompoundTag compoundTag3 = compoundTag2.getCompoundOrEmpty("References");
/*     */     
/* 145 */     for (String str : this.currentKeys) {
/* 146 */       boolean bool = OLD_STRUCTURE_REGISTRY_KEYS.contains(str.toLowerCase(Locale.ROOT));
/*     */       
/* 148 */       if (compoundTag3.getLongArray(str).isPresent() || !bool) {
/*     */         continue;
/*     */       }
/*     */       
/* 152 */       byte b = 8;
/* 153 */       LongArrayList longArrayList = new LongArrayList();
/*     */       
/* 155 */       for (int i = chunkPos.x - 8; i <= chunkPos.x + 8; i++) {
/* 156 */         for (int j = chunkPos.z - 8; j <= chunkPos.z + 8; j++) {
/* 157 */           if (hasLegacyStart(i, j, str)) {
/* 158 */             longArrayList.add(ChunkPos.asLong(i, j));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 163 */       compoundTag3.putLongArray(str, longArrayList.toLongArray());
/*     */     } 
/*     */     
/* 166 */     compoundTag2.put("References", (Tag)compoundTag3);
/* 167 */     compoundTag1.put("Structures", (Tag)compoundTag2);
/* 168 */     paramCompoundTag.put("Level", (Tag)compoundTag1);
/*     */     
/* 170 */     return paramCompoundTag;
/*     */   }
/*     */   
/*     */   private boolean hasLegacyStart(int paramInt1, int paramInt2, String paramString) {
/* 174 */     if (!this.hasLegacyData) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     if (this.dataMap.get(paramString) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(paramString))).hasStartIndex(ChunkPos.asLong(paramInt1, paramInt2))) {
/* 179 */       return true;
/*     */     }
/*     */     
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isUnhandledStructureStart(int paramInt1, int paramInt2) {
/* 186 */     if (!this.hasLegacyData) {
/* 187 */       return false;
/*     */     }
/*     */     
/* 190 */     for (String str : this.currentKeys) {
/* 191 */       if (this.dataMap.get(str) != null && ((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(str))).hasUnhandledIndex(ChunkPos.asLong(paramInt1, paramInt2))) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */   
/*     */   private CompoundTag updateStructureStart(CompoundTag paramCompoundTag, ChunkPos paramChunkPos) {
/* 199 */     CompoundTag compoundTag1 = paramCompoundTag.getCompoundOrEmpty("Level");
/* 200 */     CompoundTag compoundTag2 = compoundTag1.getCompoundOrEmpty("Structures");
/* 201 */     CompoundTag compoundTag3 = compoundTag2.getCompoundOrEmpty("Starts");
/*     */     
/* 203 */     for (String str : this.currentKeys) {
/* 204 */       Long2ObjectMap long2ObjectMap = this.dataMap.get(str);
/* 205 */       if (long2ObjectMap == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 209 */       long l = paramChunkPos.toLong();
/*     */       
/* 211 */       if (!((StructureFeatureIndexSavedData)this.indexMap.get(CURRENT_TO_LEGACY_MAP.get(str))).hasUnhandledIndex(l)) {
/*     */         continue;
/*     */       }
/*     */       
/* 215 */       CompoundTag compoundTag = (CompoundTag)long2ObjectMap.get(l);
/* 216 */       if (compoundTag == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 220 */       compoundTag3.put(str, (Tag)compoundTag);
/*     */     } 
/*     */     
/* 223 */     compoundTag2.put("Starts", (Tag)compoundTag3);
/* 224 */     compoundTag1.put("Structures", (Tag)compoundTag2);
/* 225 */     paramCompoundTag.put("Level", (Tag)compoundTag1);
/*     */     
/* 227 */     return paramCompoundTag;
/*     */   }
/*     */   
/*     */   private synchronized void populateCaches(DimensionDataStorage paramDimensionDataStorage) {
/* 231 */     if (this.cachesInitialized) {
/*     */       return;
/*     */     }
/*     */     
/* 235 */     for (Iterator<String> iterator = this.legacyKeys.iterator(); iterator.hasNext(); ) { String str1 = iterator.next();
/* 236 */       CompoundTag compoundTag = new CompoundTag();
/*     */       try {
/* 238 */         compoundTag = paramDimensionDataStorage.readTagFromDisk(str1, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES, 1493).getCompoundOrEmpty("data").getCompoundOrEmpty("Features");
/* 239 */         if (compoundTag.isEmpty()) {
/*     */           continue;
/*     */         }
/* 242 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 245 */       compoundTag.forEach((paramString, paramTag) -> {
/*     */             CompoundTag compoundTag;
/*     */             
/*     */             if (paramTag instanceof CompoundTag) {
/*     */               compoundTag = (CompoundTag)paramTag;
/*     */             } else {
/*     */               return;
/*     */             } 
/*     */             
/*     */             long l = ChunkPos.asLong(compoundTag.getIntOr("ChunkX", 0), compoundTag.getIntOr("ChunkZ", 0));
/*     */             ListTag listTag = compoundTag.getListOrEmpty("Children");
/*     */             if (!listTag.isEmpty()) {
/*     */               Optional optional = listTag.getCompound(0).flatMap(());
/*     */               Objects.requireNonNull(LEGACY_TO_CURRENT_MAP);
/*     */               optional.map(LEGACY_TO_CURRENT_MAP::get).ifPresent(());
/*     */             } 
/*     */             compoundTag.getString("id").ifPresent(());
/*     */           });
/* 263 */       String str2 = str1 + "_index";
/* 264 */       StructureFeatureIndexSavedData structureFeatureIndexSavedData = (StructureFeatureIndexSavedData)paramDimensionDataStorage.computeIfAbsent(StructureFeatureIndexSavedData.type(str2));
/*     */       
/* 266 */       if (structureFeatureIndexSavedData.getAll().isEmpty()) {
/*     */         
/* 268 */         StructureFeatureIndexSavedData structureFeatureIndexSavedData1 = new StructureFeatureIndexSavedData();
/* 269 */         this.indexMap.put(str1, structureFeatureIndexSavedData1);
/* 270 */         compoundTag.forEach((paramString, paramTag) -> {
/*     */               if (paramTag instanceof CompoundTag) {
/*     */                 CompoundTag compoundTag = (CompoundTag)paramTag; paramStructureFeatureIndexSavedData.addIndex(ChunkPos.asLong(compoundTag.getIntOr("ChunkX", 0), compoundTag.getIntOr("ChunkZ", 0)));
/*     */               } 
/*     */             }); continue;
/*     */       } 
/* 276 */       this.indexMap.put(str1, structureFeatureIndexSavedData); }
/*     */ 
/*     */ 
/*     */     
/* 280 */     this.cachesInitialized = true;
/*     */   }
/*     */   
/*     */   public static Supplier<LegacyTagFixer> getLegacyTagFixer(ResourceKey<Level> paramResourceKey, Supplier<DimensionDataStorage> paramSupplier, DataFixer paramDataFixer) {
/* 284 */     if (paramResourceKey == Level.OVERWORLD) {
/* 285 */       return () -> new LegacyStructureDataHandler(paramSupplier.get(), (List<String>)ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), (List<String>)ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"), paramDataFixer);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     if (paramResourceKey == Level.NETHER) {
/* 309 */       ImmutableList immutableList = ImmutableList.of("Fortress");
/* 310 */       return () -> new LegacyStructureDataHandler(paramSupplier.get(), paramList, paramList, paramDataFixer);
/* 311 */     }  if (paramResourceKey == Level.END) {
/* 312 */       ImmutableList immutableList = ImmutableList.of("EndCity");
/* 313 */       return () -> new LegacyStructureDataHandler(paramSupplier.get(), paramList, paramList, paramDataFixer);
/*     */     } 
/* 315 */     return LegacyTagFixer.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\LegacyStructureDataHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */