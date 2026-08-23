/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface PosRuleTestType<P extends PosRuleTest> {
/*  8 */   public static final PosRuleTestType<PosAlwaysTrueTest> ALWAYS_TRUE_TEST = register("always_true", PosAlwaysTrueTest.CODEC);
/*  9 */   public static final PosRuleTestType<LinearPosTest> LINEAR_POS_TEST = register("linear_pos", LinearPosTest.CODEC);
/* 10 */   public static final PosRuleTestType<AxisAlignedLinearPosTest> AXIS_ALIGNED_LINEAR_POS_TEST = register("axis_aligned_linear_pos", AxisAlignedLinearPosTest.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends PosRuleTest> PosRuleTestType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 16 */     return (PosRuleTestType<P>)Registry.register(BuiltInRegistries.POS_RULE_TEST, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosRuleTestType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */