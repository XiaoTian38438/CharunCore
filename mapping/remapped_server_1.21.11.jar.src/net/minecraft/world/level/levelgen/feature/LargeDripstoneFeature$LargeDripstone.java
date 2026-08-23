/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
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
/*     */ final class LargeDripstone
/*     */ {
/*     */   private BlockPos root;
/*     */   private final boolean pointingUp;
/*     */   private int radius;
/*     */   private final double bluntness;
/*     */   private final double scale;
/*     */   
/*     */   LargeDripstone(BlockPos paramBlockPos, boolean paramBoolean, int paramInt, double paramDouble1, double paramDouble2) {
/* 116 */     this.root = paramBlockPos;
/* 117 */     this.pointingUp = paramBoolean;
/* 118 */     this.radius = paramInt;
/* 119 */     this.bluntness = paramDouble1;
/* 120 */     this.scale = paramDouble2;
/*     */   }
/*     */   
/*     */   private int getHeight() {
/* 124 */     return getHeightAtRadius(0.0F);
/*     */   }
/*     */   
/*     */   private int getMinY() {
/* 128 */     if (this.pointingUp) {
/* 129 */       return this.root.getY();
/*     */     }
/* 131 */     return this.root.getY() - getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMaxY() {
/* 136 */     if (!this.pointingUp) {
/* 137 */       return this.root.getY();
/*     */     }
/* 139 */     return this.root.getY() + getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel paramWorldGenLevel, LargeDripstoneFeature.WindOffsetter paramWindOffsetter) {
/* 144 */     while (this.radius > 1) {
/* 145 */       BlockPos.MutableBlockPos mutableBlockPos = this.root.mutable();
/* 146 */       int i = Math.min(10, getHeight());
/* 147 */       for (byte b = 0; b < i; b++) {
/* 148 */         if (paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).is(Blocks.LAVA)) {
/* 149 */           return false;
/*     */         }
/* 151 */         if (DripstoneUtils.isCircleMostlyEmbeddedInStone(paramWorldGenLevel, paramWindOffsetter.offset((BlockPos)mutableBlockPos), this.radius)) {
/* 152 */           this.root = (BlockPos)mutableBlockPos;
/* 153 */           return true;
/*     */         } 
/* 155 */         mutableBlockPos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */       } 
/* 157 */       this.radius /= 2;
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   private int getHeightAtRadius(float paramFloat) {
/* 163 */     return (int)DripstoneUtils.getDripstoneHeight(paramFloat, this.radius, this.scale, this.bluntness);
/*     */   }
/*     */   
/*     */   void placeBlocks(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, LargeDripstoneFeature.WindOffsetter paramWindOffsetter) {
/* 167 */     for (int i = -this.radius; i <= this.radius; i++) {
/* 168 */       for (int j = -this.radius; j <= this.radius; j++) {
/* 169 */         float f = Mth.sqrt((i * i + j * j));
/* 170 */         if (f <= this.radius) {
/*     */ 
/*     */ 
/*     */           
/* 174 */           int k = getHeightAtRadius(f);
/* 175 */           if (k > 0) {
/*     */ 
/*     */             
/* 178 */             if (paramRandomSource.nextFloat() < 0.2D)
/*     */             {
/* 180 */               k = (int)(k * Mth.randomBetween(paramRandomSource, 0.8F, 1.0F));
/*     */             }
/*     */             
/* 183 */             BlockPos.MutableBlockPos mutableBlockPos = this.root.offset(i, 0, j).mutable();
/* 184 */             boolean bool = false;
/*     */             
/* 186 */             int m = this.pointingUp ? paramWorldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutableBlockPos.getX(), mutableBlockPos.getZ()) : Integer.MAX_VALUE;
/* 187 */             for (byte b = 0; b < k && 
/* 188 */               mutableBlockPos.getY() < m; b++) {
/*     */ 
/*     */               
/* 191 */               BlockPos blockPos = paramWindOffsetter.offset((BlockPos)mutableBlockPos);
/* 192 */               if (DripstoneUtils.isEmptyOrWaterOrLava((LevelAccessor)paramWorldGenLevel, blockPos)) {
/* 193 */                 bool = true;
/* 194 */                 Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 195 */                 paramWorldGenLevel.setBlock(blockPos, block.defaultBlockState(), 2);
/* 196 */               } else if (bool && paramWorldGenLevel.getBlockState(blockPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                 break;
/*     */               } 
/* 199 */               mutableBlockPos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isSuitableForWind(LargeDripstoneConfiguration paramLargeDripstoneConfiguration) {
/* 209 */     return (this.radius >= paramLargeDripstoneConfiguration.minRadiusForWind && this.bluntness >= paramLargeDripstoneConfiguration.minBluntnessForWind);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature$LargeDripstone.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */