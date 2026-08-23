/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LpVec3
/*    */ {
/*    */   private static final int DATA_BITS = 15;
/*    */   private static final int DATA_BITS_MASK = 32767;
/*    */   private static final double MAX_QUANTIZED_VALUE = 32766.0D;
/*    */   private static final int SCALE_BITS = 2;
/*    */   private static final int SCALE_BITS_MASK = 3;
/*    */   private static final int CONTINUATION_FLAG = 4;
/*    */   private static final int X_OFFSET = 3;
/*    */   private static final int Y_OFFSET = 18;
/*    */   private static final int Z_OFFSET = 33;
/*    */   public static final double ABS_MAX_VALUE = 1.7179869183E10D;
/*    */   public static final double ABS_MIN_VALUE = 3.051944088384301E-5D;
/*    */   
/*    */   public static boolean hasContinuationBit(int paramInt) {
/* 24 */     return ((paramInt & 0x4) == 4);
/*    */   }
/*    */   
/*    */   public static Vec3 read(ByteBuf paramByteBuf) {
/* 28 */     short s1 = paramByteBuf.readUnsignedByte();
/* 29 */     if (s1 == 0) {
/* 30 */       return Vec3.ZERO;
/*    */     }
/*    */     
/* 33 */     short s2 = paramByteBuf.readUnsignedByte();
/* 34 */     long l1 = paramByteBuf.readUnsignedInt();
/* 35 */     long l2 = l1 << 16L | (s2 << 8) | s1;
/* 36 */     long l3 = (s1 & 0x3);
/*    */     
/* 38 */     if (hasContinuationBit(s1)) {
/* 39 */       l3 |= (VarInt.read(paramByteBuf) & 0xFFFFFFFFL) << 2L;
/*    */     }
/* 41 */     return new Vec3(
/* 42 */         unpack(l2 >> 3L) * l3, 
/* 43 */         unpack(l2 >> 18L) * l3, 
/* 44 */         unpack(l2 >> 33L) * l3);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void write(ByteBuf paramByteBuf, Vec3 paramVec3) {
/* 49 */     double d1 = sanitize(paramVec3.x);
/* 50 */     double d2 = sanitize(paramVec3.y);
/* 51 */     double d3 = sanitize(paramVec3.z);
/* 52 */     double d4 = Mth.absMax(d1, Mth.absMax(d2, d3));
/* 53 */     if (d4 < 3.051944088384301E-5D) {
/* 54 */       paramByteBuf.writeByte(0);
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     long l1 = Mth.ceilLong(d4);
/* 59 */     boolean bool = ((l1 & 0x3L) != l1) ? true : false;
/* 60 */     long l2 = bool ? (l1 & 0x3L | 0x4L) : l1;
/* 61 */     long l3 = pack(d1 / l1) << 3L;
/* 62 */     long l4 = pack(d2 / l1) << 18L;
/* 63 */     long l5 = pack(d3 / l1) << 33L;
/*    */     
/* 65 */     long l6 = l2 | l3 | l4 | l5;
/* 66 */     paramByteBuf.writeByte((byte)(int)l6);
/* 67 */     paramByteBuf.writeByte((byte)(int)(l6 >> 8L));
/* 68 */     paramByteBuf.writeInt((int)(l6 >> 16L));
/*    */     
/* 70 */     if (bool) {
/* 71 */       VarInt.write(paramByteBuf, (int)(l1 >> 2L));
/*    */     }
/*    */   }
/*    */   
/*    */   private static double sanitize(double paramDouble) {
/* 76 */     return Double.isNaN(paramDouble) ? 0.0D : Math.clamp(paramDouble, -1.7179869183E10D, 1.7179869183E10D);
/*    */   }
/*    */   
/*    */   private static long pack(double paramDouble) {
/* 80 */     return Math.round((paramDouble * 0.5D + 0.5D) * 32766.0D);
/*    */   }
/*    */   
/*    */   private static double unpack(long paramLong) {
/* 84 */     return Math.min((paramLong & 0x7FFFL), 32766.0D) * 2.0D / 32766.0D - 1.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\LpVec3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */