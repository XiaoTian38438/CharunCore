/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ public final class FeatureFlagSet
/*     */ {
/*  10 */   private static final FeatureFlagSet EMPTY = new FeatureFlagSet(null, 0L);
/*     */   
/*     */   public static final int MAX_CONTAINER_SIZE = 64;
/*     */   
/*     */   private final FeatureFlagUniverse universe;
/*     */   private final long mask;
/*     */   
/*     */   private FeatureFlagSet(FeatureFlagUniverse paramFeatureFlagUniverse, long paramLong) {
/*  18 */     this.universe = paramFeatureFlagUniverse;
/*  19 */     this.mask = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   static FeatureFlagSet create(FeatureFlagUniverse paramFeatureFlagUniverse, Collection<FeatureFlag> paramCollection) {
/*  24 */     if (paramCollection.isEmpty()) {
/*  25 */       return EMPTY;
/*     */     }
/*  27 */     long l = computeMask(paramFeatureFlagUniverse, 0L, paramCollection);
/*  28 */     return new FeatureFlagSet(paramFeatureFlagUniverse, l);
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of() {
/*  32 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of(FeatureFlag paramFeatureFlag) {
/*  36 */     return new FeatureFlagSet(paramFeatureFlag.universe, paramFeatureFlag.mask);
/*     */   }
/*     */   
/*     */   public static FeatureFlagSet of(FeatureFlag paramFeatureFlag, FeatureFlag... paramVarArgs) {
/*  40 */     long l = (paramVarArgs.length == 0) ? paramFeatureFlag.mask : computeMask(paramFeatureFlag.universe, paramFeatureFlag.mask, Arrays.asList(paramVarArgs));
/*  41 */     return new FeatureFlagSet(paramFeatureFlag.universe, l);
/*     */   }
/*     */   
/*     */   private static long computeMask(FeatureFlagUniverse paramFeatureFlagUniverse, long paramLong, Iterable<FeatureFlag> paramIterable) {
/*  45 */     for (FeatureFlag featureFlag : paramIterable) {
/*  46 */       if (paramFeatureFlagUniverse != featureFlag.universe) {
/*  47 */         throw new IllegalStateException("Mismatched feature universe, expected '" + String.valueOf(paramFeatureFlagUniverse) + "', but got '" + String.valueOf(featureFlag.universe) + "'");
/*     */       }
/*  49 */       paramLong |= featureFlag.mask;
/*     */     } 
/*  51 */     return paramLong;
/*     */   }
/*     */   
/*     */   public boolean contains(FeatureFlag paramFeatureFlag) {
/*  55 */     if (this.universe != paramFeatureFlag.universe) {
/*  56 */       return false;
/*     */     }
/*  58 */     return ((this.mask & paramFeatureFlag.mask) != 0L);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  62 */     return equals(EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isSubsetOf(FeatureFlagSet paramFeatureFlagSet) {
/*  66 */     if (this.universe == null) {
/*  67 */       return true;
/*     */     }
/*  69 */     if (this.universe != paramFeatureFlagSet.universe) {
/*  70 */       return false;
/*     */     }
/*  72 */     return ((this.mask & (paramFeatureFlagSet.mask ^ 0xFFFFFFFFFFFFFFFFL)) == 0L);
/*     */   }
/*     */   
/*     */   public boolean intersects(FeatureFlagSet paramFeatureFlagSet) {
/*  76 */     if (this.universe == null || paramFeatureFlagSet.universe == null || this.universe != paramFeatureFlagSet.universe) {
/*  77 */       return false;
/*     */     }
/*  79 */     return ((this.mask & paramFeatureFlagSet.mask) != 0L);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet join(FeatureFlagSet paramFeatureFlagSet) {
/*  83 */     if (this.universe == null) {
/*  84 */       return paramFeatureFlagSet;
/*     */     }
/*  86 */     if (paramFeatureFlagSet.universe == null) {
/*  87 */       return this;
/*     */     }
/*  89 */     if (this.universe != paramFeatureFlagSet.universe) {
/*  90 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(paramFeatureFlagSet.universe) + "'");
/*     */     }
/*  92 */     return new FeatureFlagSet(this.universe, this.mask | paramFeatureFlagSet.mask);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet subtract(FeatureFlagSet paramFeatureFlagSet) {
/*  96 */     if (this.universe == null || paramFeatureFlagSet.universe == null) {
/*  97 */       return this;
/*     */     }
/*  99 */     if (this.universe != paramFeatureFlagSet.universe) {
/* 100 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(paramFeatureFlagSet.universe) + "'");
/*     */     }
/* 102 */     long l = this.mask & (paramFeatureFlagSet.mask ^ 0xFFFFFFFFFFFFFFFFL);
/* 103 */     if (l == 0L) {
/* 104 */       return EMPTY;
/*     */     }
/* 106 */     return new FeatureFlagSet(this.universe, l);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 111 */     if (this == paramObject) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (paramObject instanceof FeatureFlagSet) { FeatureFlagSet featureFlagSet = (FeatureFlagSet)paramObject; if (this.universe == featureFlagSet.universe && this.mask == featureFlagSet.mask); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return (int)HashCommon.mix(this.mask);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureFlagSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */