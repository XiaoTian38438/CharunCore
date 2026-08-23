/*     */ package net.minecraft.world.level.block.sounds;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ public class AmbientDesertBlockSoundsPlayer
/*     */ {
/*     */   private static final int IDLE_SOUND_CHANCE = 2100;
/*     */   private static final int DRY_GRASS_SOUND_CHANCE = 200;
/*     */   private static final int DEAD_BUSH_SOUND_CHANCE = 130;
/*     */   private static final int DEAD_BUSH_SOUND_BADLANDS_DECREASED_CHANCE = 3;
/*     */   private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
/*     */   private static final int SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK = 8;
/*     */   private static final int SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK = 5;
/*     */   private static final int HORIZONTAL_DIRECTIONS = 4;
/*     */   
/*     */   public static void playAmbientSandSounds(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  28 */     if (!paramLevel.getBlockState(paramBlockPos.above()).is(Blocks.AIR)) {
/*     */       return;
/*     */     }
/*     */     
/*  32 */     if (paramRandomSource.nextInt(2100) == 0 && 
/*  33 */       shouldPlayAmbientSandSound(paramLevel, paramBlockPos)) {
/*  34 */       paramLevel.playLocalSound(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), SoundEvents.SAND_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void playAmbientDryGrassSounds(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  40 */     if (paramRandomSource.nextInt(200) == 0 && 
/*  41 */       shouldPlayDesertDryVegetationBlockSounds(paramLevel, paramBlockPos.below())) {
/*  42 */       paramLevel.playPlayerSound(SoundEvents.DRY_GRASS, SoundSource.AMBIENT, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void playAmbientDeadBushSounds(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  48 */     if (paramRandomSource.nextInt(130) == 0) {
/*  49 */       BlockState blockState = paramLevel.getBlockState(paramBlockPos.below());
/*  50 */       if ((blockState.is(Blocks.RED_SAND) || blockState.is(BlockTags.TERRACOTTA)) && paramRandomSource.nextInt(3) != 0) {
/*     */         return;
/*     */       }
/*  53 */       if (shouldPlayDesertDryVegetationBlockSounds(paramLevel, paramBlockPos.below())) {
/*  54 */         paramLevel.playLocalSound(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), SoundEvents.DEAD_BUSH_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean shouldPlayDesertDryVegetationBlockSounds(Level paramLevel, BlockPos paramBlockPos) {
/*  60 */     return (paramLevel.getBlockState(paramBlockPos).is(BlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS) && paramLevel.getBlockState(paramBlockPos.below()).is(BlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS));
/*     */   }
/*     */   
/*     */   private static boolean shouldPlayAmbientSandSound(Level paramLevel, BlockPos paramBlockPos) {
/*  64 */     byte b1 = 0;
/*  65 */     byte b2 = 0;
/*  66 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/*  68 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  69 */       mutableBlockPos.set((Vec3i)paramBlockPos).move(direction, 8);
/*     */       
/*  71 */       if (columnContainsTriggeringBlock(paramLevel, mutableBlockPos) && b1++ >= 3) {
/*  72 */         return true;
/*     */       }
/*     */       
/*  75 */       b2++;
/*  76 */       int i = 4 - b2;
/*  77 */       int j = i + b1;
/*  78 */       boolean bool = (j >= 3) ? true : false;
/*     */       
/*  80 */       if (!bool) {
/*  81 */         return false;
/*     */       }
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean columnContainsTriggeringBlock(Level paramLevel, BlockPos.MutableBlockPos paramMutableBlockPos) {
/*  88 */     int i = paramLevel.getHeight(Heightmap.Types.WORLD_SURFACE, (BlockPos)paramMutableBlockPos) - 1;
/*     */     
/*  90 */     if (Math.abs(i - paramMutableBlockPos.getY()) <= 5) {
/*  91 */       boolean bool = paramLevel.getBlockState((BlockPos)paramMutableBlockPos.setY(i + 1)).isAir();
/*  92 */       return (bool && canTriggerAmbientDesertSandSounds(paramLevel.getBlockState((BlockPos)paramMutableBlockPos.setY(i))));
/*     */     } 
/*     */     
/*  95 */     paramMutableBlockPos.move(Direction.UP, 6);
/*  96 */     BlockState blockState = paramLevel.getBlockState((BlockPos)paramMutableBlockPos);
/*  97 */     paramMutableBlockPos.move(Direction.DOWN);
/*     */     
/*  99 */     for (byte b = 0; b < 10; b++) {
/* 100 */       BlockState blockState1 = paramLevel.getBlockState((BlockPos)paramMutableBlockPos);
/* 101 */       if (blockState.isAir() && canTriggerAmbientDesertSandSounds(blockState1)) {
/* 102 */         return true;
/*     */       }
/* 104 */       blockState = blockState1;
/* 105 */       paramMutableBlockPos.move(Direction.DOWN);
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean canTriggerAmbientDesertSandSounds(BlockState paramBlockState) {
/* 111 */     return paramBlockState.is(BlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\sounds\AmbientDesertBlockSoundsPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */