/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface FloatProviderType<P extends FloatProvider> {
/*  8 */   public static final FloatProviderType<ConstantFloat> CONSTANT = register("constant", ConstantFloat.CODEC);
/*  9 */   public static final FloatProviderType<UniformFloat> UNIFORM = register("uniform", UniformFloat.CODEC);
/* 10 */   public static final FloatProviderType<ClampedNormalFloat> CLAMPED_NORMAL = register("clamped_normal", ClampedNormalFloat.CODEC);
/* 11 */   public static final FloatProviderType<TrapezoidFloat> TRAPEZOID = register("trapezoid", TrapezoidFloat.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends FloatProvider> FloatProviderType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 17 */     return (FloatProviderType<P>)Registry.register(BuiltInRegistries.FLOAT_PROVIDER_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\FloatProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */