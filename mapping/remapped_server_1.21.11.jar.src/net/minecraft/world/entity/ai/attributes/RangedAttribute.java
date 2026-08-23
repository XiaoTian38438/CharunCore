/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class RangedAttribute extends Attribute {
/*    */   private final double minValue;
/*    */   private final double maxValue;
/*    */   
/*    */   public RangedAttribute(String paramString, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 10 */     super(paramString, paramDouble1);
/* 11 */     this.minValue = paramDouble2;
/* 12 */     this.maxValue = paramDouble3;
/*    */     
/* 14 */     if (paramDouble2 > paramDouble3) {
/* 15 */       throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
/*    */     }
/* 17 */     if (paramDouble1 < paramDouble2) {
/* 18 */       throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
/*    */     }
/* 20 */     if (paramDouble1 > paramDouble3) {
/* 21 */       throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
/*    */     }
/*    */   }
/*    */   
/*    */   public double getMinValue() {
/* 26 */     return this.minValue;
/*    */   }
/*    */   
/*    */   public double getMaxValue() {
/* 30 */     return this.maxValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public double sanitizeValue(double paramDouble) {
/* 35 */     if (Double.isNaN(paramDouble)) {
/* 36 */       return this.minValue;
/*    */     }
/* 38 */     return Mth.clamp(paramDouble, this.minValue, this.maxValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\RangedAttribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */