/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
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
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemStackTheFlatteningFix extends DataFix {
/*     */   private static final Map<String, String> MAP;
/*     */   
/*     */   public ItemStackTheFlatteningFix(Schema paramSchema, boolean paramBoolean) {
/*  26 */     super(paramSchema, paramBoolean);
/*     */   } private static final Set<String> IDS;
/*     */   static {
/*  29 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("minecraft:stone.0", "minecraft:stone");
/*     */           
/*     */           paramHashMap.put("minecraft:stone.1", "minecraft:granite");
/*     */           
/*     */           paramHashMap.put("minecraft:stone.2", "minecraft:polished_granite");
/*     */           paramHashMap.put("minecraft:stone.3", "minecraft:diorite");
/*     */           paramHashMap.put("minecraft:stone.4", "minecraft:polished_diorite");
/*     */           paramHashMap.put("minecraft:stone.5", "minecraft:andesite");
/*     */           paramHashMap.put("minecraft:stone.6", "minecraft:polished_andesite");
/*     */           paramHashMap.put("minecraft:dirt.0", "minecraft:dirt");
/*     */           paramHashMap.put("minecraft:dirt.1", "minecraft:coarse_dirt");
/*     */           paramHashMap.put("minecraft:dirt.2", "minecraft:podzol");
/*     */           paramHashMap.put("minecraft:leaves.0", "minecraft:oak_leaves");
/*     */           paramHashMap.put("minecraft:leaves.1", "minecraft:spruce_leaves");
/*     */           paramHashMap.put("minecraft:leaves.2", "minecraft:birch_leaves");
/*     */           paramHashMap.put("minecraft:leaves.3", "minecraft:jungle_leaves");
/*     */           paramHashMap.put("minecraft:leaves2.0", "minecraft:acacia_leaves");
/*     */           paramHashMap.put("minecraft:leaves2.1", "minecraft:dark_oak_leaves");
/*     */           paramHashMap.put("minecraft:log.0", "minecraft:oak_log");
/*     */           paramHashMap.put("minecraft:log.1", "minecraft:spruce_log");
/*     */           paramHashMap.put("minecraft:log.2", "minecraft:birch_log");
/*     */           paramHashMap.put("minecraft:log.3", "minecraft:jungle_log");
/*     */           paramHashMap.put("minecraft:log2.0", "minecraft:acacia_log");
/*     */           paramHashMap.put("minecraft:log2.1", "minecraft:dark_oak_log");
/*     */           paramHashMap.put("minecraft:sapling.0", "minecraft:oak_sapling");
/*     */           paramHashMap.put("minecraft:sapling.1", "minecraft:spruce_sapling");
/*     */           paramHashMap.put("minecraft:sapling.2", "minecraft:birch_sapling");
/*     */           paramHashMap.put("minecraft:sapling.3", "minecraft:jungle_sapling");
/*     */           paramHashMap.put("minecraft:sapling.4", "minecraft:acacia_sapling");
/*     */           paramHashMap.put("minecraft:sapling.5", "minecraft:dark_oak_sapling");
/*     */           paramHashMap.put("minecraft:planks.0", "minecraft:oak_planks");
/*     */           paramHashMap.put("minecraft:planks.1", "minecraft:spruce_planks");
/*     */           paramHashMap.put("minecraft:planks.2", "minecraft:birch_planks");
/*     */           paramHashMap.put("minecraft:planks.3", "minecraft:jungle_planks");
/*     */           paramHashMap.put("minecraft:planks.4", "minecraft:acacia_planks");
/*     */           paramHashMap.put("minecraft:planks.5", "minecraft:dark_oak_planks");
/*     */           paramHashMap.put("minecraft:sand.0", "minecraft:sand");
/*     */           paramHashMap.put("minecraft:sand.1", "minecraft:red_sand");
/*     */           paramHashMap.put("minecraft:quartz_block.0", "minecraft:quartz_block");
/*     */           paramHashMap.put("minecraft:quartz_block.1", "minecraft:chiseled_quartz_block");
/*     */           paramHashMap.put("minecraft:quartz_block.2", "minecraft:quartz_pillar");
/*     */           paramHashMap.put("minecraft:anvil.0", "minecraft:anvil");
/*     */           paramHashMap.put("minecraft:anvil.1", "minecraft:chipped_anvil");
/*     */           paramHashMap.put("minecraft:anvil.2", "minecraft:damaged_anvil");
/*     */           paramHashMap.put("minecraft:wool.0", "minecraft:white_wool");
/*     */           paramHashMap.put("minecraft:wool.1", "minecraft:orange_wool");
/*     */           paramHashMap.put("minecraft:wool.2", "minecraft:magenta_wool");
/*     */           paramHashMap.put("minecraft:wool.3", "minecraft:light_blue_wool");
/*     */           paramHashMap.put("minecraft:wool.4", "minecraft:yellow_wool");
/*     */           paramHashMap.put("minecraft:wool.5", "minecraft:lime_wool");
/*     */           paramHashMap.put("minecraft:wool.6", "minecraft:pink_wool");
/*     */           paramHashMap.put("minecraft:wool.7", "minecraft:gray_wool");
/*     */           paramHashMap.put("minecraft:wool.8", "minecraft:light_gray_wool");
/*     */           paramHashMap.put("minecraft:wool.9", "minecraft:cyan_wool");
/*     */           paramHashMap.put("minecraft:wool.10", "minecraft:purple_wool");
/*     */           paramHashMap.put("minecraft:wool.11", "minecraft:blue_wool");
/*     */           paramHashMap.put("minecraft:wool.12", "minecraft:brown_wool");
/*     */           paramHashMap.put("minecraft:wool.13", "minecraft:green_wool");
/*     */           paramHashMap.put("minecraft:wool.14", "minecraft:red_wool");
/*     */           paramHashMap.put("minecraft:wool.15", "minecraft:black_wool");
/*     */           paramHashMap.put("minecraft:carpet.0", "minecraft:white_carpet");
/*     */           paramHashMap.put("minecraft:carpet.1", "minecraft:orange_carpet");
/*     */           paramHashMap.put("minecraft:carpet.2", "minecraft:magenta_carpet");
/*     */           paramHashMap.put("minecraft:carpet.3", "minecraft:light_blue_carpet");
/*     */           paramHashMap.put("minecraft:carpet.4", "minecraft:yellow_carpet");
/*     */           paramHashMap.put("minecraft:carpet.5", "minecraft:lime_carpet");
/*     */           paramHashMap.put("minecraft:carpet.6", "minecraft:pink_carpet");
/*     */           paramHashMap.put("minecraft:carpet.7", "minecraft:gray_carpet");
/*     */           paramHashMap.put("minecraft:carpet.8", "minecraft:light_gray_carpet");
/*     */           paramHashMap.put("minecraft:carpet.9", "minecraft:cyan_carpet");
/*     */           paramHashMap.put("minecraft:carpet.10", "minecraft:purple_carpet");
/*     */           paramHashMap.put("minecraft:carpet.11", "minecraft:blue_carpet");
/*     */           paramHashMap.put("minecraft:carpet.12", "minecraft:brown_carpet");
/*     */           paramHashMap.put("minecraft:carpet.13", "minecraft:green_carpet");
/*     */           paramHashMap.put("minecraft:carpet.14", "minecraft:red_carpet");
/*     */           paramHashMap.put("minecraft:carpet.15", "minecraft:black_carpet");
/*     */           paramHashMap.put("minecraft:hardened_clay.0", "minecraft:terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.0", "minecraft:white_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.1", "minecraft:orange_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.2", "minecraft:magenta_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.3", "minecraft:light_blue_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.4", "minecraft:yellow_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.5", "minecraft:lime_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.6", "minecraft:pink_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.7", "minecraft:gray_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.8", "minecraft:light_gray_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.9", "minecraft:cyan_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.10", "minecraft:purple_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.11", "minecraft:blue_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.12", "minecraft:brown_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.13", "minecraft:green_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.14", "minecraft:red_terracotta");
/*     */           paramHashMap.put("minecraft:stained_hardened_clay.15", "minecraft:black_terracotta");
/*     */           paramHashMap.put("minecraft:silver_glazed_terracotta.0", "minecraft:light_gray_glazed_terracotta");
/*     */           paramHashMap.put("minecraft:stained_glass.0", "minecraft:white_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.1", "minecraft:orange_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.2", "minecraft:magenta_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.3", "minecraft:light_blue_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.4", "minecraft:yellow_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.5", "minecraft:lime_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.6", "minecraft:pink_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.7", "minecraft:gray_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.8", "minecraft:light_gray_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.9", "minecraft:cyan_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.10", "minecraft:purple_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.11", "minecraft:blue_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.12", "minecraft:brown_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.13", "minecraft:green_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.14", "minecraft:red_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass.15", "minecraft:black_stained_glass");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.0", "minecraft:white_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.1", "minecraft:orange_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.2", "minecraft:magenta_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.3", "minecraft:light_blue_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.4", "minecraft:yellow_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.5", "minecraft:lime_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.6", "minecraft:pink_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.7", "minecraft:gray_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.8", "minecraft:light_gray_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.9", "minecraft:cyan_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.10", "minecraft:purple_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.11", "minecraft:blue_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.12", "minecraft:brown_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.13", "minecraft:green_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.14", "minecraft:red_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:stained_glass_pane.15", "minecraft:black_stained_glass_pane");
/*     */           paramHashMap.put("minecraft:prismarine.0", "minecraft:prismarine");
/*     */           paramHashMap.put("minecraft:prismarine.1", "minecraft:prismarine_bricks");
/*     */           paramHashMap.put("minecraft:prismarine.2", "minecraft:dark_prismarine");
/*     */           paramHashMap.put("minecraft:concrete.0", "minecraft:white_concrete");
/*     */           paramHashMap.put("minecraft:concrete.1", "minecraft:orange_concrete");
/*     */           paramHashMap.put("minecraft:concrete.2", "minecraft:magenta_concrete");
/*     */           paramHashMap.put("minecraft:concrete.3", "minecraft:light_blue_concrete");
/*     */           paramHashMap.put("minecraft:concrete.4", "minecraft:yellow_concrete");
/*     */           paramHashMap.put("minecraft:concrete.5", "minecraft:lime_concrete");
/*     */           paramHashMap.put("minecraft:concrete.6", "minecraft:pink_concrete");
/*     */           paramHashMap.put("minecraft:concrete.7", "minecraft:gray_concrete");
/*     */           paramHashMap.put("minecraft:concrete.8", "minecraft:light_gray_concrete");
/*     */           paramHashMap.put("minecraft:concrete.9", "minecraft:cyan_concrete");
/*     */           paramHashMap.put("minecraft:concrete.10", "minecraft:purple_concrete");
/*     */           paramHashMap.put("minecraft:concrete.11", "minecraft:blue_concrete");
/*     */           paramHashMap.put("minecraft:concrete.12", "minecraft:brown_concrete");
/*     */           paramHashMap.put("minecraft:concrete.13", "minecraft:green_concrete");
/*     */           paramHashMap.put("minecraft:concrete.14", "minecraft:red_concrete");
/*     */           paramHashMap.put("minecraft:concrete.15", "minecraft:black_concrete");
/*     */           paramHashMap.put("minecraft:concrete_powder.0", "minecraft:white_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.1", "minecraft:orange_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.2", "minecraft:magenta_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.3", "minecraft:light_blue_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.4", "minecraft:yellow_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.5", "minecraft:lime_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.6", "minecraft:pink_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.7", "minecraft:gray_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.8", "minecraft:light_gray_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.9", "minecraft:cyan_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.10", "minecraft:purple_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.11", "minecraft:blue_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.12", "minecraft:brown_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.13", "minecraft:green_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.14", "minecraft:red_concrete_powder");
/*     */           paramHashMap.put("minecraft:concrete_powder.15", "minecraft:black_concrete_powder");
/*     */           paramHashMap.put("minecraft:cobblestone_wall.0", "minecraft:cobblestone_wall");
/*     */           paramHashMap.put("minecraft:cobblestone_wall.1", "minecraft:mossy_cobblestone_wall");
/*     */           paramHashMap.put("minecraft:sandstone.0", "minecraft:sandstone");
/*     */           paramHashMap.put("minecraft:sandstone.1", "minecraft:chiseled_sandstone");
/*     */           paramHashMap.put("minecraft:sandstone.2", "minecraft:cut_sandstone");
/*     */           paramHashMap.put("minecraft:red_sandstone.0", "minecraft:red_sandstone");
/*     */           paramHashMap.put("minecraft:red_sandstone.1", "minecraft:chiseled_red_sandstone");
/*     */           paramHashMap.put("minecraft:red_sandstone.2", "minecraft:cut_red_sandstone");
/*     */           paramHashMap.put("minecraft:stonebrick.0", "minecraft:stone_bricks");
/*     */           paramHashMap.put("minecraft:stonebrick.1", "minecraft:mossy_stone_bricks");
/*     */           paramHashMap.put("minecraft:stonebrick.2", "minecraft:cracked_stone_bricks");
/*     */           paramHashMap.put("minecraft:stonebrick.3", "minecraft:chiseled_stone_bricks");
/*     */           paramHashMap.put("minecraft:monster_egg.0", "minecraft:infested_stone");
/*     */           paramHashMap.put("minecraft:monster_egg.1", "minecraft:infested_cobblestone");
/*     */           paramHashMap.put("minecraft:monster_egg.2", "minecraft:infested_stone_bricks");
/*     */           paramHashMap.put("minecraft:monster_egg.3", "minecraft:infested_mossy_stone_bricks");
/*     */           paramHashMap.put("minecraft:monster_egg.4", "minecraft:infested_cracked_stone_bricks");
/*     */           paramHashMap.put("minecraft:monster_egg.5", "minecraft:infested_chiseled_stone_bricks");
/*     */           paramHashMap.put("minecraft:yellow_flower.0", "minecraft:dandelion");
/*     */           paramHashMap.put("minecraft:red_flower.0", "minecraft:poppy");
/*     */           paramHashMap.put("minecraft:red_flower.1", "minecraft:blue_orchid");
/*     */           paramHashMap.put("minecraft:red_flower.2", "minecraft:allium");
/*     */           paramHashMap.put("minecraft:red_flower.3", "minecraft:azure_bluet");
/*     */           paramHashMap.put("minecraft:red_flower.4", "minecraft:red_tulip");
/*     */           paramHashMap.put("minecraft:red_flower.5", "minecraft:orange_tulip");
/*     */           paramHashMap.put("minecraft:red_flower.6", "minecraft:white_tulip");
/*     */           paramHashMap.put("minecraft:red_flower.7", "minecraft:pink_tulip");
/*     */           paramHashMap.put("minecraft:red_flower.8", "minecraft:oxeye_daisy");
/*     */           paramHashMap.put("minecraft:double_plant.0", "minecraft:sunflower");
/*     */           paramHashMap.put("minecraft:double_plant.1", "minecraft:lilac");
/*     */           paramHashMap.put("minecraft:double_plant.2", "minecraft:tall_grass");
/*     */           paramHashMap.put("minecraft:double_plant.3", "minecraft:large_fern");
/*     */           paramHashMap.put("minecraft:double_plant.4", "minecraft:rose_bush");
/*     */           paramHashMap.put("minecraft:double_plant.5", "minecraft:peony");
/*     */           paramHashMap.put("minecraft:deadbush.0", "minecraft:dead_bush");
/*     */           paramHashMap.put("minecraft:tallgrass.0", "minecraft:dead_bush");
/*     */           paramHashMap.put("minecraft:tallgrass.1", "minecraft:grass");
/*     */           paramHashMap.put("minecraft:tallgrass.2", "minecraft:fern");
/*     */           paramHashMap.put("minecraft:sponge.0", "minecraft:sponge");
/*     */           paramHashMap.put("minecraft:sponge.1", "minecraft:wet_sponge");
/*     */           paramHashMap.put("minecraft:purpur_slab.0", "minecraft:purpur_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.0", "minecraft:stone_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.1", "minecraft:sandstone_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.2", "minecraft:petrified_oak_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.3", "minecraft:cobblestone_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.4", "minecraft:brick_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.5", "minecraft:stone_brick_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.6", "minecraft:nether_brick_slab");
/*     */           paramHashMap.put("minecraft:stone_slab.7", "minecraft:quartz_slab");
/*     */           paramHashMap.put("minecraft:stone_slab2.0", "minecraft:red_sandstone_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.0", "minecraft:oak_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.1", "minecraft:spruce_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.2", "minecraft:birch_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.3", "minecraft:jungle_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.4", "minecraft:acacia_slab");
/*     */           paramHashMap.put("minecraft:wooden_slab.5", "minecraft:dark_oak_slab");
/*     */           paramHashMap.put("minecraft:coal.0", "minecraft:coal");
/*     */           paramHashMap.put("minecraft:coal.1", "minecraft:charcoal");
/*     */           paramHashMap.put("minecraft:fish.0", "minecraft:cod");
/*     */           paramHashMap.put("minecraft:fish.1", "minecraft:salmon");
/*     */           paramHashMap.put("minecraft:fish.2", "minecraft:clownfish");
/*     */           paramHashMap.put("minecraft:fish.3", "minecraft:pufferfish");
/*     */           paramHashMap.put("minecraft:cooked_fish.0", "minecraft:cooked_cod");
/*     */           paramHashMap.put("minecraft:cooked_fish.1", "minecraft:cooked_salmon");
/*     */           paramHashMap.put("minecraft:skull.0", "minecraft:skeleton_skull");
/*     */           paramHashMap.put("minecraft:skull.1", "minecraft:wither_skeleton_skull");
/*     */           paramHashMap.put("minecraft:skull.2", "minecraft:zombie_head");
/*     */           paramHashMap.put("minecraft:skull.3", "minecraft:player_head");
/*     */           paramHashMap.put("minecraft:skull.4", "minecraft:creeper_head");
/*     */           paramHashMap.put("minecraft:skull.5", "minecraft:dragon_head");
/*     */           paramHashMap.put("minecraft:golden_apple.0", "minecraft:golden_apple");
/*     */           paramHashMap.put("minecraft:golden_apple.1", "minecraft:enchanted_golden_apple");
/*     */           paramHashMap.put("minecraft:fireworks.0", "minecraft:firework_rocket");
/*     */           paramHashMap.put("minecraft:firework_charge.0", "minecraft:firework_star");
/*     */           paramHashMap.put("minecraft:dye.0", "minecraft:ink_sac");
/*     */           paramHashMap.put("minecraft:dye.1", "minecraft:rose_red");
/*     */           paramHashMap.put("minecraft:dye.2", "minecraft:cactus_green");
/*     */           paramHashMap.put("minecraft:dye.3", "minecraft:cocoa_beans");
/*     */           paramHashMap.put("minecraft:dye.4", "minecraft:lapis_lazuli");
/*     */           paramHashMap.put("minecraft:dye.5", "minecraft:purple_dye");
/*     */           paramHashMap.put("minecraft:dye.6", "minecraft:cyan_dye");
/*     */           paramHashMap.put("minecraft:dye.7", "minecraft:light_gray_dye");
/*     */           paramHashMap.put("minecraft:dye.8", "minecraft:gray_dye");
/*     */           paramHashMap.put("minecraft:dye.9", "minecraft:pink_dye");
/*     */           paramHashMap.put("minecraft:dye.10", "minecraft:lime_dye");
/*     */           paramHashMap.put("minecraft:dye.11", "minecraft:dandelion_yellow");
/*     */           paramHashMap.put("minecraft:dye.12", "minecraft:light_blue_dye");
/*     */           paramHashMap.put("minecraft:dye.13", "minecraft:magenta_dye");
/*     */           paramHashMap.put("minecraft:dye.14", "minecraft:orange_dye");
/*     */           paramHashMap.put("minecraft:dye.15", "minecraft:bone_meal");
/*     */           paramHashMap.put("minecraft:silver_shulker_box.0", "minecraft:light_gray_shulker_box");
/*     */           paramHashMap.put("minecraft:fence.0", "minecraft:oak_fence");
/*     */           paramHashMap.put("minecraft:fence_gate.0", "minecraft:oak_fence_gate");
/*     */           paramHashMap.put("minecraft:wooden_door.0", "minecraft:oak_door");
/*     */           paramHashMap.put("minecraft:boat.0", "minecraft:oak_boat");
/*     */           paramHashMap.put("minecraft:lit_pumpkin.0", "minecraft:jack_o_lantern");
/*     */           paramHashMap.put("minecraft:pumpkin.0", "minecraft:carved_pumpkin");
/*     */           paramHashMap.put("minecraft:trapdoor.0", "minecraft:oak_trapdoor");
/*     */           paramHashMap.put("minecraft:nether_brick.0", "minecraft:nether_bricks");
/*     */           paramHashMap.put("minecraft:red_nether_brick.0", "minecraft:red_nether_bricks");
/*     */           paramHashMap.put("minecraft:netherbrick.0", "minecraft:nether_brick");
/*     */           paramHashMap.put("minecraft:wooden_button.0", "minecraft:oak_button");
/*     */           paramHashMap.put("minecraft:wooden_pressure_plate.0", "minecraft:oak_pressure_plate");
/*     */           paramHashMap.put("minecraft:noteblock.0", "minecraft:note_block");
/*     */           paramHashMap.put("minecraft:bed.0", "minecraft:white_bed");
/*     */           paramHashMap.put("minecraft:bed.1", "minecraft:orange_bed");
/*     */           paramHashMap.put("minecraft:bed.2", "minecraft:magenta_bed");
/*     */           paramHashMap.put("minecraft:bed.3", "minecraft:light_blue_bed");
/*     */           paramHashMap.put("minecraft:bed.4", "minecraft:yellow_bed");
/*     */           paramHashMap.put("minecraft:bed.5", "minecraft:lime_bed");
/*     */           paramHashMap.put("minecraft:bed.6", "minecraft:pink_bed");
/*     */           paramHashMap.put("minecraft:bed.7", "minecraft:gray_bed");
/*     */           paramHashMap.put("minecraft:bed.8", "minecraft:light_gray_bed");
/*     */           paramHashMap.put("minecraft:bed.9", "minecraft:cyan_bed");
/*     */           paramHashMap.put("minecraft:bed.10", "minecraft:purple_bed");
/*     */           paramHashMap.put("minecraft:bed.11", "minecraft:blue_bed");
/*     */           paramHashMap.put("minecraft:bed.12", "minecraft:brown_bed");
/*     */           paramHashMap.put("minecraft:bed.13", "minecraft:green_bed");
/*     */           paramHashMap.put("minecraft:bed.14", "minecraft:red_bed");
/*     */           paramHashMap.put("minecraft:bed.15", "minecraft:black_bed");
/*     */           paramHashMap.put("minecraft:banner.15", "minecraft:white_banner");
/*     */           paramHashMap.put("minecraft:banner.14", "minecraft:orange_banner");
/*     */           paramHashMap.put("minecraft:banner.13", "minecraft:magenta_banner");
/*     */           paramHashMap.put("minecraft:banner.12", "minecraft:light_blue_banner");
/*     */           paramHashMap.put("minecraft:banner.11", "minecraft:yellow_banner");
/*     */           paramHashMap.put("minecraft:banner.10", "minecraft:lime_banner");
/*     */           paramHashMap.put("minecraft:banner.9", "minecraft:pink_banner");
/*     */           paramHashMap.put("minecraft:banner.8", "minecraft:gray_banner");
/*     */           paramHashMap.put("minecraft:banner.7", "minecraft:light_gray_banner");
/*     */           paramHashMap.put("minecraft:banner.6", "minecraft:cyan_banner");
/*     */           paramHashMap.put("minecraft:banner.5", "minecraft:purple_banner");
/*     */           paramHashMap.put("minecraft:banner.4", "minecraft:blue_banner");
/*     */           paramHashMap.put("minecraft:banner.3", "minecraft:brown_banner");
/*     */           paramHashMap.put("minecraft:banner.2", "minecraft:green_banner");
/*     */           paramHashMap.put("minecraft:banner.1", "minecraft:red_banner");
/*     */           paramHashMap.put("minecraft:banner.0", "minecraft:black_banner");
/*     */           paramHashMap.put("minecraft:grass.0", "minecraft:grass_block");
/*     */           paramHashMap.put("minecraft:brick_block.0", "minecraft:bricks");
/*     */           paramHashMap.put("minecraft:end_bricks.0", "minecraft:end_stone_bricks");
/*     */           paramHashMap.put("minecraft:golden_rail.0", "minecraft:powered_rail");
/*     */           paramHashMap.put("minecraft:magma.0", "minecraft:magma_block");
/*     */           paramHashMap.put("minecraft:quartz_ore.0", "minecraft:nether_quartz_ore");
/*     */           paramHashMap.put("minecraft:reeds.0", "minecraft:sugar_cane");
/*     */           paramHashMap.put("minecraft:slime.0", "minecraft:slime_block");
/*     */           paramHashMap.put("minecraft:stone_stairs.0", "minecraft:cobblestone_stairs");
/*     */           paramHashMap.put("minecraft:waterlily.0", "minecraft:lily_pad");
/*     */           paramHashMap.put("minecraft:web.0", "minecraft:cobweb");
/*     */           paramHashMap.put("minecraft:snow.0", "minecraft:snow_block");
/*     */           paramHashMap.put("minecraft:snow_layer.0", "minecraft:snow");
/*     */           paramHashMap.put("minecraft:record_11.0", "minecraft:music_disc_11");
/*     */           paramHashMap.put("minecraft:record_13.0", "minecraft:music_disc_13");
/*     */           paramHashMap.put("minecraft:record_blocks.0", "minecraft:music_disc_blocks");
/*     */           paramHashMap.put("minecraft:record_cat.0", "minecraft:music_disc_cat");
/*     */           paramHashMap.put("minecraft:record_chirp.0", "minecraft:music_disc_chirp");
/*     */           paramHashMap.put("minecraft:record_far.0", "minecraft:music_disc_far");
/*     */           paramHashMap.put("minecraft:record_mall.0", "minecraft:music_disc_mall");
/*     */           paramHashMap.put("minecraft:record_mellohi.0", "minecraft:music_disc_mellohi");
/*     */           paramHashMap.put("minecraft:record_stal.0", "minecraft:music_disc_stal");
/*     */           paramHashMap.put("minecraft:record_strad.0", "minecraft:music_disc_strad");
/*     */           paramHashMap.put("minecraft:record_wait.0", "minecraft:music_disc_wait");
/*     */           paramHashMap.put("minecraft:record_ward.0", "minecraft:music_disc_ward");
/*     */         });
/* 353 */     IDS = (Set<String>)MAP.keySet().stream().map(paramString -> paramString.substring(0, paramString.indexOf('.'))).collect(Collectors.toSet());
/*     */   }
/* 355 */   private static final Set<String> DAMAGE_IDS = Sets.newHashSet((Object[])new String[] { "minecraft:bow", "minecraft:carrot_on_a_stick", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:diamond_axe", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_hoe", "minecraft:diamond_leggings", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_sword", "minecraft:elytra", "minecraft:fishing_rod", "minecraft:flint_and_steel", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_hoe", "minecraft:golden_leggings", "minecraft:golden_pickaxe", "minecraft:golden_shovel", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_hoe", "minecraft:iron_leggings", "minecraft:iron_pickaxe", "minecraft:iron_shovel", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:shears", "minecraft:shield", "minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:stone_pickaxe", "minecraft:stone_shovel", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:wooden_pickaxe", "minecraft:wooden_shovel", "minecraft:wooden_sword" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 412 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*     */     
/* 414 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 415 */     OpticFinder opticFinder2 = type.findField("tag");
/*     */     
/* 417 */     return fixTypeEverywhereTyped("ItemInstanceTheFlatteningFix", type, paramTyped -> {
/*     */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*     */           if (optional.isEmpty()) {
/*     */             return paramTyped;
/*     */           }
/*     */           null = paramTyped;
/*     */           Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*     */           int i = dynamic.get("Damage").asInt(0);
/*     */           String str = updateItem((String)((Pair)optional.get()).getSecond(), i);
/*     */           if (str != null) {
/*     */             null = null.set(paramOpticFinder1, Pair.of(References.ITEM_NAME.typeName(), str));
/*     */           }
/*     */           if (DAMAGE_IDS.contains(((Pair)optional.get()).getSecond())) {
/*     */             Typed typed = paramTyped.getOrCreateTyped(paramOpticFinder2);
/*     */             Dynamic dynamic1 = (Dynamic)typed.get(DSL.remainderFinder());
/*     */             dynamic1 = dynamic1.set("Damage", dynamic1.createInt(i));
/*     */             null = null.set(paramOpticFinder2, typed.set(DSL.remainderFinder(), dynamic1));
/*     */           } 
/*     */           return null.set(DSL.remainderFinder(), dynamic.remove("Damage"));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String updateItem(String paramString, int paramInt) {
/* 445 */     if (IDS.contains(paramString)) {
/* 446 */       String str = MAP.get(paramString + "." + paramString);
/* 447 */       return (str == null) ? MAP.get(paramString + ".0") : str;
/*     */     } 
/* 449 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackTheFlatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */