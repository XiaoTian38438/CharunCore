/*      */ package net.minecraft.world.item;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.UnaryOperator;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.BannerPatternTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*      */ import net.minecraft.world.food.Foods;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.component.Bees;
/*      */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*      */ import net.minecraft.world.item.component.BlocksAttacks;
/*      */ import net.minecraft.world.item.component.BundleContents;
/*      */ import net.minecraft.world.item.component.ChargedProjectiles;
/*      */ import net.minecraft.world.item.component.Consumable;
/*      */ import net.minecraft.world.item.component.Consumables;
/*      */ import net.minecraft.world.item.component.CustomData;
/*      */ import net.minecraft.world.item.component.DamageResistant;
/*      */ import net.minecraft.world.item.component.DeathProtection;
/*      */ import net.minecraft.world.item.component.DebugStickState;
/*      */ import net.minecraft.world.item.component.Fireworks;
/*      */ import net.minecraft.world.item.component.InstrumentComponent;
/*      */ import net.minecraft.world.item.component.ItemContainerContents;
/*      */ import net.minecraft.world.item.component.MapDecorations;
/*      */ import net.minecraft.world.item.component.MapItemColor;
/*      */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*      */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*      */ import net.minecraft.world.item.component.Tool;
/*      */ import net.minecraft.world.item.component.Weapon;
/*      */ import net.minecraft.world.item.component.WritableBookContent;
/*      */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*      */ import net.minecraft.world.item.equipment.ArmorMaterials;
/*      */ import net.minecraft.world.item.equipment.ArmorType;
/*      */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*      */ import net.minecraft.world.level.block.BeehiveBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*      */ import net.minecraft.world.level.block.LightBlock;
/*      */ import net.minecraft.world.level.block.TestBlock;
/*      */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*      */ import net.minecraft.world.level.block.entity.PotDecorations;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.waypoints.Waypoint;
/*      */ 
/*      */ public class Items {
/*   68 */   public static final Item AIR = registerBlock(Blocks.AIR, AirItem::new);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   76 */   public static final Item STONE = registerBlock(Blocks.STONE);
/*   77 */   public static final Item GRANITE = registerBlock(Blocks.GRANITE);
/*   78 */   public static final Item POLISHED_GRANITE = registerBlock(Blocks.POLISHED_GRANITE);
/*   79 */   public static final Item DIORITE = registerBlock(Blocks.DIORITE);
/*   80 */   public static final Item POLISHED_DIORITE = registerBlock(Blocks.POLISHED_DIORITE);
/*   81 */   public static final Item ANDESITE = registerBlock(Blocks.ANDESITE);
/*   82 */   public static final Item POLISHED_ANDESITE = registerBlock(Blocks.POLISHED_ANDESITE);
/*   83 */   public static final Item DEEPSLATE = registerBlock(Blocks.DEEPSLATE);
/*   84 */   public static final Item COBBLED_DEEPSLATE = registerBlock(Blocks.COBBLED_DEEPSLATE);
/*   85 */   public static final Item POLISHED_DEEPSLATE = registerBlock(Blocks.POLISHED_DEEPSLATE);
/*   86 */   public static final Item CALCITE = registerBlock(Blocks.CALCITE);
/*   87 */   public static final Item TUFF = registerBlock(Blocks.TUFF);
/*   88 */   public static final Item TUFF_SLAB = registerBlock(Blocks.TUFF_SLAB);
/*   89 */   public static final Item TUFF_STAIRS = registerBlock(Blocks.TUFF_STAIRS);
/*   90 */   public static final Item TUFF_WALL = registerBlock(Blocks.TUFF_WALL);
/*   91 */   public static final Item CHISELED_TUFF = registerBlock(Blocks.CHISELED_TUFF);
/*   92 */   public static final Item POLISHED_TUFF = registerBlock(Blocks.POLISHED_TUFF);
/*   93 */   public static final Item POLISHED_TUFF_SLAB = registerBlock(Blocks.POLISHED_TUFF_SLAB);
/*   94 */   public static final Item POLISHED_TUFF_STAIRS = registerBlock(Blocks.POLISHED_TUFF_STAIRS);
/*   95 */   public static final Item POLISHED_TUFF_WALL = registerBlock(Blocks.POLISHED_TUFF_WALL);
/*   96 */   public static final Item TUFF_BRICKS = registerBlock(Blocks.TUFF_BRICKS);
/*   97 */   public static final Item TUFF_BRICK_SLAB = registerBlock(Blocks.TUFF_BRICK_SLAB);
/*   98 */   public static final Item TUFF_BRICK_STAIRS = registerBlock(Blocks.TUFF_BRICK_STAIRS);
/*   99 */   public static final Item TUFF_BRICK_WALL = registerBlock(Blocks.TUFF_BRICK_WALL);
/*  100 */   public static final Item CHISELED_TUFF_BRICKS = registerBlock(Blocks.CHISELED_TUFF_BRICKS);
/*  101 */   public static final Item DRIPSTONE_BLOCK = registerBlock(Blocks.DRIPSTONE_BLOCK);
/*  102 */   public static final Item GRASS_BLOCK = registerBlock(Blocks.GRASS_BLOCK);
/*  103 */   public static final Item DIRT = registerBlock(Blocks.DIRT);
/*  104 */   public static final Item COARSE_DIRT = registerBlock(Blocks.COARSE_DIRT);
/*  105 */   public static final Item PODZOL = registerBlock(Blocks.PODZOL);
/*  106 */   public static final Item ROOTED_DIRT = registerBlock(Blocks.ROOTED_DIRT);
/*  107 */   public static final Item MUD = registerBlock(Blocks.MUD);
/*  108 */   public static final Item CRIMSON_NYLIUM = registerBlock(Blocks.CRIMSON_NYLIUM);
/*  109 */   public static final Item WARPED_NYLIUM = registerBlock(Blocks.WARPED_NYLIUM);
/*  110 */   public static final Item COBBLESTONE = registerBlock(Blocks.COBBLESTONE);
/*  111 */   public static final Item OAK_PLANKS = registerBlock(Blocks.OAK_PLANKS);
/*  112 */   public static final Item SPRUCE_PLANKS = registerBlock(Blocks.SPRUCE_PLANKS);
/*  113 */   public static final Item BIRCH_PLANKS = registerBlock(Blocks.BIRCH_PLANKS);
/*  114 */   public static final Item JUNGLE_PLANKS = registerBlock(Blocks.JUNGLE_PLANKS);
/*  115 */   public static final Item ACACIA_PLANKS = registerBlock(Blocks.ACACIA_PLANKS);
/*  116 */   public static final Item CHERRY_PLANKS = registerBlock(Blocks.CHERRY_PLANKS);
/*  117 */   public static final Item DARK_OAK_PLANKS = registerBlock(Blocks.DARK_OAK_PLANKS);
/*  118 */   public static final Item PALE_OAK_PLANKS = registerBlock(Blocks.PALE_OAK_PLANKS);
/*  119 */   public static final Item MANGROVE_PLANKS = registerBlock(Blocks.MANGROVE_PLANKS);
/*  120 */   public static final Item BAMBOO_PLANKS = registerBlock(Blocks.BAMBOO_PLANKS);
/*  121 */   public static final Item CRIMSON_PLANKS = registerBlock(Blocks.CRIMSON_PLANKS);
/*  122 */   public static final Item WARPED_PLANKS = registerBlock(Blocks.WARPED_PLANKS);
/*  123 */   public static final Item BAMBOO_MOSAIC = registerBlock(Blocks.BAMBOO_MOSAIC);
/*  124 */   public static final Item OAK_SAPLING = registerBlock(Blocks.OAK_SAPLING);
/*  125 */   public static final Item SPRUCE_SAPLING = registerBlock(Blocks.SPRUCE_SAPLING);
/*  126 */   public static final Item BIRCH_SAPLING = registerBlock(Blocks.BIRCH_SAPLING);
/*  127 */   public static final Item JUNGLE_SAPLING = registerBlock(Blocks.JUNGLE_SAPLING);
/*  128 */   public static final Item ACACIA_SAPLING = registerBlock(Blocks.ACACIA_SAPLING);
/*  129 */   public static final Item CHERRY_SAPLING = registerBlock(Blocks.CHERRY_SAPLING);
/*  130 */   public static final Item DARK_OAK_SAPLING = registerBlock(Blocks.DARK_OAK_SAPLING);
/*  131 */   public static final Item PALE_OAK_SAPLING = registerBlock(Blocks.PALE_OAK_SAPLING);
/*  132 */   public static final Item MANGROVE_PROPAGULE = registerBlock(Blocks.MANGROVE_PROPAGULE);
/*  133 */   public static final Item BEDROCK = registerBlock(Blocks.BEDROCK);
/*  134 */   public static final Item SAND = registerBlock(Blocks.SAND);
/*  135 */   public static final Item SUSPICIOUS_SAND = registerBlock(Blocks.SUSPICIOUS_SAND);
/*  136 */   public static final Item SUSPICIOUS_GRAVEL = registerBlock(Blocks.SUSPICIOUS_GRAVEL);
/*  137 */   public static final Item RED_SAND = registerBlock(Blocks.RED_SAND);
/*  138 */   public static final Item GRAVEL = registerBlock(Blocks.GRAVEL);
/*  139 */   public static final Item COAL_ORE = registerBlock(Blocks.COAL_ORE);
/*  140 */   public static final Item DEEPSLATE_COAL_ORE = registerBlock(Blocks.DEEPSLATE_COAL_ORE);
/*  141 */   public static final Item IRON_ORE = registerBlock(Blocks.IRON_ORE);
/*  142 */   public static final Item DEEPSLATE_IRON_ORE = registerBlock(Blocks.DEEPSLATE_IRON_ORE);
/*  143 */   public static final Item COPPER_ORE = registerBlock(Blocks.COPPER_ORE);
/*  144 */   public static final Item DEEPSLATE_COPPER_ORE = registerBlock(Blocks.DEEPSLATE_COPPER_ORE);
/*  145 */   public static final Item GOLD_ORE = registerBlock(Blocks.GOLD_ORE);
/*  146 */   public static final Item DEEPSLATE_GOLD_ORE = registerBlock(Blocks.DEEPSLATE_GOLD_ORE);
/*  147 */   public static final Item REDSTONE_ORE = registerBlock(Blocks.REDSTONE_ORE);
/*  148 */   public static final Item DEEPSLATE_REDSTONE_ORE = registerBlock(Blocks.DEEPSLATE_REDSTONE_ORE);
/*  149 */   public static final Item EMERALD_ORE = registerBlock(Blocks.EMERALD_ORE);
/*  150 */   public static final Item DEEPSLATE_EMERALD_ORE = registerBlock(Blocks.DEEPSLATE_EMERALD_ORE);
/*  151 */   public static final Item LAPIS_ORE = registerBlock(Blocks.LAPIS_ORE);
/*  152 */   public static final Item DEEPSLATE_LAPIS_ORE = registerBlock(Blocks.DEEPSLATE_LAPIS_ORE);
/*  153 */   public static final Item DIAMOND_ORE = registerBlock(Blocks.DIAMOND_ORE);
/*  154 */   public static final Item DEEPSLATE_DIAMOND_ORE = registerBlock(Blocks.DEEPSLATE_DIAMOND_ORE);
/*  155 */   public static final Item NETHER_GOLD_ORE = registerBlock(Blocks.NETHER_GOLD_ORE);
/*  156 */   public static final Item NETHER_QUARTZ_ORE = registerBlock(Blocks.NETHER_QUARTZ_ORE);
/*  157 */   public static final Item ANCIENT_DEBRIS = registerBlock(Blocks.ANCIENT_DEBRIS, (new Item.Properties()).fireResistant());
/*  158 */   public static final Item COAL_BLOCK = registerBlock(Blocks.COAL_BLOCK);
/*  159 */   public static final Item RAW_IRON_BLOCK = registerBlock(Blocks.RAW_IRON_BLOCK);
/*  160 */   public static final Item RAW_COPPER_BLOCK = registerBlock(Blocks.RAW_COPPER_BLOCK);
/*  161 */   public static final Item RAW_GOLD_BLOCK = registerBlock(Blocks.RAW_GOLD_BLOCK);
/*  162 */   public static final Item HEAVY_CORE = registerBlock(Blocks.HEAVY_CORE, (new Item.Properties()).rarity(Rarity.EPIC));
/*  163 */   public static final Item AMETHYST_BLOCK = registerBlock(Blocks.AMETHYST_BLOCK);
/*  164 */   public static final Item BUDDING_AMETHYST = registerBlock(Blocks.BUDDING_AMETHYST);
/*  165 */   public static final Item IRON_BLOCK = registerBlock(Blocks.IRON_BLOCK);
/*  166 */   public static final Item COPPER_BLOCK = registerBlock(Blocks.COPPER_BLOCK);
/*  167 */   public static final Item GOLD_BLOCK = registerBlock(Blocks.GOLD_BLOCK);
/*  168 */   public static final Item DIAMOND_BLOCK = registerBlock(Blocks.DIAMOND_BLOCK);
/*  169 */   public static final Item NETHERITE_BLOCK = registerBlock(Blocks.NETHERITE_BLOCK, (new Item.Properties()).fireResistant());
/*  170 */   public static final Item EXPOSED_COPPER = registerBlock(Blocks.EXPOSED_COPPER);
/*  171 */   public static final Item WEATHERED_COPPER = registerBlock(Blocks.WEATHERED_COPPER);
/*  172 */   public static final Item OXIDIZED_COPPER = registerBlock(Blocks.OXIDIZED_COPPER);
/*  173 */   public static final Item CHISELED_COPPER = registerBlock(Blocks.CHISELED_COPPER);
/*  174 */   public static final Item EXPOSED_CHISELED_COPPER = registerBlock(Blocks.EXPOSED_CHISELED_COPPER);
/*  175 */   public static final Item WEATHERED_CHISELED_COPPER = registerBlock(Blocks.WEATHERED_CHISELED_COPPER);
/*  176 */   public static final Item OXIDIZED_CHISELED_COPPER = registerBlock(Blocks.OXIDIZED_CHISELED_COPPER);
/*  177 */   public static final Item CUT_COPPER = registerBlock(Blocks.CUT_COPPER);
/*  178 */   public static final Item EXPOSED_CUT_COPPER = registerBlock(Blocks.EXPOSED_CUT_COPPER);
/*  179 */   public static final Item WEATHERED_CUT_COPPER = registerBlock(Blocks.WEATHERED_CUT_COPPER);
/*  180 */   public static final Item OXIDIZED_CUT_COPPER = registerBlock(Blocks.OXIDIZED_CUT_COPPER);
/*  181 */   public static final Item CUT_COPPER_STAIRS = registerBlock(Blocks.CUT_COPPER_STAIRS);
/*  182 */   public static final Item EXPOSED_CUT_COPPER_STAIRS = registerBlock(Blocks.EXPOSED_CUT_COPPER_STAIRS);
/*  183 */   public static final Item WEATHERED_CUT_COPPER_STAIRS = registerBlock(Blocks.WEATHERED_CUT_COPPER_STAIRS);
/*  184 */   public static final Item OXIDIZED_CUT_COPPER_STAIRS = registerBlock(Blocks.OXIDIZED_CUT_COPPER_STAIRS);
/*  185 */   public static final Item CUT_COPPER_SLAB = registerBlock(Blocks.CUT_COPPER_SLAB);
/*  186 */   public static final Item EXPOSED_CUT_COPPER_SLAB = registerBlock(Blocks.EXPOSED_CUT_COPPER_SLAB);
/*  187 */   public static final Item WEATHERED_CUT_COPPER_SLAB = registerBlock(Blocks.WEATHERED_CUT_COPPER_SLAB);
/*  188 */   public static final Item OXIDIZED_CUT_COPPER_SLAB = registerBlock(Blocks.OXIDIZED_CUT_COPPER_SLAB);
/*  189 */   public static final Item WAXED_COPPER_BLOCK = registerBlock(Blocks.WAXED_COPPER_BLOCK);
/*  190 */   public static final Item WAXED_EXPOSED_COPPER = registerBlock(Blocks.WAXED_EXPOSED_COPPER);
/*  191 */   public static final Item WAXED_WEATHERED_COPPER = registerBlock(Blocks.WAXED_WEATHERED_COPPER);
/*  192 */   public static final Item WAXED_OXIDIZED_COPPER = registerBlock(Blocks.WAXED_OXIDIZED_COPPER);
/*  193 */   public static final Item WAXED_CHISELED_COPPER = registerBlock(Blocks.WAXED_CHISELED_COPPER);
/*  194 */   public static final Item WAXED_EXPOSED_CHISELED_COPPER = registerBlock(Blocks.WAXED_EXPOSED_CHISELED_COPPER);
/*  195 */   public static final Item WAXED_WEATHERED_CHISELED_COPPER = registerBlock(Blocks.WAXED_WEATHERED_CHISELED_COPPER);
/*  196 */   public static final Item WAXED_OXIDIZED_CHISELED_COPPER = registerBlock(Blocks.WAXED_OXIDIZED_CHISELED_COPPER);
/*  197 */   public static final Item WAXED_CUT_COPPER = registerBlock(Blocks.WAXED_CUT_COPPER);
/*  198 */   public static final Item WAXED_EXPOSED_CUT_COPPER = registerBlock(Blocks.WAXED_EXPOSED_CUT_COPPER);
/*  199 */   public static final Item WAXED_WEATHERED_CUT_COPPER = registerBlock(Blocks.WAXED_WEATHERED_CUT_COPPER);
/*  200 */   public static final Item WAXED_OXIDIZED_CUT_COPPER = registerBlock(Blocks.WAXED_OXIDIZED_CUT_COPPER);
/*  201 */   public static final Item WAXED_CUT_COPPER_STAIRS = registerBlock(Blocks.WAXED_CUT_COPPER_STAIRS);
/*  202 */   public static final Item WAXED_EXPOSED_CUT_COPPER_STAIRS = registerBlock(Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);
/*  203 */   public static final Item WAXED_WEATHERED_CUT_COPPER_STAIRS = registerBlock(Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);
/*  204 */   public static final Item WAXED_OXIDIZED_CUT_COPPER_STAIRS = registerBlock(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
/*  205 */   public static final Item WAXED_CUT_COPPER_SLAB = registerBlock(Blocks.WAXED_CUT_COPPER_SLAB);
/*  206 */   public static final Item WAXED_EXPOSED_CUT_COPPER_SLAB = registerBlock(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB);
/*  207 */   public static final Item WAXED_WEATHERED_CUT_COPPER_SLAB = registerBlock(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB);
/*  208 */   public static final Item WAXED_OXIDIZED_CUT_COPPER_SLAB = registerBlock(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
/*  209 */   public static final Item OAK_LOG = registerBlock(Blocks.OAK_LOG);
/*  210 */   public static final Item SPRUCE_LOG = registerBlock(Blocks.SPRUCE_LOG);
/*  211 */   public static final Item BIRCH_LOG = registerBlock(Blocks.BIRCH_LOG);
/*  212 */   public static final Item JUNGLE_LOG = registerBlock(Blocks.JUNGLE_LOG);
/*  213 */   public static final Item ACACIA_LOG = registerBlock(Blocks.ACACIA_LOG);
/*  214 */   public static final Item CHERRY_LOG = registerBlock(Blocks.CHERRY_LOG);
/*  215 */   public static final Item PALE_OAK_LOG = registerBlock(Blocks.PALE_OAK_LOG);
/*  216 */   public static final Item DARK_OAK_LOG = registerBlock(Blocks.DARK_OAK_LOG);
/*  217 */   public static final Item MANGROVE_LOG = registerBlock(Blocks.MANGROVE_LOG);
/*  218 */   public static final Item MANGROVE_ROOTS = registerBlock(Blocks.MANGROVE_ROOTS);
/*  219 */   public static final Item MUDDY_MANGROVE_ROOTS = registerBlock(Blocks.MUDDY_MANGROVE_ROOTS);
/*  220 */   public static final Item CRIMSON_STEM = registerBlock(Blocks.CRIMSON_STEM);
/*  221 */   public static final Item WARPED_STEM = registerBlock(Blocks.WARPED_STEM);
/*  222 */   public static final Item BAMBOO_BLOCK = registerBlock(Blocks.BAMBOO_BLOCK);
/*  223 */   public static final Item STRIPPED_OAK_LOG = registerBlock(Blocks.STRIPPED_OAK_LOG);
/*  224 */   public static final Item STRIPPED_SPRUCE_LOG = registerBlock(Blocks.STRIPPED_SPRUCE_LOG);
/*  225 */   public static final Item STRIPPED_BIRCH_LOG = registerBlock(Blocks.STRIPPED_BIRCH_LOG);
/*  226 */   public static final Item STRIPPED_JUNGLE_LOG = registerBlock(Blocks.STRIPPED_JUNGLE_LOG);
/*  227 */   public static final Item STRIPPED_ACACIA_LOG = registerBlock(Blocks.STRIPPED_ACACIA_LOG);
/*  228 */   public static final Item STRIPPED_CHERRY_LOG = registerBlock(Blocks.STRIPPED_CHERRY_LOG);
/*  229 */   public static final Item STRIPPED_DARK_OAK_LOG = registerBlock(Blocks.STRIPPED_DARK_OAK_LOG);
/*  230 */   public static final Item STRIPPED_PALE_OAK_LOG = registerBlock(Blocks.STRIPPED_PALE_OAK_LOG);
/*  231 */   public static final Item STRIPPED_MANGROVE_LOG = registerBlock(Blocks.STRIPPED_MANGROVE_LOG);
/*  232 */   public static final Item STRIPPED_CRIMSON_STEM = registerBlock(Blocks.STRIPPED_CRIMSON_STEM);
/*  233 */   public static final Item STRIPPED_WARPED_STEM = registerBlock(Blocks.STRIPPED_WARPED_STEM);
/*  234 */   public static final Item STRIPPED_OAK_WOOD = registerBlock(Blocks.STRIPPED_OAK_WOOD);
/*  235 */   public static final Item STRIPPED_SPRUCE_WOOD = registerBlock(Blocks.STRIPPED_SPRUCE_WOOD);
/*  236 */   public static final Item STRIPPED_BIRCH_WOOD = registerBlock(Blocks.STRIPPED_BIRCH_WOOD);
/*  237 */   public static final Item STRIPPED_JUNGLE_WOOD = registerBlock(Blocks.STRIPPED_JUNGLE_WOOD);
/*  238 */   public static final Item STRIPPED_ACACIA_WOOD = registerBlock(Blocks.STRIPPED_ACACIA_WOOD);
/*  239 */   public static final Item STRIPPED_CHERRY_WOOD = registerBlock(Blocks.STRIPPED_CHERRY_WOOD);
/*  240 */   public static final Item STRIPPED_DARK_OAK_WOOD = registerBlock(Blocks.STRIPPED_DARK_OAK_WOOD);
/*  241 */   public static final Item STRIPPED_PALE_OAK_WOOD = registerBlock(Blocks.STRIPPED_PALE_OAK_WOOD);
/*  242 */   public static final Item STRIPPED_MANGROVE_WOOD = registerBlock(Blocks.STRIPPED_MANGROVE_WOOD);
/*  243 */   public static final Item STRIPPED_CRIMSON_HYPHAE = registerBlock(Blocks.STRIPPED_CRIMSON_HYPHAE);
/*  244 */   public static final Item STRIPPED_WARPED_HYPHAE = registerBlock(Blocks.STRIPPED_WARPED_HYPHAE);
/*  245 */   public static final Item STRIPPED_BAMBOO_BLOCK = registerBlock(Blocks.STRIPPED_BAMBOO_BLOCK);
/*  246 */   public static final Item OAK_WOOD = registerBlock(Blocks.OAK_WOOD);
/*  247 */   public static final Item SPRUCE_WOOD = registerBlock(Blocks.SPRUCE_WOOD);
/*  248 */   public static final Item BIRCH_WOOD = registerBlock(Blocks.BIRCH_WOOD);
/*  249 */   public static final Item JUNGLE_WOOD = registerBlock(Blocks.JUNGLE_WOOD);
/*  250 */   public static final Item ACACIA_WOOD = registerBlock(Blocks.ACACIA_WOOD);
/*  251 */   public static final Item CHERRY_WOOD = registerBlock(Blocks.CHERRY_WOOD);
/*  252 */   public static final Item PALE_OAK_WOOD = registerBlock(Blocks.PALE_OAK_WOOD);
/*  253 */   public static final Item DARK_OAK_WOOD = registerBlock(Blocks.DARK_OAK_WOOD);
/*  254 */   public static final Item MANGROVE_WOOD = registerBlock(Blocks.MANGROVE_WOOD);
/*  255 */   public static final Item CRIMSON_HYPHAE = registerBlock(Blocks.CRIMSON_HYPHAE);
/*  256 */   public static final Item WARPED_HYPHAE = registerBlock(Blocks.WARPED_HYPHAE);
/*  257 */   public static final Item OAK_LEAVES = registerBlock(Blocks.OAK_LEAVES);
/*  258 */   public static final Item SPRUCE_LEAVES = registerBlock(Blocks.SPRUCE_LEAVES);
/*  259 */   public static final Item BIRCH_LEAVES = registerBlock(Blocks.BIRCH_LEAVES);
/*  260 */   public static final Item JUNGLE_LEAVES = registerBlock(Blocks.JUNGLE_LEAVES);
/*  261 */   public static final Item ACACIA_LEAVES = registerBlock(Blocks.ACACIA_LEAVES);
/*  262 */   public static final Item CHERRY_LEAVES = registerBlock(Blocks.CHERRY_LEAVES);
/*  263 */   public static final Item DARK_OAK_LEAVES = registerBlock(Blocks.DARK_OAK_LEAVES);
/*  264 */   public static final Item PALE_OAK_LEAVES = registerBlock(Blocks.PALE_OAK_LEAVES);
/*  265 */   public static final Item MANGROVE_LEAVES = registerBlock(Blocks.MANGROVE_LEAVES);
/*  266 */   public static final Item AZALEA_LEAVES = registerBlock(Blocks.AZALEA_LEAVES);
/*  267 */   public static final Item FLOWERING_AZALEA_LEAVES = registerBlock(Blocks.FLOWERING_AZALEA_LEAVES);
/*  268 */   public static final Item SPONGE = registerBlock(Blocks.SPONGE);
/*  269 */   public static final Item WET_SPONGE = registerBlock(Blocks.WET_SPONGE);
/*  270 */   public static final Item GLASS = registerBlock(Blocks.GLASS);
/*  271 */   public static final Item TINTED_GLASS = registerBlock(Blocks.TINTED_GLASS);
/*  272 */   public static final Item LAPIS_BLOCK = registerBlock(Blocks.LAPIS_BLOCK);
/*  273 */   public static final Item SANDSTONE = registerBlock(Blocks.SANDSTONE);
/*  274 */   public static final Item CHISELED_SANDSTONE = registerBlock(Blocks.CHISELED_SANDSTONE);
/*  275 */   public static final Item CUT_SANDSTONE = registerBlock(Blocks.CUT_SANDSTONE);
/*  276 */   public static final Item COBWEB = registerBlock(Blocks.COBWEB);
/*  277 */   public static final Item SHORT_GRASS = registerBlock(Blocks.SHORT_GRASS);
/*  278 */   public static final Item FERN = registerBlock(Blocks.FERN);
/*  279 */   public static final Item BUSH = registerBlock(Blocks.BUSH);
/*  280 */   public static final Item AZALEA = registerBlock(Blocks.AZALEA);
/*  281 */   public static final Item FLOWERING_AZALEA = registerBlock(Blocks.FLOWERING_AZALEA);
/*  282 */   public static final Item DEAD_BUSH = registerBlock(Blocks.DEAD_BUSH);
/*  283 */   public static final Item FIREFLY_BUSH = registerBlock(Blocks.FIREFLY_BUSH);
/*  284 */   public static final Item DRY_SHORT_GRASS = registerBlock(Blocks.SHORT_DRY_GRASS);
/*  285 */   public static final Item DRY_TALL_GRASS = registerBlock(Blocks.TALL_DRY_GRASS);
/*  286 */   public static final Item SEAGRASS = registerBlock(Blocks.SEAGRASS);
/*  287 */   public static final Item SEA_PICKLE = registerBlock(Blocks.SEA_PICKLE);
/*  288 */   public static final Item WHITE_WOOL = registerBlock(Blocks.WHITE_WOOL);
/*  289 */   public static final Item ORANGE_WOOL = registerBlock(Blocks.ORANGE_WOOL);
/*  290 */   public static final Item MAGENTA_WOOL = registerBlock(Blocks.MAGENTA_WOOL);
/*  291 */   public static final Item LIGHT_BLUE_WOOL = registerBlock(Blocks.LIGHT_BLUE_WOOL);
/*  292 */   public static final Item YELLOW_WOOL = registerBlock(Blocks.YELLOW_WOOL);
/*  293 */   public static final Item LIME_WOOL = registerBlock(Blocks.LIME_WOOL);
/*  294 */   public static final Item PINK_WOOL = registerBlock(Blocks.PINK_WOOL);
/*  295 */   public static final Item GRAY_WOOL = registerBlock(Blocks.GRAY_WOOL);
/*  296 */   public static final Item LIGHT_GRAY_WOOL = registerBlock(Blocks.LIGHT_GRAY_WOOL);
/*  297 */   public static final Item CYAN_WOOL = registerBlock(Blocks.CYAN_WOOL);
/*  298 */   public static final Item PURPLE_WOOL = registerBlock(Blocks.PURPLE_WOOL);
/*  299 */   public static final Item BLUE_WOOL = registerBlock(Blocks.BLUE_WOOL);
/*  300 */   public static final Item BROWN_WOOL = registerBlock(Blocks.BROWN_WOOL);
/*  301 */   public static final Item GREEN_WOOL = registerBlock(Blocks.GREEN_WOOL);
/*  302 */   public static final Item RED_WOOL = registerBlock(Blocks.RED_WOOL);
/*  303 */   public static final Item BLACK_WOOL = registerBlock(Blocks.BLACK_WOOL);
/*  304 */   public static final Item DANDELION = registerBlock(Blocks.DANDELION);
/*  305 */   public static final Item OPEN_EYEBLOSSOM = registerBlock(Blocks.OPEN_EYEBLOSSOM);
/*  306 */   public static final Item CLOSED_EYEBLOSSOM = registerBlock(Blocks.CLOSED_EYEBLOSSOM);
/*  307 */   public static final Item POPPY = registerBlock(Blocks.POPPY);
/*  308 */   public static final Item BLUE_ORCHID = registerBlock(Blocks.BLUE_ORCHID);
/*  309 */   public static final Item ALLIUM = registerBlock(Blocks.ALLIUM);
/*  310 */   public static final Item AZURE_BLUET = registerBlock(Blocks.AZURE_BLUET);
/*  311 */   public static final Item RED_TULIP = registerBlock(Blocks.RED_TULIP);
/*  312 */   public static final Item ORANGE_TULIP = registerBlock(Blocks.ORANGE_TULIP);
/*  313 */   public static final Item WHITE_TULIP = registerBlock(Blocks.WHITE_TULIP);
/*  314 */   public static final Item PINK_TULIP = registerBlock(Blocks.PINK_TULIP);
/*  315 */   public static final Item OXEYE_DAISY = registerBlock(Blocks.OXEYE_DAISY);
/*  316 */   public static final Item CORNFLOWER = registerBlock(Blocks.CORNFLOWER);
/*  317 */   public static final Item LILY_OF_THE_VALLEY = registerBlock(Blocks.LILY_OF_THE_VALLEY);
/*  318 */   public static final Item WITHER_ROSE = registerBlock(Blocks.WITHER_ROSE);
/*  319 */   public static final Item TORCHFLOWER = registerBlock(Blocks.TORCHFLOWER);
/*  320 */   public static final Item PITCHER_PLANT = registerBlock(Blocks.PITCHER_PLANT);
/*  321 */   public static final Item SPORE_BLOSSOM = registerBlock(Blocks.SPORE_BLOSSOM);
/*  322 */   public static final Item BROWN_MUSHROOM = registerBlock(Blocks.BROWN_MUSHROOM);
/*  323 */   public static final Item RED_MUSHROOM = registerBlock(Blocks.RED_MUSHROOM);
/*  324 */   public static final Item CRIMSON_FUNGUS = registerBlock(Blocks.CRIMSON_FUNGUS);
/*  325 */   public static final Item WARPED_FUNGUS = registerBlock(Blocks.WARPED_FUNGUS);
/*  326 */   public static final Item CRIMSON_ROOTS = registerBlock(Blocks.CRIMSON_ROOTS);
/*  327 */   public static final Item WARPED_ROOTS = registerBlock(Blocks.WARPED_ROOTS);
/*  328 */   public static final Item NETHER_SPROUTS = registerBlock(Blocks.NETHER_SPROUTS);
/*  329 */   public static final Item WEEPING_VINES = registerBlock(Blocks.WEEPING_VINES);
/*  330 */   public static final Item TWISTING_VINES = registerBlock(Blocks.TWISTING_VINES);
/*  331 */   public static final Item SUGAR_CANE = registerBlock(Blocks.SUGAR_CANE);
/*  332 */   public static final Item KELP = registerBlock(Blocks.KELP);
/*  333 */   public static final Item PINK_PETALS = registerBlock(Blocks.PINK_PETALS);
/*  334 */   public static final Item WILDFLOWERS = registerBlock(Blocks.WILDFLOWERS);
/*  335 */   public static final Item LEAF_LITTER = registerBlock(Blocks.LEAF_LITTER);
/*  336 */   public static final Item MOSS_CARPET = registerBlock(Blocks.MOSS_CARPET);
/*  337 */   public static final Item MOSS_BLOCK = registerBlock(Blocks.MOSS_BLOCK);
/*  338 */   public static final Item PALE_MOSS_CARPET = registerBlock(Blocks.PALE_MOSS_CARPET);
/*  339 */   public static final Item PALE_HANGING_MOSS = registerBlock(Blocks.PALE_HANGING_MOSS);
/*  340 */   public static final Item PALE_MOSS_BLOCK = registerBlock(Blocks.PALE_MOSS_BLOCK);
/*  341 */   public static final Item HANGING_ROOTS = registerBlock(Blocks.HANGING_ROOTS);
/*  342 */   public static final Item BIG_DRIPLEAF = registerBlock(Blocks.BIG_DRIPLEAF, new Block[] { Blocks.BIG_DRIPLEAF_STEM });
/*  343 */   public static final Item SMALL_DRIPLEAF = registerBlock(Blocks.SMALL_DRIPLEAF, DoubleHighBlockItem::new);
/*  344 */   public static final Item BAMBOO = registerBlock(Blocks.BAMBOO);
/*  345 */   public static final Item OAK_SLAB = registerBlock(Blocks.OAK_SLAB);
/*  346 */   public static final Item SPRUCE_SLAB = registerBlock(Blocks.SPRUCE_SLAB);
/*  347 */   public static final Item BIRCH_SLAB = registerBlock(Blocks.BIRCH_SLAB);
/*  348 */   public static final Item JUNGLE_SLAB = registerBlock(Blocks.JUNGLE_SLAB);
/*  349 */   public static final Item ACACIA_SLAB = registerBlock(Blocks.ACACIA_SLAB);
/*  350 */   public static final Item CHERRY_SLAB = registerBlock(Blocks.CHERRY_SLAB);
/*  351 */   public static final Item DARK_OAK_SLAB = registerBlock(Blocks.DARK_OAK_SLAB);
/*  352 */   public static final Item PALE_OAK_SLAB = registerBlock(Blocks.PALE_OAK_SLAB);
/*  353 */   public static final Item MANGROVE_SLAB = registerBlock(Blocks.MANGROVE_SLAB);
/*  354 */   public static final Item BAMBOO_SLAB = registerBlock(Blocks.BAMBOO_SLAB);
/*  355 */   public static final Item BAMBOO_MOSAIC_SLAB = registerBlock(Blocks.BAMBOO_MOSAIC_SLAB);
/*  356 */   public static final Item CRIMSON_SLAB = registerBlock(Blocks.CRIMSON_SLAB);
/*  357 */   public static final Item WARPED_SLAB = registerBlock(Blocks.WARPED_SLAB);
/*  358 */   public static final Item STONE_SLAB = registerBlock(Blocks.STONE_SLAB);
/*  359 */   public static final Item SMOOTH_STONE_SLAB = registerBlock(Blocks.SMOOTH_STONE_SLAB);
/*  360 */   public static final Item SANDSTONE_SLAB = registerBlock(Blocks.SANDSTONE_SLAB);
/*  361 */   public static final Item CUT_STANDSTONE_SLAB = registerBlock(Blocks.CUT_SANDSTONE_SLAB);
/*  362 */   public static final Item PETRIFIED_OAK_SLAB = registerBlock(Blocks.PETRIFIED_OAK_SLAB);
/*  363 */   public static final Item COBBLESTONE_SLAB = registerBlock(Blocks.COBBLESTONE_SLAB);
/*  364 */   public static final Item BRICK_SLAB = registerBlock(Blocks.BRICK_SLAB);
/*  365 */   public static final Item STONE_BRICK_SLAB = registerBlock(Blocks.STONE_BRICK_SLAB);
/*  366 */   public static final Item MUD_BRICK_SLAB = registerBlock(Blocks.MUD_BRICK_SLAB);
/*  367 */   public static final Item NETHER_BRICK_SLAB = registerBlock(Blocks.NETHER_BRICK_SLAB);
/*  368 */   public static final Item QUARTZ_SLAB = registerBlock(Blocks.QUARTZ_SLAB);
/*  369 */   public static final Item RED_SANDSTONE_SLAB = registerBlock(Blocks.RED_SANDSTONE_SLAB);
/*  370 */   public static final Item CUT_RED_SANDSTONE_SLAB = registerBlock(Blocks.CUT_RED_SANDSTONE_SLAB);
/*  371 */   public static final Item PURPUR_SLAB = registerBlock(Blocks.PURPUR_SLAB);
/*  372 */   public static final Item PRISMARINE_SLAB = registerBlock(Blocks.PRISMARINE_SLAB);
/*  373 */   public static final Item PRISMARINE_BRICK_SLAB = registerBlock(Blocks.PRISMARINE_BRICK_SLAB);
/*  374 */   public static final Item DARK_PRISMARINE_SLAB = registerBlock(Blocks.DARK_PRISMARINE_SLAB);
/*  375 */   public static final Item SMOOTH_QUARTZ = registerBlock(Blocks.SMOOTH_QUARTZ);
/*  376 */   public static final Item SMOOTH_RED_SANDSTONE = registerBlock(Blocks.SMOOTH_RED_SANDSTONE);
/*  377 */   public static final Item SMOOTH_SANDSTONE = registerBlock(Blocks.SMOOTH_SANDSTONE);
/*  378 */   public static final Item SMOOTH_STONE = registerBlock(Blocks.SMOOTH_STONE);
/*  379 */   public static final Item BRICKS = registerBlock(Blocks.BRICKS); public static final Item ACACIA_SHELF; public static final Item BAMBOO_SHELF; public static final Item BIRCH_SHELF; public static final Item CHERRY_SHELF; public static final Item CRIMSON_SHELF; public static final Item DARK_OAK_SHELF; public static final Item JUNGLE_SHELF; public static final Item MANGROVE_SHELF; public static final Item OAK_SHELF; public static final Item PALE_OAK_SHELF; public static final Item SPRUCE_SHELF; public static final Item WARPED_SHELF; static {
/*  380 */     ACACIA_SHELF = registerBlock(Blocks.ACACIA_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  381 */     BAMBOO_SHELF = registerBlock(Blocks.BAMBOO_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  382 */     BIRCH_SHELF = registerBlock(Blocks.BIRCH_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  383 */     CHERRY_SHELF = registerBlock(Blocks.CHERRY_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  384 */     CRIMSON_SHELF = registerBlock(Blocks.CRIMSON_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  385 */     DARK_OAK_SHELF = registerBlock(Blocks.DARK_OAK_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  386 */     JUNGLE_SHELF = registerBlock(Blocks.JUNGLE_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  387 */     MANGROVE_SHELF = registerBlock(Blocks.MANGROVE_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  388 */     OAK_SHELF = registerBlock(Blocks.OAK_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  389 */     PALE_OAK_SHELF = registerBlock(Blocks.PALE_OAK_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  390 */     SPRUCE_SHELF = registerBlock(Blocks.SPRUCE_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  391 */     WARPED_SHELF = registerBlock(Blocks.WARPED_SHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  392 */   } public static final Item BOOKSHELF = registerBlock(Blocks.BOOKSHELF); public static final Item CHISELED_BOOKSHELF; static {
/*  393 */     CHISELED_BOOKSHELF = registerBlock(Blocks.CHISELED_BOOKSHELF, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  394 */   } public static final Item DECORATED_POT = registerBlock(Blocks.DECORATED_POT, (new Item.Properties()).<PotDecorations>component(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  395 */   public static final Item MOSSY_COBBLESTONE = registerBlock(Blocks.MOSSY_COBBLESTONE);
/*  396 */   public static final Item OBSIDIAN = registerBlock(Blocks.OBSIDIAN); public static final Item TORCH; static {
/*  397 */     TORCH = registerBlock(Blocks.TORCH, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.WALL_TORCH, Direction.DOWN, paramProperties));
/*  398 */   } public static final Item END_ROD = registerBlock(Blocks.END_ROD);
/*  399 */   public static final Item CHORUS_PLANT = registerBlock(Blocks.CHORUS_PLANT);
/*  400 */   public static final Item CHORUS_FLOWER = registerBlock(Blocks.CHORUS_FLOWER);
/*  401 */   public static final Item PURPUR_BLOCK = registerBlock(Blocks.PURPUR_BLOCK);
/*  402 */   public static final Item PURPUR_PILLAR = registerBlock(Blocks.PURPUR_PILLAR);
/*  403 */   public static final Item PURPUR_STAIRS = registerBlock(Blocks.PURPUR_STAIRS);
/*  404 */   public static final Item SPAWNER = registerBlock(Blocks.SPAWNER);
/*  405 */   public static final Item CREAKING_HEART = registerBlock(Blocks.CREAKING_HEART); public static final Item CHEST; static {
/*  406 */     CHEST = registerBlock(Blocks.CHEST, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  407 */   } public static final Item CRAFTING_TABLE = registerBlock(Blocks.CRAFTING_TABLE);
/*  408 */   public static final Item FARMLAND = registerBlock(Blocks.FARMLAND); public static final Item FURNACE; static {
/*  409 */     FURNACE = registerBlock(Blocks.FURNACE, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  410 */   } public static final Item LADDER = registerBlock(Blocks.LADDER);
/*  411 */   public static final Item COBBLESTONE_STAIRS = registerBlock(Blocks.COBBLESTONE_STAIRS);
/*  412 */   public static final Item SNOW = registerBlock(Blocks.SNOW);
/*  413 */   public static final Item ICE = registerBlock(Blocks.ICE);
/*  414 */   public static final Item SNOW_BLOCK = registerBlock(Blocks.SNOW_BLOCK);
/*  415 */   public static final Item CACTUS = registerBlock(Blocks.CACTUS);
/*  416 */   public static final Item CACTUS_FLOWER = registerBlock(Blocks.CACTUS_FLOWER);
/*  417 */   public static final Item CLAY = registerBlock(Blocks.CLAY);
/*  418 */   public static final Item JUKEBOX = registerBlock(Blocks.JUKEBOX);
/*  419 */   public static final Item OAK_FENCE = registerBlock(Blocks.OAK_FENCE);
/*  420 */   public static final Item SPRUCE_FENCE = registerBlock(Blocks.SPRUCE_FENCE);
/*  421 */   public static final Item BIRCH_FENCE = registerBlock(Blocks.BIRCH_FENCE);
/*  422 */   public static final Item JUNGLE_FENCE = registerBlock(Blocks.JUNGLE_FENCE);
/*  423 */   public static final Item ACACIA_FENCE = registerBlock(Blocks.ACACIA_FENCE);
/*  424 */   public static final Item CHERRY_FENCE = registerBlock(Blocks.CHERRY_FENCE);
/*  425 */   public static final Item DARK_OAK_FENCE = registerBlock(Blocks.DARK_OAK_FENCE);
/*  426 */   public static final Item PALE_OAK_FENCE = registerBlock(Blocks.PALE_OAK_FENCE);
/*  427 */   public static final Item MANGROVE_FENCE = registerBlock(Blocks.MANGROVE_FENCE);
/*  428 */   public static final Item BAMBOO_FENCE = registerBlock(Blocks.BAMBOO_FENCE);
/*  429 */   public static final Item CRIMSON_FENCE = registerBlock(Blocks.CRIMSON_FENCE);
/*  430 */   public static final Item WARPED_FENCE = registerBlock(Blocks.WARPED_FENCE);
/*  431 */   public static final Item PUMPKIN = registerBlock(Blocks.PUMPKIN); public static final Item CARVED_PUMPKIN; static {
/*  432 */     CARVED_PUMPKIN = registerBlock(Blocks.CARVED_PUMPKIN, paramProperties -> Waypoint.addHideAttribute(paramProperties).component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur")).build()));
/*  433 */   } public static final Item JACK_O_LANTERN = registerBlock(Blocks.JACK_O_LANTERN);
/*  434 */   public static final Item NETHERRACK = registerBlock(Blocks.NETHERRACK);
/*  435 */   public static final Item SOUL_SAND = registerBlock(Blocks.SOUL_SAND);
/*  436 */   public static final Item SOUL_SOIL = registerBlock(Blocks.SOUL_SOIL);
/*  437 */   public static final Item BASALT = registerBlock(Blocks.BASALT);
/*  438 */   public static final Item POLISHED_BASALT = registerBlock(Blocks.POLISHED_BASALT);
/*  439 */   public static final Item SMOOTH_BASALT = registerBlock(Blocks.SMOOTH_BASALT); public static final Item SOUL_TORCH; public static final Item COPPER_TORCH; static {
/*  440 */     SOUL_TORCH = registerBlock(Blocks.SOUL_TORCH, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.SOUL_WALL_TORCH, Direction.DOWN, paramProperties));
/*  441 */     COPPER_TORCH = registerBlock(Blocks.COPPER_TORCH, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.COPPER_WALL_TORCH, Direction.DOWN, paramProperties));
/*  442 */   } public static final Item GLOWSTONE = registerBlock(Blocks.GLOWSTONE);
/*  443 */   public static final Item INFESTED_STONE = registerBlock(Blocks.INFESTED_STONE);
/*  444 */   public static final Item INFESTED_COBBLESTONE = registerBlock(Blocks.INFESTED_COBBLESTONE);
/*  445 */   public static final Item INFESTED_STONE_BRICKS = registerBlock(Blocks.INFESTED_STONE_BRICKS);
/*  446 */   public static final Item INFESTED_MOSSY_STONE_BRICKS = registerBlock(Blocks.INFESTED_MOSSY_STONE_BRICKS);
/*  447 */   public static final Item INFESTED_CRACKED_STONE_BRICKS = registerBlock(Blocks.INFESTED_CRACKED_STONE_BRICKS);
/*  448 */   public static final Item INFESTED_CHISELED_STONE_BRICKS = registerBlock(Blocks.INFESTED_CHISELED_STONE_BRICKS);
/*  449 */   public static final Item INFESTED_DEEPSLATE = registerBlock(Blocks.INFESTED_DEEPSLATE);
/*  450 */   public static final Item STONE_BRICKS = registerBlock(Blocks.STONE_BRICKS);
/*  451 */   public static final Item MOSSY_STONE_BRICKS = registerBlock(Blocks.MOSSY_STONE_BRICKS);
/*  452 */   public static final Item CRACKED_STONE_BRICKS = registerBlock(Blocks.CRACKED_STONE_BRICKS);
/*  453 */   public static final Item CHISELED_STONE_BRICKS = registerBlock(Blocks.CHISELED_STONE_BRICKS);
/*  454 */   public static final Item PACKED_MUD = registerBlock(Blocks.PACKED_MUD);
/*  455 */   public static final Item MUD_BRICKS = registerBlock(Blocks.MUD_BRICKS);
/*  456 */   public static final Item DEEPSLATE_BRICKS = registerBlock(Blocks.DEEPSLATE_BRICKS);
/*  457 */   public static final Item CRACKED_DEEPSLATE_BRICKS = registerBlock(Blocks.CRACKED_DEEPSLATE_BRICKS);
/*  458 */   public static final Item DEEPSLATE_TILES = registerBlock(Blocks.DEEPSLATE_TILES);
/*  459 */   public static final Item CRACKED_DEEPSLATE_TILES = registerBlock(Blocks.CRACKED_DEEPSLATE_TILES);
/*  460 */   public static final Item CHISELED_DEEPSLATE = registerBlock(Blocks.CHISELED_DEEPSLATE);
/*  461 */   public static final Item REINFORCED_DEEPSLATE = registerBlock(Blocks.REINFORCED_DEEPSLATE);
/*  462 */   public static final Item BROWN_MUSHROOM_BLOCK = registerBlock(Blocks.BROWN_MUSHROOM_BLOCK);
/*  463 */   public static final Item RED_MUSHROOM_BLOCK = registerBlock(Blocks.RED_MUSHROOM_BLOCK);
/*  464 */   public static final Item MUSHROOM_STEM = registerBlock(Blocks.MUSHROOM_STEM);
/*  465 */   public static final Item IRON_BARS = registerBlock(Blocks.IRON_BARS);
/*  466 */   public static final WeatheringCopperItems COPPER_BARS = WeatheringCopperItems.create(Blocks.COPPER_BARS, Items::registerBlock);
/*  467 */   public static final Item IRON_CHAIN = registerBlock(Blocks.IRON_CHAIN);
/*  468 */   public static final WeatheringCopperItems COPPER_CHAIN = WeatheringCopperItems.create(Blocks.COPPER_CHAIN, Items::registerBlock);
/*  469 */   public static final Item GLASS_PANE = registerBlock(Blocks.GLASS_PANE);
/*  470 */   public static final Item MELON = registerBlock(Blocks.MELON);
/*  471 */   public static final Item VINE = registerBlock(Blocks.VINE);
/*  472 */   public static final Item GLOW_LICHEN = registerBlock(Blocks.GLOW_LICHEN);
/*  473 */   public static final Item RESIN_CLUMP = registerItem("resin_clump", createBlockItemWithCustomItemName(Blocks.RESIN_CLUMP));
/*  474 */   public static final Item RESIN_BLOCK = registerBlock(Blocks.RESIN_BLOCK);
/*  475 */   public static final Item RESIN_BRICKS = registerBlock(Blocks.RESIN_BRICKS);
/*  476 */   public static final Item RESIN_BRICK_STAIRS = registerBlock(Blocks.RESIN_BRICK_STAIRS);
/*  477 */   public static final Item RESIN_BRICK_SLAB = registerBlock(Blocks.RESIN_BRICK_SLAB);
/*  478 */   public static final Item RESIN_BRICK_WALL = registerBlock(Blocks.RESIN_BRICK_WALL);
/*  479 */   public static final Item CHISELED_RESIN_BRICKS = registerBlock(Blocks.CHISELED_RESIN_BRICKS);
/*      */   
/*  481 */   public static final Item BRICK_STAIRS = registerBlock(Blocks.BRICK_STAIRS);
/*  482 */   public static final Item STONE_BRICK_STAIRS = registerBlock(Blocks.STONE_BRICK_STAIRS);
/*  483 */   public static final Item MUD_BRICK_STAIRS = registerBlock(Blocks.MUD_BRICK_STAIRS);
/*  484 */   public static final Item MYCELIUM = registerBlock(Blocks.MYCELIUM);
/*  485 */   public static final Item LILY_PAD = registerBlock(Blocks.LILY_PAD, PlaceOnWaterBlockItem::new);
/*  486 */   public static final Item NETHER_BRICKS = registerBlock(Blocks.NETHER_BRICKS);
/*  487 */   public static final Item CRACKED_NETHER_BRICKS = registerBlock(Blocks.CRACKED_NETHER_BRICKS);
/*  488 */   public static final Item CHISELED_NETHER_BRICKS = registerBlock(Blocks.CHISELED_NETHER_BRICKS);
/*  489 */   public static final Item NETHER_BRICK_FENCE = registerBlock(Blocks.NETHER_BRICK_FENCE);
/*  490 */   public static final Item NETHER_BRICK_STAIRS = registerBlock(Blocks.NETHER_BRICK_STAIRS);
/*      */   
/*  492 */   public static final Item SCULK = registerBlock(Blocks.SCULK);
/*  493 */   public static final Item SCULK_VEIN = registerBlock(Blocks.SCULK_VEIN);
/*  494 */   public static final Item SCULK_CATALYST = registerBlock(Blocks.SCULK_CATALYST);
/*  495 */   public static final Item SCULK_SHRIEKER = registerBlock(Blocks.SCULK_SHRIEKER);
/*      */   
/*  497 */   public static final Item ENCHANTING_TABLE = registerBlock(Blocks.ENCHANTING_TABLE);
/*  498 */   public static final Item END_PORTAL_FRAME = registerBlock(Blocks.END_PORTAL_FRAME);
/*  499 */   public static final Item END_STONE = registerBlock(Blocks.END_STONE);
/*  500 */   public static final Item END_STONE_BRICKS = registerBlock(Blocks.END_STONE_BRICKS);
/*  501 */   public static final Item DRAGON_EGG = registerBlock(Blocks.DRAGON_EGG, (new Item.Properties()).rarity(Rarity.EPIC));
/*  502 */   public static final Item SANDSTONE_STAIRS = registerBlock(Blocks.SANDSTONE_STAIRS);
/*  503 */   public static final Item ENDER_CHEST = registerBlock(Blocks.ENDER_CHEST);
/*  504 */   public static final Item EMERALD_BLOCK = registerBlock(Blocks.EMERALD_BLOCK);
/*  505 */   public static final Item OAK_STAIRS = registerBlock(Blocks.OAK_STAIRS);
/*  506 */   public static final Item SPRUCE_STAIRS = registerBlock(Blocks.SPRUCE_STAIRS);
/*  507 */   public static final Item BIRCH_STAIRS = registerBlock(Blocks.BIRCH_STAIRS);
/*  508 */   public static final Item JUNGLE_STAIRS = registerBlock(Blocks.JUNGLE_STAIRS);
/*  509 */   public static final Item ACACIA_STAIRS = registerBlock(Blocks.ACACIA_STAIRS);
/*  510 */   public static final Item CHERRY_STAIRS = registerBlock(Blocks.CHERRY_STAIRS);
/*  511 */   public static final Item DARK_OAK_STAIRS = registerBlock(Blocks.DARK_OAK_STAIRS);
/*  512 */   public static final Item PALE_OAK_STAIRS = registerBlock(Blocks.PALE_OAK_STAIRS);
/*  513 */   public static final Item MANGROVE_STAIRS = registerBlock(Blocks.MANGROVE_STAIRS);
/*  514 */   public static final Item BAMBOO_STAIRS = registerBlock(Blocks.BAMBOO_STAIRS);
/*  515 */   public static final Item BAMBOO_MOSAIC_STAIRS = registerBlock(Blocks.BAMBOO_MOSAIC_STAIRS);
/*  516 */   public static final Item CRIMSON_STAIRS = registerBlock(Blocks.CRIMSON_STAIRS);
/*  517 */   public static final Item WARPED_STAIRS = registerBlock(Blocks.WARPED_STAIRS);
/*  518 */   public static final Item COMMAND_BLOCK = registerBlock(Blocks.COMMAND_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  519 */   public static final Item BEACON = registerBlock(Blocks.BEACON, (new Item.Properties()).rarity(Rarity.RARE));
/*      */   
/*  521 */   public static final Item COBBLESTONE_WALL = registerBlock(Blocks.COBBLESTONE_WALL);
/*  522 */   public static final Item MOSSY_COBBLESTONE_WALL = registerBlock(Blocks.MOSSY_COBBLESTONE_WALL);
/*  523 */   public static final Item BRICK_WALL = registerBlock(Blocks.BRICK_WALL);
/*  524 */   public static final Item PRISMARINE_WALL = registerBlock(Blocks.PRISMARINE_WALL);
/*  525 */   public static final Item RED_SANDSTONE_WALL = registerBlock(Blocks.RED_SANDSTONE_WALL);
/*  526 */   public static final Item MOSSY_STONE_BRICK_WALL = registerBlock(Blocks.MOSSY_STONE_BRICK_WALL);
/*  527 */   public static final Item GRANITE_WALL = registerBlock(Blocks.GRANITE_WALL);
/*  528 */   public static final Item STONE_BRICK_WALL = registerBlock(Blocks.STONE_BRICK_WALL);
/*  529 */   public static final Item MUD_BRICK_WALL = registerBlock(Blocks.MUD_BRICK_WALL);
/*  530 */   public static final Item NETHER_BRICK_WALL = registerBlock(Blocks.NETHER_BRICK_WALL);
/*  531 */   public static final Item ANDESITE_WALL = registerBlock(Blocks.ANDESITE_WALL);
/*  532 */   public static final Item RED_NETHER_BRICK_WALL = registerBlock(Blocks.RED_NETHER_BRICK_WALL);
/*  533 */   public static final Item SANDSTONE_WALL = registerBlock(Blocks.SANDSTONE_WALL);
/*  534 */   public static final Item END_STONE_BRICK_WALL = registerBlock(Blocks.END_STONE_BRICK_WALL);
/*  535 */   public static final Item DIORITE_WALL = registerBlock(Blocks.DIORITE_WALL);
/*  536 */   public static final Item BLACKSTONE_WALL = registerBlock(Blocks.BLACKSTONE_WALL);
/*  537 */   public static final Item POLISHED_BLACKSTONE_WALL = registerBlock(Blocks.POLISHED_BLACKSTONE_WALL);
/*  538 */   public static final Item POLISHED_BLACKSTONE_BRICK_WALL = registerBlock(Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*  539 */   public static final Item COBBLED_DEEPSLATE_WALL = registerBlock(Blocks.COBBLED_DEEPSLATE_WALL);
/*  540 */   public static final Item POLISHED_DEEPSLATE_WALL = registerBlock(Blocks.POLISHED_DEEPSLATE_WALL);
/*  541 */   public static final Item DEEPSLATE_BRICK_WALL = registerBlock(Blocks.DEEPSLATE_BRICK_WALL);
/*  542 */   public static final Item DEEPSLATE_TILE_WALL = registerBlock(Blocks.DEEPSLATE_TILE_WALL);
/*      */   
/*  544 */   public static final Item ANVIL = registerBlock(Blocks.ANVIL);
/*  545 */   public static final Item CHIPPED_ANVIL = registerBlock(Blocks.CHIPPED_ANVIL);
/*  546 */   public static final Item DAMAGED_ANVIL = registerBlock(Blocks.DAMAGED_ANVIL);
/*  547 */   public static final Item CHISELED_QUARTZ_BLOCK = registerBlock(Blocks.CHISELED_QUARTZ_BLOCK);
/*  548 */   public static final Item QUARTZ_BLOCK = registerBlock(Blocks.QUARTZ_BLOCK);
/*  549 */   public static final Item QUARTZ_BRICKS = registerBlock(Blocks.QUARTZ_BRICKS);
/*  550 */   public static final Item QUARTZ_PILLAR = registerBlock(Blocks.QUARTZ_PILLAR);
/*  551 */   public static final Item QUARTZ_STAIRS = registerBlock(Blocks.QUARTZ_STAIRS);
/*  552 */   public static final Item WHITE_TERRACOTTA = registerBlock(Blocks.WHITE_TERRACOTTA);
/*  553 */   public static final Item ORANGE_TERRACOTTA = registerBlock(Blocks.ORANGE_TERRACOTTA);
/*  554 */   public static final Item MAGENTA_TERRACOTTA = registerBlock(Blocks.MAGENTA_TERRACOTTA);
/*  555 */   public static final Item LIGHT_BLUE_TERRACOTTA = registerBlock(Blocks.LIGHT_BLUE_TERRACOTTA);
/*  556 */   public static final Item YELLOW_TERRACOTTA = registerBlock(Blocks.YELLOW_TERRACOTTA);
/*  557 */   public static final Item LIME_TERRACOTTA = registerBlock(Blocks.LIME_TERRACOTTA);
/*  558 */   public static final Item PINK_TERRACOTTA = registerBlock(Blocks.PINK_TERRACOTTA);
/*  559 */   public static final Item GRAY_TERRACOTTA = registerBlock(Blocks.GRAY_TERRACOTTA);
/*  560 */   public static final Item LIGHT_GRAY_TERRACOTTA = registerBlock(Blocks.LIGHT_GRAY_TERRACOTTA);
/*  561 */   public static final Item CYAN_TERRACOTTA = registerBlock(Blocks.CYAN_TERRACOTTA);
/*  562 */   public static final Item PURPLE_TERRACOTTA = registerBlock(Blocks.PURPLE_TERRACOTTA);
/*  563 */   public static final Item BLUE_TERRACOTTA = registerBlock(Blocks.BLUE_TERRACOTTA);
/*  564 */   public static final Item BROWN_TERRACOTTA = registerBlock(Blocks.BROWN_TERRACOTTA);
/*  565 */   public static final Item GREEN_TERRACOTTA = registerBlock(Blocks.GREEN_TERRACOTTA);
/*  566 */   public static final Item RED_TERRACOTTA = registerBlock(Blocks.RED_TERRACOTTA);
/*  567 */   public static final Item BLACK_TERRACOTTA = registerBlock(Blocks.BLACK_TERRACOTTA);
/*  568 */   public static final Item BARRIER = registerBlock(Blocks.BARRIER, (new Item.Properties()).rarity(Rarity.EPIC)); public static final Item LIGHT; static {
/*  569 */     LIGHT = registerBlock(Blocks.LIGHT, paramProperties -> paramProperties.rarity(Rarity.EPIC).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)LightBlock.LEVEL, Integer.valueOf(15))));
/*  570 */   } public static final Item HAY_BLOCK = registerBlock(Blocks.HAY_BLOCK); public static final Item WHITE_CARPET; public static final Item ORANGE_CARPET; public static final Item MAGENTA_CARPET; public static final Item LIGHT_BLUE_CARPET; public static final Item YELLOW_CARPET; public static final Item LIME_CARPET; public static final Item PINK_CARPET; public static final Item GRAY_CARPET; public static final Item LIGHT_GRAY_CARPET; public static final Item CYAN_CARPET; public static final Item PURPLE_CARPET; public static final Item BLUE_CARPET; public static final Item BROWN_CARPET; public static final Item GREEN_CARPET; public static final Item RED_CARPET; public static final Item BLACK_CARPET; static {
/*  571 */     WHITE_CARPET = registerBlock(Blocks.WHITE_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.WHITE)));
/*  572 */     ORANGE_CARPET = registerBlock(Blocks.ORANGE_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.ORANGE)));
/*  573 */     MAGENTA_CARPET = registerBlock(Blocks.MAGENTA_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.MAGENTA)));
/*  574 */     LIGHT_BLUE_CARPET = registerBlock(Blocks.LIGHT_BLUE_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.LIGHT_BLUE)));
/*  575 */     YELLOW_CARPET = registerBlock(Blocks.YELLOW_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.YELLOW)));
/*  576 */     LIME_CARPET = registerBlock(Blocks.LIME_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.LIME)));
/*  577 */     PINK_CARPET = registerBlock(Blocks.PINK_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.PINK)));
/*  578 */     GRAY_CARPET = registerBlock(Blocks.GRAY_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.GRAY)));
/*  579 */     LIGHT_GRAY_CARPET = registerBlock(Blocks.LIGHT_GRAY_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.LIGHT_GRAY)));
/*  580 */     CYAN_CARPET = registerBlock(Blocks.CYAN_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.CYAN)));
/*  581 */     PURPLE_CARPET = registerBlock(Blocks.PURPLE_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.PURPLE)));
/*  582 */     BLUE_CARPET = registerBlock(Blocks.BLUE_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.BLUE)));
/*  583 */     BROWN_CARPET = registerBlock(Blocks.BROWN_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.BROWN)));
/*  584 */     GREEN_CARPET = registerBlock(Blocks.GREEN_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.GREEN)));
/*  585 */     RED_CARPET = registerBlock(Blocks.RED_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.RED)));
/*  586 */     BLACK_CARPET = registerBlock(Blocks.BLACK_CARPET, paramProperties -> paramProperties.component(DataComponents.EQUIPPABLE, Equippable.llamaSwag(DyeColor.BLACK)));
/*  587 */   } public static final Item TERRACOTTA = registerBlock(Blocks.TERRACOTTA);
/*  588 */   public static final Item PACKED_ICE = registerBlock(Blocks.PACKED_ICE);
/*  589 */   public static final Item DIRT_PATH = registerBlock(Blocks.DIRT_PATH);
/*  590 */   public static final Item SUNFLOWER = registerBlock(Blocks.SUNFLOWER, DoubleHighBlockItem::new);
/*  591 */   public static final Item LILAC = registerBlock(Blocks.LILAC, DoubleHighBlockItem::new);
/*  592 */   public static final Item ROSE_BUSH = registerBlock(Blocks.ROSE_BUSH, DoubleHighBlockItem::new);
/*  593 */   public static final Item PEONY = registerBlock(Blocks.PEONY, DoubleHighBlockItem::new);
/*  594 */   public static final Item TALL_GRASS = registerBlock(Blocks.TALL_GRASS, DoubleHighBlockItem::new);
/*  595 */   public static final Item LARGE_FERN = registerBlock(Blocks.LARGE_FERN, DoubleHighBlockItem::new);
/*  596 */   public static final Item WHITE_STAINED_GLASS = registerBlock(Blocks.WHITE_STAINED_GLASS);
/*  597 */   public static final Item ORANGE_STAINED_GLASS = registerBlock(Blocks.ORANGE_STAINED_GLASS);
/*  598 */   public static final Item MAGENTA_STAINED_GLASS = registerBlock(Blocks.MAGENTA_STAINED_GLASS);
/*  599 */   public static final Item LIGHT_BLUE_STAINED_GLASS = registerBlock(Blocks.LIGHT_BLUE_STAINED_GLASS);
/*  600 */   public static final Item YELLOW_STAINED_GLASS = registerBlock(Blocks.YELLOW_STAINED_GLASS);
/*  601 */   public static final Item LIME_STAINED_GLASS = registerBlock(Blocks.LIME_STAINED_GLASS);
/*  602 */   public static final Item PINK_STAINED_GLASS = registerBlock(Blocks.PINK_STAINED_GLASS);
/*  603 */   public static final Item GRAY_STAINED_GLASS = registerBlock(Blocks.GRAY_STAINED_GLASS);
/*  604 */   public static final Item LIGHT_GRAY_STAINED_GLASS = registerBlock(Blocks.LIGHT_GRAY_STAINED_GLASS);
/*  605 */   public static final Item CYAN_STAINED_GLASS = registerBlock(Blocks.CYAN_STAINED_GLASS);
/*  606 */   public static final Item PURPLE_STAINED_GLASS = registerBlock(Blocks.PURPLE_STAINED_GLASS);
/*  607 */   public static final Item BLUE_STAINED_GLASS = registerBlock(Blocks.BLUE_STAINED_GLASS);
/*  608 */   public static final Item BROWN_STAINED_GLASS = registerBlock(Blocks.BROWN_STAINED_GLASS);
/*  609 */   public static final Item GREEN_STAINED_GLASS = registerBlock(Blocks.GREEN_STAINED_GLASS);
/*  610 */   public static final Item RED_STAINED_GLASS = registerBlock(Blocks.RED_STAINED_GLASS);
/*  611 */   public static final Item BLACK_STAINED_GLASS = registerBlock(Blocks.BLACK_STAINED_GLASS);
/*  612 */   public static final Item WHITE_STAINED_GLASS_PANE = registerBlock(Blocks.WHITE_STAINED_GLASS_PANE);
/*  613 */   public static final Item ORANGE_STAINED_GLASS_PANE = registerBlock(Blocks.ORANGE_STAINED_GLASS_PANE);
/*  614 */   public static final Item MAGENTA_STAINED_GLASS_PANE = registerBlock(Blocks.MAGENTA_STAINED_GLASS_PANE);
/*  615 */   public static final Item LIGHT_BLUE_STAINED_GLASS_PANE = registerBlock(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
/*  616 */   public static final Item YELLOW_STAINED_GLASS_PANE = registerBlock(Blocks.YELLOW_STAINED_GLASS_PANE);
/*  617 */   public static final Item LIME_STAINED_GLASS_PANE = registerBlock(Blocks.LIME_STAINED_GLASS_PANE);
/*  618 */   public static final Item PINK_STAINED_GLASS_PANE = registerBlock(Blocks.PINK_STAINED_GLASS_PANE);
/*  619 */   public static final Item GRAY_STAINED_GLASS_PANE = registerBlock(Blocks.GRAY_STAINED_GLASS_PANE);
/*  620 */   public static final Item LIGHT_GRAY_STAINED_GLASS_PANE = registerBlock(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
/*  621 */   public static final Item CYAN_STAINED_GLASS_PANE = registerBlock(Blocks.CYAN_STAINED_GLASS_PANE);
/*  622 */   public static final Item PURPLE_STAINED_GLASS_PANE = registerBlock(Blocks.PURPLE_STAINED_GLASS_PANE);
/*  623 */   public static final Item BLUE_STAINED_GLASS_PANE = registerBlock(Blocks.BLUE_STAINED_GLASS_PANE);
/*  624 */   public static final Item BROWN_STAINED_GLASS_PANE = registerBlock(Blocks.BROWN_STAINED_GLASS_PANE);
/*  625 */   public static final Item GREEN_STAINED_GLASS_PANE = registerBlock(Blocks.GREEN_STAINED_GLASS_PANE);
/*  626 */   public static final Item RED_STAINED_GLASS_PANE = registerBlock(Blocks.RED_STAINED_GLASS_PANE);
/*  627 */   public static final Item BLACK_STAINED_GLASS_PANE = registerBlock(Blocks.BLACK_STAINED_GLASS_PANE);
/*  628 */   public static final Item PRISMARINE = registerBlock(Blocks.PRISMARINE);
/*  629 */   public static final Item PRISMARINE_BRICKS = registerBlock(Blocks.PRISMARINE_BRICKS);
/*  630 */   public static final Item DARK_PRISMARINE = registerBlock(Blocks.DARK_PRISMARINE);
/*  631 */   public static final Item PRISMARINE_STAIRS = registerBlock(Blocks.PRISMARINE_STAIRS);
/*  632 */   public static final Item PRISMARINE_BRICK_STAIRS = registerBlock(Blocks.PRISMARINE_BRICK_STAIRS);
/*  633 */   public static final Item DARK_PRISMARINE_STAIRS = registerBlock(Blocks.DARK_PRISMARINE_STAIRS);
/*  634 */   public static final Item SEA_LANTERN = registerBlock(Blocks.SEA_LANTERN);
/*  635 */   public static final Item RED_SANDSTONE = registerBlock(Blocks.RED_SANDSTONE);
/*  636 */   public static final Item CHISELED_RED_SANDSTONE = registerBlock(Blocks.CHISELED_RED_SANDSTONE);
/*  637 */   public static final Item CUT_RED_SANDSTONE = registerBlock(Blocks.CUT_RED_SANDSTONE);
/*  638 */   public static final Item RED_SANDSTONE_STAIRS = registerBlock(Blocks.RED_SANDSTONE_STAIRS);
/*  639 */   public static final Item REPEATING_COMMAND_BLOCK = registerBlock(Blocks.REPEATING_COMMAND_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  640 */   public static final Item CHAIN_COMMAND_BLOCK = registerBlock(Blocks.CHAIN_COMMAND_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  641 */   public static final Item MAGMA_BLOCK = registerBlock(Blocks.MAGMA_BLOCK);
/*  642 */   public static final Item NETHER_WART_BLOCK = registerBlock(Blocks.NETHER_WART_BLOCK);
/*  643 */   public static final Item WARPED_WART_BLOCK = registerBlock(Blocks.WARPED_WART_BLOCK);
/*  644 */   public static final Item RED_NETHER_BRICKS = registerBlock(Blocks.RED_NETHER_BRICKS);
/*  645 */   public static final Item BONE_BLOCK = registerBlock(Blocks.BONE_BLOCK);
/*  646 */   public static final Item STRUCTURE_VOID = registerBlock(Blocks.STRUCTURE_VOID, (new Item.Properties()).rarity(Rarity.EPIC));
/*  647 */   public static final Item SHULKER_BOX = registerBlock(Blocks.SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  648 */   public static final Item WHITE_SHULKER_BOX = registerBlock(Blocks.WHITE_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  649 */   public static final Item ORANGE_SHULKER_BOX = registerBlock(Blocks.ORANGE_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  650 */   public static final Item MAGENTA_SHULKER_BOX = registerBlock(Blocks.MAGENTA_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  651 */   public static final Item LIGHT_BLUE_SHULKER_BOX = registerBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  652 */   public static final Item YELLOW_SHULKER_BOX = registerBlock(Blocks.YELLOW_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  653 */   public static final Item LIME_SHULKER_BOX = registerBlock(Blocks.LIME_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  654 */   public static final Item PINK_SHULKER_BOX = registerBlock(Blocks.PINK_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  655 */   public static final Item GRAY_SHULKER_BOX = registerBlock(Blocks.GRAY_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  656 */   public static final Item LIGHT_GRAY_SHULKER_BOX = registerBlock(Blocks.LIGHT_GRAY_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  657 */   public static final Item CYAN_SHULKER_BOX = registerBlock(Blocks.CYAN_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  658 */   public static final Item PURPLE_SHULKER_BOX = registerBlock(Blocks.PURPLE_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  659 */   public static final Item BLUE_SHULKER_BOX = registerBlock(Blocks.BLUE_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  660 */   public static final Item BROWN_SHULKER_BOX = registerBlock(Blocks.BROWN_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  661 */   public static final Item GREEN_SHULKER_BOX = registerBlock(Blocks.GREEN_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  662 */   public static final Item RED_SHULKER_BOX = registerBlock(Blocks.RED_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  663 */   public static final Item BLACK_SHULKER_BOX = registerBlock(Blocks.BLACK_SHULKER_BOX, (new Item.Properties()).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  664 */   public static final Item WHITE_GLAZED_TERRACOTTA = registerBlock(Blocks.WHITE_GLAZED_TERRACOTTA);
/*  665 */   public static final Item ORANGE_GLAZED_TERRACOTTA = registerBlock(Blocks.ORANGE_GLAZED_TERRACOTTA);
/*  666 */   public static final Item MAGENTA_GLAZED_TERRACOTTA = registerBlock(Blocks.MAGENTA_GLAZED_TERRACOTTA);
/*  667 */   public static final Item LIGHT_BLUE_GLAZED_TERRACOTTA = registerBlock(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
/*  668 */   public static final Item YELLOW_GLAZED_TERRACOTTA = registerBlock(Blocks.YELLOW_GLAZED_TERRACOTTA);
/*  669 */   public static final Item LIME_GLAZED_TERRACOTTA = registerBlock(Blocks.LIME_GLAZED_TERRACOTTA);
/*  670 */   public static final Item PINK_GLAZED_TERRACOTTA = registerBlock(Blocks.PINK_GLAZED_TERRACOTTA);
/*  671 */   public static final Item GRAY_GLAZED_TERRACOTTA = registerBlock(Blocks.GRAY_GLAZED_TERRACOTTA);
/*  672 */   public static final Item LIGHT_GRAY_GLAZED_TERRACOTTA = registerBlock(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
/*  673 */   public static final Item CYAN_GLAZED_TERRACOTTA = registerBlock(Blocks.CYAN_GLAZED_TERRACOTTA);
/*  674 */   public static final Item PURPLE_GLAZED_TERRACOTTA = registerBlock(Blocks.PURPLE_GLAZED_TERRACOTTA);
/*  675 */   public static final Item BLUE_GLAZED_TERRACOTTA = registerBlock(Blocks.BLUE_GLAZED_TERRACOTTA);
/*  676 */   public static final Item BROWN_GLAZED_TERRACOTTA = registerBlock(Blocks.BROWN_GLAZED_TERRACOTTA);
/*  677 */   public static final Item GREEN_GLAZED_TERRACOTTA = registerBlock(Blocks.GREEN_GLAZED_TERRACOTTA);
/*  678 */   public static final Item RED_GLAZED_TERRACOTTA = registerBlock(Blocks.RED_GLAZED_TERRACOTTA);
/*  679 */   public static final Item BLACK_GLAZED_TERRACOTTA = registerBlock(Blocks.BLACK_GLAZED_TERRACOTTA);
/*  680 */   public static final Item WHITE_CONCRETE = registerBlock(Blocks.WHITE_CONCRETE);
/*  681 */   public static final Item ORANGE_CONCRETE = registerBlock(Blocks.ORANGE_CONCRETE);
/*  682 */   public static final Item MAGENTA_CONCRETE = registerBlock(Blocks.MAGENTA_CONCRETE);
/*  683 */   public static final Item LIGHT_BLUE_CONCRETE = registerBlock(Blocks.LIGHT_BLUE_CONCRETE);
/*  684 */   public static final Item YELLOW_CONCRETE = registerBlock(Blocks.YELLOW_CONCRETE);
/*  685 */   public static final Item LIME_CONCRETE = registerBlock(Blocks.LIME_CONCRETE);
/*  686 */   public static final Item PINK_CONCRETE = registerBlock(Blocks.PINK_CONCRETE);
/*  687 */   public static final Item GRAY_CONCRETE = registerBlock(Blocks.GRAY_CONCRETE);
/*  688 */   public static final Item LIGHT_GRAY_CONCRETE = registerBlock(Blocks.LIGHT_GRAY_CONCRETE);
/*  689 */   public static final Item CYAN_CONCRETE = registerBlock(Blocks.CYAN_CONCRETE);
/*  690 */   public static final Item PURPLE_CONCRETE = registerBlock(Blocks.PURPLE_CONCRETE);
/*  691 */   public static final Item BLUE_CONCRETE = registerBlock(Blocks.BLUE_CONCRETE);
/*  692 */   public static final Item BROWN_CONCRETE = registerBlock(Blocks.BROWN_CONCRETE);
/*  693 */   public static final Item GREEN_CONCRETE = registerBlock(Blocks.GREEN_CONCRETE);
/*  694 */   public static final Item RED_CONCRETE = registerBlock(Blocks.RED_CONCRETE);
/*  695 */   public static final Item BLACK_CONCRETE = registerBlock(Blocks.BLACK_CONCRETE);
/*  696 */   public static final Item WHITE_CONCRETE_POWDER = registerBlock(Blocks.WHITE_CONCRETE_POWDER);
/*  697 */   public static final Item ORANGE_CONCRETE_POWDER = registerBlock(Blocks.ORANGE_CONCRETE_POWDER);
/*  698 */   public static final Item MAGENTA_CONCRETE_POWDER = registerBlock(Blocks.MAGENTA_CONCRETE_POWDER);
/*  699 */   public static final Item LIGHT_BLUE_CONCRETE_POWDER = registerBlock(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
/*  700 */   public static final Item YELLOW_CONCRETE_POWDER = registerBlock(Blocks.YELLOW_CONCRETE_POWDER);
/*  701 */   public static final Item LIME_CONCRETE_POWDER = registerBlock(Blocks.LIME_CONCRETE_POWDER);
/*  702 */   public static final Item PINK_CONCRETE_POWDER = registerBlock(Blocks.PINK_CONCRETE_POWDER);
/*  703 */   public static final Item GRAY_CONCRETE_POWDER = registerBlock(Blocks.GRAY_CONCRETE_POWDER);
/*  704 */   public static final Item LIGHT_GRAY_CONCRETE_POWDER = registerBlock(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
/*  705 */   public static final Item CYAN_CONCRETE_POWDER = registerBlock(Blocks.CYAN_CONCRETE_POWDER);
/*  706 */   public static final Item PURPLE_CONCRETE_POWDER = registerBlock(Blocks.PURPLE_CONCRETE_POWDER);
/*  707 */   public static final Item BLUE_CONCRETE_POWDER = registerBlock(Blocks.BLUE_CONCRETE_POWDER);
/*  708 */   public static final Item BROWN_CONCRETE_POWDER = registerBlock(Blocks.BROWN_CONCRETE_POWDER);
/*  709 */   public static final Item GREEN_CONCRETE_POWDER = registerBlock(Blocks.GREEN_CONCRETE_POWDER);
/*  710 */   public static final Item RED_CONCRETE_POWDER = registerBlock(Blocks.RED_CONCRETE_POWDER);
/*  711 */   public static final Item BLACK_CONCRETE_POWDER = registerBlock(Blocks.BLACK_CONCRETE_POWDER);
/*  712 */   public static final Item TURTLE_EGG = registerBlock(Blocks.TURTLE_EGG); public static final Item SNIFFER_EGG; static {
/*  713 */     SNIFFER_EGG = registerBlock(Blocks.SNIFFER_EGG, paramProperties -> paramProperties.rarity(Rarity.UNCOMMON));
/*  714 */   } public static final Item DRIED_GHAST = registerBlock(Blocks.DRIED_GHAST);
/*  715 */   public static final Item DEAD_TUBE_CORAL_BLOCK = registerBlock(Blocks.DEAD_TUBE_CORAL_BLOCK);
/*  716 */   public static final Item DEAD_BRAIN_CORAL_BLOCK = registerBlock(Blocks.DEAD_BRAIN_CORAL_BLOCK);
/*  717 */   public static final Item DEAD_BUBBLE_CORAL_BLOCK = registerBlock(Blocks.DEAD_BUBBLE_CORAL_BLOCK);
/*  718 */   public static final Item DEAD_FIRE_CORAL_BLOCK = registerBlock(Blocks.DEAD_FIRE_CORAL_BLOCK);
/*  719 */   public static final Item DEAD_HORN_CORAL_BLOCK = registerBlock(Blocks.DEAD_HORN_CORAL_BLOCK);
/*  720 */   public static final Item TUBE_CORAL_BLOCK = registerBlock(Blocks.TUBE_CORAL_BLOCK);
/*  721 */   public static final Item BRAIN_CORAL_BLOCK = registerBlock(Blocks.BRAIN_CORAL_BLOCK);
/*  722 */   public static final Item BUBBLE_CORAL_BLOCK = registerBlock(Blocks.BUBBLE_CORAL_BLOCK);
/*  723 */   public static final Item FIRE_CORAL_BLOCK = registerBlock(Blocks.FIRE_CORAL_BLOCK);
/*  724 */   public static final Item HORN_CORAL_BLOCK = registerBlock(Blocks.HORN_CORAL_BLOCK);
/*  725 */   public static final Item TUBE_CORAL = registerBlock(Blocks.TUBE_CORAL);
/*  726 */   public static final Item BRAIN_CORAL = registerBlock(Blocks.BRAIN_CORAL);
/*  727 */   public static final Item BUBBLE_CORAL = registerBlock(Blocks.BUBBLE_CORAL);
/*  728 */   public static final Item FIRE_CORAL = registerBlock(Blocks.FIRE_CORAL);
/*  729 */   public static final Item HORN_CORAL = registerBlock(Blocks.HORN_CORAL);
/*  730 */   public static final Item DEAD_BRAIN_CORAL = registerBlock(Blocks.DEAD_BRAIN_CORAL);
/*  731 */   public static final Item DEAD_BUBBLE_CORAL = registerBlock(Blocks.DEAD_BUBBLE_CORAL);
/*  732 */   public static final Item DEAD_FIRE_CORAL = registerBlock(Blocks.DEAD_FIRE_CORAL);
/*  733 */   public static final Item DEAD_HORN_CORAL = registerBlock(Blocks.DEAD_HORN_CORAL);
/*  734 */   public static final Item DEAD_TUBE_CORAL = registerBlock(Blocks.DEAD_TUBE_CORAL); public static final Item TUBE_CORAL_FAN; public static final Item BRAIN_CORAL_FAN; public static final Item BUBBLE_CORAL_FAN; public static final Item FIRE_CORAL_FAN; public static final Item HORN_CORAL_FAN; public static final Item DEAD_TUBE_CORAL_FAN; public static final Item DEAD_BRAIN_CORAL_FAN; public static final Item DEAD_BUBBLE_CORAL_FAN; public static final Item DEAD_FIRE_CORAL_FAN; public static final Item DEAD_HORN_CORAL_FAN; static {
/*  735 */     TUBE_CORAL_FAN = registerBlock(Blocks.TUBE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.TUBE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  736 */     BRAIN_CORAL_FAN = registerBlock(Blocks.BRAIN_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.BRAIN_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  737 */     BUBBLE_CORAL_FAN = registerBlock(Blocks.BUBBLE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.BUBBLE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  738 */     FIRE_CORAL_FAN = registerBlock(Blocks.FIRE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.FIRE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  739 */     HORN_CORAL_FAN = registerBlock(Blocks.HORN_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.HORN_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  740 */     DEAD_TUBE_CORAL_FAN = registerBlock(Blocks.DEAD_TUBE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DEAD_TUBE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  741 */     DEAD_BRAIN_CORAL_FAN = registerBlock(Blocks.DEAD_BRAIN_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DEAD_BRAIN_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  742 */     DEAD_BUBBLE_CORAL_FAN = registerBlock(Blocks.DEAD_BUBBLE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  743 */     DEAD_FIRE_CORAL_FAN = registerBlock(Blocks.DEAD_FIRE_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DEAD_FIRE_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  744 */     DEAD_HORN_CORAL_FAN = registerBlock(Blocks.DEAD_HORN_CORAL_FAN, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DEAD_HORN_CORAL_WALL_FAN, Direction.DOWN, paramProperties));
/*  745 */   } public static final Item BLUE_ICE = registerBlock(Blocks.BLUE_ICE);
/*  746 */   public static final Item CONDUIT = registerBlock(Blocks.CONDUIT, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/*      */   
/*  748 */   public static final Item POLISHED_GRANITE_STAIRS = registerBlock(Blocks.POLISHED_GRANITE_STAIRS);
/*  749 */   public static final Item SMOOTH_RED_SANDSTONE_STAIRS = registerBlock(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
/*  750 */   public static final Item MOSSY_STONE_BRICK_STAIRS = registerBlock(Blocks.MOSSY_STONE_BRICK_STAIRS);
/*  751 */   public static final Item POLISHED_DIORITE_STAIRS = registerBlock(Blocks.POLISHED_DIORITE_STAIRS);
/*  752 */   public static final Item MOSSY_COBBLESTONE_STAIRS = registerBlock(Blocks.MOSSY_COBBLESTONE_STAIRS);
/*  753 */   public static final Item END_STONE_BRICK_STAIRS = registerBlock(Blocks.END_STONE_BRICK_STAIRS);
/*  754 */   public static final Item STONE_STAIRS = registerBlock(Blocks.STONE_STAIRS);
/*  755 */   public static final Item SMOOTH_SANDSTONE_STAIRS = registerBlock(Blocks.SMOOTH_SANDSTONE_STAIRS);
/*  756 */   public static final Item SMOOTH_QUARTZ_STAIRS = registerBlock(Blocks.SMOOTH_QUARTZ_STAIRS);
/*  757 */   public static final Item GRANITE_STAIRS = registerBlock(Blocks.GRANITE_STAIRS);
/*  758 */   public static final Item ANDESITE_STAIRS = registerBlock(Blocks.ANDESITE_STAIRS);
/*  759 */   public static final Item RED_NETHER_BRICK_STAIRS = registerBlock(Blocks.RED_NETHER_BRICK_STAIRS);
/*  760 */   public static final Item POLISHED_ANDESITE_STAIRS = registerBlock(Blocks.POLISHED_ANDESITE_STAIRS);
/*  761 */   public static final Item DIORITE_STAIRS = registerBlock(Blocks.DIORITE_STAIRS);
/*  762 */   public static final Item COBBLED_DEEPSLATE_STAIRS = registerBlock(Blocks.COBBLED_DEEPSLATE_STAIRS);
/*  763 */   public static final Item POLISHED_DEEPSLATE_STAIRS = registerBlock(Blocks.POLISHED_DEEPSLATE_STAIRS);
/*  764 */   public static final Item DEEPSLATE_BRICK_STAIRS = registerBlock(Blocks.DEEPSLATE_BRICK_STAIRS);
/*  765 */   public static final Item DEEPSLATE_TILE_STAIRS = registerBlock(Blocks.DEEPSLATE_TILE_STAIRS);
/*      */   
/*  767 */   public static final Item POLISHED_GRANITE_SLAB = registerBlock(Blocks.POLISHED_GRANITE_SLAB);
/*  768 */   public static final Item SMOOTH_RED_SANDSTONE_SLAB = registerBlock(Blocks.SMOOTH_RED_SANDSTONE_SLAB);
/*  769 */   public static final Item MOSSY_STONE_BRICK_SLAB = registerBlock(Blocks.MOSSY_STONE_BRICK_SLAB);
/*  770 */   public static final Item POLISHED_DIORITE_SLAB = registerBlock(Blocks.POLISHED_DIORITE_SLAB);
/*  771 */   public static final Item MOSSY_COBBLESTONE_SLAB = registerBlock(Blocks.MOSSY_COBBLESTONE_SLAB);
/*  772 */   public static final Item END_STONE_BRICK_SLAB = registerBlock(Blocks.END_STONE_BRICK_SLAB);
/*  773 */   public static final Item SMOOTH_SANDSTONE_SLAB = registerBlock(Blocks.SMOOTH_SANDSTONE_SLAB);
/*  774 */   public static final Item SMOOTH_QUARTZ_SLAB = registerBlock(Blocks.SMOOTH_QUARTZ_SLAB);
/*  775 */   public static final Item GRANITE_SLAB = registerBlock(Blocks.GRANITE_SLAB);
/*  776 */   public static final Item ANDESITE_SLAB = registerBlock(Blocks.ANDESITE_SLAB);
/*  777 */   public static final Item RED_NETHER_BRICK_SLAB = registerBlock(Blocks.RED_NETHER_BRICK_SLAB);
/*  778 */   public static final Item POLISHED_ANDESITE_SLAB = registerBlock(Blocks.POLISHED_ANDESITE_SLAB);
/*  779 */   public static final Item DIORITE_SLAB = registerBlock(Blocks.DIORITE_SLAB);
/*  780 */   public static final Item COBBLED_DEEPSLATE_SLAB = registerBlock(Blocks.COBBLED_DEEPSLATE_SLAB);
/*  781 */   public static final Item POLISHED_DEEPSLATE_SLAB = registerBlock(Blocks.POLISHED_DEEPSLATE_SLAB);
/*  782 */   public static final Item DEEPSLATE_BRICK_SLAB = registerBlock(Blocks.DEEPSLATE_BRICK_SLAB);
/*  783 */   public static final Item DEEPSLATE_TILE_SLAB = registerBlock(Blocks.DEEPSLATE_TILE_SLAB);
/*      */   
/*  785 */   public static final Item SCAFFOLDING = registerBlock(Blocks.SCAFFOLDING, ScaffoldingBlockItem::new);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  791 */   public static final Item REDSTONE = registerItem("redstone", createBlockItemWithCustomItemName(Blocks.REDSTONE_WIRE), (new Item.Properties()).trimMaterial(TrimMaterials.REDSTONE)); public static final Item REDSTONE_TORCH; static {
/*  792 */     REDSTONE_TORCH = registerBlock(Blocks.REDSTONE_TORCH, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.REDSTONE_WALL_TORCH, Direction.DOWN, paramProperties));
/*  793 */   } public static final Item REDSTONE_BLOCK = registerBlock(Blocks.REDSTONE_BLOCK);
/*  794 */   public static final Item REPEATER = registerBlock(Blocks.REPEATER);
/*  795 */   public static final Item COMPARATOR = registerBlock(Blocks.COMPARATOR);
/*  796 */   public static final Item PISTON = registerBlock(Blocks.PISTON);
/*  797 */   public static final Item STICKY_PISTON = registerBlock(Blocks.STICKY_PISTON);
/*  798 */   public static final Item SLIME_BLOCK = registerBlock(Blocks.SLIME_BLOCK);
/*  799 */   public static final Item HONEY_BLOCK = registerBlock(Blocks.HONEY_BLOCK);
/*  800 */   public static final Item OBSERVER = registerBlock(Blocks.OBSERVER); public static final Item HOPPER; public static final Item DISPENSER; public static final Item DROPPER; static {
/*  801 */     HOPPER = registerBlock(Blocks.HOPPER, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  802 */     DISPENSER = registerBlock(Blocks.DISPENSER, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*  803 */     DROPPER = registerBlock(Blocks.DROPPER, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*      */   }
/*      */   
/*  806 */   public static final Item LECTERN = registerBlock(Blocks.LECTERN);
/*  807 */   public static final Item TARGET = registerBlock(Blocks.TARGET);
/*  808 */   public static final Item LEVER = registerBlock(Blocks.LEVER);
/*  809 */   public static final Item LIGHTNING_ROD = registerBlock(Blocks.LIGHTNING_ROD);
/*  810 */   public static final Item EXPOSED_LIGHTNING_ROD = registerBlock(Blocks.EXPOSED_LIGHTNING_ROD);
/*  811 */   public static final Item WEATHERED_LIGHTNING_ROD = registerBlock(Blocks.WEATHERED_LIGHTNING_ROD);
/*  812 */   public static final Item OXIDIZED_LIGHTNING_ROD = registerBlock(Blocks.OXIDIZED_LIGHTNING_ROD);
/*  813 */   public static final Item WAXED_LIGHTNING_ROD = registerBlock(Blocks.WAXED_LIGHTNING_ROD);
/*  814 */   public static final Item WAXED_EXPOSED_LIGHTNING_ROD = registerBlock(Blocks.WAXED_EXPOSED_LIGHTNING_ROD);
/*  815 */   public static final Item WAXED_WEATHERED_LIGHTNING_ROD = registerBlock(Blocks.WAXED_WEATHERED_LIGHTNING_ROD);
/*  816 */   public static final Item WAXED_OXIDIZED_LIGHTNING_ROD = registerBlock(Blocks.WAXED_OXIDIZED_LIGHTNING_ROD);
/*  817 */   public static final Item DAYLIGHT_DETECTOR = registerBlock(Blocks.DAYLIGHT_DETECTOR);
/*  818 */   public static final Item SCULK_SENSOR = registerBlock(Blocks.SCULK_SENSOR);
/*  819 */   public static final Item CALIBRATED_SCULK_SENSOR = registerBlock(Blocks.CALIBRATED_SCULK_SENSOR);
/*  820 */   public static final Item TRIPWIRE_HOOK = registerBlock(Blocks.TRIPWIRE_HOOK); public static final Item TRAPPED_CHEST; static {
/*  821 */     TRAPPED_CHEST = registerBlock(Blocks.TRAPPED_CHEST, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/*      */   }
/*      */   
/*  824 */   public static final Item TNT = registerBlock(Blocks.TNT);
/*  825 */   public static final Item REDSTONE_LAMP = registerBlock(Blocks.REDSTONE_LAMP);
/*  826 */   public static final Item NOTE_BLOCK = registerBlock(Blocks.NOTE_BLOCK);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  832 */   public static final Item STONE_BUTTON = registerBlock(Blocks.STONE_BUTTON);
/*  833 */   public static final Item POLISHED_BLACKSTONE_BUTTON = registerBlock(Blocks.POLISHED_BLACKSTONE_BUTTON);
/*  834 */   public static final Item OAK_BUTTON = registerBlock(Blocks.OAK_BUTTON);
/*  835 */   public static final Item SPRUCE_BUTTON = registerBlock(Blocks.SPRUCE_BUTTON);
/*  836 */   public static final Item BIRCH_BUTTON = registerBlock(Blocks.BIRCH_BUTTON);
/*  837 */   public static final Item JUNGLE_BUTTON = registerBlock(Blocks.JUNGLE_BUTTON);
/*  838 */   public static final Item ACACIA_BUTTON = registerBlock(Blocks.ACACIA_BUTTON);
/*  839 */   public static final Item CHERRY_BUTTON = registerBlock(Blocks.CHERRY_BUTTON);
/*  840 */   public static final Item DARK_OAK_BUTTON = registerBlock(Blocks.DARK_OAK_BUTTON);
/*  841 */   public static final Item PALE_OAK_BUTTON = registerBlock(Blocks.PALE_OAK_BUTTON);
/*  842 */   public static final Item MANGROVE_BUTTON = registerBlock(Blocks.MANGROVE_BUTTON);
/*  843 */   public static final Item BAMBOO_BUTTON = registerBlock(Blocks.BAMBOO_BUTTON);
/*  844 */   public static final Item CRIMSON_BUTTON = registerBlock(Blocks.CRIMSON_BUTTON);
/*  845 */   public static final Item WARPED_BUTTON = registerBlock(Blocks.WARPED_BUTTON);
/*      */ 
/*      */   
/*  848 */   public static final Item STONE_PRESSURE_PLATE = registerBlock(Blocks.STONE_PRESSURE_PLATE);
/*  849 */   public static final Item POLISHED_BLACKSTONE_PRESSURE_PLATE = registerBlock(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
/*  850 */   public static final Item LIGHT_WEIGHTED_PRESSURE_PLATE = registerBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*  851 */   public static final Item HEAVY_WEIGHTED_PRESSURE_PLATE = registerBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*  852 */   public static final Item OAK_PRESSURE_PLATE = registerBlock(Blocks.OAK_PRESSURE_PLATE);
/*  853 */   public static final Item SPRUCE_PRESSURE_PLATE = registerBlock(Blocks.SPRUCE_PRESSURE_PLATE);
/*  854 */   public static final Item BIRCH_PRESSURE_PLATE = registerBlock(Blocks.BIRCH_PRESSURE_PLATE);
/*  855 */   public static final Item JUNGLE_PRESSURE_PLATE = registerBlock(Blocks.JUNGLE_PRESSURE_PLATE);
/*  856 */   public static final Item ACACIA_PRESSURE_PLATE = registerBlock(Blocks.ACACIA_PRESSURE_PLATE);
/*  857 */   public static final Item CHERRY_PRESSURE_PLATE = registerBlock(Blocks.CHERRY_PRESSURE_PLATE);
/*  858 */   public static final Item DARK_OAK_PRESSURE_PLATE = registerBlock(Blocks.DARK_OAK_PRESSURE_PLATE);
/*  859 */   public static final Item PALE_OAK_PRESSURE_PLATE = registerBlock(Blocks.PALE_OAK_PRESSURE_PLATE);
/*  860 */   public static final Item MANGROVE_PRESSURE_PLATE = registerBlock(Blocks.MANGROVE_PRESSURE_PLATE);
/*  861 */   public static final Item BAMBOO_PRESSURE_PLATE = registerBlock(Blocks.BAMBOO_PRESSURE_PLATE);
/*  862 */   public static final Item CRIMSON_PRESSURE_PLATE = registerBlock(Blocks.CRIMSON_PRESSURE_PLATE);
/*  863 */   public static final Item WARPED_PRESSURE_PLATE = registerBlock(Blocks.WARPED_PRESSURE_PLATE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  869 */   public static final Item IRON_DOOR = registerBlock(Blocks.IRON_DOOR, DoubleHighBlockItem::new);
/*  870 */   public static final Item OAK_DOOR = registerBlock(Blocks.OAK_DOOR, DoubleHighBlockItem::new);
/*  871 */   public static final Item SPRUCE_DOOR = registerBlock(Blocks.SPRUCE_DOOR, DoubleHighBlockItem::new);
/*  872 */   public static final Item BIRCH_DOOR = registerBlock(Blocks.BIRCH_DOOR, DoubleHighBlockItem::new);
/*  873 */   public static final Item JUNGLE_DOOR = registerBlock(Blocks.JUNGLE_DOOR, DoubleHighBlockItem::new);
/*  874 */   public static final Item ACACIA_DOOR = registerBlock(Blocks.ACACIA_DOOR, DoubleHighBlockItem::new);
/*  875 */   public static final Item CHERRY_DOOR = registerBlock(Blocks.CHERRY_DOOR, DoubleHighBlockItem::new);
/*  876 */   public static final Item DARK_OAK_DOOR = registerBlock(Blocks.DARK_OAK_DOOR, DoubleHighBlockItem::new);
/*  877 */   public static final Item PALE_OAK_DOOR = registerBlock(Blocks.PALE_OAK_DOOR, DoubleHighBlockItem::new);
/*  878 */   public static final Item MANGROVE_DOOR = registerBlock(Blocks.MANGROVE_DOOR, DoubleHighBlockItem::new);
/*  879 */   public static final Item BAMBOO_DOOR = registerBlock(Blocks.BAMBOO_DOOR, DoubleHighBlockItem::new);
/*  880 */   public static final Item CRIMSON_DOOR = registerBlock(Blocks.CRIMSON_DOOR, DoubleHighBlockItem::new);
/*  881 */   public static final Item WARPED_DOOR = registerBlock(Blocks.WARPED_DOOR, DoubleHighBlockItem::new);
/*  882 */   public static final Item COPPER_DOOR = registerBlock(Blocks.COPPER_DOOR, DoubleHighBlockItem::new);
/*  883 */   public static final Item EXPOSED_COPPER_DOOR = registerBlock(Blocks.EXPOSED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  884 */   public static final Item WEATHERED_COPPER_DOOR = registerBlock(Blocks.WEATHERED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  885 */   public static final Item OXIDIZED_COPPER_DOOR = registerBlock(Blocks.OXIDIZED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  886 */   public static final Item WAXED_COPPER_DOOR = registerBlock(Blocks.WAXED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  887 */   public static final Item WAXED_EXPOSED_COPPER_DOOR = registerBlock(Blocks.WAXED_EXPOSED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  888 */   public static final Item WAXED_WEATHERED_COPPER_DOOR = registerBlock(Blocks.WAXED_WEATHERED_COPPER_DOOR, DoubleHighBlockItem::new);
/*  889 */   public static final Item WAXED_OXIDIZED_COPPER_DOOR = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_DOOR, DoubleHighBlockItem::new);
/*      */ 
/*      */   
/*  892 */   public static final Item IRON_TRAPDOOR = registerBlock(Blocks.IRON_TRAPDOOR);
/*  893 */   public static final Item OAK_TRAPDOOR = registerBlock(Blocks.OAK_TRAPDOOR);
/*  894 */   public static final Item SPRUCE_TRAPDOOR = registerBlock(Blocks.SPRUCE_TRAPDOOR);
/*  895 */   public static final Item BIRCH_TRAPDOOR = registerBlock(Blocks.BIRCH_TRAPDOOR);
/*  896 */   public static final Item JUNGLE_TRAPDOOR = registerBlock(Blocks.JUNGLE_TRAPDOOR);
/*  897 */   public static final Item ACACIA_TRAPDOOR = registerBlock(Blocks.ACACIA_TRAPDOOR);
/*  898 */   public static final Item CHERRY_TRAPDOOR = registerBlock(Blocks.CHERRY_TRAPDOOR);
/*  899 */   public static final Item DARK_OAK_TRAPDOOR = registerBlock(Blocks.DARK_OAK_TRAPDOOR);
/*  900 */   public static final Item PALE_OAK_TRAPDOOR = registerBlock(Blocks.PALE_OAK_TRAPDOOR);
/*  901 */   public static final Item MANGROVE_TRAPDOOR = registerBlock(Blocks.MANGROVE_TRAPDOOR);
/*  902 */   public static final Item BAMBOO_TRAPDOOR = registerBlock(Blocks.BAMBOO_TRAPDOOR);
/*  903 */   public static final Item CRIMSON_TRAPDOOR = registerBlock(Blocks.CRIMSON_TRAPDOOR);
/*  904 */   public static final Item WARPED_TRAPDOOR = registerBlock(Blocks.WARPED_TRAPDOOR);
/*  905 */   public static final Item COPPER_TRAPDOOR = registerBlock(Blocks.COPPER_TRAPDOOR);
/*  906 */   public static final Item EXPOSED_COPPER_TRAPDOOR = registerBlock(Blocks.EXPOSED_COPPER_TRAPDOOR);
/*  907 */   public static final Item WEATHERED_COPPER_TRAPDOOR = registerBlock(Blocks.WEATHERED_COPPER_TRAPDOOR);
/*  908 */   public static final Item OXIDIZED_COPPER_TRAPDOOR = registerBlock(Blocks.OXIDIZED_COPPER_TRAPDOOR);
/*  909 */   public static final Item WAXED_COPPER_TRAPDOOR = registerBlock(Blocks.WAXED_COPPER_TRAPDOOR);
/*  910 */   public static final Item WAXED_EXPOSED_COPPER_TRAPDOOR = registerBlock(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
/*  911 */   public static final Item WAXED_WEATHERED_COPPER_TRAPDOOR = registerBlock(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
/*  912 */   public static final Item WAXED_OXIDIZED_COPPER_TRAPDOOR = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
/*      */ 
/*      */   
/*  915 */   public static final Item OAK_FENCE_GATE = registerBlock(Blocks.OAK_FENCE_GATE);
/*  916 */   public static final Item SPRUCE_FENCE_GATE = registerBlock(Blocks.SPRUCE_FENCE_GATE);
/*  917 */   public static final Item BIRCH_FENCE_GATE = registerBlock(Blocks.BIRCH_FENCE_GATE);
/*  918 */   public static final Item JUNGLE_FENCE_GATE = registerBlock(Blocks.JUNGLE_FENCE_GATE);
/*  919 */   public static final Item ACACIA_FENCE_GATE = registerBlock(Blocks.ACACIA_FENCE_GATE);
/*  920 */   public static final Item CHERRY_FENCE_GATE = registerBlock(Blocks.CHERRY_FENCE_GATE);
/*  921 */   public static final Item DARK_OAK_FENCE_GATE = registerBlock(Blocks.DARK_OAK_FENCE_GATE);
/*  922 */   public static final Item PALE_OAK_FENCE_GATE = registerBlock(Blocks.PALE_OAK_FENCE_GATE);
/*  923 */   public static final Item MANGROVE_FENCE_GATE = registerBlock(Blocks.MANGROVE_FENCE_GATE);
/*  924 */   public static final Item BAMBOO_FENCE_GATE = registerBlock(Blocks.BAMBOO_FENCE_GATE);
/*  925 */   public static final Item CRIMSON_FENCE_GATE = registerBlock(Blocks.CRIMSON_FENCE_GATE);
/*  926 */   public static final Item WARPED_FENCE_GATE = registerBlock(Blocks.WARPED_FENCE_GATE);
/*      */   
/*  928 */   public static final Item POWERED_RAIL = registerBlock(Blocks.POWERED_RAIL);
/*  929 */   public static final Item DETECTOR_RAIL = registerBlock(Blocks.DETECTOR_RAIL);
/*  930 */   public static final Item RAIL = registerBlock(Blocks.RAIL);
/*  931 */   public static final Item ACTIVATOR_RAIL = registerBlock(Blocks.ACTIVATOR_RAIL);
/*  932 */   public static final Item SADDLE = registerItem("saddle", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.saddle()));
/*      */   
/*  934 */   public static final Item WHITE_HARNESS = registerItem("white_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.WHITE)));
/*  935 */   public static final Item ORANGE_HARNESS = registerItem("orange_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.ORANGE)));
/*  936 */   public static final Item MAGENTA_HARNESS = registerItem("magenta_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.MAGENTA)));
/*  937 */   public static final Item LIGHT_BLUE_HARNESS = registerItem("light_blue_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.LIGHT_BLUE)));
/*  938 */   public static final Item YELLOW_HARNESS = registerItem("yellow_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.YELLOW)));
/*  939 */   public static final Item LIME_HARNESS = registerItem("lime_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.LIME)));
/*  940 */   public static final Item PINK_HARNESS = registerItem("pink_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.PINK)));
/*  941 */   public static final Item GRAY_HARNESS = registerItem("gray_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.GRAY)));
/*  942 */   public static final Item LIGHT_GRAY_HARNESS = registerItem("light_gray_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.LIGHT_GRAY)));
/*  943 */   public static final Item CYAN_HARNESS = registerItem("cyan_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.CYAN)));
/*  944 */   public static final Item PURPLE_HARNESS = registerItem("purple_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.PURPLE)));
/*  945 */   public static final Item BLUE_HARNESS = registerItem("blue_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.BLUE)));
/*  946 */   public static final Item BROWN_HARNESS = registerItem("brown_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.BROWN)));
/*  947 */   public static final Item GREEN_HARNESS = registerItem("green_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.GREEN)));
/*  948 */   public static final Item RED_HARNESS = registerItem("red_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.RED)));
/*  949 */   public static final Item BLACK_HARNESS = registerItem("black_harness", (new Item.Properties()).stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.harness(DyeColor.BLACK))); public static final Item MINECART; public static final Item CHEST_MINECART; public static final Item FURNACE_MINECART; public static final Item TNT_MINECART; public static final Item HOPPER_MINECART; public static final Item CARROT_ON_A_STICK; public static final Item WARPED_FUNGUS_ON_A_STICK;
/*      */   static {
/*  951 */     MINECART = registerItem("minecart", paramProperties -> new MinecartItem(EntityType.MINECART, paramProperties), (new Item.Properties()).stacksTo(1));
/*  952 */     CHEST_MINECART = registerItem("chest_minecart", paramProperties -> new MinecartItem(EntityType.CHEST_MINECART, paramProperties), (new Item.Properties()).stacksTo(1));
/*  953 */     FURNACE_MINECART = registerItem("furnace_minecart", paramProperties -> new MinecartItem(EntityType.FURNACE_MINECART, paramProperties), (new Item.Properties()).stacksTo(1));
/*  954 */     TNT_MINECART = registerItem("tnt_minecart", paramProperties -> new MinecartItem(EntityType.TNT_MINECART, paramProperties), (new Item.Properties()).stacksTo(1));
/*  955 */     HOPPER_MINECART = registerItem("hopper_minecart", paramProperties -> new MinecartItem(EntityType.HOPPER_MINECART, paramProperties), (new Item.Properties()).stacksTo(1));
/*  956 */     CARROT_ON_A_STICK = registerItem("carrot_on_a_stick", paramProperties -> new FoodOnAStickItem<>(EntityType.PIG, 7, paramProperties), (new Item.Properties()).durability(25));
/*  957 */     WARPED_FUNGUS_ON_A_STICK = registerItem("warped_fungus_on_a_stick", paramProperties -> new FoodOnAStickItem<>(EntityType.STRIDER, 1, paramProperties), (new Item.Properties()).durability(100));
/*  958 */   } public static final Item PHANTOM_MEMBRANE = registerItem("phantom_membrane");
/*  959 */   public static final Item ELYTRA = registerItem("elytra", (new Item.Properties()).durability(432).rarity(Rarity.EPIC).<Unit>component(DataComponents.GLIDER, Unit.INSTANCE).<Equippable>component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST).setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setAsset(EquipmentAssets.ELYTRA).setDamageOnHurt(false).build()).repairable(PHANTOM_MEMBRANE)); public static final Item OAK_BOAT; public static final Item OAK_CHEST_BOAT; public static final Item SPRUCE_BOAT; public static final Item SPRUCE_CHEST_BOAT; public static final Item BIRCH_BOAT; public static final Item BIRCH_CHEST_BOAT; public static final Item JUNGLE_BOAT; public static final Item JUNGLE_CHEST_BOAT; public static final Item ACACIA_BOAT; public static final Item ACACIA_CHEST_BOAT; public static final Item CHERRY_BOAT; public static final Item CHERRY_CHEST_BOAT; public static final Item DARK_OAK_BOAT; public static final Item DARK_OAK_CHEST_BOAT; public static final Item PALE_OAK_BOAT; public static final Item PALE_OAK_CHEST_BOAT; public static final Item MANGROVE_BOAT; public static final Item MANGROVE_CHEST_BOAT; public static final Item BAMBOO_RAFT; public static final Item BAMBOO_CHEST_RAFT; static {
/*  960 */     OAK_BOAT = registerItem("oak_boat", paramProperties -> new BoatItem(EntityType.OAK_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  961 */     OAK_CHEST_BOAT = registerItem("oak_chest_boat", paramProperties -> new BoatItem(EntityType.OAK_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  962 */     SPRUCE_BOAT = registerItem("spruce_boat", paramProperties -> new BoatItem(EntityType.SPRUCE_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  963 */     SPRUCE_CHEST_BOAT = registerItem("spruce_chest_boat", paramProperties -> new BoatItem(EntityType.SPRUCE_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  964 */     BIRCH_BOAT = registerItem("birch_boat", paramProperties -> new BoatItem(EntityType.BIRCH_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  965 */     BIRCH_CHEST_BOAT = registerItem("birch_chest_boat", paramProperties -> new BoatItem(EntityType.BIRCH_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  966 */     JUNGLE_BOAT = registerItem("jungle_boat", paramProperties -> new BoatItem(EntityType.JUNGLE_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  967 */     JUNGLE_CHEST_BOAT = registerItem("jungle_chest_boat", paramProperties -> new BoatItem(EntityType.JUNGLE_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  968 */     ACACIA_BOAT = registerItem("acacia_boat", paramProperties -> new BoatItem(EntityType.ACACIA_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  969 */     ACACIA_CHEST_BOAT = registerItem("acacia_chest_boat", paramProperties -> new BoatItem(EntityType.ACACIA_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  970 */     CHERRY_BOAT = registerItem("cherry_boat", paramProperties -> new BoatItem(EntityType.CHERRY_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  971 */     CHERRY_CHEST_BOAT = registerItem("cherry_chest_boat", paramProperties -> new BoatItem(EntityType.CHERRY_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  972 */     DARK_OAK_BOAT = registerItem("dark_oak_boat", paramProperties -> new BoatItem(EntityType.DARK_OAK_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  973 */     DARK_OAK_CHEST_BOAT = registerItem("dark_oak_chest_boat", paramProperties -> new BoatItem(EntityType.DARK_OAK_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  974 */     PALE_OAK_BOAT = registerItem("pale_oak_boat", paramProperties -> new BoatItem(EntityType.PALE_OAK_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  975 */     PALE_OAK_CHEST_BOAT = registerItem("pale_oak_chest_boat", paramProperties -> new BoatItem(EntityType.PALE_OAK_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  976 */     MANGROVE_BOAT = registerItem("mangrove_boat", paramProperties -> new BoatItem(EntityType.MANGROVE_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  977 */     MANGROVE_CHEST_BOAT = registerItem("mangrove_chest_boat", paramProperties -> new BoatItem(EntityType.MANGROVE_CHEST_BOAT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  978 */     BAMBOO_RAFT = registerItem("bamboo_raft", paramProperties -> new BoatItem(EntityType.BAMBOO_RAFT, paramProperties), (new Item.Properties()).stacksTo(1));
/*  979 */     BAMBOO_CHEST_RAFT = registerItem("bamboo_chest_raft", paramProperties -> new BoatItem(EntityType.BAMBOO_CHEST_RAFT, paramProperties), (new Item.Properties()).stacksTo(1));
/*      */   }
/*  981 */   public static final Item STRUCTURE_BLOCK = registerBlock(Blocks.STRUCTURE_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  982 */   public static final Item JIGSAW = registerBlock(Blocks.JIGSAW, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  983 */   public static final Item TEST_BLOCK = registerBlock(Blocks.TEST_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)TestBlock.MODE, (Comparable)TestBlockMode.START)));
/*  984 */   public static final Item TEST_INSTANCE_BLOCK = registerBlock(Blocks.TEST_INSTANCE_BLOCK, GameMasterBlockItem::new, (new Item.Properties()).rarity(Rarity.EPIC));
/*  985 */   public static final Item TURTLE_HELMET = registerItem("turtle_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.TURTLE_SCUTE, ArmorType.HELMET));
/*  986 */   public static final Item TURTLE_SCUTE = registerItem("turtle_scute");
/*  987 */   public static final Item ARMADILLO_SCUTE = registerItem("armadillo_scute");
/*  988 */   public static final Item WOLF_ARMOR = registerItem("wolf_armor", (new Item.Properties()).wolfArmor(ArmorMaterials.ARMADILLO_SCUTE));
/*  989 */   public static final Item FLINT_AND_STEEL = registerItem("flint_and_steel", FlintAndSteelItem::new, (new Item.Properties()).durability(64));
/*  990 */   public static final Item BOWL = registerItem("bowl");
/*  991 */   public static final Item APPLE = registerItem("apple", (new Item.Properties()).food(Foods.APPLE));
/*  992 */   public static final Item BOW = registerItem("bow", BowItem::new, (new Item.Properties()).durability(384).enchantable(1));
/*  993 */   public static final Item ARROW = registerItem("arrow", ArrowItem::new);
/*  994 */   public static final Item COAL = registerItem("coal");
/*  995 */   public static final Item CHARCOAL = registerItem("charcoal");
/*  996 */   public static final Item DIAMOND = registerItem("diamond", (new Item.Properties()).trimMaterial(TrimMaterials.DIAMOND));
/*  997 */   public static final Item EMERALD = registerItem("emerald", (new Item.Properties()).trimMaterial(TrimMaterials.EMERALD));
/*  998 */   public static final Item LAPIS_LAZULI = registerItem("lapis_lazuli", (new Item.Properties()).trimMaterial(TrimMaterials.LAPIS));
/*  999 */   public static final Item QUARTZ = registerItem("quartz", (new Item.Properties()).trimMaterial(TrimMaterials.QUARTZ));
/* 1000 */   public static final Item AMETHYST_SHARD = registerItem("amethyst_shard", (new Item.Properties()).trimMaterial(TrimMaterials.AMETHYST));
/* 1001 */   public static final Item RAW_IRON = registerItem("raw_iron");
/* 1002 */   public static final Item IRON_INGOT = registerItem("iron_ingot", (new Item.Properties()).trimMaterial(TrimMaterials.IRON));
/* 1003 */   public static final Item RAW_COPPER = registerItem("raw_copper");
/* 1004 */   public static final Item COPPER_INGOT = registerItem("copper_ingot", (new Item.Properties()).trimMaterial(TrimMaterials.COPPER));
/* 1005 */   public static final Item RAW_GOLD = registerItem("raw_gold");
/* 1006 */   public static final Item GOLD_INGOT = registerItem("gold_ingot", (new Item.Properties()).trimMaterial(TrimMaterials.GOLD));
/* 1007 */   public static final Item NETHERITE_INGOT = registerItem("netherite_ingot", (new Item.Properties()).fireResistant().trimMaterial(TrimMaterials.NETHERITE));
/* 1008 */   public static final Item NETHERITE_SCRAP = registerItem("netherite_scrap", (new Item.Properties()).fireResistant());
/* 1009 */   public static final Item WOODEN_SWORD = registerItem("wooden_sword", (new Item.Properties()).sword(ToolMaterial.WOOD, 3.0F, -2.4F)); public static final Item WOODEN_SHOVEL; static {
/* 1010 */     WOODEN_SHOVEL = registerItem("wooden_shovel", paramProperties -> new ShovelItem(ToolMaterial.WOOD, 1.5F, -3.0F, paramProperties));
/* 1011 */   } public static final Item WOODEN_PICKAXE = registerItem("wooden_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.WOOD, 1.0F, -2.8F)); public static final Item WOODEN_AXE; public static final Item WOODEN_HOE; static {
/* 1012 */     WOODEN_AXE = registerItem("wooden_axe", paramProperties -> new AxeItem(ToolMaterial.WOOD, 6.0F, -3.2F, paramProperties));
/* 1013 */     WOODEN_HOE = registerItem("wooden_hoe", paramProperties -> new HoeItem(ToolMaterial.WOOD, 0.0F, -3.0F, paramProperties));
/* 1014 */   } public static final Item COPPER_SWORD = registerItem("copper_sword", (new Item.Properties()).sword(ToolMaterial.COPPER, 3.0F, -2.4F)); public static final Item COPPER_SHOVEL; static {
/* 1015 */     COPPER_SHOVEL = registerItem("copper_shovel", paramProperties -> new ShovelItem(ToolMaterial.COPPER, 1.5F, -3.0F, paramProperties));
/* 1016 */   } public static final Item COPPER_PICKAXE = registerItem("copper_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.COPPER, 1.0F, -2.8F)); public static final Item COPPER_AXE; public static final Item COPPER_HOE; static {
/* 1017 */     COPPER_AXE = registerItem("copper_axe", paramProperties -> new AxeItem(ToolMaterial.COPPER, 7.0F, -3.2F, paramProperties));
/* 1018 */     COPPER_HOE = registerItem("copper_hoe", paramProperties -> new HoeItem(ToolMaterial.COPPER, -1.0F, -2.0F, paramProperties));
/* 1019 */   } public static final Item STONE_SWORD = registerItem("stone_sword", (new Item.Properties()).sword(ToolMaterial.STONE, 3.0F, -2.4F)); public static final Item STONE_SHOVEL; static {
/* 1020 */     STONE_SHOVEL = registerItem("stone_shovel", paramProperties -> new ShovelItem(ToolMaterial.STONE, 1.5F, -3.0F, paramProperties));
/* 1021 */   } public static final Item STONE_PICKAXE = registerItem("stone_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.STONE, 1.0F, -2.8F)); public static final Item STONE_AXE; public static final Item STONE_HOE; static {
/* 1022 */     STONE_AXE = registerItem("stone_axe", paramProperties -> new AxeItem(ToolMaterial.STONE, 7.0F, -3.2F, paramProperties));
/* 1023 */     STONE_HOE = registerItem("stone_hoe", paramProperties -> new HoeItem(ToolMaterial.STONE, -1.0F, -2.0F, paramProperties));
/* 1024 */   } public static final Item GOLDEN_SWORD = registerItem("golden_sword", (new Item.Properties()).sword(ToolMaterial.GOLD, 3.0F, -2.4F)); public static final Item GOLDEN_SHOVEL; static {
/* 1025 */     GOLDEN_SHOVEL = registerItem("golden_shovel", paramProperties -> new ShovelItem(ToolMaterial.GOLD, 1.5F, -3.0F, paramProperties));
/* 1026 */   } public static final Item GOLDEN_PICKAXE = registerItem("golden_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.GOLD, 1.0F, -2.8F)); public static final Item GOLDEN_AXE; public static final Item GOLDEN_HOE; static {
/* 1027 */     GOLDEN_AXE = registerItem("golden_axe", paramProperties -> new AxeItem(ToolMaterial.GOLD, 6.0F, -3.0F, paramProperties));
/* 1028 */     GOLDEN_HOE = registerItem("golden_hoe", paramProperties -> new HoeItem(ToolMaterial.GOLD, 0.0F, -3.0F, paramProperties));
/* 1029 */   } public static final Item IRON_SWORD = registerItem("iron_sword", (new Item.Properties()).sword(ToolMaterial.IRON, 3.0F, -2.4F)); public static final Item IRON_SHOVEL; static {
/* 1030 */     IRON_SHOVEL = registerItem("iron_shovel", paramProperties -> new ShovelItem(ToolMaterial.IRON, 1.5F, -3.0F, paramProperties));
/* 1031 */   } public static final Item IRON_PICKAXE = registerItem("iron_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.IRON, 1.0F, -2.8F)); public static final Item IRON_AXE; public static final Item IRON_HOE; static {
/* 1032 */     IRON_AXE = registerItem("iron_axe", paramProperties -> new AxeItem(ToolMaterial.IRON, 6.0F, -3.1F, paramProperties));
/* 1033 */     IRON_HOE = registerItem("iron_hoe", paramProperties -> new HoeItem(ToolMaterial.IRON, -2.0F, -1.0F, paramProperties));
/* 1034 */   } public static final Item DIAMOND_SWORD = registerItem("diamond_sword", (new Item.Properties()).sword(ToolMaterial.DIAMOND, 3.0F, -2.4F)); public static final Item DIAMOND_SHOVEL; static {
/* 1035 */     DIAMOND_SHOVEL = registerItem("diamond_shovel", paramProperties -> new ShovelItem(ToolMaterial.DIAMOND, 1.5F, -3.0F, paramProperties));
/* 1036 */   } public static final Item DIAMOND_PICKAXE = registerItem("diamond_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.DIAMOND, 1.0F, -2.8F)); public static final Item DIAMOND_AXE; public static final Item DIAMOND_HOE; static {
/* 1037 */     DIAMOND_AXE = registerItem("diamond_axe", paramProperties -> new AxeItem(ToolMaterial.DIAMOND, 5.0F, -3.0F, paramProperties));
/* 1038 */     DIAMOND_HOE = registerItem("diamond_hoe", paramProperties -> new HoeItem(ToolMaterial.DIAMOND, -3.0F, 0.0F, paramProperties));
/* 1039 */   } public static final Item NETHERITE_SWORD = registerItem("netherite_sword", (new Item.Properties()).sword(ToolMaterial.NETHERITE, 3.0F, -2.4F).fireResistant()); public static final Item NETHERITE_SHOVEL; static {
/* 1040 */     NETHERITE_SHOVEL = registerItem("netherite_shovel", paramProperties -> new ShovelItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, paramProperties), (new Item.Properties()).fireResistant());
/* 1041 */   } public static final Item NETHERITE_PICKAXE = registerItem("netherite_pickaxe", (new Item.Properties()).pickaxe(ToolMaterial.NETHERITE, 1.0F, -2.8F).fireResistant()); public static final Item NETHERITE_AXE; public static final Item NETHERITE_HOE; static {
/* 1042 */     NETHERITE_AXE = registerItem("netherite_axe", paramProperties -> new AxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, paramProperties), (new Item.Properties()).fireResistant());
/* 1043 */     NETHERITE_HOE = registerItem("netherite_hoe", paramProperties -> new HoeItem(ToolMaterial.NETHERITE, -4.0F, 0.0F, paramProperties), (new Item.Properties()).fireResistant());
/* 1044 */   } public static final Item STICK = registerItem("stick");
/* 1045 */   public static final Item MUSHROOM_STEW = registerItem("mushroom_stew", (new Item.Properties()).stacksTo(1).food(Foods.MUSHROOM_STEW).usingConvertsTo(BOWL));
/* 1046 */   public static final Item STRING = registerItem("string", createBlockItemWithCustomItemName(Blocks.TRIPWIRE));
/* 1047 */   public static final Item FEATHER = registerItem("feather");
/* 1048 */   public static final Item GUNPOWDER = registerItem("gunpowder");
/* 1049 */   public static final Item WHEAT_SEEDS = registerItem("wheat_seeds", createBlockItemWithCustomItemName(Blocks.WHEAT));
/* 1050 */   public static final Item WHEAT = registerItem("wheat");
/* 1051 */   public static final Item BREAD = registerItem("bread", (new Item.Properties()).food(Foods.BREAD));
/* 1052 */   public static final Item LEATHER_HELMET = registerItem("leather_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
/* 1053 */   public static final Item LEATHER_CHESTPLATE = registerItem("leather_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.CHESTPLATE));
/* 1054 */   public static final Item LEATHER_LEGGINGS = registerItem("leather_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.LEGGINGS));
/* 1055 */   public static final Item LEATHER_BOOTS = registerItem("leather_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS));
/* 1056 */   public static final Item COPPER_HELMET = registerItem("copper_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.COPPER, ArmorType.HELMET));
/* 1057 */   public static final Item COPPER_CHESTPLATE = registerItem("copper_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.COPPER, ArmorType.CHESTPLATE));
/* 1058 */   public static final Item COPPER_LEGGINGS = registerItem("copper_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.COPPER, ArmorType.LEGGINGS));
/* 1059 */   public static final Item COPPER_BOOTS = registerItem("copper_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.COPPER, ArmorType.BOOTS));
/* 1060 */   public static final Item CHAINMAIL_HELMET = registerItem("chainmail_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET).rarity(Rarity.UNCOMMON));
/* 1061 */   public static final Item CHAINMAIL_CHESTPLATE = registerItem("chainmail_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON));
/* 1062 */   public static final Item CHAINMAIL_LEGGINGS = registerItem("chainmail_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS).rarity(Rarity.UNCOMMON));
/* 1063 */   public static final Item CHAINMAIL_BOOTS = registerItem("chainmail_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS).rarity(Rarity.UNCOMMON));
/* 1064 */   public static final Item IRON_HELMET = registerItem("iron_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
/* 1065 */   public static final Item IRON_CHESTPLATE = registerItem("iron_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.IRON, ArmorType.CHESTPLATE));
/* 1066 */   public static final Item IRON_LEGGINGS = registerItem("iron_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.IRON, ArmorType.LEGGINGS));
/* 1067 */   public static final Item IRON_BOOTS = registerItem("iron_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.IRON, ArmorType.BOOTS));
/* 1068 */   public static final Item DIAMOND_HELMET = registerItem("diamond_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
/* 1069 */   public static final Item DIAMOND_CHESTPLATE = registerItem("diamond_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE));
/* 1070 */   public static final Item DIAMOND_LEGGINGS = registerItem("diamond_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS));
/* 1071 */   public static final Item DIAMOND_BOOTS = registerItem("diamond_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.BOOTS));
/* 1072 */   public static final Item GOLDEN_HELMET = registerItem("golden_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET));
/* 1073 */   public static final Item GOLDEN_CHESTPLATE = registerItem("golden_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE));
/* 1074 */   public static final Item GOLDEN_LEGGINGS = registerItem("golden_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.GOLD, ArmorType.LEGGINGS));
/* 1075 */   public static final Item GOLDEN_BOOTS = registerItem("golden_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.GOLD, ArmorType.BOOTS));
/* 1076 */   public static final Item NETHERITE_HELMET = registerItem("netherite_helmet", (new Item.Properties()).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).fireResistant());
/* 1077 */   public static final Item NETHERITE_CHESTPLATE = registerItem("netherite_chestplate", (new Item.Properties()).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.CHESTPLATE).fireResistant());
/* 1078 */   public static final Item NETHERITE_LEGGINGS = registerItem("netherite_leggings", (new Item.Properties()).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.LEGGINGS).fireResistant());
/* 1079 */   public static final Item NETHERITE_BOOTS = registerItem("netherite_boots", (new Item.Properties()).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.BOOTS).fireResistant());
/* 1080 */   public static final Item FLINT = registerItem("flint");
/* 1081 */   public static final Item PORKCHOP = registerItem("porkchop", (new Item.Properties()).food(Foods.PORKCHOP));
/* 1082 */   public static final Item COOKED_PORKCHOP = registerItem("cooked_porkchop", (new Item.Properties()).food(Foods.COOKED_PORKCHOP)); public static final Item PAINTING; static {
/* 1083 */     PAINTING = registerItem("painting", paramProperties -> new HangingEntityItem(EntityType.PAINTING, paramProperties));
/* 1084 */   } public static final Item GOLDEN_APPLE = registerItem("golden_apple", (new Item.Properties()).food(Foods.GOLDEN_APPLE, Consumables.GOLDEN_APPLE)); public static final Item OAK_SIGN; public static final Item SPRUCE_SIGN; public static final Item BIRCH_SIGN; public static final Item JUNGLE_SIGN; public static final Item ACACIA_SIGN; public static final Item CHERRY_SIGN; public static final Item DARK_OAK_SIGN; public static final Item PALE_OAK_SIGN; public static final Item MANGROVE_SIGN; public static final Item BAMBOO_SIGN; public static final Item CRIMSON_SIGN; public static final Item WARPED_SIGN; public static final Item OAK_HANGING_SIGN; public static final Item SPRUCE_HANGING_SIGN;
/* 1085 */   public static final Item ENCHANTED_GOLDEN_APPLE = registerItem("enchanted_golden_apple", (new Item.Properties()).rarity(Rarity.RARE).food(Foods.ENCHANTED_GOLDEN_APPLE, Consumables.ENCHANTED_GOLDEN_APPLE).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true))); public static final Item BIRCH_HANGING_SIGN; public static final Item JUNGLE_HANGING_SIGN; public static final Item ACACIA_HANGING_SIGN; public static final Item CHERRY_HANGING_SIGN; public static final Item DARK_OAK_HANGING_SIGN; public static final Item PALE_OAK_HANGING_SIGN; public static final Item MANGROVE_HANGING_SIGN; public static final Item BAMBOO_HANGING_SIGN; public static final Item CRIMSON_HANGING_SIGN; public static final Item WARPED_HANGING_SIGN; public static final Item BUCKET; public static final Item WATER_BUCKET; public static final Item LAVA_BUCKET; public static final Item POWDER_SNOW_BUCKET; static {
/* 1086 */     OAK_SIGN = registerBlock(Blocks.OAK_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.OAK_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1087 */     SPRUCE_SIGN = registerBlock(Blocks.SPRUCE_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.SPRUCE_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1088 */     BIRCH_SIGN = registerBlock(Blocks.BIRCH_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.BIRCH_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1089 */     JUNGLE_SIGN = registerBlock(Blocks.JUNGLE_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.JUNGLE_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1090 */     ACACIA_SIGN = registerBlock(Blocks.ACACIA_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.ACACIA_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1091 */     CHERRY_SIGN = registerBlock(Blocks.CHERRY_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.CHERRY_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1092 */     DARK_OAK_SIGN = registerBlock(Blocks.DARK_OAK_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.DARK_OAK_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1093 */     PALE_OAK_SIGN = registerBlock(Blocks.PALE_OAK_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.PALE_OAK_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1094 */     MANGROVE_SIGN = registerBlock(Blocks.MANGROVE_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.MANGROVE_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1095 */     BAMBOO_SIGN = registerBlock(Blocks.BAMBOO_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.BAMBOO_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1096 */     CRIMSON_SIGN = registerBlock(Blocks.CRIMSON_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.CRIMSON_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1097 */     WARPED_SIGN = registerBlock(Blocks.WARPED_SIGN, (paramBlock, paramProperties) -> new SignItem(paramBlock, Blocks.WARPED_WALL_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1098 */     OAK_HANGING_SIGN = registerBlock(Blocks.OAK_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.OAK_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1099 */     SPRUCE_HANGING_SIGN = registerBlock(Blocks.SPRUCE_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.SPRUCE_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1100 */     BIRCH_HANGING_SIGN = registerBlock(Blocks.BIRCH_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.BIRCH_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1101 */     JUNGLE_HANGING_SIGN = registerBlock(Blocks.JUNGLE_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.JUNGLE_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1102 */     ACACIA_HANGING_SIGN = registerBlock(Blocks.ACACIA_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.ACACIA_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1103 */     CHERRY_HANGING_SIGN = registerBlock(Blocks.CHERRY_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.CHERRY_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1104 */     DARK_OAK_HANGING_SIGN = registerBlock(Blocks.DARK_OAK_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.DARK_OAK_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1105 */     PALE_OAK_HANGING_SIGN = registerBlock(Blocks.PALE_OAK_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.PALE_OAK_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1106 */     MANGROVE_HANGING_SIGN = registerBlock(Blocks.MANGROVE_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.MANGROVE_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1107 */     BAMBOO_HANGING_SIGN = registerBlock(Blocks.BAMBOO_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.BAMBOO_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1108 */     CRIMSON_HANGING_SIGN = registerBlock(Blocks.CRIMSON_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.CRIMSON_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1109 */     WARPED_HANGING_SIGN = registerBlock(Blocks.WARPED_HANGING_SIGN, (paramBlock, paramProperties) -> new HangingSignItem(paramBlock, Blocks.WARPED_WALL_HANGING_SIGN, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1110 */     BUCKET = registerItem("bucket", paramProperties -> new BucketItem(Fluids.EMPTY, paramProperties), (new Item.Properties()).stacksTo(16));
/* 1111 */     WATER_BUCKET = registerItem("water_bucket", paramProperties -> new BucketItem((Fluid)Fluids.WATER, paramProperties), (new Item.Properties()).craftRemainder(BUCKET).stacksTo(1));
/* 1112 */     LAVA_BUCKET = registerItem("lava_bucket", paramProperties -> new BucketItem((Fluid)Fluids.LAVA, paramProperties), (new Item.Properties()).craftRemainder(BUCKET).stacksTo(1));
/* 1113 */     POWDER_SNOW_BUCKET = registerItem("powder_snow_bucket", paramProperties -> new SolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, paramProperties), (new Item.Properties()).stacksTo(1).useItemDescriptionPrefix());
/* 1114 */   } public static final Item SNOWBALL = registerItem("snowball", SnowballItem::new, (new Item.Properties()).stacksTo(16));
/* 1115 */   public static final Item LEATHER = registerItem("leather");
/* 1116 */   public static final Item MILK_BUCKET = registerItem("milk_bucket", (new Item.Properties()).craftRemainder(BUCKET).<Consumable>component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).usingConvertsTo(BUCKET).stacksTo(1)); public static final Item PUFFERFISH_BUCKET; public static final Item SALMON_BUCKET; public static final Item COD_BUCKET; public static final Item TROPICAL_FISH_BUCKET; public static final Item AXOLOTL_BUCKET; public static final Item TADPOLE_BUCKET; static {
/* 1117 */     PUFFERFISH_BUCKET = registerItem("pufferfish_bucket", paramProperties -> new MobBucketItem(EntityType.PUFFERFISH, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, paramProperties), (new Item.Properties()).stacksTo(1).<CustomData>component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).component(DataComponents.FOOD, Foods.PUFFERFISH));
/* 1118 */     SALMON_BUCKET = registerItem("salmon_bucket", paramProperties -> new MobBucketItem(EntityType.SALMON, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, paramProperties), (new Item.Properties()).stacksTo(1).<CustomData>component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).component(DataComponents.FOOD, Foods.SALMON));
/* 1119 */     COD_BUCKET = registerItem("cod_bucket", paramProperties -> new MobBucketItem(EntityType.COD, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, paramProperties), (new Item.Properties()).stacksTo(1).<CustomData>component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).component(DataComponents.FOOD, Foods.COD));
/* 1120 */     TROPICAL_FISH_BUCKET = registerItem("tropical_fish_bucket", paramProperties -> new MobBucketItem(EntityType.TROPICAL_FISH, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, paramProperties), (new Item.Properties()).stacksTo(1).<CustomData>component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).component(DataComponents.FOOD, Foods.TROPICAL_FISH));
/* 1121 */     AXOLOTL_BUCKET = registerItem("axolotl_bucket", paramProperties -> new MobBucketItem(EntityType.AXOLOTL, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_AXOLOTL, paramProperties), (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
/* 1122 */     TADPOLE_BUCKET = registerItem("tadpole_bucket", paramProperties -> new MobBucketItem(EntityType.TADPOLE, (Fluid)Fluids.WATER, SoundEvents.BUCKET_EMPTY_TADPOLE, paramProperties), (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
/* 1123 */   } public static final Item BRICK = registerItem("brick");
/* 1124 */   public static final Item CLAY_BALL = registerItem("clay_ball");
/* 1125 */   public static final Item DRIED_KELP_BLOCK = registerBlock(Blocks.DRIED_KELP_BLOCK);
/* 1126 */   public static final Item PAPER = registerItem("paper");
/* 1127 */   public static final Item BOOK = registerItem("book", (new Item.Properties()).enchantable(1));
/* 1128 */   public static final Item SLIME_BALL = registerItem("slime_ball");
/* 1129 */   public static final Item EGG = registerItem("egg", EggItem::new, (new Item.Properties()).stacksTo(16).component(DataComponents.CHICKEN_VARIANT, new EitherHolder(ChickenVariants.TEMPERATE)));
/* 1130 */   public static final Item BLUE_EGG = registerItem("blue_egg", EggItem::new, (new Item.Properties()).stacksTo(16).component(DataComponents.CHICKEN_VARIANT, new EitherHolder(ChickenVariants.COLD)));
/* 1131 */   public static final Item BROWN_EGG = registerItem("brown_egg", EggItem::new, (new Item.Properties()).stacksTo(16).component(DataComponents.CHICKEN_VARIANT, new EitherHolder(ChickenVariants.WARM)));
/* 1132 */   public static final Item COMPASS = registerItem("compass", CompassItem::new);
/* 1133 */   public static final Item RECOVERY_COMPASS = registerItem("recovery_compass", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1134 */   public static final Item BUNDLE = registerItem("bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1135 */   public static final Item WHITE_BUNDLE = registerItem("white_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1136 */   public static final Item ORANGE_BUNDLE = registerItem("orange_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1137 */   public static final Item MAGENTA_BUNDLE = registerItem("magenta_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1138 */   public static final Item LIGHT_BLUE_BUNDLE = registerItem("light_blue_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1139 */   public static final Item YELLOW_BUNDLE = registerItem("yellow_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1140 */   public static final Item LIME_BUNDLE = registerItem("lime_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1141 */   public static final Item PINK_BUNDLE = registerItem("pink_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1142 */   public static final Item GRAY_BUNDLE = registerItem("gray_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1143 */   public static final Item LIGHT_GRAY_BUNDLE = registerItem("light_gray_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1144 */   public static final Item CYAN_BUNDLE = registerItem("cyan_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1145 */   public static final Item PURPLE_BUNDLE = registerItem("purple_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1146 */   public static final Item BLUE_BUNDLE = registerItem("blue_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1147 */   public static final Item BROWN_BUNDLE = registerItem("brown_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1148 */   public static final Item GREEN_BUNDLE = registerItem("green_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1149 */   public static final Item RED_BUNDLE = registerItem("red_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1150 */   public static final Item BLACK_BUNDLE = registerItem("black_bundle", BundleItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
/* 1151 */   public static final Item FISHING_ROD = registerItem("fishing_rod", FishingRodItem::new, (new Item.Properties()).durability(64).enchantable(1));
/* 1152 */   public static final Item CLOCK = registerItem("clock");
/* 1153 */   public static final Item SPYGLASS = registerItem("spyglass", SpyglassItem::new, (new Item.Properties()).stacksTo(1));
/* 1154 */   public static final Item GLOWSTONE_DUST = registerItem("glowstone_dust");
/* 1155 */   public static final Item COD = registerItem("cod", (new Item.Properties()).food(Foods.COD));
/* 1156 */   public static final Item SALMON = registerItem("salmon", (new Item.Properties()).food(Foods.SALMON));
/* 1157 */   public static final Item TROPICAL_FISH = registerItem("tropical_fish", (new Item.Properties()).food(Foods.TROPICAL_FISH));
/* 1158 */   public static final Item PUFFERFISH = registerItem("pufferfish", (new Item.Properties()).food(Foods.PUFFERFISH, Consumables.PUFFERFISH));
/* 1159 */   public static final Item COOKED_COD = registerItem("cooked_cod", (new Item.Properties()).food(Foods.COOKED_COD));
/* 1160 */   public static final Item COOKED_SALMON = registerItem("cooked_salmon", (new Item.Properties()).food(Foods.COOKED_SALMON));
/* 1161 */   public static final Item INK_SAC = registerItem("ink_sac", InkSacItem::new);
/* 1162 */   public static final Item GLOW_INK_SAC = registerItem("glow_ink_sac", GlowInkSacItem::new);
/* 1163 */   public static final Item COCOA_BEANS = registerItem("cocoa_beans", createBlockItemWithCustomItemName(Blocks.COCOA)); public static final Item WHITE_DYE; public static final Item ORANGE_DYE; public static final Item MAGENTA_DYE; public static final Item LIGHT_BLUE_DYE; public static final Item YELLOW_DYE; public static final Item LIME_DYE; public static final Item PINK_DYE; public static final Item GRAY_DYE; public static final Item LIGHT_GRAY_DYE; public static final Item CYAN_DYE; public static final Item PURPLE_DYE; public static final Item BLUE_DYE; public static final Item BROWN_DYE; public static final Item GREEN_DYE; public static final Item RED_DYE; public static final Item BLACK_DYE; static {
/* 1164 */     WHITE_DYE = registerItem("white_dye", paramProperties -> new DyeItem(DyeColor.WHITE, paramProperties));
/* 1165 */     ORANGE_DYE = registerItem("orange_dye", paramProperties -> new DyeItem(DyeColor.ORANGE, paramProperties));
/* 1166 */     MAGENTA_DYE = registerItem("magenta_dye", paramProperties -> new DyeItem(DyeColor.MAGENTA, paramProperties));
/* 1167 */     LIGHT_BLUE_DYE = registerItem("light_blue_dye", paramProperties -> new DyeItem(DyeColor.LIGHT_BLUE, paramProperties));
/* 1168 */     YELLOW_DYE = registerItem("yellow_dye", paramProperties -> new DyeItem(DyeColor.YELLOW, paramProperties));
/* 1169 */     LIME_DYE = registerItem("lime_dye", paramProperties -> new DyeItem(DyeColor.LIME, paramProperties));
/* 1170 */     PINK_DYE = registerItem("pink_dye", paramProperties -> new DyeItem(DyeColor.PINK, paramProperties));
/* 1171 */     GRAY_DYE = registerItem("gray_dye", paramProperties -> new DyeItem(DyeColor.GRAY, paramProperties));
/* 1172 */     LIGHT_GRAY_DYE = registerItem("light_gray_dye", paramProperties -> new DyeItem(DyeColor.LIGHT_GRAY, paramProperties));
/* 1173 */     CYAN_DYE = registerItem("cyan_dye", paramProperties -> new DyeItem(DyeColor.CYAN, paramProperties));
/* 1174 */     PURPLE_DYE = registerItem("purple_dye", paramProperties -> new DyeItem(DyeColor.PURPLE, paramProperties));
/* 1175 */     BLUE_DYE = registerItem("blue_dye", paramProperties -> new DyeItem(DyeColor.BLUE, paramProperties));
/* 1176 */     BROWN_DYE = registerItem("brown_dye", paramProperties -> new DyeItem(DyeColor.BROWN, paramProperties));
/* 1177 */     GREEN_DYE = registerItem("green_dye", paramProperties -> new DyeItem(DyeColor.GREEN, paramProperties));
/* 1178 */     RED_DYE = registerItem("red_dye", paramProperties -> new DyeItem(DyeColor.RED, paramProperties));
/* 1179 */     BLACK_DYE = registerItem("black_dye", paramProperties -> new DyeItem(DyeColor.BLACK, paramProperties));
/* 1180 */   } public static final Item BONE_MEAL = registerItem("bone_meal", BoneMealItem::new);
/* 1181 */   public static final Item BONE = registerItem("bone");
/* 1182 */   public static final Item SUGAR = registerItem("sugar");
/* 1183 */   public static final Item CAKE = registerBlock(Blocks.CAKE, (new Item.Properties()).stacksTo(1));
/* 1184 */   public static final Item WHITE_BED = registerBlock(Blocks.WHITE_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1185 */   public static final Item ORANGE_BED = registerBlock(Blocks.ORANGE_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1186 */   public static final Item MAGENTA_BED = registerBlock(Blocks.MAGENTA_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1187 */   public static final Item LIGHT_BLUE_BED = registerBlock(Blocks.LIGHT_BLUE_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1188 */   public static final Item YELLOW_BED = registerBlock(Blocks.YELLOW_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1189 */   public static final Item LIME_BED = registerBlock(Blocks.LIME_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1190 */   public static final Item PINK_BED = registerBlock(Blocks.PINK_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1191 */   public static final Item GRAY_BED = registerBlock(Blocks.GRAY_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1192 */   public static final Item LIGHT_GRAY_BED = registerBlock(Blocks.LIGHT_GRAY_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1193 */   public static final Item CYAN_BED = registerBlock(Blocks.CYAN_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1194 */   public static final Item PURPLE_BED = registerBlock(Blocks.PURPLE_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1195 */   public static final Item BLUE_BED = registerBlock(Blocks.BLUE_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1196 */   public static final Item BROWN_BED = registerBlock(Blocks.BROWN_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1197 */   public static final Item GREEN_BED = registerBlock(Blocks.GREEN_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1198 */   public static final Item RED_BED = registerBlock(Blocks.RED_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1199 */   public static final Item BLACK_BED = registerBlock(Blocks.BLACK_BED, BedItem::new, (new Item.Properties()).stacksTo(1));
/* 1200 */   public static final Item COOKIE = registerItem("cookie", (new Item.Properties()).food(Foods.COOKIE)); public static final Item CRAFTER; static {
/* 1201 */     CRAFTER = registerBlock(Blocks.CRAFTER, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1202 */   } public static final Item FILLED_MAP = registerItem("filled_map", MapItem::new, (new Item.Properties()).<MapItemColor>component(DataComponents.MAP_COLOR, MapItemColor.DEFAULT).component(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY));
/* 1203 */   public static final Item SHEARS = registerItem("shears", ShearsItem::new, (new Item.Properties()).durability(238).component(DataComponents.TOOL, ShearsItem.createToolProperties()));
/* 1204 */   public static final Item MELON_SLICE = registerItem("melon_slice", (new Item.Properties()).food(Foods.MELON_SLICE));
/* 1205 */   public static final Item DRIED_KELP = registerItem("dried_kelp", (new Item.Properties()).food(Foods.DRIED_KELP, Consumables.DRIED_KELP));
/* 1206 */   public static final Item PUMPKIN_SEEDS = registerItem(net.minecraft.references.Items.PUMPKIN_SEEDS, createBlockItemWithCustomItemName(Blocks.PUMPKIN_STEM));
/* 1207 */   public static final Item MELON_SEEDS = registerItem(net.minecraft.references.Items.MELON_SEEDS, createBlockItemWithCustomItemName(Blocks.MELON_STEM));
/* 1208 */   public static final Item BEEF = registerItem("beef", (new Item.Properties()).food(Foods.BEEF));
/* 1209 */   public static final Item COOKED_BEEF = registerItem("cooked_beef", (new Item.Properties()).food(Foods.COOKED_BEEF));
/* 1210 */   public static final Item CHICKEN = registerItem("chicken", (new Item.Properties()).food(Foods.CHICKEN, Consumables.CHICKEN));
/* 1211 */   public static final Item COOKED_CHICKEN = registerItem("cooked_chicken", (new Item.Properties()).food(Foods.COOKED_CHICKEN));
/* 1212 */   public static final Item ROTTEN_FLESH = registerItem("rotten_flesh", (new Item.Properties()).food(Foods.ROTTEN_FLESH, Consumables.ROTTEN_FLESH));
/* 1213 */   public static final Item ENDER_PEARL = registerItem("ender_pearl", EnderpearlItem::new, (new Item.Properties()).stacksTo(16).useCooldown(1.0F));
/* 1214 */   public static final Item BLAZE_ROD = registerItem("blaze_rod");
/* 1215 */   public static final Item GHAST_TEAR = registerItem("ghast_tear");
/* 1216 */   public static final Item GOLD_NUGGET = registerItem("gold_nugget");
/* 1217 */   public static final Item NETHER_WART = registerItem("nether_wart", createBlockItemWithCustomItemName(Blocks.NETHER_WART));
/* 1218 */   public static final Item GLASS_BOTTLE = registerItem("glass_bottle", BottleItem::new);
/* 1219 */   public static final Item POTION = registerItem("potion", PotionItem::new, (new Item.Properties()).stacksTo(1).<PotionContents>component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).<Consumable>component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(GLASS_BOTTLE));
/* 1220 */   public static final Item SPIDER_EYE = registerItem("spider_eye", (new Item.Properties()).food(Foods.SPIDER_EYE, Consumables.SPIDER_EYE));
/* 1221 */   public static final Item FERMENTED_SPIDER_EYE = registerItem("fermented_spider_eye");
/* 1222 */   public static final Item BLAZE_POWDER = registerItem("blaze_powder");
/* 1223 */   public static final Item MAGMA_CREAM = registerItem("magma_cream"); public static final Item BREWING_STAND; static {
/* 1224 */     BREWING_STAND = registerBlock(Blocks.BREWING_STAND, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1225 */   } public static final Item CAULDRON = registerBlock(Blocks.CAULDRON, new Block[] { Blocks.WATER_CAULDRON, Blocks.LAVA_CAULDRON, Blocks.POWDER_SNOW_CAULDRON });
/* 1226 */   public static final Item ENDER_EYE = registerItem("ender_eye", EnderEyeItem::new);
/* 1227 */   public static final Item GLISTERING_MELON_SLICE = registerItem("glistering_melon_slice");
/*      */ 
/*      */   
/* 1230 */   public static final Item CHICKEN_SPAWN_EGG = registerSpawnEgg(EntityType.CHICKEN);
/* 1231 */   public static final Item COW_SPAWN_EGG = registerSpawnEgg(EntityType.COW);
/* 1232 */   public static final Item PIG_SPAWN_EGG = registerSpawnEgg(EntityType.PIG);
/* 1233 */   public static final Item SHEEP_SPAWN_EGG = registerSpawnEgg(EntityType.SHEEP);
/*      */ 
/*      */   
/* 1236 */   public static final Item CAMEL_SPAWN_EGG = registerSpawnEgg(EntityType.CAMEL);
/* 1237 */   public static final Item DONKEY_SPAWN_EGG = registerSpawnEgg(EntityType.DONKEY);
/* 1238 */   public static final Item HORSE_SPAWN_EGG = registerSpawnEgg(EntityType.HORSE);
/* 1239 */   public static final Item MULE_SPAWN_EGG = registerSpawnEgg(EntityType.MULE);
/*      */ 
/*      */   
/* 1242 */   public static final Item CAT_SPAWN_EGG = registerSpawnEgg(EntityType.CAT);
/* 1243 */   public static final Item PARROT_SPAWN_EGG = registerSpawnEgg(EntityType.PARROT);
/* 1244 */   public static final Item WOLF_SPAWN_EGG = registerSpawnEgg(EntityType.WOLF);
/*      */ 
/*      */   
/* 1247 */   public static final Item ARMADILLO_SPAWN_EGG = registerSpawnEgg(EntityType.ARMADILLO);
/* 1248 */   public static final Item BAT_SPAWN_EGG = registerSpawnEgg(EntityType.BAT);
/* 1249 */   public static final Item BEE_SPAWN_EGG = registerSpawnEgg(EntityType.BEE);
/* 1250 */   public static final Item FOX_SPAWN_EGG = registerSpawnEgg(EntityType.FOX);
/* 1251 */   public static final Item GOAT_SPAWN_EGG = registerSpawnEgg(EntityType.GOAT);
/* 1252 */   public static final Item LLAMA_SPAWN_EGG = registerSpawnEgg(EntityType.LLAMA);
/* 1253 */   public static final Item OCELOT_SPAWN_EGG = registerSpawnEgg(EntityType.OCELOT);
/* 1254 */   public static final Item PANDA_SPAWN_EGG = registerSpawnEgg(EntityType.PANDA);
/* 1255 */   public static final Item POLAR_BEAR_SPAWN_EGG = registerSpawnEgg(EntityType.POLAR_BEAR);
/* 1256 */   public static final Item RABBIT_SPAWN_EGG = registerSpawnEgg(EntityType.RABBIT);
/*      */ 
/*      */   
/* 1259 */   public static final Item AXOLOTL_SPAWN_EGG = registerSpawnEgg(EntityType.AXOLOTL);
/* 1260 */   public static final Item COD_SPAWN_EGG = registerSpawnEgg(EntityType.COD);
/* 1261 */   public static final Item DOLPHIN_SPAWN_EGG = registerSpawnEgg(EntityType.DOLPHIN);
/* 1262 */   public static final Item FROG_SPAWN_EGG = registerSpawnEgg(EntityType.FROG);
/* 1263 */   public static final Item GLOW_SQUID_SPAWN_EGG = registerSpawnEgg(EntityType.GLOW_SQUID);
/* 1264 */   public static final Item NAUTILUS_SPAWN_EGG = registerSpawnEgg(EntityType.NAUTILUS);
/* 1265 */   public static final Item PUFFERFISH_SPAWN_EGG = registerSpawnEgg(EntityType.PUFFERFISH);
/* 1266 */   public static final Item SALMON_SPAWN_EGG = registerSpawnEgg(EntityType.SALMON);
/* 1267 */   public static final Item SQUID_SPAWN_EGG = registerSpawnEgg(EntityType.SQUID);
/* 1268 */   public static final Item TADPOLE_SPAWN_EGG = registerSpawnEgg(EntityType.TADPOLE);
/* 1269 */   public static final Item TROPICAL_FISH_SPAWN_EGG = registerSpawnEgg(EntityType.TROPICAL_FISH);
/* 1270 */   public static final Item TURTLE_SPAWN_EGG = registerSpawnEgg(EntityType.TURTLE);
/*      */ 
/*      */   
/* 1273 */   public static final Item ALLAY_SPAWN_EGG = registerSpawnEgg(EntityType.ALLAY);
/* 1274 */   public static final Item MOOSHROOM_SPAWN_EGG = registerSpawnEgg(EntityType.MOOSHROOM);
/* 1275 */   public static final Item SNIFFER_SPAWN_EGG = registerSpawnEgg(EntityType.SNIFFER);
/*      */ 
/*      */   
/* 1278 */   public static final Item COPPER_GOLEM_SPAWN_EGG = registerSpawnEgg(EntityType.COPPER_GOLEM);
/* 1279 */   public static final Item IRON_GOLEM_SPAWN_EGG = registerSpawnEgg(EntityType.IRON_GOLEM);
/* 1280 */   public static final Item SNOW_GOLEM_SPAWN_EGG = registerSpawnEgg(EntityType.SNOW_GOLEM);
/*      */ 
/*      */   
/* 1283 */   public static final Item TRADER_LLAMA_SPAWN_EGG = registerSpawnEgg(EntityType.TRADER_LLAMA);
/* 1284 */   public static final Item VILLAGER_SPAWN_EGG = registerSpawnEgg(EntityType.VILLAGER);
/* 1285 */   public static final Item WANDERING_TRADER_SPAWN_EGG = registerSpawnEgg(EntityType.WANDERING_TRADER);
/*      */ 
/*      */   
/* 1288 */   public static final Item BOGGED_SPAWN_EGG = registerSpawnEgg(EntityType.BOGGED);
/* 1289 */   public static final Item CAMEL_HUSK_SPAWN_EGG = registerSpawnEgg(EntityType.CAMEL_HUSK);
/* 1290 */   public static final Item DROWNED_SPAWN_EGG = registerSpawnEgg(EntityType.DROWNED);
/* 1291 */   public static final Item HUSK_SPAWN_EGG = registerSpawnEgg(EntityType.HUSK);
/* 1292 */   public static final Item PARCHED_SPAWN_EGG = registerSpawnEgg(EntityType.PARCHED);
/* 1293 */   public static final Item SKELETON_SPAWN_EGG = registerSpawnEgg(EntityType.SKELETON);
/* 1294 */   public static final Item SKELETON_HORSE_SPAWN_EGG = registerSpawnEgg(EntityType.SKELETON_HORSE);
/* 1295 */   public static final Item STRAY_SPAWN_EGG = registerSpawnEgg(EntityType.STRAY);
/* 1296 */   public static final Item WITHER_SPAWN_EGG = registerSpawnEgg(EntityType.WITHER);
/* 1297 */   public static final Item WITHER_SKELETON_SPAWN_EGG = registerSpawnEgg(EntityType.WITHER_SKELETON);
/* 1298 */   public static final Item ZOMBIE_SPAWN_EGG = registerSpawnEgg(EntityType.ZOMBIE);
/* 1299 */   public static final Item ZOMBIE_HORSE_SPAWN_EGG = registerSpawnEgg(EntityType.ZOMBIE_HORSE);
/* 1300 */   public static final Item ZOMBIE_NAUTILUS_SPAWN_EGG = registerSpawnEgg(EntityType.ZOMBIE_NAUTILUS);
/* 1301 */   public static final Item ZOMBIE_VILLAGER_SPAWN_EGG = registerSpawnEgg(EntityType.ZOMBIE_VILLAGER);
/*      */ 
/*      */   
/* 1304 */   public static final Item CAVE_SPIDER_SPAWN_EGG = registerSpawnEgg(EntityType.CAVE_SPIDER);
/* 1305 */   public static final Item SPIDER_SPAWN_EGG = registerSpawnEgg(EntityType.SPIDER);
/*      */ 
/*      */   
/* 1308 */   public static final Item BREEZE_SPAWN_EGG = registerSpawnEgg(EntityType.BREEZE);
/* 1309 */   public static final Item CREAKING_SPAWN_EGG = registerSpawnEgg(EntityType.CREAKING);
/* 1310 */   public static final Item CREEPER_SPAWN_EGG = registerSpawnEgg(EntityType.CREEPER);
/* 1311 */   public static final Item ELDER_GUARDIAN_SPAWN_EGG = registerSpawnEgg(EntityType.ELDER_GUARDIAN);
/* 1312 */   public static final Item GUARDIAN_SPAWN_EGG = registerSpawnEgg(EntityType.GUARDIAN);
/* 1313 */   public static final Item PHANTOM_SPAWN_EGG = registerSpawnEgg(EntityType.PHANTOM);
/* 1314 */   public static final Item SILVERFISH_SPAWN_EGG = registerSpawnEgg(EntityType.SILVERFISH);
/* 1315 */   public static final Item SLIME_SPAWN_EGG = registerSpawnEgg(EntityType.SLIME);
/* 1316 */   public static final Item WARDEN_SPAWN_EGG = registerSpawnEgg(EntityType.WARDEN);
/* 1317 */   public static final Item WITCH_SPAWN_EGG = registerSpawnEgg(EntityType.WITCH);
/*      */ 
/*      */   
/* 1320 */   public static final Item EVOKER_SPAWN_EGG = registerSpawnEgg(EntityType.EVOKER);
/* 1321 */   public static final Item PILLAGER_SPAWN_EGG = registerSpawnEgg(EntityType.PILLAGER);
/* 1322 */   public static final Item RAVAGER_SPAWN_EGG = registerSpawnEgg(EntityType.RAVAGER);
/* 1323 */   public static final Item VINDICATOR_SPAWN_EGG = registerSpawnEgg(EntityType.VINDICATOR);
/* 1324 */   public static final Item VEX_SPAWN_EGG = registerSpawnEgg(EntityType.VEX);
/*      */ 
/*      */   
/* 1327 */   public static final Item BLAZE_SPAWN_EGG = registerSpawnEgg(EntityType.BLAZE);
/* 1328 */   public static final Item GHAST_SPAWN_EGG = registerSpawnEgg(EntityType.GHAST);
/* 1329 */   public static final Item HAPPY_GHAST_SPAWN_EGG = registerSpawnEgg(EntityType.HAPPY_GHAST);
/* 1330 */   public static final Item HOGLIN_SPAWN_EGG = registerSpawnEgg(EntityType.HOGLIN);
/* 1331 */   public static final Item MAGMA_CUBE_SPAWN_EGG = registerSpawnEgg(EntityType.MAGMA_CUBE);
/* 1332 */   public static final Item PIGLIN_SPAWN_EGG = registerSpawnEgg(EntityType.PIGLIN);
/* 1333 */   public static final Item PIGLIN_BRUTE_SPAWN_EGG = registerSpawnEgg(EntityType.PIGLIN_BRUTE);
/* 1334 */   public static final Item STRIDER_SPAWN_EGG = registerSpawnEgg(EntityType.STRIDER);
/* 1335 */   public static final Item ZOGLIN_SPAWN_EGG = registerSpawnEgg(EntityType.ZOGLIN);
/* 1336 */   public static final Item ZOMBIFIED_PIGLIN_SPAWN_EGG = registerSpawnEgg(EntityType.ZOMBIFIED_PIGLIN);
/*      */ 
/*      */   
/* 1339 */   public static final Item ENDER_DRAGON_SPAWN_EGG = registerSpawnEgg(EntityType.ENDER_DRAGON);
/* 1340 */   public static final Item ENDERMAN_SPAWN_EGG = registerSpawnEgg(EntityType.ENDERMAN);
/* 1341 */   public static final Item ENDERMITE_SPAWN_EGG = registerSpawnEgg(EntityType.ENDERMITE);
/* 1342 */   public static final Item SHULKER_SPAWN_EGG = registerSpawnEgg(EntityType.SHULKER);
/*      */   
/* 1344 */   public static final Item EXPERIENCE_BOTTLE = registerItem("experience_bottle", ExperienceBottleItem::new, (new Item.Properties()).rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)));
/* 1345 */   public static final Item FIRE_CHARGE = registerItem("fire_charge", FireChargeItem::new);
/* 1346 */   public static final Item WIND_CHARGE = registerItem("wind_charge", WindChargeItem::new, (new Item.Properties()).useCooldown(0.5F));
/* 1347 */   public static final Item WRITABLE_BOOK = registerItem("writable_book", WritableBookItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY));
/* 1348 */   public static final Item WRITTEN_BOOK = registerItem("written_book", WrittenBookItem::new, (new Item.Properties()).stacksTo(16).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)));
/* 1349 */   public static final Item BREEZE_ROD = registerItem("breeze_rod");
/* 1350 */   public static final Item MACE = registerItem("mace", MaceItem::new, (new Item.Properties()).rarity(Rarity.EPIC).durability(500).<Tool>component(DataComponents.TOOL, MaceItem.createToolProperties()).repairable(BREEZE_ROD).attributes(MaceItem.createAttributes()).enchantable(15).component(DataComponents.WEAPON, new Weapon(1))); public static final Item ITEM_FRAME; public static final Item GLOW_ITEM_FRAME; static {
/* 1351 */     ITEM_FRAME = registerItem("item_frame", paramProperties -> new ItemFrameItem(EntityType.ITEM_FRAME, paramProperties));
/* 1352 */     GLOW_ITEM_FRAME = registerItem("glow_item_frame", paramProperties -> new ItemFrameItem(EntityType.GLOW_ITEM_FRAME, paramProperties));
/* 1353 */   } public static final Item FLOWER_POT = registerBlock(Blocks.FLOWER_POT);
/* 1354 */   public static final Item CARROT = registerItem("carrot", createBlockItemWithCustomItemName(Blocks.CARROTS), (new Item.Properties()).food(Foods.CARROT));
/* 1355 */   public static final Item POTATO = registerItem("potato", createBlockItemWithCustomItemName(Blocks.POTATOES), (new Item.Properties()).food(Foods.POTATO));
/* 1356 */   public static final Item BAKED_POTATO = registerItem("baked_potato", (new Item.Properties()).food(Foods.BAKED_POTATO));
/* 1357 */   public static final Item POISONOUS_POTATO = registerItem("poisonous_potato", (new Item.Properties()).food(Foods.POISONOUS_POTATO, Consumables.POISONOUS_POTATO));
/* 1358 */   public static final Item MAP = registerItem("map", EmptyMapItem::new);
/* 1359 */   public static final Item GOLDEN_CARROT = registerItem("golden_carrot", (new Item.Properties()).food(Foods.GOLDEN_CARROT)); public static final Item SKELETON_SKULL; public static final Item WITHER_SKELETON_SKULL; public static final Item PLAYER_HEAD; public static final Item ZOMBIE_HEAD; public static final Item CREEPER_HEAD; public static final Item DRAGON_HEAD; public static final Item PIGLIN_HEAD; static {
/* 1360 */     SKELETON_SKULL = registerBlock(Blocks.SKELETON_SKULL, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.SKELETON_WALL_SKULL, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.UNCOMMON).equippableUnswappable(EquipmentSlot.HEAD));
/* 1361 */     WITHER_SKELETON_SKULL = registerBlock(Blocks.WITHER_SKELETON_SKULL, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.WITHER_SKELETON_WALL_SKULL, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.RARE).equippableUnswappable(EquipmentSlot.HEAD));
/* 1362 */     PLAYER_HEAD = registerBlock(Blocks.PLAYER_HEAD, (paramBlock, paramProperties) -> new PlayerHeadItem(paramBlock, Blocks.PLAYER_WALL_HEAD, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.UNCOMMON).equippableUnswappable(EquipmentSlot.HEAD));
/* 1363 */     ZOMBIE_HEAD = registerBlock(Blocks.ZOMBIE_HEAD, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.ZOMBIE_WALL_HEAD, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.UNCOMMON).equippableUnswappable(EquipmentSlot.HEAD));
/* 1364 */     CREEPER_HEAD = registerBlock(Blocks.CREEPER_HEAD, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.CREEPER_WALL_HEAD, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.UNCOMMON).equippableUnswappable(EquipmentSlot.HEAD));
/* 1365 */     DRAGON_HEAD = registerBlock(Blocks.DRAGON_HEAD, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.DRAGON_WALL_HEAD, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.EPIC).equippableUnswappable(EquipmentSlot.HEAD));
/* 1366 */     PIGLIN_HEAD = registerBlock(Blocks.PIGLIN_HEAD, (paramBlock, paramProperties) -> new StandingAndWallBlockItem(paramBlock, Blocks.PIGLIN_WALL_HEAD, Direction.DOWN, Waypoint.addHideAttribute(paramProperties)), (new Item.Properties()).rarity(Rarity.UNCOMMON).equippableUnswappable(EquipmentSlot.HEAD));
/* 1367 */   } public static final Item NETHER_STAR = registerItem("nether_star", (new Item.Properties()).rarity(Rarity.RARE).<Boolean>component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)).component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION)));
/* 1368 */   public static final Item PUMPKIN_PIE = registerItem("pumpkin_pie", (new Item.Properties()).food(Foods.PUMPKIN_PIE));
/* 1369 */   public static final Item FIREWORK_ROCKET = registerItem("firework_rocket", FireworkRocketItem::new, (new Item.Properties()).component(DataComponents.FIREWORKS, new Fireworks(1, List.of())));
/* 1370 */   public static final Item FIREWORK_STAR = registerItem("firework_star");
/* 1371 */   public static final Item ENCHANTED_BOOK = registerItem("enchanted_book", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).<ItemEnchantments>component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)));
/* 1372 */   public static final Item NETHER_BRICK = registerItem("nether_brick");
/* 1373 */   public static final Item RESIN_BRICK = registerItem("resin_brick", (new Item.Properties()).trimMaterial(TrimMaterials.RESIN));
/* 1374 */   public static final Item PRISMARINE_SHARD = registerItem("prismarine_shard");
/* 1375 */   public static final Item PRISMARINE_CRYSTALS = registerItem("prismarine_crystals");
/* 1376 */   public static final Item RABBIT = registerItem("rabbit", (new Item.Properties()).food(Foods.RABBIT));
/* 1377 */   public static final Item COOKED_RABBIT = registerItem("cooked_rabbit", (new Item.Properties()).food(Foods.COOKED_RABBIT));
/* 1378 */   public static final Item RABBIT_STEW = registerItem("rabbit_stew", (new Item.Properties()).stacksTo(1).food(Foods.RABBIT_STEW).usingConvertsTo(BOWL));
/* 1379 */   public static final Item RABBIT_FOOT = registerItem("rabbit_foot");
/* 1380 */   public static final Item RABBIT_HIDE = registerItem("rabbit_hide");
/* 1381 */   public static final Item ARMOR_STAND = registerItem("armor_stand", ArmorStandItem::new, (new Item.Properties()).stacksTo(16));
/* 1382 */   public static final Item COPPER_HORSE_ARMOR = registerItem("copper_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.COPPER));
/* 1383 */   public static final Item IRON_HORSE_ARMOR = registerItem("iron_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.IRON));
/* 1384 */   public static final Item GOLDEN_HORSE_ARMOR = registerItem("golden_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.GOLD));
/* 1385 */   public static final Item DIAMOND_HORSE_ARMOR = registerItem("diamond_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.DIAMOND));
/* 1386 */   public static final Item NETHERITE_HORSE_ARMOR = registerItem("netherite_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.NETHERITE).fireResistant());
/* 1387 */   public static final Item LEATHER_HORSE_ARMOR = registerItem("leather_horse_armor", (new Item.Properties()).horseArmor(ArmorMaterials.LEATHER));
/* 1388 */   public static final Item LEAD = registerItem("lead", LeadItem::new);
/* 1389 */   public static final Item NAME_TAG = registerItem("name_tag", NameTagItem::new); public static final Item COMMAND_BLOCK_MINECART; static {
/* 1390 */     COMMAND_BLOCK_MINECART = registerItem("command_block_minecart", paramProperties -> new MinecartItem(EntityType.COMMAND_BLOCK_MINECART, paramProperties), (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC));
/* 1391 */   } public static final Item MUTTON = registerItem("mutton", (new Item.Properties()).food(Foods.MUTTON));
/* 1392 */   public static final Item COOKED_MUTTON = registerItem("cooked_mutton", (new Item.Properties()).food(Foods.COOKED_MUTTON)); public static final Item WHITE_BANNER; public static final Item ORANGE_BANNER; public static final Item MAGENTA_BANNER; public static final Item LIGHT_BLUE_BANNER; public static final Item YELLOW_BANNER; public static final Item LIME_BANNER; public static final Item PINK_BANNER; public static final Item GRAY_BANNER; public static final Item LIGHT_GRAY_BANNER; public static final Item CYAN_BANNER; public static final Item PURPLE_BANNER; public static final Item BLUE_BANNER; public static final Item BROWN_BANNER; public static final Item GREEN_BANNER; public static final Item RED_BANNER; public static final Item BLACK_BANNER; static {
/* 1393 */     WHITE_BANNER = registerBlock(Blocks.WHITE_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.WHITE_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1394 */     ORANGE_BANNER = registerBlock(Blocks.ORANGE_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.ORANGE_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1395 */     MAGENTA_BANNER = registerBlock(Blocks.MAGENTA_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.MAGENTA_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1396 */     LIGHT_BLUE_BANNER = registerBlock(Blocks.LIGHT_BLUE_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.LIGHT_BLUE_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1397 */     YELLOW_BANNER = registerBlock(Blocks.YELLOW_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.YELLOW_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1398 */     LIME_BANNER = registerBlock(Blocks.LIME_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.LIME_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1399 */     PINK_BANNER = registerBlock(Blocks.PINK_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.PINK_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1400 */     GRAY_BANNER = registerBlock(Blocks.GRAY_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.GRAY_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1401 */     LIGHT_GRAY_BANNER = registerBlock(Blocks.LIGHT_GRAY_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.LIGHT_GRAY_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1402 */     CYAN_BANNER = registerBlock(Blocks.CYAN_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.CYAN_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1403 */     PURPLE_BANNER = registerBlock(Blocks.PURPLE_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.PURPLE_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1404 */     BLUE_BANNER = registerBlock(Blocks.BLUE_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.BLUE_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1405 */     BROWN_BANNER = registerBlock(Blocks.BROWN_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.BROWN_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1406 */     GREEN_BANNER = registerBlock(Blocks.GREEN_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.GREEN_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1407 */     RED_BANNER = registerBlock(Blocks.RED_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.RED_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1408 */     BLACK_BANNER = registerBlock(Blocks.BLACK_BANNER, (paramBlock, paramProperties) -> new BannerItem(paramBlock, Blocks.BLACK_WALL_BANNER, paramProperties), (new Item.Properties()).stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
/* 1409 */   } public static final Item END_CRYSTAL = registerItem("end_crystal", EndCrystalItem::new, (new Item.Properties()).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)));
/* 1410 */   public static final Item CHORUS_FRUIT = registerItem("chorus_fruit", (new Item.Properties()).food(Foods.CHORUS_FRUIT, Consumables.CHORUS_FRUIT).useCooldown(1.0F));
/* 1411 */   public static final Item POPPED_CHORUS_FRUIT = registerItem("popped_chorus_fruit");
/* 1412 */   public static final Item TORCHFLOWER_SEEDS = registerItem("torchflower_seeds", createBlockItemWithCustomItemName(Blocks.TORCHFLOWER_CROP));
/* 1413 */   public static final Item PITCHER_POD = registerItem("pitcher_pod", createBlockItemWithCustomItemName(Blocks.PITCHER_CROP));
/* 1414 */   public static final Item BEETROOT = registerItem("beetroot", (new Item.Properties()).food(Foods.BEETROOT));
/* 1415 */   public static final Item BEETROOT_SEEDS = registerItem("beetroot_seeds", createBlockItemWithCustomItemName(Blocks.BEETROOTS));
/* 1416 */   public static final Item BEETROOT_SOUP = registerItem("beetroot_soup", (new Item.Properties()).stacksTo(1).food(Foods.BEETROOT_SOUP).usingConvertsTo(BOWL));
/* 1417 */   public static final Item DRAGON_BREATH = registerItem("dragon_breath", (new Item.Properties()).craftRemainder(GLASS_BOTTLE).rarity(Rarity.UNCOMMON));
/* 1418 */   public static final Item SPLASH_POTION = registerItem("splash_potion", SplashPotionItem::new, (new Item.Properties()).stacksTo(1).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
/* 1419 */   public static final Item SPECTRAL_ARROW = registerItem("spectral_arrow", SpectralArrowItem::new);
/* 1420 */   public static final Item TIPPED_ARROW = registerItem("tipped_arrow", TippedArrowItem::new, (new Item.Properties()).<PotionContents>component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(DataComponents.POTION_DURATION_SCALE, Float.valueOf(0.125F)));
/* 1421 */   public static final Item LINGERING_POTION = registerItem("lingering_potion", LingeringPotionItem::new, (new Item.Properties()).stacksTo(1).<PotionContents>component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(DataComponents.POTION_DURATION_SCALE, Float.valueOf(0.25F)));
/* 1422 */   public static final Item SHIELD = registerItem("shield", ShieldItem::new, (new Item.Properties()).durability(336).<BannerPatternLayers>component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).repairable(ItemTags.WOODEN_TOOL_MATERIALS).equippableUnswappable(EquipmentSlot.OFFHAND).<BlocksAttacks>component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(0.25F, 1.0F, 
/*      */ 
/*      */           
/* 1425 */           List.of(new BlocksAttacks.DamageReduction(90.0F, 
/* 1426 */               Optional.empty(), 0.0F, 1.0F)), new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F), 
/*      */ 
/*      */           
/* 1429 */           Optional.of(DamageTypeTags.BYPASSES_SHIELD), 
/* 1430 */           Optional.of(SoundEvents.SHIELD_BLOCK), 
/* 1431 */           Optional.of(SoundEvents.SHIELD_BREAK)))
/* 1432 */       .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));
/*      */   
/* 1434 */   public static final Item WOODEN_SPEAR = registerItem("wooden_spear", (new Item.Properties()).spear(ToolMaterial.WOOD, 0.65F, 0.7F, 0.75F, 5.0F, 14.0F, 10.0F, 5.1F, 15.0F, 4.6F));
/* 1435 */   public static final Item STONE_SPEAR = registerItem("stone_spear", (new Item.Properties()).spear(ToolMaterial.STONE, 0.75F, 0.82F, 0.7F, 4.5F, 10.0F, 9.0F, 5.1F, 13.75F, 4.6F));
/* 1436 */   public static final Item COPPER_SPEAR = registerItem("copper_spear", (new Item.Properties()).spear(ToolMaterial.COPPER, 0.85F, 0.82F, 0.65F, 4.0F, 9.0F, 8.25F, 5.1F, 12.5F, 4.6F));
/* 1437 */   public static final Item IRON_SPEAR = registerItem("iron_spear", (new Item.Properties()).spear(ToolMaterial.IRON, 0.95F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F));
/* 1438 */   public static final Item GOLDEN_SPEAR = registerItem("golden_spear", (new Item.Properties()).spear(ToolMaterial.GOLD, 0.95F, 0.7F, 0.7F, 3.5F, 10.0F, 8.5F, 5.1F, 13.75F, 4.6F));
/* 1439 */   public static final Item DIAMOND_SPEAR = registerItem("diamond_spear", (new Item.Properties()).spear(ToolMaterial.DIAMOND, 1.05F, 1.075F, 0.5F, 3.0F, 7.5F, 6.5F, 5.1F, 10.0F, 4.6F));
/* 1440 */   public static final Item NETHERITE_SPEAR = registerItem("netherite_spear", (new Item.Properties()).spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 7.0F, 5.5F, 5.1F, 8.75F, 4.6F).fireResistant());
/* 1441 */   public static final Item TOTEM_OF_UNDYING = registerItem("totem_of_undying", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING));
/* 1442 */   public static final Item SHULKER_SHELL = registerItem("shulker_shell");
/* 1443 */   public static final Item IRON_NUGGET = registerItem("iron_nugget");
/* 1444 */   public static final Item COPPER_NUGGET = registerItem("copper_nugget");
/* 1445 */   public static final Item KNOWLEDGE_BOOK = registerItem("knowledge_book", KnowledgeBookItem::new, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.RECIPES, List.of()));
/* 1446 */   public static final Item DEBUG_STICK = registerItem("debug_stick", DebugStickItem::new, (new Item.Properties()).stacksTo(1).rarity(Rarity.EPIC).<DebugStickState>component(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.valueOf(true)));
/* 1447 */   public static final Item MUSIC_DISC_13 = registerItem("music_disc_13", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.THIRTEEN));
/* 1448 */   public static final Item MUSIC_DISC_CAT = registerItem("music_disc_cat", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.CAT));
/* 1449 */   public static final Item MUSIC_DISC_BLOCKS = registerItem("music_disc_blocks", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.BLOCKS));
/* 1450 */   public static final Item MUSIC_DISC_CHIRP = registerItem("music_disc_chirp", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.CHIRP));
/* 1451 */   public static final Item MUSIC_DISC_CREATOR = registerItem("music_disc_creator", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongs.CREATOR));
/* 1452 */   public static final Item MUSIC_DISC_CREATOR_MUSIC_BOX = registerItem("music_disc_creator_music_box", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.CREATOR_MUSIC_BOX));
/* 1453 */   public static final Item MUSIC_DISC_FAR = registerItem("music_disc_far", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.FAR));
/* 1454 */   public static final Item MUSIC_DISC_LAVA_CHICKEN = registerItem("music_disc_lava_chicken", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongs.LAVA_CHICKEN));
/* 1455 */   public static final Item MUSIC_DISC_MALL = registerItem("music_disc_mall", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.MALL));
/* 1456 */   public static final Item MUSIC_DISC_MELLOHI = registerItem("music_disc_mellohi", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.MELLOHI));
/* 1457 */   public static final Item MUSIC_DISC_STAL = registerItem("music_disc_stal", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.STAL));
/* 1458 */   public static final Item MUSIC_DISC_STRAD = registerItem("music_disc_strad", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.STRAD));
/* 1459 */   public static final Item MUSIC_DISC_WARD = registerItem("music_disc_ward", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.WARD));
/* 1460 */   public static final Item MUSIC_DISC_11 = registerItem("music_disc_11", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.ELEVEN));
/* 1461 */   public static final Item MUSIC_DISC_WAIT = registerItem("music_disc_wait", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.WAIT));
/* 1462 */   public static final Item MUSIC_DISC_OTHERSIDE = registerItem("music_disc_otherside", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongs.OTHERSIDE));
/* 1463 */   public static final Item MUSIC_DISC_RELIC = registerItem("music_disc_relic", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.RELIC));
/* 1464 */   public static final Item MUSIC_DISC_5 = registerItem("music_disc_5", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.FIVE));
/* 1465 */   public static final Item MUSIC_DISC_PIGSTEP = registerItem("music_disc_pigstep", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(JukeboxSongs.PIGSTEP));
/* 1466 */   public static final Item MUSIC_DISC_PRECIPICE = registerItem("music_disc_precipice", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.PRECIPICE));
/* 1467 */   public static final Item MUSIC_DISC_TEARS = registerItem("music_disc_tears", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(JukeboxSongs.TEARS));
/* 1468 */   public static final Item DISC_FRAGMENT_5 = registerItem("disc_fragment_5", DiscFragmentItem::new, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1469 */   public static final Item TRIDENT = registerItem("trident", TridentItem::new, (new Item.Properties()).rarity(Rarity.RARE).durability(250).attributes(TridentItem.createAttributes()).<Tool>component(DataComponents.TOOL, TridentItem.createToolProperties()).enchantable(1).component(DataComponents.WEAPON, new Weapon(1)));
/* 1470 */   public static final Item NAUTILUS_SHELL = registerItem("nautilus_shell", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1471 */   public static final Item IRON_NAUTILUS_ARMOR = registerItem("iron_nautilus_armor", (new Item.Properties()).nautilusArmor(ArmorMaterials.IRON));
/* 1472 */   public static final Item GOLDEN_NAUTILUS_ARMOR = registerItem("golden_nautilus_armor", (new Item.Properties()).nautilusArmor(ArmorMaterials.GOLD));
/* 1473 */   public static final Item DIAMOND_NAUTILUS_ARMOR = registerItem("diamond_nautilus_armor", (new Item.Properties()).nautilusArmor(ArmorMaterials.DIAMOND));
/* 1474 */   public static final Item NETHERITE_NAUTILUS_ARMOR = registerItem("netherite_nautilus_armor", (new Item.Properties()).nautilusArmor(ArmorMaterials.NETHERITE).fireResistant());
/* 1475 */   public static final Item COPPER_NAUTILUS_ARMOR = registerItem("copper_nautilus_armor", (new Item.Properties()).nautilusArmor(ArmorMaterials.COPPER));
/* 1476 */   public static final Item HEART_OF_THE_SEA = registerItem("heart_of_the_sea", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1477 */   public static final Item CROSSBOW = registerItem("crossbow", CrossbowItem::new, (new Item.Properties()).stacksTo(1).durability(465).<ChargedProjectiles>component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).enchantable(1));
/* 1478 */   public static final Item SUSPICIOUS_STEW = registerItem("suspicious_stew", (new Item.Properties()).stacksTo(1).food(Foods.SUSPICIOUS_STEW).<SuspiciousStewEffects>component(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY).usingConvertsTo(BOWL));
/* 1479 */   public static final Item LOOM = registerBlock(Blocks.LOOM);
/* 1480 */   public static final Item FLOWER_BANNER_PATTERN = registerItem("flower_banner_pattern", (new Item.Properties()).stacksTo(1).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_FLOWER));
/* 1481 */   public static final Item CREEPER_BANNER_PATTERN = registerItem("creeper_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_CREEPER));
/* 1482 */   public static final Item SKULL_BANNER_PATTERN = registerItem("skull_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_SKULL));
/* 1483 */   public static final Item MOJANG_BANNER_PATTERN = registerItem("mojang_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_MOJANG));
/* 1484 */   public static final Item GLOBE_BANNER_PATTERN = registerItem("globe_banner_pattern", (new Item.Properties()).stacksTo(1).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_GLOBE));
/* 1485 */   public static final Item PIGLIN_BANNER_PATTERN = registerItem("piglin_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_PIGLIN));
/* 1486 */   public static final Item FLOW_BANNER_PATTERN = registerItem("flow_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_FLOW));
/* 1487 */   public static final Item GUSTER_BANNER_PATTERN = registerItem("guster_banner_pattern", (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_GUSTER));
/* 1488 */   public static final Item FIELD_MASONED_BANNER_PATTERN = registerItem("field_masoned_banner_pattern", (new Item.Properties()).stacksTo(1).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_FIELD_MASONED));
/* 1489 */   public static final Item BORDURE_INDENTED_BANNER_PATTERN = registerItem("bordure_indented_banner_pattern", (new Item.Properties()).stacksTo(1).component(DataComponents.PROVIDES_BANNER_PATTERNS, BannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED));
/* 1490 */   public static final Item GOAT_HORN = registerItem("goat_horn", InstrumentItem::new, (new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(1).component(DataComponents.INSTRUMENT, new InstrumentComponent(Instruments.PONDER_GOAT_HORN)));
/* 1491 */   public static final Item COMPOSTER = registerBlock(Blocks.COMPOSTER); public static final Item BARREL; public static final Item SMOKER; public static final Item BLAST_FURNACE; static {
/* 1492 */     BARREL = registerBlock(Blocks.BARREL, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1493 */     SMOKER = registerBlock(Blocks.SMOKER, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1494 */     BLAST_FURNACE = registerBlock(Blocks.BLAST_FURNACE, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1495 */   } public static final Item CARTOGRAPHY_TABLE = registerBlock(Blocks.CARTOGRAPHY_TABLE);
/* 1496 */   public static final Item FLETCHING_TABLE = registerBlock(Blocks.FLETCHING_TABLE);
/* 1497 */   public static final Item GRINDSTONE = registerBlock(Blocks.GRINDSTONE);
/* 1498 */   public static final Item SMITHING_TABLE = registerBlock(Blocks.SMITHING_TABLE);
/* 1499 */   public static final Item STONECUTTER = registerBlock(Blocks.STONECUTTER);
/* 1500 */   public static final Item BELL = registerBlock(Blocks.BELL);
/*      */   
/* 1502 */   public static final Item LANTERN = registerBlock(Blocks.LANTERN);
/* 1503 */   public static final Item SOUL_LANTERN = registerBlock(Blocks.SOUL_LANTERN);
/* 1504 */   public static final WeatheringCopperItems COPPER_LANTERN = WeatheringCopperItems.create(Blocks.COPPER_LANTERN, Items::registerBlock);
/*      */   
/* 1506 */   public static final Item SWEET_BERRIES = registerItem("sweet_berries", createBlockItemWithCustomItemName(Blocks.SWEET_BERRY_BUSH), (new Item.Properties()).food(Foods.SWEET_BERRIES));
/* 1507 */   public static final Item GLOW_BERRIES = registerItem("glow_berries", createBlockItemWithCustomItemName(Blocks.CAVE_VINES), (new Item.Properties()).food(Foods.GLOW_BERRIES)); public static final Item CAMPFIRE; public static final Item SOUL_CAMPFIRE; static {
/* 1508 */     CAMPFIRE = registerBlock(Blocks.CAMPFIRE, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1509 */     SOUL_CAMPFIRE = registerBlock(Blocks.SOUL_CAMPFIRE, paramProperties -> paramProperties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
/* 1510 */   } public static final Item SHROOMLIGHT = registerBlock(Blocks.SHROOMLIGHT);
/*      */   
/* 1512 */   public static final Item HONEYCOMB = registerItem("honeycomb", HoneycombItem::new);
/* 1513 */   public static final Item BEE_NEST = registerBlock(Blocks.BEE_NEST, (new Item.Properties()).<Bees>component(DataComponents.BEES, Bees.EMPTY).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)BeehiveBlock.HONEY_LEVEL, Integer.valueOf(0))));
/* 1514 */   public static final Item BEEHIVE = registerBlock(Blocks.BEEHIVE, (new Item.Properties()).<Bees>component(DataComponents.BEES, Bees.EMPTY).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)BeehiveBlock.HONEY_LEVEL, Integer.valueOf(0))));
/* 1515 */   public static final Item HONEY_BOTTLE = registerItem("honey_bottle", (new Item.Properties()).craftRemainder(GLASS_BOTTLE).food(Foods.HONEY_BOTTLE, Consumables.HONEY_BOTTLE).usingConvertsTo(GLASS_BOTTLE).stacksTo(16));
/* 1516 */   public static final Item HONEYCOMB_BLOCK = registerBlock(Blocks.HONEYCOMB_BLOCK);
/*      */   
/* 1518 */   public static final Item LODESTONE = registerBlock(Blocks.LODESTONE);
/* 1519 */   public static final Item CRYING_OBSIDIAN = registerBlock(Blocks.CRYING_OBSIDIAN);
/* 1520 */   public static final Item BLACKSTONE = registerBlock(Blocks.BLACKSTONE);
/* 1521 */   public static final Item BLACKSTONE_SLAB = registerBlock(Blocks.BLACKSTONE_SLAB);
/* 1522 */   public static final Item BLACKSTONE_STAIRS = registerBlock(Blocks.BLACKSTONE_STAIRS);
/* 1523 */   public static final Item GILDED_BLACKSTONE = registerBlock(Blocks.GILDED_BLACKSTONE);
/* 1524 */   public static final Item POLISHED_BLACKSTONE = registerBlock(Blocks.POLISHED_BLACKSTONE);
/* 1525 */   public static final Item POLISHED_BLACKSTONE_SLAB = registerBlock(Blocks.POLISHED_BLACKSTONE_SLAB);
/* 1526 */   public static final Item POLISHED_BLACKSTONE_STAIRS = registerBlock(Blocks.POLISHED_BLACKSTONE_STAIRS);
/* 1527 */   public static final Item CHISELED_POLISHED_BLACKSTONE = registerBlock(Blocks.CHISELED_POLISHED_BLACKSTONE);
/* 1528 */   public static final Item POLISHED_BLACKSTONE_BRICKS = registerBlock(Blocks.POLISHED_BLACKSTONE_BRICKS);
/* 1529 */   public static final Item POLISHED_BLACKSTONE_BRICK_SLAB = registerBlock(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/* 1530 */   public static final Item POLISHED_BLACKSTONE_BRICK_STAIRS = registerBlock(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/* 1531 */   public static final Item CRACKED_POLISHED_BLACKSTONE_BRICKS = registerBlock(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*      */   
/* 1533 */   public static final Item RESPAWN_ANCHOR = registerBlock(Blocks.RESPAWN_ANCHOR);
/* 1534 */   public static final Item CANDLE = registerBlock(Blocks.CANDLE);
/* 1535 */   public static final Item WHITE_CANDLE = registerBlock(Blocks.WHITE_CANDLE);
/* 1536 */   public static final Item ORANGE_CANDLE = registerBlock(Blocks.ORANGE_CANDLE);
/* 1537 */   public static final Item MAGENTA_CANDLE = registerBlock(Blocks.MAGENTA_CANDLE);
/* 1538 */   public static final Item LIGHT_BLUE_CANDLE = registerBlock(Blocks.LIGHT_BLUE_CANDLE);
/* 1539 */   public static final Item YELLOW_CANDLE = registerBlock(Blocks.YELLOW_CANDLE);
/* 1540 */   public static final Item LIME_CANDLE = registerBlock(Blocks.LIME_CANDLE);
/* 1541 */   public static final Item PINK_CANDLE = registerBlock(Blocks.PINK_CANDLE);
/* 1542 */   public static final Item GRAY_CANDLE = registerBlock(Blocks.GRAY_CANDLE);
/* 1543 */   public static final Item LIGHT_GRAY_CANDLE = registerBlock(Blocks.LIGHT_GRAY_CANDLE);
/* 1544 */   public static final Item CYAN_CANDLE = registerBlock(Blocks.CYAN_CANDLE);
/* 1545 */   public static final Item PURPLE_CANDLE = registerBlock(Blocks.PURPLE_CANDLE);
/* 1546 */   public static final Item BLUE_CANDLE = registerBlock(Blocks.BLUE_CANDLE);
/* 1547 */   public static final Item BROWN_CANDLE = registerBlock(Blocks.BROWN_CANDLE);
/* 1548 */   public static final Item GREEN_CANDLE = registerBlock(Blocks.GREEN_CANDLE);
/* 1549 */   public static final Item RED_CANDLE = registerBlock(Blocks.RED_CANDLE);
/* 1550 */   public static final Item BLACK_CANDLE = registerBlock(Blocks.BLACK_CANDLE);
/* 1551 */   public static final Item SMALL_AMETHYST_BUD = registerBlock(Blocks.SMALL_AMETHYST_BUD);
/* 1552 */   public static final Item MEDIUM_AMETHYST_BUD = registerBlock(Blocks.MEDIUM_AMETHYST_BUD);
/* 1553 */   public static final Item LARGE_AMETHYST_BUD = registerBlock(Blocks.LARGE_AMETHYST_BUD);
/* 1554 */   public static final Item AMETHYST_CLUSTER = registerBlock(Blocks.AMETHYST_CLUSTER);
/* 1555 */   public static final Item POINTED_DRIPSTONE = registerBlock(Blocks.POINTED_DRIPSTONE);
/* 1556 */   public static final Item OCHRE_FROGLIGHT = registerBlock(Blocks.OCHRE_FROGLIGHT);
/* 1557 */   public static final Item VERDANT_FROGLIGHT = registerBlock(Blocks.VERDANT_FROGLIGHT);
/* 1558 */   public static final Item PEARLESCENT_FROGLIGHT = registerBlock(Blocks.PEARLESCENT_FROGLIGHT);
/* 1559 */   public static final Item FROGSPAWN = registerBlock(Blocks.FROGSPAWN, PlaceOnWaterBlockItem::new);
/* 1560 */   public static final Item ECHO_SHARD = registerItem("echo_shard", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1561 */   public static final Item BRUSH = registerItem("brush", BrushItem::new, (new Item.Properties()).durability(64));
/*      */   
/* 1563 */   public static final Item NETHERITE_UPGRADE_SMITHING_TEMPLATE = registerItem("netherite_upgrade_smithing_template", SmithingTemplateItem::createNetheriteUpgradeTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1564 */   public static final Item SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("sentry_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1565 */   public static final Item DUNE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("dune_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1566 */   public static final Item COAST_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("coast_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1567 */   public static final Item WILD_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("wild_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1568 */   public static final Item WARD_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("ward_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.RARE));
/* 1569 */   public static final Item EYE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("eye_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.RARE));
/* 1570 */   public static final Item VEX_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("vex_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.RARE));
/* 1571 */   public static final Item TIDE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("tide_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1572 */   public static final Item SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("snout_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1573 */   public static final Item RIB_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("rib_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1574 */   public static final Item SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("spire_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.RARE));
/* 1575 */   public static final Item WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("wayfinder_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1576 */   public static final Item SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("shaper_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1577 */   public static final Item SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("silence_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.EPIC));
/* 1578 */   public static final Item RAISER_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("raiser_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1579 */   public static final Item HOST_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("host_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1580 */   public static final Item FLOW_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("flow_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1581 */   public static final Item BOLT_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("bolt_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, (new Item.Properties()).rarity(Rarity.UNCOMMON));
/*      */ 
/*      */   
/* 1584 */   public static final Item ANGLER_POTTERY_SHERD = registerItem("angler_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1585 */   public static final Item ARCHER_POTTERY_SHERD = registerItem("archer_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1586 */   public static final Item ARMS_UP_POTTERY_SHERD = registerItem("arms_up_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1587 */   public static final Item BLADE_POTTERY_SHERD = registerItem("blade_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1588 */   public static final Item BREWER_POTTERY_SHERD = registerItem("brewer_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1589 */   public static final Item BURN_POTTERY_SHERD = registerItem("burn_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1590 */   public static final Item DANGER_POTTERY_SHERD = registerItem("danger_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1591 */   public static final Item EXPLORER_POTTERY_SHERD = registerItem("explorer_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1592 */   public static final Item FLOW_POTTERY_SHERD = registerItem("flow_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1593 */   public static final Item FRIEND_POTTERY_SHERD = registerItem("friend_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1594 */   public static final Item GUSTER_POTTERY_SHERD = registerItem("guster_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1595 */   public static final Item HEART_POTTERY_SHERD = registerItem("heart_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1596 */   public static final Item HEARTBREAK_POTTERY_SHERD = registerItem("heartbreak_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1597 */   public static final Item HOWL_POTTERY_SHERD = registerItem("howl_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1598 */   public static final Item MINER_POTTERY_SHERD = registerItem("miner_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1599 */   public static final Item MOURNER_POTTERY_SHERD = registerItem("mourner_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1600 */   public static final Item PLENTY_POTTERY_SHERD = registerItem("plenty_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1601 */   public static final Item PRIZE_POTTERY_SHERD = registerItem("prize_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1602 */   public static final Item SCRAPE_POTTERY_SHERD = registerItem("scrape_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1603 */   public static final Item SHEAF_POTTERY_SHERD = registerItem("sheaf_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1604 */   public static final Item SHELTER_POTTERY_SHERD = registerItem("shelter_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1605 */   public static final Item SKULL_POTTERY_SHERD = registerItem("skull_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1606 */   public static final Item SNORT_POTTERY_SHERD = registerItem("snort_pottery_sherd", (new Item.Properties()).rarity(Rarity.UNCOMMON));
/* 1607 */   public static final Item COPPER_GRATE = registerBlock(Blocks.COPPER_GRATE);
/* 1608 */   public static final Item EXPOSED_COPPER_GRATE = registerBlock(Blocks.EXPOSED_COPPER_GRATE);
/* 1609 */   public static final Item WEATHERED_COPPER_GRATE = registerBlock(Blocks.WEATHERED_COPPER_GRATE);
/* 1610 */   public static final Item OXIDIZED_COPPER_GRATE = registerBlock(Blocks.OXIDIZED_COPPER_GRATE);
/* 1611 */   public static final Item WAXED_COPPER_GRATE = registerBlock(Blocks.WAXED_COPPER_GRATE);
/* 1612 */   public static final Item WAXED_EXPOSED_COPPER_GRATE = registerBlock(Blocks.WAXED_EXPOSED_COPPER_GRATE);
/* 1613 */   public static final Item WAXED_WEATHERED_COPPER_GRATE = registerBlock(Blocks.WAXED_WEATHERED_COPPER_GRATE);
/* 1614 */   public static final Item WAXED_OXIDIZED_COPPER_GRATE = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_GRATE);
/*      */   
/* 1616 */   public static final Item COPPER_BULB = registerBlock(Blocks.COPPER_BULB);
/* 1617 */   public static final Item EXPOSED_COPPER_BULB = registerBlock(Blocks.EXPOSED_COPPER_BULB);
/* 1618 */   public static final Item WEATHERED_COPPER_BULB = registerBlock(Blocks.WEATHERED_COPPER_BULB);
/* 1619 */   public static final Item OXIDIZED_COPPER_BULB = registerBlock(Blocks.OXIDIZED_COPPER_BULB);
/* 1620 */   public static final Item WAXED_COPPER_BULB = registerBlock(Blocks.WAXED_COPPER_BULB);
/* 1621 */   public static final Item WAXED_EXPOSED_COPPER_BULB = registerBlock(Blocks.WAXED_EXPOSED_COPPER_BULB);
/* 1622 */   public static final Item WAXED_WEATHERED_COPPER_BULB = registerBlock(Blocks.WAXED_WEATHERED_COPPER_BULB);
/* 1623 */   public static final Item WAXED_OXIDIZED_COPPER_BULB = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_BULB);
/*      */   
/* 1625 */   public static final Item COPPER_CHEST = registerBlock(Blocks.COPPER_CHEST);
/* 1626 */   public static final Item EXPOSED_COPPER_CHEST = registerBlock(Blocks.EXPOSED_COPPER_CHEST);
/* 1627 */   public static final Item WEATHERED_COPPER_CHEST = registerBlock(Blocks.WEATHERED_COPPER_CHEST);
/* 1628 */   public static final Item OXIDIZED_COPPER_CHEST = registerBlock(Blocks.OXIDIZED_COPPER_CHEST);
/* 1629 */   public static final Item WAXED_COPPER_CHEST = registerBlock(Blocks.WAXED_COPPER_CHEST);
/* 1630 */   public static final Item WAXED_EXPOSED_COPPER_CHEST = registerBlock(Blocks.WAXED_EXPOSED_COPPER_CHEST);
/* 1631 */   public static final Item WAXED_WEATHERED_COPPER_CHEST = registerBlock(Blocks.WAXED_WEATHERED_COPPER_CHEST);
/* 1632 */   public static final Item WAXED_OXIDIZED_COPPER_CHEST = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_CHEST);
/*      */   
/* 1634 */   public static final Item COPPER_GOLEM_STATUE = registerBlock(Blocks.COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1635 */   public static final Item EXPOSED_COPPER_GOLEM_STATUE = registerBlock(Blocks.EXPOSED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1636 */   public static final Item WEATHERED_COPPER_GOLEM_STATUE = registerBlock(Blocks.WEATHERED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1637 */   public static final Item OXIDIZED_COPPER_GOLEM_STATUE = registerBlock(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1638 */   public static final Item WAXED_COPPER_GOLEM_STATUE = registerBlock(Blocks.WAXED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1639 */   public static final Item WAXED_EXPOSED_COPPER_GOLEM_STATUE = registerBlock(Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1640 */   public static final Item WAXED_WEATHERED_COPPER_GOLEM_STATUE = registerBlock(Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/* 1641 */   public static final Item WAXED_OXIDIZED_COPPER_GOLEM_STATUE = registerBlock(Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, (new Item.Properties()).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.STANDING)));
/*      */   
/* 1643 */   public static final Item TRIAL_SPAWNER = registerBlock(Blocks.TRIAL_SPAWNER);
/* 1644 */   public static final Item TRIAL_KEY = registerItem("trial_key");
/* 1645 */   public static final Item OMINOUS_TRIAL_KEY = registerItem("ominous_trial_key");
/* 1646 */   public static final Item VAULT = registerBlock(Blocks.VAULT);
/* 1647 */   public static final Item OMINOUS_BOTTLE = registerItem("ominous_bottle", (new Item.Properties()).rarity(Rarity.UNCOMMON).<Consumable>component(DataComponents.CONSUMABLE, Consumables.OMINOUS_BOTTLE).component(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(0)));
/*      */   
/*      */   private static Function<Item.Properties, Item> createBlockItemWithCustomItemName(Block paramBlock) {
/* 1650 */     return paramProperties -> new BlockItem(paramBlock, paramProperties.useItemDescriptionPrefix());
/*      */   }
/*      */   
/*      */   private static ResourceKey<Item> vanillaItemId(String paramString) {
/* 1654 */     return ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(paramString));
/*      */   }
/*      */   
/*      */   private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> paramResourceKey) {
/* 1658 */     return ResourceKey.create(Registries.ITEM, paramResourceKey.identifier());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Item registerSpawnEgg(EntityType<?> paramEntityType) {
/* 1667 */     return registerItem(ResourceKey.create(Registries.ITEM, EntityType.getKey(paramEntityType).withSuffix("_spawn_egg")), SpawnEggItem::new, (new Item.Properties()).spawnEgg(paramEntityType));
/*      */   }
/*      */   
/*      */   public static Item registerBlock(Block paramBlock) {
/* 1671 */     return registerBlock(paramBlock, BlockItem::new);
/*      */   }
/*      */   
/*      */   public static Item registerBlock(Block paramBlock, Item.Properties paramProperties) {
/* 1675 */     return registerBlock(paramBlock, BlockItem::new, paramProperties);
/*      */   }
/*      */   
/*      */   public static Item registerBlock(Block paramBlock, UnaryOperator<Item.Properties> paramUnaryOperator) {
/* 1679 */     return registerBlock(paramBlock, (paramBlock, paramProperties) -> new BlockItem(paramBlock, paramUnaryOperator.apply(paramProperties)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Item registerBlock(Block paramBlock, Block... paramVarArgs) {
/* 1684 */     Item item = registerBlock(paramBlock);
/*      */     
/* 1686 */     for (Block block : paramVarArgs) {
/* 1687 */       Item.BY_BLOCK.put(block, item);
/*      */     }
/*      */     
/* 1690 */     return item;
/*      */   }
/*      */   
/*      */   public static Item registerBlock(Block paramBlock, BiFunction<Block, Item.Properties, Item> paramBiFunction) {
/* 1694 */     return registerBlock(paramBlock, paramBiFunction, new Item.Properties());
/*      */   }
/*      */   
/*      */   public static Item registerBlock(Block paramBlock, BiFunction<Block, Item.Properties, Item> paramBiFunction, Item.Properties paramProperties) {
/* 1698 */     return registerItem(blockIdToItemId(paramBlock.builtInRegistryHolder().key()), paramProperties -> (Item)paramBiFunction.apply(paramBlock, paramProperties), paramProperties.useBlockDescriptionPrefix());
/*      */   }
/*      */   
/*      */   public static Item registerItem(String paramString, Function<Item.Properties, Item> paramFunction) {
/* 1702 */     return registerItem(vanillaItemId(paramString), paramFunction, new Item.Properties());
/*      */   }
/*      */   
/*      */   public static Item registerItem(String paramString, Function<Item.Properties, Item> paramFunction, Item.Properties paramProperties) {
/* 1706 */     return registerItem(vanillaItemId(paramString), paramFunction, paramProperties);
/*      */   }
/*      */   
/*      */   public static Item registerItem(String paramString, Item.Properties paramProperties) {
/* 1710 */     return registerItem(vanillaItemId(paramString), Item::new, paramProperties);
/*      */   }
/*      */   
/*      */   public static Item registerItem(String paramString) {
/* 1714 */     return registerItem(vanillaItemId(paramString), Item::new, new Item.Properties());
/*      */   }
/*      */   
/*      */   public static Item registerItem(ResourceKey<Item> paramResourceKey, Function<Item.Properties, Item> paramFunction) {
/* 1718 */     return registerItem(paramResourceKey, paramFunction, new Item.Properties());
/*      */   }
/*      */   
/*      */   public static Item registerItem(ResourceKey<Item> paramResourceKey, Function<Item.Properties, Item> paramFunction, Item.Properties paramProperties) {
/* 1722 */     Item item = paramFunction.apply(paramProperties.setId(paramResourceKey));
/* 1723 */     if (item instanceof BlockItem) { BlockItem blockItem = (BlockItem)item;
/* 1724 */       blockItem.registerBlocks(Item.BY_BLOCK, item); }
/*      */     
/* 1726 */     return (Item)Registry.register((Registry)BuiltInRegistries.ITEM, paramResourceKey, item);
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\Items.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */