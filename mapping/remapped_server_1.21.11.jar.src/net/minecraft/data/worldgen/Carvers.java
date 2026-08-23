/*    */ package net.minecraft.data.worldgen;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.valueproviders.ConstantFloat;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.UniformFloat;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
/*    */ import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class Carvers {
/* 22 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = createKey("cave");
/* 23 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE_EXTRA_UNDERGROUND = createKey("cave_extra_underground");
/* 24 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CANYON = createKey("canyon");
/* 25 */   public static final ResourceKey<ConfiguredWorldCarver<?>> NETHER_CAVE = createKey("nether_cave");
/*    */   
/*    */   private static ResourceKey<ConfiguredWorldCarver<?>> createKey(String paramString) {
/* 28 */     return ResourceKey.create(Registries.CONFIGURED_CARVER, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> paramBootstrapContext) {
/* 32 */     HolderGetter<?> holderGetter = paramBootstrapContext.lookup(Registries.BLOCK);
/*    */     
/* 34 */     paramBootstrapContext.register(CAVE, WorldCarver.CAVE.configured((CarverConfiguration)new CaveCarverConfiguration(0.15F, 
/*    */             
/* 36 */             (HeightProvider)UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)), 
/* 37 */             (FloatProvider)UniformFloat.of(0.1F, 0.9F), 
/* 38 */             VerticalAnchor.aboveBottom(8), 
/* 39 */             CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()), (HolderSet)holderGetter
/* 40 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 41 */             (FloatProvider)UniformFloat.of(0.7F, 1.4F), 
/* 42 */             (FloatProvider)UniformFloat.of(0.8F, 1.3F), 
/* 43 */             (FloatProvider)UniformFloat.of(-1.0F, -0.4F))));
/*    */ 
/*    */     
/* 46 */     paramBootstrapContext.register(CAVE_EXTRA_UNDERGROUND, WorldCarver.CAVE.configured((CarverConfiguration)new CaveCarverConfiguration(0.07F, 
/*    */             
/* 48 */             (HeightProvider)UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(47)), 
/* 49 */             (FloatProvider)UniformFloat.of(0.1F, 0.9F), 
/* 50 */             VerticalAnchor.aboveBottom(8), 
/* 51 */             CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()), (HolderSet)holderGetter
/* 52 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 53 */             (FloatProvider)UniformFloat.of(0.7F, 1.4F), 
/* 54 */             (FloatProvider)UniformFloat.of(0.8F, 1.3F), 
/* 55 */             (FloatProvider)UniformFloat.of(-1.0F, -0.4F))));
/*    */ 
/*    */     
/* 58 */     paramBootstrapContext.register(CANYON, WorldCarver.CANYON.configured((CarverConfiguration)new CanyonCarverConfiguration(0.01F, 
/*    */             
/* 60 */             (HeightProvider)UniformHeight.of(VerticalAnchor.absolute(10), VerticalAnchor.absolute(67)), 
/* 61 */             (FloatProvider)ConstantFloat.of(3.0F), 
/* 62 */             VerticalAnchor.aboveBottom(8), 
/* 63 */             CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), (HolderSet)holderGetter
/* 64 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 65 */             (FloatProvider)UniformFloat.of(-0.125F, 0.125F), new CanyonCarverConfiguration.CanyonShapeConfiguration(
/*    */               
/* 67 */               (FloatProvider)UniformFloat.of(0.75F, 1.0F), 
/* 68 */               (FloatProvider)TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, 
/*    */               
/* 70 */               (FloatProvider)UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     paramBootstrapContext.register(NETHER_CAVE, WorldCarver.NETHER_CAVE.configured((CarverConfiguration)new CaveCarverConfiguration(0.2F, 
/*    */             
/* 78 */             (HeightProvider)UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.belowTop(1)), 
/* 79 */             (FloatProvider)ConstantFloat.of(0.5F), 
/* 80 */             VerticalAnchor.aboveBottom(10), (HolderSet)holderGetter
/* 81 */             .getOrThrow(BlockTags.NETHER_CARVER_REPLACEABLES), 
/* 82 */             (FloatProvider)ConstantFloat.of(1.0F), 
/* 83 */             (FloatProvider)ConstantFloat.of(1.0F), 
/* 84 */             (FloatProvider)ConstantFloat.of(-0.7F))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\Carvers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */