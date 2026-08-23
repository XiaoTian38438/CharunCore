/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateMatchTest extends RuleTest {
/*    */   public static final MapCodec<BlockStateMatchTest> CODEC;
/*    */   
/*    */   static {
/*  8 */     CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, paramBlockStateMatchTest -> paramBlockStateMatchTest.blockState);
/*    */   }
/*    */   private final BlockState blockState;
/*    */   
/*    */   public BlockStateMatchTest(BlockState paramBlockState) {
/* 13 */     this.blockState = paramBlockState;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 18 */     return (paramBlockState == this.blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 23 */     return RuleTestType.BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */