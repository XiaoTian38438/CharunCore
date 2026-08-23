/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
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
/*    */ public abstract class StructureProcessor
/*    */ {
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 20 */     return paramStructureBlockInfo2;
/*    */   }
/*    */   
/*    */   protected abstract StructureProcessorType<?> getType();
/*    */   
/*    */   public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor paramServerLevelAccessor, BlockPos paramBlockPos1, BlockPos paramBlockPos2, List<StructureTemplate.StructureBlockInfo> paramList1, List<StructureTemplate.StructureBlockInfo> paramList2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 26 */     return paramList2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */