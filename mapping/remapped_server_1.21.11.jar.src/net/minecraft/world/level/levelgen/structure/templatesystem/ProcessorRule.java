/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*    */ 
/*    */ public class ProcessorRule
/*    */ {
/* 17 */   public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)RuleTest.CODEC.fieldOf("input_predicate").forGetter(()), (App)RuleTest.CODEC.fieldOf("location_predicate").forGetter(()), (App)PosRuleTest.CODEC.lenientOptionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(()), (App)BlockState.CODEC.fieldOf("output_state").forGetter(()), (App)RuleBlockEntityModifier.CODEC.lenientOptionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(())).apply((Applicative)paramInstance, ProcessorRule::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<ProcessorRule> CODEC;
/*    */ 
/*    */   
/*    */   private final RuleTest inputPredicate;
/*    */   
/*    */   private final RuleTest locPredicate;
/*    */   
/*    */   private final PosRuleTest posPredicate;
/*    */   
/*    */   private final BlockState outputState;
/*    */   
/*    */   private final RuleBlockEntityModifier blockEntityModifier;
/*    */ 
/*    */   
/*    */   public ProcessorRule(RuleTest paramRuleTest1, RuleTest paramRuleTest2, BlockState paramBlockState) {
/* 39 */     this(paramRuleTest1, paramRuleTest2, PosAlwaysTrueTest.INSTANCE, paramBlockState);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest paramRuleTest1, RuleTest paramRuleTest2, PosRuleTest paramPosRuleTest, BlockState paramBlockState) {
/* 43 */     this(paramRuleTest1, paramRuleTest2, paramPosRuleTest, paramBlockState, (RuleBlockEntityModifier)DEFAULT_BLOCK_ENTITY_MODIFIER);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest paramRuleTest1, RuleTest paramRuleTest2, PosRuleTest paramPosRuleTest, BlockState paramBlockState, RuleBlockEntityModifier paramRuleBlockEntityModifier) {
/* 47 */     this.inputPredicate = paramRuleTest1;
/* 48 */     this.locPredicate = paramRuleTest2;
/* 49 */     this.posPredicate = paramPosRuleTest;
/* 50 */     this.outputState = paramBlockState;
/* 51 */     this.blockEntityModifier = paramRuleBlockEntityModifier;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState paramBlockState1, BlockState paramBlockState2, BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, RandomSource paramRandomSource) {
/* 55 */     return (this.inputPredicate.test(paramBlockState1, paramRandomSource) && this.locPredicate.test(paramBlockState2, paramRandomSource) && this.posPredicate.test(paramBlockPos1, paramBlockPos2, paramBlockPos3, paramRandomSource));
/*    */   }
/*    */   
/*    */   public BlockState getOutputState() {
/* 59 */     return this.outputState;
/*    */   }
/*    */   
/*    */   public CompoundTag getOutputTag(RandomSource paramRandomSource, CompoundTag paramCompoundTag) {
/* 63 */     return this.blockEntityModifier.apply(paramRandomSource, paramCompoundTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProcessorRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */