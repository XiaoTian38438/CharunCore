/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BonemealableBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class BoneMealItem extends Item {
/*     */   public static final int GRASS_SPREAD_WIDTH = 3;
/*     */   
/*     */   public BoneMealItem(Item.Properties paramProperties) {
/*  34 */     super(paramProperties);
/*     */   }
/*     */   public static final int GRASS_SPREAD_HEIGHT = 1; public static final int GRASS_COUNT_MULTIPLIER = 3;
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*  39 */     Level level = paramUseOnContext.getLevel();
/*  40 */     BlockPos blockPos1 = paramUseOnContext.getClickedPos();
/*  41 */     BlockPos blockPos2 = blockPos1.relative(paramUseOnContext.getClickedFace());
/*     */ 
/*     */     
/*  44 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/*  45 */     if (growCrop(itemStack, level, blockPos1)) {
/*  46 */       if (!level.isClientSide()) {
/*  47 */         itemStack.causeUseVibration((Entity)paramUseOnContext.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
/*  48 */         level.levelEvent(1505, blockPos1, 15);
/*     */       } 
/*  50 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  54 */     BlockState blockState = level.getBlockState(blockPos1);
/*  55 */     boolean bool = blockState.isFaceSturdy((BlockGetter)level, blockPos1, paramUseOnContext.getClickedFace());
/*  56 */     if (bool && 
/*  57 */       growWaterPlant(itemStack, level, blockPos2, paramUseOnContext.getClickedFace())) {
/*  58 */       if (!level.isClientSide()) {
/*  59 */         itemStack.causeUseVibration((Entity)paramUseOnContext.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
/*  60 */         level.levelEvent(1505, blockPos2, 15);
/*     */       } 
/*  62 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static boolean growCrop(ItemStack paramItemStack, Level paramLevel, BlockPos paramBlockPos) {
/*  70 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */     
/*  72 */     Block block = blockState.getBlock(); if (block instanceof BonemealableBlock) { BonemealableBlock bonemealableBlock = (BonemealableBlock)block;
/*     */       
/*  74 */       if (bonemealableBlock.isValidBonemealTarget((LevelReader)paramLevel, paramBlockPos, blockState)) {
/*  75 */         if (paramLevel instanceof ServerLevel) {
/*  76 */           if (bonemealableBlock.isBonemealSuccess(paramLevel, paramLevel.random, paramBlockPos, blockState)) {
/*  77 */             bonemealableBlock.performBonemeal((ServerLevel)paramLevel, paramLevel.random, paramBlockPos, blockState);
/*     */           }
/*  79 */           paramItemStack.shrink(1);
/*     */         } 
/*  81 */         return true;
/*     */       }  }
/*     */     
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean growWaterPlant(ItemStack paramItemStack, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  88 */     if (!paramLevel.getBlockState(paramBlockPos).is(Blocks.WATER) || paramLevel.getFluidState(paramBlockPos).getAmount() != 8) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     if (!(paramLevel instanceof ServerLevel)) {
/*  93 */       return true;
/*     */     }
/*     */     
/*  96 */     RandomSource randomSource = paramLevel.getRandom();
/*     */     
/*     */     byte b;
/*  99 */     label49: for (b = 0; b < ''; b++) {
/* 100 */       BlockPos blockPos = paramBlockPos;
/* 101 */       BlockState blockState = Blocks.SEAGRASS.defaultBlockState();
/*     */       
/* 103 */       for (byte b1 = 0; b1 < b / 16; b1++) {
/* 104 */         blockPos = blockPos.offset(randomSource.nextInt(3) - 1, (randomSource.nextInt(3) - 1) * randomSource.nextInt(3) / 2, randomSource.nextInt(3) - 1);
/*     */         
/* 106 */         if (paramLevel.getBlockState(blockPos).isCollisionShapeFullBlock((BlockGetter)paramLevel, blockPos)) {
/*     */           continue label49;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 112 */       Holder holder = paramLevel.getBiome(blockPos);
/* 113 */       if (holder.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
/* 114 */         if (b == 0 && paramDirection != null && paramDirection.getAxis().isHorizontal()) {
/*     */           
/* 116 */           blockState = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, paramLevel.random).map(paramHolder -> ((Block)paramHolder.value()).defaultBlockState()).orElse(blockState);
/* 117 */           if (blockState.hasProperty((Property)BaseCoralWallFanBlock.FACING)) {
/* 118 */             blockState = (BlockState)blockState.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)paramDirection);
/*     */           }
/* 120 */         } else if (randomSource.nextInt(4) == 0) {
/* 121 */           blockState = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.UNDERWATER_BONEMEALS, paramLevel.random).map(paramHolder -> ((Block)paramHolder.value()).defaultBlockState()).orElse(blockState);
/*     */         } 
/*     */       }
/*     */       
/* 125 */       if (blockState.is(BlockTags.WALL_CORALS, paramBlockStateBase -> paramBlockStateBase.hasProperty((Property)BaseCoralWallFanBlock.FACING))) {
/* 126 */         byte b2 = 0;
/* 127 */         while (!blockState.canSurvive((LevelReader)paramLevel, blockPos) && b2 < 4) {
/* 128 */           blockState = (BlockState)blockState.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(randomSource));
/* 129 */           b2++;
/*     */         } 
/*     */       } 
/*     */       
/* 133 */       if (blockState.canSurvive((LevelReader)paramLevel, blockPos)) {
/*     */ 
/*     */ 
/*     */         
/* 137 */         BlockState blockState1 = paramLevel.getBlockState(blockPos);
/* 138 */         if (blockState1.is(Blocks.WATER) && paramLevel.getFluidState(blockPos).getAmount() == 8) {
/* 139 */           paramLevel.setBlock(blockPos, blockState, 3);
/*     */         
/*     */         }
/* 142 */         else if (blockState1.is(Blocks.SEAGRASS) && ((BonemealableBlock)Blocks.SEAGRASS).isValidBonemealTarget((LevelReader)paramLevel, blockPos, blockState1) && randomSource.nextInt(10) == 0) {
/* 143 */           ((BonemealableBlock)Blocks.SEAGRASS).performBonemeal((ServerLevel)paramLevel, randomSource, blockPos, blockState1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     paramItemStack.shrink(1);
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   public static void addGrowthParticles(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt) {
/* 153 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 154 */     Block block = blockState.getBlock(); if (block instanceof BonemealableBlock) { BonemealableBlock bonemealableBlock = (BonemealableBlock)block;
/* 155 */       BlockPos blockPos = bonemealableBlock.getParticlePos(paramBlockPos);
/* 156 */       switch (bonemealableBlock.getType()) {
/*     */         case NEIGHBOR_SPREADER:
/* 158 */           ParticleUtils.spawnParticles(paramLevelAccessor, blockPos, paramInt * 3, 3.0D, 1.0D, false, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); break;
/*     */         case GROWER:
/* 160 */           ParticleUtils.spawnParticleInBlock(paramLevelAccessor, blockPos, paramInt, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); break;
/*     */       }  }
/* 162 */     else if (blockState.is(Blocks.WATER))
/* 163 */     { ParticleUtils.spawnParticles(paramLevelAccessor, paramBlockPos, paramInt * 3, 3.0D, 1.0D, false, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BoneMealItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */