/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class XoroshiroRandomSource implements RandomSource {
/*     */   private static final float FLOAT_UNIT = 5.9604645E-8F;
/*     */   private static final double DOUBLE_UNIT = 1.1102230246251565E-16D;
/*     */   public static final Codec<XoroshiroRandomSource> CODEC;
/*     */   private Xoroshiro128PlusPlus randomNumberGenerator;
/*     */   
/*     */   static {
/*  15 */     CODEC = Xoroshiro128PlusPlus.CODEC.xmap(paramXoroshiro128PlusPlus -> new XoroshiroRandomSource(paramXoroshiro128PlusPlus), paramXoroshiroRandomSource -> paramXoroshiroRandomSource.randomNumberGenerator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  21 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*     */   
/*     */   public XoroshiroRandomSource(long paramLong) {
/*  24 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(paramLong));
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(RandomSupport.Seed128bit paramSeed128bit) {
/*  28 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(paramSeed128bit);
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(long paramLong1, long paramLong2) {
/*  32 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(paramLong1, paramLong2);
/*     */   }
/*     */   
/*     */   private XoroshiroRandomSource(Xoroshiro128PlusPlus paramXoroshiro128PlusPlus) {
/*  36 */     this.randomNumberGenerator = paramXoroshiro128PlusPlus;
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource fork() {
/*  41 */     return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionalRandomFactory forkPositional() {
/*  46 */     return new XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/*  51 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(paramLong));
/*  52 */     this.gaussianSource.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt() {
/*  57 */     return (int)this.randomNumberGenerator.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt(int paramInt) {
/*  62 */     if (paramInt <= 0) {
/*  63 */       throw new IllegalArgumentException("Bound must be positive");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     long l1 = Integer.toUnsignedLong(nextInt());
/*     */ 
/*     */     
/*  72 */     long l2 = l1 * paramInt;
/*     */     
/*  74 */     long l3 = l2 & 0xFFFFFFFFL;
/*     */ 
/*     */     
/*  77 */     if (l3 < paramInt) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       int i = Integer.remainderUnsigned((paramInt ^ 0xFFFFFFFF) + 1, paramInt);
/*  83 */       while (l3 < i) {
/*     */         
/*  85 */         l1 = Integer.toUnsignedLong(nextInt());
/*  86 */         l2 = l1 * paramInt;
/*  87 */         l3 = l2 & 0xFFFFFFFFL;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     long l4 = l2 >> 32L;
/*     */     
/*  93 */     return (int)l4;
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  98 */     return this.randomNumberGenerator.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() {
/* 103 */     return ((this.randomNumberGenerator.nextLong() & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/* 108 */     return (float)nextBits(24) * 5.9604645E-8F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/* 113 */     return nextBits(53) * 1.1102230246251565E-16D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextGaussian() {
/* 118 */     return this.gaussianSource.nextGaussian();
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCount(int paramInt) {
/* 123 */     for (byte b = 0; b < paramInt; b++) {
/* 124 */       this.randomNumberGenerator.nextLong();
/*     */     }
/*     */   }
/*     */   
/*     */   private long nextBits(int paramInt) {
/* 129 */     return this.randomNumberGenerator.nextLong() >>> 64 - paramInt;
/*     */   }
/*     */   
/*     */   public static class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
/*     */     private final long seedLo;
/*     */     private final long seedHi;
/*     */     
/*     */     public XoroshiroPositionalRandomFactory(long param1Long1, long param1Long2) {
/* 137 */       this.seedLo = param1Long1;
/* 138 */       this.seedHi = param1Long2;
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource at(int param1Int1, int param1Int2, int param1Int3) {
/* 143 */       long l1 = Mth.getSeed(param1Int1, param1Int2, param1Int3);
/* 144 */       long l2 = l1 ^ this.seedLo;
/* 145 */       return new XoroshiroRandomSource(l2, this.seedHi);
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource fromHashOf(String param1String) {
/* 150 */       RandomSupport.Seed128bit seed128bit = RandomSupport.seedFromHashOf(param1String);
/* 151 */       return new XoroshiroRandomSource(seed128bit.xor(this.seedLo, this.seedHi));
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource fromSeed(long param1Long) {
/* 156 */       return new XoroshiroRandomSource(param1Long ^ this.seedLo, param1Long ^ this.seedHi);
/*     */     }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     public void parityConfigString(StringBuilder param1StringBuilder) {
/* 162 */       param1StringBuilder.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */