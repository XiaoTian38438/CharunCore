/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MonsterRoomFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  27 */   private static final EntityType<?>[] MOBS = new EntityType[] { EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER };
/*  28 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public MonsterRoomFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/*  31 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/*  36 */     Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
/*  37 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  38 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  39 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  40 */     byte b1 = 3;
/*  41 */     int i = randomSource.nextInt(2) + 2;
/*  42 */     int j = -i - 1;
/*  43 */     int k = i + 1;
/*     */     
/*  45 */     byte b = -1;
/*  46 */     byte b2 = 4;
/*     */     
/*  48 */     int m = randomSource.nextInt(2) + 2;
/*  49 */     int n = -m - 1;
/*  50 */     int i1 = m + 1;
/*     */     
/*  52 */     byte b3 = 0; int i2;
/*  53 */     for (i2 = j; i2 <= k; i2++) {
/*  54 */       for (byte b4 = -1; b4 <= 4; b4++) {
/*  55 */         for (int i3 = n; i3 <= i1; i3++) {
/*  56 */           BlockPos blockPos1 = blockPos.offset(i2, b4, i3);
/*  57 */           boolean bool = worldGenLevel.getBlockState(blockPos1).isSolid();
/*     */           
/*  59 */           if (b4 == -1 && !bool) {
/*  60 */             return false;
/*     */           }
/*  62 */           if (b4 == 4 && !bool) {
/*  63 */             return false;
/*     */           }
/*     */           
/*  66 */           if ((i2 == j || i2 == k || i3 == n || i3 == i1) && 
/*  67 */             b4 == 0 && worldGenLevel.isEmptyBlock(blockPos1) && worldGenLevel.isEmptyBlock(blockPos1.above())) {
/*  68 */             b3++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  75 */     if (b3 < 1 || b3 > 5) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     for (i2 = j; i2 <= k; i2++) {
/*  80 */       for (byte b4 = 3; b4 >= -1; b4--) {
/*  81 */         for (int i3 = n; i3 <= i1; i3++) {
/*  82 */           BlockPos blockPos1 = blockPos.offset(i2, b4, i3);
/*     */           
/*  84 */           BlockState blockState = worldGenLevel.getBlockState(blockPos1);
/*  85 */           if (i2 == j || b4 == -1 || i3 == n || i2 == k || b4 == 4 || i3 == i1) {
/*  86 */             if (blockPos1.getY() >= worldGenLevel.getMinY() && !worldGenLevel.getBlockState(blockPos1.below()).isSolid()) {
/*  87 */               worldGenLevel.setBlock(blockPos1, AIR, 2);
/*  88 */             } else if (blockState.isSolid() && 
/*  89 */               !blockState.is(Blocks.CHEST)) {
/*  90 */               if (b4 == -1 && randomSource.nextInt(4) != 0) {
/*  91 */                 safeSetBlock(worldGenLevel, blockPos1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), predicate);
/*     */               } else {
/*  93 */                 safeSetBlock(worldGenLevel, blockPos1, Blocks.COBBLESTONE.defaultBlockState(), predicate);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*  98 */           } else if (!blockState.is(Blocks.CHEST) && !blockState.is(Blocks.SPAWNER)) {
/*  99 */             safeSetBlock(worldGenLevel, blockPos1, AIR, predicate);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 106 */     for (i2 = 0; i2 < 2; i2++) {
/* 107 */       for (byte b4 = 0; b4 < 3; b4++) {
/* 108 */         int i3 = blockPos.getX() + randomSource.nextInt(i * 2 + 1) - i;
/* 109 */         int i4 = blockPos.getY();
/* 110 */         int i5 = blockPos.getZ() + randomSource.nextInt(m * 2 + 1) - m;
/* 111 */         BlockPos blockPos1 = new BlockPos(i3, i4, i5);
/*     */         
/* 113 */         if (worldGenLevel.isEmptyBlock(blockPos1)) {
/*     */ 
/*     */ 
/*     */           
/* 117 */           byte b5 = 0;
/* 118 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 119 */             if (worldGenLevel.getBlockState(blockPos1.relative(direction)).isSolid()) {
/* 120 */               b5++;
/*     */             }
/*     */           } 
/*     */           
/* 124 */           if (b5 == 1) {
/*     */ 
/*     */ 
/*     */             
/* 128 */             safeSetBlock(worldGenLevel, blockPos1, StructurePiece.reorient((BlockGetter)worldGenLevel, blockPos1, Blocks.CHEST.defaultBlockState()), predicate);
/* 129 */             RandomizableContainer.setBlockEntityLootTable((BlockGetter)worldGenLevel, randomSource, blockPos1, BuiltInLootTables.SIMPLE_DUNGEON);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 135 */     safeSetBlock(worldGenLevel, blockPos, Blocks.SPAWNER.defaultBlockState(), predicate);
/* 136 */     BlockEntity blockEntity = worldGenLevel.getBlockEntity(blockPos);
/*     */     
/* 138 */     if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity)blockEntity;
/* 139 */       spawnerBlockEntity.setEntityId(randomEntityId(randomSource), randomSource); }
/*     */     else
/* 141 */     { LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", new Object[] { Integer.valueOf(blockPos.getX()), Integer.valueOf(blockPos.getY()), Integer.valueOf(blockPos.getZ()) }); }
/*     */ 
/*     */     
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   private EntityType<?> randomEntityId(RandomSource paramRandomSource) {
/* 148 */     return (EntityType)Util.getRandom((Object[])MOBS, paramRandomSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\MonsterRoomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */