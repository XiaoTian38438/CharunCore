/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BiasedToBottomInt extends IntProvider {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)paramInstance, BiasedToBottomInt::new)).validate(paramBiasedToBottomInt -> (paramBiasedToBottomInt.maxInclusive < paramBiasedToBottomInt.minInclusive) ? DataResult.error(()) : DataResult.success(paramBiasedToBottomInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BiasedToBottomInt> CODEC;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   private BiasedToBottomInt(int paramInt1, int paramInt2) {
/* 24 */     this.minInclusive = paramInt1;
/* 25 */     this.maxInclusive = paramInt2;
/*    */   }
/*    */   
/*    */   public static BiasedToBottomInt of(int paramInt1, int paramInt2) {
/* 29 */     return new BiasedToBottomInt(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 34 */     return this.minInclusive + paramRandomSource.nextInt(paramRandomSource.nextInt(this.maxInclusive - this.minInclusive + 1) + 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 39 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 44 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 49 */     return IntProviderType.BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\BiasedToBottomInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */