/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.hash.Hashing;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.util.LinearCongruentialGenerator;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BiomeManager
/*     */ {
/*  12 */   public static final int CHUNK_CENTER_QUART = QuartPos.fromBlock(8);
/*     */   
/*     */   private static final int ZOOM_BITS = 2;
/*     */   
/*     */   private static final int ZOOM = 4;
/*     */   private static final int ZOOM_MASK = 3;
/*     */   private final NoiseBiomeSource noiseBiomeSource;
/*     */   private final long biomeZoomSeed;
/*     */   
/*     */   public BiomeManager(NoiseBiomeSource paramNoiseBiomeSource, long paramLong) {
/*  22 */     this.noiseBiomeSource = paramNoiseBiomeSource;
/*  23 */     this.biomeZoomSeed = paramLong;
/*     */   }
/*     */   
/*     */   public static long obfuscateSeed(long paramLong) {
/*  27 */     return Hashing.sha256().hashLong(paramLong).asLong();
/*     */   }
/*     */   
/*     */   public BiomeManager withDifferentSource(NoiseBiomeSource paramNoiseBiomeSource) {
/*  31 */     return new BiomeManager(paramNoiseBiomeSource, this.biomeZoomSeed);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getBiome(BlockPos paramBlockPos) {
/*  35 */     int i = paramBlockPos.getX() - 2;
/*  36 */     int j = paramBlockPos.getY() - 2;
/*  37 */     int k = paramBlockPos.getZ() - 2;
/*     */     
/*  39 */     int m = i >> 2;
/*  40 */     int n = j >> 2;
/*  41 */     int i1 = k >> 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     double d1 = (i & 0x3) / 4.0D;
/*  47 */     double d2 = (j & 0x3) / 4.0D;
/*  48 */     double d3 = (k & 0x3) / 4.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int i2 = 0;
/*  54 */     double d4 = Double.POSITIVE_INFINITY; int i3;
/*  55 */     for (i3 = 0; i3 < 8; i3++) {
/*  56 */       boolean bool1 = ((i3 & 0x4) == 0) ? true : false;
/*  57 */       boolean bool2 = ((i3 & 0x2) == 0) ? true : false;
/*  58 */       boolean bool3 = ((i3 & 0x1) == 0) ? true : false;
/*     */       
/*  60 */       int i6 = bool1 ? m : (m + 1);
/*  61 */       int i7 = bool2 ? n : (n + 1);
/*  62 */       int i8 = bool3 ? i1 : (i1 + 1);
/*     */       
/*  64 */       double d5 = bool1 ? d1 : (d1 - 1.0D);
/*  65 */       double d6 = bool2 ? d2 : (d2 - 1.0D);
/*  66 */       double d7 = bool3 ? d3 : (d3 - 1.0D);
/*     */       
/*  68 */       double d8 = getFiddledDistance(this.biomeZoomSeed, i6, i7, i8, d5, d6, d7);
/*  69 */       if (d4 > d8) {
/*  70 */         i2 = i3;
/*  71 */         d4 = d8;
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     i3 = ((i2 & 0x4) == 0) ? m : (m + 1);
/*  76 */     int i4 = ((i2 & 0x2) == 0) ? n : (n + 1);
/*  77 */     int i5 = ((i2 & 0x1) == 0) ? i1 : (i1 + 1);
/*     */     
/*  79 */     return this.noiseBiomeSource.getNoiseBiome(i3, i4, i5);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiomeAtPosition(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  83 */     int i = QuartPos.fromBlock(Mth.floor(paramDouble1));
/*  84 */     int j = QuartPos.fromBlock(Mth.floor(paramDouble2));
/*  85 */     int k = QuartPos.fromBlock(Mth.floor(paramDouble3));
/*  86 */     return getNoiseBiomeAtQuart(i, j, k);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiomeAtPosition(BlockPos paramBlockPos) {
/*  90 */     int i = QuartPos.fromBlock(paramBlockPos.getX());
/*  91 */     int j = QuartPos.fromBlock(paramBlockPos.getY());
/*  92 */     int k = QuartPos.fromBlock(paramBlockPos.getZ());
/*  93 */     return getNoiseBiomeAtQuart(i, j, k);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiomeAtQuart(int paramInt1, int paramInt2, int paramInt3) {
/*  97 */     return this.noiseBiomeSource.getNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   private static double getFiddledDistance(long paramLong, int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 101 */     long l = paramLong;
/*     */     
/* 103 */     l = LinearCongruentialGenerator.next(l, paramInt1);
/* 104 */     l = LinearCongruentialGenerator.next(l, paramInt2);
/* 105 */     l = LinearCongruentialGenerator.next(l, paramInt3);
/* 106 */     l = LinearCongruentialGenerator.next(l, paramInt1);
/* 107 */     l = LinearCongruentialGenerator.next(l, paramInt2);
/* 108 */     l = LinearCongruentialGenerator.next(l, paramInt3);
/*     */     
/* 110 */     double d1 = getFiddle(l);
/*     */     
/* 112 */     l = LinearCongruentialGenerator.next(l, paramLong);
/*     */     
/* 114 */     double d2 = getFiddle(l);
/*     */     
/* 116 */     l = LinearCongruentialGenerator.next(l, paramLong);
/*     */     
/* 118 */     double d3 = getFiddle(l);
/*     */     
/* 120 */     return Mth.square(paramDouble3 + d3) + Mth.square(paramDouble2 + d2) + Mth.square(paramDouble1 + d1);
/*     */   }
/*     */   
/*     */   private static double getFiddle(long paramLong) {
/* 124 */     double d = Math.floorMod(paramLong >> 24L, 1024) / 1024.0D;
/* 125 */     return (d - 0.5D) * 0.9D;
/*     */   }
/*     */   
/*     */   public static interface NoiseBiomeSource {
/*     */     Holder<Biome> getNoiseBiome(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */