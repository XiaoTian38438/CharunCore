/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LevelDataGeneratorOptionsFix extends DataFix {
/*     */   static {
/*  29 */     MAP = (Map<String, String>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("0", "minecraft:ocean");
/*     */           paramHashMap.put("1", "minecraft:plains");
/*     */           paramHashMap.put("2", "minecraft:desert");
/*     */           paramHashMap.put("3", "minecraft:mountains");
/*     */           paramHashMap.put("4", "minecraft:forest");
/*     */           paramHashMap.put("5", "minecraft:taiga");
/*     */           paramHashMap.put("6", "minecraft:swamp");
/*     */           paramHashMap.put("7", "minecraft:river");
/*     */           paramHashMap.put("8", "minecraft:nether");
/*     */           paramHashMap.put("9", "minecraft:the_end");
/*     */           paramHashMap.put("10", "minecraft:frozen_ocean");
/*     */           paramHashMap.put("11", "minecraft:frozen_river");
/*     */           paramHashMap.put("12", "minecraft:snowy_tundra");
/*     */           paramHashMap.put("13", "minecraft:snowy_mountains");
/*     */           paramHashMap.put("14", "minecraft:mushroom_fields");
/*     */           paramHashMap.put("15", "minecraft:mushroom_field_shore");
/*     */           paramHashMap.put("16", "minecraft:beach");
/*     */           paramHashMap.put("17", "minecraft:desert_hills");
/*     */           paramHashMap.put("18", "minecraft:wooded_hills");
/*     */           paramHashMap.put("19", "minecraft:taiga_hills");
/*     */           paramHashMap.put("20", "minecraft:mountain_edge");
/*     */           paramHashMap.put("21", "minecraft:jungle");
/*     */           paramHashMap.put("22", "minecraft:jungle_hills");
/*     */           paramHashMap.put("23", "minecraft:jungle_edge");
/*     */           paramHashMap.put("24", "minecraft:deep_ocean");
/*     */           paramHashMap.put("25", "minecraft:stone_shore");
/*     */           paramHashMap.put("26", "minecraft:snowy_beach");
/*     */           paramHashMap.put("27", "minecraft:birch_forest");
/*     */           paramHashMap.put("28", "minecraft:birch_forest_hills");
/*     */           paramHashMap.put("29", "minecraft:dark_forest");
/*     */           paramHashMap.put("30", "minecraft:snowy_taiga");
/*     */           paramHashMap.put("31", "minecraft:snowy_taiga_hills");
/*     */           paramHashMap.put("32", "minecraft:giant_tree_taiga");
/*     */           paramHashMap.put("33", "minecraft:giant_tree_taiga_hills");
/*     */           paramHashMap.put("34", "minecraft:wooded_mountains");
/*     */           paramHashMap.put("35", "minecraft:savanna");
/*     */           paramHashMap.put("36", "minecraft:savanna_plateau");
/*     */           paramHashMap.put("37", "minecraft:badlands");
/*     */           paramHashMap.put("38", "minecraft:wooded_badlands_plateau");
/*     */           paramHashMap.put("39", "minecraft:badlands_plateau");
/*     */           paramHashMap.put("40", "minecraft:small_end_islands");
/*     */           paramHashMap.put("41", "minecraft:end_midlands");
/*     */           paramHashMap.put("42", "minecraft:end_highlands");
/*     */           paramHashMap.put("43", "minecraft:end_barrens");
/*     */           paramHashMap.put("44", "minecraft:warm_ocean");
/*     */           paramHashMap.put("45", "minecraft:lukewarm_ocean");
/*     */           paramHashMap.put("46", "minecraft:cold_ocean");
/*     */           paramHashMap.put("47", "minecraft:deep_warm_ocean");
/*     */           paramHashMap.put("48", "minecraft:deep_lukewarm_ocean");
/*     */           paramHashMap.put("49", "minecraft:deep_cold_ocean");
/*     */           paramHashMap.put("50", "minecraft:deep_frozen_ocean");
/*     */           paramHashMap.put("127", "minecraft:the_void");
/*     */           paramHashMap.put("129", "minecraft:sunflower_plains");
/*     */           paramHashMap.put("130", "minecraft:desert_lakes");
/*     */           paramHashMap.put("131", "minecraft:gravelly_mountains");
/*     */           paramHashMap.put("132", "minecraft:flower_forest");
/*     */           paramHashMap.put("133", "minecraft:taiga_mountains");
/*     */           paramHashMap.put("134", "minecraft:swamp_hills");
/*     */           paramHashMap.put("140", "minecraft:ice_spikes");
/*     */           paramHashMap.put("149", "minecraft:modified_jungle");
/*     */           paramHashMap.put("151", "minecraft:modified_jungle_edge");
/*     */           paramHashMap.put("155", "minecraft:tall_birch_forest");
/*     */           paramHashMap.put("156", "minecraft:tall_birch_hills");
/*     */           paramHashMap.put("157", "minecraft:dark_forest_hills");
/*     */           paramHashMap.put("158", "minecraft:snowy_taiga_mountains");
/*     */           paramHashMap.put("160", "minecraft:giant_spruce_taiga");
/*     */           paramHashMap.put("161", "minecraft:giant_spruce_taiga_hills");
/*     */           paramHashMap.put("162", "minecraft:modified_gravelly_mountains");
/*     */           paramHashMap.put("163", "minecraft:shattered_savanna");
/*     */           paramHashMap.put("164", "minecraft:shattered_savanna_plateau");
/*     */           paramHashMap.put("165", "minecraft:eroded_badlands");
/*     */           paramHashMap.put("166", "minecraft:modified_wooded_badlands_plateau");
/*     */           paramHashMap.put("167", "minecraft:modified_badlands_plateau");
/*     */         });
/*     */   }
/*     */   
/*     */   static final Map<String, String> MAP;
/*     */   public static final String GENERATOR_OPTIONS = "generatorOptions";
/*     */   
/*     */   public LevelDataGeneratorOptionsFix(Schema paramSchema, boolean paramBoolean) {
/* 110 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 115 */     Type type = getOutputSchema().getType(References.LEVEL);
/* 116 */     return fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", getInputSchema().getType(References.LEVEL), type, paramTyped -> Util.writeAndReadTypedOrThrow(paramTyped, paramType, ()));
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
/*     */   private static <T> Dynamic<T> convert(String paramString, DynamicOps<T> paramDynamicOps) {
/*     */     ArrayList<Pair> arrayList;
/* 134 */     Iterator<String> iterator = Splitter.on(';').split(paramString).iterator();
/*     */ 
/*     */     
/* 137 */     String str = "minecraft:plains";
/* 138 */     HashMap<String, HashMap> hashMap = Maps.newHashMap();
/*     */     
/* 140 */     if (!paramString.isEmpty() && iterator.hasNext()) {
/* 141 */       List<Pair<Integer, String>> list = getLayersInfoFromString(iterator.next());
/*     */       
/* 143 */       if (!list.isEmpty()) {
/* 144 */         if (iterator.hasNext()) {
/* 145 */           str = MAP.getOrDefault(iterator.next(), "minecraft:plains");
/*     */         }
/*     */         
/* 148 */         if (iterator.hasNext()) {
/* 149 */           String[] arrayOfString = ((String)iterator.next()).toLowerCase(Locale.ROOT).split(",");
/*     */           
/* 151 */           for (String str1 : arrayOfString) {
/* 152 */             String[] arrayOfString1 = str1.split("\\(", 2);
/*     */             
/* 154 */             if (!arrayOfString1[0].isEmpty()) {
/* 155 */               hashMap.put(arrayOfString1[0], Maps.newHashMap());
/*     */               
/* 157 */               if (arrayOfString1.length > 1 && arrayOfString1[1].endsWith(")") && arrayOfString1[1].length() > 1) {
/* 158 */                 String[] arrayOfString2 = arrayOfString1[1].substring(0, arrayOfString1[1].length() - 1).split(" ");
/*     */                 
/* 160 */                 for (String str2 : arrayOfString2) {
/* 161 */                   String[] arrayOfString3 = str2.split("=", 2);
/* 162 */                   if (arrayOfString3.length == 2) {
/* 163 */                     ((Map<String, String>)hashMap.get(arrayOfString1[0])).put(arrayOfString3[0], arrayOfString3[1]);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } else {
/* 170 */           hashMap.put("village", Maps.newHashMap());
/*     */         } 
/*     */       } 
/*     */     } else {
/* 174 */       arrayList = Lists.newArrayList();
/* 175 */       arrayList.add(Pair.of(Integer.valueOf(1), "minecraft:bedrock"));
/* 176 */       arrayList.add(Pair.of(Integer.valueOf(2), "minecraft:dirt"));
/* 177 */       arrayList.add(Pair.of(Integer.valueOf(1), "minecraft:grass_block"));
/* 178 */       hashMap.put("village", Maps.newHashMap());
/*     */     } 
/*     */     
/* 181 */     Object object1 = paramDynamicOps.createList(arrayList.stream().map(paramPair -> paramDynamicOps.createMap((Map)ImmutableMap.of(paramDynamicOps.createString("height"), paramDynamicOps.createInt(((Integer)paramPair.getFirst()).intValue()), paramDynamicOps.createString("block"), paramDynamicOps.createString((String)paramPair.getSecond())))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     Object object2 = paramDynamicOps.createMap((Map)hashMap.entrySet().stream().map(paramEntry -> Pair.of(paramDynamicOps.createString(((String)paramEntry.getKey()).toLowerCase(Locale.ROOT)), paramDynamicOps.createMap((Map)((Map)paramEntry.getValue()).entrySet().stream().map(()).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 193 */         .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
/*     */     
/* 195 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createMap((Map)ImmutableMap.of(paramDynamicOps
/* 196 */             .createString("layers"), object1, paramDynamicOps
/* 197 */             .createString("biome"), paramDynamicOps.createString(str), paramDynamicOps
/* 198 */             .createString("structures"), object2)));
/*     */   }
/*     */   
/*     */   private static Pair<Integer, String> getLayerInfoFromString(String paramString) {
/*     */     boolean bool;
/* 203 */     String[] arrayOfString = paramString.split("\\*", 2);
/*     */ 
/*     */     
/* 206 */     if (arrayOfString.length == 2) {
/*     */       try {
/* 208 */         bool = Integer.parseInt(arrayOfString[0]);
/* 209 */       } catch (NumberFormatException numberFormatException) {
/* 210 */         return null;
/*     */       } 
/*     */     } else {
/* 213 */       bool = true;
/*     */     } 
/*     */     
/* 216 */     String str = arrayOfString[arrayOfString.length - 1];
/* 217 */     return Pair.of(Integer.valueOf(bool), str);
/*     */   }
/*     */   
/*     */   private static List<Pair<Integer, String>> getLayersInfoFromString(String paramString) {
/* 221 */     ArrayList<Pair<Integer, String>> arrayList = Lists.newArrayList();
/* 222 */     String[] arrayOfString = paramString.split(",");
/*     */     
/* 224 */     for (String str : arrayOfString) {
/* 225 */       Pair<Integer, String> pair = getLayerInfoFromString(str);
/* 226 */       if (pair == null) {
/* 227 */         return Collections.emptyList();
/*     */       }
/* 229 */       arrayList.add(pair);
/*     */     } 
/*     */     
/* 232 */     return arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LevelDataGeneratorOptionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */