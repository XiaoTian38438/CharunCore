/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LavaSubmergedBlockProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 15 */   public static final MapCodec<LavaSubmergedBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/* 16 */   public static final LavaSubmergedBlockProcessor INSTANCE = new LavaSubmergedBlockProcessor();
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 20 */     BlockPos blockPos = paramStructureBlockInfo2.pos();
/* 21 */     boolean bool = paramLevelReader.getBlockState(blockPos).is(Blocks.LAVA);
/* 22 */     if (bool && !Block.isShapeFullBlock(paramStructureBlockInfo2.state().getShape((BlockGetter)paramLevelReader, blockPos))) {
/* 23 */       return new StructureTemplate.StructureBlockInfo(blockPos, Blocks.LAVA.defaultBlockState(), paramStructureBlockInfo2.nbt());
/*    */     }
/* 25 */     return paramStructureBlockInfo2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 30 */     return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LavaSubmergedBlockProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */