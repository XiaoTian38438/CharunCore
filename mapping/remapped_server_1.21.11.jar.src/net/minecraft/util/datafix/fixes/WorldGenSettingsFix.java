/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class WorldGenSettingsFix extends DataFix {
/*     */   private static final String VILLAGE = "minecraft:village";
/*     */   private static final String DESERT_PYRAMID = "minecraft:desert_pyramid";
/*     */   private static final String IGLOO = "minecraft:igloo";
/*     */   private static final String JUNGLE_TEMPLE = "minecraft:jungle_pyramid";
/*     */   private static final String SWAMP_HUT = "minecraft:swamp_hut";
/*     */   private static final String PILLAGER_OUTPOST = "minecraft:pillager_outpost";
/*     */   private static final String END_CITY = "minecraft:endcity";
/*     */   private static final String WOODLAND_MANSION = "minecraft:mansion";
/*     */   private static final String OCEAN_MONUMENT = "minecraft:monument";
/*     */   
/*     */   public WorldGenSettingsFix(Schema paramSchema) {
/*  28 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     return fixTypeEverywhereTyped("WorldGenSettings building", getInputSchema().getType(References.WORLD_GEN_SETTINGS), paramTyped -> paramTyped.update(DSL.remainderFinder(), WorldGenSettingsFix::fix));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> noise(long paramLong, DynamicLike<T> paramDynamicLike, Dynamic<T> paramDynamic1, Dynamic<T> paramDynamic2) {
/*  37 */     return paramDynamicLike.createMap((Map)ImmutableMap.of(paramDynamicLike
/*  38 */           .createString("type"), paramDynamicLike.createString("minecraft:noise"), paramDynamicLike
/*  39 */           .createString("biome_source"), paramDynamic2, paramDynamicLike
/*  40 */           .createString("seed"), paramDynamicLike.createLong(paramLong), paramDynamicLike
/*  41 */           .createString("settings"), paramDynamic1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> paramDynamic, long paramLong, boolean paramBoolean1, boolean paramBoolean2) {
/*  49 */     ImmutableMap.Builder builder = ImmutableMap.builder().put(paramDynamic.createString("type"), paramDynamic.createString("minecraft:vanilla_layered")).put(paramDynamic.createString("seed"), paramDynamic.createLong(paramLong)).put(paramDynamic.createString("large_biomes"), paramDynamic.createBoolean(paramBoolean2));
/*     */     
/*  51 */     if (paramBoolean1) {
/*  52 */       builder.put(paramDynamic.createString("legacy_biome_init_layer"), paramDynamic.createBoolean(paramBoolean1));
/*     */     }
/*     */     
/*  55 */     return paramDynamic.createMap((Map)builder.build());
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
/*  68 */   private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/*  69 */     .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
/*  70 */     .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
/*  71 */     .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
/*  72 */     .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
/*  73 */     .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
/*  74 */     .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
/*  75 */     .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
/*  76 */     .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
/*  77 */     .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
/*  78 */     .build();
/*     */   private static final class StructureFeatureConfiguration { public static final Codec<StructureFeatureConfiguration> CODEC; final int spacing; final int separation; final int salt;
/*     */     static {
/*  81 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)param1Instance, StructureFeatureConfiguration::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StructureFeatureConfiguration(int param1Int1, int param1Int2, int param1Int3) {
/*  92 */       this.spacing = param1Int1;
/*  93 */       this.separation = param1Int2;
/*  94 */       this.salt = param1Int3;
/*     */     }
/*     */     
/*     */     public <T> Dynamic<T> serialize(DynamicOps<T> param1DynamicOps) {
/*  98 */       return new Dynamic(param1DynamicOps, CODEC.encodeStart(param1DynamicOps, this).result().orElse(param1DynamicOps.emptyMap()));
/*     */     } }
/*     */   
/*     */   private static <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/*     */     Dynamic<T> dynamic;
/* 103 */     DynamicOps<?> dynamicOps = paramDynamic.getOps();
/*     */     
/* 105 */     long l = paramDynamic.get("RandomSeed").asLong(0L);
/*     */     
/* 107 */     Optional<String> optional = paramDynamic.get("generatorName").asString().map(paramString -> paramString.toLowerCase(Locale.ROOT)).result();
/*     */     
/* 109 */     Optional optional1 = paramDynamic.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> paramOptional.equals(Optional.of("customized")) ? paramDynamic.get("generatorOptions").asString().result() : Optional.empty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     boolean bool = false;
/* 117 */     if (optional.equals(Optional.of("customized")))
/* 118 */     { dynamic = defaultOverworld(paramDynamic, l); }
/* 119 */     else if (optional.isEmpty())
/* 120 */     { dynamic = defaultOverworld(paramDynamic, l); }
/*     */     else
/* 122 */     { String str; boolean bool3; byte b; boolean bool4; OptionalDynamic<?> optionalDynamic; ImmutableMap.Builder builder1; Map<Dynamic<?>, Dynamic<?>> map; OptionalDynamic optionalDynamic1, optionalDynamic2; Optional optional2; Dynamic<T> dynamic1, dynamic2, dynamic3; switch ((String)optional.get())
/*     */       { case "flat":
/* 124 */           optionalDynamic = paramDynamic.get("generatorOptions");
/* 125 */           map = fixFlatStructures(dynamicOps, optionalDynamic);
/*     */           
/* 127 */           dynamic = paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 128 */                 .createString("type"), paramDynamic.createString("minecraft:flat"), paramDynamic
/* 129 */                 .createString("settings"), paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 130 */                     .createString("structures"), paramDynamic.createMap(map), paramDynamic
/* 131 */                     .createString("layers"), optionalDynamic.get("layers").result().orElseGet(() -> paramDynamic.createList(Stream.of(new Dynamic[] { paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("height"), paramDynamic.createInt(1), paramDynamic.createString("block"), paramDynamic.createString("minecraft:bedrock"))), paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("height"), paramDynamic.createInt(2), paramDynamic.createString("block"), paramDynamic.createString("minecraft:dirt"))), paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("height"), paramDynamic.createInt(1), paramDynamic.createString("block"), paramDynamic.createString("minecraft:grass_block"))) }))), paramDynamic
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
/* 145 */                     .createString("biome"), paramDynamic.createString(optionalDynamic.get("biome").asString("minecraft:plains"))))));
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
/* 201 */           bool3 = paramDynamic.get("MapFeatures").asBoolean(true);
/* 202 */           bool4 = paramDynamic.get("BonusChest").asBoolean(false);
/*     */           
/* 204 */           builder1 = ImmutableMap.builder();
/* 205 */           builder1.put(dynamicOps.createString("seed"), dynamicOps.createLong(l));
/* 206 */           builder1.put(dynamicOps.createString("generate_features"), dynamicOps.createBoolean(bool3));
/* 207 */           builder1.put(dynamicOps.createString("bonus_chest"), dynamicOps.createBoolean(bool4));
/* 208 */           builder1.put(dynamicOps.createString("dimensions"), vanillaLevels(paramDynamic, l, dynamic, bool));
/* 209 */           optional1.ifPresent(paramString -> paramBuilder.put(paramDynamicOps.createString("legacy_custom_options"), paramDynamicOps.createString(paramString)));
/*     */           
/* 211 */           return new Dynamic(dynamicOps, dynamicOps.createMap((Map)builder1.build()));case "debug_all_block_states": dynamic = paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("type"), paramDynamic.createString("minecraft:debug"))); bool3 = paramDynamic.get("MapFeatures").asBoolean(true); bool4 = paramDynamic.get("BonusChest").asBoolean(false); builder1 = ImmutableMap.builder(); builder1.put(dynamicOps.createString("seed"), dynamicOps.createLong(l)); builder1.put(dynamicOps.createString("generate_features"), dynamicOps.createBoolean(bool3)); builder1.put(dynamicOps.createString("bonus_chest"), dynamicOps.createBoolean(bool4)); builder1.put(dynamicOps.createString("dimensions"), vanillaLevels(paramDynamic, l, dynamic, bool)); optional1.ifPresent(paramString -> paramBuilder.put(paramDynamicOps.createString("legacy_custom_options"), paramDynamicOps.createString(paramString))); return new Dynamic(dynamicOps, dynamicOps.createMap((Map)builder1.build()));case "buffet": optionalDynamic1 = paramDynamic.get("generatorOptions"); optionalDynamic2 = optionalDynamic1.get("chunk_generator"); optional2 = optionalDynamic2.get("type").asString().result(); if (Objects.equals(optional2, Optional.of("minecraft:caves"))) { dynamic1 = paramDynamic.createString("minecraft:caves"); bool = true; } else if (Objects.equals(optional2, Optional.of("minecraft:floating_islands"))) { dynamic1 = paramDynamic.createString("minecraft:floating_islands"); } else { dynamic1 = paramDynamic.createString("minecraft:overworld"); }  dynamic2 = optionalDynamic1.get("biome_source").result().orElseGet(() -> paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic.createString("type"), paramDynamic.createString("minecraft:fixed")))); if (dynamic2.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) { String str1 = dynamic2.get("options").get("biomes").asStream().findFirst().flatMap(paramDynamic -> paramDynamic.asString().result()).orElse("minecraft:ocean"); dynamic3 = dynamic2.remove("options").set("biome", paramDynamic.createString(str1)); } else { dynamic3 = dynamic2; }  dynamic = noise(l, (DynamicLike<T>)paramDynamic, dynamic1, dynamic3); bool3 = paramDynamic.get("MapFeatures").asBoolean(true); bool4 = paramDynamic.get("BonusChest").asBoolean(false); builder1 = ImmutableMap.builder(); builder1.put(dynamicOps.createString("seed"), dynamicOps.createLong(l)); builder1.put(dynamicOps.createString("generate_features"), dynamicOps.createBoolean(bool3)); builder1.put(dynamicOps.createString("bonus_chest"), dynamicOps.createBoolean(bool4)); builder1.put(dynamicOps.createString("dimensions"), vanillaLevels(paramDynamic, l, dynamic, bool)); optional1.ifPresent(paramString -> paramBuilder.put(paramDynamicOps.createString("legacy_custom_options"), paramDynamicOps.createString(paramString))); return new Dynamic(dynamicOps, dynamicOps.createMap((Map)builder1.build())); }  boolean bool5 = ((String)optional.get()).equals("default"); boolean bool6 = (((String)optional.get()).equals("default_1_1") || (bool5 && paramDynamic.get("generatorVersion").asInt(0) == 0)) ? true : false; boolean bool7 = ((String)optional.get()).equals("amplified"); boolean bool8 = ((String)optional.get()).equals("largebiomes"); dynamic = noise(l, (DynamicLike<T>)paramDynamic, paramDynamic.createString(bool7 ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(paramDynamic, l, bool6, bool8)); }  boolean bool1 = paramDynamic.get("MapFeatures").asBoolean(true); boolean bool2 = paramDynamic.get("BonusChest").asBoolean(false); ImmutableMap.Builder builder = ImmutableMap.builder(); builder.put(dynamicOps.createString("seed"), dynamicOps.createLong(l)); builder.put(dynamicOps.createString("generate_features"), dynamicOps.createBoolean(bool1)); builder.put(dynamicOps.createString("bonus_chest"), dynamicOps.createBoolean(bool2)); builder.put(dynamicOps.createString("dimensions"), vanillaLevels(paramDynamic, l, dynamic, bool)); optional1.ifPresent(paramString -> paramBuilder.put(paramDynamicOps.createString("legacy_custom_options"), paramDynamicOps.createString(paramString))); return new Dynamic(dynamicOps, dynamicOps.createMap((Map)builder.build()));
/*     */   }
/*     */   
/*     */   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> paramDynamic, long paramLong) {
/* 215 */     return noise(paramLong, (DynamicLike<T>)paramDynamic, paramDynamic.createString("minecraft:overworld"), vanillaBiomeSource(paramDynamic, paramLong, false, false));
/*     */   }
/*     */   
/*     */   protected static <T> T vanillaLevels(Dynamic<T> paramDynamic1, long paramLong, Dynamic<T> paramDynamic2, boolean paramBoolean) {
/* 219 */     DynamicOps dynamicOps = paramDynamic1.getOps();
/* 220 */     return (T)dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps
/* 221 */           .createString("minecraft:overworld"), dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps
/* 222 */               .createString("type"), dynamicOps.createString("minecraft:overworld" + (paramBoolean ? "_caves" : "")), dynamicOps
/* 223 */               .createString("generator"), paramDynamic2.getValue())), dynamicOps
/*     */           
/* 225 */           .createString("minecraft:the_nether"), dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps
/* 226 */               .createString("type"), dynamicOps.createString("minecraft:the_nether"), dynamicOps
/* 227 */               .createString("generator"), noise(paramLong, (DynamicLike<T>)paramDynamic1, paramDynamic1.createString("minecraft:nether"), paramDynamic1.createMap((Map)ImmutableMap.of(paramDynamic1
/* 228 */                     .createString("type"), paramDynamic1.createString("minecraft:multi_noise"), paramDynamic1
/* 229 */                     .createString("seed"), paramDynamic1.createLong(paramLong), paramDynamic1
/* 230 */                     .createString("preset"), paramDynamic1.createString("minecraft:nether"))))
/* 231 */               .getValue())), dynamicOps
/*     */           
/* 233 */           .createString("minecraft:the_end"), dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps
/* 234 */               .createString("type"), dynamicOps.createString("minecraft:the_end"), dynamicOps
/* 235 */               .createString("generator"), noise(paramLong, (DynamicLike<T>)paramDynamic1, paramDynamic1.createString("minecraft:end"), paramDynamic1.createMap((Map)ImmutableMap.of(paramDynamic1
/* 236 */                     .createString("type"), paramDynamic1.createString("minecraft:the_end"), paramDynamic1
/* 237 */                     .createString("seed"), paramDynamic1.createLong(paramLong))))
/* 238 */               .getValue()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> paramDynamicOps, OptionalDynamic<T> paramOptionalDynamic) {
/* 244 */     MutableInt mutableInt1 = new MutableInt(32);
/* 245 */     MutableInt mutableInt2 = new MutableInt(3);
/* 246 */     MutableInt mutableInt3 = new MutableInt(128);
/* 247 */     MutableBoolean mutableBoolean = new MutableBoolean(false);
/* 248 */     HashMap<String, StructureFeatureConfiguration> hashMap = Maps.newHashMap();
/*     */     
/* 250 */     if (paramOptionalDynamic.result().isEmpty()) {
/* 251 */       mutableBoolean.setTrue();
/* 252 */       hashMap.put("minecraft:village", (StructureFeatureConfiguration)DEFAULTS.get("minecraft:village"));
/*     */     } 
/*     */     
/* 255 */     paramOptionalDynamic.get("structures").flatMap(Dynamic::getMapValues).ifSuccess(paramMap2 -> paramMap2.forEach(()));
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
/* 318 */     ImmutableMap.Builder builder = ImmutableMap.builder();
/* 319 */     builder.put(paramOptionalDynamic.createString("structures"), paramOptionalDynamic.createMap((Map)hashMap.entrySet().stream().collect(Collectors.toMap(paramEntry -> paramOptionalDynamic.createString((String)paramEntry.getKey()), paramEntry -> ((StructureFeatureConfiguration)paramEntry.getValue()).serialize(paramDynamicOps)))));
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (mutableBoolean.isTrue()) {
/* 324 */       builder.put(paramOptionalDynamic.createString("stronghold"), paramOptionalDynamic.createMap((Map)ImmutableMap.of(paramOptionalDynamic
/* 325 */               .createString("distance"), paramOptionalDynamic.createInt(mutableInt1.intValue()), paramOptionalDynamic
/* 326 */               .createString("spread"), paramOptionalDynamic.createInt(mutableInt2.intValue()), paramOptionalDynamic
/* 327 */               .createString("count"), paramOptionalDynamic.createInt(mutableInt3.intValue()))));
/*     */     }
/*     */     
/* 330 */     return (Map<Dynamic<T>, Dynamic<T>>)builder.build();
/*     */   }
/*     */   
/*     */   private static int getInt(String paramString, int paramInt) {
/* 334 */     return NumberUtils.toInt(paramString, paramInt);
/*     */   }
/*     */   
/*     */   private static int getInt(String paramString, int paramInt1, int paramInt2) {
/* 338 */     return Math.max(paramInt2, getInt(paramString, paramInt1));
/*     */   }
/*     */   
/*     */   private static void setSpacing(Map<String, StructureFeatureConfiguration> paramMap, String paramString1, String paramString2, int paramInt) {
/* 342 */     StructureFeatureConfiguration structureFeatureConfiguration = paramMap.getOrDefault(paramString1, (StructureFeatureConfiguration)DEFAULTS.get(paramString1));
/* 343 */     int i = getInt(paramString2, structureFeatureConfiguration.spacing, paramInt);
/* 344 */     paramMap.put(paramString1, new StructureFeatureConfiguration(i, structureFeatureConfiguration.separation, structureFeatureConfiguration.salt));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */