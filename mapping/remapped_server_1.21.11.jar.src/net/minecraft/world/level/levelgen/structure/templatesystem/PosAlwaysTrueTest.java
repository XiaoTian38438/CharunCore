/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class PosAlwaysTrueTest extends PosRuleTest {
/*  8 */   public static final MapCodec<PosAlwaysTrueTest> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 10 */   public static final PosAlwaysTrueTest INSTANCE = new PosAlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, RandomSource paramRandomSource) {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 22 */     return PosRuleTestType.ALWAYS_TRUE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosAlwaysTrueTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */