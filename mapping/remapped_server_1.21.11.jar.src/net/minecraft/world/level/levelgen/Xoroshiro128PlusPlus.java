/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.stream.LongStream;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ public class Xoroshiro128PlusPlus
/*    */ {
/*    */   private long seedLo;
/*    */   private long seedHi;
/*    */   public static final Codec<Xoroshiro128PlusPlus> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = Codec.LONG_STREAM.comapFlatMap(paramLongStream -> Util.fixedSize(paramLongStream, 2).map(()), paramXoroshiro128PlusPlus -> LongStream.of(new long[] { paramXoroshiro128PlusPlus.seedLo, paramXoroshiro128PlusPlus.seedHi }));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Xoroshiro128PlusPlus(RandomSupport.Seed128bit paramSeed128bit) {
/* 22 */     this(paramSeed128bit.seedLo(), paramSeed128bit.seedHi());
/*    */   }
/*    */   
/*    */   public Xoroshiro128PlusPlus(long paramLong1, long paramLong2) {
/* 26 */     this.seedLo = paramLong1;
/* 27 */     this.seedHi = paramLong2;
/* 28 */     if ((this.seedLo | this.seedHi) == 0L) {
/* 29 */       this.seedLo = -7046029254386353131L;
/* 30 */       this.seedHi = 7640891576956012809L;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long nextLong() {
/* 36 */     long l1 = this.seedLo;
/* 37 */     long l2 = this.seedHi;
/* 38 */     long l3 = Long.rotateLeft(l1 + l2, 17) + l1;
/*    */     
/* 40 */     l2 ^= l1;
/* 41 */     this.seedLo = Long.rotateLeft(l1, 49) ^ l2 ^ l2 << 21L;
/* 42 */     this.seedHi = Long.rotateLeft(l2, 28);
/*    */     
/* 44 */     return l3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\Xoroshiro128PlusPlus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */