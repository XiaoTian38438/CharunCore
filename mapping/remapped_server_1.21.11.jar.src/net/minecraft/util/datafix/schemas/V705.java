/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
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
/*     */ public class V705
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public V705(int paramInt, Schema paramSchema) {
/*  34 */     super(paramInt, paramSchema);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  38 */     paramSchema.registerSimple(paramMap, paramString);
/*     */   }
/*     */   
/*     */   protected static void registerThrowableProjectile(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/*  42 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/*  49 */     HashMap<String, Supplier<TypeTemplate>> hashMap = Maps.newHashMap();
/*     */     
/*  51 */     paramSchema.register(hashMap, "minecraft:area_effect_cloud", paramString -> DSL.optionalFields("Particle", References.PARTICLE.in(paramSchema)));
/*     */ 
/*     */     
/*  54 */     registerMob(paramSchema, hashMap, "minecraft:armor_stand");
/*  55 */     paramSchema.register(hashMap, "minecraft:arrow", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/*  58 */     registerMob(paramSchema, hashMap, "minecraft:bat");
/*  59 */     registerMob(paramSchema, hashMap, "minecraft:blaze");
/*  60 */     paramSchema.registerSimple(hashMap, "minecraft:boat");
/*  61 */     registerMob(paramSchema, hashMap, "minecraft:cave_spider");
/*  62 */     paramSchema.register(hashMap, "minecraft:chest_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/*  66 */     registerMob(paramSchema, hashMap, "minecraft:chicken");
/*  67 */     paramSchema.register(hashMap, "minecraft:commandblock_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/*  71 */     registerMob(paramSchema, hashMap, "minecraft:cow");
/*  72 */     registerMob(paramSchema, hashMap, "minecraft:creeper");
/*  73 */     paramSchema.register(hashMap, "minecraft:donkey", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/*  77 */     paramSchema.registerSimple(hashMap, "minecraft:dragon_fireball");
/*  78 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:egg");
/*  79 */     registerMob(paramSchema, hashMap, "minecraft:elder_guardian");
/*  80 */     paramSchema.registerSimple(hashMap, "minecraft:ender_crystal");
/*  81 */     registerMob(paramSchema, hashMap, "minecraft:ender_dragon");
/*  82 */     paramSchema.register(hashMap, "minecraft:enderman", paramString -> DSL.optionalFields("carried", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/*  85 */     registerMob(paramSchema, hashMap, "minecraft:endermite");
/*  86 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:ender_pearl");
/*  87 */     paramSchema.registerSimple(hashMap, "minecraft:eye_of_ender_signal");
/*  88 */     paramSchema.register(hashMap, "minecraft:falling_block", paramString -> DSL.optionalFields("Block", References.BLOCK_NAME.in(paramSchema), "TileEntityData", References.BLOCK_ENTITY.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/*  92 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:fireball");
/*  93 */     paramSchema.register(hashMap, "minecraft:fireworks_rocket", paramString -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/*  96 */     paramSchema.register(hashMap, "minecraft:furnace_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/*  99 */     registerMob(paramSchema, hashMap, "minecraft:ghast");
/* 100 */     registerMob(paramSchema, hashMap, "minecraft:giant");
/* 101 */     registerMob(paramSchema, hashMap, "minecraft:guardian");
/* 102 */     paramSchema.register(hashMap, "minecraft:hopper_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*     */ 
/*     */ 
/*     */     
/* 106 */     paramSchema.register(hashMap, "minecraft:horse", paramString -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(paramSchema), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     registerMob(paramSchema, hashMap, "minecraft:husk");
/* 112 */     paramSchema.register(hashMap, "minecraft:item", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 115 */     paramSchema.register(hashMap, "minecraft:item_frame", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 118 */     paramSchema.registerSimple(hashMap, "minecraft:leash_knot");
/* 119 */     registerMob(paramSchema, hashMap, "minecraft:magma_cube");
/* 120 */     paramSchema.register(hashMap, "minecraft:minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 123 */     registerMob(paramSchema, hashMap, "minecraft:mooshroom");
/* 124 */     paramSchema.register(hashMap, "minecraft:mule", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 128 */     registerMob(paramSchema, hashMap, "minecraft:ocelot");
/* 129 */     paramSchema.registerSimple(hashMap, "minecraft:painting");
/* 130 */     registerMob(paramSchema, hashMap, "minecraft:parrot");
/* 131 */     registerMob(paramSchema, hashMap, "minecraft:pig");
/* 132 */     registerMob(paramSchema, hashMap, "minecraft:polar_bear");
/* 133 */     paramSchema.register(hashMap, "minecraft:potion", paramString -> DSL.optionalFields("Potion", References.ITEM_STACK.in(paramSchema), "inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 137 */     registerMob(paramSchema, hashMap, "minecraft:rabbit");
/* 138 */     registerMob(paramSchema, hashMap, "minecraft:sheep");
/* 139 */     registerMob(paramSchema, hashMap, "minecraft:shulker");
/* 140 */     paramSchema.registerSimple(hashMap, "minecraft:shulker_bullet");
/* 141 */     registerMob(paramSchema, hashMap, "minecraft:silverfish");
/* 142 */     registerMob(paramSchema, hashMap, "minecraft:skeleton");
/* 143 */     paramSchema.register(hashMap, "minecraft:skeleton_horse", paramString -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 146 */     registerMob(paramSchema, hashMap, "minecraft:slime");
/* 147 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:small_fireball");
/* 148 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:snowball");
/* 149 */     registerMob(paramSchema, hashMap, "minecraft:snowman");
/* 150 */     paramSchema.register(hashMap, "minecraft:spawner_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema), References.UNTAGGED_SPAWNER.in(paramSchema)));
/*     */ 
/*     */ 
/*     */     
/* 154 */     paramSchema.register(hashMap, "minecraft:spectral_arrow", paramString -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 157 */     registerMob(paramSchema, hashMap, "minecraft:spider");
/* 158 */     registerMob(paramSchema, hashMap, "minecraft:squid");
/* 159 */     registerMob(paramSchema, hashMap, "minecraft:stray");
/* 160 */     paramSchema.registerSimple(hashMap, "minecraft:tnt");
/* 161 */     paramSchema.register(hashMap, "minecraft:tnt_minecart", paramString -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(paramSchema)));
/*     */ 
/*     */     
/* 164 */     paramSchema.register(hashMap, "minecraft:villager", paramString -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     registerMob(paramSchema, hashMap, "minecraft:villager_golem");
/* 171 */     registerMob(paramSchema, hashMap, "minecraft:witch");
/* 172 */     registerMob(paramSchema, hashMap, "minecraft:wither");
/* 173 */     registerMob(paramSchema, hashMap, "minecraft:wither_skeleton");
/* 174 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:wither_skull");
/* 175 */     registerMob(paramSchema, hashMap, "minecraft:wolf");
/* 176 */     registerThrowableProjectile(paramSchema, hashMap, "minecraft:xp_bottle");
/* 177 */     paramSchema.registerSimple(hashMap, "minecraft:xp_orb");
/* 178 */     registerMob(paramSchema, hashMap, "minecraft:zombie");
/* 179 */     paramSchema.register(hashMap, "minecraft:zombie_horse", paramString -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */     
/* 182 */     registerMob(paramSchema, hashMap, "minecraft:zombie_pigman");
/* 183 */     paramSchema.register(hashMap, "minecraft:zombie_villager", paramString -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     paramSchema.registerSimple(hashMap, "minecraft:evocation_fangs");
/* 191 */     registerMob(paramSchema, hashMap, "minecraft:evocation_illager");
/* 192 */     registerMob(paramSchema, hashMap, "minecraft:illusion_illager");
/* 193 */     paramSchema.register(hashMap, "minecraft:llama", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema), "DecorItem", References.ITEM_STACK.in(paramSchema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     paramSchema.registerSimple(hashMap, "minecraft:llama_spit");
/* 199 */     registerMob(paramSchema, hashMap, "minecraft:vex");
/* 200 */     registerMob(paramSchema, hashMap, "minecraft:vindication_illager");
/*     */     
/* 202 */     return hashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 207 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 208 */     paramSchema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(paramSchema), DSL.optionalFields("CustomName", DSL.constType(DSL.string()), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), paramMap))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     paramSchema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(paramSchema), "tag", V99.itemStackTag(paramSchema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   static final Map<String, String> ITEM_TO_ENTITY = (Map<String, String>)ImmutableMap.builder()
/* 222 */     .put("minecraft:armor_stand", "minecraft:armor_stand")
/* 223 */     .put("minecraft:painting", "minecraft:painting")
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     .put("minecraft:armadillo_spawn_egg", "minecraft:armadillo")
/* 230 */     .put("minecraft:allay_spawn_egg", "minecraft:allay")
/* 231 */     .put("minecraft:axolotl_spawn_egg", "minecraft:axolotl")
/* 232 */     .put("minecraft:bat_spawn_egg", "minecraft:bat")
/* 233 */     .put("minecraft:bee_spawn_egg", "minecraft:bee")
/* 234 */     .put("minecraft:blaze_spawn_egg", "minecraft:blaze")
/* 235 */     .put("minecraft:bogged_spawn_egg", "minecraft:bogged")
/* 236 */     .put("minecraft:breeze_spawn_egg", "minecraft:breeze")
/* 237 */     .put("minecraft:cat_spawn_egg", "minecraft:cat")
/* 238 */     .put("minecraft:camel_spawn_egg", "minecraft:camel")
/* 239 */     .put("minecraft:cave_spider_spawn_egg", "minecraft:cave_spider")
/* 240 */     .put("minecraft:chicken_spawn_egg", "minecraft:chicken")
/* 241 */     .put("minecraft:cod_spawn_egg", "minecraft:cod")
/* 242 */     .put("minecraft:cow_spawn_egg", "minecraft:cow")
/* 243 */     .put("minecraft:creeper_spawn_egg", "minecraft:creeper")
/* 244 */     .put("minecraft:dolphin_spawn_egg", "minecraft:dolphin")
/* 245 */     .put("minecraft:donkey_spawn_egg", "minecraft:donkey")
/* 246 */     .put("minecraft:drowned_spawn_egg", "minecraft:drowned")
/* 247 */     .put("minecraft:elder_guardian_spawn_egg", "minecraft:elder_guardian")
/* 248 */     .put("minecraft:ender_dragon_spawn_egg", "minecraft:ender_dragon")
/* 249 */     .put("minecraft:enderman_spawn_egg", "minecraft:enderman")
/* 250 */     .put("minecraft:endermite_spawn_egg", "minecraft:endermite")
/* 251 */     .put("minecraft:evoker_spawn_egg", "minecraft:evoker")
/* 252 */     .put("minecraft:fox_spawn_egg", "minecraft:fox")
/* 253 */     .put("minecraft:frog_spawn_egg", "minecraft:frog")
/* 254 */     .put("minecraft:ghast_spawn_egg", "minecraft:ghast")
/* 255 */     .put("minecraft:glow_squid_spawn_egg", "minecraft:glow_squid")
/* 256 */     .put("minecraft:goat_spawn_egg", "minecraft:goat")
/* 257 */     .put("minecraft:guardian_spawn_egg", "minecraft:guardian")
/* 258 */     .put("minecraft:hoglin_spawn_egg", "minecraft:hoglin")
/* 259 */     .put("minecraft:horse_spawn_egg", "minecraft:horse")
/* 260 */     .put("minecraft:husk_spawn_egg", "minecraft:husk")
/* 261 */     .put("minecraft:iron_golem_spawn_egg", "minecraft:iron_golem")
/* 262 */     .put("minecraft:llama_spawn_egg", "minecraft:llama")
/* 263 */     .put("minecraft:magma_cube_spawn_egg", "minecraft:magma_cube")
/* 264 */     .put("minecraft:mooshroom_spawn_egg", "minecraft:mooshroom")
/* 265 */     .put("minecraft:mule_spawn_egg", "minecraft:mule")
/* 266 */     .put("minecraft:ocelot_spawn_egg", "minecraft:ocelot")
/* 267 */     .put("minecraft:panda_spawn_egg", "minecraft:panda")
/* 268 */     .put("minecraft:parrot_spawn_egg", "minecraft:parrot")
/* 269 */     .put("minecraft:phantom_spawn_egg", "minecraft:phantom")
/* 270 */     .put("minecraft:pig_spawn_egg", "minecraft:pig")
/* 271 */     .put("minecraft:piglin_spawn_egg", "minecraft:piglin")
/* 272 */     .put("minecraft:piglin_brute_spawn_egg", "minecraft:piglin_brute")
/* 273 */     .put("minecraft:pillager_spawn_egg", "minecraft:pillager")
/* 274 */     .put("minecraft:polar_bear_spawn_egg", "minecraft:polar_bear")
/* 275 */     .put("minecraft:pufferfish_spawn_egg", "minecraft:pufferfish")
/* 276 */     .put("minecraft:rabbit_spawn_egg", "minecraft:rabbit")
/* 277 */     .put("minecraft:ravager_spawn_egg", "minecraft:ravager")
/* 278 */     .put("minecraft:salmon_spawn_egg", "minecraft:salmon")
/* 279 */     .put("minecraft:sheep_spawn_egg", "minecraft:sheep")
/* 280 */     .put("minecraft:shulker_spawn_egg", "minecraft:shulker")
/* 281 */     .put("minecraft:silverfish_spawn_egg", "minecraft:silverfish")
/* 282 */     .put("minecraft:skeleton_spawn_egg", "minecraft:skeleton")
/* 283 */     .put("minecraft:skeleton_horse_spawn_egg", "minecraft:skeleton_horse")
/* 284 */     .put("minecraft:slime_spawn_egg", "minecraft:slime")
/* 285 */     .put("minecraft:sniffer_spawn_egg", "minecraft:sniffer")
/* 286 */     .put("minecraft:snow_golem_spawn_egg", "minecraft:snow_golem")
/* 287 */     .put("minecraft:spider_spawn_egg", "minecraft:spider")
/* 288 */     .put("minecraft:squid_spawn_egg", "minecraft:squid")
/* 289 */     .put("minecraft:stray_spawn_egg", "minecraft:stray")
/* 290 */     .put("minecraft:strider_spawn_egg", "minecraft:strider")
/* 291 */     .put("minecraft:tadpole_spawn_egg", "minecraft:tadpole")
/* 292 */     .put("minecraft:trader_llama_spawn_egg", "minecraft:trader_llama")
/* 293 */     .put("minecraft:tropical_fish_spawn_egg", "minecraft:tropical_fish")
/* 294 */     .put("minecraft:turtle_spawn_egg", "minecraft:turtle")
/* 295 */     .put("minecraft:vex_spawn_egg", "minecraft:vex")
/* 296 */     .put("minecraft:villager_spawn_egg", "minecraft:villager")
/* 297 */     .put("minecraft:vindicator_spawn_egg", "minecraft:vindicator")
/* 298 */     .put("minecraft:wandering_trader_spawn_egg", "minecraft:wandering_trader")
/* 299 */     .put("minecraft:warden_spawn_egg", "minecraft:warden")
/* 300 */     .put("minecraft:witch_spawn_egg", "minecraft:witch")
/* 301 */     .put("minecraft:wither_spawn_egg", "minecraft:wither")
/* 302 */     .put("minecraft:wither_skeleton_spawn_egg", "minecraft:wither_skeleton")
/* 303 */     .put("minecraft:wolf_spawn_egg", "minecraft:wolf")
/* 304 */     .put("minecraft:zoglin_spawn_egg", "minecraft:zoglin")
/* 305 */     .put("minecraft:zombie_spawn_egg", "minecraft:zombie")
/* 306 */     .put("minecraft:zombie_horse_spawn_egg", "minecraft:zombie_horse")
/* 307 */     .put("minecraft:zombie_villager_spawn_egg", "minecraft:zombie_villager")
/* 308 */     .put("minecraft:zombified_piglin_spawn_egg", "minecraft:zombified_piglin")
/* 309 */     .put("minecraft:item_frame", "minecraft:item_frame")
/* 310 */     .put("minecraft:boat", "minecraft:oak_boat")
/* 311 */     .put("minecraft:oak_boat", "minecraft:oak_boat")
/* 312 */     .put("minecraft:oak_chest_boat", "minecraft:oak_chest_boat")
/* 313 */     .put("minecraft:spruce_boat", "minecraft:spruce_boat")
/* 314 */     .put("minecraft:spruce_chest_boat", "minecraft:spruce_chest_boat")
/* 315 */     .put("minecraft:birch_boat", "minecraft:birch_boat")
/* 316 */     .put("minecraft:birch_chest_boat", "minecraft:birch_chest_boat")
/* 317 */     .put("minecraft:jungle_boat", "minecraft:jungle_boat")
/* 318 */     .put("minecraft:jungle_chest_boat", "minecraft:jungle_chest_boat")
/* 319 */     .put("minecraft:acacia_boat", "minecraft:acacia_boat")
/* 320 */     .put("minecraft:acacia_chest_boat", "minecraft:acacia_chest_boat")
/* 321 */     .put("minecraft:cherry_boat", "minecraft:cherry_boat")
/* 322 */     .put("minecraft:cherry_chest_boat", "minecraft:cherry_chest_boat")
/* 323 */     .put("minecraft:dark_oak_boat", "minecraft:dark_oak_boat")
/* 324 */     .put("minecraft:dark_oak_chest_boat", "minecraft:dark_oak_chest_boat")
/* 325 */     .put("minecraft:mangrove_boat", "minecraft:mangrove_boat")
/* 326 */     .put("minecraft:mangrove_chest_boat", "minecraft:mangrove_chest_boat")
/* 327 */     .put("minecraft:bamboo_raft", "minecraft:bamboo_raft")
/* 328 */     .put("minecraft:bamboo_chest_raft", "minecraft:bamboo_chest_raft")
/* 329 */     .put("minecraft:minecart", "minecraft:minecart")
/* 330 */     .put("minecraft:chest_minecart", "minecraft:chest_minecart")
/* 331 */     .put("minecraft:furnace_minecart", "minecraft:furnace_minecart")
/* 332 */     .put("minecraft:tnt_minecart", "minecraft:tnt_minecart")
/* 333 */     .put("minecraft:hopper_minecart", "minecraft:hopper_minecart")
/* 334 */     .build();
/*     */   
/* 336 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> param1DynamicOps, T param1T) {
/* 339 */         return V99.addNames(new Dynamic(param1DynamicOps, param1T), V704.ITEM_TO_BLOCKENTITY, V705.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V705.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */