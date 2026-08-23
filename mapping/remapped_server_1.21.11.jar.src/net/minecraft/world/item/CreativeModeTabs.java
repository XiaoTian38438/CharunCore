/*      */ package net.minecraft.world.item;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.InstrumentTags;
/*      */ import net.minecraft.tags.PaintingVariantTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.alchemy.Potion;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.component.Fireworks;
/*      */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.LightBlock;
/*      */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*      */ import net.minecraft.world.level.block.TestBlock;
/*      */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*      */ 
/*      */ public class CreativeModeTabs {
/*   46 */   private static final Identifier INVENTORY_BACKGROUND = CreativeModeTab.createTextureLocation("inventory");
/*   47 */   private static final Identifier SEARCH_BACKGROUND = CreativeModeTab.createTextureLocation("item_search");
/*      */   
/*      */   private static ResourceKey<CreativeModeTab> createKey(String paramString) {
/*   50 */     return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace(paramString));
/*      */   }
/*      */   
/*   53 */   private static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
/*   54 */   private static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = createKey("colored_blocks");
/*   55 */   private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = createKey("natural_blocks");
/*   56 */   private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = createKey("functional_blocks");
/*   57 */   private static final ResourceKey<CreativeModeTab> REDSTONE_BLOCKS = createKey("redstone_blocks");
/*   58 */   private static final ResourceKey<CreativeModeTab> HOTBAR = createKey("hotbar");
/*   59 */   private static final ResourceKey<CreativeModeTab> SEARCH = createKey("search");
/*   60 */   private static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = createKey("tools_and_utilities");
/*   61 */   private static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
/*   62 */   private static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = createKey("food_and_drinks");
/*   63 */   private static final ResourceKey<CreativeModeTab> INGREDIENTS = createKey("ingredients");
/*   64 */   private static final ResourceKey<CreativeModeTab> SPAWN_EGGS = createKey("spawn_eggs");
/*   65 */   private static final ResourceKey<CreativeModeTab> OP_BLOCKS = createKey("op_blocks");
/*   66 */   private static final ResourceKey<CreativeModeTab> INVENTORY = createKey("inventory");
/*      */   
/*      */   public static CreativeModeTab bootstrap(Registry<CreativeModeTab> paramRegistry) {
/*   69 */     Registry.register(paramRegistry, BUILDING_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
/*   70 */         .title((Component)Component.translatable("itemGroup.buildingBlocks"))
/*   71 */         .icon(() -> new ItemStack((ItemLike)Blocks.BRICKS))
/*   72 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.OAK_LOG);
/*      */             
/*      */             paramOutput.accept(Items.OAK_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_OAK_LOG);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_OAK_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.OAK_PLANKS);
/*      */             
/*      */             paramOutput.accept(Items.OAK_STAIRS);
/*      */             
/*      */             paramOutput.accept(Items.OAK_SLAB);
/*      */             
/*      */             paramOutput.accept(Items.OAK_FENCE);
/*      */             
/*      */             paramOutput.accept(Items.OAK_FENCE_GATE);
/*      */             
/*      */             paramOutput.accept(Items.OAK_DOOR);
/*      */             
/*      */             paramOutput.accept(Items.OAK_TRAPDOOR);
/*      */             
/*      */             paramOutput.accept(Items.OAK_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.OAK_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_LOG);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_SPRUCE_LOG);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_SPRUCE_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_PLANKS);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_STAIRS);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_SLAB);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_FENCE);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_FENCE_GATE);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_DOOR);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_TRAPDOOR);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.SPRUCE_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_LOG);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_BIRCH_LOG);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_BIRCH_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_PLANKS);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_STAIRS);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_SLAB);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_FENCE);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_FENCE_GATE);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_DOOR);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_TRAPDOOR);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.BIRCH_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_LOG);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_JUNGLE_LOG);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_JUNGLE_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_PLANKS);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_STAIRS);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_SLAB);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_FENCE);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_FENCE_GATE);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_DOOR);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_TRAPDOOR);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.JUNGLE_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_LOG);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_ACACIA_LOG);
/*      */             
/*      */             paramOutput.accept(Items.STRIPPED_ACACIA_WOOD);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_PLANKS);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_STAIRS);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_SLAB);
/*      */             
/*      */             paramOutput.accept(Items.ACACIA_FENCE);
/*      */             paramOutput.accept(Items.ACACIA_FENCE_GATE);
/*      */             paramOutput.accept(Items.ACACIA_DOOR);
/*      */             paramOutput.accept(Items.ACACIA_TRAPDOOR);
/*      */             paramOutput.accept(Items.ACACIA_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.ACACIA_BUTTON);
/*      */             paramOutput.accept(Items.DARK_OAK_LOG);
/*      */             paramOutput.accept(Items.DARK_OAK_WOOD);
/*      */             paramOutput.accept(Items.STRIPPED_DARK_OAK_LOG);
/*      */             paramOutput.accept(Items.STRIPPED_DARK_OAK_WOOD);
/*      */             paramOutput.accept(Items.DARK_OAK_PLANKS);
/*      */             paramOutput.accept(Items.DARK_OAK_STAIRS);
/*      */             paramOutput.accept(Items.DARK_OAK_SLAB);
/*      */             paramOutput.accept(Items.DARK_OAK_FENCE);
/*      */             paramOutput.accept(Items.DARK_OAK_FENCE_GATE);
/*      */             paramOutput.accept(Items.DARK_OAK_DOOR);
/*      */             paramOutput.accept(Items.DARK_OAK_TRAPDOOR);
/*      */             paramOutput.accept(Items.DARK_OAK_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.DARK_OAK_BUTTON);
/*      */             paramOutput.accept(Items.MANGROVE_LOG);
/*      */             paramOutput.accept(Items.MANGROVE_WOOD);
/*      */             paramOutput.accept(Items.STRIPPED_MANGROVE_LOG);
/*      */             paramOutput.accept(Items.STRIPPED_MANGROVE_WOOD);
/*      */             paramOutput.accept(Items.MANGROVE_PLANKS);
/*      */             paramOutput.accept(Items.MANGROVE_STAIRS);
/*      */             paramOutput.accept(Items.MANGROVE_SLAB);
/*      */             paramOutput.accept(Items.MANGROVE_FENCE);
/*      */             paramOutput.accept(Items.MANGROVE_FENCE_GATE);
/*      */             paramOutput.accept(Items.MANGROVE_DOOR);
/*      */             paramOutput.accept(Items.MANGROVE_TRAPDOOR);
/*      */             paramOutput.accept(Items.MANGROVE_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.MANGROVE_BUTTON);
/*      */             paramOutput.accept(Items.CHERRY_LOG);
/*      */             paramOutput.accept(Items.CHERRY_WOOD);
/*      */             paramOutput.accept(Items.STRIPPED_CHERRY_LOG);
/*      */             paramOutput.accept(Items.STRIPPED_CHERRY_WOOD);
/*      */             paramOutput.accept(Items.CHERRY_PLANKS);
/*      */             paramOutput.accept(Items.CHERRY_STAIRS);
/*      */             paramOutput.accept(Items.CHERRY_SLAB);
/*      */             paramOutput.accept(Items.CHERRY_FENCE);
/*      */             paramOutput.accept(Items.CHERRY_FENCE_GATE);
/*      */             paramOutput.accept(Items.CHERRY_DOOR);
/*      */             paramOutput.accept(Items.CHERRY_TRAPDOOR);
/*      */             paramOutput.accept(Items.CHERRY_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.CHERRY_BUTTON);
/*      */             paramOutput.accept(Items.PALE_OAK_LOG);
/*      */             paramOutput.accept(Items.PALE_OAK_WOOD);
/*      */             paramOutput.accept(Items.STRIPPED_PALE_OAK_LOG);
/*      */             paramOutput.accept(Items.STRIPPED_PALE_OAK_WOOD);
/*      */             paramOutput.accept(Items.PALE_OAK_PLANKS);
/*      */             paramOutput.accept(Items.PALE_OAK_STAIRS);
/*      */             paramOutput.accept(Items.PALE_OAK_SLAB);
/*      */             paramOutput.accept(Items.PALE_OAK_FENCE);
/*      */             paramOutput.accept(Items.PALE_OAK_FENCE_GATE);
/*      */             paramOutput.accept(Items.PALE_OAK_DOOR);
/*      */             paramOutput.accept(Items.PALE_OAK_TRAPDOOR);
/*      */             paramOutput.accept(Items.PALE_OAK_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.PALE_OAK_BUTTON);
/*      */             paramOutput.accept(Items.BAMBOO_BLOCK);
/*      */             paramOutput.accept(Items.STRIPPED_BAMBOO_BLOCK);
/*      */             paramOutput.accept(Items.BAMBOO_PLANKS);
/*      */             paramOutput.accept(Items.BAMBOO_MOSAIC);
/*      */             paramOutput.accept(Items.BAMBOO_STAIRS);
/*      */             paramOutput.accept(Items.BAMBOO_MOSAIC_STAIRS);
/*      */             paramOutput.accept(Items.BAMBOO_SLAB);
/*      */             paramOutput.accept(Items.BAMBOO_MOSAIC_SLAB);
/*      */             paramOutput.accept(Items.BAMBOO_FENCE);
/*      */             paramOutput.accept(Items.BAMBOO_FENCE_GATE);
/*      */             paramOutput.accept(Items.BAMBOO_DOOR);
/*      */             paramOutput.accept(Items.BAMBOO_TRAPDOOR);
/*      */             paramOutput.accept(Items.BAMBOO_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.BAMBOO_BUTTON);
/*      */             paramOutput.accept(Items.CRIMSON_STEM);
/*      */             paramOutput.accept(Items.CRIMSON_HYPHAE);
/*      */             paramOutput.accept(Items.STRIPPED_CRIMSON_STEM);
/*      */             paramOutput.accept(Items.STRIPPED_CRIMSON_HYPHAE);
/*      */             paramOutput.accept(Items.CRIMSON_PLANKS);
/*      */             paramOutput.accept(Items.CRIMSON_STAIRS);
/*      */             paramOutput.accept(Items.CRIMSON_SLAB);
/*      */             paramOutput.accept(Items.CRIMSON_FENCE);
/*      */             paramOutput.accept(Items.CRIMSON_FENCE_GATE);
/*      */             paramOutput.accept(Items.CRIMSON_DOOR);
/*      */             paramOutput.accept(Items.CRIMSON_TRAPDOOR);
/*      */             paramOutput.accept(Items.CRIMSON_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.CRIMSON_BUTTON);
/*      */             paramOutput.accept(Items.WARPED_STEM);
/*      */             paramOutput.accept(Items.WARPED_HYPHAE);
/*      */             paramOutput.accept(Items.STRIPPED_WARPED_STEM);
/*      */             paramOutput.accept(Items.STRIPPED_WARPED_HYPHAE);
/*      */             paramOutput.accept(Items.WARPED_PLANKS);
/*      */             paramOutput.accept(Items.WARPED_STAIRS);
/*      */             paramOutput.accept(Items.WARPED_SLAB);
/*      */             paramOutput.accept(Items.WARPED_FENCE);
/*      */             paramOutput.accept(Items.WARPED_FENCE_GATE);
/*      */             paramOutput.accept(Items.WARPED_DOOR);
/*      */             paramOutput.accept(Items.WARPED_TRAPDOOR);
/*      */             paramOutput.accept(Items.WARPED_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.WARPED_BUTTON);
/*      */             paramOutput.accept(Items.STONE);
/*      */             paramOutput.accept(Items.STONE_STAIRS);
/*      */             paramOutput.accept(Items.STONE_SLAB);
/*      */             paramOutput.accept(Items.STONE_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.STONE_BUTTON);
/*      */             paramOutput.accept(Items.COBBLESTONE);
/*      */             paramOutput.accept(Items.COBBLESTONE_STAIRS);
/*      */             paramOutput.accept(Items.COBBLESTONE_SLAB);
/*      */             paramOutput.accept(Items.COBBLESTONE_WALL);
/*      */             paramOutput.accept(Items.MOSSY_COBBLESTONE);
/*      */             paramOutput.accept(Items.MOSSY_COBBLESTONE_STAIRS);
/*      */             paramOutput.accept(Items.MOSSY_COBBLESTONE_SLAB);
/*      */             paramOutput.accept(Items.MOSSY_COBBLESTONE_WALL);
/*      */             paramOutput.accept(Items.SMOOTH_STONE);
/*      */             paramOutput.accept(Items.SMOOTH_STONE_SLAB);
/*      */             paramOutput.accept(Items.STONE_BRICKS);
/*      */             paramOutput.accept(Items.CRACKED_STONE_BRICKS);
/*      */             paramOutput.accept(Items.STONE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.STONE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.STONE_BRICK_WALL);
/*      */             paramOutput.accept(Items.CHISELED_STONE_BRICKS);
/*      */             paramOutput.accept(Items.MOSSY_STONE_BRICKS);
/*      */             paramOutput.accept(Items.MOSSY_STONE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.MOSSY_STONE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.MOSSY_STONE_BRICK_WALL);
/*      */             paramOutput.accept(Items.GRANITE);
/*      */             paramOutput.accept(Items.GRANITE_STAIRS);
/*      */             paramOutput.accept(Items.GRANITE_SLAB);
/*      */             paramOutput.accept(Items.GRANITE_WALL);
/*      */             paramOutput.accept(Items.POLISHED_GRANITE);
/*      */             paramOutput.accept(Items.POLISHED_GRANITE_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_GRANITE_SLAB);
/*      */             paramOutput.accept(Items.DIORITE);
/*      */             paramOutput.accept(Items.DIORITE_STAIRS);
/*      */             paramOutput.accept(Items.DIORITE_SLAB);
/*      */             paramOutput.accept(Items.DIORITE_WALL);
/*      */             paramOutput.accept(Items.POLISHED_DIORITE);
/*      */             paramOutput.accept(Items.POLISHED_DIORITE_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_DIORITE_SLAB);
/*      */             paramOutput.accept(Items.ANDESITE);
/*      */             paramOutput.accept(Items.ANDESITE_STAIRS);
/*      */             paramOutput.accept(Items.ANDESITE_SLAB);
/*      */             paramOutput.accept(Items.ANDESITE_WALL);
/*      */             paramOutput.accept(Items.POLISHED_ANDESITE);
/*      */             paramOutput.accept(Items.POLISHED_ANDESITE_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_ANDESITE_SLAB);
/*      */             paramOutput.accept(Items.DEEPSLATE);
/*      */             paramOutput.accept(Items.COBBLED_DEEPSLATE);
/*      */             paramOutput.accept(Items.COBBLED_DEEPSLATE_STAIRS);
/*      */             paramOutput.accept(Items.COBBLED_DEEPSLATE_SLAB);
/*      */             paramOutput.accept(Items.COBBLED_DEEPSLATE_WALL);
/*      */             paramOutput.accept(Items.CHISELED_DEEPSLATE);
/*      */             paramOutput.accept(Items.POLISHED_DEEPSLATE);
/*      */             paramOutput.accept(Items.POLISHED_DEEPSLATE_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_DEEPSLATE_SLAB);
/*      */             paramOutput.accept(Items.POLISHED_DEEPSLATE_WALL);
/*      */             paramOutput.accept(Items.DEEPSLATE_BRICKS);
/*      */             paramOutput.accept(Items.CRACKED_DEEPSLATE_BRICKS);
/*      */             paramOutput.accept(Items.DEEPSLATE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.DEEPSLATE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.DEEPSLATE_BRICK_WALL);
/*      */             paramOutput.accept(Items.DEEPSLATE_TILES);
/*      */             paramOutput.accept(Items.CRACKED_DEEPSLATE_TILES);
/*      */             paramOutput.accept(Items.DEEPSLATE_TILE_STAIRS);
/*      */             paramOutput.accept(Items.DEEPSLATE_TILE_SLAB);
/*      */             paramOutput.accept(Items.DEEPSLATE_TILE_WALL);
/*      */             paramOutput.accept(Items.REINFORCED_DEEPSLATE);
/*      */             paramOutput.accept(Items.TUFF);
/*      */             paramOutput.accept(Items.TUFF_STAIRS);
/*      */             paramOutput.accept(Items.TUFF_SLAB);
/*      */             paramOutput.accept(Items.TUFF_WALL);
/*      */             paramOutput.accept(Items.CHISELED_TUFF);
/*      */             paramOutput.accept(Items.POLISHED_TUFF);
/*      */             paramOutput.accept(Items.POLISHED_TUFF_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_TUFF_SLAB);
/*      */             paramOutput.accept(Items.POLISHED_TUFF_WALL);
/*      */             paramOutput.accept(Items.TUFF_BRICKS);
/*      */             paramOutput.accept(Items.TUFF_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.TUFF_BRICK_SLAB);
/*      */             paramOutput.accept(Items.TUFF_BRICK_WALL);
/*      */             paramOutput.accept(Items.CHISELED_TUFF_BRICKS);
/*      */             paramOutput.accept(Items.BRICKS);
/*      */             paramOutput.accept(Items.BRICK_STAIRS);
/*      */             paramOutput.accept(Items.BRICK_SLAB);
/*      */             paramOutput.accept(Items.BRICK_WALL);
/*      */             paramOutput.accept(Items.PACKED_MUD);
/*      */             paramOutput.accept(Items.MUD_BRICKS);
/*      */             paramOutput.accept(Items.MUD_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.MUD_BRICK_SLAB);
/*      */             paramOutput.accept(Items.MUD_BRICK_WALL);
/*      */             paramOutput.accept(Items.RESIN_BRICKS);
/*      */             paramOutput.accept(Items.RESIN_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.RESIN_BRICK_SLAB);
/*      */             paramOutput.accept(Items.RESIN_BRICK_WALL);
/*      */             paramOutput.accept(Items.CHISELED_RESIN_BRICKS);
/*      */             paramOutput.accept(Items.SANDSTONE);
/*      */             paramOutput.accept(Items.SANDSTONE_STAIRS);
/*      */             paramOutput.accept(Items.SANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.SANDSTONE_WALL);
/*      */             paramOutput.accept(Items.CHISELED_SANDSTONE);
/*      */             paramOutput.accept(Items.SMOOTH_SANDSTONE);
/*      */             paramOutput.accept(Items.SMOOTH_SANDSTONE_STAIRS);
/*      */             paramOutput.accept(Items.SMOOTH_SANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.CUT_SANDSTONE);
/*      */             paramOutput.accept(Items.CUT_STANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.RED_SANDSTONE);
/*      */             paramOutput.accept(Items.RED_SANDSTONE_STAIRS);
/*      */             paramOutput.accept(Items.RED_SANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.RED_SANDSTONE_WALL);
/*      */             paramOutput.accept(Items.CHISELED_RED_SANDSTONE);
/*      */             paramOutput.accept(Items.SMOOTH_RED_SANDSTONE);
/*      */             paramOutput.accept(Items.SMOOTH_RED_SANDSTONE_STAIRS);
/*      */             paramOutput.accept(Items.SMOOTH_RED_SANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.CUT_RED_SANDSTONE);
/*      */             paramOutput.accept(Items.CUT_RED_SANDSTONE_SLAB);
/*      */             paramOutput.accept(Items.SEA_LANTERN);
/*      */             paramOutput.accept(Items.PRISMARINE);
/*      */             paramOutput.accept(Items.PRISMARINE_STAIRS);
/*      */             paramOutput.accept(Items.PRISMARINE_SLAB);
/*      */             paramOutput.accept(Items.PRISMARINE_WALL);
/*      */             paramOutput.accept(Items.PRISMARINE_BRICKS);
/*      */             paramOutput.accept(Items.PRISMARINE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.PRISMARINE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.DARK_PRISMARINE);
/*      */             paramOutput.accept(Items.DARK_PRISMARINE_STAIRS);
/*      */             paramOutput.accept(Items.DARK_PRISMARINE_SLAB);
/*      */             paramOutput.accept(Items.NETHERRACK);
/*      */             paramOutput.accept(Items.NETHER_BRICKS);
/*      */             paramOutput.accept(Items.CRACKED_NETHER_BRICKS);
/*      */             paramOutput.accept(Items.NETHER_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.NETHER_BRICK_SLAB);
/*      */             paramOutput.accept(Items.NETHER_BRICK_WALL);
/*      */             paramOutput.accept(Items.NETHER_BRICK_FENCE);
/*      */             paramOutput.accept(Items.CHISELED_NETHER_BRICKS);
/*      */             paramOutput.accept(Items.RED_NETHER_BRICKS);
/*      */             paramOutput.accept(Items.RED_NETHER_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.RED_NETHER_BRICK_SLAB);
/*      */             paramOutput.accept(Items.RED_NETHER_BRICK_WALL);
/*      */             paramOutput.accept(Items.BASALT);
/*      */             paramOutput.accept(Items.SMOOTH_BASALT);
/*      */             paramOutput.accept(Items.POLISHED_BASALT);
/*      */             paramOutput.accept(Items.BLACKSTONE);
/*      */             paramOutput.accept(Items.GILDED_BLACKSTONE);
/*      */             paramOutput.accept(Items.BLACKSTONE_STAIRS);
/*      */             paramOutput.accept(Items.BLACKSTONE_SLAB);
/*      */             paramOutput.accept(Items.BLACKSTONE_WALL);
/*      */             paramOutput.accept(Items.CHISELED_POLISHED_BLACKSTONE);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_SLAB);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_WALL);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_BUTTON);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_BRICKS);
/*      */             paramOutput.accept(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.POLISHED_BLACKSTONE_BRICK_WALL);
/*      */             paramOutput.accept(Items.END_STONE);
/*      */             paramOutput.accept(Items.END_STONE_BRICKS);
/*      */             paramOutput.accept(Items.END_STONE_BRICK_STAIRS);
/*      */             paramOutput.accept(Items.END_STONE_BRICK_SLAB);
/*      */             paramOutput.accept(Items.END_STONE_BRICK_WALL);
/*      */             paramOutput.accept(Items.PURPUR_BLOCK);
/*      */             paramOutput.accept(Items.PURPUR_PILLAR);
/*      */             paramOutput.accept(Items.PURPUR_STAIRS);
/*      */             paramOutput.accept(Items.PURPUR_SLAB);
/*      */             paramOutput.accept(Items.COAL_BLOCK);
/*      */             paramOutput.accept(Items.IRON_BLOCK);
/*      */             paramOutput.accept(Items.IRON_BARS);
/*      */             paramOutput.accept(Items.IRON_DOOR);
/*      */             paramOutput.accept(Items.IRON_TRAPDOOR);
/*      */             paramOutput.accept(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.IRON_CHAIN);
/*      */             paramOutput.accept(Items.GOLD_BLOCK);
/*      */             paramOutput.accept(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*      */             paramOutput.accept(Items.REDSTONE_BLOCK);
/*      */             paramOutput.accept(Items.EMERALD_BLOCK);
/*      */             paramOutput.accept(Items.LAPIS_BLOCK);
/*      */             paramOutput.accept(Items.DIAMOND_BLOCK);
/*      */             paramOutput.accept(Items.NETHERITE_BLOCK);
/*      */             paramOutput.accept(Items.QUARTZ_BLOCK);
/*      */             paramOutput.accept(Items.QUARTZ_STAIRS);
/*      */             paramOutput.accept(Items.QUARTZ_SLAB);
/*      */             paramOutput.accept(Items.CHISELED_QUARTZ_BLOCK);
/*      */             paramOutput.accept(Items.QUARTZ_BRICKS);
/*      */             paramOutput.accept(Items.QUARTZ_PILLAR);
/*      */             paramOutput.accept(Items.SMOOTH_QUARTZ);
/*      */             paramOutput.accept(Items.SMOOTH_QUARTZ_STAIRS);
/*      */             paramOutput.accept(Items.SMOOTH_QUARTZ_SLAB);
/*      */             paramOutput.accept(Items.AMETHYST_BLOCK);
/*      */             paramOutput.accept(Items.COPPER_BLOCK);
/*      */             paramOutput.accept(Items.CHISELED_COPPER);
/*      */             paramOutput.accept(Items.COPPER_GRATE);
/*      */             paramOutput.accept(Items.CUT_COPPER);
/*      */             paramOutput.accept(Items.CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.unaffected());
/*      */             paramOutput.accept(Items.COPPER_DOOR);
/*      */             paramOutput.accept(Items.COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.unaffected());
/*      */             paramOutput.accept(Items.EXPOSED_COPPER);
/*      */             paramOutput.accept(Items.EXPOSED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.EXPOSED_CUT_COPPER);
/*      */             paramOutput.accept(Items.EXPOSED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.EXPOSED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.exposed());
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.exposed());
/*      */             paramOutput.accept(Items.WEATHERED_COPPER);
/*      */             paramOutput.accept(Items.WEATHERED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.WEATHERED_CUT_COPPER);
/*      */             paramOutput.accept(Items.WEATHERED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.WEATHERED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.weathered());
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.weathered());
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER);
/*      */             paramOutput.accept(Items.OXIDIZED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.OXIDIZED_CUT_COPPER);
/*      */             paramOutput.accept(Items.OXIDIZED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.OXIDIZED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.oxidized());
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.oxidized());
/*      */             paramOutput.accept(Items.WAXED_COPPER_BLOCK);
/*      */             paramOutput.accept(Items.WAXED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.WAXED_CUT_COPPER);
/*      */             paramOutput.accept(Items.WAXED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.WAXED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.waxed());
/*      */             paramOutput.accept(Items.WAXED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.WAXED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.WAXED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.waxed());
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_CUT_COPPER);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.waxedExposed());
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.waxedExposed());
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_CUT_COPPER);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.waxedWeathered());
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.waxedWeathered());
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_CHISELED_COPPER);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_GRATE);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_CUT_COPPER);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
/*      */             paramOutput.accept(Items.COPPER_BARS.waxedOxidized());
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_DOOR);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/*      */             paramOutput.accept(Items.COPPER_CHAIN.waxedOxidized());
/*  568 */           }).build());
/*  569 */     Registry.register(paramRegistry, COLORED_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
/*  570 */         .title((Component)Component.translatable("itemGroup.coloredBlocks"))
/*  571 */         .icon(() -> new ItemStack((ItemLike)Blocks.CYAN_WOOL))
/*  572 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.WHITE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_GRAY_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.GRAY_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.BLACK_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.BROWN_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.RED_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.ORANGE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.YELLOW_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.LIME_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.GREEN_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.CYAN_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_BLUE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.BLUE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.PURPLE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.MAGENTA_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.PINK_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.WHITE_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_GRAY_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.GRAY_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.BLACK_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.BROWN_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.RED_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.ORANGE_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.YELLOW_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.LIME_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.GREEN_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.CYAN_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_BLUE_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.BLUE_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.PURPLE_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.MAGENTA_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.PINK_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.WHITE_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_GRAY_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.GRAY_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.BLACK_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.BROWN_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.RED_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.ORANGE_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.YELLOW_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.LIME_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.GREEN_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.CYAN_TERRACOTTA);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_BLUE_TERRACOTTA);
/*      */             paramOutput.accept(Items.BLUE_TERRACOTTA);
/*      */             paramOutput.accept(Items.PURPLE_TERRACOTTA);
/*      */             paramOutput.accept(Items.MAGENTA_TERRACOTTA);
/*      */             paramOutput.accept(Items.PINK_TERRACOTTA);
/*      */             paramOutput.accept(Items.WHITE_CONCRETE);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_CONCRETE);
/*      */             paramOutput.accept(Items.GRAY_CONCRETE);
/*      */             paramOutput.accept(Items.BLACK_CONCRETE);
/*      */             paramOutput.accept(Items.BROWN_CONCRETE);
/*      */             paramOutput.accept(Items.RED_CONCRETE);
/*      */             paramOutput.accept(Items.ORANGE_CONCRETE);
/*      */             paramOutput.accept(Items.YELLOW_CONCRETE);
/*      */             paramOutput.accept(Items.LIME_CONCRETE);
/*      */             paramOutput.accept(Items.GREEN_CONCRETE);
/*      */             paramOutput.accept(Items.CYAN_CONCRETE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_CONCRETE);
/*      */             paramOutput.accept(Items.BLUE_CONCRETE);
/*      */             paramOutput.accept(Items.PURPLE_CONCRETE);
/*      */             paramOutput.accept(Items.MAGENTA_CONCRETE);
/*      */             paramOutput.accept(Items.PINK_CONCRETE);
/*      */             paramOutput.accept(Items.WHITE_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.GRAY_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.BLACK_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.BROWN_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.RED_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.ORANGE_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.YELLOW_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.LIME_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.GREEN_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.CYAN_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.BLUE_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.PURPLE_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.MAGENTA_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.PINK_CONCRETE_POWDER);
/*      */             paramOutput.accept(Items.WHITE_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.GRAY_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.BLACK_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.BROWN_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.RED_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.ORANGE_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.YELLOW_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.LIME_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.GREEN_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.CYAN_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.BLUE_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.PURPLE_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.MAGENTA_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.PINK_GLAZED_TERRACOTTA);
/*      */             paramOutput.accept(Items.GLASS);
/*      */             paramOutput.accept(Items.TINTED_GLASS);
/*      */             paramOutput.accept(Items.WHITE_STAINED_GLASS);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_STAINED_GLASS);
/*      */             paramOutput.accept(Items.GRAY_STAINED_GLASS);
/*      */             paramOutput.accept(Items.BLACK_STAINED_GLASS);
/*      */             paramOutput.accept(Items.BROWN_STAINED_GLASS);
/*      */             paramOutput.accept(Items.RED_STAINED_GLASS);
/*      */             paramOutput.accept(Items.ORANGE_STAINED_GLASS);
/*      */             paramOutput.accept(Items.YELLOW_STAINED_GLASS);
/*      */             paramOutput.accept(Items.LIME_STAINED_GLASS);
/*      */             paramOutput.accept(Items.GREEN_STAINED_GLASS);
/*      */             paramOutput.accept(Items.CYAN_STAINED_GLASS);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_STAINED_GLASS);
/*      */             paramOutput.accept(Items.BLUE_STAINED_GLASS);
/*      */             paramOutput.accept(Items.PURPLE_STAINED_GLASS);
/*      */             paramOutput.accept(Items.MAGENTA_STAINED_GLASS);
/*      */             paramOutput.accept(Items.PINK_STAINED_GLASS);
/*      */             paramOutput.accept(Items.GLASS_PANE);
/*      */             paramOutput.accept(Items.WHITE_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.GRAY_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.BLACK_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.BROWN_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.RED_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.ORANGE_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.YELLOW_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.LIME_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.GREEN_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.CYAN_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.BLUE_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.PURPLE_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.MAGENTA_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.PINK_STAINED_GLASS_PANE);
/*      */             paramOutput.accept(Items.SHULKER_BOX);
/*      */             paramOutput.accept(Items.WHITE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_SHULKER_BOX);
/*      */             paramOutput.accept(Items.GRAY_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BLACK_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BROWN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.RED_SHULKER_BOX);
/*      */             paramOutput.accept(Items.ORANGE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.YELLOW_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIME_SHULKER_BOX);
/*      */             paramOutput.accept(Items.GREEN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.CYAN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BLUE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.PURPLE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.MAGENTA_SHULKER_BOX);
/*      */             paramOutput.accept(Items.PINK_SHULKER_BOX);
/*      */             paramOutput.accept(Items.WHITE_BED);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_BED);
/*      */             paramOutput.accept(Items.GRAY_BED);
/*      */             paramOutput.accept(Items.BLACK_BED);
/*      */             paramOutput.accept(Items.BROWN_BED);
/*      */             paramOutput.accept(Items.RED_BED);
/*      */             paramOutput.accept(Items.ORANGE_BED);
/*      */             paramOutput.accept(Items.YELLOW_BED);
/*      */             paramOutput.accept(Items.LIME_BED);
/*      */             paramOutput.accept(Items.GREEN_BED);
/*      */             paramOutput.accept(Items.CYAN_BED);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_BED);
/*      */             paramOutput.accept(Items.BLUE_BED);
/*      */             paramOutput.accept(Items.PURPLE_BED);
/*      */             paramOutput.accept(Items.MAGENTA_BED);
/*      */             paramOutput.accept(Items.PINK_BED);
/*      */             paramOutput.accept(Items.CANDLE);
/*      */             paramOutput.accept(Items.WHITE_CANDLE);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_CANDLE);
/*      */             paramOutput.accept(Items.GRAY_CANDLE);
/*      */             paramOutput.accept(Items.BLACK_CANDLE);
/*      */             paramOutput.accept(Items.BROWN_CANDLE);
/*      */             paramOutput.accept(Items.RED_CANDLE);
/*      */             paramOutput.accept(Items.ORANGE_CANDLE);
/*      */             paramOutput.accept(Items.YELLOW_CANDLE);
/*      */             paramOutput.accept(Items.LIME_CANDLE);
/*      */             paramOutput.accept(Items.GREEN_CANDLE);
/*      */             paramOutput.accept(Items.CYAN_CANDLE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_CANDLE);
/*      */             paramOutput.accept(Items.BLUE_CANDLE);
/*      */             paramOutput.accept(Items.PURPLE_CANDLE);
/*      */             paramOutput.accept(Items.MAGENTA_CANDLE);
/*      */             paramOutput.accept(Items.PINK_CANDLE);
/*      */             paramOutput.accept(Items.WHITE_BANNER);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_BANNER);
/*      */             paramOutput.accept(Items.GRAY_BANNER);
/*      */             paramOutput.accept(Items.BLACK_BANNER);
/*      */             paramOutput.accept(Items.BROWN_BANNER);
/*      */             paramOutput.accept(Items.RED_BANNER);
/*      */             paramOutput.accept(Items.ORANGE_BANNER);
/*      */             paramOutput.accept(Items.YELLOW_BANNER);
/*      */             paramOutput.accept(Items.LIME_BANNER);
/*      */             paramOutput.accept(Items.GREEN_BANNER);
/*      */             paramOutput.accept(Items.CYAN_BANNER);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_BANNER);
/*      */             paramOutput.accept(Items.BLUE_BANNER);
/*      */             paramOutput.accept(Items.PURPLE_BANNER);
/*      */             paramOutput.accept(Items.MAGENTA_BANNER);
/*      */             paramOutput.accept(Items.PINK_BANNER);
/*  815 */           }).build());
/*  816 */     Registry.register(paramRegistry, NATURAL_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
/*  817 */         .title((Component)Component.translatable("itemGroup.natural"))
/*  818 */         .icon(() -> new ItemStack((ItemLike)Blocks.GRASS_BLOCK))
/*  819 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.GRASS_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.PODZOL);
/*      */             
/*      */             paramOutput.accept(Items.MYCELIUM);
/*      */             
/*      */             paramOutput.accept(Items.DIRT_PATH);
/*      */             
/*      */             paramOutput.accept(Items.DIRT);
/*      */             
/*      */             paramOutput.accept(Items.COARSE_DIRT);
/*      */             
/*      */             paramOutput.accept(Items.ROOTED_DIRT);
/*      */             
/*      */             paramOutput.accept(Items.FARMLAND);
/*      */             
/*      */             paramOutput.accept(Items.MUD);
/*      */             
/*      */             paramOutput.accept(Items.CLAY);
/*      */             
/*      */             paramOutput.accept(Items.GRAVEL);
/*      */             
/*      */             paramOutput.accept(Items.SAND);
/*      */             
/*      */             paramOutput.accept(Items.SANDSTONE);
/*      */             
/*      */             paramOutput.accept(Items.RED_SAND);
/*      */             
/*      */             paramOutput.accept(Items.RED_SANDSTONE);
/*      */             
/*      */             paramOutput.accept(Items.ICE);
/*      */             
/*      */             paramOutput.accept(Items.PACKED_ICE);
/*      */             
/*      */             paramOutput.accept(Items.BLUE_ICE);
/*      */             
/*      */             paramOutput.accept(Items.SNOW_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.SNOW);
/*      */             
/*      */             paramOutput.accept(Items.MOSS_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.MOSS_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.PALE_MOSS_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.PALE_MOSS_CARPET);
/*      */             
/*      */             paramOutput.accept(Items.PALE_HANGING_MOSS);
/*      */             
/*      */             paramOutput.accept(Items.STONE);
/*      */             
/*      */             paramOutput.accept(Items.DEEPSLATE);
/*      */             
/*      */             paramOutput.accept(Items.GRANITE);
/*      */             
/*      */             paramOutput.accept(Items.DIORITE);
/*      */             
/*      */             paramOutput.accept(Items.ANDESITE);
/*      */             
/*      */             paramOutput.accept(Items.CALCITE);
/*      */             
/*      */             paramOutput.accept(Items.TUFF);
/*      */             
/*      */             paramOutput.accept(Items.DRIPSTONE_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.POINTED_DRIPSTONE);
/*      */             
/*      */             paramOutput.accept(Items.PRISMARINE);
/*      */             
/*      */             paramOutput.accept(Items.MAGMA_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.OBSIDIAN);
/*      */             
/*      */             paramOutput.accept(Items.CRYING_OBSIDIAN);
/*      */             
/*      */             paramOutput.accept(Items.NETHERRACK);
/*      */             
/*      */             paramOutput.accept(Items.CRIMSON_NYLIUM);
/*      */             
/*      */             paramOutput.accept(Items.WARPED_NYLIUM);
/*      */             
/*      */             paramOutput.accept(Items.SOUL_SAND);
/*      */             
/*      */             paramOutput.accept(Items.SOUL_SOIL);
/*      */             
/*      */             paramOutput.accept(Items.BONE_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.BLACKSTONE);
/*      */             
/*      */             paramOutput.accept(Items.BASALT);
/*      */             
/*      */             paramOutput.accept(Items.SMOOTH_BASALT);
/*      */             
/*      */             paramOutput.accept(Items.END_STONE);
/*      */             
/*      */             paramOutput.accept(Items.COAL_ORE);
/*      */             
/*      */             paramOutput.accept(Items.DEEPSLATE_COAL_ORE);
/*      */             
/*      */             paramOutput.accept(Items.IRON_ORE);
/*      */             
/*      */             paramOutput.accept(Items.DEEPSLATE_IRON_ORE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_ORE);
/*      */             
/*      */             paramOutput.accept(Items.DEEPSLATE_COPPER_ORE);
/*      */             
/*      */             paramOutput.accept(Items.GOLD_ORE);
/*      */             
/*      */             paramOutput.accept(Items.DEEPSLATE_GOLD_ORE);
/*      */             paramOutput.accept(Items.REDSTONE_ORE);
/*      */             paramOutput.accept(Items.DEEPSLATE_REDSTONE_ORE);
/*      */             paramOutput.accept(Items.EMERALD_ORE);
/*      */             paramOutput.accept(Items.DEEPSLATE_EMERALD_ORE);
/*      */             paramOutput.accept(Items.LAPIS_ORE);
/*      */             paramOutput.accept(Items.DEEPSLATE_LAPIS_ORE);
/*      */             paramOutput.accept(Items.DIAMOND_ORE);
/*      */             paramOutput.accept(Items.DEEPSLATE_DIAMOND_ORE);
/*      */             paramOutput.accept(Items.NETHER_GOLD_ORE);
/*      */             paramOutput.accept(Items.NETHER_QUARTZ_ORE);
/*      */             paramOutput.accept(Items.ANCIENT_DEBRIS);
/*      */             paramOutput.accept(Items.RAW_IRON_BLOCK);
/*      */             paramOutput.accept(Items.RAW_COPPER_BLOCK);
/*      */             paramOutput.accept(Items.RAW_GOLD_BLOCK);
/*      */             paramOutput.accept(Items.GLOWSTONE);
/*      */             paramOutput.accept(Items.AMETHYST_BLOCK);
/*      */             paramOutput.accept(Items.BUDDING_AMETHYST);
/*      */             paramOutput.accept(Items.SMALL_AMETHYST_BUD);
/*      */             paramOutput.accept(Items.MEDIUM_AMETHYST_BUD);
/*      */             paramOutput.accept(Items.LARGE_AMETHYST_BUD);
/*      */             paramOutput.accept(Items.AMETHYST_CLUSTER);
/*      */             paramOutput.accept(Items.OAK_LOG);
/*      */             paramOutput.accept(Items.SPRUCE_LOG);
/*      */             paramOutput.accept(Items.BIRCH_LOG);
/*      */             paramOutput.accept(Items.JUNGLE_LOG);
/*      */             paramOutput.accept(Items.ACACIA_LOG);
/*      */             paramOutput.accept(Items.DARK_OAK_LOG);
/*      */             paramOutput.accept(Items.MANGROVE_LOG);
/*      */             paramOutput.accept(Items.MANGROVE_ROOTS);
/*      */             paramOutput.accept(Items.MUDDY_MANGROVE_ROOTS);
/*      */             paramOutput.accept(Items.CHERRY_LOG);
/*      */             paramOutput.accept(Items.PALE_OAK_LOG);
/*      */             paramOutput.accept(Items.MUSHROOM_STEM);
/*      */             paramOutput.accept(Items.CRIMSON_STEM);
/*      */             paramOutput.accept(Items.WARPED_STEM);
/*      */             paramOutput.accept(Items.OAK_LEAVES);
/*      */             paramOutput.accept(Items.SPRUCE_LEAVES);
/*      */             paramOutput.accept(Items.BIRCH_LEAVES);
/*      */             paramOutput.accept(Items.JUNGLE_LEAVES);
/*      */             paramOutput.accept(Items.ACACIA_LEAVES);
/*      */             paramOutput.accept(Items.DARK_OAK_LEAVES);
/*      */             paramOutput.accept(Items.MANGROVE_LEAVES);
/*      */             paramOutput.accept(Items.CHERRY_LEAVES);
/*      */             paramOutput.accept(Items.PALE_OAK_LEAVES);
/*      */             paramOutput.accept(Items.AZALEA_LEAVES);
/*      */             paramOutput.accept(Items.FLOWERING_AZALEA_LEAVES);
/*      */             paramOutput.accept(Items.BROWN_MUSHROOM_BLOCK);
/*      */             paramOutput.accept(Items.RED_MUSHROOM_BLOCK);
/*      */             paramOutput.accept(Items.NETHER_WART_BLOCK);
/*      */             paramOutput.accept(Items.WARPED_WART_BLOCK);
/*      */             paramOutput.accept(Items.SHROOMLIGHT);
/*      */             paramOutput.accept(Items.OAK_SAPLING);
/*      */             paramOutput.accept(Items.SPRUCE_SAPLING);
/*      */             paramOutput.accept(Items.BIRCH_SAPLING);
/*      */             paramOutput.accept(Items.JUNGLE_SAPLING);
/*      */             paramOutput.accept(Items.ACACIA_SAPLING);
/*      */             paramOutput.accept(Items.DARK_OAK_SAPLING);
/*      */             paramOutput.accept(Items.MANGROVE_PROPAGULE);
/*      */             paramOutput.accept(Items.CHERRY_SAPLING);
/*      */             paramOutput.accept(Items.PALE_OAK_SAPLING);
/*      */             paramOutput.accept(Items.AZALEA);
/*      */             paramOutput.accept(Items.FLOWERING_AZALEA);
/*      */             paramOutput.accept(Items.BROWN_MUSHROOM);
/*      */             paramOutput.accept(Items.RED_MUSHROOM);
/*      */             paramOutput.accept(Items.CRIMSON_FUNGUS);
/*      */             paramOutput.accept(Items.WARPED_FUNGUS);
/*      */             paramOutput.accept(Items.SHORT_GRASS);
/*      */             paramOutput.accept(Items.FERN);
/*      */             paramOutput.accept(Items.DRY_SHORT_GRASS);
/*      */             paramOutput.accept(Items.BUSH);
/*      */             paramOutput.accept(Items.DEAD_BUSH);
/*      */             paramOutput.accept(Items.DANDELION);
/*      */             paramOutput.accept(Items.POPPY);
/*      */             paramOutput.accept(Items.BLUE_ORCHID);
/*      */             paramOutput.accept(Items.ALLIUM);
/*      */             paramOutput.accept(Items.AZURE_BLUET);
/*      */             paramOutput.accept(Items.RED_TULIP);
/*      */             paramOutput.accept(Items.ORANGE_TULIP);
/*      */             paramOutput.accept(Items.WHITE_TULIP);
/*      */             paramOutput.accept(Items.PINK_TULIP);
/*      */             paramOutput.accept(Items.OXEYE_DAISY);
/*      */             paramOutput.accept(Items.CORNFLOWER);
/*      */             paramOutput.accept(Items.LILY_OF_THE_VALLEY);
/*      */             paramOutput.accept(Items.TORCHFLOWER);
/*      */             paramOutput.accept(Items.CACTUS_FLOWER);
/*      */             paramOutput.accept(Items.CLOSED_EYEBLOSSOM);
/*      */             paramOutput.accept(Items.OPEN_EYEBLOSSOM);
/*      */             paramOutput.accept(Items.WITHER_ROSE);
/*      */             paramOutput.accept(Items.PINK_PETALS);
/*      */             paramOutput.accept(Items.WILDFLOWERS);
/*      */             paramOutput.accept(Items.LEAF_LITTER);
/*      */             paramOutput.accept(Items.SPORE_BLOSSOM);
/*      */             paramOutput.accept(Items.FIREFLY_BUSH);
/*      */             paramOutput.accept(Items.BAMBOO);
/*      */             paramOutput.accept(Items.SUGAR_CANE);
/*      */             paramOutput.accept(Items.CACTUS);
/*      */             paramOutput.accept(Items.CRIMSON_ROOTS);
/*      */             paramOutput.accept(Items.WARPED_ROOTS);
/*      */             paramOutput.accept(Items.NETHER_SPROUTS);
/*      */             paramOutput.accept(Items.WEEPING_VINES);
/*      */             paramOutput.accept(Items.TWISTING_VINES);
/*      */             paramOutput.accept(Items.VINE);
/*      */             paramOutput.accept(Items.TALL_GRASS);
/*      */             paramOutput.accept(Items.LARGE_FERN);
/*      */             paramOutput.accept(Items.DRY_TALL_GRASS);
/*      */             paramOutput.accept(Items.SUNFLOWER);
/*      */             paramOutput.accept(Items.LILAC);
/*      */             paramOutput.accept(Items.ROSE_BUSH);
/*      */             paramOutput.accept(Items.PEONY);
/*      */             paramOutput.accept(Items.PITCHER_PLANT);
/*      */             paramOutput.accept(Items.BIG_DRIPLEAF);
/*      */             paramOutput.accept(Items.SMALL_DRIPLEAF);
/*      */             paramOutput.accept(Items.CHORUS_PLANT);
/*      */             paramOutput.accept(Items.CHORUS_FLOWER);
/*      */             paramOutput.accept(Items.GLOW_LICHEN);
/*      */             paramOutput.accept(Items.HANGING_ROOTS);
/*      */             paramOutput.accept(Items.FROGSPAWN);
/*      */             paramOutput.accept(Items.TURTLE_EGG);
/*      */             paramOutput.accept(Items.SNIFFER_EGG);
/*      */             paramOutput.accept(Items.DRIED_GHAST);
/*      */             paramOutput.accept(Items.WHEAT_SEEDS);
/*      */             paramOutput.accept(Items.COCOA_BEANS);
/*      */             paramOutput.accept(Items.PUMPKIN_SEEDS);
/*      */             paramOutput.accept(Items.MELON_SEEDS);
/*      */             paramOutput.accept(Items.BEETROOT_SEEDS);
/*      */             paramOutput.accept(Items.TORCHFLOWER_SEEDS);
/*      */             paramOutput.accept(Items.PITCHER_POD);
/*      */             paramOutput.accept(Items.GLOW_BERRIES);
/*      */             paramOutput.accept(Items.SWEET_BERRIES);
/*      */             paramOutput.accept(Items.NETHER_WART);
/*      */             paramOutput.accept(Items.LILY_PAD);
/*      */             paramOutput.accept(Items.SEAGRASS);
/*      */             paramOutput.accept(Items.SEA_PICKLE);
/*      */             paramOutput.accept(Items.KELP);
/*      */             paramOutput.accept(Items.DRIED_KELP_BLOCK);
/*      */             paramOutput.accept(Items.TUBE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.BRAIN_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.BUBBLE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.FIRE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.HORN_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.DEAD_TUBE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.DEAD_BRAIN_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.DEAD_BUBBLE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.DEAD_FIRE_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.DEAD_HORN_CORAL_BLOCK);
/*      */             paramOutput.accept(Items.TUBE_CORAL);
/*      */             paramOutput.accept(Items.BRAIN_CORAL);
/*      */             paramOutput.accept(Items.BUBBLE_CORAL);
/*      */             paramOutput.accept(Items.FIRE_CORAL);
/*      */             paramOutput.accept(Items.HORN_CORAL);
/*      */             paramOutput.accept(Items.DEAD_TUBE_CORAL);
/*      */             paramOutput.accept(Items.DEAD_BRAIN_CORAL);
/*      */             paramOutput.accept(Items.DEAD_BUBBLE_CORAL);
/*      */             paramOutput.accept(Items.DEAD_FIRE_CORAL);
/*      */             paramOutput.accept(Items.DEAD_HORN_CORAL);
/*      */             paramOutput.accept(Items.TUBE_CORAL_FAN);
/*      */             paramOutput.accept(Items.BRAIN_CORAL_FAN);
/*      */             paramOutput.accept(Items.BUBBLE_CORAL_FAN);
/*      */             paramOutput.accept(Items.FIRE_CORAL_FAN);
/*      */             paramOutput.accept(Items.HORN_CORAL_FAN);
/*      */             paramOutput.accept(Items.DEAD_TUBE_CORAL_FAN);
/*      */             paramOutput.accept(Items.DEAD_BRAIN_CORAL_FAN);
/*      */             paramOutput.accept(Items.DEAD_BUBBLE_CORAL_FAN);
/*      */             paramOutput.accept(Items.DEAD_FIRE_CORAL_FAN);
/*      */             paramOutput.accept(Items.DEAD_HORN_CORAL_FAN);
/*      */             paramOutput.accept(Items.SPONGE);
/*      */             paramOutput.accept(Items.WET_SPONGE);
/*      */             paramOutput.accept(Items.MELON);
/*      */             paramOutput.accept(Items.PUMPKIN);
/*      */             paramOutput.accept(Items.CARVED_PUMPKIN);
/*      */             paramOutput.accept(Items.JACK_O_LANTERN);
/*      */             paramOutput.accept(Items.HAY_BLOCK);
/*      */             paramOutput.accept(Items.BEE_NEST);
/*      */             paramOutput.accept(Items.HONEYCOMB_BLOCK);
/*      */             paramOutput.accept(Items.SLIME_BLOCK);
/*      */             paramOutput.accept(Items.HONEY_BLOCK);
/*      */             paramOutput.accept(Items.RESIN_BLOCK);
/*      */             paramOutput.accept(Items.OCHRE_FROGLIGHT);
/*      */             paramOutput.accept(Items.VERDANT_FROGLIGHT);
/*      */             paramOutput.accept(Items.PEARLESCENT_FROGLIGHT);
/*      */             paramOutput.accept(Items.SCULK);
/*      */             paramOutput.accept(Items.SCULK_VEIN);
/*      */             paramOutput.accept(Items.SCULK_CATALYST);
/*      */             paramOutput.accept(Items.SCULK_SHRIEKER);
/*      */             paramOutput.accept(Items.SCULK_SENSOR);
/*      */             paramOutput.accept(Items.COBWEB);
/*      */             paramOutput.accept(Items.BEDROCK);
/* 1118 */           }).build());
/* 1119 */     Registry.register(paramRegistry, FUNCTIONAL_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
/* 1120 */         .title((Component)Component.translatable("itemGroup.functional"))
/* 1121 */         .icon(() -> new ItemStack(Items.OAK_SIGN))
/* 1122 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.TORCH);
/*      */             
/*      */             paramOutput.accept(Items.SOUL_TORCH);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_TORCH);
/*      */             
/*      */             paramOutput.accept(Items.REDSTONE_TORCH);
/*      */             
/*      */             paramOutput.accept(Items.LANTERN);
/*      */             
/*      */             paramOutput.accept(Items.SOUL_LANTERN);
/*      */             
/*      */             Objects.requireNonNull(paramOutput);
/*      */             
/*      */             Items.COPPER_LANTERN.forEach(paramOutput::accept);
/*      */             
/*      */             paramOutput.accept(Items.IRON_CHAIN);
/*      */             
/*      */             Objects.requireNonNull(paramOutput);
/*      */             
/*      */             Items.COPPER_CHAIN.forEach(paramOutput::accept);
/*      */             
/*      */             paramOutput.accept(Items.END_ROD);
/*      */             
/*      */             paramOutput.accept(Items.SEA_LANTERN);
/*      */             
/*      */             paramOutput.accept(Items.REDSTONE_LAMP);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.GLOWSTONE);
/*      */             
/*      */             paramOutput.accept(Items.SHROOMLIGHT);
/*      */             
/*      */             paramOutput.accept(Items.OCHRE_FROGLIGHT);
/*      */             
/*      */             paramOutput.accept(Items.VERDANT_FROGLIGHT);
/*      */             
/*      */             paramOutput.accept(Items.PEARLESCENT_FROGLIGHT);
/*      */             
/*      */             paramOutput.accept(Items.CRYING_OBSIDIAN);
/*      */             
/*      */             paramOutput.accept(Items.GLOW_LICHEN);
/*      */             
/*      */             paramOutput.accept(Items.MAGMA_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.CRAFTING_TABLE);
/*      */             
/*      */             paramOutput.accept(Items.STONECUTTER);
/*      */             
/*      */             paramOutput.accept(Items.CARTOGRAPHY_TABLE);
/*      */             
/*      */             paramOutput.accept(Items.FLETCHING_TABLE);
/*      */             
/*      */             paramOutput.accept(Items.SMITHING_TABLE);
/*      */             
/*      */             paramOutput.accept(Items.GRINDSTONE);
/*      */             
/*      */             paramOutput.accept(Items.LOOM);
/*      */             
/*      */             paramOutput.accept(Items.FURNACE);
/*      */             
/*      */             paramOutput.accept(Items.SMOKER);
/*      */             
/*      */             paramOutput.accept(Items.BLAST_FURNACE);
/*      */             
/*      */             paramOutput.accept(Items.CAMPFIRE);
/*      */             
/*      */             paramOutput.accept(Items.SOUL_CAMPFIRE);
/*      */             
/*      */             paramOutput.accept(Items.ANVIL);
/*      */             
/*      */             paramOutput.accept(Items.CHIPPED_ANVIL);
/*      */             
/*      */             paramOutput.accept(Items.DAMAGED_ANVIL);
/*      */             
/*      */             paramOutput.accept(Items.COMPOSTER);
/*      */             
/*      */             paramOutput.accept(Items.NOTE_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.JUKEBOX);
/*      */             
/*      */             paramOutput.accept(Items.ENCHANTING_TABLE);
/*      */             
/*      */             paramOutput.accept(Items.END_CRYSTAL);
/*      */             
/*      */             paramOutput.accept(Items.BREWING_STAND);
/*      */             paramOutput.accept(Items.CAULDRON);
/*      */             paramOutput.accept(Items.BELL);
/*      */             paramOutput.accept(Items.BEACON);
/*      */             paramOutput.accept(Items.CONDUIT);
/*      */             paramOutput.accept(Items.LODESTONE);
/*      */             paramOutput.accept(Items.LADDER);
/*      */             paramOutput.accept(Items.SCAFFOLDING);
/*      */             paramOutput.accept(Items.BEE_NEST);
/*      */             paramOutput.accept(Items.BEEHIVE);
/*      */             paramOutput.accept(Items.SUSPICIOUS_SAND);
/*      */             paramOutput.accept(Items.SUSPICIOUS_GRAVEL);
/*      */             paramOutput.accept(Items.LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.EXPOSED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.WEATHERED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.OXIDIZED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.WAXED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_LIGHTNING_ROD);
/*      */             paramOutput.accept(Items.FLOWER_POT);
/*      */             paramOutput.accept(Items.DECORATED_POT);
/*      */             paramOutput.accept(Items.ARMOR_STAND);
/*      */             paramOutput.accept(Items.ITEM_FRAME);
/*      */             paramOutput.accept(Items.GLOW_ITEM_FRAME);
/*      */             paramOutput.accept(Items.PAINTING);
/*      */             paramItemDisplayParameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent(());
/*      */             paramOutput.accept(Items.BOOKSHELF);
/*      */             paramOutput.accept(Items.CHISELED_BOOKSHELF);
/*      */             paramOutput.accept(Items.OAK_SHELF);
/*      */             paramOutput.accept(Items.SPRUCE_SHELF);
/*      */             paramOutput.accept(Items.BIRCH_SHELF);
/*      */             paramOutput.accept(Items.JUNGLE_SHELF);
/*      */             paramOutput.accept(Items.ACACIA_SHELF);
/*      */             paramOutput.accept(Items.DARK_OAK_SHELF);
/*      */             paramOutput.accept(Items.MANGROVE_SHELF);
/*      */             paramOutput.accept(Items.CHERRY_SHELF);
/*      */             paramOutput.accept(Items.PALE_OAK_SHELF);
/*      */             paramOutput.accept(Items.BAMBOO_SHELF);
/*      */             paramOutput.accept(Items.CRIMSON_SHELF);
/*      */             paramOutput.accept(Items.WARPED_SHELF);
/*      */             paramOutput.accept(Items.LECTERN);
/*      */             paramOutput.accept(Items.TINTED_GLASS);
/*      */             paramOutput.accept(Items.OAK_SIGN);
/*      */             paramOutput.accept(Items.OAK_HANGING_SIGN);
/*      */             paramOutput.accept(Items.SPRUCE_SIGN);
/*      */             paramOutput.accept(Items.SPRUCE_HANGING_SIGN);
/*      */             paramOutput.accept(Items.BIRCH_SIGN);
/*      */             paramOutput.accept(Items.BIRCH_HANGING_SIGN);
/*      */             paramOutput.accept(Items.JUNGLE_SIGN);
/*      */             paramOutput.accept(Items.JUNGLE_HANGING_SIGN);
/*      */             paramOutput.accept(Items.ACACIA_SIGN);
/*      */             paramOutput.accept(Items.ACACIA_HANGING_SIGN);
/*      */             paramOutput.accept(Items.DARK_OAK_SIGN);
/*      */             paramOutput.accept(Items.DARK_OAK_HANGING_SIGN);
/*      */             paramOutput.accept(Items.MANGROVE_SIGN);
/*      */             paramOutput.accept(Items.MANGROVE_HANGING_SIGN);
/*      */             paramOutput.accept(Items.CHERRY_SIGN);
/*      */             paramOutput.accept(Items.CHERRY_HANGING_SIGN);
/*      */             paramOutput.accept(Items.PALE_OAK_SIGN);
/*      */             paramOutput.accept(Items.PALE_OAK_HANGING_SIGN);
/*      */             paramOutput.accept(Items.BAMBOO_SIGN);
/*      */             paramOutput.accept(Items.BAMBOO_HANGING_SIGN);
/*      */             paramOutput.accept(Items.CRIMSON_SIGN);
/*      */             paramOutput.accept(Items.CRIMSON_HANGING_SIGN);
/*      */             paramOutput.accept(Items.WARPED_SIGN);
/*      */             paramOutput.accept(Items.WARPED_HANGING_SIGN);
/*      */             paramOutput.accept(Items.CHEST);
/*      */             paramOutput.accept(Items.COPPER_CHEST);
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.WAXED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.BARREL);
/*      */             paramOutput.accept(Items.ENDER_CHEST);
/*      */             paramOutput.accept(Items.SHULKER_BOX);
/*      */             paramOutput.accept(Items.WHITE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_SHULKER_BOX);
/*      */             paramOutput.accept(Items.GRAY_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BLACK_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BROWN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.RED_SHULKER_BOX);
/*      */             paramOutput.accept(Items.ORANGE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.YELLOW_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIME_SHULKER_BOX);
/*      */             paramOutput.accept(Items.GREEN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.CYAN_SHULKER_BOX);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.BLUE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.PURPLE_SHULKER_BOX);
/*      */             paramOutput.accept(Items.MAGENTA_SHULKER_BOX);
/*      */             paramOutput.accept(Items.PINK_SHULKER_BOX);
/*      */             paramOutput.accept(Items.RESPAWN_ANCHOR);
/*      */             paramOutput.accept(Items.WHITE_BED);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_BED);
/*      */             paramOutput.accept(Items.GRAY_BED);
/*      */             paramOutput.accept(Items.BLACK_BED);
/*      */             paramOutput.accept(Items.BROWN_BED);
/*      */             paramOutput.accept(Items.RED_BED);
/*      */             paramOutput.accept(Items.ORANGE_BED);
/*      */             paramOutput.accept(Items.YELLOW_BED);
/*      */             paramOutput.accept(Items.LIME_BED);
/*      */             paramOutput.accept(Items.GREEN_BED);
/*      */             paramOutput.accept(Items.CYAN_BED);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_BED);
/*      */             paramOutput.accept(Items.BLUE_BED);
/*      */             paramOutput.accept(Items.PURPLE_BED);
/*      */             paramOutput.accept(Items.MAGENTA_BED);
/*      */             paramOutput.accept(Items.PINK_BED);
/*      */             paramOutput.accept(Items.CANDLE);
/*      */             paramOutput.accept(Items.WHITE_CANDLE);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_CANDLE);
/*      */             paramOutput.accept(Items.GRAY_CANDLE);
/*      */             paramOutput.accept(Items.BLACK_CANDLE);
/*      */             paramOutput.accept(Items.BROWN_CANDLE);
/*      */             paramOutput.accept(Items.RED_CANDLE);
/*      */             paramOutput.accept(Items.ORANGE_CANDLE);
/*      */             paramOutput.accept(Items.YELLOW_CANDLE);
/*      */             paramOutput.accept(Items.LIME_CANDLE);
/*      */             paramOutput.accept(Items.GREEN_CANDLE);
/*      */             paramOutput.accept(Items.CYAN_CANDLE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_CANDLE);
/*      */             paramOutput.accept(Items.BLUE_CANDLE);
/*      */             paramOutput.accept(Items.PURPLE_CANDLE);
/*      */             paramOutput.accept(Items.MAGENTA_CANDLE);
/*      */             paramOutput.accept(Items.PINK_CANDLE);
/*      */             paramOutput.accept(Items.WHITE_BANNER);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_BANNER);
/*      */             paramOutput.accept(Items.GRAY_BANNER);
/*      */             paramOutput.accept(Items.BLACK_BANNER);
/*      */             paramOutput.accept(Items.BROWN_BANNER);
/*      */             paramOutput.accept(Items.RED_BANNER);
/*      */             paramOutput.accept(Items.ORANGE_BANNER);
/*      */             paramOutput.accept(Items.YELLOW_BANNER);
/*      */             paramOutput.accept(Items.LIME_BANNER);
/*      */             paramOutput.accept(Items.GREEN_BANNER);
/*      */             paramOutput.accept(Items.CYAN_BANNER);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_BANNER);
/*      */             paramOutput.accept(Items.BLUE_BANNER);
/*      */             paramOutput.accept(Items.PURPLE_BANNER);
/*      */             paramOutput.accept(Items.MAGENTA_BANNER);
/*      */             paramOutput.accept(Items.PINK_BANNER);
/*      */             paramOutput.accept(Raid.getOminousBannerInstance((HolderGetter)paramItemDisplayParameters.holders().lookupOrThrow(Registries.BANNER_PATTERN)));
/*      */             paramOutput.accept(Items.SKELETON_SKULL);
/*      */             paramOutput.accept(Items.WITHER_SKELETON_SKULL);
/*      */             paramOutput.accept(Items.PLAYER_HEAD);
/*      */             paramOutput.accept(Items.ZOMBIE_HEAD);
/*      */             paramOutput.accept(Items.CREEPER_HEAD);
/*      */             paramOutput.accept(Items.PIGLIN_HEAD);
/*      */             paramOutput.accept(Items.DRAGON_HEAD);
/*      */             paramOutput.accept(Items.DRAGON_EGG);
/*      */             paramOutput.accept(Items.END_PORTAL_FRAME);
/*      */             paramOutput.accept(Items.VAULT);
/*      */             paramOutput.accept(Items.ENDER_EYE);
/*      */             paramOutput.accept(Items.COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.EXPOSED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.WEATHERED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.OXIDIZED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.WAXED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_GOLEM_STATUE);
/*      */             paramOutput.accept(Items.INFESTED_STONE);
/*      */             paramOutput.accept(Items.INFESTED_COBBLESTONE);
/*      */             paramOutput.accept(Items.INFESTED_STONE_BRICKS);
/*      */             paramOutput.accept(Items.INFESTED_MOSSY_STONE_BRICKS);
/*      */             paramOutput.accept(Items.INFESTED_CRACKED_STONE_BRICKS);
/*      */             paramOutput.accept(Items.INFESTED_CHISELED_STONE_BRICKS);
/*      */             paramOutput.accept(Items.INFESTED_DEEPSLATE);
/* 1395 */           }).build());
/* 1396 */     Registry.register(paramRegistry, REDSTONE_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 4)
/* 1397 */         .title((Component)Component.translatable("itemGroup.redstone"))
/* 1398 */         .icon(() -> new ItemStack(Items.REDSTONE))
/* 1399 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.REDSTONE);
/*      */             
/*      */             paramOutput.accept(Items.REDSTONE_TORCH);
/*      */             
/*      */             paramOutput.accept(Items.REDSTONE_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.REPEATER);
/*      */             
/*      */             paramOutput.accept(Items.COMPARATOR);
/*      */             
/*      */             paramOutput.accept(Items.TARGET);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/*      */             
/*      */             paramOutput.accept(Items.LEVER);
/*      */             
/*      */             paramOutput.accept(Items.OAK_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.STONE_BUTTON);
/*      */             
/*      */             paramOutput.accept(Items.OAK_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.STONE_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*      */             
/*      */             paramOutput.accept(Items.SCULK_SENSOR);
/*      */             
/*      */             paramOutput.accept(Items.CALIBRATED_SCULK_SENSOR);
/*      */             
/*      */             paramOutput.accept(Items.SCULK_SHRIEKER);
/*      */             
/*      */             paramOutput.accept(Items.AMETHYST_BLOCK);
/*      */             
/*      */             paramOutput.accept(Items.WHITE_WOOL);
/*      */             
/*      */             paramOutput.accept(Items.TRIPWIRE_HOOK);
/*      */             
/*      */             paramOutput.accept(Items.STRING);
/*      */             
/*      */             paramOutput.accept(Items.LECTERN);
/*      */             
/*      */             paramOutput.accept(Items.DAYLIGHT_DETECTOR);
/*      */             
/*      */             paramOutput.accept(Items.WAXED_LIGHTNING_ROD);
/*      */             
/*      */             paramOutput.accept(Items.PISTON);
/*      */             paramOutput.accept(Items.STICKY_PISTON);
/*      */             paramOutput.accept(Items.SLIME_BLOCK);
/*      */             paramOutput.accept(Items.HONEY_BLOCK);
/*      */             paramOutput.accept(Items.DISPENSER);
/*      */             paramOutput.accept(Items.DROPPER);
/*      */             paramOutput.accept(Items.CRAFTER);
/*      */             paramOutput.accept(Items.HOPPER);
/*      */             paramOutput.accept(Items.CHEST);
/*      */             paramOutput.accept(Items.WAXED_COPPER_CHEST);
/*      */             paramOutput.accept(Items.BARREL);
/*      */             paramOutput.accept(Items.CHISELED_BOOKSHELF);
/*      */             paramOutput.accept(Items.OAK_SHELF);
/*      */             paramOutput.accept(Items.FURNACE);
/*      */             paramOutput.accept(Items.TRAPPED_CHEST);
/*      */             paramOutput.accept(Items.JUKEBOX);
/*      */             paramOutput.accept(Items.DECORATED_POT);
/*      */             paramOutput.accept(Items.OBSERVER);
/*      */             paramOutput.accept(Items.NOTE_BLOCK);
/*      */             paramOutput.accept(Items.COMPOSTER);
/*      */             paramOutput.accept(Items.CAULDRON);
/*      */             paramOutput.accept(Items.RAIL);
/*      */             paramOutput.accept(Items.POWERED_RAIL);
/*      */             paramOutput.accept(Items.DETECTOR_RAIL);
/*      */             paramOutput.accept(Items.ACTIVATOR_RAIL);
/*      */             paramOutput.accept(Items.MINECART);
/*      */             paramOutput.accept(Items.HOPPER_MINECART);
/*      */             paramOutput.accept(Items.CHEST_MINECART);
/*      */             paramOutput.accept(Items.FURNACE_MINECART);
/*      */             paramOutput.accept(Items.TNT_MINECART);
/*      */             paramOutput.accept(Items.OAK_CHEST_BOAT);
/*      */             paramOutput.accept(Items.BAMBOO_CHEST_RAFT);
/*      */             paramOutput.accept(Items.OAK_DOOR);
/*      */             paramOutput.accept(Items.IRON_DOOR);
/*      */             paramOutput.accept(Items.OAK_FENCE_GATE);
/*      */             paramOutput.accept(Items.OAK_TRAPDOOR);
/*      */             paramOutput.accept(Items.IRON_TRAPDOOR);
/*      */             paramOutput.accept(Items.TNT);
/*      */             paramOutput.accept(Items.REDSTONE_LAMP);
/*      */             paramOutput.accept(Items.BELL);
/*      */             paramOutput.accept(Items.BIG_DRIPLEAF);
/*      */             paramOutput.accept(Items.ARMOR_STAND);
/*      */             paramOutput.accept(Items.REDSTONE_ORE);
/* 1497 */           }).build());
/* 1498 */     Registry.register(paramRegistry, HOTBAR, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 5)
/* 1499 */         .title((Component)Component.translatable("itemGroup.hotbar"))
/* 1500 */         .icon(() -> new ItemStack((ItemLike)Blocks.BOOKSHELF))
/* 1501 */         .alignedRight()
/* 1502 */         .type(CreativeModeTab.Type.HOTBAR)
/* 1503 */         .build());
/* 1504 */     Registry.register(paramRegistry, SEARCH, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 6)
/* 1505 */         .title((Component)Component.translatable("itemGroup.search"))
/* 1506 */         .icon(() -> new ItemStack(Items.COMPASS))
/* 1507 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
/*      */             
/*      */             for (CreativeModeTab creativeModeTab : paramRegistry) {
/*      */               if (creativeModeTab.getType() != CreativeModeTab.Type.SEARCH) {
/*      */                 set.addAll(creativeModeTab.getSearchTabDisplayItems());
/*      */               }
/*      */             } 
/*      */             paramOutput.acceptAll(set);
/* 1516 */           }).backgroundTexture(SEARCH_BACKGROUND)
/* 1517 */         .alignedRight()
/* 1518 */         .type(CreativeModeTab.Type.SEARCH)
/* 1519 */         .build());
/* 1520 */     Registry.register(paramRegistry, TOOLS_AND_UTILITIES, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
/* 1521 */         .title((Component)Component.translatable("itemGroup.tools"))
/* 1522 */         .icon(() -> new ItemStack(Items.DIAMOND_PICKAXE))
/* 1523 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.WOODEN_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.WOODEN_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.WOODEN_AXE);
/*      */             
/*      */             paramOutput.accept(Items.WOODEN_HOE);
/*      */             
/*      */             paramOutput.accept(Items.STONE_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.STONE_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.STONE_AXE);
/*      */             
/*      */             paramOutput.accept(Items.STONE_HOE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_AXE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_HOE);
/*      */             
/*      */             paramOutput.accept(Items.IRON_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.IRON_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.IRON_AXE);
/*      */             
/*      */             paramOutput.accept(Items.IRON_HOE);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_AXE);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_HOE);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_AXE);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_HOE);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_SHOVEL);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_PICKAXE);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_AXE);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_HOE);
/*      */             
/*      */             paramOutput.accept(Items.BUCKET);
/*      */             
/*      */             paramOutput.accept(Items.WATER_BUCKET);
/*      */             
/*      */             paramOutput.accept(Items.COD_BUCKET);
/*      */             
/*      */             paramOutput.accept(Items.SALMON_BUCKET);
/*      */             
/*      */             paramOutput.accept(Items.TROPICAL_FISH_BUCKET);
/*      */             paramOutput.accept(Items.PUFFERFISH_BUCKET);
/*      */             paramOutput.accept(Items.AXOLOTL_BUCKET);
/*      */             paramOutput.accept(Items.TADPOLE_BUCKET);
/*      */             paramOutput.accept(Items.LAVA_BUCKET);
/*      */             paramOutput.accept(Items.POWDER_SNOW_BUCKET);
/*      */             paramOutput.accept(Items.MILK_BUCKET);
/*      */             paramOutput.accept(Items.FISHING_ROD);
/*      */             paramOutput.accept(Items.FLINT_AND_STEEL);
/*      */             paramOutput.accept(Items.FIRE_CHARGE);
/*      */             paramOutput.accept(Items.BONE_MEAL);
/*      */             paramOutput.accept(Items.SHEARS);
/*      */             paramOutput.accept(Items.BRUSH);
/*      */             paramOutput.accept(Items.NAME_TAG);
/*      */             paramOutput.accept(Items.LEAD);
/*      */             paramOutput.accept(Items.BUNDLE);
/*      */             paramOutput.accept(Items.WHITE_BUNDLE);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_BUNDLE);
/*      */             paramOutput.accept(Items.GRAY_BUNDLE);
/*      */             paramOutput.accept(Items.BLACK_BUNDLE);
/*      */             paramOutput.accept(Items.BROWN_BUNDLE);
/*      */             paramOutput.accept(Items.RED_BUNDLE);
/*      */             paramOutput.accept(Items.ORANGE_BUNDLE);
/*      */             paramOutput.accept(Items.YELLOW_BUNDLE);
/*      */             paramOutput.accept(Items.LIME_BUNDLE);
/*      */             paramOutput.accept(Items.GREEN_BUNDLE);
/*      */             paramOutput.accept(Items.CYAN_BUNDLE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_BUNDLE);
/*      */             paramOutput.accept(Items.BLUE_BUNDLE);
/*      */             paramOutput.accept(Items.PURPLE_BUNDLE);
/*      */             paramOutput.accept(Items.MAGENTA_BUNDLE);
/*      */             paramOutput.accept(Items.PINK_BUNDLE);
/*      */             paramOutput.accept(Items.COMPASS);
/*      */             paramOutput.accept(Items.RECOVERY_COMPASS);
/*      */             paramOutput.accept(Items.CLOCK);
/*      */             paramOutput.accept(Items.SPYGLASS);
/*      */             paramOutput.accept(Items.MAP);
/*      */             paramOutput.accept(Items.WRITABLE_BOOK);
/*      */             paramOutput.accept(Items.WIND_CHARGE);
/*      */             paramOutput.accept(Items.ENDER_PEARL);
/*      */             paramOutput.accept(Items.ENDER_EYE);
/*      */             paramOutput.accept(Items.ELYTRA);
/*      */             generateFireworksAllDurations(paramOutput, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*      */             paramOutput.accept(Items.SADDLE);
/*      */             paramOutput.accept(Items.WHITE_HARNESS);
/*      */             paramOutput.accept(Items.LIGHT_GRAY_HARNESS);
/*      */             paramOutput.accept(Items.GRAY_HARNESS);
/*      */             paramOutput.accept(Items.BLACK_HARNESS);
/*      */             paramOutput.accept(Items.BROWN_HARNESS);
/*      */             paramOutput.accept(Items.RED_HARNESS);
/*      */             paramOutput.accept(Items.ORANGE_HARNESS);
/*      */             paramOutput.accept(Items.YELLOW_HARNESS);
/*      */             paramOutput.accept(Items.LIME_HARNESS);
/*      */             paramOutput.accept(Items.GREEN_HARNESS);
/*      */             paramOutput.accept(Items.CYAN_HARNESS);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_HARNESS);
/*      */             paramOutput.accept(Items.BLUE_HARNESS);
/*      */             paramOutput.accept(Items.PURPLE_HARNESS);
/*      */             paramOutput.accept(Items.MAGENTA_HARNESS);
/*      */             paramOutput.accept(Items.PINK_HARNESS);
/*      */             paramOutput.accept(Items.CARROT_ON_A_STICK);
/*      */             paramOutput.accept(Items.WARPED_FUNGUS_ON_A_STICK);
/*      */             paramOutput.accept(Items.OAK_BOAT);
/*      */             paramOutput.accept(Items.OAK_CHEST_BOAT);
/*      */             paramOutput.accept(Items.SPRUCE_BOAT);
/*      */             paramOutput.accept(Items.SPRUCE_CHEST_BOAT);
/*      */             paramOutput.accept(Items.BIRCH_BOAT);
/*      */             paramOutput.accept(Items.BIRCH_CHEST_BOAT);
/*      */             paramOutput.accept(Items.JUNGLE_BOAT);
/*      */             paramOutput.accept(Items.JUNGLE_CHEST_BOAT);
/*      */             paramOutput.accept(Items.ACACIA_BOAT);
/*      */             paramOutput.accept(Items.ACACIA_CHEST_BOAT);
/*      */             paramOutput.accept(Items.DARK_OAK_BOAT);
/*      */             paramOutput.accept(Items.DARK_OAK_CHEST_BOAT);
/*      */             paramOutput.accept(Items.MANGROVE_BOAT);
/*      */             paramOutput.accept(Items.MANGROVE_CHEST_BOAT);
/*      */             paramOutput.accept(Items.CHERRY_BOAT);
/*      */             paramOutput.accept(Items.CHERRY_CHEST_BOAT);
/*      */             paramOutput.accept(Items.PALE_OAK_BOAT);
/*      */             paramOutput.accept(Items.PALE_OAK_CHEST_BOAT);
/*      */             paramOutput.accept(Items.BAMBOO_RAFT);
/*      */             paramOutput.accept(Items.BAMBOO_CHEST_RAFT);
/*      */             paramOutput.accept(Items.RAIL);
/*      */             paramOutput.accept(Items.POWERED_RAIL);
/*      */             paramOutput.accept(Items.DETECTOR_RAIL);
/*      */             paramOutput.accept(Items.ACTIVATOR_RAIL);
/*      */             paramOutput.accept(Items.MINECART);
/*      */             paramOutput.accept(Items.HOPPER_MINECART);
/*      */             paramOutput.accept(Items.CHEST_MINECART);
/*      */             paramOutput.accept(Items.FURNACE_MINECART);
/*      */             paramOutput.accept(Items.TNT_MINECART);
/*      */             paramItemDisplayParameters.holders().lookup(Registries.INSTRUMENT).ifPresent(());
/*      */             paramOutput.accept(Items.MUSIC_DISC_13);
/*      */             paramOutput.accept(Items.MUSIC_DISC_CAT);
/*      */             paramOutput.accept(Items.MUSIC_DISC_BLOCKS);
/*      */             paramOutput.accept(Items.MUSIC_DISC_CHIRP);
/*      */             paramOutput.accept(Items.MUSIC_DISC_FAR);
/*      */             paramOutput.accept(Items.MUSIC_DISC_MALL);
/*      */             paramOutput.accept(Items.MUSIC_DISC_MELLOHI);
/*      */             paramOutput.accept(Items.MUSIC_DISC_STAL);
/*      */             paramOutput.accept(Items.MUSIC_DISC_STRAD);
/*      */             paramOutput.accept(Items.MUSIC_DISC_WARD);
/*      */             paramOutput.accept(Items.MUSIC_DISC_11);
/*      */             paramOutput.accept(Items.MUSIC_DISC_CREATOR_MUSIC_BOX);
/*      */             paramOutput.accept(Items.MUSIC_DISC_WAIT);
/*      */             paramOutput.accept(Items.MUSIC_DISC_CREATOR);
/*      */             paramOutput.accept(Items.MUSIC_DISC_PRECIPICE);
/*      */             paramOutput.accept(Items.MUSIC_DISC_OTHERSIDE);
/*      */             paramOutput.accept(Items.MUSIC_DISC_RELIC);
/*      */             paramOutput.accept(Items.MUSIC_DISC_5);
/*      */             paramOutput.accept(Items.MUSIC_DISC_PIGSTEP);
/*      */             paramOutput.accept(Items.MUSIC_DISC_TEARS);
/*      */             paramOutput.accept(Items.MUSIC_DISC_LAVA_CHICKEN);
/* 1701 */           }).build());
/* 1702 */     Registry.register(paramRegistry, COMBAT, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 1)
/* 1703 */         .title((Component)Component.translatable("itemGroup.combat"))
/* 1704 */         .icon(() -> new ItemStack(Items.NETHERITE_SWORD))
/* 1705 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.WOODEN_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.STONE_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.IRON_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_SWORD);
/*      */             
/*      */             paramOutput.accept(Items.WOODEN_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.STONE_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.IRON_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_SPEAR);
/*      */             
/*      */             paramOutput.accept(Items.WOODEN_AXE);
/*      */             
/*      */             paramOutput.accept(Items.STONE_AXE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_AXE);
/*      */             
/*      */             paramOutput.accept(Items.IRON_AXE);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_AXE);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND_AXE);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_AXE);
/*      */             
/*      */             paramOutput.accept(Items.TRIDENT);
/*      */             
/*      */             paramOutput.accept(Items.MACE);
/*      */             
/*      */             paramOutput.accept(Items.SHIELD);
/*      */             
/*      */             paramOutput.accept(Items.LEATHER_HELMET);
/*      */             
/*      */             paramOutput.accept(Items.LEATHER_CHESTPLATE);
/*      */             
/*      */             paramOutput.accept(Items.LEATHER_LEGGINGS);
/*      */             
/*      */             paramOutput.accept(Items.LEATHER_BOOTS);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_HELMET);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_CHESTPLATE);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_LEGGINGS);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_BOOTS);
/*      */             
/*      */             paramOutput.accept(Items.CHAINMAIL_HELMET);
/*      */             
/*      */             paramOutput.accept(Items.CHAINMAIL_CHESTPLATE);
/*      */             
/*      */             paramOutput.accept(Items.CHAINMAIL_LEGGINGS);
/*      */             
/*      */             paramOutput.accept(Items.CHAINMAIL_BOOTS);
/*      */             
/*      */             paramOutput.accept(Items.IRON_HELMET);
/*      */             paramOutput.accept(Items.IRON_CHESTPLATE);
/*      */             paramOutput.accept(Items.IRON_LEGGINGS);
/*      */             paramOutput.accept(Items.IRON_BOOTS);
/*      */             paramOutput.accept(Items.GOLDEN_HELMET);
/*      */             paramOutput.accept(Items.GOLDEN_CHESTPLATE);
/*      */             paramOutput.accept(Items.GOLDEN_LEGGINGS);
/*      */             paramOutput.accept(Items.GOLDEN_BOOTS);
/*      */             paramOutput.accept(Items.DIAMOND_HELMET);
/*      */             paramOutput.accept(Items.DIAMOND_CHESTPLATE);
/*      */             paramOutput.accept(Items.DIAMOND_LEGGINGS);
/*      */             paramOutput.accept(Items.DIAMOND_BOOTS);
/*      */             paramOutput.accept(Items.NETHERITE_HELMET);
/*      */             paramOutput.accept(Items.NETHERITE_CHESTPLATE);
/*      */             paramOutput.accept(Items.NETHERITE_LEGGINGS);
/*      */             paramOutput.accept(Items.NETHERITE_BOOTS);
/*      */             paramOutput.accept(Items.TURTLE_HELMET);
/*      */             paramOutput.accept(Items.LEATHER_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.COPPER_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.IRON_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.GOLDEN_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.DIAMOND_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.NETHERITE_HORSE_ARMOR);
/*      */             paramOutput.accept(Items.WOLF_ARMOR);
/*      */             paramOutput.accept(Items.COPPER_NAUTILUS_ARMOR);
/*      */             paramOutput.accept(Items.IRON_NAUTILUS_ARMOR);
/*      */             paramOutput.accept(Items.GOLDEN_NAUTILUS_ARMOR);
/*      */             paramOutput.accept(Items.DIAMOND_NAUTILUS_ARMOR);
/*      */             paramOutput.accept(Items.NETHERITE_NAUTILUS_ARMOR);
/*      */             paramOutput.accept(Items.TOTEM_OF_UNDYING);
/*      */             paramOutput.accept(Items.TNT);
/*      */             paramOutput.accept(Items.END_CRYSTAL);
/*      */             paramOutput.accept(Items.SNOWBALL);
/*      */             paramOutput.accept(Items.EGG);
/*      */             paramOutput.accept(Items.BROWN_EGG);
/*      */             paramOutput.accept(Items.BLUE_EGG);
/*      */             paramOutput.accept(Items.WIND_CHARGE);
/*      */             paramOutput.accept(Items.BOW);
/*      */             paramOutput.accept(Items.CROSSBOW);
/*      */             generateFireworksAllDurations(paramOutput, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*      */             paramOutput.accept(Items.ARROW);
/*      */             paramOutput.accept(Items.SPECTRAL_ARROW);
/*      */             paramItemDisplayParameters.holders().lookup(Registries.POTION).ifPresent(());
/* 1821 */           }).build());
/* 1822 */     Registry.register(paramRegistry, FOOD_AND_DRINKS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 2)
/* 1823 */         .title((Component)Component.translatable("itemGroup.foodAndDrink"))
/* 1824 */         .icon(() -> new ItemStack(Items.GOLDEN_APPLE))
/* 1825 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.APPLE);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_APPLE);
/*      */             
/*      */             paramOutput.accept(Items.ENCHANTED_GOLDEN_APPLE);
/*      */             
/*      */             paramOutput.accept(Items.MELON_SLICE);
/*      */             
/*      */             paramOutput.accept(Items.SWEET_BERRIES);
/*      */             
/*      */             paramOutput.accept(Items.GLOW_BERRIES);
/*      */             
/*      */             paramOutput.accept(Items.CHORUS_FRUIT);
/*      */             
/*      */             paramOutput.accept(Items.CARROT);
/*      */             
/*      */             paramOutput.accept(Items.GOLDEN_CARROT);
/*      */             
/*      */             paramOutput.accept(Items.POTATO);
/*      */             
/*      */             paramOutput.accept(Items.BAKED_POTATO);
/*      */             
/*      */             paramOutput.accept(Items.POISONOUS_POTATO);
/*      */             
/*      */             paramOutput.accept(Items.BEETROOT);
/*      */             
/*      */             paramOutput.accept(Items.DRIED_KELP);
/*      */             
/*      */             paramOutput.accept(Items.BEEF);
/*      */             
/*      */             paramOutput.accept(Items.COOKED_BEEF);
/*      */             
/*      */             paramOutput.accept(Items.PORKCHOP);
/*      */             
/*      */             paramOutput.accept(Items.COOKED_PORKCHOP);
/*      */             
/*      */             paramOutput.accept(Items.MUTTON);
/*      */             
/*      */             paramOutput.accept(Items.COOKED_MUTTON);
/*      */             
/*      */             paramOutput.accept(Items.CHICKEN);
/*      */             
/*      */             paramOutput.accept(Items.COOKED_CHICKEN);
/*      */             
/*      */             paramOutput.accept(Items.RABBIT);
/*      */             paramOutput.accept(Items.COOKED_RABBIT);
/*      */             paramOutput.accept(Items.COD);
/*      */             paramOutput.accept(Items.COOKED_COD);
/*      */             paramOutput.accept(Items.SALMON);
/*      */             paramOutput.accept(Items.COOKED_SALMON);
/*      */             paramOutput.accept(Items.TROPICAL_FISH);
/*      */             paramOutput.accept(Items.PUFFERFISH);
/*      */             paramOutput.accept(Items.BREAD);
/*      */             paramOutput.accept(Items.COOKIE);
/*      */             paramOutput.accept(Items.CAKE);
/*      */             paramOutput.accept(Items.PUMPKIN_PIE);
/*      */             paramOutput.accept(Items.ROTTEN_FLESH);
/*      */             paramOutput.accept(Items.SPIDER_EYE);
/*      */             paramOutput.accept(Items.MUSHROOM_STEW);
/*      */             paramOutput.accept(Items.BEETROOT_SOUP);
/*      */             paramOutput.accept(Items.RABBIT_STEW);
/*      */             generateSuspiciousStews(paramOutput, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*      */             paramOutput.accept(Items.MILK_BUCKET);
/*      */             paramOutput.accept(Items.HONEY_BOTTLE);
/*      */             generateOminousBottles(paramOutput, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*      */             paramItemDisplayParameters.holders().lookup(Registries.POTION).ifPresent(());
/* 1892 */           }).build());
/* 1893 */     Registry.register(paramRegistry, INGREDIENTS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 3)
/* 1894 */         .title((Component)Component.translatable("itemGroup.ingredients"))
/* 1895 */         .icon(() -> new ItemStack(Items.IRON_INGOT))
/* 1896 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.COAL);
/*      */             
/*      */             paramOutput.accept(Items.CHARCOAL);
/*      */             
/*      */             paramOutput.accept(Items.RAW_COPPER);
/*      */             
/*      */             paramOutput.accept(Items.RAW_IRON);
/*      */             
/*      */             paramOutput.accept(Items.RAW_GOLD);
/*      */             
/*      */             paramOutput.accept(Items.EMERALD);
/*      */             
/*      */             paramOutput.accept(Items.LAPIS_LAZULI);
/*      */             
/*      */             paramOutput.accept(Items.DIAMOND);
/*      */             
/*      */             paramOutput.accept(Items.ANCIENT_DEBRIS);
/*      */             
/*      */             paramOutput.accept(Items.QUARTZ);
/*      */             
/*      */             paramOutput.accept(Items.AMETHYST_SHARD);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_NUGGET);
/*      */             
/*      */             paramOutput.accept(Items.IRON_NUGGET);
/*      */             
/*      */             paramOutput.accept(Items.GOLD_NUGGET);
/*      */             
/*      */             paramOutput.accept(Items.COPPER_INGOT);
/*      */             
/*      */             paramOutput.accept(Items.IRON_INGOT);
/*      */             
/*      */             paramOutput.accept(Items.GOLD_INGOT);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_SCRAP);
/*      */             
/*      */             paramOutput.accept(Items.NETHERITE_INGOT);
/*      */             
/*      */             paramOutput.accept(Items.STICK);
/*      */             
/*      */             paramOutput.accept(Items.FLINT);
/*      */             
/*      */             paramOutput.accept(Items.WHEAT);
/*      */             
/*      */             paramOutput.accept(Items.BONE);
/*      */             
/*      */             paramOutput.accept(Items.BONE_MEAL);
/*      */             
/*      */             paramOutput.accept(Items.STRING);
/*      */             
/*      */             paramOutput.accept(Items.FEATHER);
/*      */             
/*      */             paramOutput.accept(Items.SNOWBALL);
/*      */             
/*      */             paramOutput.accept(Items.EGG);
/*      */             
/*      */             paramOutput.accept(Items.BROWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.BLUE_EGG);
/*      */             
/*      */             paramOutput.accept(Items.LEATHER);
/*      */             
/*      */             paramOutput.accept(Items.RABBIT_HIDE);
/*      */             
/*      */             paramOutput.accept(Items.HONEYCOMB);
/*      */             
/*      */             paramOutput.accept(Items.RESIN_CLUMP);
/*      */             
/*      */             paramOutput.accept(Items.INK_SAC);
/*      */             
/*      */             paramOutput.accept(Items.GLOW_INK_SAC);
/*      */             
/*      */             paramOutput.accept(Items.TURTLE_SCUTE);
/*      */             
/*      */             paramOutput.accept(Items.ARMADILLO_SCUTE);
/*      */             
/*      */             paramOutput.accept(Items.SLIME_BALL);
/*      */             
/*      */             paramOutput.accept(Items.CLAY_BALL);
/*      */             
/*      */             paramOutput.accept(Items.PRISMARINE_SHARD);
/*      */             
/*      */             paramOutput.accept(Items.PRISMARINE_CRYSTALS);
/*      */             
/*      */             paramOutput.accept(Items.NAUTILUS_SHELL);
/*      */             
/*      */             paramOutput.accept(Items.HEART_OF_THE_SEA);
/*      */             
/*      */             paramOutput.accept(Items.FIRE_CHARGE);
/*      */             
/*      */             paramOutput.accept(Items.BLAZE_ROD);
/*      */             
/*      */             paramOutput.accept(Items.BREEZE_ROD);
/*      */             
/*      */             paramOutput.accept(Items.HEAVY_CORE);
/*      */             
/*      */             paramOutput.accept(Items.NETHER_STAR);
/*      */             
/*      */             paramOutput.accept(Items.ENDER_PEARL);
/*      */             
/*      */             paramOutput.accept(Items.ENDER_EYE);
/*      */             
/*      */             paramOutput.accept(Items.SHULKER_SHELL);
/*      */             
/*      */             paramOutput.accept(Items.POPPED_CHORUS_FRUIT);
/*      */             
/*      */             paramOutput.accept(Items.ECHO_SHARD);
/*      */             
/*      */             paramOutput.accept(Items.DISC_FRAGMENT_5);
/*      */             
/*      */             paramOutput.accept(Items.WHITE_DYE);
/*      */             
/*      */             paramOutput.accept(Items.LIGHT_GRAY_DYE);
/*      */             paramOutput.accept(Items.GRAY_DYE);
/*      */             paramOutput.accept(Items.BLACK_DYE);
/*      */             paramOutput.accept(Items.BROWN_DYE);
/*      */             paramOutput.accept(Items.RED_DYE);
/*      */             paramOutput.accept(Items.ORANGE_DYE);
/*      */             paramOutput.accept(Items.YELLOW_DYE);
/*      */             paramOutput.accept(Items.LIME_DYE);
/*      */             paramOutput.accept(Items.GREEN_DYE);
/*      */             paramOutput.accept(Items.CYAN_DYE);
/*      */             paramOutput.accept(Items.LIGHT_BLUE_DYE);
/*      */             paramOutput.accept(Items.BLUE_DYE);
/*      */             paramOutput.accept(Items.PURPLE_DYE);
/*      */             paramOutput.accept(Items.MAGENTA_DYE);
/*      */             paramOutput.accept(Items.PINK_DYE);
/*      */             paramOutput.accept(Items.BOWL);
/*      */             paramOutput.accept(Items.BRICK);
/*      */             paramOutput.accept(Items.NETHER_BRICK);
/*      */             paramOutput.accept(Items.RESIN_BRICK);
/*      */             paramOutput.accept(Items.PAPER);
/*      */             paramOutput.accept(Items.BOOK);
/*      */             paramOutput.accept(Items.FIREWORK_STAR);
/*      */             paramOutput.accept(Items.GLASS_BOTTLE);
/*      */             paramOutput.accept(Items.NETHER_WART);
/*      */             paramOutput.accept(Items.REDSTONE);
/*      */             paramOutput.accept(Items.GLOWSTONE_DUST);
/*      */             paramOutput.accept(Items.GUNPOWDER);
/*      */             paramOutput.accept(Items.DRAGON_BREATH);
/*      */             paramOutput.accept(Items.FERMENTED_SPIDER_EYE);
/*      */             paramOutput.accept(Items.BLAZE_POWDER);
/*      */             paramOutput.accept(Items.SUGAR);
/*      */             paramOutput.accept(Items.RABBIT_FOOT);
/*      */             paramOutput.accept(Items.GLISTERING_MELON_SLICE);
/*      */             paramOutput.accept(Items.SPIDER_EYE);
/*      */             paramOutput.accept(Items.PUFFERFISH);
/*      */             paramOutput.accept(Items.MAGMA_CREAM);
/*      */             paramOutput.accept(Items.GOLDEN_CARROT);
/*      */             paramOutput.accept(Items.GHAST_TEAR);
/*      */             paramOutput.accept(Items.TURTLE_HELMET);
/*      */             paramOutput.accept(Items.PHANTOM_MEMBRANE);
/*      */             paramOutput.accept(Items.FIELD_MASONED_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.BORDURE_INDENTED_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.FLOWER_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.CREEPER_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.SKULL_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.MOJANG_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.GLOBE_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.PIGLIN_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.FLOW_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.GUSTER_BANNER_PATTERN);
/*      */             paramOutput.accept(Items.ANGLER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.ARCHER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.ARMS_UP_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.BLADE_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.BREWER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.BURN_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.DANGER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.EXPLORER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.FLOW_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.FRIEND_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.GUSTER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.HEART_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.HEARTBREAK_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.HOWL_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.MINER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.MOURNER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.PLENTY_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.PRIZE_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.SCRAPE_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.SHEAF_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.SHELTER_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.SKULL_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.SNORT_POTTERY_SHERD);
/*      */             paramOutput.accept(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */             paramOutput.accept(Items.EXPERIENCE_BOTTLE);
/*      */             paramOutput.accept(Items.TRIAL_KEY);
/*      */             paramOutput.accept(Items.OMINOUS_TRIAL_KEY);
/*      */             paramItemDisplayParameters.holders().lookup(Registries.ENCHANTMENT).ifPresent(());
/* 2105 */           }).build());
/* 2106 */     Registry.register(paramRegistry, SPAWN_EGGS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 4)
/* 2107 */         .title((Component)Component.translatable("itemGroup.spawnEggs"))
/* 2108 */         .icon(() -> new ItemStack(Items.CREEPER_SPAWN_EGG))
/* 2109 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             paramOutput.accept(Items.SPAWNER);
/*      */             
/*      */             paramOutput.accept(Items.TRIAL_SPAWNER);
/*      */             
/*      */             paramOutput.accept(Items.CREAKING_HEART);
/*      */             
/*      */             paramOutput.accept(Items.CHICKEN_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.COW_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.PIG_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.SHEEP_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.CAMEL_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.DONKEY_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.HORSE_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.MULE_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.CAT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.PARROT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.WOLF_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.ARMADILLO_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.BAT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.BEE_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.FOX_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.GOAT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.LLAMA_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.OCELOT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.PANDA_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.POLAR_BEAR_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.RABBIT_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.AXOLOTL_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.COD_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.DOLPHIN_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.FROG_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.GLOW_SQUID_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.NAUTILUS_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.PUFFERFISH_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.SALMON_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.SQUID_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.TADPOLE_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.TROPICAL_FISH_SPAWN_EGG);
/*      */             
/*      */             paramOutput.accept(Items.TURTLE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ALLAY_SPAWN_EGG);
/*      */             paramOutput.accept(Items.MOOSHROOM_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SNIFFER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.COPPER_GOLEM_SPAWN_EGG);
/*      */             paramOutput.accept(Items.IRON_GOLEM_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SNOW_GOLEM_SPAWN_EGG);
/*      */             paramOutput.accept(Items.TRADER_LLAMA_SPAWN_EGG);
/*      */             paramOutput.accept(Items.VILLAGER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.WANDERING_TRADER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.BOGGED_SPAWN_EGG);
/*      */             paramOutput.accept(Items.CAMEL_HUSK_SPAWN_EGG);
/*      */             paramOutput.accept(Items.DROWNED_SPAWN_EGG);
/*      */             paramOutput.accept(Items.HUSK_SPAWN_EGG);
/*      */             paramOutput.accept(Items.PARCHED_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SKELETON_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SKELETON_HORSE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.STRAY_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOMBIE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOMBIE_HORSE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOMBIE_NAUTILUS_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOMBIE_VILLAGER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.CAVE_SPIDER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SPIDER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.BREEZE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.CREAKING_SPAWN_EGG);
/*      */             paramOutput.accept(Items.CREEPER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ELDER_GUARDIAN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.GUARDIAN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.PHANTOM_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SILVERFISH_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SLIME_SPAWN_EGG);
/*      */             paramOutput.accept(Items.WARDEN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.WITCH_SPAWN_EGG);
/*      */             paramOutput.accept(Items.EVOKER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.PILLAGER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.RAVAGER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.VEX_SPAWN_EGG);
/*      */             paramOutput.accept(Items.VINDICATOR_SPAWN_EGG);
/*      */             paramOutput.accept(Items.BLAZE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.GHAST_SPAWN_EGG);
/*      */             paramOutput.accept(Items.HAPPY_GHAST_SPAWN_EGG);
/*      */             paramOutput.accept(Items.HOGLIN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.MAGMA_CUBE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.PIGLIN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.PIGLIN_BRUTE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.STRIDER_SPAWN_EGG);
/*      */             paramOutput.accept(Items.WITHER_SKELETON_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOGLIN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ENDERMAN_SPAWN_EGG);
/*      */             paramOutput.accept(Items.ENDERMITE_SPAWN_EGG);
/*      */             paramOutput.accept(Items.SHULKER_SPAWN_EGG);
/* 2233 */           }).build());
/* 2234 */     Registry.register(paramRegistry, OP_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 5)
/* 2235 */         .title((Component)Component.translatable("itemGroup.op"))
/* 2236 */         .icon(() -> new ItemStack(Items.COMMAND_BLOCK))
/* 2237 */         .alignedRight()
/* 2238 */         .displayItems((paramItemDisplayParameters, paramOutput) -> {
/*      */             if (paramItemDisplayParameters.hasPermissions()) {
/*      */               paramOutput.accept(Items.COMMAND_BLOCK);
/*      */               
/*      */               paramOutput.accept(Items.CHAIN_COMMAND_BLOCK);
/*      */               
/*      */               paramOutput.accept(Items.REPEATING_COMMAND_BLOCK);
/*      */               
/*      */               paramOutput.accept(Items.COMMAND_BLOCK_MINECART);
/*      */               
/*      */               paramOutput.accept(Items.JIGSAW);
/*      */               paramOutput.accept(Items.STRUCTURE_BLOCK);
/*      */               paramOutput.accept(Items.STRUCTURE_VOID);
/*      */               paramOutput.accept(Items.BARRIER);
/*      */               paramOutput.accept(Items.DEBUG_STICK);
/*      */               paramOutput.accept(Items.TEST_INSTANCE_BLOCK);
/*      */               for (TestBlockMode testBlockMode : TestBlockMode.values()) {
/*      */                 paramOutput.accept(TestBlock.setModeOnStack(new ItemStack(Items.TEST_BLOCK), testBlockMode));
/*      */               }
/*      */               for (byte b = 15; b >= 0; b--) {
/*      */                 paramOutput.accept(LightBlock.setLightOnStack(new ItemStack(Items.LIGHT), b));
/*      */               }
/*      */               paramItemDisplayParameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent(());
/*      */             } 
/* 2262 */           }).build());
/* 2263 */     return (CreativeModeTab)Registry.register(paramRegistry, INVENTORY, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 6)
/* 2264 */         .title((Component)Component.translatable("itemGroup.inventory"))
/* 2265 */         .icon(() -> new ItemStack((ItemLike)Blocks.CHEST))
/* 2266 */         .backgroundTexture(INVENTORY_BACKGROUND)
/* 2267 */         .hideTitle()
/* 2268 */         .alignedRight()
/* 2269 */         .type(CreativeModeTab.Type.INVENTORY)
/* 2270 */         .noScrollBar()
/* 2271 */         .build());
/*      */   }
/*      */   
/*      */   public static void validate() {
/* 2275 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 2276 */     for (ResourceKey resourceKey : BuiltInRegistries.CREATIVE_MODE_TAB.registryKeySet()) {
/* 2277 */       CreativeModeTab creativeModeTab = (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(resourceKey);
/* 2278 */       String str1 = creativeModeTab.getDisplayName().getString();
/* 2279 */       String str2 = (String)hashMap.put(Pair.of(creativeModeTab.row(), Integer.valueOf(creativeModeTab.column())), str1);
/* 2280 */       if (str2 != null) {
/* 2281 */         throw new IllegalArgumentException("Duplicate position: " + str1 + " vs. " + str2);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/* 2286 */   private static final Comparator<Holder<PaintingVariant>> PAINTING_COMPARATOR = Comparator.comparing(Holder::value, Comparator.comparingInt(PaintingVariant::area).thenComparing(PaintingVariant::width));
/*      */   
/*      */   private static CreativeModeTab.ItemDisplayParameters CACHED_PARAMETERS;
/*      */   
/*      */   public static CreativeModeTab getDefaultTab() {
/* 2291 */     return (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(BUILDING_BLOCKS);
/*      */   }
/*      */   
/*      */   private static void generatePotionEffectTypes(CreativeModeTab.Output paramOutput, HolderLookup<Potion> paramHolderLookup, Item paramItem, CreativeModeTab.TabVisibility paramTabVisibility, FeatureFlagSet paramFeatureFlagSet) {
/* 2295 */     paramHolderLookup.listElements()
/* 2296 */       .filter(paramReference -> ((Potion)paramReference.value()).isEnabled(paramFeatureFlagSet))
/* 2297 */       .map(paramReference -> PotionContents.createItemStack(paramItem, (Holder)paramReference))
/* 2298 */       .forEach(paramItemStack -> paramOutput.accept(paramItemStack, paramTabVisibility));
/*      */   }
/*      */   
/*      */   private static void generateEnchantmentBookTypesOnlyMaxLevel(CreativeModeTab.Output paramOutput, HolderLookup<Enchantment> paramHolderLookup, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2302 */     paramHolderLookup.listElements()
/* 2303 */       .map(paramReference -> EnchantmentHelper.createBook(new EnchantmentInstance((Holder)paramReference, ((Enchantment)paramReference.value()).getMaxLevel())))
/* 2304 */       .forEach(paramItemStack -> paramOutput.accept(paramItemStack, paramTabVisibility));
/*      */   }
/*      */   
/*      */   private static void generateEnchantmentBookTypesAllLevels(CreativeModeTab.Output paramOutput, HolderLookup<Enchantment> paramHolderLookup, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2308 */     paramHolderLookup.listElements()
/* 2309 */       .flatMap(paramReference -> IntStream.rangeClosed(((Enchantment)paramReference.value()).getMinLevel(), ((Enchantment)paramReference.value()).getMaxLevel()).mapToObj(()))
/* 2310 */       .forEach(paramItemStack -> paramOutput.accept(paramItemStack, paramTabVisibility));
/*      */   }
/*      */   
/*      */   private static void generateInstrumentTypes(CreativeModeTab.Output paramOutput, HolderLookup<Instrument> paramHolderLookup, Item paramItem, TagKey<Instrument> paramTagKey, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2314 */     paramHolderLookup.get(paramTagKey).ifPresent(paramNamed -> paramNamed.stream().map(()).forEach(()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void generateSuspiciousStews(CreativeModeTab.Output paramOutput, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2322 */     List list = SuspiciousEffectHolder.getAllEffectHolders();
/* 2323 */     Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 2324 */     for (SuspiciousEffectHolder suspiciousEffectHolder : list) {
/* 2325 */       ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW);
/* 2326 */       itemStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, suspiciousEffectHolder.getSuspiciousEffects());
/* 2327 */       set.add(itemStack);
/*      */     } 
/* 2329 */     paramOutput.acceptAll(set, paramTabVisibility);
/*      */   }
/*      */   
/*      */   private static void generateOminousBottles(CreativeModeTab.Output paramOutput, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2333 */     for (byte b = 0; b <= 4; b++) {
/* 2334 */       ItemStack itemStack = new ItemStack(Items.OMINOUS_BOTTLE);
/* 2335 */       itemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(b));
/* 2336 */       paramOutput.accept(itemStack, paramTabVisibility);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void generateFireworksAllDurations(CreativeModeTab.Output paramOutput, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2341 */     for (byte b : FireworkRocketItem.CRAFTABLE_DURATIONS) {
/* 2342 */       ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
/* 2343 */       itemStack.set(DataComponents.FIREWORKS, new Fireworks(b, List.of()));
/* 2344 */       paramOutput.accept(itemStack, paramTabVisibility);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void generatePresetPaintings(CreativeModeTab.Output paramOutput, HolderLookup.Provider paramProvider, HolderLookup.RegistryLookup<PaintingVariant> paramRegistryLookup, Predicate<Holder<PaintingVariant>> paramPredicate, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 2349 */     RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 2350 */     paramRegistryLookup.listElements()
/* 2351 */       .filter(paramPredicate)
/* 2352 */       .sorted(PAINTING_COMPARATOR)
/* 2353 */       .forEach(paramReference -> {
/*      */           ItemStack itemStack = new ItemStack(Items.PAINTING);
/*      */           itemStack.set(DataComponents.PAINTING_VARIANT, paramReference);
/*      */           paramOutput.accept(itemStack, paramTabVisibility);
/*      */         });
/*      */   }
/*      */   
/*      */   public static List<CreativeModeTab> tabs() {
/* 2361 */     return streamAllTabs().filter(CreativeModeTab::shouldDisplay).toList();
/*      */   }
/*      */   
/*      */   public static List<CreativeModeTab> allTabs() {
/* 2365 */     return streamAllTabs().toList();
/*      */   }
/*      */   
/*      */   private static Stream<CreativeModeTab> streamAllTabs() {
/* 2369 */     return BuiltInRegistries.CREATIVE_MODE_TAB.stream();
/*      */   }
/*      */   
/*      */   public static CreativeModeTab searchTab() {
/* 2373 */     return (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(SEARCH);
/*      */   }
/*      */   
/*      */   private static void buildAllTabContents(CreativeModeTab.ItemDisplayParameters paramItemDisplayParameters) {
/* 2377 */     streamAllTabs().filter(paramCreativeModeTab -> (paramCreativeModeTab.getType() == CreativeModeTab.Type.CATEGORY)).forEach(paramCreativeModeTab -> paramCreativeModeTab.buildContents(paramItemDisplayParameters));
/*      */     
/* 2379 */     streamAllTabs().filter(paramCreativeModeTab -> (paramCreativeModeTab.getType() != CreativeModeTab.Type.CATEGORY)).forEach(paramCreativeModeTab -> paramCreativeModeTab.buildContents(paramItemDisplayParameters));
/*      */   }
/*      */   
/*      */   public static boolean tryRebuildTabContents(FeatureFlagSet paramFeatureFlagSet, boolean paramBoolean, HolderLookup.Provider paramProvider) {
/* 2383 */     if (CACHED_PARAMETERS != null && !CACHED_PARAMETERS.needsUpdate(paramFeatureFlagSet, paramBoolean, paramProvider)) {
/* 2384 */       return false;
/*      */     }
/*      */     
/* 2387 */     CACHED_PARAMETERS = new CreativeModeTab.ItemDisplayParameters(paramFeatureFlagSet, paramBoolean, paramProvider);
/* 2388 */     buildAllTabContents(CACHED_PARAMETERS);
/* 2389 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\CreativeModeTabs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */