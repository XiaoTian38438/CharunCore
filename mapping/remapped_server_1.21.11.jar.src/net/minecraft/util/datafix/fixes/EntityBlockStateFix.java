/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityBlockStateFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final Map<String, Integer> MAP;
/*     */   
/*     */   public EntityBlockStateFix(Schema paramSchema, boolean paramBoolean) {
/*  34 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */   static {
/*  37 */     MAP = (Map<String, Integer>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("minecraft:air", Integer.valueOf(0));
/*     */           paramHashMap.put("minecraft:stone", Integer.valueOf(1));
/*     */           paramHashMap.put("minecraft:grass", Integer.valueOf(2));
/*     */           paramHashMap.put("minecraft:dirt", Integer.valueOf(3));
/*     */           paramHashMap.put("minecraft:cobblestone", Integer.valueOf(4));
/*     */           paramHashMap.put("minecraft:planks", Integer.valueOf(5));
/*     */           paramHashMap.put("minecraft:sapling", Integer.valueOf(6));
/*     */           paramHashMap.put("minecraft:bedrock", Integer.valueOf(7));
/*     */           paramHashMap.put("minecraft:flowing_water", Integer.valueOf(8));
/*     */           paramHashMap.put("minecraft:water", Integer.valueOf(9));
/*     */           paramHashMap.put("minecraft:flowing_lava", Integer.valueOf(10));
/*     */           paramHashMap.put("minecraft:lava", Integer.valueOf(11));
/*     */           paramHashMap.put("minecraft:sand", Integer.valueOf(12));
/*     */           paramHashMap.put("minecraft:gravel", Integer.valueOf(13));
/*     */           paramHashMap.put("minecraft:gold_ore", Integer.valueOf(14));
/*     */           paramHashMap.put("minecraft:iron_ore", Integer.valueOf(15));
/*     */           paramHashMap.put("minecraft:coal_ore", Integer.valueOf(16));
/*     */           paramHashMap.put("minecraft:log", Integer.valueOf(17));
/*     */           paramHashMap.put("minecraft:leaves", Integer.valueOf(18));
/*     */           paramHashMap.put("minecraft:sponge", Integer.valueOf(19));
/*     */           paramHashMap.put("minecraft:glass", Integer.valueOf(20));
/*     */           paramHashMap.put("minecraft:lapis_ore", Integer.valueOf(21));
/*     */           paramHashMap.put("minecraft:lapis_block", Integer.valueOf(22));
/*     */           paramHashMap.put("minecraft:dispenser", Integer.valueOf(23));
/*     */           paramHashMap.put("minecraft:sandstone", Integer.valueOf(24));
/*     */           paramHashMap.put("minecraft:noteblock", Integer.valueOf(25));
/*     */           paramHashMap.put("minecraft:bed", Integer.valueOf(26));
/*     */           paramHashMap.put("minecraft:golden_rail", Integer.valueOf(27));
/*     */           paramHashMap.put("minecraft:detector_rail", Integer.valueOf(28));
/*     */           paramHashMap.put("minecraft:sticky_piston", Integer.valueOf(29));
/*     */           paramHashMap.put("minecraft:web", Integer.valueOf(30));
/*     */           paramHashMap.put("minecraft:tallgrass", Integer.valueOf(31));
/*     */           paramHashMap.put("minecraft:deadbush", Integer.valueOf(32));
/*     */           paramHashMap.put("minecraft:piston", Integer.valueOf(33));
/*     */           paramHashMap.put("minecraft:piston_head", Integer.valueOf(34));
/*     */           paramHashMap.put("minecraft:wool", Integer.valueOf(35));
/*     */           paramHashMap.put("minecraft:piston_extension", Integer.valueOf(36));
/*     */           paramHashMap.put("minecraft:yellow_flower", Integer.valueOf(37));
/*     */           paramHashMap.put("minecraft:red_flower", Integer.valueOf(38));
/*     */           paramHashMap.put("minecraft:brown_mushroom", Integer.valueOf(39));
/*     */           paramHashMap.put("minecraft:red_mushroom", Integer.valueOf(40));
/*     */           paramHashMap.put("minecraft:gold_block", Integer.valueOf(41));
/*     */           paramHashMap.put("minecraft:iron_block", Integer.valueOf(42));
/*     */           paramHashMap.put("minecraft:double_stone_slab", Integer.valueOf(43));
/*     */           paramHashMap.put("minecraft:stone_slab", Integer.valueOf(44));
/*     */           paramHashMap.put("minecraft:brick_block", Integer.valueOf(45));
/*     */           paramHashMap.put("minecraft:tnt", Integer.valueOf(46));
/*     */           paramHashMap.put("minecraft:bookshelf", Integer.valueOf(47));
/*     */           paramHashMap.put("minecraft:mossy_cobblestone", Integer.valueOf(48));
/*     */           paramHashMap.put("minecraft:obsidian", Integer.valueOf(49));
/*     */           paramHashMap.put("minecraft:torch", Integer.valueOf(50));
/*     */           paramHashMap.put("minecraft:fire", Integer.valueOf(51));
/*     */           paramHashMap.put("minecraft:mob_spawner", Integer.valueOf(52));
/*     */           paramHashMap.put("minecraft:oak_stairs", Integer.valueOf(53));
/*     */           paramHashMap.put("minecraft:chest", Integer.valueOf(54));
/*     */           paramHashMap.put("minecraft:redstone_wire", Integer.valueOf(55));
/*     */           paramHashMap.put("minecraft:diamond_ore", Integer.valueOf(56));
/*     */           paramHashMap.put("minecraft:diamond_block", Integer.valueOf(57));
/*     */           paramHashMap.put("minecraft:crafting_table", Integer.valueOf(58));
/*     */           paramHashMap.put("minecraft:wheat", Integer.valueOf(59));
/*     */           paramHashMap.put("minecraft:farmland", Integer.valueOf(60));
/*     */           paramHashMap.put("minecraft:furnace", Integer.valueOf(61));
/*     */           paramHashMap.put("minecraft:lit_furnace", Integer.valueOf(62));
/*     */           paramHashMap.put("minecraft:standing_sign", Integer.valueOf(63));
/*     */           paramHashMap.put("minecraft:wooden_door", Integer.valueOf(64));
/*     */           paramHashMap.put("minecraft:ladder", Integer.valueOf(65));
/*     */           paramHashMap.put("minecraft:rail", Integer.valueOf(66));
/*     */           paramHashMap.put("minecraft:stone_stairs", Integer.valueOf(67));
/*     */           paramHashMap.put("minecraft:wall_sign", Integer.valueOf(68));
/*     */           paramHashMap.put("minecraft:lever", Integer.valueOf(69));
/*     */           paramHashMap.put("minecraft:stone_pressure_plate", Integer.valueOf(70));
/*     */           paramHashMap.put("minecraft:iron_door", Integer.valueOf(71));
/*     */           paramHashMap.put("minecraft:wooden_pressure_plate", Integer.valueOf(72));
/*     */           paramHashMap.put("minecraft:redstone_ore", Integer.valueOf(73));
/*     */           paramHashMap.put("minecraft:lit_redstone_ore", Integer.valueOf(74));
/*     */           paramHashMap.put("minecraft:unlit_redstone_torch", Integer.valueOf(75));
/*     */           paramHashMap.put("minecraft:redstone_torch", Integer.valueOf(76));
/*     */           paramHashMap.put("minecraft:stone_button", Integer.valueOf(77));
/*     */           paramHashMap.put("minecraft:snow_layer", Integer.valueOf(78));
/*     */           paramHashMap.put("minecraft:ice", Integer.valueOf(79));
/*     */           paramHashMap.put("minecraft:snow", Integer.valueOf(80));
/*     */           paramHashMap.put("minecraft:cactus", Integer.valueOf(81));
/*     */           paramHashMap.put("minecraft:clay", Integer.valueOf(82));
/*     */           paramHashMap.put("minecraft:reeds", Integer.valueOf(83));
/*     */           paramHashMap.put("minecraft:jukebox", Integer.valueOf(84));
/*     */           paramHashMap.put("minecraft:fence", Integer.valueOf(85));
/*     */           paramHashMap.put("minecraft:pumpkin", Integer.valueOf(86));
/*     */           paramHashMap.put("minecraft:netherrack", Integer.valueOf(87));
/*     */           paramHashMap.put("minecraft:soul_sand", Integer.valueOf(88));
/*     */           paramHashMap.put("minecraft:glowstone", Integer.valueOf(89));
/*     */           paramHashMap.put("minecraft:portal", Integer.valueOf(90));
/*     */           paramHashMap.put("minecraft:lit_pumpkin", Integer.valueOf(91));
/*     */           paramHashMap.put("minecraft:cake", Integer.valueOf(92));
/*     */           paramHashMap.put("minecraft:unpowered_repeater", Integer.valueOf(93));
/*     */           paramHashMap.put("minecraft:powered_repeater", Integer.valueOf(94));
/*     */           paramHashMap.put("minecraft:stained_glass", Integer.valueOf(95));
/*     */           paramHashMap.put("minecraft:trapdoor", Integer.valueOf(96));
/*     */           paramHashMap.put("minecraft:monster_egg", Integer.valueOf(97));
/*     */           paramHashMap.put("minecraft:stonebrick", Integer.valueOf(98));
/*     */           paramHashMap.put("minecraft:brown_mushroom_block", Integer.valueOf(99));
/*     */           paramHashMap.put("minecraft:red_mushroom_block", Integer.valueOf(100));
/*     */           paramHashMap.put("minecraft:iron_bars", Integer.valueOf(101));
/*     */           paramHashMap.put("minecraft:glass_pane", Integer.valueOf(102));
/*     */           paramHashMap.put("minecraft:melon_block", Integer.valueOf(103));
/*     */           paramHashMap.put("minecraft:pumpkin_stem", Integer.valueOf(104));
/*     */           paramHashMap.put("minecraft:melon_stem", Integer.valueOf(105));
/*     */           paramHashMap.put("minecraft:vine", Integer.valueOf(106));
/*     */           paramHashMap.put("minecraft:fence_gate", Integer.valueOf(107));
/*     */           paramHashMap.put("minecraft:brick_stairs", Integer.valueOf(108));
/*     */           paramHashMap.put("minecraft:stone_brick_stairs", Integer.valueOf(109));
/*     */           paramHashMap.put("minecraft:mycelium", Integer.valueOf(110));
/*     */           paramHashMap.put("minecraft:waterlily", Integer.valueOf(111));
/*     */           paramHashMap.put("minecraft:nether_brick", Integer.valueOf(112));
/*     */           paramHashMap.put("minecraft:nether_brick_fence", Integer.valueOf(113));
/*     */           paramHashMap.put("minecraft:nether_brick_stairs", Integer.valueOf(114));
/*     */           paramHashMap.put("minecraft:nether_wart", Integer.valueOf(115));
/*     */           paramHashMap.put("minecraft:enchanting_table", Integer.valueOf(116));
/*     */           paramHashMap.put("minecraft:brewing_stand", Integer.valueOf(117));
/*     */           paramHashMap.put("minecraft:cauldron", Integer.valueOf(118));
/*     */           paramHashMap.put("minecraft:end_portal", Integer.valueOf(119));
/*     */           paramHashMap.put("minecraft:end_portal_frame", Integer.valueOf(120));
/*     */           paramHashMap.put("minecraft:end_stone", Integer.valueOf(121));
/*     */           paramHashMap.put("minecraft:dragon_egg", Integer.valueOf(122));
/*     */           paramHashMap.put("minecraft:redstone_lamp", Integer.valueOf(123));
/*     */           paramHashMap.put("minecraft:lit_redstone_lamp", Integer.valueOf(124));
/*     */           paramHashMap.put("minecraft:double_wooden_slab", Integer.valueOf(125));
/*     */           paramHashMap.put("minecraft:wooden_slab", Integer.valueOf(126));
/*     */           paramHashMap.put("minecraft:cocoa", Integer.valueOf(127));
/*     */           paramHashMap.put("minecraft:sandstone_stairs", Integer.valueOf(128));
/*     */           paramHashMap.put("minecraft:emerald_ore", Integer.valueOf(129));
/*     */           paramHashMap.put("minecraft:ender_chest", Integer.valueOf(130));
/*     */           paramHashMap.put("minecraft:tripwire_hook", Integer.valueOf(131));
/*     */           paramHashMap.put("minecraft:tripwire", Integer.valueOf(132));
/*     */           paramHashMap.put("minecraft:emerald_block", Integer.valueOf(133));
/*     */           paramHashMap.put("minecraft:spruce_stairs", Integer.valueOf(134));
/*     */           paramHashMap.put("minecraft:birch_stairs", Integer.valueOf(135));
/*     */           paramHashMap.put("minecraft:jungle_stairs", Integer.valueOf(136));
/*     */           paramHashMap.put("minecraft:command_block", Integer.valueOf(137));
/*     */           paramHashMap.put("minecraft:beacon", Integer.valueOf(138));
/*     */           paramHashMap.put("minecraft:cobblestone_wall", Integer.valueOf(139));
/*     */           paramHashMap.put("minecraft:flower_pot", Integer.valueOf(140));
/*     */           paramHashMap.put("minecraft:carrots", Integer.valueOf(141));
/*     */           paramHashMap.put("minecraft:potatoes", Integer.valueOf(142));
/*     */           paramHashMap.put("minecraft:wooden_button", Integer.valueOf(143));
/*     */           paramHashMap.put("minecraft:skull", Integer.valueOf(144));
/*     */           paramHashMap.put("minecraft:anvil", Integer.valueOf(145));
/*     */           paramHashMap.put("minecraft:trapped_chest", Integer.valueOf(146));
/*     */           paramHashMap.put("minecraft:light_weighted_pressure_plate", Integer.valueOf(147));
/*     */           paramHashMap.put("minecraft:heavy_weighted_pressure_plate", Integer.valueOf(148));
/*     */           paramHashMap.put("minecraft:unpowered_comparator", Integer.valueOf(149));
/*     */           paramHashMap.put("minecraft:powered_comparator", Integer.valueOf(150));
/*     */           paramHashMap.put("minecraft:daylight_detector", Integer.valueOf(151));
/*     */           paramHashMap.put("minecraft:redstone_block", Integer.valueOf(152));
/*     */           paramHashMap.put("minecraft:quartz_ore", Integer.valueOf(153));
/*     */           paramHashMap.put("minecraft:hopper", Integer.valueOf(154));
/*     */           paramHashMap.put("minecraft:quartz_block", Integer.valueOf(155));
/*     */           paramHashMap.put("minecraft:quartz_stairs", Integer.valueOf(156));
/*     */           paramHashMap.put("minecraft:activator_rail", Integer.valueOf(157));
/*     */           paramHashMap.put("minecraft:dropper", Integer.valueOf(158));
/*     */           paramHashMap.put("minecraft:stained_hardened_clay", Integer.valueOf(159));
/*     */           paramHashMap.put("minecraft:stained_glass_pane", Integer.valueOf(160));
/*     */           paramHashMap.put("minecraft:leaves2", Integer.valueOf(161));
/*     */           paramHashMap.put("minecraft:log2", Integer.valueOf(162));
/*     */           paramHashMap.put("minecraft:acacia_stairs", Integer.valueOf(163));
/*     */           paramHashMap.put("minecraft:dark_oak_stairs", Integer.valueOf(164));
/*     */           paramHashMap.put("minecraft:slime", Integer.valueOf(165));
/*     */           paramHashMap.put("minecraft:barrier", Integer.valueOf(166));
/*     */           paramHashMap.put("minecraft:iron_trapdoor", Integer.valueOf(167));
/*     */           paramHashMap.put("minecraft:prismarine", Integer.valueOf(168));
/*     */           paramHashMap.put("minecraft:sea_lantern", Integer.valueOf(169));
/*     */           paramHashMap.put("minecraft:hay_block", Integer.valueOf(170));
/*     */           paramHashMap.put("minecraft:carpet", Integer.valueOf(171));
/*     */           paramHashMap.put("minecraft:hardened_clay", Integer.valueOf(172));
/*     */           paramHashMap.put("minecraft:coal_block", Integer.valueOf(173));
/*     */           paramHashMap.put("minecraft:packed_ice", Integer.valueOf(174));
/*     */           paramHashMap.put("minecraft:double_plant", Integer.valueOf(175));
/*     */           paramHashMap.put("minecraft:standing_banner", Integer.valueOf(176));
/*     */           paramHashMap.put("minecraft:wall_banner", Integer.valueOf(177));
/*     */           paramHashMap.put("minecraft:daylight_detector_inverted", Integer.valueOf(178));
/*     */           paramHashMap.put("minecraft:red_sandstone", Integer.valueOf(179));
/*     */           paramHashMap.put("minecraft:red_sandstone_stairs", Integer.valueOf(180));
/*     */           paramHashMap.put("minecraft:double_stone_slab2", Integer.valueOf(181));
/*     */           paramHashMap.put("minecraft:stone_slab2", Integer.valueOf(182));
/*     */           paramHashMap.put("minecraft:spruce_fence_gate", Integer.valueOf(183));
/*     */           paramHashMap.put("minecraft:birch_fence_gate", Integer.valueOf(184));
/*     */           paramHashMap.put("minecraft:jungle_fence_gate", Integer.valueOf(185));
/*     */           paramHashMap.put("minecraft:dark_oak_fence_gate", Integer.valueOf(186));
/*     */           paramHashMap.put("minecraft:acacia_fence_gate", Integer.valueOf(187));
/*     */           paramHashMap.put("minecraft:spruce_fence", Integer.valueOf(188));
/*     */           paramHashMap.put("minecraft:birch_fence", Integer.valueOf(189));
/*     */           paramHashMap.put("minecraft:jungle_fence", Integer.valueOf(190));
/*     */           paramHashMap.put("minecraft:dark_oak_fence", Integer.valueOf(191));
/*     */           paramHashMap.put("minecraft:acacia_fence", Integer.valueOf(192));
/*     */           paramHashMap.put("minecraft:spruce_door", Integer.valueOf(193));
/*     */           paramHashMap.put("minecraft:birch_door", Integer.valueOf(194));
/*     */           paramHashMap.put("minecraft:jungle_door", Integer.valueOf(195));
/*     */           paramHashMap.put("minecraft:acacia_door", Integer.valueOf(196));
/*     */           paramHashMap.put("minecraft:dark_oak_door", Integer.valueOf(197));
/*     */           paramHashMap.put("minecraft:end_rod", Integer.valueOf(198));
/*     */           paramHashMap.put("minecraft:chorus_plant", Integer.valueOf(199));
/*     */           paramHashMap.put("minecraft:chorus_flower", Integer.valueOf(200));
/*     */           paramHashMap.put("minecraft:purpur_block", Integer.valueOf(201));
/*     */           paramHashMap.put("minecraft:purpur_pillar", Integer.valueOf(202));
/*     */           paramHashMap.put("minecraft:purpur_stairs", Integer.valueOf(203));
/*     */           paramHashMap.put("minecraft:purpur_double_slab", Integer.valueOf(204));
/*     */           paramHashMap.put("minecraft:purpur_slab", Integer.valueOf(205));
/*     */           paramHashMap.put("minecraft:end_bricks", Integer.valueOf(206));
/*     */           paramHashMap.put("minecraft:beetroots", Integer.valueOf(207));
/*     */           paramHashMap.put("minecraft:grass_path", Integer.valueOf(208));
/*     */           paramHashMap.put("minecraft:end_gateway", Integer.valueOf(209));
/*     */           paramHashMap.put("minecraft:repeating_command_block", Integer.valueOf(210));
/*     */           paramHashMap.put("minecraft:chain_command_block", Integer.valueOf(211));
/*     */           paramHashMap.put("minecraft:frosted_ice", Integer.valueOf(212));
/*     */           paramHashMap.put("minecraft:magma", Integer.valueOf(213));
/*     */           paramHashMap.put("minecraft:nether_wart_block", Integer.valueOf(214));
/*     */           paramHashMap.put("minecraft:red_nether_brick", Integer.valueOf(215));
/*     */           paramHashMap.put("minecraft:bone_block", Integer.valueOf(216));
/*     */           paramHashMap.put("minecraft:structure_void", Integer.valueOf(217));
/*     */           paramHashMap.put("minecraft:observer", Integer.valueOf(218));
/*     */           paramHashMap.put("minecraft:white_shulker_box", Integer.valueOf(219));
/*     */           paramHashMap.put("minecraft:orange_shulker_box", Integer.valueOf(220));
/*     */           paramHashMap.put("minecraft:magenta_shulker_box", Integer.valueOf(221));
/*     */           paramHashMap.put("minecraft:light_blue_shulker_box", Integer.valueOf(222));
/*     */           paramHashMap.put("minecraft:yellow_shulker_box", Integer.valueOf(223));
/*     */           paramHashMap.put("minecraft:lime_shulker_box", Integer.valueOf(224));
/*     */           paramHashMap.put("minecraft:pink_shulker_box", Integer.valueOf(225));
/*     */           paramHashMap.put("minecraft:gray_shulker_box", Integer.valueOf(226));
/*     */           paramHashMap.put("minecraft:silver_shulker_box", Integer.valueOf(227));
/*     */           paramHashMap.put("minecraft:cyan_shulker_box", Integer.valueOf(228));
/*     */           paramHashMap.put("minecraft:purple_shulker_box", Integer.valueOf(229));
/*     */           paramHashMap.put("minecraft:blue_shulker_box", Integer.valueOf(230));
/*     */           paramHashMap.put("minecraft:brown_shulker_box", Integer.valueOf(231));
/*     */           paramHashMap.put("minecraft:green_shulker_box", Integer.valueOf(232));
/*     */           paramHashMap.put("minecraft:red_shulker_box", Integer.valueOf(233));
/*     */           paramHashMap.put("minecraft:black_shulker_box", Integer.valueOf(234));
/*     */           paramHashMap.put("minecraft:white_glazed_terracotta", Integer.valueOf(235));
/*     */           paramHashMap.put("minecraft:orange_glazed_terracotta", Integer.valueOf(236));
/*     */           paramHashMap.put("minecraft:magenta_glazed_terracotta", Integer.valueOf(237));
/*     */           paramHashMap.put("minecraft:light_blue_glazed_terracotta", Integer.valueOf(238));
/*     */           paramHashMap.put("minecraft:yellow_glazed_terracotta", Integer.valueOf(239));
/*     */           paramHashMap.put("minecraft:lime_glazed_terracotta", Integer.valueOf(240));
/*     */           paramHashMap.put("minecraft:pink_glazed_terracotta", Integer.valueOf(241));
/*     */           paramHashMap.put("minecraft:gray_glazed_terracotta", Integer.valueOf(242));
/*     */           paramHashMap.put("minecraft:silver_glazed_terracotta", Integer.valueOf(243));
/*     */           paramHashMap.put("minecraft:cyan_glazed_terracotta", Integer.valueOf(244));
/*     */           paramHashMap.put("minecraft:purple_glazed_terracotta", Integer.valueOf(245));
/*     */           paramHashMap.put("minecraft:blue_glazed_terracotta", Integer.valueOf(246));
/*     */           paramHashMap.put("minecraft:brown_glazed_terracotta", Integer.valueOf(247));
/*     */           paramHashMap.put("minecraft:green_glazed_terracotta", Integer.valueOf(248));
/*     */           paramHashMap.put("minecraft:red_glazed_terracotta", Integer.valueOf(249));
/*     */           paramHashMap.put("minecraft:black_glazed_terracotta", Integer.valueOf(250));
/*     */           paramHashMap.put("minecraft:concrete", Integer.valueOf(251));
/*     */           paramHashMap.put("minecraft:concrete_powder", Integer.valueOf(252));
/*     */           paramHashMap.put("minecraft:structure_block", Integer.valueOf(255));
/*     */         });
/*     */   }
/*     */   public static int getBlockId(String paramString) {
/* 295 */     Integer integer = MAP.get(paramString);
/* 296 */     return (integer == null) ? 0 : integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 301 */     Schema schema1 = getInputSchema();
/* 302 */     Schema schema2 = getOutputSchema();
/*     */     
/* 304 */     Function function1 = paramTyped -> updateBlockToBlockState(paramTyped, "DisplayTile", "DisplayData", "DisplayState");
/* 305 */     Function function2 = paramTyped -> updateBlockToBlockState(paramTyped, "inTile", "inData", "inBlockState");
/*     */     
/* 307 */     Type type = DSL.and(
/* 308 */         DSL.optional((Type)DSL.field("inTile", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())))), 
/* 309 */         DSL.remainderType());
/*     */ 
/*     */     
/* 312 */     Function function3 = paramTyped -> paramTyped.update(paramType.finder(), DSL.remainderType(), Pair::getSecond);
/*     */     
/* 314 */     return fixTypeEverywhereTyped("EntityBlockStateFix", schema1.getType(References.ENTITY), schema2.getType(References.ENTITY), paramTyped -> {
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:falling_block", this::updateFallingBlock);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:enderman", ());
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:arrow", paramFunction1);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:spectral_arrow", paramFunction1);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:egg", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:ender_pearl", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:fireball", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:potion", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:small_fireball", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:snowball", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:wither_skull", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:xp_bottle", paramFunction2);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:commandblock_minecart", paramFunction3);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:minecart", paramFunction3);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:chest_minecart", paramFunction3);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:furnace_minecart", paramFunction3);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:tnt_minecart", paramFunction3);
/*     */           paramTyped = updateEntity(paramTyped, "minecraft:hopper_minecart", paramFunction3);
/*     */           return updateEntity(paramTyped, "minecraft:spawner_minecart", paramFunction3);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private Typed<?> updateFallingBlock(Typed<?> paramTyped) {
/* 339 */     Type type1 = DSL.optional((Type)DSL.field("Block", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()))));
/* 340 */     Type type2 = DSL.optional((Type)DSL.field("BlockState", DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType())));
/*     */     
/* 342 */     Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*     */     
/* 344 */     return paramTyped.update(type1.finder(), type2, paramEither -> {
/*     */           int i = ((Integer)paramEither.map((), ())).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           int j = paramDynamic.get("Data").asInt(0) & 0xF;
/*     */ 
/*     */ 
/*     */           
/*     */           return Either.left(Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(i << 4 | j)));
/* 355 */         }).set(DSL.remainderFinder(), dynamic.remove("Data").remove("TileID").remove("Tile"));
/*     */   }
/*     */   
/*     */   private Typed<?> updateBlockToBlockState(Typed<?> paramTyped, String paramString1, String paramString2, String paramString3) {
/* 359 */     Tag.TagType tagType1 = DSL.field(paramString1, DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())));
/* 360 */     Tag.TagType tagType2 = DSL.field(paramString3, DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType()));
/*     */     
/* 362 */     Dynamic dynamic = (Dynamic)paramTyped.getOrCreate(DSL.remainderFinder());
/*     */     
/* 364 */     return paramTyped.update(tagType1.finder(), (Type)tagType2, paramPair -> {
/*     */           int i = ((Integer)((Either)paramPair.getSecond()).map((), EntityBlockStateFix::getBlockId)).intValue();
/*     */           
/*     */           int j = paramDynamic.get(paramString).asInt(0) & 0xF;
/*     */           return Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(i << 4 | j));
/* 369 */         }).set(DSL.remainderFinder(), dynamic.remove(paramString2));
/*     */   }
/*     */   
/*     */   private Typed<?> updateEntity(Typed<?> paramTyped, String paramString, Function<Typed<?>, Typed<?>> paramFunction) {
/* 373 */     Type type1 = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 374 */     Type type2 = getOutputSchema().getChoiceType(References.ENTITY, paramString);
/* 375 */     return paramTyped.updateTyped(DSL.namedChoice(paramString, type1), type2, paramFunction);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityBlockStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */