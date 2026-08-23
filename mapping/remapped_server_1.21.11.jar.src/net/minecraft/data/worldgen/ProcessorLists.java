/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CopperBulbBlock;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AxisAlignedLinearPosTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class ProcessorLists {
/*  38 */   private static final ResourceKey<StructureProcessorList> EMPTY = createKey("empty");
/*     */   
/*  40 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_PLAINS = createKey("zombie_plains");
/*  41 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_SAVANNA = createKey("zombie_savanna");
/*  42 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_SNOWY = createKey("zombie_snowy");
/*  43 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_TAIGA = createKey("zombie_taiga");
/*  44 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_DESERT = createKey("zombie_desert");
/*  45 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_10_PERCENT = createKey("mossify_10_percent");
/*  46 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_20_PERCENT = createKey("mossify_20_percent");
/*  47 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_70_PERCENT = createKey("mossify_70_percent");
/*  48 */   public static final ResourceKey<StructureProcessorList> STREET_PLAINS = createKey("street_plains");
/*  49 */   public static final ResourceKey<StructureProcessorList> STREET_SAVANNA = createKey("street_savanna");
/*  50 */   public static final ResourceKey<StructureProcessorList> STREET_SNOWY_OR_TAIGA = createKey("street_snowy_or_taiga");
/*  51 */   public static final ResourceKey<StructureProcessorList> FARM_PLAINS = createKey("farm_plains");
/*  52 */   public static final ResourceKey<StructureProcessorList> FARM_SAVANNA = createKey("farm_savanna");
/*  53 */   public static final ResourceKey<StructureProcessorList> FARM_SNOWY = createKey("farm_snowy");
/*  54 */   public static final ResourceKey<StructureProcessorList> FARM_TAIGA = createKey("farm_taiga");
/*  55 */   public static final ResourceKey<StructureProcessorList> FARM_DESERT = createKey("farm_desert");
/*  56 */   public static final ResourceKey<StructureProcessorList> OUTPOST_ROT = createKey("outpost_rot");
/*  57 */   public static final ResourceKey<StructureProcessorList> BOTTOM_RAMPART = createKey("bottom_rampart");
/*  58 */   public static final ResourceKey<StructureProcessorList> TREASURE_ROOMS = createKey("treasure_rooms");
/*  59 */   public static final ResourceKey<StructureProcessorList> HOUSING = createKey("housing");
/*  60 */   public static final ResourceKey<StructureProcessorList> SIDE_WALL_DEGRADATION = createKey("side_wall_degradation");
/*  61 */   public static final ResourceKey<StructureProcessorList> STABLE_DEGRADATION = createKey("stable_degradation");
/*  62 */   public static final ResourceKey<StructureProcessorList> BASTION_GENERIC_DEGRADATION = createKey("bastion_generic_degradation");
/*  63 */   public static final ResourceKey<StructureProcessorList> RAMPART_DEGRADATION = createKey("rampart_degradation");
/*  64 */   public static final ResourceKey<StructureProcessorList> ENTRANCE_REPLACEMENT = createKey("entrance_replacement");
/*  65 */   public static final ResourceKey<StructureProcessorList> BRIDGE = createKey("bridge");
/*  66 */   public static final ResourceKey<StructureProcessorList> ROOF = createKey("roof");
/*  67 */   public static final ResourceKey<StructureProcessorList> HIGH_WALL = createKey("high_wall");
/*  68 */   public static final ResourceKey<StructureProcessorList> HIGH_RAMPART = createKey("high_rampart");
/*  69 */   public static final ResourceKey<StructureProcessorList> FOSSIL_ROT = createKey("fossil_rot");
/*  70 */   public static final ResourceKey<StructureProcessorList> FOSSIL_COAL = createKey("fossil_coal");
/*  71 */   public static final ResourceKey<StructureProcessorList> FOSSIL_DIAMONDS = createKey("fossil_diamonds");
/*  72 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_START_DEGRADATION = createKey("ancient_city_start_degradation");
/*  73 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_GENERIC_DEGRADATION = createKey("ancient_city_generic_degradation");
/*  74 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_WALLS_DEGRADATION = createKey("ancient_city_walls_degradation");
/*  75 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_HOUSES_ARCHAEOLOGY = createKey("trail_ruins_houses_archaeology");
/*  76 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_ROADS_ARCHAEOLOGY = createKey("trail_ruins_roads_archaeology");
/*  77 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_TOWER_TOP_ARCHAEOLOGY = createKey("trail_ruins_tower_top_archaeology");
/*  78 */   public static final ResourceKey<StructureProcessorList> TRIAL_CHAMBERS_COPPER_BULB_DEGRADATION = createKey("trial_chambers_copper_bulb_degradation");
/*     */   
/*     */   private static ResourceKey<StructureProcessorList> createKey(String paramString) {
/*  81 */     return ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<StructureProcessorList> paramBootstrapContext, ResourceKey<StructureProcessorList> paramResourceKey, List<StructureProcessor> paramList) {
/*  85 */     paramBootstrapContext.register(paramResourceKey, new StructureProcessorList(paramList));
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureProcessorList> paramBootstrapContext) {
/*  89 */     HolderGetter<?> holderGetter = paramBootstrapContext.lookup(Registries.BLOCK);
/*     */     
/*  91 */     ProcessorRule processorRule1 = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState());
/*  92 */     ProcessorRule processorRule2 = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GILDED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE.defaultBlockState());
/*     */     
/*  94 */     register(paramBootstrapContext, EMPTY, (List<StructureProcessor>)ImmutableList.of());
/*     */     
/*  96 */     register(paramBootstrapContext, ZOMBIE_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/*  97 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  98 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  99 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 100 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 101 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MOSSY_COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 102 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHITE_TERRACOTTA, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 103 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 104 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_PLANKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 105 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_STAIRS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 106 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_OAK_LOG, 0.02F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 107 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 108 */                 .defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 109 */                     .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 110 */                     .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 111 */                   .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 112 */                   .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 113 */                   .defaultBlockState()) }))));
/*     */ 
/*     */     
/* 116 */     register(paramBootstrapContext, ZOMBIE_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 117 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 118 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 119 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 120 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_STAIRS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 121 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 122 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 123 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ORANGE_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 124 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.YELLOW_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 125 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.RED_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 126 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 127 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 128 */                   .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 129 */                     .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 130 */                   .defaultBlockState()) }))));
/*     */ 
/*     */     
/* 133 */     register(paramBootstrapContext, ZOMBIE_SNOWY, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 134 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 135 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 136 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.LANTERN), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 137 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 138 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_SLAB, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 139 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 140 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 141 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 142 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 143 */                   .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 144 */                   .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 145 */                 .defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 146 */                   .defaultBlockState()) }))));
/*     */ 
/*     */     
/* 149 */     register(paramBootstrapContext, ZOMBIE_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 150 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 151 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 152 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 153 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.CAMPFIRE), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)Blocks.CAMPFIRE
/* 154 */                 .defaultBlockState().setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 155 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_LOG, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 156 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 157 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 158 */                   .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/* 159 */                   .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PUMPKIN_STEM
/* 160 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 161 */                 .defaultBlockState()), (Object[])new ProcessorRule[0]))));
/*     */ 
/*     */     
/* 164 */     register(paramBootstrapContext, ZOMBIE_DESERT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 165 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 166 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 167 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 168 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CUT_SANDSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 169 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.TERRACOTTA, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 170 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_STAIRS, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 171 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_SLAB, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 172 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 173 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 174 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 177 */     register(paramBootstrapContext, MOSSIFY_10_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 178 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 181 */     register(paramBootstrapContext, MOSSIFY_20_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 182 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 185 */     register(paramBootstrapContext, MOSSIFY_70_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.7F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 186 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 189 */     register(paramBootstrapContext, STREET_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.OAK_PLANKS
/* 190 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 191 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 192 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 193 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 196 */     register(paramBootstrapContext, STREET_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.ACACIA_PLANKS
/* 197 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 198 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 199 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 200 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 203 */     register(paramBootstrapContext, STREET_SNOWY_OR_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.SPRUCE_PLANKS
/* 204 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.ICE), Blocks.SPRUCE_PLANKS
/* 205 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 206 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 207 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 208 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 211 */     register(paramBootstrapContext, FARM_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 212 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 213 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 214 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 217 */     register(paramBootstrapContext, FARM_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 218 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 221 */     register(paramBootstrapContext, FARM_SNOWY, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 222 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 223 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 226 */     register(paramBootstrapContext, FARM_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PUMPKIN_STEM
/* 227 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 228 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 231 */     register(paramBootstrapContext, FARM_DESERT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 232 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 233 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 236 */     register(paramBootstrapContext, OUTPOST_ROT, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.05F)));
/*     */     
/* 238 */     register(paramBootstrapContext, BOTTOM_RAMPART, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MAGMA_BLOCK, 0.75F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 239 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POLISHED_BLACKSTONE_BRICKS
/* 240 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     register(paramBootstrapContext, TREASURE_ROOMS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.35F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 246 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 247 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     register(paramBootstrapContext, HOUSING, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 253 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 254 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     register(paramBootstrapContext, SIDE_WALL_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 260 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 261 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     register(paramBootstrapContext, STABLE_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 267 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 268 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     register(paramBootstrapContext, BASTION_GENERIC_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 274 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 275 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 276 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     register(paramBootstrapContext, RAMPART_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 282 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 283 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 284 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 285 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 286 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     register(paramBootstrapContext, ENTRANCE_REPLACEMENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 292 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.6F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 293 */                 .defaultBlockState()), processorRule2, processorRule1))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     register(paramBootstrapContext, BRIDGE, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 299 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 300 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 303 */     register(paramBootstrapContext, ROOF, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 304 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 305 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE
/* 306 */                 .defaultBlockState())))));
/*     */ 
/*     */     
/* 309 */     register(paramBootstrapContext, HIGH_WALL, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 310 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 311 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE
/* 312 */                 .defaultBlockState()), processorRule2))));
/*     */ 
/*     */ 
/*     */     
/* 316 */     register(paramBootstrapContext, HIGH_RAMPART, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 317 */                 .defaultBlockState()), new ProcessorRule((RuleTest)AlwaysTrueTest.INSTANCE, (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)new AxisAlignedLinearPosTest(0.0F, 0.05F, 0, 100, Direction.Axis.Y), Blocks.AIR
/* 318 */                 .defaultBlockState()), processorRule2))));
/*     */ 
/*     */ 
/*     */     
/* 322 */     register(paramBootstrapContext, FOSSIL_ROT, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.9F), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     register(paramBootstrapContext, FOSSIL_COAL, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.1F), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     register(paramBootstrapContext, FOSSIL_DIAMONDS, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.1F), new RuleProcessor(
/*     */             
/* 334 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.COAL_ORE), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE_DIAMOND_ORE
/* 335 */                 .defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     register(paramBootstrapContext, ANCIENT_CITY_START_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor(
/* 341 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_BRICKS
/* 342 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_TILES
/* 343 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 344 */                 .defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     register(paramBootstrapContext, ANCIENT_CITY_GENERIC_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor((HolderSet)holderGetter
/* 350 */             .getOrThrow(BlockTags.ANCIENT_CITY_REPLACEABLE), 0.95F), new RuleProcessor(
/* 351 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_BRICKS
/* 352 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_TILES
/* 353 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 354 */                 .defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     register(paramBootstrapContext, ANCIENT_CITY_WALLS_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor((HolderSet)holderGetter
/* 360 */             .getOrThrow(BlockTags.ANCIENT_CITY_REPLACEABLE), 0.95F), new RuleProcessor(
/* 361 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_BRICKS
/* 362 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_TILES
/* 363 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILE_SLAB, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 364 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 365 */                 .defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     register(paramBootstrapContext, TRAIL_RUINS_HOUSES_ARCHAEOLOGY, (List)List.of(new RuleProcessor(
/* 371 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.DIRT
/* 372 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COARSE_DIRT
/* 373 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MUD_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PACKED_MUD
/* 374 */                 .defaultBlockState()))), 
/*     */           
/* 376 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 6), 
/* 377 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 3)));
/*     */ 
/*     */     
/* 380 */     register(paramBootstrapContext, TRAIL_RUINS_ROADS_ARCHAEOLOGY, (List)List.of(new RuleProcessor(
/* 381 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.DIRT
/* 382 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COARSE_DIRT
/* 383 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MUD_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PACKED_MUD
/* 384 */                 .defaultBlockState()))), 
/*     */           
/* 386 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 2)));
/*     */ 
/*     */     
/* 389 */     register(paramBootstrapContext, TRAIL_RUINS_TOWER_TOP_ARCHAEOLOGY, (List)List.of(
/* 390 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 2)));
/*     */ 
/*     */     
/* 393 */     register(paramBootstrapContext, TRIAL_CHAMBERS_COPPER_BULB_DEGRADATION, (List)List.of(new RuleProcessor(
/* 394 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)Blocks.WAXED_OXIDIZED_COPPER_BULB
/* 395 */                 .defaultBlockState().setValue((Property)CopperBulbBlock.LIT, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.33333334F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)Blocks.WAXED_WEATHERED_COPPER_BULB
/* 396 */                 .defaultBlockState().setValue((Property)CopperBulbBlock.LIT, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)Blocks.WAXED_EXPOSED_COPPER_BULB
/* 397 */                 .defaultBlockState().setValue((Property)CopperBulbBlock.LIT, Boolean.valueOf(true))))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CappedProcessor trailsArchyLootProcessor(ResourceKey<LootTable> paramResourceKey, int paramInt) {
/* 404 */     return new CappedProcessor((StructureProcessor)new RuleProcessor(
/* 405 */           List.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.TRAIL_RUINS_REPLACEABLE), (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)PosAlwaysTrueTest.INSTANCE, Blocks.SUSPICIOUS_GRAVEL
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 410 */               .defaultBlockState(), (RuleBlockEntityModifier)new AppendLoot(paramResourceKey)))), 
/*     */ 
/*     */ 
/*     */         
/* 414 */         (IntProvider)ConstantInt.of(paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\ProcessorLists.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */