/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockAgeProcessor extends StructureProcessor {
/*     */   public static final MapCodec<BlockAgeProcessor> CODEC;
/*     */   
/*     */   static {
/*  19 */     CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockAgeProcessor::new, paramBlockAgeProcessor -> Float.valueOf(paramBlockAgeProcessor.mossiness));
/*     */   }
/*     */   private static final float PROBABILITY_OF_REPLACING_FULL_BLOCK = 0.5F;
/*     */   private static final float PROBABILITY_OF_REPLACING_STAIRS = 0.5F;
/*     */   private static final float PROBABILITY_OF_REPLACING_OBSIDIAN = 0.15F;
/*  24 */   private static final BlockState[] NON_MOSSY_REPLACEMENTS = new BlockState[] { Blocks.STONE_SLAB
/*  25 */       .defaultBlockState(), Blocks.STONE_BRICK_SLAB
/*  26 */       .defaultBlockState() };
/*     */   
/*     */   private final float mossiness;
/*     */ 
/*     */   
/*     */   public BlockAgeProcessor(float paramFloat) {
/*  32 */     this.mossiness = paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/*  37 */     RandomSource randomSource = paramStructurePlaceSettings.getRandom(paramStructureBlockInfo2.pos());
/*     */     
/*  39 */     BlockState blockState1 = paramStructureBlockInfo2.state();
/*  40 */     BlockPos blockPos = paramStructureBlockInfo2.pos();
/*  41 */     BlockState blockState2 = null;
/*  42 */     if (blockState1.is(Blocks.STONE_BRICKS) || blockState1.is(Blocks.STONE) || blockState1.is(Blocks.CHISELED_STONE_BRICKS)) {
/*  43 */       blockState2 = maybeReplaceFullStoneBlock(randomSource);
/*  44 */     } else if (blockState1.is(BlockTags.STAIRS)) {
/*  45 */       blockState2 = maybeReplaceStairs(blockState1, randomSource);
/*  46 */     } else if (blockState1.is(BlockTags.SLABS)) {
/*  47 */       blockState2 = maybeReplaceSlab(blockState1, randomSource);
/*  48 */     } else if (blockState1.is(BlockTags.WALLS)) {
/*  49 */       blockState2 = maybeReplaceWall(blockState1, randomSource);
/*  50 */     } else if (blockState1.is(Blocks.OBSIDIAN)) {
/*  51 */       blockState2 = maybeReplaceObsidian(randomSource);
/*     */     } 
/*  53 */     if (blockState2 != null) {
/*  54 */       return new StructureTemplate.StructureBlockInfo(blockPos, blockState2, paramStructureBlockInfo2.nbt());
/*     */     }
/*  56 */     return paramStructureBlockInfo2;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceFullStoneBlock(RandomSource paramRandomSource) {
/*  60 */     if (paramRandomSource.nextFloat() >= 0.5F) {
/*  61 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  65 */     BlockState[] arrayOfBlockState1 = { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(paramRandomSource, Blocks.STONE_BRICK_STAIRS) };
/*     */ 
/*     */ 
/*     */     
/*  69 */     BlockState[] arrayOfBlockState2 = { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(paramRandomSource, Blocks.MOSSY_STONE_BRICK_STAIRS) };
/*     */ 
/*     */     
/*  72 */     return getRandomBlock(paramRandomSource, arrayOfBlockState1, arrayOfBlockState2);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceStairs(BlockState paramBlockState, RandomSource paramRandomSource) {
/*  76 */     if (paramRandomSource.nextFloat() >= 0.5F) {
/*  77 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     BlockState[] arrayOfBlockState = { Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(paramBlockState), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
/*     */ 
/*     */     
/*  85 */     return getRandomBlock(paramRandomSource, NON_MOSSY_REPLACEMENTS, arrayOfBlockState);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceSlab(BlockState paramBlockState, RandomSource paramRandomSource) {
/*  89 */     if (paramRandomSource.nextFloat() < this.mossiness) {
/*  90 */       return Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(paramBlockState);
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceWall(BlockState paramBlockState, RandomSource paramRandomSource) {
/*  96 */     if (paramRandomSource.nextFloat() < this.mossiness) {
/*  97 */       return Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(paramBlockState);
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceObsidian(RandomSource paramRandomSource) {
/* 103 */     if (paramRandomSource.nextFloat() < 0.15F) {
/* 104 */       return Blocks.CRYING_OBSIDIAN.defaultBlockState();
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockState getRandomFacingStairs(RandomSource paramRandomSource, Block paramBlock) {
/* 110 */     return (BlockState)((BlockState)paramBlock.defaultBlockState()
/* 111 */       .setValue((Property)StairBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource)))
/* 112 */       .setValue((Property)StairBlock.HALF, (Comparable)Util.getRandom((Object[])Half.values(), paramRandomSource));
/*     */   }
/*     */   
/*     */   private BlockState getRandomBlock(RandomSource paramRandomSource, BlockState[] paramArrayOfBlockState1, BlockState[] paramArrayOfBlockState2) {
/* 116 */     if (paramRandomSource.nextFloat() < this.mossiness) {
/* 117 */       return getRandomBlock(paramRandomSource, paramArrayOfBlockState2);
/*     */     }
/* 119 */     return getRandomBlock(paramRandomSource, paramArrayOfBlockState1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockState getRandomBlock(RandomSource paramRandomSource, BlockState[] paramArrayOfBlockState) {
/* 124 */     return paramArrayOfBlockState[paramRandomSource.nextInt(paramArrayOfBlockState.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected StructureProcessorType<?> getType() {
/* 129 */     return StructureProcessorType.BLOCK_AGE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockAgeProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */