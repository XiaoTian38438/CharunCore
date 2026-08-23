/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface LerpFunction<T> {
/*    */   static LerpFunction<Float> ofFloat() {
/*  8 */     return Mth::lerp;
/*    */   }
/*    */   
/*    */   static LerpFunction<Float> ofDegrees(float paramFloat) {
/* 12 */     return (paramFloat2, paramFloat3, paramFloat4) -> {
/*    */         float f = Mth.wrapDegrees(paramFloat4.floatValue() - paramFloat3.floatValue());
/*    */         return (Math.abs(f) >= paramFloat1) ? paramFloat4 : Float.valueOf(paramFloat3.floatValue() + paramFloat2 * f);
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> LerpFunction<T> ofConstant() {
/* 22 */     return (paramFloat, paramObject1, paramObject2) -> paramObject1;
/*    */   }
/*    */   
/*    */   static <T> LerpFunction<T> ofStep(float paramFloat) {
/* 26 */     return (paramFloat2, paramObject1, paramObject2) -> (paramFloat2 >= paramFloat1) ? paramObject2 : paramObject1;
/*    */   }
/*    */   
/*    */   static LerpFunction<Integer> ofColor() {
/* 30 */     return ARGB::srgbLerp;
/*    */   }
/*    */   
/*    */   T apply(float paramFloat, T paramT1, T paramT2);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\LerpFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */