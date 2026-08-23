/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class VecDeltaCodec
/*    */ {
/*    */   private static final double TRUNCATION_STEPS = 4096.0D;
/*  9 */   private Vec3 base = Vec3.ZERO;
/*    */   
/*    */   @VisibleForTesting
/*    */   static long encode(double paramDouble) {
/* 13 */     return Math.round(paramDouble * 4096.0D);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static double decode(long paramLong) {
/* 18 */     return paramLong / 4096.0D;
/*    */   }
/*    */   
/*    */   public Vec3 decode(long paramLong1, long paramLong2, long paramLong3) {
/* 22 */     if (paramLong1 == 0L && paramLong2 == 0L && paramLong3 == 0L) {
/* 23 */       return this.base;
/*    */     }
/* 25 */     double d1 = (paramLong1 == 0L) ? this.base.x : decode(encode(this.base.x) + paramLong1);
/* 26 */     double d2 = (paramLong2 == 0L) ? this.base.y : decode(encode(this.base.y) + paramLong2);
/* 27 */     double d3 = (paramLong3 == 0L) ? this.base.z : decode(encode(this.base.z) + paramLong3);
/* 28 */     return new Vec3(d1, d2, d3);
/*    */   }
/*    */   
/*    */   public long encodeX(Vec3 paramVec3) {
/* 32 */     return encode(paramVec3.x) - encode(this.base.x);
/*    */   }
/*    */   
/*    */   public long encodeY(Vec3 paramVec3) {
/* 36 */     return encode(paramVec3.y) - encode(this.base.y);
/*    */   }
/*    */   
/*    */   public long encodeZ(Vec3 paramVec3) {
/* 40 */     return encode(paramVec3.z) - encode(this.base.z);
/*    */   }
/*    */   
/*    */   public Vec3 delta(Vec3 paramVec3) {
/* 44 */     return paramVec3.subtract(this.base);
/*    */   }
/*    */   
/*    */   public void setBase(Vec3 paramVec3) {
/* 48 */     this.base = paramVec3;
/*    */   }
/*    */   
/*    */   public Vec3 getBase() {
/* 52 */     return this.base;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\VecDeltaCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */