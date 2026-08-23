/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class V99
/*     */   extends Schema
/*     */ {
/*  75 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public V99(int paramInt, Schema paramSchema) {
/*  78 */     super(paramInt, paramSchema);
/*     */   }
/*     */   static final Map<String, String> ITEM_TO_BLOCKENTITY;
/*     */   protected static void registerThrowableProjectile(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  82 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerMinecart(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  89 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerInventory(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  95 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 102 */     HashMap<String, Supplier<TypeTemplate>> hashMap = Maps.newHashMap();
/*     */     
/* 104 */     paramSchema.register(hashMap, "Item", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 107 */     paramSchema.registerSimple(hashMap, "XPOrb");
/* 108 */     registerThrowableProjectile(paramSchema, hashMap, "ThrownEgg");
/* 109 */     paramSchema.registerSimple(hashMap, "LeashKnot");
/* 110 */     paramSchema.registerSimple(hashMap, "Painting");
/* 111 */     paramSchema.register(hashMap, "Arrow", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 114 */     paramSchema.register(hashMap, "TippedArrow", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 117 */     paramSchema.register(hashMap, "SpectralArrow", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 120 */     registerThrowableProjectile(paramSchema, hashMap, "Snowball");
/* 121 */     registerThrowableProjectile(paramSchema, hashMap, "Fireball");
/* 122 */     registerThrowableProjectile(paramSchema, hashMap, "SmallFireball");
/* 123 */     registerThrowableProjectile(paramSchema, hashMap, "ThrownEnderpearl");
/* 124 */     paramSchema.registerSimple(hashMap, "EyeOfEnderSignal");
/* 125 */     paramSchema.register(hashMap, "ThrownPotion", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema), "Potion", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 129 */     registerThrowableProjectile(paramSchema, hashMap, "ThrownExpBottle");
/* 130 */     paramSchema.register(hashMap, "ItemFrame", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 133 */     registerThrowableProjectile(paramSchema, hashMap, "WitherSkull");
/* 134 */     paramSchema.registerSimple(hashMap, "PrimedTnt");
/* 135 */     paramSchema.register(hashMap, "FallingSand", paramString -> DSL.optionalFields("Block", References.BLOCK_NAME.in(paramSchema), "TileEntityData", References.BLOCK_ENTITY.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 139 */     paramSchema.register(hashMap, "FireworksRocketEntity", paramString -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 142 */     paramSchema.registerSimple(hashMap, "Boat");
/*     */ 
/*     */     
/* 145 */     paramSchema.register(hashMap, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 149 */     registerMinecart(paramSchema, hashMap, "MinecartRideable");
/* 150 */     paramSchema.register(hashMap, "MinecartChest", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 154 */     registerMinecart(paramSchema, hashMap, "MinecartFurnace");
/* 155 */     registerMinecart(paramSchema, hashMap, "MinecartTNT");
/* 156 */     paramSchema.register(hashMap, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), References.UNTAGGED_SPAWNER.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 160 */     paramSchema.register(hashMap, "MinecartHopper", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 164 */     paramSchema.register(hashMap, "MinecartCommandBlock", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     paramSchema.registerSimple(hashMap, "ArmorStand");
/* 173 */     paramSchema.registerSimple(hashMap, "Creeper");
/* 174 */     paramSchema.registerSimple(hashMap, "Skeleton");
/* 175 */     paramSchema.registerSimple(hashMap, "Spider");
/* 176 */     paramSchema.registerSimple(hashMap, "Giant");
/* 177 */     paramSchema.registerSimple(hashMap, "Zombie");
/* 178 */     paramSchema.registerSimple(hashMap, "Slime");
/* 179 */     paramSchema.registerSimple(hashMap, "Ghast");
/* 180 */     paramSchema.registerSimple(hashMap, "PigZombie");
/* 181 */     paramSchema.register(hashMap, "Enderman", paramString -> DSL.optionalFields("carried", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 184 */     paramSchema.registerSimple(hashMap, "CaveSpider");
/* 185 */     paramSchema.registerSimple(hashMap, "Silverfish");
/* 186 */     paramSchema.registerSimple(hashMap, "Blaze");
/* 187 */     paramSchema.registerSimple(hashMap, "LavaSlime");
/* 188 */     paramSchema.registerSimple(hashMap, "EnderDragon");
/* 189 */     paramSchema.registerSimple(hashMap, "WitherBoss");
/* 190 */     paramSchema.registerSimple(hashMap, "Bat");
/* 191 */     paramSchema.registerSimple(hashMap, "Witch");
/* 192 */     paramSchema.registerSimple(hashMap, "Endermite");
/* 193 */     paramSchema.registerSimple(hashMap, "Guardian");
/* 194 */     paramSchema.registerSimple(hashMap, "Pig");
/* 195 */     paramSchema.registerSimple(hashMap, "Sheep");
/* 196 */     paramSchema.registerSimple(hashMap, "Cow");
/* 197 */     paramSchema.registerSimple(hashMap, "Chicken");
/* 198 */     paramSchema.registerSimple(hashMap, "Squid");
/* 199 */     paramSchema.registerSimple(hashMap, "Wolf");
/* 200 */     paramSchema.registerSimple(hashMap, "MushroomCow");
/* 201 */     paramSchema.registerSimple(hashMap, "SnowMan");
/* 202 */     paramSchema.registerSimple(hashMap, "Ozelot");
/* 203 */     paramSchema.registerSimple(hashMap, "VillagerGolem");
/* 204 */     paramSchema.register(hashMap, "EntityHorse", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "ArmorItem", References.ITEM_STACK.in(paramSchema), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     paramSchema.registerSimple(hashMap, "Rabbit");
/* 210 */     paramSchema.register(hashMap, "Villager", paramString -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     paramSchema.registerSimple(hashMap, "EnderCrystal");
/*     */ 
/*     */ 
/*     */     
/* 220 */     paramSchema.register(hashMap, "AreaEffectCloud", paramString -> DSL.optionalFields("Particle", References.PARTICLE.in(paramSchema)));
/*     */ 
/*     */     
/* 223 */     paramSchema.registerSimple(hashMap, "ShulkerBullet");
/* 224 */     paramSchema.registerSimple(hashMap, "DragonFireball");
/* 225 */     paramSchema.registerSimple(hashMap, "Shulker");
/*     */     
/* 227 */     return hashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 232 */     HashMap<String, Supplier<TypeTemplate>> hashMap = Maps.newHashMap();
/*     */     
/* 234 */     registerInventory(paramSchema, hashMap, "Furnace");
/* 235 */     registerInventory(paramSchema, hashMap, "Chest");
/* 236 */     paramSchema.registerSimple(hashMap, "EnderChest");
/* 237 */     paramSchema.register(hashMap, "RecordPlayer", paramString -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 240 */     registerInventory(paramSchema, hashMap, "Trap");
/* 241 */     registerInventory(paramSchema, hashMap, "Dropper");
/* 242 */     paramSchema.register(hashMap, "Sign", () -> sign(paramSchema));
/* 243 */     paramSchema.register(hashMap, "MobSpawner", paramString -> References.UNTAGGED_SPAWNER.in(paramSchema));
/* 244 */     paramSchema.registerSimple(hashMap, "Music");
/* 245 */     paramSchema.registerSimple(hashMap, "Piston");
/* 246 */     registerInventory(paramSchema, hashMap, "Cauldron");
/* 247 */     paramSchema.registerSimple(hashMap, "EnchantTable");
/* 248 */     paramSchema.registerSimple(hashMap, "Airportal");
/* 249 */     paramSchema.register(hashMap, "Control", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */     
/* 252 */     paramSchema.registerSimple(hashMap, "Beacon");
/* 253 */     paramSchema.register(hashMap, "Skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */     
/* 256 */     paramSchema.registerSimple(hashMap, "DLDetector");
/* 257 */     registerInventory(paramSchema, hashMap, "Hopper");
/* 258 */     paramSchema.registerSimple(hashMap, "Comparator");
/* 259 */     paramSchema.register(hashMap, "FlowerPot", paramString -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(paramSchema))));
/*     */ 
/*     */     
/* 262 */     paramSchema.register(hashMap, "Banner", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     paramSchema.registerSimple(hashMap, "Structure");
/* 269 */     paramSchema.registerSimple(hashMap, "EndGateway");
/* 270 */     return hashMap;
/*     */   }
/*     */   
/*     */   public static TypeTemplate sign(Schema paramSchema) {
/* 274 */     return DSL.optionalFields(new Pair[] {
/* 275 */           Pair.of("Text1", References.TEXT_COMPONENT.in(paramSchema)), 
/* 276 */           Pair.of("Text2", References.TEXT_COMPONENT.in(paramSchema)), 
/* 277 */           Pair.of("Text3", References.TEXT_COMPONENT.in(paramSchema)), 
/* 278 */           Pair.of("Text4", References.TEXT_COMPONENT.in(paramSchema)), 
/* 279 */           Pair.of("FilteredText1", References.TEXT_COMPONENT.in(paramSchema)), 
/* 280 */           Pair.of("FilteredText2", References.TEXT_COMPONENT.in(paramSchema)), 
/* 281 */           Pair.of("FilteredText3", References.TEXT_COMPONENT.in(paramSchema)), 
/* 282 */           Pair.of("FilteredText4", References.TEXT_COMPONENT.in(paramSchema))
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 288 */     paramSchema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(paramSchema))), References.LIGHTWEIGHT_LEVEL.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     paramSchema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 295 */     paramSchema.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "EnderItems", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 299 */     paramSchema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(paramSchema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(paramSchema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(paramSchema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     paramSchema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(paramSchema), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), paramMap)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     paramSchema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(paramSchema), References.ENTITY.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 316 */     paramSchema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 317 */     paramSchema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(paramSchema), DSL.optionalFields("CustomName", DSL.constType(DSL.string()), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), paramMap))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     paramSchema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(paramSchema)), "tag", itemStackTag(paramSchema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */     
/* 328 */     paramSchema.registerType(false, References.OPTIONS, DSL::remainder);
/* 329 */     paramSchema.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString())));
/* 330 */     paramSchema.registerType(false, References.ITEM_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 331 */     paramSchema.registerType(false, References.STATS, DSL::remainder);
/* 332 */     paramSchema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 333 */     paramSchema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 334 */     paramSchema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(paramSchema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     paramSchema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 342 */     paramSchema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 343 */     paramSchema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 344 */     paramSchema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(paramSchema)), "Teams", DSL.list(References.TEAM.in(paramSchema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(paramSchema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     paramSchema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 354 */     paramSchema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     paramSchema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 360 */     paramSchema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 361 */     paramSchema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/* 362 */     paramSchema.registerType(false, References.OBJECTIVE, DSL::remainder);
/* 363 */     paramSchema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(paramSchema), "MemberNameSuffix", References.TEXT_COMPONENT.in(paramSchema), "DisplayName", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     paramSchema.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
/* 370 */     paramSchema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 371 */     paramSchema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 372 */     paramSchema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(paramSchema))));
/*     */ 
/*     */     
/* 375 */     paramSchema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 376 */     paramSchema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(paramSchema), "buyB", References.ITEM_STACK.in(paramSchema), "sell", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 381 */     paramSchema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 382 */     paramSchema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 383 */     paramSchema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(paramSchema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(paramSchema))), "palette", DSL.list(References.BLOCK_STATE.in(paramSchema))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     paramSchema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 389 */     paramSchema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/* 390 */     paramSchema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.optional(DSL.field("Equipment", DSL.list(References.ITEM_STACK.in(paramSchema)))));
/*     */   }
/*     */   
/*     */   public static TypeTemplate itemStackTag(Schema paramSchema) {
/* 394 */     return DSL.optionalFields(new Pair[] {
/* 395 */           Pair.of("EntityTag", References.ENTITY_TREE.in(paramSchema)), 
/* 396 */           Pair.of("BlockEntityTag", References.BLOCK_ENTITY.in(paramSchema)), 
/* 397 */           Pair.of("CanDestroy", DSL.list(References.BLOCK_NAME.in(paramSchema))), 
/* 398 */           Pair.of("CanPlaceOn", DSL.list(References.BLOCK_NAME.in(paramSchema))), 
/* 399 */           Pair.of("Items", DSL.list(References.ITEM_STACK.in(paramSchema))), 
/* 400 */           Pair.of("ChargedProjectiles", DSL.list(References.ITEM_STACK.in(paramSchema))), 
/*     */ 
/*     */           
/* 403 */           Pair.of("pages", DSL.list(References.TEXT_COMPONENT.in(paramSchema))), 
/* 404 */           Pair.of("filtered_pages", DSL.compoundList(References.TEXT_COMPONENT.in(paramSchema))), 
/* 405 */           Pair.of("display", DSL.optionalFields("Name", References.TEXT_COMPONENT
/* 406 */               .in(paramSchema), "Lore", 
/* 407 */               DSL.list(References.TEXT_COMPONENT.in(paramSchema))))
/*     */         });
/*     */   }
/*     */   
/*     */   static {
/* 412 */     ITEM_TO_BLOCKENTITY = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("minecraft:furnace", "Furnace");
/*     */           paramHashMap.put("minecraft:lit_furnace", "Furnace");
/*     */           paramHashMap.put("minecraft:chest", "Chest");
/*     */           paramHashMap.put("minecraft:trapped_chest", "Chest");
/*     */           paramHashMap.put("minecraft:ender_chest", "EnderChest");
/*     */           paramHashMap.put("minecraft:jukebox", "RecordPlayer");
/*     */           paramHashMap.put("minecraft:dispenser", "Trap");
/*     */           paramHashMap.put("minecraft:dropper", "Dropper");
/*     */           paramHashMap.put("minecraft:sign", "Sign");
/*     */           paramHashMap.put("minecraft:mob_spawner", "MobSpawner");
/*     */           paramHashMap.put("minecraft:noteblock", "Music");
/*     */           paramHashMap.put("minecraft:brewing_stand", "Cauldron");
/*     */           paramHashMap.put("minecraft:enhanting_table", "EnchantTable");
/*     */           paramHashMap.put("minecraft:command_block", "CommandBlock");
/*     */           paramHashMap.put("minecraft:beacon", "Beacon");
/*     */           paramHashMap.put("minecraft:skull", "Skull");
/*     */           paramHashMap.put("minecraft:daylight_detector", "DLDetector");
/*     */           paramHashMap.put("minecraft:hopper", "Hopper");
/*     */           paramHashMap.put("minecraft:banner", "Banner");
/*     */           paramHashMap.put("minecraft:flower_pot", "FlowerPot");
/*     */           paramHashMap.put("minecraft:repeating_command_block", "CommandBlock");
/*     */           paramHashMap.put("minecraft:chain_command_block", "CommandBlock");
/*     */           paramHashMap.put("minecraft:standing_sign", "Sign");
/*     */           paramHashMap.put("minecraft:wall_sign", "Sign");
/*     */           paramHashMap.put("minecraft:piston_head", "Piston");
/*     */           paramHashMap.put("minecraft:daylight_detector_inverted", "DLDetector");
/*     */           paramHashMap.put("minecraft:unpowered_comparator", "Comparator");
/*     */           paramHashMap.put("minecraft:powered_comparator", "Comparator");
/*     */           paramHashMap.put("minecraft:wall_banner", "Banner");
/*     */           paramHashMap.put("minecraft:standing_banner", "Banner");
/*     */           paramHashMap.put("minecraft:structure_block", "Structure");
/*     */           paramHashMap.put("minecraft:end_portal", "Airportal");
/*     */           paramHashMap.put("minecraft:end_gateway", "EndGateway");
/*     */           paramHashMap.put("minecraft:shield", "Banner");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 454 */   public static final Map<String, String> ITEM_TO_ENTITY = Map.of("minecraft:armor_stand", "ArmorStand", "minecraft:painting", "Painting");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 459 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> param1DynamicOps, T param1T) {
/* 462 */         return V99.addNames(new Dynamic(param1DynamicOps, param1T), V99.ITEM_TO_BLOCKENTITY, V99.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */   
/*     */   protected static <T> T addNames(Dynamic<T> paramDynamic, Map<String, String> paramMap1, Map<String, String> paramMap2) {
/* 467 */     return (T)paramDynamic.update("tag", paramDynamic2 -> paramDynamic2.update("BlockEntityTag", ()).update("EntityTag", ()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 490 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V99.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */