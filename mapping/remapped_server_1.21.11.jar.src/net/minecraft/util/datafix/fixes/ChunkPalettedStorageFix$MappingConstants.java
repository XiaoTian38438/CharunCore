/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
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
/*     */ class MappingConstants
/*     */ {
/*  53 */   static final BitSet VIRTUAL = new BitSet(256);
/*  54 */   static final BitSet FIX = new BitSet(256);
/*  55 */   static final Dynamic<?> PUMPKIN = ExtraDataFixUtils.blockState("minecraft:pumpkin");
/*  56 */   static final Dynamic<?> SNOWY_PODZOL = ExtraDataFixUtils.blockState("minecraft:podzol", Map.of("snowy", "true"));
/*  57 */   static final Dynamic<?> SNOWY_GRASS = ExtraDataFixUtils.blockState("minecraft:grass_block", Map.of("snowy", "true"));
/*  58 */   static final Dynamic<?> SNOWY_MYCELIUM = ExtraDataFixUtils.blockState("minecraft:mycelium", Map.of("snowy", "true"));
/*  59 */   static final Dynamic<?> UPPER_SUNFLOWER = ExtraDataFixUtils.blockState("minecraft:sunflower", Map.of("half", "upper"));
/*  60 */   static final Dynamic<?> UPPER_LILAC = ExtraDataFixUtils.blockState("minecraft:lilac", Map.of("half", "upper"));
/*  61 */   static final Dynamic<?> UPPER_TALL_GRASS = ExtraDataFixUtils.blockState("minecraft:tall_grass", Map.of("half", "upper"));
/*  62 */   static final Dynamic<?> UPPER_LARGE_FERN = ExtraDataFixUtils.blockState("minecraft:large_fern", Map.of("half", "upper"));
/*  63 */   static final Dynamic<?> UPPER_ROSE_BUSH = ExtraDataFixUtils.blockState("minecraft:rose_bush", Map.of("half", "upper"));
/*  64 */   static final Dynamic<?> UPPER_PEONY = ExtraDataFixUtils.blockState("minecraft:peony", Map.of("half", "upper")); static final Map<String, Dynamic<?>> FLOWER_POT_MAP; static final Map<String, Dynamic<?>> SKULL_MAP; static final Map<String, Dynamic<?>> DOOR_MAP;
/*     */   static {
/*  66 */     FLOWER_POT_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("minecraft:air0", ExtraDataFixUtils.blockState("minecraft:flower_pot"));
/*     */           
/*     */           paramHashMap.put("minecraft:red_flower0", ExtraDataFixUtils.blockState("minecraft:potted_poppy"));
/*     */           paramHashMap.put("minecraft:red_flower1", ExtraDataFixUtils.blockState("minecraft:potted_blue_orchid"));
/*     */           paramHashMap.put("minecraft:red_flower2", ExtraDataFixUtils.blockState("minecraft:potted_allium"));
/*     */           paramHashMap.put("minecraft:red_flower3", ExtraDataFixUtils.blockState("minecraft:potted_azure_bluet"));
/*     */           paramHashMap.put("minecraft:red_flower4", ExtraDataFixUtils.blockState("minecraft:potted_red_tulip"));
/*     */           paramHashMap.put("minecraft:red_flower5", ExtraDataFixUtils.blockState("minecraft:potted_orange_tulip"));
/*     */           paramHashMap.put("minecraft:red_flower6", ExtraDataFixUtils.blockState("minecraft:potted_white_tulip"));
/*     */           paramHashMap.put("minecraft:red_flower7", ExtraDataFixUtils.blockState("minecraft:potted_pink_tulip"));
/*     */           paramHashMap.put("minecraft:red_flower8", ExtraDataFixUtils.blockState("minecraft:potted_oxeye_daisy"));
/*     */           paramHashMap.put("minecraft:yellow_flower0", ExtraDataFixUtils.blockState("minecraft:potted_dandelion"));
/*     */           paramHashMap.put("minecraft:sapling0", ExtraDataFixUtils.blockState("minecraft:potted_oak_sapling"));
/*     */           paramHashMap.put("minecraft:sapling1", ExtraDataFixUtils.blockState("minecraft:potted_spruce_sapling"));
/*     */           paramHashMap.put("minecraft:sapling2", ExtraDataFixUtils.blockState("minecraft:potted_birch_sapling"));
/*     */           paramHashMap.put("minecraft:sapling3", ExtraDataFixUtils.blockState("minecraft:potted_jungle_sapling"));
/*     */           paramHashMap.put("minecraft:sapling4", ExtraDataFixUtils.blockState("minecraft:potted_acacia_sapling"));
/*     */           paramHashMap.put("minecraft:sapling5", ExtraDataFixUtils.blockState("minecraft:potted_dark_oak_sapling"));
/*     */           paramHashMap.put("minecraft:red_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_red_mushroom"));
/*     */           paramHashMap.put("minecraft:brown_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_brown_mushroom"));
/*     */           paramHashMap.put("minecraft:deadbush0", ExtraDataFixUtils.blockState("minecraft:potted_dead_bush"));
/*     */           paramHashMap.put("minecraft:tallgrass2", ExtraDataFixUtils.blockState("minecraft:potted_fern"));
/*     */           paramHashMap.put("minecraft:cactus0", ExtraDataFixUtils.blockState("minecraft:potted_cactus"));
/*     */         });
/*  91 */     SKULL_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           mapSkull(paramHashMap, 0, "skeleton", "skull");
/*     */ 
/*     */           
/*     */           mapSkull(paramHashMap, 1, "wither_skeleton", "skull");
/*     */ 
/*     */           
/*     */           mapSkull(paramHashMap, 2, "zombie", "head");
/*     */ 
/*     */           
/*     */           mapSkull(paramHashMap, 3, "player", "head");
/*     */ 
/*     */           
/*     */           mapSkull(paramHashMap, 4, "creeper", "head");
/*     */ 
/*     */           
/*     */           mapSkull(paramHashMap, 5, "dragon", "head");
/*     */         });
/*     */     
/* 110 */     DOOR_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           mapDoor(paramHashMap, "oak_door");
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
/*     */           mapDoor(paramHashMap, "iron_door");
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
/*     */           mapDoor(paramHashMap, "spruce_door");
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
/*     */           mapDoor(paramHashMap, "birch_door");
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
/*     */           mapDoor(paramHashMap, "jungle_door");
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
/*     */           mapDoor(paramHashMap, "acacia_door");
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
/*     */           mapDoor(paramHashMap, "dark_oak_door");
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     NOTE_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           for (byte b = 0; b < 26; b++) {
/*     */             paramHashMap.put("true" + b, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "true", "note", String.valueOf(b))));
/*     */             
/*     */             paramHashMap.put("false" + b, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "false", "note", String.valueOf(b))));
/*     */           } 
/*     */         });
/* 195 */     DYE_COLOR_MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> {
/*     */           paramInt2ObjectOpenHashMap.put(0, "white");
/*     */           
/*     */           paramInt2ObjectOpenHashMap.put(1, "orange");
/*     */           paramInt2ObjectOpenHashMap.put(2, "magenta");
/*     */           paramInt2ObjectOpenHashMap.put(3, "light_blue");
/*     */           paramInt2ObjectOpenHashMap.put(4, "yellow");
/*     */           paramInt2ObjectOpenHashMap.put(5, "lime");
/*     */           paramInt2ObjectOpenHashMap.put(6, "pink");
/*     */           paramInt2ObjectOpenHashMap.put(7, "gray");
/*     */           paramInt2ObjectOpenHashMap.put(8, "light_gray");
/*     */           paramInt2ObjectOpenHashMap.put(9, "cyan");
/*     */           paramInt2ObjectOpenHashMap.put(10, "purple");
/*     */           paramInt2ObjectOpenHashMap.put(11, "blue");
/*     */           paramInt2ObjectOpenHashMap.put(12, "brown");
/*     */           paramInt2ObjectOpenHashMap.put(13, "green");
/*     */           paramInt2ObjectOpenHashMap.put(14, "red");
/*     */           paramInt2ObjectOpenHashMap.put(15, "black");
/*     */         });
/* 214 */     BED_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry entry = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (!Objects.equals(entry.getValue(), "red")) {
/*     */               addBeds(paramHashMap, entry.getIntKey(), (String)entry.getValue());
/*     */             }
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     BANNER_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry entry = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (!Objects.equals(entry.getValue(), "white")) {
/*     */               addBanners(paramHashMap, 15 - entry.getIntKey(), (String)entry.getValue());
/*     */             }
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 257 */     FIX.set(2);
/* 258 */     FIX.set(3);
/* 259 */     FIX.set(110);
/*     */     
/* 261 */     FIX.set(140);
/* 262 */     FIX.set(144);
/*     */     
/* 264 */     FIX.set(25);
/*     */     
/* 266 */     FIX.set(86);
/*     */ 
/*     */     
/* 269 */     FIX.set(26);
/* 270 */     FIX.set(176);
/* 271 */     FIX.set(177);
/*     */     
/* 273 */     FIX.set(175);
/*     */     
/* 275 */     FIX.set(64);
/* 276 */     FIX.set(71);
/* 277 */     FIX.set(193);
/* 278 */     FIX.set(194);
/* 279 */     FIX.set(195);
/* 280 */     FIX.set(196);
/* 281 */     FIX.set(197);
/*     */     
/* 283 */     VIRTUAL.set(54);
/* 284 */     VIRTUAL.set(146);
/*     */     
/* 286 */     VIRTUAL.set(25);
/*     */     
/* 288 */     VIRTUAL.set(26);
/*     */     
/* 290 */     VIRTUAL.set(51);
/*     */     
/* 292 */     VIRTUAL.set(53);
/* 293 */     VIRTUAL.set(67);
/* 294 */     VIRTUAL.set(108);
/* 295 */     VIRTUAL.set(109);
/* 296 */     VIRTUAL.set(114);
/* 297 */     VIRTUAL.set(128);
/* 298 */     VIRTUAL.set(134);
/* 299 */     VIRTUAL.set(135);
/* 300 */     VIRTUAL.set(136);
/* 301 */     VIRTUAL.set(156);
/* 302 */     VIRTUAL.set(163);
/* 303 */     VIRTUAL.set(164);
/* 304 */     VIRTUAL.set(180);
/* 305 */     VIRTUAL.set(203);
/*     */     
/* 307 */     VIRTUAL.set(55);
/*     */     
/* 309 */     VIRTUAL.set(85);
/* 310 */     VIRTUAL.set(113);
/* 311 */     VIRTUAL.set(188);
/* 312 */     VIRTUAL.set(189);
/* 313 */     VIRTUAL.set(190);
/* 314 */     VIRTUAL.set(191);
/* 315 */     VIRTUAL.set(192);
/*     */     
/* 317 */     VIRTUAL.set(93);
/* 318 */     VIRTUAL.set(94);
/*     */     
/* 320 */     VIRTUAL.set(101);
/* 321 */     VIRTUAL.set(102);
/* 322 */     VIRTUAL.set(160);
/*     */     
/* 324 */     VIRTUAL.set(106);
/*     */ 
/*     */     
/* 327 */     VIRTUAL.set(107);
/* 328 */     VIRTUAL.set(183);
/* 329 */     VIRTUAL.set(184);
/* 330 */     VIRTUAL.set(185);
/* 331 */     VIRTUAL.set(186);
/* 332 */     VIRTUAL.set(187);
/*     */     
/* 334 */     VIRTUAL.set(132);
/* 335 */     VIRTUAL.set(139);
/*     */     
/* 337 */     VIRTUAL.set(199);
/*     */   }
/*     */   static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP; private static final Int2ObjectMap<String> DYE_COLOR_MAP; static final Map<String, Dynamic<?>> BED_BLOCK_MAP; static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP;
/* 340 */   static final Dynamic<?> AIR = ExtraDataFixUtils.blockState("minecraft:air");
/*     */   
/*     */   private static void mapSkull(Map<String, Dynamic<?>> paramMap, int paramInt, String paramString1, String paramString2) {
/*     */     paramMap.put("" + paramInt + "north", ExtraDataFixUtils.blockState("minecraft:" + paramString1 + "_wall_" + paramString2, Map.of("facing", "north")));
/*     */     paramMap.put("" + paramInt + "east", ExtraDataFixUtils.blockState("minecraft:" + paramString1 + "_wall_" + paramString2, Map.of("facing", "east")));
/*     */     paramMap.put("" + paramInt + "south", ExtraDataFixUtils.blockState("minecraft:" + paramString1 + "_wall_" + paramString2, Map.of("facing", "south")));
/*     */     paramMap.put("" + paramInt + "west", ExtraDataFixUtils.blockState("minecraft:" + paramString1 + "_wall_" + paramString2, Map.of("facing", "west")));
/*     */     for (byte b = 0; b < 16; b++)
/*     */       paramMap.put("" + paramInt + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString1 + "_" + paramString2, Map.of("rotation", String.valueOf(b)))); 
/*     */   }
/*     */   
/*     */   private static void mapDoor(Map<String, Dynamic<?>> paramMap, String paramString) {
/*     */     String str = "minecraft:" + paramString;
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastlowerrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "eastupperrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northlowerrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "northupperrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southlowerrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "southupperrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westlowerrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperleftfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperleftfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperlefttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperlefttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperrightfalsefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperrightfalsetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperrighttruefalse", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/*     */     paramMap.put("minecraft:" + paramString + "westupperrighttruetrue", ExtraDataFixUtils.blockState(str, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */   }
/*     */   
/*     */   private static void addBeds(Map<String, Dynamic<?>> paramMap, int paramInt, String paramString) {
/*     */     paramMap.put("southfalsefoot" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "south", "occupied", "false", "part", "foot")));
/*     */     paramMap.put("westfalsefoot" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "west", "occupied", "false", "part", "foot")));
/*     */     paramMap.put("northfalsefoot" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "north", "occupied", "false", "part", "foot")));
/*     */     paramMap.put("eastfalsefoot" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "east", "occupied", "false", "part", "foot")));
/*     */     paramMap.put("southfalsehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "south", "occupied", "false", "part", "head")));
/*     */     paramMap.put("westfalsehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "west", "occupied", "false", "part", "head")));
/*     */     paramMap.put("northfalsehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "north", "occupied", "false", "part", "head")));
/*     */     paramMap.put("eastfalsehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "east", "occupied", "false", "part", "head")));
/*     */     paramMap.put("southtruehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "south", "occupied", "true", "part", "head")));
/*     */     paramMap.put("westtruehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "west", "occupied", "true", "part", "head")));
/*     */     paramMap.put("northtruehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "north", "occupied", "true", "part", "head")));
/*     */     paramMap.put("easttruehead" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_bed", Map.of("facing", "east", "occupied", "true", "part", "head")));
/*     */   }
/*     */   
/*     */   private static void addBanners(Map<String, Dynamic<?>> paramMap, int paramInt, String paramString) {
/*     */     for (byte b = 0; b < 16; b++)
/*     */       paramMap.put("" + b + "_" + b, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_banner", Map.of("rotation", String.valueOf(b)))); 
/*     */     paramMap.put("north_" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_wall_banner", Map.of("facing", "north")));
/*     */     paramMap.put("south_" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_wall_banner", Map.of("facing", "south")));
/*     */     paramMap.put("west_" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_wall_banner", Map.of("facing", "west")));
/*     */     paramMap.put("east_" + paramInt, ExtraDataFixUtils.blockState("minecraft:" + paramString + "_wall_banner", Map.of("facing", "east")));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$MappingConstants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */