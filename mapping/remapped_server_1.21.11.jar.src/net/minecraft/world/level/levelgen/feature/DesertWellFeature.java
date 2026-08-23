/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class DesertWellFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  20 */   private static final BlockStatePredicate IS_SAND = BlockStatePredicate.forBlock(Blocks.SAND);
/*     */   
/*  22 */   private final BlockState sand = Blocks.SAND.defaultBlockState();
/*  23 */   private final BlockState sandSlab = Blocks.SANDSTONE_SLAB.defaultBlockState();
/*  24 */   private final BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
/*  25 */   private final BlockState water = Blocks.WATER.defaultBlockState();
/*     */   
/*     */   public DesertWellFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/*  28 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/*  33 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  34 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/*  35 */     blockPos1 = blockPos1.above();
/*     */     
/*  37 */     while (worldGenLevel.isEmptyBlock(blockPos1) && blockPos1.getY() > worldGenLevel.getMinY() + 2) {
/*  38 */       blockPos1 = blockPos1.below();
/*     */     }
/*     */     
/*  41 */     if (!IS_SAND.test(worldGenLevel.getBlockState(blockPos1))) {
/*  42 */       return false;
/*     */     }
/*     */     
/*     */     byte b1;
/*     */     
/*  47 */     for (b1 = -2; b1 <= 2; b1++) {
/*  48 */       for (byte b = -2; b <= 2; b++) {
/*  49 */         if (worldGenLevel.isEmptyBlock(blockPos1.offset(b1, -1, b)) && worldGenLevel.isEmptyBlock(blockPos1.offset(b1, -2, b))) {
/*  50 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  56 */     for (b1 = -2; b1 <= 0; b1++) {
/*  57 */       for (byte b = -2; b <= 2; b++) {
/*  58 */         for (byte b3 = -2; b3 <= 2; b3++) {
/*  59 */           worldGenLevel.setBlock(blockPos1.offset(b, b1, b3), this.sandstone, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  65 */     worldGenLevel.setBlock(blockPos1, this.water, 2);
/*  66 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  67 */       worldGenLevel.setBlock(blockPos1.relative(direction), this.water, 2);
/*     */     }
/*     */ 
/*     */     
/*  71 */     BlockPos blockPos2 = blockPos1.below();
/*  72 */     worldGenLevel.setBlock(blockPos2, this.sand, 2);
/*  73 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  74 */       worldGenLevel.setBlock(blockPos2.relative(direction), this.sand, 2);
/*     */     }
/*     */     
/*     */     byte b2;
/*  78 */     for (b2 = -2; b2 <= 2; b2++) {
/*  79 */       for (byte b = -2; b <= 2; b++) {
/*  80 */         if (b2 == -2 || b2 == 2 || b == -2 || b == 2) {
/*  81 */           worldGenLevel.setBlock(blockPos1.offset(b2, 1, b), this.sandstone, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     worldGenLevel.setBlock(blockPos1.offset(2, 1, 0), this.sandSlab, 2);
/*  87 */     worldGenLevel.setBlock(blockPos1.offset(-2, 1, 0), this.sandSlab, 2);
/*  88 */     worldGenLevel.setBlock(blockPos1.offset(0, 1, 2), this.sandSlab, 2);
/*  89 */     worldGenLevel.setBlock(blockPos1.offset(0, 1, -2), this.sandSlab, 2);
/*     */ 
/*     */     
/*  92 */     for (b2 = -1; b2 <= 1; b2++) {
/*  93 */       for (byte b = -1; b <= 1; b++) {
/*  94 */         if (b2 == 0 && b == 0) {
/*  95 */           worldGenLevel.setBlock(blockPos1.offset(b2, 4, b), this.sandstone, 2);
/*     */         } else {
/*  97 */           worldGenLevel.setBlock(blockPos1.offset(b2, 4, b), this.sandSlab, 2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 103 */     for (b2 = 1; b2 <= 3; b2++) {
/* 104 */       worldGenLevel.setBlock(blockPos1.offset(-1, b2, -1), this.sandstone, 2);
/* 105 */       worldGenLevel.setBlock(blockPos1.offset(-1, b2, 1), this.sandstone, 2);
/* 106 */       worldGenLevel.setBlock(blockPos1.offset(1, b2, -1), this.sandstone, 2);
/* 107 */       worldGenLevel.setBlock(blockPos1.offset(1, b2, 1), this.sandstone, 2);
/*     */     } 
/*     */     
/* 110 */     BlockPos blockPos3 = blockPos1;
/* 111 */     List<BlockPos> list = List.of(blockPos3, blockPos3.east(), blockPos3.south(), blockPos3.west(), blockPos3.north());
/*     */     
/* 113 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 114 */     placeSusSand(worldGenLevel, ((BlockPos)Util.getRandom(list, randomSource)).below(1));
/* 115 */     placeSusSand(worldGenLevel, ((BlockPos)Util.getRandom(list, randomSource)).below(2));
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   private static void placeSusSand(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos) {
/* 121 */     paramWorldGenLevel.setBlock(paramBlockPos, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 3);
/* 122 */     paramWorldGenLevel.getBlockEntity(paramBlockPos, BlockEntityType.BRUSHABLE_BLOCK).ifPresent(paramBrushableBlockEntity -> paramBrushableBlockEntity.setLootTable(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, paramBlockPos.asLong()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\DesertWellFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */