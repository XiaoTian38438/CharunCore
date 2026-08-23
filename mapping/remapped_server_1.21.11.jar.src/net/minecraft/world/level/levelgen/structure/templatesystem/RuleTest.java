/*   */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ import net.minecraft.util.RandomSource;
/*   */ import net.minecraft.world.level.block.state.BlockState;
/*   */ 
/*   */ public abstract class RuleTest {
/* 9 */   public static final Codec<RuleTest> CODEC = BuiltInRegistries.RULE_TEST.byNameCodec().dispatch("predicate_type", RuleTest::getType, RuleTestType::codec);
/*   */   
/*   */   public abstract boolean test(BlockState paramBlockState, RandomSource paramRandomSource);
/*   */   
/*   */   protected abstract RuleTestType<?> getType();
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */