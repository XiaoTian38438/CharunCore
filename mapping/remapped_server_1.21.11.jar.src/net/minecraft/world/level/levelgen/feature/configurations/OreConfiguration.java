/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class OreConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("size").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(())).apply((Applicative)paramInstance, OreConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<OreConfiguration> CODEC;
/*    */   
/*    */   public final List<TargetBlockState> targetStates;
/*    */   public final int size;
/*    */   public final float discardChanceOnAirExposure;
/*    */   
/*    */   public OreConfiguration(List<TargetBlockState> paramList, int paramInt, float paramFloat) {
/* 23 */     this.size = paramInt;
/* 24 */     this.targetStates = paramList;
/* 25 */     this.discardChanceOnAirExposure = paramFloat;
/*    */   }
/*    */   
/*    */   public OreConfiguration(List<TargetBlockState> paramList, int paramInt) {
/* 29 */     this(paramList, paramInt, 0.0F);
/*    */   }
/*    */   
/*    */   public OreConfiguration(RuleTest paramRuleTest, BlockState paramBlockState, int paramInt, float paramFloat) {
/* 33 */     this((List<TargetBlockState>)ImmutableList.of(new TargetBlockState(paramRuleTest, paramBlockState)), paramInt, paramFloat);
/*    */   }
/*    */   
/*    */   public OreConfiguration(RuleTest paramRuleTest, BlockState paramBlockState, int paramInt) {
/* 37 */     this((List<TargetBlockState>)ImmutableList.of(new TargetBlockState(paramRuleTest, paramBlockState)), paramInt, 0.0F);
/*    */   }
/*    */   
/*    */   public static TargetBlockState target(RuleTest paramRuleTest, BlockState paramBlockState) {
/* 41 */     return new TargetBlockState(paramRuleTest, paramBlockState);
/*    */   }
/*    */   public static class TargetBlockState { public static final Codec<TargetBlockState> CODEC;
/*    */     static {
/* 45 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)RuleTest.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)param1Instance, TargetBlockState::new));
/*    */     }
/*    */ 
/*    */     
/*    */     public final RuleTest target;
/*    */     
/*    */     public final BlockState state;
/*    */     
/*    */     TargetBlockState(RuleTest param1RuleTest, BlockState param1BlockState) {
/* 54 */       this.target = param1RuleTest;
/* 55 */       this.state = param1BlockState;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */