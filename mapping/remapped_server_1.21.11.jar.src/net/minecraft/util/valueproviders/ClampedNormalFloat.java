/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedNormalFloat
/*    */   extends FloatProvider {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("mean").forGetter(()), (App)Codec.FLOAT.fieldOf("deviation").forGetter(()), (App)Codec.FLOAT.fieldOf("min").forGetter(()), (App)Codec.FLOAT.fieldOf("max").forGetter(())).apply((Applicative)paramInstance, ClampedNormalFloat::new)).validate(paramClampedNormalFloat -> (paramClampedNormalFloat.max < paramClampedNormalFloat.min) ? DataResult.error(()) : DataResult.success(paramClampedNormalFloat));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedNormalFloat> CODEC;
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final float min;
/*    */   private final float max;
/*    */   
/*    */   public static ClampedNormalFloat of(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 29 */     return new ClampedNormalFloat(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
/*    */   }
/*    */   
/*    */   private ClampedNormalFloat(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 33 */     this.mean = paramFloat1;
/* 34 */     this.deviation = paramFloat2;
/* 35 */     this.min = paramFloat3;
/* 36 */     this.max = paramFloat4;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource paramRandomSource) {
/* 41 */     return sample(paramRandomSource, this.mean, this.deviation, this.min, this.max);
/*    */   }
/*    */   
/*    */   public static float sample(RandomSource paramRandomSource, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 45 */     return Mth.clamp(Mth.normal(paramRandomSource, paramFloat1, paramFloat2), paramFloat3, paramFloat4);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 50 */     return this.min;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 55 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 60 */     return FloatProviderType.CLAMPED_NORMAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\ClampedNormalFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */