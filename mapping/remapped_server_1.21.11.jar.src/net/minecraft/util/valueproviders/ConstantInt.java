/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ConstantInt extends IntProvider {
/*  8 */   public static final ConstantInt ZERO = new ConstantInt(0);
/*    */   
/* 10 */   public static final MapCodec<ConstantInt> CODEC = Codec.INT.fieldOf("value").xmap(ConstantInt::of, ConstantInt::getValue);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   public static ConstantInt of(int paramInt) {
/* 15 */     if (paramInt == 0) {
/* 16 */       return ZERO;
/*    */     }
/* 18 */     return new ConstantInt(paramInt);
/*    */   }
/*    */   
/*    */   private ConstantInt(int paramInt) {
/* 22 */     this.value = paramInt;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 26 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 31 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 36 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 41 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 46 */     return IntProviderType.CONSTANT;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return Integer.toString(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\ConstantInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */