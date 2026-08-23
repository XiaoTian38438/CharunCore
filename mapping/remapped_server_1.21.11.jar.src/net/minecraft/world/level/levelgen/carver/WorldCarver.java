/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.Aquifer;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class WorldCarver<C extends CarverConfiguration>
/*     */ {
/*  34 */   public static final WorldCarver<CaveCarverConfiguration> CAVE = register("cave", new CaveWorldCarver(CaveCarverConfiguration.CODEC));
/*  35 */   public static final WorldCarver<CaveCarverConfiguration> NETHER_CAVE = register("nether_cave", new NetherWorldCarver(CaveCarverConfiguration.CODEC));
/*  36 */   public static final WorldCarver<CanyonCarverConfiguration> CANYON = register("canyon", new CanyonWorldCarver(CanyonCarverConfiguration.CODEC));
/*     */   
/*  38 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  39 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*  40 */   protected static final FluidState WATER = Fluids.WATER.defaultFluidState();
/*  41 */   protected static final FluidState LAVA = Fluids.LAVA.defaultFluidState();
/*     */   
/*     */   private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String paramString, F paramF) {
/*  44 */     return (F)Registry.register(BuiltInRegistries.CARVER, paramString, paramF);
/*     */   }
/*     */   
/*  47 */   protected Set<Fluid> liquids = (Set<Fluid>)ImmutableSet.of(Fluids.WATER);
/*     */ 
/*     */   
/*     */   private final MapCodec<ConfiguredWorldCarver<C>> configuredCodec;
/*     */ 
/*     */   
/*     */   public WorldCarver(Codec<C> paramCodec) {
/*  54 */     this.configuredCodec = paramCodec.fieldOf("config").xmap(this::configured, ConfiguredWorldCarver::config);
/*     */   }
/*     */   
/*     */   public ConfiguredWorldCarver<C> configured(C paramC) {
/*  58 */     return new ConfiguredWorldCarver<>(this, paramC);
/*     */   }
/*     */   
/*     */   public MapCodec<ConfiguredWorldCarver<C>> configuredCodec() {
/*  62 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   public int getRange() {
/*  66 */     return 4;
/*     */   }
/*     */   
/*     */   protected boolean carveEllipsoid(CarvingContext paramCarvingContext, C paramC, ChunkAccess paramChunkAccess, Function<BlockPos, Holder<Biome>> paramFunction, Aquifer paramAquifer, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, CarvingMask paramCarvingMask, CarveSkipChecker paramCarveSkipChecker) {
/*  70 */     ChunkPos chunkPos = paramChunkAccess.getPos();
/*     */     
/*  72 */     double d1 = chunkPos.getMiddleBlockX();
/*  73 */     double d2 = chunkPos.getMiddleBlockZ();
/*     */     
/*  75 */     double d3 = 16.0D + paramDouble4 * 2.0D;
/*  76 */     if (Math.abs(paramDouble1 - d1) > d3 || Math.abs(paramDouble3 - d2) > d3) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     int i = chunkPos.getMinBlockX();
/*  81 */     int j = chunkPos.getMinBlockZ();
/*     */ 
/*     */     
/*  84 */     int k = Math.max(Mth.floor(paramDouble1 - paramDouble4) - i - 1, 0);
/*  85 */     int m = Math.min(Mth.floor(paramDouble1 + paramDouble4) - i, 15);
/*     */ 
/*     */     
/*  88 */     int n = Math.max(Mth.floor(paramDouble2 - paramDouble5) - 1, paramCarvingContext.getMinGenY() + 1);
/*  89 */     byte b = paramChunkAccess.isUpgrading() ? 0 : 7;
/*  90 */     int i1 = Math.min(Mth.floor(paramDouble2 + paramDouble5) + 1, paramCarvingContext.getMinGenY() + paramCarvingContext.getGenDepth() - 1 - b);
/*     */     
/*  92 */     int i2 = Math.max(Mth.floor(paramDouble3 - paramDouble4) - j - 1, 0);
/*  93 */     int i3 = Math.min(Mth.floor(paramDouble3 + paramDouble4) - j, 15);
/*     */     
/*  95 */     boolean bool = false;
/*  96 */     BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
/*  97 */     BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
/*     */     
/*  99 */     for (int i4 = k; i4 <= m; i4++) {
/* 100 */       int i5 = chunkPos.getBlockX(i4);
/*     */ 
/*     */       
/* 103 */       double d = (i5 + 0.5D - paramDouble1) / paramDouble4;
/* 104 */       for (int i6 = i2; i6 <= i3; i6++) {
/* 105 */         int i7 = chunkPos.getBlockZ(i6);
/* 106 */         double d4 = (i7 + 0.5D - paramDouble3) / paramDouble4;
/* 107 */         if (d * d + d4 * d4 < 1.0D) {
/*     */ 
/*     */ 
/*     */           
/* 111 */           MutableBoolean mutableBoolean = new MutableBoolean(false);
/*     */           
/* 113 */           for (int i8 = i1; i8 > n; i8--) {
/* 114 */             double d5 = (i8 - 0.5D - paramDouble2) / paramDouble5;
/* 115 */             if (!paramCarveSkipChecker.shouldSkip(paramCarvingContext, d, d5, d4, i8))
/*     */             {
/*     */ 
/*     */               
/* 119 */               if (!paramCarvingMask.get(i4, i8, i6) || isDebugEnabled((CarverConfiguration)paramC)) {
/* 120 */                 paramCarvingMask.set(i4, i8, i6);
/*     */                 
/* 122 */                 mutableBlockPos1.set(i5, i8, i7);
/* 123 */                 bool |= carveBlock(paramCarvingContext, paramC, paramChunkAccess, paramFunction, paramCarvingMask, mutableBlockPos1, mutableBlockPos2, paramAquifer, mutableBoolean);
/*     */               }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 129 */     return bool;
/*     */   }
/*     */   
/*     */   protected boolean carveBlock(CarvingContext paramCarvingContext, C paramC, ChunkAccess paramChunkAccess, Function<BlockPos, Holder<Biome>> paramFunction, CarvingMask paramCarvingMask, BlockPos.MutableBlockPos paramMutableBlockPos1, BlockPos.MutableBlockPos paramMutableBlockPos2, Aquifer paramAquifer, MutableBoolean paramMutableBoolean) {
/* 133 */     BlockState blockState1 = paramChunkAccess.getBlockState((BlockPos)paramMutableBlockPos1);
/*     */ 
/*     */     
/* 136 */     if (blockState1.is(Blocks.GRASS_BLOCK) || blockState1.is(Blocks.MYCELIUM)) {
/* 137 */       paramMutableBoolean.setTrue();
/*     */     }
/* 139 */     if (!canReplaceBlock(paramC, blockState1) && !isDebugEnabled((CarverConfiguration)paramC)) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     BlockState blockState2 = getCarveState(paramCarvingContext, paramC, (BlockPos)paramMutableBlockPos1, paramAquifer);
/* 144 */     if (blockState2 == null) {
/* 145 */       return false;
/*     */     }
/* 147 */     paramChunkAccess.setBlockState((BlockPos)paramMutableBlockPos1, blockState2);
/* 148 */     if (paramAquifer.shouldScheduleFluidUpdate() && !blockState2.getFluidState().isEmpty())
/*     */     {
/* 150 */       paramChunkAccess.markPosForPostprocessing((BlockPos)paramMutableBlockPos1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (paramMutableBoolean.isTrue()) {
/* 156 */       paramMutableBlockPos2.setWithOffset((Vec3i)paramMutableBlockPos1, Direction.DOWN);
/* 157 */       if (paramChunkAccess.getBlockState((BlockPos)paramMutableBlockPos2).is(Blocks.DIRT)) {
/* 158 */         paramCarvingContext.topMaterial(paramFunction, paramChunkAccess, (BlockPos)paramMutableBlockPos2, !blockState2.getFluidState().isEmpty()).ifPresent(paramBlockState -> {
/*     */               paramChunkAccess.setBlockState((BlockPos)paramMutableBlockPos, paramBlockState);
/*     */               
/*     */               if (!paramBlockState.getFluidState().isEmpty()) {
/*     */                 paramChunkAccess.markPosForPostprocessing((BlockPos)paramMutableBlockPos);
/*     */               }
/*     */             });
/*     */       }
/*     */     } 
/* 167 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockState getCarveState(CarvingContext paramCarvingContext, C paramC, BlockPos paramBlockPos, Aquifer paramAquifer) {
/* 175 */     if (paramBlockPos.getY() <= ((CarverConfiguration)paramC).lavaLevel.resolveY(paramCarvingContext))
/*     */     {
/* 177 */       return LAVA.createLegacyBlock();
/*     */     }
/*     */     
/* 180 */     BlockState blockState = paramAquifer.computeSubstance((DensityFunction.FunctionContext)new DensityFunction.SinglePointContext(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ()), 0.0D);
/* 181 */     if (blockState == null)
/*     */     {
/* 183 */       return isDebugEnabled((CarverConfiguration)paramC) ? ((CarverConfiguration)paramC).debugSettings.getBarrierState() : null;
/*     */     }
/*     */     
/* 186 */     return isDebugEnabled((CarverConfiguration)paramC) ? getDebugState((CarverConfiguration)paramC, blockState) : blockState;
/*     */   }
/*     */   
/*     */   private static BlockState getDebugState(CarverConfiguration paramCarverConfiguration, BlockState paramBlockState) {
/* 190 */     if (paramBlockState.is(Blocks.AIR))
/* 191 */       return paramCarverConfiguration.debugSettings.getAirState(); 
/* 192 */     if (paramBlockState.is(Blocks.WATER)) {
/* 193 */       BlockState blockState = paramCarverConfiguration.debugSettings.getWaterState();
/* 194 */       if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/* 195 */         return (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */       }
/* 197 */       return blockState;
/* 198 */     }  if (paramBlockState.is(Blocks.LAVA)) {
/* 199 */       return paramCarverConfiguration.debugSettings.getLavaState();
/*     */     }
/* 201 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canReplaceBlock(C paramC, BlockState paramBlockState) {
/* 213 */     return paramBlockState.is(((CarverConfiguration)paramC).replaceable);
/*     */   }
/*     */   
/*     */   protected static boolean canReach(ChunkPos paramChunkPos, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, float paramFloat) {
/* 217 */     double d1 = paramChunkPos.getMiddleBlockX();
/* 218 */     double d2 = paramChunkPos.getMiddleBlockZ();
/*     */     
/* 220 */     double d3 = paramDouble1 - d1;
/* 221 */     double d4 = paramDouble2 - d2;
/* 222 */     double d5 = (paramInt2 - paramInt1);
/* 223 */     double d6 = (paramFloat + 2.0F + 16.0F);
/*     */     
/* 225 */     return (d3 * d3 + d4 * d4 - d5 * d5 <= d6 * d6);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDebugEnabled(CarverConfiguration paramCarverConfiguration) {
/* 230 */     return (SharedConstants.DEBUG_CARVERS || paramCarverConfiguration.debugSettings.isDebugMode());
/*     */   }
/*     */   
/*     */   public abstract boolean carve(CarvingContext paramCarvingContext, C paramC, ChunkAccess paramChunkAccess, Function<BlockPos, Holder<Biome>> paramFunction, RandomSource paramRandomSource, Aquifer paramAquifer, ChunkPos paramChunkPos, CarvingMask paramCarvingMask);
/*     */   
/*     */   public abstract boolean isStartChunk(C paramC, RandomSource paramRandomSource);
/*     */   
/*     */   public static interface CarveSkipChecker {
/*     */     boolean shouldSkip(CarvingContext param1CarvingContext, double param1Double1, double param1Double2, double param1Double3, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\WorldCarver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */