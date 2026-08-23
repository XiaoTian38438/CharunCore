/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ 
/*    */ 
/*    */ public class ProtectedBlockProcessor
/*    */   extends StructureProcessor
/*    */ {
/*    */   public final TagKey<Block> cannotReplace;
/*    */   public static final MapCodec<ProtectedBlockProcessor> CODEC;
/*    */   
/*    */   static {
/* 20 */     CODEC = TagKey.hashedCodec(Registries.BLOCK).xmap(ProtectedBlockProcessor::new, paramProtectedBlockProcessor -> paramProtectedBlockProcessor.cannotReplace).fieldOf("value");
/*    */   }
/*    */   public ProtectedBlockProcessor(TagKey<Block> paramTagKey) {
/* 23 */     this.cannotReplace = paramTagKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 28 */     if (Feature.isReplaceable(this.cannotReplace).test(paramLevelReader.getBlockState(paramStructureBlockInfo2.pos()))) {
/* 29 */       return paramStructureBlockInfo2;
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 36 */     return StructureProcessorType.PROTECTED_BLOCKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProtectedBlockProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */