/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MultipliedFloats
/*    */   implements SampledFloat {
/*    */   private final SampledFloat[] values;
/*    */   
/*    */   public MultipliedFloats(SampledFloat... paramVarArgs) {
/* 11 */     this.values = paramVarArgs;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource paramRandomSource) {
/* 16 */     float f = 1.0F;
/* 17 */     for (SampledFloat sampledFloat : this.values) {
/* 18 */       f *= sampledFloat.sample(paramRandomSource);
/*    */     }
/* 20 */     return f;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "MultipliedFloats" + Arrays.toString((Object[])this.values);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\MultipliedFloats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */