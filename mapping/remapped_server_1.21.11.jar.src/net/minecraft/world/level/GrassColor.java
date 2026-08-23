/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class GrassColor {
/*  4 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] paramArrayOfint) {
/*  7 */     pixels = paramArrayOfint;
/*    */   }
/*    */   
/*    */   public static int get(double paramDouble1, double paramDouble2) {
/* 11 */     return ColorMapColorUtil.get(paramDouble1, paramDouble2, pixels, -65281);
/*    */   }
/*    */   
/*    */   public static int getDefaultColor() {
/* 15 */     return get(0.5D, 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\GrassColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */