/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class PoiTypes {
/*  25 */   public static final ResourceKey<PoiType> ARMORER = createKey("armorer");
/*  26 */   public static final ResourceKey<PoiType> BUTCHER = createKey("butcher");
/*  27 */   public static final ResourceKey<PoiType> CARTOGRAPHER = createKey("cartographer");
/*  28 */   public static final ResourceKey<PoiType> CLERIC = createKey("cleric");
/*  29 */   public static final ResourceKey<PoiType> FARMER = createKey("farmer");
/*  30 */   public static final ResourceKey<PoiType> FISHERMAN = createKey("fisherman");
/*  31 */   public static final ResourceKey<PoiType> FLETCHER = createKey("fletcher");
/*  32 */   public static final ResourceKey<PoiType> LEATHERWORKER = createKey("leatherworker");
/*  33 */   public static final ResourceKey<PoiType> LIBRARIAN = createKey("librarian");
/*  34 */   public static final ResourceKey<PoiType> MASON = createKey("mason");
/*  35 */   public static final ResourceKey<PoiType> SHEPHERD = createKey("shepherd");
/*  36 */   public static final ResourceKey<PoiType> TOOLSMITH = createKey("toolsmith");
/*  37 */   public static final ResourceKey<PoiType> WEAPONSMITH = createKey("weaponsmith");
/*  38 */   public static final ResourceKey<PoiType> HOME = createKey("home");
/*  39 */   public static final ResourceKey<PoiType> MEETING = createKey("meeting");
/*  40 */   public static final ResourceKey<PoiType> BEEHIVE = createKey("beehive");
/*  41 */   public static final ResourceKey<PoiType> BEE_NEST = createKey("bee_nest");
/*  42 */   public static final ResourceKey<PoiType> NETHER_PORTAL = createKey("nether_portal");
/*  43 */   public static final ResourceKey<PoiType> LODESTONE = createKey("lodestone");
/*  44 */   public static final ResourceKey<PoiType> LIGHTNING_ROD = createKey("lightning_rod");
/*  45 */   public static final ResourceKey<PoiType> TEST_INSTANCE = createKey("test_instance");
/*     */   
/*     */   private static final Set<BlockState> BEDS;
/*     */   private static final Set<BlockState> CAULDRONS;
/*     */   private static final Set<BlockState> LIGHTNING_RODS;
/*     */   
/*     */   static {
/*  52 */     BEDS = (Set<BlockState>)ImmutableList.of(Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, (Object[])new Block[] { Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }).stream().flatMap(paramBlock -> paramBlock.getStateDefinition().getPossibleStates().stream()).filter(paramBlockState -> (paramBlockState.getValue((Property)BedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     CAULDRONS = (Set<BlockState>)ImmutableList.of(Blocks.CAULDRON, Blocks.LAVA_CAULDRON, Blocks.WATER_CAULDRON, Blocks.POWDER_SNOW_CAULDRON).stream().flatMap(paramBlock -> paramBlock.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     LIGHTNING_RODS = (Set<BlockState>)ImmutableList.of(Blocks.LIGHTNING_ROD, Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WEATHERED_LIGHTNING_ROD, Blocks.OXIDIZED_LIGHTNING_ROD, Blocks.WAXED_LIGHTNING_ROD, Blocks.WAXED_EXPOSED_LIGHTNING_ROD, Blocks.WAXED_WEATHERED_LIGHTNING_ROD, Blocks.WAXED_OXIDIZED_LIGHTNING_ROD).stream().flatMap(paramBlock -> paramBlock.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*  66 */   private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE = Maps.newHashMap();
/*     */   
/*     */   private static Set<BlockState> getBlockStates(Block paramBlock) {
/*  69 */     return (Set<BlockState>)ImmutableSet.copyOf((Collection)paramBlock.getStateDefinition().getPossibleStates());
/*     */   }
/*     */   
/*     */   private static ResourceKey<PoiType> createKey(String paramString) {
/*  73 */     return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   private static PoiType register(Registry<PoiType> paramRegistry, ResourceKey<PoiType> paramResourceKey, Set<BlockState> paramSet, int paramInt1, int paramInt2) {
/*  77 */     PoiType poiType = new PoiType(paramSet, paramInt1, paramInt2);
/*  78 */     Registry.register(paramRegistry, paramResourceKey, poiType);
/*  79 */     registerBlockStates((Holder<PoiType>)paramRegistry.getOrThrow(paramResourceKey), paramSet);
/*  80 */     return poiType;
/*     */   }
/*     */   
/*     */   private static void registerBlockStates(Holder<PoiType> paramHolder, Set<BlockState> paramSet) {
/*  84 */     paramSet.forEach(paramBlockState -> {
/*     */           Holder holder = TYPE_BY_STATE.put(paramBlockState, paramHolder);
/*     */           if (holder != null) {
/*     */             throw (IllegalStateException)Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", new Object[] { paramBlockState })));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Optional<Holder<PoiType>> forState(BlockState paramBlockState) {
/*  93 */     return Optional.ofNullable(TYPE_BY_STATE.get(paramBlockState));
/*     */   }
/*     */   
/*     */   public static boolean hasPoi(BlockState paramBlockState) {
/*  97 */     return TYPE_BY_STATE.containsKey(paramBlockState);
/*     */   }
/*     */   
/*     */   public static PoiType bootstrap(Registry<PoiType> paramRegistry) {
/* 101 */     register(paramRegistry, ARMORER, getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
/* 102 */     register(paramRegistry, BUTCHER, getBlockStates(Blocks.SMOKER), 1, 1);
/* 103 */     register(paramRegistry, CARTOGRAPHER, getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
/* 104 */     register(paramRegistry, CLERIC, getBlockStates(Blocks.BREWING_STAND), 1, 1);
/* 105 */     register(paramRegistry, FARMER, getBlockStates(Blocks.COMPOSTER), 1, 1);
/* 106 */     register(paramRegistry, FISHERMAN, getBlockStates(Blocks.BARREL), 1, 1);
/* 107 */     register(paramRegistry, FLETCHER, getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
/* 108 */     register(paramRegistry, LEATHERWORKER, CAULDRONS, 1, 1);
/* 109 */     register(paramRegistry, LIBRARIAN, getBlockStates(Blocks.LECTERN), 1, 1);
/* 110 */     register(paramRegistry, MASON, getBlockStates(Blocks.STONECUTTER), 1, 1);
/* 111 */     register(paramRegistry, SHEPHERD, getBlockStates(Blocks.LOOM), 1, 1);
/* 112 */     register(paramRegistry, TOOLSMITH, getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
/* 113 */     register(paramRegistry, WEAPONSMITH, getBlockStates(Blocks.GRINDSTONE), 1, 1);
/* 114 */     register(paramRegistry, HOME, BEDS, 1, 1);
/* 115 */     register(paramRegistry, MEETING, getBlockStates(Blocks.BELL), 32, 6);
/* 116 */     register(paramRegistry, BEEHIVE, getBlockStates(Blocks.BEEHIVE), 0, 1);
/* 117 */     register(paramRegistry, BEE_NEST, getBlockStates(Blocks.BEE_NEST), 0, 1);
/* 118 */     register(paramRegistry, NETHER_PORTAL, getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
/* 119 */     register(paramRegistry, LODESTONE, getBlockStates(Blocks.LODESTONE), 0, 1);
/* 120 */     register(paramRegistry, TEST_INSTANCE, getBlockStates(Blocks.TEST_INSTANCE_BLOCK), 0, 1);
/* 121 */     return register(paramRegistry, LIGHTNING_ROD, LIGHTNING_RODS, 0, 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\village\poi\PoiTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */