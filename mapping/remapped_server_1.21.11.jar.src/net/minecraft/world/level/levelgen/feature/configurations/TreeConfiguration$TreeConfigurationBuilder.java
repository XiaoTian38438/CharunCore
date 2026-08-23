/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
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
/*     */ public class TreeConfigurationBuilder
/*     */ {
/*     */   public final BlockStateProvider trunkProvider;
/*     */   private final TrunkPlacer trunkPlacer;
/*     */   public final BlockStateProvider foliageProvider;
/*     */   private final FoliagePlacer foliagePlacer;
/*     */   private final Optional<RootPlacer> rootPlacer;
/*     */   private BlockStateProvider dirtProvider;
/*     */   private final FeatureSize minimumSize;
/*  63 */   private List<TreeDecorator> decorators = (List<TreeDecorator>)ImmutableList.of();
/*     */   private boolean ignoreVines;
/*     */   private boolean forceDirt;
/*     */   
/*     */   public TreeConfigurationBuilder(BlockStateProvider paramBlockStateProvider1, TrunkPlacer paramTrunkPlacer, BlockStateProvider paramBlockStateProvider2, FoliagePlacer paramFoliagePlacer, Optional<RootPlacer> paramOptional, FeatureSize paramFeatureSize) {
/*  68 */     this.trunkProvider = paramBlockStateProvider1;
/*  69 */     this.trunkPlacer = paramTrunkPlacer;
/*  70 */     this.foliageProvider = paramBlockStateProvider2;
/*  71 */     this.dirtProvider = (BlockStateProvider)BlockStateProvider.simple(Blocks.DIRT);
/*  72 */     this.foliagePlacer = paramFoliagePlacer;
/*  73 */     this.rootPlacer = paramOptional;
/*  74 */     this.minimumSize = paramFeatureSize;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder(BlockStateProvider paramBlockStateProvider1, TrunkPlacer paramTrunkPlacer, BlockStateProvider paramBlockStateProvider2, FoliagePlacer paramFoliagePlacer, FeatureSize paramFeatureSize) {
/*  78 */     this(paramBlockStateProvider1, paramTrunkPlacer, paramBlockStateProvider2, paramFoliagePlacer, Optional.empty(), paramFeatureSize);
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder dirt(BlockStateProvider paramBlockStateProvider) {
/*  82 */     this.dirtProvider = paramBlockStateProvider;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder decorators(List<TreeDecorator> paramList) {
/*  87 */     this.decorators = paramList;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder ignoreVines() {
/*  92 */     this.ignoreVines = true;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder forceDirt() {
/*  97 */     this.forceDirt = true;
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfiguration build() {
/* 102 */     return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration$TreeConfigurationBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */