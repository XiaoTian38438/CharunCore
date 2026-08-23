/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class IntProvider {
/* 10 */   private static final Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.INT, BuiltInRegistries.INT_PROVIDER_TYPE
/*    */       
/* 12 */       .byNameCodec().dispatch(IntProvider::getType, IntProviderType::codec));
/*    */   static {
/* 14 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(paramEither -> (IntProvider)paramEither.map(ConstantInt::of, ()), paramIntProvider -> (paramIntProvider.getType() == IntProviderType.CONSTANT) ? Either.left(Integer.valueOf(((ConstantInt)paramIntProvider).getValue())) : Either.right(paramIntProvider));
/*    */   }
/*    */   
/*    */   public static final Codec<IntProvider> CODEC;
/*    */   
/*    */   public static Codec<IntProvider> codec(int paramInt1, int paramInt2) {
/* 20 */     return validateCodec(paramInt1, paramInt2, CODEC);
/*    */   }
/*    */   
/*    */   public static <T extends IntProvider> Codec<T> validateCodec(int paramInt1, int paramInt2, Codec<T> paramCodec) {
/* 24 */     return paramCodec.validate(paramIntProvider -> validate(paramInt1, paramInt2, paramIntProvider));
/*    */   }
/*    */   
/*    */   private static <T extends IntProvider> DataResult<T> validate(int paramInt1, int paramInt2, T paramT) {
/* 28 */     if (paramT.getMinValue() < paramInt1) {
/* 29 */       return DataResult.error(() -> "Value provider too low: " + paramInt + " [" + paramIntProvider.getMinValue() + "-" + paramIntProvider.getMaxValue() + "]");
/*    */     }
/* 31 */     if (paramT.getMaxValue() > paramInt2) {
/* 32 */       return DataResult.error(() -> "Value provider too high: " + paramInt + " [" + paramIntProvider.getMinValue() + "-" + paramIntProvider.getMaxValue() + "]");
/*    */     }
/* 34 */     return DataResult.success(paramT);
/*    */   }
/*    */   
/* 37 */   public static final Codec<IntProvider> NON_NEGATIVE_CODEC = codec(0, 2147483647);
/* 38 */   public static final Codec<IntProvider> POSITIVE_CODEC = codec(1, 2147483647);
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource);
/*    */   
/*    */   public abstract int getMinValue();
/*    */   
/*    */   public abstract int getMaxValue();
/*    */   
/*    */   public abstract IntProviderType<?> getType();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\IntProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */