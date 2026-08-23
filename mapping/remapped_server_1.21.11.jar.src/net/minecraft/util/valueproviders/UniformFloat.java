/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class UniformFloat extends FloatProvider {
/*    */   public static final MapCodec<UniformFloat> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("min_inclusive").forGetter(()), (App)Codec.FLOAT.fieldOf("max_exclusive").forGetter(())).apply((Applicative)paramInstance, UniformFloat::new)).validate(paramUniformFloat -> (paramUniformFloat.maxExclusive <= paramUniformFloat.minInclusive) ? DataResult.error(()) : DataResult.success(paramUniformFloat));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final float minInclusive;
/*    */   
/*    */   private final float maxExclusive;
/*    */ 
/*    */   
/*    */   private UniformFloat(float paramFloat1, float paramFloat2) {
/* 28 */     this.minInclusive = paramFloat1;
/* 29 */     this.maxExclusive = paramFloat2;
/*    */   }
/*    */   
/*    */   public static UniformFloat of(float paramFloat1, float paramFloat2) {
/* 33 */     if (paramFloat2 <= paramFloat1) {
/* 34 */       throw new IllegalArgumentException("Max must exceed min");
/*    */     }
/* 36 */     return new UniformFloat(paramFloat1, paramFloat2);
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource paramRandomSource) {
/* 41 */     return Mth.randomBetween(paramRandomSource, this.minInclusive, this.maxExclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 46 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 51 */     return this.maxExclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 56 */     return FloatProviderType.UNIFORM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "[" + this.minInclusive + "-" + this.maxExclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\UniformFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */