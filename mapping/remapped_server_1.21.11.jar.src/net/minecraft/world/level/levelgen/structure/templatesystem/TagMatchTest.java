/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TagMatchTest extends RuleTest {
/*    */   public static final MapCodec<TagMatchTest> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag").xmap(TagMatchTest::new, paramTagMatchTest -> paramTagMatchTest.tag);
/*    */   }
/*    */   private final TagKey<Block> tag;
/*    */   
/*    */   public TagMatchTest(TagKey<Block> paramTagKey) {
/* 16 */     this.tag = paramTagKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 21 */     return paramBlockState.is(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 26 */     return RuleTestType.TAG_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\TagMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */