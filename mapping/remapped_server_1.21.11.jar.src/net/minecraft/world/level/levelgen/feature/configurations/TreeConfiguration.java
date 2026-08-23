/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfiguration implements FeatureConfiguration {
/*     */   static {
/*  18 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(()), (App)FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(()), (App)RootPlacer.CODEC.optionalFieldOf("root_placer").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter(()), (App)FeatureSize.CODEC.fieldOf("minimum_size").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(()), (App)Codec.BOOL.fieldOf("ignore_vines").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("force_dirt").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)paramInstance, TreeConfiguration::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<TreeConfiguration> CODEC;
/*     */   
/*     */   public final BlockStateProvider trunkProvider;
/*     */   
/*     */   public final BlockStateProvider dirtProvider;
/*     */   
/*     */   public final TrunkPlacer trunkPlacer;
/*     */   
/*     */   public final BlockStateProvider foliageProvider;
/*     */   
/*     */   public final FoliagePlacer foliagePlacer;
/*     */   
/*     */   public final Optional<RootPlacer> rootPlacer;
/*     */   
/*     */   public final FeatureSize minimumSize;
/*     */   
/*     */   public final List<TreeDecorator> decorators;
/*     */   public final boolean ignoreVines;
/*     */   public final boolean forceDirt;
/*     */   
/*     */   protected TreeConfiguration(BlockStateProvider paramBlockStateProvider1, TrunkPlacer paramTrunkPlacer, BlockStateProvider paramBlockStateProvider2, FoliagePlacer paramFoliagePlacer, Optional<RootPlacer> paramOptional, BlockStateProvider paramBlockStateProvider3, FeatureSize paramFeatureSize, List<TreeDecorator> paramList, boolean paramBoolean1, boolean paramBoolean2) {
/*  43 */     this.trunkProvider = paramBlockStateProvider1;
/*  44 */     this.trunkPlacer = paramTrunkPlacer;
/*  45 */     this.foliageProvider = paramBlockStateProvider2;
/*  46 */     this.foliagePlacer = paramFoliagePlacer;
/*  47 */     this.rootPlacer = paramOptional;
/*  48 */     this.dirtProvider = paramBlockStateProvider3;
/*  49 */     this.minimumSize = paramFeatureSize;
/*  50 */     this.decorators = paramList;
/*  51 */     this.ignoreVines = paramBoolean1;
/*  52 */     this.forceDirt = paramBoolean2;
/*     */   }
/*     */   
/*     */   public static class TreeConfigurationBuilder {
/*     */     public final BlockStateProvider trunkProvider;
/*     */     private final TrunkPlacer trunkPlacer;
/*     */     public final BlockStateProvider foliageProvider;
/*     */     private final FoliagePlacer foliagePlacer;
/*     */     private final Optional<RootPlacer> rootPlacer;
/*     */     private BlockStateProvider dirtProvider;
/*     */     private final FeatureSize minimumSize;
/*  63 */     private List<TreeDecorator> decorators = (List<TreeDecorator>)ImmutableList.of();
/*     */     private boolean ignoreVines;
/*     */     private boolean forceDirt;
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider param1BlockStateProvider1, TrunkPlacer param1TrunkPlacer, BlockStateProvider param1BlockStateProvider2, FoliagePlacer param1FoliagePlacer, Optional<RootPlacer> param1Optional, FeatureSize param1FeatureSize) {
/*  68 */       this.trunkProvider = param1BlockStateProvider1;
/*  69 */       this.trunkPlacer = param1TrunkPlacer;
/*  70 */       this.foliageProvider = param1BlockStateProvider2;
/*  71 */       this.dirtProvider = (BlockStateProvider)BlockStateProvider.simple(Blocks.DIRT);
/*  72 */       this.foliagePlacer = param1FoliagePlacer;
/*  73 */       this.rootPlacer = param1Optional;
/*  74 */       this.minimumSize = param1FeatureSize;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider param1BlockStateProvider1, TrunkPlacer param1TrunkPlacer, BlockStateProvider param1BlockStateProvider2, FoliagePlacer param1FoliagePlacer, FeatureSize param1FeatureSize) {
/*  78 */       this(param1BlockStateProvider1, param1TrunkPlacer, param1BlockStateProvider2, param1FoliagePlacer, Optional.empty(), param1FeatureSize);
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder dirt(BlockStateProvider param1BlockStateProvider) {
/*  82 */       this.dirtProvider = param1BlockStateProvider;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder decorators(List<TreeDecorator> param1List) {
/*  87 */       this.decorators = param1List;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder ignoreVines() {
/*  92 */       this.ignoreVines = true;
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder forceDirt() {
/*  97 */       this.forceDirt = true;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfiguration build() {
/* 102 */       return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */