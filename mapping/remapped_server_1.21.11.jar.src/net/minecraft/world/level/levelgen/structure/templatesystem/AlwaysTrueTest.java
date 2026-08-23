/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class AlwaysTrueTest extends RuleTest {
/*  8 */   public static final MapCodec<AlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 10 */   public static final AlwaysTrueTest INSTANCE = new AlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 22 */     return RuleTestType.ALWAYS_TRUE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AlwaysTrueTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */