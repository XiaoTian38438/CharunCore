/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
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
/*    */ 
/*    */ 
/*    */ public class TargetBlockState
/*    */ {
/*    */   public static final Codec<TargetBlockState> CODEC;
/*    */   public final RuleTest target;
/*    */   public final BlockState state;
/*    */   
/*    */   static {
/* 45 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)RuleTest.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)paramInstance, TargetBlockState::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   TargetBlockState(RuleTest paramRuleTest, BlockState paramBlockState) {
/* 54 */     this.target = paramRuleTest;
/* 55 */     this.state = paramBlockState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration$TargetBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */