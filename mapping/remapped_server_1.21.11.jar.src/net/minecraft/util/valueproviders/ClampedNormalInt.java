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
/*    */ public class ClampedNormalInt
/*    */   extends IntProvider {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("mean").forGetter(()), (App)Codec.FLOAT.fieldOf("deviation").forGetter(()), (App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)paramInstance, ClampedNormalInt::new)).validate(paramClampedNormalInt -> (paramClampedNormalInt.maxInclusive < paramClampedNormalInt.minInclusive) ? DataResult.error(()) : DataResult.success(paramClampedNormalInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedNormalInt> CODEC;
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static ClampedNormalInt of(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
/* 29 */     return new ClampedNormalInt(paramFloat1, paramFloat2, paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   private ClampedNormalInt(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
/* 33 */     this.mean = paramFloat1;
/* 34 */     this.deviation = paramFloat2;
/* 35 */     this.minInclusive = paramInt1;
/* 36 */     this.maxInclusive = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 41 */     return sample(paramRandomSource, this.mean, this.deviation, this.minInclusive, this.maxInclusive);
/*    */   }
/*    */   
/*    */   public static int sample(RandomSource paramRandomSource, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 45 */     return (int)Mth.clamp(Mth.normal(paramRandomSource, paramFloat1, paramFloat2), paramFloat3, paramFloat4);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 50 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 55 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 60 */     return IntProviderType.CLAMPED_NORMAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\ClampedNormalInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */