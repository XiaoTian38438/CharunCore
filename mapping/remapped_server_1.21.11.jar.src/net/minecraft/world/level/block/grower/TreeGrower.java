/*     */ package net.minecraft.world.level.block.grower;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ 
/*     */ public final class TreeGrower {
/*     */   public static final Codec<TreeGrower> CODEC;
/*  26 */   private static final Map<String, TreeGrower> GROWERS = (Map<String, TreeGrower>)new Object2ObjectArrayMap(); static {
/*  27 */     Objects.requireNonNull(GROWERS); CODEC = Codec.stringResolver(paramTreeGrower -> paramTreeGrower.name, GROWERS::get);
/*     */   }
/*  29 */   public static final TreeGrower OAK = new TreeGrower("oak", 0.1F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.OAK), Optional.of(TreeFeatures.FANCY_OAK), Optional.of(TreeFeatures.OAK_BEES_005), Optional.of(TreeFeatures.FANCY_OAK_BEES_005));
/*  30 */   public static final TreeGrower SPRUCE = new TreeGrower("spruce", 0.5F, Optional.of(TreeFeatures.MEGA_SPRUCE), Optional.of(TreeFeatures.MEGA_PINE), Optional.of(TreeFeatures.SPRUCE), Optional.empty(), Optional.empty(), Optional.empty());
/*  31 */   public static final TreeGrower MANGROVE = new TreeGrower("mangrove", 0.85F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.MANGROVE), Optional.of(TreeFeatures.TALL_MANGROVE), Optional.empty(), Optional.empty());
/*     */   
/*  33 */   public static final TreeGrower AZALEA = new TreeGrower("azalea", Optional.empty(), Optional.of(TreeFeatures.AZALEA_TREE), Optional.empty());
/*  34 */   public static final TreeGrower BIRCH = new TreeGrower("birch", Optional.empty(), Optional.of(TreeFeatures.BIRCH), Optional.of(TreeFeatures.BIRCH_BEES_005));
/*  35 */   public static final TreeGrower JUNGLE = new TreeGrower("jungle", Optional.of(TreeFeatures.MEGA_JUNGLE_TREE), Optional.of(TreeFeatures.JUNGLE_TREE_NO_VINE), Optional.empty());
/*  36 */   public static final TreeGrower ACACIA = new TreeGrower("acacia", Optional.empty(), Optional.of(TreeFeatures.ACACIA), Optional.empty());
/*  37 */   public static final TreeGrower CHERRY = new TreeGrower("cherry", Optional.empty(), Optional.of(TreeFeatures.CHERRY), Optional.of(TreeFeatures.CHERRY_BEES_005));
/*  38 */   public static final TreeGrower DARK_OAK = new TreeGrower("dark_oak", Optional.of(TreeFeatures.DARK_OAK), Optional.empty(), Optional.empty());
/*  39 */   public static final TreeGrower PALE_OAK = new TreeGrower("pale_oak", Optional.of(TreeFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty());
/*     */   
/*     */   private final String name;
/*     */   private final float secondaryChance;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers;
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers;
/*     */   
/*     */   public TreeGrower(String paramString, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional1, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional2, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional3) {
/*  51 */     this(paramString, 0.0F, paramOptional1, Optional.empty(), paramOptional2, Optional.empty(), paramOptional3, Optional.empty());
/*     */   }
/*     */   
/*     */   public TreeGrower(String paramString, float paramFloat, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional1, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional2, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional3, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional4, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional5, Optional<ResourceKey<ConfiguredFeature<?, ?>>> paramOptional6) {
/*  55 */     this.name = paramString;
/*  56 */     this.secondaryChance = paramFloat;
/*  57 */     this.megaTree = paramOptional1;
/*  58 */     this.secondaryMegaTree = paramOptional2;
/*  59 */     this.tree = paramOptional3;
/*  60 */     this.secondaryTree = paramOptional4;
/*  61 */     this.flowers = paramOptional5;
/*  62 */     this.secondaryFlowers = paramOptional6;
/*     */     
/*  64 */     GROWERS.put(paramString, this);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource paramRandomSource, boolean paramBoolean) {
/*  68 */     if (paramRandomSource.nextFloat() < this.secondaryChance) {
/*  69 */       if (paramBoolean && this.secondaryFlowers.isPresent()) {
/*  70 */         return this.secondaryFlowers.get();
/*     */       }
/*  72 */       if (this.secondaryTree.isPresent()) {
/*  73 */         return this.secondaryTree.get();
/*     */       }
/*     */     } 
/*  76 */     if (paramBoolean && this.flowers.isPresent()) {
/*  77 */       return this.flowers.get();
/*     */     }
/*  79 */     return this.tree.orElse(null);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource paramRandomSource) {
/*  83 */     if (this.secondaryMegaTree.isPresent() && paramRandomSource.nextFloat() < this.secondaryChance) {
/*  84 */       return this.secondaryMegaTree.get();
/*     */     }
/*  86 */     return this.megaTree.orElse(null);
/*     */   }
/*     */   
/*     */   public boolean growTree(ServerLevel paramServerLevel, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos, BlockState paramBlockState, RandomSource paramRandomSource) {
/*  90 */     ResourceKey<ConfiguredFeature<?, ?>> resourceKey1 = getConfiguredMegaFeature(paramRandomSource);
/*  91 */     if (resourceKey1 != null) {
/*  92 */       Holder holder1 = paramServerLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(resourceKey1).orElse(null);
/*  93 */       if (holder1 != null) {
/*  94 */         for (byte b = 0; b >= -1; b--) {
/*  95 */           for (byte b1 = 0; b1 >= -1; b1--) {
/*  96 */             if (isTwoByTwoSapling(paramBlockState, (BlockGetter)paramServerLevel, paramBlockPos, b, b1)) {
/*  97 */               ConfiguredFeature configuredFeature1 = (ConfiguredFeature)holder1.value();
/*     */               
/*  99 */               BlockState blockState1 = Blocks.AIR.defaultBlockState();
/* 100 */               paramServerLevel.setBlock(paramBlockPos.offset(b, 0, b1), blockState1, 260);
/* 101 */               paramServerLevel.setBlock(paramBlockPos.offset(b + 1, 0, b1), blockState1, 260);
/* 102 */               paramServerLevel.setBlock(paramBlockPos.offset(b, 0, b1 + 1), blockState1, 260);
/* 103 */               paramServerLevel.setBlock(paramBlockPos.offset(b + 1, 0, b1 + 1), blockState1, 260);
/*     */               
/* 105 */               if (configuredFeature1.place((WorldGenLevel)paramServerLevel, paramChunkGenerator, paramRandomSource, paramBlockPos.offset(b, 0, b1))) {
/* 106 */                 return true;
/*     */               }
/* 108 */               paramServerLevel.setBlock(paramBlockPos.offset(b, 0, b1), paramBlockState, 260);
/* 109 */               paramServerLevel.setBlock(paramBlockPos.offset(b + 1, 0, b1), paramBlockState, 260);
/* 110 */               paramServerLevel.setBlock(paramBlockPos.offset(b, 0, b1 + 1), paramBlockState, 260);
/* 111 */               paramServerLevel.setBlock(paramBlockPos.offset(b + 1, 0, b1 + 1), paramBlockState, 260);
/* 112 */               return false;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 119 */     ResourceKey<ConfiguredFeature<?, ?>> resourceKey2 = getConfiguredFeature(paramRandomSource, hasFlowers((LevelAccessor)paramServerLevel, paramBlockPos));
/* 120 */     if (resourceKey2 == null) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     Holder holder = paramServerLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(resourceKey2).orElse(null);
/* 125 */     if (holder == null) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     ConfiguredFeature configuredFeature = (ConfiguredFeature)holder.value();
/*     */     
/* 131 */     BlockState blockState = paramServerLevel.getFluidState(paramBlockPos).createLegacyBlock();
/* 132 */     paramServerLevel.setBlock(paramBlockPos, blockState, 260);
/*     */     
/* 134 */     if (configuredFeature.place((WorldGenLevel)paramServerLevel, paramChunkGenerator, paramRandomSource, paramBlockPos)) {
/* 135 */       if (paramServerLevel.getBlockState(paramBlockPos) == blockState) {
/* 136 */         paramServerLevel.sendBlockUpdated(paramBlockPos, paramBlockState, blockState, 2);
/*     */       }
/* 138 */       return true;
/*     */     } 
/*     */     
/* 141 */     paramServerLevel.setBlock(paramBlockPos, paramBlockState, 260);
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isTwoByTwoSapling(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 146 */     Block block = paramBlockState.getBlock();
/* 147 */     return (paramBlockGetter.getBlockState(paramBlockPos.offset(paramInt1, 0, paramInt2)).is(block) && paramBlockGetter
/* 148 */       .getBlockState(paramBlockPos.offset(paramInt1 + 1, 0, paramInt2)).is(block) && paramBlockGetter
/* 149 */       .getBlockState(paramBlockPos.offset(paramInt1, 0, paramInt2 + 1)).is(block) && paramBlockGetter
/* 150 */       .getBlockState(paramBlockPos.offset(paramInt1 + 1, 0, paramInt2 + 1)).is(block));
/*     */   }
/*     */   
/*     */   private boolean hasFlowers(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 154 */     for (BlockPos blockPos : BlockPos.MutableBlockPos.betweenClosed(paramBlockPos.below().north(2).west(2), paramBlockPos.above().south(2).east(2))) {
/* 155 */       if (paramLevelAccessor.getBlockState(blockPos).is(BlockTags.FLOWERS)) {
/* 156 */         return true;
/*     */       }
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\grower\TreeGrower.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */