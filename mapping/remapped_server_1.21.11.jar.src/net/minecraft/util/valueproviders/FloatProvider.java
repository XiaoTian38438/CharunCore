/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public abstract class FloatProvider implements SampledFloat {
/*  9 */   private static final Codec<Either<Float, FloatProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.FLOAT, BuiltInRegistries.FLOAT_PROVIDER_TYPE
/*    */       
/* 11 */       .byNameCodec().dispatch(FloatProvider::getType, FloatProviderType::codec));
/*    */   static {
/* 13 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(paramEither -> (FloatProvider)paramEither.map(ConstantFloat::of, ()), paramFloatProvider -> (paramFloatProvider.getType() == FloatProviderType.CONSTANT) ? Either.left(Float.valueOf(((ConstantFloat)paramFloatProvider).getValue())) : Either.right(paramFloatProvider));
/*    */   }
/*    */   
/*    */   public static final Codec<FloatProvider> CODEC;
/*    */   
/*    */   public static Codec<FloatProvider> codec(float paramFloat1, float paramFloat2) {
/* 19 */     return CODEC.validate(paramFloatProvider -> (paramFloatProvider.getMinValue() < paramFloat1) ? DataResult.error(()) : ((paramFloatProvider.getMaxValue() > paramFloat2) ? DataResult.error(()) : DataResult.success(paramFloatProvider)));
/*    */   }
/*    */   
/*    */   public abstract float getMinValue();
/*    */   
/*    */   public abstract float getMaxValue();
/*    */   
/*    */   public abstract FloatProviderType<?> getType();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\FloatProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */