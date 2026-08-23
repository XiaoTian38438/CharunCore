/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public interface ColorMapColorUtil
/*    */ {
/*    */   static int get(double paramDouble1, double paramDouble2, int[] paramArrayOfint, int paramInt) {
/*  6 */     paramDouble2 *= paramDouble1;
/*  7 */     int i = (int)((1.0D - paramDouble1) * 255.0D);
/*  8 */     int j = (int)((1.0D - paramDouble2) * 255.0D);
/*  9 */     int k = j << 8 | i;
/* 10 */     if (k >= paramArrayOfint.length) {
/* 11 */       return paramInt;
/*    */     }
/* 13 */     return paramArrayOfint[k];
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ColorMapColorUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */