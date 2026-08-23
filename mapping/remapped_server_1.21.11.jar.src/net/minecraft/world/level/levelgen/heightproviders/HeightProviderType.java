/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface HeightProviderType<P extends HeightProvider> {
/*  8 */   public static final HeightProviderType<ConstantHeight> CONSTANT = register("constant", ConstantHeight.CODEC);
/*  9 */   public static final HeightProviderType<UniformHeight> UNIFORM = register("uniform", UniformHeight.CODEC);
/* 10 */   public static final HeightProviderType<BiasedToBottomHeight> BIASED_TO_BOTTOM = register("biased_to_bottom", BiasedToBottomHeight.CODEC);
/* 11 */   public static final HeightProviderType<VeryBiasedToBottomHeight> VERY_BIASED_TO_BOTTOM = register("very_biased_to_bottom", VeryBiasedToBottomHeight.CODEC);
/* 12 */   public static final HeightProviderType<TrapezoidHeight> TRAPEZOID = register("trapezoid", TrapezoidHeight.CODEC);
/* 13 */   public static final HeightProviderType<WeightedListHeight> WEIGHTED_LIST = register("weighted_list", WeightedListHeight.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   private static <P extends HeightProvider> HeightProviderType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 18 */     return (HeightProviderType<P>)Registry.register(BuiltInRegistries.HEIGHT_PROVIDER_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\HeightProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */