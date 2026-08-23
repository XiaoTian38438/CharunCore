/*   */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import net.minecraft.core.BlockPos;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ import net.minecraft.util.RandomSource;
/*   */ 
/*   */ public abstract class PosRuleTest {
/* 9 */   public static final Codec<PosRuleTest> CODEC = BuiltInRegistries.POS_RULE_TEST.byNameCodec().dispatch("predicate_type", PosRuleTest::getType, PosRuleTestType::codec);
/*   */   
/*   */   public abstract boolean test(BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, RandomSource paramRandomSource);
/*   */   
/*   */   protected abstract PosRuleTestType<?> getType();
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosRuleTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */