/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMatchTest extends RuleTest {
/*    */   public static final MapCodec<BlockMatchTest> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").xmap(BlockMatchTest::new, paramBlockMatchTest -> paramBlockMatchTest.block);
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public BlockMatchTest(Block paramBlock) {
/* 15 */     this.block = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 20 */     return paramBlockState.is(this.block);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 25 */     return RuleTestType.BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */