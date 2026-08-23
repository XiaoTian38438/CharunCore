/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class ChunkHeightAndBiomeFix
/*     */   extends DataFix {
/*     */   public static final String DATAFIXER_CONTEXT_TAG = "__context";
/*     */   private static final String NAME = "ChunkHeightAndBiomeFix";
/*     */   private static final int OLD_SECTION_COUNT = 16;
/*     */   private static final int NEW_SECTION_COUNT = 24;
/*     */   private static final int NEW_MIN_SECTION_Y = -4;
/*     */   public static final int BLOCKS_PER_SECTION = 4096;
/*     */   private static final int LONGS_PER_SECTION = 64;
/*     */   private static final int HEIGHTMAP_BITS = 9;
/*     */   private static final long HEIGHTMAP_MASK = 511L;
/*     */   private static final int HEIGHTMAP_OFFSET = 64;
/*  54 */   private static final String[] HEIGHTMAP_TYPES = new String[] { "WORLD_SURFACE_WG", "WORLD_SURFACE", "WORLD_SURFACE_IGNORE_SNOW", "OCEAN_FLOOR_WG", "OCEAN_FLOOR", "MOTION_BLOCKING", "MOTION_BLOCKING_NO_LEAVES" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final Set<String> STATUS_IS_OR_AFTER_SURFACE = Set.of("surface", "carvers", "liquid_carvers", "features", "light", "spawn", "heightmaps", "full");
/*  65 */   private static final Set<String> STATUS_IS_OR_AFTER_NOISE = Set.of("noise", "surface", "carvers", "liquid_carvers", "features", "light", "spawn", "heightmaps", "full");
/*     */   
/*  67 */   private static final Set<String> BLOCKS_BEFORE_FEATURE_STATUS = Set.of(new String[] { "minecraft:air", "minecraft:basalt", "minecraft:bedrock", "minecraft:blackstone", "minecraft:calcite", "minecraft:cave_air", "minecraft:coarse_dirt", "minecraft:crimson_nylium", "minecraft:dirt", "minecraft:end_stone", "minecraft:grass_block", "minecraft:gravel", "minecraft:ice", "minecraft:lava", "minecraft:mycelium", "minecraft:nether_wart_block", "minecraft:netherrack", "minecraft:orange_terracotta", "minecraft:packed_ice", "minecraft:podzol", "minecraft:powder_snow", "minecraft:red_sand", "minecraft:red_sandstone", "minecraft:sand", "minecraft:sandstone", "minecraft:snow_block", "minecraft:soul_sand", "minecraft:soul_soil", "minecraft:stone", "minecraft:terracotta", "minecraft:warped_nylium", "minecraft:warped_wart_block", "minecraft:water", "minecraft:white_terracotta" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_LAYER_SIZE = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_SIZE = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BIOME_CONTAINER_TOP_LAYER_OFFSET = 1008;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_BIOME = "minecraft:plains";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private static final Int2ObjectMap<String> BIOMES_BY_ID = (Int2ObjectMap<String>)new Int2ObjectOpenHashMap();
/*     */   
/*     */   static {
/* 112 */     BIOMES_BY_ID.put(0, "minecraft:ocean");
/* 113 */     BIOMES_BY_ID.put(1, "minecraft:plains");
/* 114 */     BIOMES_BY_ID.put(2, "minecraft:desert");
/* 115 */     BIOMES_BY_ID.put(3, "minecraft:mountains");
/* 116 */     BIOMES_BY_ID.put(4, "minecraft:forest");
/* 117 */     BIOMES_BY_ID.put(5, "minecraft:taiga");
/* 118 */     BIOMES_BY_ID.put(6, "minecraft:swamp");
/* 119 */     BIOMES_BY_ID.put(7, "minecraft:river");
/* 120 */     BIOMES_BY_ID.put(8, "minecraft:nether_wastes");
/* 121 */     BIOMES_BY_ID.put(9, "minecraft:the_end");
/* 122 */     BIOMES_BY_ID.put(10, "minecraft:frozen_ocean");
/* 123 */     BIOMES_BY_ID.put(11, "minecraft:frozen_river");
/* 124 */     BIOMES_BY_ID.put(12, "minecraft:snowy_tundra");
/* 125 */     BIOMES_BY_ID.put(13, "minecraft:snowy_mountains");
/* 126 */     BIOMES_BY_ID.put(14, "minecraft:mushroom_fields");
/* 127 */     BIOMES_BY_ID.put(15, "minecraft:mushroom_field_shore");
/* 128 */     BIOMES_BY_ID.put(16, "minecraft:beach");
/* 129 */     BIOMES_BY_ID.put(17, "minecraft:desert_hills");
/* 130 */     BIOMES_BY_ID.put(18, "minecraft:wooded_hills");
/* 131 */     BIOMES_BY_ID.put(19, "minecraft:taiga_hills");
/* 132 */     BIOMES_BY_ID.put(20, "minecraft:mountain_edge");
/* 133 */     BIOMES_BY_ID.put(21, "minecraft:jungle");
/* 134 */     BIOMES_BY_ID.put(22, "minecraft:jungle_hills");
/* 135 */     BIOMES_BY_ID.put(23, "minecraft:jungle_edge");
/* 136 */     BIOMES_BY_ID.put(24, "minecraft:deep_ocean");
/* 137 */     BIOMES_BY_ID.put(25, "minecraft:stone_shore");
/* 138 */     BIOMES_BY_ID.put(26, "minecraft:snowy_beach");
/* 139 */     BIOMES_BY_ID.put(27, "minecraft:birch_forest");
/* 140 */     BIOMES_BY_ID.put(28, "minecraft:birch_forest_hills");
/* 141 */     BIOMES_BY_ID.put(29, "minecraft:dark_forest");
/* 142 */     BIOMES_BY_ID.put(30, "minecraft:snowy_taiga");
/* 143 */     BIOMES_BY_ID.put(31, "minecraft:snowy_taiga_hills");
/* 144 */     BIOMES_BY_ID.put(32, "minecraft:giant_tree_taiga");
/* 145 */     BIOMES_BY_ID.put(33, "minecraft:giant_tree_taiga_hills");
/* 146 */     BIOMES_BY_ID.put(34, "minecraft:wooded_mountains");
/* 147 */     BIOMES_BY_ID.put(35, "minecraft:savanna");
/* 148 */     BIOMES_BY_ID.put(36, "minecraft:savanna_plateau");
/* 149 */     BIOMES_BY_ID.put(37, "minecraft:badlands");
/* 150 */     BIOMES_BY_ID.put(38, "minecraft:wooded_badlands_plateau");
/* 151 */     BIOMES_BY_ID.put(39, "minecraft:badlands_plateau");
/* 152 */     BIOMES_BY_ID.put(40, "minecraft:small_end_islands");
/* 153 */     BIOMES_BY_ID.put(41, "minecraft:end_midlands");
/* 154 */     BIOMES_BY_ID.put(42, "minecraft:end_highlands");
/* 155 */     BIOMES_BY_ID.put(43, "minecraft:end_barrens");
/* 156 */     BIOMES_BY_ID.put(44, "minecraft:warm_ocean");
/* 157 */     BIOMES_BY_ID.put(45, "minecraft:lukewarm_ocean");
/* 158 */     BIOMES_BY_ID.put(46, "minecraft:cold_ocean");
/* 159 */     BIOMES_BY_ID.put(47, "minecraft:deep_warm_ocean");
/* 160 */     BIOMES_BY_ID.put(48, "minecraft:deep_lukewarm_ocean");
/* 161 */     BIOMES_BY_ID.put(49, "minecraft:deep_cold_ocean");
/* 162 */     BIOMES_BY_ID.put(50, "minecraft:deep_frozen_ocean");
/* 163 */     BIOMES_BY_ID.put(127, "minecraft:the_void");
/* 164 */     BIOMES_BY_ID.put(129, "minecraft:sunflower_plains");
/* 165 */     BIOMES_BY_ID.put(130, "minecraft:desert_lakes");
/* 166 */     BIOMES_BY_ID.put(131, "minecraft:gravelly_mountains");
/* 167 */     BIOMES_BY_ID.put(132, "minecraft:flower_forest");
/* 168 */     BIOMES_BY_ID.put(133, "minecraft:taiga_mountains");
/* 169 */     BIOMES_BY_ID.put(134, "minecraft:swamp_hills");
/* 170 */     BIOMES_BY_ID.put(140, "minecraft:ice_spikes");
/* 171 */     BIOMES_BY_ID.put(149, "minecraft:modified_jungle");
/* 172 */     BIOMES_BY_ID.put(151, "minecraft:modified_jungle_edge");
/* 173 */     BIOMES_BY_ID.put(155, "minecraft:tall_birch_forest");
/* 174 */     BIOMES_BY_ID.put(156, "minecraft:tall_birch_hills");
/* 175 */     BIOMES_BY_ID.put(157, "minecraft:dark_forest_hills");
/* 176 */     BIOMES_BY_ID.put(158, "minecraft:snowy_taiga_mountains");
/* 177 */     BIOMES_BY_ID.put(160, "minecraft:giant_spruce_taiga");
/* 178 */     BIOMES_BY_ID.put(161, "minecraft:giant_spruce_taiga_hills");
/* 179 */     BIOMES_BY_ID.put(162, "minecraft:modified_gravelly_mountains");
/* 180 */     BIOMES_BY_ID.put(163, "minecraft:shattered_savanna");
/* 181 */     BIOMES_BY_ID.put(164, "minecraft:shattered_savanna_plateau");
/* 182 */     BIOMES_BY_ID.put(165, "minecraft:eroded_badlands");
/* 183 */     BIOMES_BY_ID.put(166, "minecraft:modified_wooded_badlands_plateau");
/* 184 */     BIOMES_BY_ID.put(167, "minecraft:modified_badlands_plateau");
/* 185 */     BIOMES_BY_ID.put(168, "minecraft:bamboo_jungle");
/* 186 */     BIOMES_BY_ID.put(169, "minecraft:bamboo_jungle_hills");
/* 187 */     BIOMES_BY_ID.put(170, "minecraft:soul_sand_valley");
/* 188 */     BIOMES_BY_ID.put(171, "minecraft:crimson_forest");
/* 189 */     BIOMES_BY_ID.put(172, "minecraft:warped_forest");
/* 190 */     BIOMES_BY_ID.put(173, "minecraft:basalt_deltas");
/* 191 */     BIOMES_BY_ID.put(174, "minecraft:dripstone_caves");
/* 192 */     BIOMES_BY_ID.put(175, "minecraft:lush_caves");
/* 193 */     BIOMES_BY_ID.put(177, "minecraft:meadow");
/* 194 */     BIOMES_BY_ID.put(178, "minecraft:grove");
/* 195 */     BIOMES_BY_ID.put(179, "minecraft:snowy_slopes");
/* 196 */     BIOMES_BY_ID.put(180, "minecraft:snowcapped_peaks");
/* 197 */     BIOMES_BY_ID.put(181, "minecraft:lofty_peaks");
/* 198 */     BIOMES_BY_ID.put(182, "minecraft:stony_peaks");
/*     */   }
/*     */   
/*     */   public ChunkHeightAndBiomeFix(Schema paramSchema) {
/* 202 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 207 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 208 */     OpticFinder opticFinder1 = type1.findField("Level");
/* 209 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Sections");
/*     */     
/* 211 */     Schema schema = getOutputSchema();
/* 212 */     Type type2 = schema.getType(References.CHUNK);
/* 213 */     Type type3 = type2.findField("Level").type();
/* 214 */     Type type4 = type3.findField("Sections").type();
/*     */     
/* 216 */     return fixTypeEverywhereTyped("ChunkHeightAndBiomeFix", type1, type2, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, paramType1, ()));
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> predictChunkStatusBeforeSurface(Dynamic<?> paramDynamic, Set<String> paramSet) {
/* 291 */     return paramDynamic.update("Status", paramDynamic -> {
/*     */           String str = paramDynamic.asString("empty");
/*     */ 
/*     */           
/*     */           if (STATUS_IS_OR_AFTER_SURFACE.contains(str)) {
/*     */             return paramDynamic;
/*     */           }
/*     */ 
/*     */           
/*     */           paramSet.remove("minecraft:air");
/*     */ 
/*     */           
/*     */           boolean bool1 = !paramSet.isEmpty() ? true : false;
/*     */ 
/*     */           
/*     */           paramSet.removeAll(BLOCKS_BEFORE_FEATURE_STATUS);
/*     */           
/*     */           boolean bool2 = !paramSet.isEmpty() ? true : false;
/*     */           
/* 310 */           return bool2 ? paramDynamic.createString("liquid_carvers") : (("noise".equals(str) || bool1) ? paramDynamic.createString("noise") : ("biomes".equals(str) ? paramDynamic.createString("structure_references") : paramDynamic));
/*     */         });
/*     */   }
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
/*     */   private static Dynamic<?>[] getBiomeContainers(Dynamic<?> paramDynamic, boolean paramBoolean, int paramInt, MutableBoolean paramMutableBoolean) {
/* 324 */     Dynamic[] arrayOfDynamic = new Dynamic[paramBoolean ? 24 : 16];
/*     */     
/* 326 */     int[] arrayOfInt = paramDynamic.get("Biomes").asIntStreamOpt().result().map(IntStream::toArray).orElse(null);
/* 327 */     if (arrayOfInt != null && arrayOfInt.length == 1536) {
/* 328 */       paramMutableBoolean.setValue(true);
/*     */       
/* 330 */       for (byte b = 0; b < 24; b++) {
/* 331 */         byte b1 = b;
/* 332 */         arrayOfDynamic[b] = makeBiomeContainer(paramDynamic, paramInt2 -> getOldBiome(paramArrayOfint, paramInt1 * 64 + paramInt2));
/*     */       } 
/* 334 */     } else if (arrayOfInt != null && arrayOfInt.length == 1024) {
/* 335 */       for (byte b = 0; b < 16; b++) {
/* 336 */         int i = b - paramInt;
/* 337 */         byte b1 = b;
/* 338 */         arrayOfDynamic[i] = makeBiomeContainer(paramDynamic, paramInt2 -> getOldBiome(paramArrayOfint, paramInt1 * 64 + paramInt2));
/*     */       } 
/* 340 */       if (paramBoolean) {
/* 341 */         Dynamic<?> dynamic1 = makeBiomeContainer(paramDynamic, paramInt -> getOldBiome(paramArrayOfint, paramInt % 16));
/* 342 */         Dynamic<?> dynamic2 = makeBiomeContainer(paramDynamic, paramInt -> getOldBiome(paramArrayOfint, paramInt % 16 + 1008)); byte b1;
/* 343 */         for (b1 = 0; b1 < 4; b1++) {
/* 344 */           arrayOfDynamic[b1] = dynamic1;
/*     */         }
/* 346 */         for (b1 = 20; b1 < 24; b1++) {
/* 347 */           arrayOfDynamic[b1] = dynamic2;
/*     */         }
/*     */       } 
/*     */     } else {
/* 351 */       Arrays.fill((Object[])arrayOfDynamic, makePalettedContainer(paramDynamic.createList(Stream.of(paramDynamic.createString("minecraft:plains")))));
/*     */     } 
/* 353 */     return (Dynamic<?>[])arrayOfDynamic;
/*     */   }
/*     */   
/*     */   private static int getOldBiome(int[] paramArrayOfint, int paramInt) {
/* 357 */     return paramArrayOfint[paramInt] & 0xFF;
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateChunkTag(Dynamic<?> paramDynamic, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Supplier<ChunkProtoTickListFix.PoorMansPalettedContainer> paramSupplier) {
/* 361 */     paramDynamic = paramDynamic.remove("Biomes");
/*     */     
/* 363 */     if (!paramBoolean1) {
/* 364 */       return updateCarvingMasks(paramDynamic, 16, 0);
/*     */     }
/*     */     
/* 367 */     if (paramBoolean2) {
/* 368 */       return updateCarvingMasks(paramDynamic, 24, 0);
/*     */     }
/*     */     
/* 371 */     paramDynamic = updateHeightmaps(paramDynamic);
/* 372 */     paramDynamic = addPaddingEntries(paramDynamic, "LiquidsToBeTicked");
/* 373 */     paramDynamic = addPaddingEntries(paramDynamic, "PostProcessing");
/* 374 */     paramDynamic = addPaddingEntries(paramDynamic, "ToBeTicked");
/* 375 */     paramDynamic = updateCarvingMasks(paramDynamic, 24, 4);
/* 376 */     paramDynamic = paramDynamic.update("UpgradeData", ChunkHeightAndBiomeFix::shiftUpgradeData);
/*     */     
/* 378 */     if (!paramBoolean3) {
/* 379 */       return paramDynamic;
/*     */     }
/*     */     
/* 382 */     Optional<Dynamic> optional = paramDynamic.get("Status").result();
/* 383 */     if (optional.isPresent()) {
/* 384 */       Dynamic dynamic = optional.get();
/* 385 */       String str = dynamic.asString("");
/* 386 */       if (!"empty".equals(str)) {
/* 387 */         paramDynamic = paramDynamic.set("blending_data", paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 388 */                 .createString("old_noise"), paramDynamic.createBoolean(STATUS_IS_OR_AFTER_NOISE.contains(str)))));
/*     */ 
/*     */         
/* 391 */         if (!SharedConstants.DEBUG_DISABLE_BELOW_ZERO_RETROGENERATION) {
/* 392 */           ChunkProtoTickListFix.PoorMansPalettedContainer poorMansPalettedContainer = paramSupplier.get();
/* 393 */           if (poorMansPalettedContainer != null) {
/* 394 */             BitSet bitSet = new BitSet(256);
/* 395 */             boolean bool = str.equals("noise");
/* 396 */             for (byte b = 0; b < 16; b++) {
/* 397 */               for (byte b1 = 0; b1 < 16; b1++) {
/* 398 */                 Dynamic<?> dynamic1 = poorMansPalettedContainer.get(b1, 0, b);
/* 399 */                 boolean bool1 = (dynamic1 != null && "minecraft:bedrock".equals(dynamic1.get("Name").asString("")));
/* 400 */                 boolean bool2 = (dynamic1 != null && "minecraft:air".equals(dynamic1.get("Name").asString(""))) ? true : false;
/* 401 */                 if (bool2) {
/* 402 */                   bitSet.set(b * 16 + b1);
/*     */                 }
/* 404 */                 bool |= bool1;
/*     */               } 
/*     */             } 
/*     */             
/* 408 */             if (bool && bitSet.cardinality() != bitSet.size()) {
/*     */               
/* 410 */               Dynamic dynamic1 = "full".equals(str) ? paramDynamic.createString("heightmaps") : dynamic;
/*     */               
/* 412 */               paramDynamic = paramDynamic.set("below_zero_retrogen", paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 413 */                       .createString("target_status"), dynamic1, paramDynamic
/* 414 */                       .createString("missing_bedrock"), paramDynamic.createLongList(LongStream.of(bitSet.toLongArray())))));
/*     */ 
/*     */               
/* 417 */               paramDynamic = paramDynamic.set("Status", paramDynamic.createString("empty"));
/*     */             } 
/* 419 */             paramDynamic = paramDynamic.set("isLightOn", paramDynamic.createBoolean(false));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 424 */     return paramDynamic;
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> shiftUpgradeData(Dynamic<T> paramDynamic) {
/* 428 */     return paramDynamic.update("Indices", paramDynamic -> {
/*     */           HashMap<Object, Object> hashMap = new HashMap<>();
/*     */           paramDynamic.getMapValues().ifSuccess(());
/*     */           return paramDynamic.createMap(hashMap);
/*     */         });
/*     */   }
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
/*     */   private static Dynamic<?> updateCarvingMasks(Dynamic<?> paramDynamic, int paramInt1, int paramInt2) {
/* 449 */     Dynamic dynamic = paramDynamic.get("CarvingMasks").orElseEmptyMap();
/* 450 */     dynamic = dynamic.updateMapValues(paramPair -> {
/*     */           long[] arrayOfLong1 = BitSet.valueOf(((Dynamic)paramPair.getSecond()).asByteBuffer().array()).toLongArray();
/*     */           
/*     */           long[] arrayOfLong2 = new long[64 * paramInt1];
/*     */           
/*     */           System.arraycopy(arrayOfLong1, 0, arrayOfLong2, 64 * paramInt2, arrayOfLong1.length);
/*     */           
/*     */           return Pair.of(paramPair.getFirst(), paramDynamic.createLongList(LongStream.of(arrayOfLong2)));
/*     */         });
/* 459 */     return paramDynamic.set("CarvingMasks", dynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> addPaddingEntries(Dynamic<?> paramDynamic, String paramString) {
/* 463 */     List<Dynamic> list = (List)paramDynamic.get(paramString).orElseEmptyList().asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 464 */     if (list.size() == 24)
/*     */     {
/* 466 */       return paramDynamic;
/*     */     }
/* 468 */     Dynamic dynamic = paramDynamic.emptyList();
/* 469 */     for (byte b = 0; b < 4; b++) {
/* 470 */       list.add(0, dynamic);
/* 471 */       list.add(dynamic);
/*     */     } 
/* 473 */     return paramDynamic.set(paramString, paramDynamic.createList(list.stream()));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateHeightmaps(Dynamic<?> paramDynamic) {
/* 477 */     return paramDynamic.update("Heightmaps", paramDynamic -> {
/*     */           for (String str : HEIGHTMAP_TYPES) {
/*     */             paramDynamic = paramDynamic.update(str, ChunkHeightAndBiomeFix::getFixedHeightmap);
/*     */           }
/*     */           return paramDynamic;
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> getFixedHeightmap(Dynamic<?> paramDynamic) {
/* 486 */     return paramDynamic.createLongList(paramDynamic.asLongStream().map(paramLong -> {
/*     */             long l = 0L;
/*     */             for (byte b = 0; b + 9 <= 64; b += 9) {
/*     */               long l2;
/*     */               long l1 = paramLong >> b & 0x1FFL;
/*     */               if (l1 == 0L) {
/*     */                 l2 = 0L;
/*     */               } else {
/*     */                 l2 = Math.min(l1 + 64L, 511L);
/*     */               } 
/*     */               l |= l2 << b;
/*     */             } 
/*     */             return l;
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> makeBiomeContainer(Dynamic<?> paramDynamic, Int2IntFunction paramInt2IntFunction) {
/* 506 */     Int2IntLinkedOpenHashMap int2IntLinkedOpenHashMap = new Int2IntLinkedOpenHashMap();
/* 507 */     for (byte b1 = 0; b1 < 64; b1++) {
/* 508 */       int n = paramInt2IntFunction.applyAsInt(b1);
/* 509 */       if (!int2IntLinkedOpenHashMap.containsKey(n)) {
/* 510 */         int2IntLinkedOpenHashMap.put(n, int2IntLinkedOpenHashMap.size());
/*     */       }
/*     */     } 
/* 513 */     Dynamic<?> dynamic1 = paramDynamic.createList(int2IntLinkedOpenHashMap.keySet().stream().map(paramInteger -> paramDynamic.createString((String)BIOMES_BY_ID.getOrDefault(paramInteger.intValue(), "minecraft:plains"))));
/*     */     
/* 515 */     int i = ceillog2(int2IntLinkedOpenHashMap.size());
/* 516 */     if (i == 0) {
/* 517 */       return makePalettedContainer(dynamic1);
/*     */     }
/*     */     
/* 520 */     int j = 64 / i;
/* 521 */     int k = (64 + j - 1) / j;
/* 522 */     long[] arrayOfLong = new long[k];
/* 523 */     byte b2 = 0;
/* 524 */     int m = 0;
/* 525 */     for (byte b3 = 0; b3 < 64; b3++) {
/* 526 */       int n = paramInt2IntFunction.applyAsInt(b3);
/* 527 */       arrayOfLong[b2] = arrayOfLong[b2] | int2IntLinkedOpenHashMap.get(n) << m;
/* 528 */       m += i;
/* 529 */       if (m + i > 64) {
/* 530 */         b2++;
/* 531 */         m = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 535 */     Dynamic<?> dynamic2 = paramDynamic.createLongList(Arrays.stream(arrayOfLong));
/* 536 */     return makePalettedContainer(dynamic1, dynamic2);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> makePalettedContainer(Dynamic<?> paramDynamic) {
/* 540 */     return paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("palette"), paramDynamic));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> makePalettedContainer(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2) {
/* 544 */     return paramDynamic1.createMap((Map)ImmutableMap.of(paramDynamic1.createString("palette"), paramDynamic1, paramDynamic1.createString("data"), paramDynamic2));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> makeOptimizedPalettedContainer(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2) {
/* 548 */     List<Dynamic<?>> list = (List)paramDynamic1.asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 549 */     if (list.size() == 1) {
/* 550 */       return makePalettedContainer(paramDynamic1);
/*     */     }
/* 552 */     paramDynamic1 = padPaletteEntries(paramDynamic1, paramDynamic2, list);
/* 553 */     return makePalettedContainer(paramDynamic1, paramDynamic2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> padPaletteEntries(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2, List<Dynamic<?>> paramList) {
/* 559 */     long l1 = paramDynamic2.asLongStream().count() * 64L;
/* 560 */     long l2 = l1 / 4096L;
/*     */     
/* 562 */     int i = paramList.size();
/* 563 */     int j = ceillog2(i);
/*     */     
/* 565 */     if (l2 > j) {
/* 566 */       Dynamic<?> dynamic = paramDynamic1.createMap((Map)ImmutableMap.of(paramDynamic1.createString("Name"), paramDynamic1.createString("minecraft:air")));
/*     */       
/* 568 */       int k = (1 << (int)(l2 - 1L)) + 1;
/* 569 */       int m = k - i;
/* 570 */       for (byte b = 0; b < m; b++) {
/* 571 */         paramList.add(dynamic);
/*     */       }
/* 573 */       return paramDynamic1.createList(paramList.stream());
/*     */     } 
/* 575 */     return paramDynamic1;
/*     */   }
/*     */   
/*     */   public static int ceillog2(int paramInt) {
/* 579 */     if (paramInt == 0) {
/* 580 */       return 0;
/*     */     }
/*     */     
/* 583 */     return (int)Math.ceil(Math.log(paramInt) / Math.log(2.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkHeightAndBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */