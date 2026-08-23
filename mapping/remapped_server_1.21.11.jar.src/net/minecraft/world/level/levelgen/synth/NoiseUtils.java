/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoiseUtils
/*    */ {
/*    */   public static double biasTowardsExtreme(double paramDouble1, double paramDouble2) {
/* 14 */     return paramDouble1 + Math.sin(Math.PI * paramDouble1) * paramDouble2 / Math.PI;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder paramStringBuilder, double paramDouble1, double paramDouble2, double paramDouble3, byte[] paramArrayOfbyte) {
/* 19 */     paramStringBuilder.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)paramDouble1), Float.valueOf((float)paramDouble2), Float.valueOf((float)paramDouble3), Byte.valueOf(paramArrayOfbyte[0]), Byte.valueOf(paramArrayOfbyte[255]) }));
/*    */   }
/*    */ 
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder paramStringBuilder, double paramDouble1, double paramDouble2, double paramDouble3, int[] paramArrayOfint) {
/* 24 */     paramStringBuilder.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)paramDouble1), Float.valueOf((float)paramDouble2), Float.valueOf((float)paramDouble3), Integer.valueOf(paramArrayOfint[0]), Integer.valueOf(paramArrayOfint[255]) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\synth\NoiseUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */