/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class JigsawReplacementProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 19 */   public static final MapCodec<JigsawReplacementProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 21 */   public static final JigsawReplacementProcessor INSTANCE = new JigsawReplacementProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 28 */     BlockState blockState2, blockState1 = paramStructureBlockInfo2.state();
/* 29 */     if (!blockState1.is(Blocks.JIGSAW) || SharedConstants.DEBUG_KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN) {
/* 30 */       return paramStructureBlockInfo2;
/*    */     }
/*    */     
/* 33 */     if (paramStructureBlockInfo2.nbt() == null) {
/* 34 */       LOGGER.warn("Jigsaw block at {} is missing nbt, will not replace", paramBlockPos1);
/* 35 */       return paramStructureBlockInfo2;
/*    */     } 
/*    */     
/* 38 */     String str = paramStructureBlockInfo2.nbt().getStringOr("final_state", "minecraft:air");
/*    */     
/*    */     try {
/* 41 */       BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(paramLevelReader.holderLookup(Registries.BLOCK), str, true);
/* 42 */       blockState2 = blockResult.blockState();
/* 43 */     } catch (CommandSyntaxException commandSyntaxException) {
/* 44 */       LOGGER.error("Failed to parse jigsaw replacement state '{}' at {}: {}", new Object[] { str, paramBlockPos1, commandSyntaxException.getMessage() });
/* 45 */       return null;
/*    */     } 
/* 47 */     if (blockState2.is(Blocks.STRUCTURE_VOID)) {
/* 48 */       return null;
/*    */     }
/* 50 */     return new StructureTemplate.StructureBlockInfo(paramStructureBlockInfo2.pos(), blockState2, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 55 */     return StructureProcessorType.JIGSAW_REPLACEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\JigsawReplacementProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */