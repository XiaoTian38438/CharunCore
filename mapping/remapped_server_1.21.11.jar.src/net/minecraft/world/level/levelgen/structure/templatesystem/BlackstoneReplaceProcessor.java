/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SlabBlock;
/*    */ import net.minecraft.world.level.block.StairBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlackstoneReplaceProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 20 */   public static final MapCodec<BlackstoneReplaceProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();
/*    */   
/*    */   private final Map<Block, Block> replacements;
/*    */   
/*    */   private BlackstoneReplaceProcessor() {
/* 27 */     this.replacements = (Map<Block, Block>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*    */           paramHashMap.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
/*    */           paramHashMap.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
/*    */           paramHashMap.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
/*    */           paramHashMap.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           paramHashMap.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           paramHashMap.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           paramHashMap.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           paramHashMap.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
/*    */           paramHashMap.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           paramHashMap.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           paramHashMap.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           paramHashMap.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           paramHashMap.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           paramHashMap.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           paramHashMap.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           paramHashMap.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           paramHashMap.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           paramHashMap.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           paramHashMap.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           paramHashMap.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           paramHashMap.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
/*    */           paramHashMap.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*    */           paramHashMap.put(Blocks.IRON_BARS, Blocks.IRON_CHAIN);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 67 */     Block block = this.replacements.get(paramStructureBlockInfo2.state().getBlock());
/* 68 */     if (block == null) {
/* 69 */       return paramStructureBlockInfo2;
/*    */     }
/* 71 */     BlockState blockState1 = paramStructureBlockInfo2.state();
/* 72 */     BlockState blockState2 = block.defaultBlockState();
/* 73 */     if (blockState1.hasProperty((Property)StairBlock.FACING)) {
/* 74 */       blockState2 = (BlockState)blockState2.setValue((Property)StairBlock.FACING, blockState1.getValue((Property)StairBlock.FACING));
/*    */     }
/* 76 */     if (blockState1.hasProperty((Property)StairBlock.HALF)) {
/* 77 */       blockState2 = (BlockState)blockState2.setValue((Property)StairBlock.HALF, blockState1.getValue((Property)StairBlock.HALF));
/*    */     }
/* 79 */     if (blockState1.hasProperty((Property)SlabBlock.TYPE)) {
/* 80 */       blockState2 = (BlockState)blockState2.setValue((Property)SlabBlock.TYPE, blockState1.getValue((Property)SlabBlock.TYPE));
/*    */     }
/* 82 */     return new StructureTemplate.StructureBlockInfo(paramStructureBlockInfo2.pos(), blockState2, paramStructureBlockInfo2.nbt());
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 87 */     return StructureProcessorType.BLACKSTONE_REPLACE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlackstoneReplaceProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */