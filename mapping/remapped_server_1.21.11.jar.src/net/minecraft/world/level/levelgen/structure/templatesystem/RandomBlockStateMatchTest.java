/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockStateMatchTest extends RuleTest {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockState.CODEC.fieldOf("block_state").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)paramInstance, RandomBlockStateMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomBlockStateMatchTest> CODEC;
/*    */   private final BlockState blockState;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockStateMatchTest(BlockState paramBlockState, float paramFloat) {
/* 19 */     this.blockState = paramBlockState;
/* 20 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 25 */     return (paramBlockState == this.blockState && paramRandomSource.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 30 */     return RuleTestType.RANDOM_BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */