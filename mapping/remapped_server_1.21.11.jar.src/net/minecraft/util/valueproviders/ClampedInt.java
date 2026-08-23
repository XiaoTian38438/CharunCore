/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedInt extends IntProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)IntProvider.CODEC.fieldOf("source").forGetter(()), (App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)paramInstance, ClampedInt::new)).validate(paramClampedInt -> (paramClampedInt.maxInclusive < paramClampedInt.minInclusive) ? DataResult.error(()) : DataResult.success(paramClampedInt));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedInt> CODEC;
/*    */   
/*    */   private final IntProvider source;
/*    */   
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static ClampedInt of(IntProvider paramIntProvider, int paramInt1, int paramInt2) {
/* 27 */     return new ClampedInt(paramIntProvider, paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   public ClampedInt(IntProvider paramIntProvider, int paramInt1, int paramInt2) {
/* 31 */     this.source = paramIntProvider;
/* 32 */     this.minInclusive = paramInt1;
/* 33 */     this.maxInclusive = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 38 */     return Mth.clamp(this.source.sample(paramRandomSource), this.minInclusive, this.maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 43 */     return Math.max(this.minInclusive, this.source.getMinValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 48 */     return Math.min(this.maxInclusive, this.source.getMaxValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 53 */     return IntProviderType.CLAMPED;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\ClampedInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */