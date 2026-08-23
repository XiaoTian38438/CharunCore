/*    */ package net.minecraft.util.valueproviders;
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
/*    */ public class UniformInt extends IntProvider {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)paramInstance, UniformInt::new)).validate(paramUniformInt -> (paramUniformInt.maxInclusive < paramUniformInt.minInclusive) ? DataResult.error(()) : DataResult.success(paramUniformInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<UniformInt> CODEC;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   private UniformInt(int paramInt1, int paramInt2) {
/* 25 */     this.minInclusive = paramInt1;
/* 26 */     this.maxInclusive = paramInt2;
/*    */   }
/*    */   
/*    */   public static UniformInt of(int paramInt1, int paramInt2) {
/* 30 */     return new UniformInt(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 35 */     return Mth.randomBetweenInclusive(paramRandomSource, this.minInclusive, this.maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 40 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 45 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 50 */     return IntProviderType.UNIFORM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\UniformInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */