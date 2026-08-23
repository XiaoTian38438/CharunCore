/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MarsagliaPolarGaussian
/*    */ {
/*    */   public final RandomSource randomSource;
/*    */   private double nextNextGaussian;
/*    */   private boolean haveNextNextGaussian;
/*    */   
/*    */   public MarsagliaPolarGaussian(RandomSource paramRandomSource) {
/* 13 */     this.randomSource = paramRandomSource;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 17 */     this.haveNextNextGaussian = false;
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/*    */     double d1, d2, d3;
/* 22 */     if (this.haveNextNextGaussian) {
/* 23 */       this.haveNextNextGaussian = false;
/* 24 */       return this.nextNextGaussian;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 33 */       d1 = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 34 */       d2 = 2.0D * this.randomSource.nextDouble() - 1.0D;
/* 35 */       d3 = Mth.square(d1) + Mth.square(d2);
/* 36 */     } while (d3 >= 1.0D || d3 == 0.0D);
/*    */     
/* 38 */     double d4 = Math.sqrt(-2.0D * Math.log(d3) / d3);
/*    */     
/* 40 */     this.nextNextGaussian = d2 * d4;
/* 41 */     this.haveNextNextGaussian = true;
/* 42 */     return d1 * d4;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\MarsagliaPolarGaussian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */