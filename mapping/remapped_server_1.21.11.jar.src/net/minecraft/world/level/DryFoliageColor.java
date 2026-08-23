/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class DryFoliageColor
/*    */ {
/*    */   public static final int FOLIAGE_DRY_DEFAULT = -10732494;
/*  6 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] paramArrayOfint) {
/*  9 */     pixels = paramArrayOfint;
/*    */   }
/*    */   
/*    */   public static int get(double paramDouble1, double paramDouble2) {
/* 13 */     return ColorMapColorUtil.get(paramDouble1, paramDouble2, pixels, -10732494);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\DryFoliageColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */