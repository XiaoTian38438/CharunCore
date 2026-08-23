/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface IntProviderType<P extends IntProvider> {
/*  8 */   public static final IntProviderType<ConstantInt> CONSTANT = register("constant", ConstantInt.CODEC);
/*  9 */   public static final IntProviderType<UniformInt> UNIFORM = register("uniform", UniformInt.CODEC);
/* 10 */   public static final IntProviderType<BiasedToBottomInt> BIASED_TO_BOTTOM = register("biased_to_bottom", BiasedToBottomInt.CODEC);
/* 11 */   public static final IntProviderType<ClampedInt> CLAMPED = register("clamped", ClampedInt.CODEC);
/* 12 */   public static final IntProviderType<WeightedListInt> WEIGHTED_LIST = register("weighted_list", WeightedListInt.CODEC);
/* 13 */   public static final IntProviderType<ClampedNormalInt> CLAMPED_NORMAL = register("clamped_normal", ClampedNormalInt.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends IntProvider> IntProviderType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 19 */     return (IntProviderType<P>)Registry.register(BuiltInRegistries.INT_PROVIDER_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\IntProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */