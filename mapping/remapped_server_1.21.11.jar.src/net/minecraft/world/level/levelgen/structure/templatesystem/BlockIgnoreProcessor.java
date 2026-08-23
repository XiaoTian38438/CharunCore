/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockIgnoreProcessor
/*    */   extends StructureProcessor
/*    */ {
/*    */   public static final MapCodec<BlockIgnoreProcessor> CODEC;
/*    */   
/*    */   static {
/* 20 */     CODEC = BlockState.CODEC.xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).listOf().fieldOf("blocks").xmap(BlockIgnoreProcessor::new, paramBlockIgnoreProcessor -> paramBlockIgnoreProcessor.toIgnore);
/*    */   }
/* 22 */   public static final BlockIgnoreProcessor STRUCTURE_BLOCK = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.STRUCTURE_BLOCK));
/* 23 */   public static final BlockIgnoreProcessor AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR));
/* 24 */   public static final BlockIgnoreProcessor STRUCTURE_AND_AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
/*    */   
/*    */   private final ImmutableList<Block> toIgnore;
/*    */   
/*    */   public BlockIgnoreProcessor(List<Block> paramList) {
/* 29 */     this.toIgnore = ImmutableList.copyOf(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 34 */     if (this.toIgnore.contains(paramStructureBlockInfo2.state().getBlock())) {
/* 35 */       return null;
/*    */     }
/* 37 */     return paramStructureBlockInfo2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 42 */     return StructureProcessorType.BLOCK_IGNORE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockIgnoreProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */