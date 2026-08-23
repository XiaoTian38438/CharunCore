/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
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
/*     */ public class V1460
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public V1460(int paramInt, Schema paramSchema) {
/*  75 */     super(paramInt, paramSchema);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  79 */     paramSchema.registerSimple(paramMap, paramString);
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  83 */     paramSchema.register(paramMap, paramString, () -> V1458.nameableInventory(paramSchema));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/*  88 */     HashMap<String, Supplier<TypeTemplate>> hashMap = Maps.newHashMap();
/*     */     
/*  90 */     paramSchema.register(hashMap, "minecraft:area_effect_cloud", paramString -> DSL.optionalFields("Particle", References.PARTICLE.in(paramSchema)));
/*     */ 
/*     */     
/*  93 */     registerMob(paramSchema, hashMap, "minecraft:armor_stand");
/*  94 */     paramSchema.register(hashMap, "minecraft:arrow", paramString -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/*  97 */     registerMob(paramSchema, hashMap, "minecraft:bat");
/*  98 */     registerMob(paramSchema, hashMap, "minecraft:blaze");
/*  99 */     paramSchema.registerSimple(hashMap, "minecraft:boat");
/* 100 */     registerMob(paramSchema, hashMap, "minecraft:cave_spider");
/* 101 */     paramSchema.register(hashMap, "minecraft:chest_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 105 */     registerMob(paramSchema, hashMap, "minecraft:chicken");
/* 106 */     paramSchema.register(hashMap, "minecraft:commandblock_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 110 */     registerMob(paramSchema, hashMap, "minecraft:cow");
/* 111 */     registerMob(paramSchema, hashMap, "minecraft:creeper");
/* 112 */     paramSchema.register(hashMap, "minecraft:donkey", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 116 */     paramSchema.registerSimple(hashMap, "minecraft:dragon_fireball");
/* 117 */     paramSchema.registerSimple(hashMap, "minecraft:egg");
/* 118 */     registerMob(paramSchema, hashMap, "minecraft:elder_guardian");
/* 119 */     paramSchema.registerSimple(hashMap, "minecraft:ender_crystal");
/* 120 */     registerMob(paramSchema, hashMap, "minecraft:ender_dragon");
/* 121 */     paramSchema.register(hashMap, "minecraft:enderman", paramString -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 124 */     registerMob(paramSchema, hashMap, "minecraft:endermite");
/* 125 */     paramSchema.registerSimple(hashMap, "minecraft:ender_pearl");
/* 126 */     paramSchema.registerSimple(hashMap, "minecraft:evocation_fangs");
/* 127 */     registerMob(paramSchema, hashMap, "minecraft:evocation_illager");
/* 128 */     paramSchema.registerSimple(hashMap, "minecraft:eye_of_ender_signal");
/* 129 */     paramSchema.register(hashMap, "minecraft:falling_block", paramString -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(paramSchema), "TileEntityData", References.BLOCK_ENTITY.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 133 */     paramSchema.registerSimple(hashMap, "minecraft:fireball");
/* 134 */     paramSchema.register(hashMap, "minecraft:fireworks_rocket", paramString -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 137 */     paramSchema.register(hashMap, "minecraft:furnace_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 140 */     registerMob(paramSchema, hashMap, "minecraft:ghast");
/* 141 */     registerMob(paramSchema, hashMap, "minecraft:giant");
/* 142 */     registerMob(paramSchema, hashMap, "minecraft:guardian");
/* 143 */     paramSchema.register(hashMap, "minecraft:hopper_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 147 */     paramSchema.register(hashMap, "minecraft:horse", paramString -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(paramSchema), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 151 */     registerMob(paramSchema, hashMap, "minecraft:husk");
/* 152 */     registerMob(paramSchema, hashMap, "minecraft:illusion_illager");
/* 153 */     paramSchema.register(hashMap, "minecraft:item", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 156 */     paramSchema.register(hashMap, "minecraft:item_frame", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 159 */     paramSchema.registerSimple(hashMap, "minecraft:leash_knot");
/* 160 */     paramSchema.register(hashMap, "minecraft:llama", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema), "DecorItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     paramSchema.registerSimple(hashMap, "minecraft:llama_spit");
/* 166 */     registerMob(paramSchema, hashMap, "minecraft:magma_cube");
/* 167 */     paramSchema.register(hashMap, "minecraft:minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 170 */     registerMob(paramSchema, hashMap, "minecraft:mooshroom");
/* 171 */     paramSchema.register(hashMap, "minecraft:mule", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 175 */     registerMob(paramSchema, hashMap, "minecraft:ocelot");
/* 176 */     paramSchema.registerSimple(hashMap, "minecraft:painting");
/* 177 */     registerMob(paramSchema, hashMap, "minecraft:parrot");
/* 178 */     registerMob(paramSchema, hashMap, "minecraft:pig");
/* 179 */     registerMob(paramSchema, hashMap, "minecraft:polar_bear");
/* 180 */     paramSchema.register(hashMap, "minecraft:potion", paramString -> DSL.optionalFields("Potion", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 183 */     registerMob(paramSchema, hashMap, "minecraft:rabbit");
/* 184 */     registerMob(paramSchema, hashMap, "minecraft:sheep");
/* 185 */     registerMob(paramSchema, hashMap, "minecraft:shulker");
/* 186 */     paramSchema.registerSimple(hashMap, "minecraft:shulker_bullet");
/* 187 */     registerMob(paramSchema, hashMap, "minecraft:silverfish");
/* 188 */     registerMob(paramSchema, hashMap, "minecraft:skeleton");
/* 189 */     paramSchema.register(hashMap, "minecraft:skeleton_horse", paramString -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 192 */     registerMob(paramSchema, hashMap, "minecraft:slime");
/* 193 */     paramSchema.registerSimple(hashMap, "minecraft:small_fireball");
/* 194 */     paramSchema.registerSimple(hashMap, "minecraft:snowball");
/* 195 */     registerMob(paramSchema, hashMap, "minecraft:snowman");
/* 196 */     paramSchema.register(hashMap, "minecraft:spawner_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), References.UNTAGGED_SPAWNER.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 200 */     paramSchema.register(hashMap, "minecraft:spectral_arrow", paramString -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 203 */     registerMob(paramSchema, hashMap, "minecraft:spider");
/* 204 */     registerMob(paramSchema, hashMap, "minecraft:squid");
/* 205 */     registerMob(paramSchema, hashMap, "minecraft:stray");
/* 206 */     paramSchema.registerSimple(hashMap, "minecraft:tnt");
/* 207 */     paramSchema.register(hashMap, "minecraft:tnt_minecart", paramString -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 210 */     registerMob(paramSchema, hashMap, "minecraft:vex");
/* 211 */     paramSchema.register(hashMap, "minecraft:villager", paramString -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     registerMob(paramSchema, hashMap, "minecraft:villager_golem");
/* 218 */     registerMob(paramSchema, hashMap, "minecraft:vindication_illager");
/* 219 */     registerMob(paramSchema, hashMap, "minecraft:witch");
/* 220 */     registerMob(paramSchema, hashMap, "minecraft:wither");
/* 221 */     registerMob(paramSchema, hashMap, "minecraft:wither_skeleton");
/* 222 */     paramSchema.registerSimple(hashMap, "minecraft:wither_skull");
/* 223 */     registerMob(paramSchema, hashMap, "minecraft:wolf");
/* 224 */     paramSchema.registerSimple(hashMap, "minecraft:xp_bottle");
/* 225 */     paramSchema.registerSimple(hashMap, "minecraft:xp_orb");
/* 226 */     registerMob(paramSchema, hashMap, "minecraft:zombie");
/* 227 */     paramSchema.register(hashMap, "minecraft:zombie_horse", paramString -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 230 */     registerMob(paramSchema, hashMap, "minecraft:zombie_pigman");
/* 231 */     paramSchema.register(hashMap, "minecraft:zombie_villager", paramString -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     return hashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 242 */     HashMap<String, Supplier<TypeTemplate>> hashMap = Maps.newHashMap();
/*     */     
/* 244 */     registerInventory(paramSchema, hashMap, "minecraft:furnace");
/* 245 */     registerInventory(paramSchema, hashMap, "minecraft:chest");
/* 246 */     registerInventory(paramSchema, hashMap, "minecraft:trapped_chest");
/* 247 */     paramSchema.registerSimple(hashMap, "minecraft:ender_chest");
/* 248 */     paramSchema.register(hashMap, "minecraft:jukebox", paramString -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 251 */     registerInventory(paramSchema, hashMap, "minecraft:dispenser");
/* 252 */     registerInventory(paramSchema, hashMap, "minecraft:dropper");
/* 253 */     paramSchema.register(hashMap, "minecraft:sign", () -> V99.sign(paramSchema));
/* 254 */     paramSchema.register(hashMap, "minecraft:mob_spawner", paramString -> References.UNTAGGED_SPAWNER.in(paramSchema));
/* 255 */     paramSchema.register(hashMap, "minecraft:piston", paramString -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(paramSchema)));
/*     */ 
/*     */     
/* 258 */     registerInventory(paramSchema, hashMap, "minecraft:brewing_stand");
/* 259 */     paramSchema.register(hashMap, "minecraft:enchanting_table", () -> V1458.nameable(paramSchema));
/* 260 */     paramSchema.registerSimple(hashMap, "minecraft:end_portal");
/* 261 */     paramSchema.register(hashMap, "minecraft:beacon", () -> V1458.nameable(paramSchema));
/* 262 */     paramSchema.register(hashMap, "minecraft:skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */     
/* 265 */     paramSchema.registerSimple(hashMap, "minecraft:daylight_detector");
/* 266 */     registerInventory(paramSchema, hashMap, "minecraft:hopper");
/* 267 */     paramSchema.registerSimple(hashMap, "minecraft:comparator");
/* 268 */     paramSchema.register(hashMap, "minecraft:banner", () -> V1458.nameable(paramSchema));
/* 269 */     paramSchema.registerSimple(hashMap, "minecraft:structure_block");
/* 270 */     paramSchema.registerSimple(hashMap, "minecraft:end_gateway");
/* 271 */     paramSchema.register(hashMap, "minecraft:command_block", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */     
/* 274 */     registerInventory(paramSchema, hashMap, "minecraft:shulker_box");
/* 275 */     paramSchema.registerSimple(hashMap, "minecraft:bed");
/*     */     
/* 277 */     return hashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 282 */     paramSchema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(paramSchema))), References.LIGHTWEIGHT_LEVEL.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     paramSchema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 289 */     paramSchema.registerType(false, References.RECIPE, () -> DSL.constType(namespacedString()));
/* 290 */     paramSchema.registerType(false, References.PLAYER, () -> DSL.optionalFields(new Pair[] { Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(paramSchema))), Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(paramSchema))), Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema))), Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(paramSchema))), Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(paramSchema)), Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(paramSchema)), Pair.of("recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(paramSchema)), "toBeDisplayed", DSL.list(References.RECIPE.in(paramSchema)))) }));
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
/* 306 */     paramSchema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(paramSchema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(paramSchema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(paramSchema))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(paramSchema)))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     paramSchema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(paramSchema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), paramMap)));
/*     */ 
/*     */ 
/*     */     
/* 320 */     paramSchema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(paramSchema)), References.ENTITY.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 324 */     paramSchema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(paramSchema), DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(paramSchema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), paramMap))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     paramSchema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(paramSchema), "tag", V99.itemStackTag(paramSchema)), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */     
/* 335 */     paramSchema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(paramSchema))));
/* 336 */     paramSchema.registerType(false, References.OPTIONS, DSL::remainder);
/* 337 */     paramSchema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(paramSchema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(paramSchema))), "palette", DSL.list(References.BLOCK_STATE.in(paramSchema))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     paramSchema.registerType(false, References.BLOCK_NAME, () -> DSL.constType(namespacedString()));
/* 343 */     paramSchema.registerType(false, References.ITEM_NAME, () -> DSL.constType(namespacedString()));
/* 344 */     paramSchema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 345 */     paramSchema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/*     */     
/* 347 */     Supplier supplier = () -> DSL.compoundList(References.ITEM_NAME.in(paramSchema), DSL.constType(DSL.intType()));
/*     */     
/* 349 */     paramSchema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(new Pair[] { Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", paramSupplier.get()), Pair.of("minecraft:used", paramSupplier.get()), Pair.of("minecraft:broken", paramSupplier.get()), Pair.of("minecraft:picked_up", paramSupplier.get()), Pair.of("minecraft:dropped", paramSupplier.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))) })));
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
/* 362 */     paramSchema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 363 */     paramSchema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 364 */     paramSchema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(paramSchema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     paramSchema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 372 */     paramSchema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 373 */     paramSchema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 374 */     paramSchema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(paramSchema)), "Teams", DSL.list(References.TEAM.in(paramSchema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(paramSchema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     paramSchema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 384 */     paramSchema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     paramSchema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 390 */     paramSchema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 391 */     paramSchema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/*     */     
/* 393 */     Map<String, Supplier<TypeTemplate>> map = V1451_6.createCriterionTypes(paramSchema);
/* 394 */     paramSchema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), paramMap), "DisplayName", References.TEXT_COMPONENT.in(paramSchema)), V1451_6.UNPACK_OBJECTIVE_ID, V1451_6.REPACK_OBJECTIVE_ID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     paramSchema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(paramSchema), "MemberNameSuffix", References.TEXT_COMPONENT.in(paramSchema), "DisplayName", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     paramSchema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(paramSchema))), "SpawnData", References.ENTITY_TREE.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     paramSchema.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(paramSchema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.string())))));
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
/* 427 */     paramSchema.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 428 */     paramSchema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/* 429 */     paramSchema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 430 */     paramSchema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 431 */     paramSchema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(paramSchema))));
/*     */ 
/*     */     
/* 434 */     paramSchema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 435 */     paramSchema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(paramSchema), "buyB", References.ITEM_STACK.in(paramSchema), "sell", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     paramSchema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 441 */     paramSchema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 442 */     paramSchema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(paramSchema)))), new TypeTemplate[] { DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(paramSchema)))), DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(paramSchema))), DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(paramSchema))) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1460.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */