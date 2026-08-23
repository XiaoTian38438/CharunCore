/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceBlockConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(())).apply((Applicative)paramInstance, ReplaceBlockConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final Codec<ReplaceBlockConfiguration> CODEC;
/*    */   public final List<OreConfiguration.TargetBlockState> targetStates;
/*    */   
/*    */   public ReplaceBlockConfiguration(BlockState paramBlockState1, BlockState paramBlockState2) {
/* 19 */     this((List<OreConfiguration.TargetBlockState>)ImmutableList.of(OreConfiguration.target((RuleTest)new BlockStateMatchTest(paramBlockState1), paramBlockState2)));
/*    */   }
/*    */   
/*    */   public ReplaceBlockConfiguration(List<OreConfiguration.TargetBlockState> paramList) {
/* 23 */     this.targetStates = paramList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceBlockConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */