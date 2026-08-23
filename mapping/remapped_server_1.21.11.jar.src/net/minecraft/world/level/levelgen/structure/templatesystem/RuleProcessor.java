/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RuleProcessor extends StructureProcessor {
/*    */   public static final MapCodec<RuleProcessor> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, paramRuleProcessor -> paramRuleProcessor.rules);
/*    */   }
/*    */   private final ImmutableList<ProcessorRule> rules;
/*    */   
/*    */   public RuleProcessor(List<? extends ProcessorRule> paramList) {
/* 20 */     this.rules = ImmutableList.copyOf(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 25 */     RandomSource randomSource = RandomSource.create(Mth.getSeed((Vec3i)paramStructureBlockInfo2.pos()));
/* 26 */     BlockState blockState = paramLevelReader.getBlockState(paramStructureBlockInfo2.pos());
/* 27 */     for (UnmodifiableIterator<ProcessorRule> unmodifiableIterator = this.rules.iterator(); unmodifiableIterator.hasNext(); ) { ProcessorRule processorRule = unmodifiableIterator.next();
/* 28 */       if (processorRule.test(paramStructureBlockInfo2.state(), blockState, paramStructureBlockInfo1.pos(), paramStructureBlockInfo2.pos(), paramBlockPos2, randomSource)) {
/* 29 */         return new StructureTemplate.StructureBlockInfo(paramStructureBlockInfo2.pos(), processorRule.getOutputState(), processorRule.getOutputTag(randomSource, paramStructureBlockInfo2.nbt()));
/*    */       } }
/*    */     
/* 32 */     return paramStructureBlockInfo2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 37 */     return StructureProcessorType.RULE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */