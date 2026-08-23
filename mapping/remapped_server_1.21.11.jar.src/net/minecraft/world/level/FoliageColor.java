/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class FoliageColor
/*    */ {
/*    */   public static final int FOLIAGE_EVERGREEN = -10380959;
/*    */   public static final int FOLIAGE_BIRCH = -8345771;
/*    */   public static final int FOLIAGE_DEFAULT = -12012264;
/*    */   public static final int FOLIAGE_MANGROVE = -7158200;
/*  9 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] paramArrayOfint) {
/* 12 */     pixels = paramArrayOfint;
/*    */   }
/*    */   
/*    */   public static int get(double paramDouble1, double paramDouble2) {
/* 16 */     return ColorMapColorUtil.get(paramDouble1, paramDouble2, pixels, -12012264);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\FoliageColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */