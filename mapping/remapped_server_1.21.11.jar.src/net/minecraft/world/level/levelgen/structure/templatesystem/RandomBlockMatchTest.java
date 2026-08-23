/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockMatchTest extends RuleTest {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)paramInstance, RandomBlockMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomBlockMatchTest> CODEC;
/*    */   private final Block block;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockMatchTest(Block paramBlock, float paramFloat) {
/* 21 */     this.block = paramBlock;
/* 22 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 27 */     return (paramBlockState.is(this.block) && paramRandomSource.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 32 */     return RuleTestType.RANDOM_BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */