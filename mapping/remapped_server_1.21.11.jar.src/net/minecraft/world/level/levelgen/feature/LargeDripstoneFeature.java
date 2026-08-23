/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LargeDripstoneFeature
/*     */   extends Feature<LargeDripstoneConfiguration>
/*     */ {
/*     */   public LargeDripstoneFeature(Codec<LargeDripstoneConfiguration> paramCodec) {
/*  28 */     super(paramCodec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> paramFeaturePlaceContext) {
/*     */     WindOffsetter windOffsetter;
/*  33 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  34 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  35 */     LargeDripstoneConfiguration largeDripstoneConfiguration = paramFeaturePlaceContext.config();
/*  36 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*     */     
/*  38 */     if (!DripstoneUtils.isEmptyOrWater((LevelAccessor)worldGenLevel, blockPos)) {
/*  39 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  43 */     Optional<Column.Range> optional = Column.scan((LevelSimulatedReader)worldGenLevel, blockPos, largeDripstoneConfiguration.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isDripstoneBaseOrLava);
/*  44 */     if (optional.isEmpty() || !(optional.get() instanceof Column.Range))
/*     */     {
/*     */ 
/*     */       
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     Column.Range range = optional.get();
/*     */     
/*  53 */     if (range.height() < 4)
/*     */     {
/*  55 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  59 */     int i = (int)(range.height() * largeDripstoneConfiguration.maxColumnRadiusToCaveHeightRatio);
/*  60 */     int j = Mth.clamp(i, largeDripstoneConfiguration.columnRadius.getMinValue(), largeDripstoneConfiguration.columnRadius.getMaxValue());
/*  61 */     int k = Mth.randomBetweenInclusive(randomSource, largeDripstoneConfiguration.columnRadius.getMinValue(), j);
/*     */     
/*  63 */     LargeDripstone largeDripstone1 = makeDripstone(blockPos.atY(range.ceiling() - 1), false, randomSource, k, largeDripstoneConfiguration.stalactiteBluntness, largeDripstoneConfiguration.heightScale);
/*  64 */     LargeDripstone largeDripstone2 = makeDripstone(blockPos.atY(range.floor() + 1), true, randomSource, k, largeDripstoneConfiguration.stalagmiteBluntness, largeDripstoneConfiguration.heightScale);
/*     */ 
/*     */     
/*  67 */     if (largeDripstone1.isSuitableForWind(largeDripstoneConfiguration) && largeDripstone2.isSuitableForWind(largeDripstoneConfiguration)) {
/*  68 */       windOffsetter = new WindOffsetter(blockPos.getY(), randomSource, largeDripstoneConfiguration.windSpeed);
/*     */     } else {
/*  70 */       windOffsetter = WindOffsetter.noWind();
/*     */     } 
/*     */     
/*  73 */     boolean bool1 = largeDripstone1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldGenLevel, windOffsetter);
/*  74 */     boolean bool2 = largeDripstone2.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldGenLevel, windOffsetter);
/*     */     
/*  76 */     if (bool1) {
/*  77 */       largeDripstone1.placeBlocks(worldGenLevel, randomSource, windOffsetter);
/*     */     }
/*     */     
/*  80 */     if (bool2) {
/*  81 */       largeDripstone2.placeBlocks(worldGenLevel, randomSource, windOffsetter);
/*     */     }
/*     */     
/*  84 */     if (SharedConstants.DEBUG_LARGE_DRIPSTONE) {
/*  85 */       placeDebugMarkers(worldGenLevel, blockPos, range, windOffsetter);
/*     */     }
/*     */     
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   private static LargeDripstone makeDripstone(BlockPos paramBlockPos, boolean paramBoolean, RandomSource paramRandomSource, int paramInt, FloatProvider paramFloatProvider1, FloatProvider paramFloatProvider2) {
/*  92 */     return new LargeDripstone(paramBlockPos, paramBoolean, paramInt, paramFloatProvider1.sample(paramRandomSource), paramFloatProvider2.sample(paramRandomSource));
/*     */   }
/*     */   
/*     */   private void placeDebugMarkers(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, Column.Range paramRange, WindOffsetter paramWindOffsetter) {
/*  96 */     paramWorldGenLevel.setBlock(paramWindOffsetter.offset(paramBlockPos.atY(paramRange.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
/*  97 */     paramWorldGenLevel.setBlock(paramWindOffsetter.offset(paramBlockPos.atY(paramRange.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
/*  98 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.atY(paramRange.floor() + 2).mutable();
/*  99 */     while (mutableBlockPos.getY() < paramRange.ceiling() - 1) {
/* 100 */       BlockPos blockPos = paramWindOffsetter.offset((BlockPos)mutableBlockPos);
/* 101 */       if (DripstoneUtils.isEmptyOrWater((LevelAccessor)paramWorldGenLevel, blockPos) || paramWorldGenLevel.getBlockState(blockPos).is(Blocks.DRIPSTONE_BLOCK)) {
/* 102 */         paramWorldGenLevel.setBlock(blockPos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
/*     */       }
/* 104 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class LargeDripstone {
/*     */     private BlockPos root;
/*     */     private final boolean pointingUp;
/*     */     private int radius;
/*     */     private final double bluntness;
/*     */     private final double scale;
/*     */     
/*     */     LargeDripstone(BlockPos param1BlockPos, boolean param1Boolean, int param1Int, double param1Double1, double param1Double2) {
/* 116 */       this.root = param1BlockPos;
/* 117 */       this.pointingUp = param1Boolean;
/* 118 */       this.radius = param1Int;
/* 119 */       this.bluntness = param1Double1;
/* 120 */       this.scale = param1Double2;
/*     */     }
/*     */     
/*     */     private int getHeight() {
/* 124 */       return getHeightAtRadius(0.0F);
/*     */     }
/*     */     
/*     */     private int getMinY() {
/* 128 */       if (this.pointingUp) {
/* 129 */         return this.root.getY();
/*     */       }
/* 131 */       return this.root.getY() - getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getMaxY() {
/* 136 */       if (!this.pointingUp) {
/* 137 */         return this.root.getY();
/*     */       }
/* 139 */       return this.root.getY() + getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel param1WorldGenLevel, LargeDripstoneFeature.WindOffsetter param1WindOffsetter) {
/* 144 */       while (this.radius > 1) {
/* 145 */         BlockPos.MutableBlockPos mutableBlockPos = this.root.mutable();
/* 146 */         int i = Math.min(10, getHeight());
/* 147 */         for (byte b = 0; b < i; b++) {
/* 148 */           if (param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos).is(Blocks.LAVA)) {
/* 149 */             return false;
/*     */           }
/* 151 */           if (DripstoneUtils.isCircleMostlyEmbeddedInStone(param1WorldGenLevel, param1WindOffsetter.offset((BlockPos)mutableBlockPos), this.radius)) {
/* 152 */             this.root = (BlockPos)mutableBlockPos;
/* 153 */             return true;
/*     */           } 
/* 155 */           mutableBlockPos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */         } 
/* 157 */         this.radius /= 2;
/*     */       } 
/* 159 */       return false;
/*     */     }
/*     */     
/*     */     private int getHeightAtRadius(float param1Float) {
/* 163 */       return (int)DripstoneUtils.getDripstoneHeight(param1Float, this.radius, this.scale, this.bluntness);
/*     */     }
/*     */     
/*     */     void placeBlocks(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, LargeDripstoneFeature.WindOffsetter param1WindOffsetter) {
/* 167 */       for (int i = -this.radius; i <= this.radius; i++) {
/* 168 */         for (int j = -this.radius; j <= this.radius; j++) {
/* 169 */           float f = Mth.sqrt((i * i + j * j));
/* 170 */           if (f <= this.radius) {
/*     */ 
/*     */ 
/*     */             
/* 174 */             int k = getHeightAtRadius(f);
/* 175 */             if (k > 0) {
/*     */ 
/*     */               
/* 178 */               if (param1RandomSource.nextFloat() < 0.2D)
/*     */               {
/* 180 */                 k = (int)(k * Mth.randomBetween(param1RandomSource, 0.8F, 1.0F));
/*     */               }
/*     */               
/* 183 */               BlockPos.MutableBlockPos mutableBlockPos = this.root.offset(i, 0, j).mutable();
/* 184 */               boolean bool = false;
/*     */               
/* 186 */               int m = this.pointingUp ? param1WorldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutableBlockPos.getX(), mutableBlockPos.getZ()) : Integer.MAX_VALUE;
/* 187 */               for (byte b = 0; b < k && 
/* 188 */                 mutableBlockPos.getY() < m; b++) {
/*     */ 
/*     */                 
/* 191 */                 BlockPos blockPos = param1WindOffsetter.offset((BlockPos)mutableBlockPos);
/* 192 */                 if (DripstoneUtils.isEmptyOrWaterOrLava((LevelAccessor)param1WorldGenLevel, blockPos)) {
/* 193 */                   bool = true;
/* 194 */                   Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 195 */                   param1WorldGenLevel.setBlock(blockPos, block.defaultBlockState(), 2);
/* 196 */                 } else if (bool && param1WorldGenLevel.getBlockState(blockPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                   break;
/*     */                 } 
/* 199 */                 mutableBlockPos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isSuitableForWind(LargeDripstoneConfiguration param1LargeDripstoneConfiguration) {
/* 209 */       return (this.radius >= param1LargeDripstoneConfiguration.minRadiusForWind && this.bluntness >= param1LargeDripstoneConfiguration.minBluntnessForWind);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WindOffsetter
/*     */   {
/*     */     private final int originY;
/*     */     
/*     */     private final Vec3 windSpeed;
/*     */     
/*     */     WindOffsetter(int param1Int, RandomSource param1RandomSource, FloatProvider param1FloatProvider) {
/* 221 */       this.originY = param1Int;
/*     */       
/* 223 */       float f1 = param1FloatProvider.sample(param1RandomSource);
/*     */       
/* 225 */       float f2 = Mth.randomBetween(param1RandomSource, 0.0F, 3.1415927F);
/* 226 */       this.windSpeed = new Vec3((Mth.cos(f2) * f1), 0.0D, (Mth.sin(f2) * f1));
/*     */     }
/*     */     
/*     */     private WindOffsetter() {
/* 230 */       this.originY = 0;
/* 231 */       this.windSpeed = null;
/*     */     }
/*     */     
/*     */     static WindOffsetter noWind() {
/* 235 */       return new WindOffsetter();
/*     */     }
/*     */     
/*     */     BlockPos offset(BlockPos param1BlockPos) {
/* 239 */       if (this.windSpeed == null) {
/* 240 */         return param1BlockPos;
/*     */       }
/* 242 */       int i = this.originY - param1BlockPos.getY();
/* 243 */       Vec3 vec3 = this.windSpeed.scale(i);
/* 244 */       return param1BlockPos.offset(Mth.floor(vec3.x), 0, Mth.floor(vec3.z));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */