/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TrapezoidFloat
/*    */   extends FloatProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("min").forGetter(()), (App)Codec.FLOAT.fieldOf("max").forGetter(()), (App)Codec.FLOAT.fieldOf("plateau").forGetter(())).apply((Applicative)paramInstance, TrapezoidFloat::new)).validate(paramTrapezoidFloat -> (paramTrapezoidFloat.max < paramTrapezoidFloat.min) ? DataResult.error(()) : ((paramTrapezoidFloat.plateau > paramTrapezoidFloat.max - paramTrapezoidFloat.min) ? DataResult.error(()) : DataResult.success(paramTrapezoidFloat)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<TrapezoidFloat> CODEC;
/*    */   
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   private final float plateau;
/*    */ 
/*    */   
/*    */   public static TrapezoidFloat of(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 30 */     return new TrapezoidFloat(paramFloat1, paramFloat2, paramFloat3);
/*    */   }
/*    */   
/*    */   private TrapezoidFloat(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 34 */     this.min = paramFloat1;
/* 35 */     this.max = paramFloat2;
/* 36 */     this.plateau = paramFloat3;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource paramRandomSource) {
/* 41 */     float f1 = this.max - this.min;
/* 42 */     float f2 = (f1 - this.plateau) / 2.0F;
/* 43 */     float f3 = f1 - f2;
/*    */     
/* 45 */     return this.min + paramRandomSource.nextFloat() * f3 + paramRandomSource.nextFloat() * f2;
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
/* 60 */     return FloatProviderType.TRAPEZOID;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "trapezoid(" + this.plateau + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\TrapezoidFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */