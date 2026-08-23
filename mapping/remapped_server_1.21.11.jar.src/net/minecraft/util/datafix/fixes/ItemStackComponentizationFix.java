/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemStackComponentizationFix extends DataFix {
/*     */   private static final int HIDE_ENCHANTMENTS = 1;
/*     */   private static final int HIDE_MODIFIERS = 2;
/*     */   private static final int HIDE_UNBREAKABLE = 4;
/*     */   private static final int HIDE_CAN_DESTROY = 8;
/*     */   private static final int HIDE_CAN_PLACE = 16;
/*     */   private static final int HIDE_ADDITIONAL = 32;
/*     */   private static final int HIDE_DYE = 64;
/*     */   private static final int HIDE_UPGRADES = 128;
/*  38 */   private static final Set<String> POTION_HOLDER_IDS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final Set<String> BUCKETED_MOB_IDS = Set.of("minecraft:pufferfish_bucket", "minecraft:salmon_bucket", "minecraft:cod_bucket", "minecraft:tropical_fish_bucket", "minecraft:axolotl_bucket", "minecraft:tadpole_bucket");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final List<String> BUCKETED_MOB_TAGS = List.of("NoAI", "Silent", "NoGravity", "Glowing", "Invulnerable", "Health", "Age", "Variant", "HuntingCooldown", "BucketVariantTag");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static final Set<String> BOOLEAN_BLOCK_STATE_PROPERTIES = Set.of(new String[] { "attached", "bottom", "conditional", "disarmed", "drag", "enabled", "extended", "eye", "falling", "hanging", "has_bottle_0", "has_bottle_1", "has_bottle_2", "has_record", "has_book", "inverted", "in_wall", "lit", "locked", "occupied", "open", "persistent", "powered", "short", "signal_fire", "snowy", "triggered", "unstable", "waterlogged", "berries", "bloom", "shrieking", "can_summon", "up", "down", "north", "east", "south", "west", "slot_0_occupied", "slot_1_occupied", "slot_2_occupied", "slot_3_occupied", "slot_4_occupied", "slot_5_occupied", "cracked", "crafting" });
/*     */   
/*  68 */   private static final Splitter PROPERTY_SPLITTER = Splitter.on(',');
/*     */   
/*     */   public ItemStackComponentizationFix(Schema paramSchema) {
/*  71 */     super(paramSchema, true);
/*     */   }
/*     */   
/*     */   private static void fixItemStack(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/*  75 */     int i = paramItemStackData.removeTag("HideFlags").asInt(0);
/*  76 */     paramItemStackData.moveTagToComponent("Damage", "minecraft:damage", paramDynamic.createInt(0));
/*  77 */     paramItemStackData.moveTagToComponent("RepairCost", "minecraft:repair_cost", paramDynamic.createInt(0));
/*  78 */     paramItemStackData.moveTagToComponent("CustomModelData", "minecraft:custom_model_data");
/*     */     
/*  80 */     paramItemStackData.removeTag("BlockStateTag").result()
/*  81 */       .ifPresent(paramDynamic -> paramItemStackData.setComponent("minecraft:block_state", fixBlockStateTag(paramDynamic)));
/*     */     
/*  83 */     paramItemStackData.moveTagToComponent("EntityTag", "minecraft:entity_data");
/*  84 */     paramItemStackData.fixSubTag("BlockEntityTag", false, paramDynamic -> {
/*     */           String str = NamespacedSchema.ensureNamespaced(paramDynamic.get("id").asString(""));
/*     */           
/*     */           paramDynamic = fixBlockEntityTag(paramItemStackData, paramDynamic, str);
/*     */           
/*     */           Dynamic dynamic = paramDynamic.remove("id");
/*     */           
/*     */           return dynamic.equals(paramDynamic.emptyMap()) ? dynamic : paramDynamic;
/*     */         });
/*  93 */     paramItemStackData.moveTagToComponent("BlockEntityTag", "minecraft:block_entity_data");
/*     */     
/*  95 */     if (paramItemStackData.removeTag("Unbreakable").asBoolean(false)) {
/*  96 */       Dynamic<?> dynamic = paramDynamic.emptyMap();
/*     */       
/*  98 */       if ((i & 0x4) != 0) {
/*  99 */         dynamic = dynamic.set("show_in_tooltip", paramDynamic.createBoolean(false));
/*     */       }
/* 101 */       paramItemStackData.setComponent("minecraft:unbreakable", dynamic);
/*     */     } 
/*     */     
/* 104 */     fixEnchantments(paramItemStackData, paramDynamic, "Enchantments", "minecraft:enchantments", ((i & 0x1) != 0));
/* 105 */     if (paramItemStackData.is("minecraft:enchanted_book"))
/*     */     {
/* 107 */       fixEnchantments(paramItemStackData, paramDynamic, "StoredEnchantments", "minecraft:stored_enchantments", ((i & 0x20) != 0));
/*     */     }
/*     */     
/* 110 */     paramItemStackData.fixSubTag("display", false, paramDynamic -> fixDisplay(paramItemStackData, paramDynamic, paramInt));
/*     */     
/* 112 */     fixAdventureModeChecks(paramItemStackData, paramDynamic, i);
/* 113 */     fixAttributeModifiers(paramItemStackData, paramDynamic, i);
/*     */     
/* 115 */     Optional<Dynamic> optional = paramItemStackData.removeTag("Trim").result();
/* 116 */     if (optional.isPresent()) {
/* 117 */       Dynamic<?> dynamic = optional.get();
/* 118 */       if ((i & 0x80) != 0) {
/* 119 */         dynamic = dynamic.set("show_in_tooltip", dynamic.createBoolean(false));
/*     */       }
/* 121 */       paramItemStackData.setComponent("minecraft:trim", dynamic);
/*     */     } 
/*     */     
/* 124 */     if ((i & 0x20) != 0) {
/* 125 */       paramItemStackData.setComponent("minecraft:hide_additional_tooltip", paramDynamic.emptyMap());
/*     */     }
/*     */     
/* 128 */     if (paramItemStackData.is("minecraft:crossbow")) {
/*     */       
/* 130 */       paramItemStackData.removeTag("Charged");
/* 131 */       paramItemStackData.moveTagToComponent("ChargedProjectiles", "minecraft:charged_projectiles", paramDynamic.createList(Stream.empty()));
/*     */     } 
/* 133 */     if (paramItemStackData.is("minecraft:bundle")) {
/* 134 */       paramItemStackData.moveTagToComponent("Items", "minecraft:bundle_contents", paramDynamic.createList(Stream.empty()));
/*     */     }
/* 136 */     if (paramItemStackData.is("minecraft:filled_map")) {
/* 137 */       paramItemStackData.moveTagToComponent("map", "minecraft:map_id");
/*     */ 
/*     */       
/* 140 */       Map map = (Map)paramItemStackData.removeTag("Decorations").asStream().map(ItemStackComponentizationFix::fixMapDecoration).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (paramDynamic1, paramDynamic2) -> paramDynamic1));
/*     */ 
/*     */ 
/*     */       
/* 144 */       if (!map.isEmpty()) {
/* 145 */         paramItemStackData.setComponent("minecraft:map_decorations", paramDynamic.createMap(map));
/*     */       }
/*     */     } 
/* 148 */     if (paramItemStackData.is(POTION_HOLDER_IDS)) {
/* 149 */       fixPotionContents(paramItemStackData, paramDynamic);
/*     */     }
/* 151 */     if (paramItemStackData.is("minecraft:writable_book")) {
/* 152 */       fixWritableBook(paramItemStackData, paramDynamic);
/*     */     }
/* 154 */     if (paramItemStackData.is("minecraft:written_book")) {
/* 155 */       fixWrittenBook(paramItemStackData, paramDynamic);
/*     */     }
/* 157 */     if (paramItemStackData.is("minecraft:suspicious_stew")) {
/* 158 */       paramItemStackData.moveTagToComponent("effects", "minecraft:suspicious_stew_effects");
/*     */     }
/* 160 */     if (paramItemStackData.is("minecraft:debug_stick")) {
/* 161 */       paramItemStackData.moveTagToComponent("DebugProperty", "minecraft:debug_stick_state");
/*     */     }
/* 163 */     if (paramItemStackData.is(BUCKETED_MOB_IDS)) {
/* 164 */       fixBucketedMobData(paramItemStackData, paramDynamic);
/*     */     }
/* 166 */     if (paramItemStackData.is("minecraft:goat_horn")) {
/* 167 */       paramItemStackData.moveTagToComponent("instrument", "minecraft:instrument");
/*     */     }
/* 169 */     if (paramItemStackData.is("minecraft:knowledge_book")) {
/* 170 */       paramItemStackData.moveTagToComponent("Recipes", "minecraft:recipes");
/*     */     }
/* 172 */     if (paramItemStackData.is("minecraft:compass")) {
/* 173 */       fixLodestoneTracker(paramItemStackData, paramDynamic);
/*     */     }
/* 175 */     if (paramItemStackData.is("minecraft:firework_rocket")) {
/* 176 */       fixFireworkRocket(paramItemStackData);
/*     */     }
/* 178 */     if (paramItemStackData.is("minecraft:firework_star")) {
/* 179 */       fixFireworkStar(paramItemStackData);
/*     */     }
/* 181 */     if (paramItemStackData.is("minecraft:player_head")) {
/* 182 */       paramItemStackData.removeTag("SkullOwner").result().ifPresent(paramDynamic -> paramItemStackData.setComponent("minecraft:profile", fixProfile(paramDynamic)));
/*     */     }
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
/*     */   private static Dynamic<?> fixBlockStateTag(Dynamic<?> paramDynamic) {
/* 206 */     Objects.requireNonNull(paramDynamic); return (Dynamic)DataFixUtils.orElse(paramDynamic.asMapOpt().result().map(paramStream -> (Map)paramStream.collect(Collectors.toMap(Pair::getFirst, ()))).map(paramDynamic::createMap), paramDynamic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixDisplay(ItemStackData paramItemStackData, Dynamic<?> paramDynamic, int paramInt) {
/* 213 */     paramDynamic.get("Name").result().filter(LegacyComponentDataFixUtils::isStrictlyValidJson).ifPresent(paramDynamic -> paramItemStackData.setComponent("minecraft:custom_name", paramDynamic));
/*     */ 
/*     */ 
/*     */     
/* 217 */     OptionalDynamic optionalDynamic = paramDynamic.get("Lore");
/* 218 */     if (optionalDynamic.result().isPresent()) {
/* 219 */       paramItemStackData.setComponent("minecraft:lore", paramDynamic.createList(paramDynamic.get("Lore").asStream()
/* 220 */             .filter(LegacyComponentDataFixUtils::isStrictlyValidJson)));
/*     */     }
/*     */     
/* 223 */     Optional<Integer> optional = paramDynamic.get("color").asNumber().result().map(Number::intValue);
/* 224 */     boolean bool = ((paramInt & 0x40) != 0) ? true : false;
/* 225 */     if (optional.isPresent() || bool) {
/*     */       
/* 227 */       Dynamic<?> dynamic = paramDynamic.emptyMap().set("rgb", paramDynamic.createInt(((Integer)optional.orElse(Integer.valueOf(10511680))).intValue()));
/* 228 */       if (bool) {
/* 229 */         dynamic = dynamic.set("show_in_tooltip", paramDynamic.createBoolean(false));
/*     */       }
/* 231 */       paramItemStackData.setComponent("minecraft:dyed_color", dynamic);
/*     */     } 
/*     */     
/* 234 */     Optional<String> optional1 = paramDynamic.get("LocName").asString().result();
/* 235 */     if (optional1.isPresent()) {
/* 236 */       paramItemStackData.setComponent("minecraft:item_name", LegacyComponentDataFixUtils.createTranslatableComponent(paramDynamic.getOps(), optional1.get()));
/*     */     }
/*     */     
/* 239 */     if (paramItemStackData.is("minecraft:filled_map")) {
/* 240 */       paramItemStackData.setComponent("minecraft:map_color", paramDynamic.get("MapColor"));
/* 241 */       paramDynamic = paramDynamic.remove("MapColor");
/*     */     } 
/*     */     
/* 244 */     return paramDynamic.remove("Name").remove("Lore").remove("color").remove("LocName");
/*     */   } private static <T> Dynamic<T> fixBlockEntityTag(ItemStackData paramItemStackData, Dynamic<T> paramDynamic, String paramString) {
/*     */     Optional<Dynamic> optional2;
/*     */     List list;
/* 248 */     paramItemStackData.setComponent("minecraft:lock", paramDynamic.get("Lock"));
/* 249 */     paramDynamic = paramDynamic.remove("Lock");
/*     */     
/* 251 */     Optional<Dynamic> optional1 = paramDynamic.get("LootTable").result();
/* 252 */     if (optional1.isPresent()) {
/* 253 */       Dynamic<?> dynamic = paramDynamic.emptyMap().set("loot_table", optional1.get());
/* 254 */       long l = paramDynamic.get("LootTableSeed").asLong(0L);
/* 255 */       if (l != 0L) {
/* 256 */         dynamic = dynamic.set("seed", paramDynamic.createLong(l));
/*     */       }
/* 258 */       paramItemStackData.setComponent("minecraft:container_loot", dynamic);
/* 259 */       paramDynamic = paramDynamic.remove("LootTable").remove("LootTableSeed");
/*     */     } 
/*     */     
/* 262 */     switch (paramString) {
/*     */       case "minecraft:skull":
/* 264 */         paramItemStackData.setComponent("minecraft:note_block_sound", paramDynamic.get("note_block_sound"));
/*     */ 
/*     */       
/*     */       case "minecraft:decorated_pot":
/* 268 */         paramItemStackData.setComponent("minecraft:pot_decorations", paramDynamic.get("sherds"));
/* 269 */         optional2 = paramDynamic.get("item").result();
/* 270 */         if (optional2.isPresent()) {
/* 271 */           paramItemStackData.setComponent("minecraft:container", paramDynamic.createList(Stream.of(paramDynamic
/* 272 */                   .emptyMap()
/* 273 */                   .set("slot", paramDynamic.createInt(0))
/* 274 */                   .set("item", optional2.get()))));
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       case "minecraft:banner":
/* 280 */         paramItemStackData.setComponent("minecraft:banner_patterns", paramDynamic.get("patterns"));
/* 281 */         optional2 = paramDynamic.get("Base").asNumber().result();
/* 282 */         if (optional2.isPresent()) {
/* 283 */           paramItemStackData.setComponent("minecraft:base_color", paramDynamic.createString(ExtraDataFixUtils.dyeColorIdToName(((Number)optional2.get()).intValue())));
/*     */         }
/*     */ 
/*     */       
/*     */       case "minecraft:shulker_box":
/*     */       case "minecraft:chest":
/*     */       case "minecraft:trapped_chest":
/*     */       case "minecraft:furnace":
/*     */       case "minecraft:ender_chest":
/*     */       case "minecraft:dispenser":
/*     */       case "minecraft:dropper":
/*     */       case "minecraft:brewing_stand":
/*     */       case "minecraft:hopper":
/*     */       case "minecraft:barrel":
/*     */       case "minecraft:smoker":
/*     */       case "minecraft:blast_furnace":
/*     */       case "minecraft:campfire":
/*     */       case "minecraft:chiseled_bookshelf":
/*     */       case "minecraft:crafter":
/* 302 */         list = paramDynamic.get("Items").asList(paramDynamic -> paramDynamic.emptyMap().set("slot", paramDynamic.createInt(paramDynamic.get("Slot").asByte((byte)0) & 0xFF)).set("item", paramDynamic.remove("Slot")));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 307 */         if (!list.isEmpty()) {
/* 308 */           paramItemStackData.setComponent("minecraft:container", paramDynamic.createList(list.stream()));
/*     */         }
/*     */ 
/*     */       
/*     */       case "minecraft:beehive":
/* 313 */         paramItemStackData.setComponent("minecraft:bees", paramDynamic.get("bees"));
/*     */     } 
/*     */     
/* 316 */     return paramDynamic;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixEnchantments(ItemStackData paramItemStackData, Dynamic<?> paramDynamic, String paramString1, String paramString2, boolean paramBoolean) {
/* 321 */     OptionalDynamic<?> optionalDynamic = paramItemStackData.removeTag(paramString1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     List list = optionalDynamic.asList(Function.identity()).stream().flatMap(paramDynamic -> parseEnchantment(paramDynamic).stream()).filter(paramPair -> (((Integer)paramPair.getSecond()).intValue() > 0)).toList();
/*     */     
/* 328 */     if (!list.isEmpty() || paramBoolean) {
/* 329 */       Dynamic<?> dynamic = paramDynamic.emptyMap();
/*     */       
/* 331 */       Dynamic dynamic1 = paramDynamic.emptyMap();
/* 332 */       for (Pair pair : list) {
/* 333 */         dynamic1 = dynamic1.set((String)pair.getFirst(), paramDynamic.createInt(((Integer)pair.getSecond()).intValue()));
/*     */       }
/* 335 */       dynamic = dynamic.set("levels", dynamic1);
/*     */       
/* 337 */       if (paramBoolean) {
/* 338 */         dynamic = dynamic.set("show_in_tooltip", paramDynamic.createBoolean(false));
/*     */       }
/* 340 */       paramItemStackData.setComponent(paramString2, dynamic);
/*     */     } 
/*     */ 
/*     */     
/* 344 */     if (optionalDynamic.result().isPresent() && list.isEmpty()) {
/* 345 */       paramItemStackData.setComponent("minecraft:enchantment_glint_override", paramDynamic.createBoolean(true));
/*     */     }
/*     */   }
/*     */   
/*     */   private static Optional<Pair<String, Integer>> parseEnchantment(Dynamic<?> paramDynamic) {
/* 350 */     return paramDynamic.get("id").asString().apply2stable((paramString, paramNumber) -> Pair.of(paramString, Integer.valueOf(Mth.clamp(paramNumber.intValue(), 0, 255))), paramDynamic
/*     */         
/* 352 */         .get("lvl").asNumber())
/* 353 */       .result();
/*     */   }
/*     */   
/*     */   private static void fixAdventureModeChecks(ItemStackData paramItemStackData, Dynamic<?> paramDynamic, int paramInt) {
/* 357 */     fixBlockStatePredicates(paramItemStackData, paramDynamic, "CanDestroy", "minecraft:can_break", ((paramInt & 0x8) != 0));
/* 358 */     fixBlockStatePredicates(paramItemStackData, paramDynamic, "CanPlaceOn", "minecraft:can_place_on", ((paramInt & 0x10) != 0));
/*     */   }
/*     */   
/*     */   private static void fixBlockStatePredicates(ItemStackData paramItemStackData, Dynamic<?> paramDynamic, String paramString1, String paramString2, boolean paramBoolean) {
/* 362 */     Optional<Dynamic> optional = paramItemStackData.removeTag(paramString1).result();
/* 363 */     if (optional.isEmpty()) {
/*     */       return;
/*     */     }
/* 366 */     Dynamic<?> dynamic = paramDynamic.emptyMap().set("predicates", paramDynamic.createList(((Dynamic)optional.get()).asStream().map(paramDynamic -> (Dynamic)DataFixUtils.orElse(paramDynamic.asString().map(()).result(), paramDynamic))));
/*     */ 
/*     */ 
/*     */     
/* 370 */     if (paramBoolean) {
/* 371 */       dynamic = dynamic.set("show_in_tooltip", paramDynamic.createBoolean(false));
/*     */     }
/* 373 */     paramItemStackData.setComponent(paramString2, dynamic);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixBlockStatePredicate(Dynamic<?> paramDynamic, String paramString) {
/* 377 */     int i = paramString.indexOf('[');
/* 378 */     int j = paramString.indexOf('{');
/*     */     
/* 380 */     int k = paramString.length();
/* 381 */     if (i != -1) {
/* 382 */       k = i;
/*     */     }
/* 384 */     if (j != -1) {
/* 385 */       k = Math.min(k, j);
/*     */     }
/*     */     
/* 388 */     String str = paramString.substring(0, k);
/*     */     
/* 390 */     Dynamic<?> dynamic = paramDynamic.emptyMap().set("blocks", paramDynamic.createString(str.trim()));
/*     */     
/* 392 */     int m = paramString.indexOf(']');
/* 393 */     if (i != -1 && m != -1) {
/* 394 */       Dynamic dynamic1 = paramDynamic.emptyMap();
/* 395 */       Iterable iterable = PROPERTY_SPLITTER.split(paramString.substring(i + 1, m));
/* 396 */       for (String str1 : iterable) {
/* 397 */         int i1 = str1.indexOf('=');
/* 398 */         if (i1 == -1) {
/*     */           continue;
/*     */         }
/* 401 */         String str2 = str1.substring(0, i1).trim();
/* 402 */         String str3 = str1.substring(i1 + 1).trim();
/* 403 */         dynamic1 = dynamic1.set(str2, paramDynamic.createString(str3));
/*     */       } 
/* 405 */       dynamic = dynamic.set("state", dynamic1);
/*     */     } 
/*     */     
/* 408 */     int n = paramString.indexOf('}');
/* 409 */     if (j != -1 && n != -1) {
/* 410 */       dynamic = dynamic.set("nbt", paramDynamic.createString(paramString.substring(j, n + 1)));
/*     */     }
/*     */     
/* 413 */     return dynamic;
/*     */   }
/*     */   
/*     */   private static void fixAttributeModifiers(ItemStackData paramItemStackData, Dynamic<?> paramDynamic, int paramInt) {
/* 417 */     OptionalDynamic<?> optionalDynamic = paramItemStackData.removeTag("AttributeModifiers");
/* 418 */     if (optionalDynamic.result().isEmpty()) {
/*     */       return;
/*     */     }
/* 421 */     boolean bool = ((paramInt & 0x2) != 0) ? true : false;
/* 422 */     List list = optionalDynamic.asList(ItemStackComponentizationFix::fixAttributeModifier);
/*     */     
/* 424 */     Dynamic<?> dynamic = paramDynamic.emptyMap().set("modifiers", paramDynamic.createList(list.stream()));
/* 425 */     if (bool) {
/* 426 */       dynamic = dynamic.set("show_in_tooltip", paramDynamic.createBoolean(false));
/*     */     }
/* 428 */     paramItemStackData.setComponent("minecraft:attribute_modifiers", dynamic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixAttributeModifier(Dynamic<?> paramDynamic) {
/* 436 */     Dynamic<?> dynamic = paramDynamic.emptyMap().set("name", paramDynamic.createString("")).set("amount", paramDynamic.createDouble(0.0D)).set("operation", paramDynamic.createString("add_value"));
/* 437 */     dynamic = Dynamic.copyField(paramDynamic, "AttributeName", dynamic, "type");
/* 438 */     dynamic = Dynamic.copyField(paramDynamic, "Slot", dynamic, "slot");
/* 439 */     dynamic = Dynamic.copyField(paramDynamic, "UUID", dynamic, "uuid");
/* 440 */     dynamic = Dynamic.copyField(paramDynamic, "Name", dynamic, "name");
/* 441 */     dynamic = Dynamic.copyField(paramDynamic, "Amount", dynamic, "amount");
/* 442 */     dynamic = Dynamic.copyAndFixField(paramDynamic, "Operation", dynamic, "operation", paramDynamic -> { switch (paramDynamic.asInt(0)) { default:
/*     */             
/*     */             case 1:
/*     */             
/*     */             case 2:
/*     */               break; }
/*     */            return paramDynamic.createString("add_multiplied_total");
/* 449 */         }); return dynamic;
/*     */   }
/*     */   
/*     */   private static Pair<Dynamic<?>, Dynamic<?>> fixMapDecoration(Dynamic<?> paramDynamic) {
/* 453 */     Dynamic dynamic1 = (Dynamic)DataFixUtils.orElseGet(paramDynamic.get("id").result(), () -> paramDynamic.createString(""));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     Dynamic dynamic2 = paramDynamic.emptyMap().set("type", paramDynamic.createString(fixMapDecorationType(paramDynamic.get("type").asInt(0)))).set("x", paramDynamic.createDouble(paramDynamic.get("x").asDouble(0.0D))).set("z", paramDynamic.createDouble(paramDynamic.get("z").asDouble(0.0D))).set("rotation", paramDynamic.createFloat((float)paramDynamic.get("rot").asDouble(0.0D)));
/* 459 */     return Pair.of(dynamic1, dynamic2);
/*     */   }
/*     */   
/*     */   private static String fixMapDecorationType(int paramInt) {
/* 463 */     switch (paramInt) { default: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 20: case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 497 */       "swamp_hut";
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixPotionContents(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 502 */     Dynamic<?> dynamic = paramDynamic.emptyMap();
/* 503 */     Optional<String> optional = paramItemStackData.removeTag("Potion").asString().result().filter(paramString -> !paramString.equals("minecraft:empty"));
/* 504 */     if (optional.isPresent()) {
/* 505 */       dynamic = dynamic.set("potion", paramDynamic.createString(optional.get()));
/*     */     }
/* 507 */     dynamic = paramItemStackData.moveTagInto("CustomPotionColor", dynamic, "custom_color");
/* 508 */     dynamic = paramItemStackData.moveTagInto("custom_potion_effects", dynamic, "custom_effects");
/* 509 */     if (!dynamic.equals(paramDynamic.emptyMap())) {
/* 510 */       paramItemStackData.setComponent("minecraft:potion_contents", dynamic);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixWritableBook(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 515 */     Dynamic<?> dynamic = fixBookPages(paramItemStackData, paramDynamic);
/* 516 */     if (dynamic != null) {
/* 517 */       paramItemStackData.setComponent("minecraft:writable_book_content", paramDynamic.emptyMap().set("pages", dynamic));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixWrittenBook(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 522 */     Dynamic<?> dynamic1 = fixBookPages(paramItemStackData, paramDynamic);
/* 523 */     String str = paramItemStackData.removeTag("title").asString("");
/* 524 */     Optional<String> optional = paramItemStackData.removeTag("filtered_title").asString().result();
/* 525 */     Dynamic<?> dynamic2 = paramDynamic.emptyMap();
/* 526 */     dynamic2 = dynamic2.set("title", createFilteredText(paramDynamic, str, optional));
/* 527 */     dynamic2 = paramItemStackData.moveTagInto("author", dynamic2, "author");
/* 528 */     dynamic2 = paramItemStackData.moveTagInto("resolved", dynamic2, "resolved");
/* 529 */     dynamic2 = paramItemStackData.moveTagInto("generation", dynamic2, "generation");
/* 530 */     if (dynamic1 != null) {
/* 531 */       dynamic2 = dynamic2.set("pages", dynamic1);
/*     */     }
/* 533 */     paramItemStackData.setComponent("minecraft:written_book_content", dynamic2);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixBookPages(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 537 */     List<String> list = paramItemStackData.removeTag("pages").asList(paramDynamic -> paramDynamic.asString(""));
/* 538 */     Map map = paramItemStackData.removeTag("filtered_pages").asMap(paramDynamic -> paramDynamic.asString("0"), paramDynamic -> paramDynamic.asString(""));
/* 539 */     if (list.isEmpty()) {
/* 540 */       return null;
/*     */     }
/* 542 */     ArrayList<Dynamic<?>> arrayList = new ArrayList(list.size());
/* 543 */     for (byte b = 0; b < list.size(); b++) {
/* 544 */       String str1 = list.get(b);
/* 545 */       String str2 = (String)map.get(String.valueOf(b));
/* 546 */       arrayList.add(createFilteredText(paramDynamic, str1, Optional.ofNullable(str2)));
/*     */     } 
/* 548 */     return paramDynamic.createList(arrayList.stream());
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> createFilteredText(Dynamic<?> paramDynamic, String paramString, Optional<String> paramOptional) {
/* 553 */     Dynamic<?> dynamic = paramDynamic.emptyMap().set("raw", paramDynamic.createString(paramString));
/* 554 */     if (paramOptional.isPresent()) {
/* 555 */       dynamic = dynamic.set("filtered", paramDynamic.createString(paramOptional.get()));
/*     */     }
/* 557 */     return dynamic;
/*     */   }
/*     */   
/*     */   private static void fixBucketedMobData(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 561 */     Dynamic<?> dynamic = paramDynamic.emptyMap();
/* 562 */     for (String str : BUCKETED_MOB_TAGS) {
/* 563 */       dynamic = paramItemStackData.moveTagInto(str, dynamic, str);
/*     */     }
/*     */     
/* 566 */     if (!dynamic.equals(paramDynamic.emptyMap())) {
/* 567 */       paramItemStackData.setComponent("minecraft:bucket_entity_data", dynamic);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixLodestoneTracker(ItemStackData paramItemStackData, Dynamic<?> paramDynamic) {
/* 572 */     Optional<Dynamic> optional1 = paramItemStackData.removeTag("LodestonePos").result();
/* 573 */     Optional<Dynamic> optional2 = paramItemStackData.removeTag("LodestoneDimension").result();
/* 574 */     if (optional1.isEmpty() && optional2.isEmpty()) {
/*     */       return;
/*     */     }
/* 577 */     boolean bool = paramItemStackData.removeTag("LodestoneTracked").asBoolean(true);
/* 578 */     Dynamic<?> dynamic = paramDynamic.emptyMap();
/* 579 */     if (optional1.isPresent() && optional2.isPresent()) {
/* 580 */       dynamic = dynamic.set("target", paramDynamic.emptyMap()
/* 581 */           .set("pos", optional1.get())
/* 582 */           .set("dimension", optional2.get()));
/*     */     }
/*     */     
/* 585 */     if (!bool) {
/* 586 */       dynamic = dynamic.set("tracked", paramDynamic.createBoolean(false));
/*     */     }
/* 588 */     paramItemStackData.setComponent("minecraft:lodestone_tracker", dynamic);
/*     */   }
/*     */   
/*     */   private static void fixFireworkStar(ItemStackData paramItemStackData) {
/* 592 */     paramItemStackData.fixSubTag("Explosion", true, paramDynamic -> {
/*     */           paramItemStackData.setComponent("minecraft:firework_explosion", fixFireworkExplosion(paramDynamic));
/*     */           return paramDynamic.remove("Type").remove("Colors").remove("FadeColors").remove("Trail").remove("Flicker");
/*     */         });
/*     */   }
/*     */   
/*     */   private static void fixFireworkRocket(ItemStackData paramItemStackData) {
/* 599 */     paramItemStackData.fixSubTag("Fireworks", true, paramDynamic -> {
/*     */           Stream stream = paramDynamic.get("Explosions").asStream().map(ItemStackComponentizationFix::fixFireworkExplosion);
/*     */           int i = paramDynamic.get("Flight").asInt(0);
/*     */           paramItemStackData.setComponent("minecraft:fireworks", paramDynamic.emptyMap().set("explosions", paramDynamic.createList(stream)).set("flight_duration", paramDynamic.createByte((byte)i)));
/*     */           return paramDynamic.remove("Explosions").remove("Flight");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixFireworkExplosion(Dynamic<?> paramDynamic) {
/* 611 */     switch (paramDynamic.get("Type").asInt(0)) { default: 
/*     */       case 1: 
/*     */       case 2: 
/*     */       case 3:
/*     */       
/*     */       case 4:
/* 617 */         break; }  paramDynamic = paramDynamic.set("shape", paramDynamic.createString("burst")).remove("Type");
/* 618 */     paramDynamic = paramDynamic.renameField("Colors", "colors");
/* 619 */     paramDynamic = paramDynamic.renameField("FadeColors", "fade_colors");
/* 620 */     paramDynamic = paramDynamic.renameField("Trail", "has_trail");
/* 621 */     paramDynamic = paramDynamic.renameField("Flicker", "has_twinkle");
/* 622 */     return paramDynamic;
/*     */   }
/*     */   
/*     */   public static Dynamic<?> fixProfile(Dynamic<?> paramDynamic) {
/* 626 */     Optional<String> optional = paramDynamic.asString().result();
/* 627 */     if (optional.isPresent()) {
/* 628 */       if (isValidPlayerName(optional.get())) {
/* 629 */         return paramDynamic.emptyMap().set("name", paramDynamic.createString(optional.get()));
/*     */       }
/* 631 */       return paramDynamic.emptyMap();
/*     */     } 
/*     */ 
/*     */     
/* 635 */     String str = paramDynamic.get("Name").asString("");
/* 636 */     Optional<Dynamic> optional1 = paramDynamic.get("Id").result();
/* 637 */     Dynamic<?> dynamic1 = fixProfileProperties(paramDynamic.get("Properties"));
/*     */     
/* 639 */     Dynamic<?> dynamic2 = paramDynamic.emptyMap();
/* 640 */     if (isValidPlayerName(str)) {
/* 641 */       dynamic2 = dynamic2.set("name", paramDynamic.createString(str));
/*     */     }
/* 643 */     if (optional1.isPresent()) {
/* 644 */       dynamic2 = dynamic2.set("id", optional1.get());
/*     */     }
/* 646 */     if (dynamic1 != null) {
/* 647 */       dynamic2 = dynamic2.set("properties", dynamic1);
/*     */     }
/*     */     
/* 650 */     return dynamic2;
/*     */   }
/*     */   
/*     */   private static boolean isValidPlayerName(String paramString) {
/* 654 */     if (paramString.length() > 16) {
/* 655 */       return false;
/*     */     }
/* 657 */     return paramString.chars().filter(paramInt -> (paramInt <= 32 || paramInt >= 127)).findAny().isEmpty();
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixProfileProperties(OptionalDynamic<?> paramOptionalDynamic) {
/* 661 */     Map map = paramOptionalDynamic.asMap(paramDynamic -> paramDynamic.asString(""), paramDynamic -> paramDynamic.asList(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 669 */     if (map.isEmpty()) {
/* 670 */       return null;
/*     */     }
/* 672 */     return paramOptionalDynamic.createList(map.entrySet().stream()
/* 673 */         .flatMap(paramEntry -> ((List)paramEntry.getValue()).stream().map(())));
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
/*     */   protected TypeRewriteRule makeRule() {
/* 689 */     return writeFixAndRead("ItemStack componentization", getInputSchema().getType(References.ITEM_STACK), getOutputSchema().getType(References.ITEM_STACK), paramDynamic -> {
/*     */           Optional<?> optional = ItemStackData.read(paramDynamic).map(());
/*     */           return (Dynamic)DataFixUtils.orElse(optional, paramDynamic);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ItemStackData
/*     */   {
/*     */     private final String item;
/*     */     
/*     */     private final int count;
/*     */     
/*     */     private Dynamic<?> components;
/*     */     private final Dynamic<?> remainder;
/*     */     Dynamic<?> tag;
/*     */     
/*     */     private ItemStackData(String param1String, int param1Int, Dynamic<?> param1Dynamic) {
/* 707 */       this.item = NamespacedSchema.ensureNamespaced(param1String);
/* 708 */       this.count = param1Int;
/* 709 */       this.components = param1Dynamic.emptyMap();
/* 710 */       this.tag = param1Dynamic.get("tag").orElseEmptyMap();
/*     */       
/* 712 */       this.remainder = param1Dynamic.remove("tag");
/*     */     }
/*     */     
/*     */     public static Optional<ItemStackData> read(Dynamic<?> param1Dynamic) {
/* 716 */       return param1Dynamic.get("id").asString().apply2stable((param1String, param1Number) -> new ItemStackData(param1String, param1Number.intValue(), param1Dynamic.remove("id").remove("Count")), param1Dynamic
/*     */           
/* 718 */           .get("Count").asNumber())
/* 719 */         .result();
/*     */     }
/*     */     
/*     */     public OptionalDynamic<?> removeTag(String param1String) {
/* 723 */       OptionalDynamic<?> optionalDynamic = this.tag.get(param1String);
/* 724 */       this.tag = this.tag.remove(param1String);
/* 725 */       return optionalDynamic;
/*     */     }
/*     */     
/*     */     public void setComponent(String param1String, Dynamic<?> param1Dynamic) {
/* 729 */       this.components = this.components.set(param1String, param1Dynamic);
/*     */     }
/*     */     
/*     */     public void setComponent(String param1String, OptionalDynamic<?> param1OptionalDynamic) {
/* 733 */       param1OptionalDynamic.result().ifPresent(param1Dynamic -> this.components = this.components.set(param1String, param1Dynamic));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Dynamic<?> moveTagInto(String param1String1, Dynamic<?> param1Dynamic, String param1String2) {
/* 739 */       Optional<Dynamic> optional = removeTag(param1String1).result();
/* 740 */       if (optional.isPresent()) {
/* 741 */         return param1Dynamic.set(param1String2, optional.get());
/*     */       }
/* 743 */       return param1Dynamic;
/*     */     }
/*     */     
/*     */     public void moveTagToComponent(String param1String1, String param1String2, Dynamic<?> param1Dynamic) {
/* 747 */       Optional<Dynamic> optional = removeTag(param1String1).result();
/* 748 */       if (optional.isPresent() && !((Dynamic)optional.get()).equals(param1Dynamic)) {
/* 749 */         setComponent(param1String2, optional.get());
/*     */       }
/*     */     }
/*     */     
/*     */     public void moveTagToComponent(String param1String1, String param1String2) {
/* 754 */       removeTag(param1String1).result().ifPresent(param1Dynamic -> setComponent(param1String, param1Dynamic));
/*     */     }
/*     */     
/*     */     public void fixSubTag(String param1String, boolean param1Boolean, UnaryOperator<Dynamic<?>> param1UnaryOperator) {
/* 758 */       OptionalDynamic optionalDynamic = this.tag.get(param1String);
/* 759 */       if (param1Boolean && optionalDynamic.result().isEmpty()) {
/*     */         return;
/*     */       }
/* 762 */       Dynamic<?> dynamic = optionalDynamic.orElseEmptyMap();
/* 763 */       dynamic = param1UnaryOperator.apply(dynamic);
/* 764 */       if (dynamic.equals(dynamic.emptyMap())) {
/* 765 */         this.tag = this.tag.remove(param1String);
/*     */       } else {
/* 767 */         this.tag = this.tag.set(param1String, dynamic);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Dynamic<?> write() {
/* 774 */       Dynamic<?> dynamic = this.tag.emptyMap().set("id", this.tag.createString(this.item)).set("count", this.tag.createInt(this.count));
/* 775 */       if (!this.tag.equals(this.tag.emptyMap())) {
/* 776 */         this.components = this.components.set("minecraft:custom_data", this.tag);
/*     */       }
/* 778 */       if (!this.components.equals(this.tag.emptyMap())) {
/* 779 */         dynamic = dynamic.set("components", this.components);
/*     */       }
/* 781 */       return mergeRemainder(dynamic, this.remainder);
/*     */     }
/*     */     
/*     */     private static <T> Dynamic<T> mergeRemainder(Dynamic<T> param1Dynamic, Dynamic<?> param1Dynamic1) {
/* 785 */       DynamicOps dynamicOps = param1Dynamic.getOps();
/* 786 */       return dynamicOps.getMap(param1Dynamic.getValue())
/* 787 */         .flatMap(param1MapLike -> param1DynamicOps.mergeToMap(param1Dynamic.convert(param1DynamicOps).getValue(), param1MapLike))
/* 788 */         .map(param1Object -> new Dynamic(param1DynamicOps, param1Object))
/* 789 */         .result().orElse(param1Dynamic);
/*     */     }
/*     */     
/*     */     public boolean is(String param1String) {
/* 793 */       return this.item.equals(param1String);
/*     */     }
/*     */     
/*     */     public boolean is(Set<String> param1Set) {
/* 797 */       return param1Set.contains(this.item);
/*     */     }
/*     */     
/*     */     public boolean hasComponent(String param1String) {
/* 801 */       return this.components.get(param1String).result().isPresent();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackComponentizationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */