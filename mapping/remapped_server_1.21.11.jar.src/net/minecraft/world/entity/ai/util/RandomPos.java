/*     */ package net.minecraft.world.entity.ai.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RandomPos {
/*     */   private static final int RANDOM_POS_ATTEMPTS = 10;
/*     */   
/*     */   public static BlockPos generateRandomDirection(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/*  20 */     int i = paramRandomSource.nextInt(2 * paramInt1 + 1) - paramInt1;
/*  21 */     int j = paramRandomSource.nextInt(2 * paramInt2 + 1) - paramInt2;
/*  22 */     int k = paramRandomSource.nextInt(2 * paramInt1 + 1) - paramInt1;
/*     */     
/*  24 */     return new BlockPos(i, j, k);
/*     */   }
/*     */   
/*     */   public static BlockPos generateRandomDirectionWithinRadians(RandomSource paramRandomSource, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, double paramDouble3, double paramDouble4, double paramDouble5) {
/*  28 */     double d1 = Mth.atan2(paramDouble4, paramDouble3) - 1.5707963705062866D;
/*  29 */     double d2 = d1 + (2.0F * paramRandomSource.nextFloat() - 1.0F) * paramDouble5;
/*  30 */     double d3 = Mth.lerp(Math.sqrt(paramRandomSource.nextDouble()), paramDouble1, paramDouble2) * Mth.SQRT_OF_TWO;
/*  31 */     double d4 = -d3 * Math.sin(d2);
/*  32 */     double d5 = d3 * Math.cos(d2);
/*     */     
/*  34 */     if (Math.abs(d4) > paramDouble2 || Math.abs(d5) > paramDouble2) {
/*  35 */       return null;
/*     */     }
/*     */     
/*  38 */     int i = paramRandomSource.nextInt(2 * paramInt1 + 1) - paramInt1 + paramInt2;
/*  39 */     return BlockPos.containing(d4, i, d5);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static BlockPos moveUpOutOfSolid(BlockPos paramBlockPos, int paramInt, Predicate<BlockPos> paramPredicate) {
/*  44 */     if (paramPredicate.test(paramBlockPos)) {
/*     */       
/*  46 */       BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable().move(Direction.UP);
/*  47 */       while (mutableBlockPos.getY() <= paramInt && paramPredicate.test(mutableBlockPos)) {
/*  48 */         mutableBlockPos.move(Direction.UP);
/*     */       }
/*     */       
/*  51 */       return mutableBlockPos.immutable();
/*     */     } 
/*     */     
/*  54 */     return paramBlockPos;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static BlockPos moveUpToAboveSolid(BlockPos paramBlockPos, int paramInt1, int paramInt2, Predicate<BlockPos> paramPredicate) {
/*  59 */     if (paramInt1 < 0) {
/*  60 */       throw new IllegalArgumentException("aboveSolidAmount was " + paramInt1 + ", expected >= 0");
/*     */     }
/*     */     
/*  63 */     if (paramPredicate.test(paramBlockPos)) {
/*     */       
/*  65 */       BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable().move(Direction.UP);
/*  66 */       while (mutableBlockPos.getY() <= paramInt2 && paramPredicate.test(mutableBlockPos)) {
/*  67 */         mutableBlockPos.move(Direction.UP);
/*     */       }
/*  69 */       int i = mutableBlockPos.getY();
/*     */       
/*  71 */       while (mutableBlockPos.getY() <= paramInt2 && mutableBlockPos.getY() - i < paramInt1) {
/*  72 */         mutableBlockPos.move(Direction.UP);
/*  73 */         if (paramPredicate.test(mutableBlockPos)) {
/*  74 */           mutableBlockPos.move(Direction.DOWN);
/*     */           break;
/*     */         } 
/*     */       } 
/*  78 */       return mutableBlockPos.immutable();
/*     */     } 
/*     */     
/*  81 */     return paramBlockPos;
/*     */   }
/*     */   
/*     */   public static Vec3 generateRandomPos(PathfinderMob paramPathfinderMob, Supplier<BlockPos> paramSupplier) {
/*  85 */     Objects.requireNonNull(paramPathfinderMob); return generateRandomPos(paramSupplier, paramPathfinderMob::getWalkTargetValue);
/*     */   }
/*     */   
/*     */   public static Vec3 generateRandomPos(Supplier<BlockPos> paramSupplier, ToDoubleFunction<BlockPos> paramToDoubleFunction) {
/*  89 */     double d = Double.NEGATIVE_INFINITY;
/*  90 */     BlockPos blockPos = null;
/*     */     
/*  92 */     for (byte b = 0; b < 10; b++) {
/*  93 */       BlockPos blockPos1 = paramSupplier.get();
/*  94 */       if (blockPos1 != null) {
/*     */ 
/*     */ 
/*     */         
/*  98 */         double d1 = paramToDoubleFunction.applyAsDouble(blockPos1);
/*  99 */         if (d1 > d) {
/* 100 */           d = d1;
/* 101 */           blockPos = blockPos1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     return (blockPos != null) ? Vec3.atBottomCenterOf((Vec3i)blockPos) : null;
/*     */   }
/*     */   
/*     */   public static BlockPos generateRandomPosTowardDirection(PathfinderMob paramPathfinderMob, double paramDouble, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 109 */     double d1 = paramBlockPos.getX();
/* 110 */     double d2 = paramBlockPos.getZ();
/*     */     
/* 112 */     if (paramPathfinderMob.hasHome() && paramDouble > 1.0D) {
/* 113 */       BlockPos blockPos = paramPathfinderMob.getHomePosition();
/*     */       
/* 115 */       if (paramPathfinderMob.getX() > blockPos.getX()) {
/* 116 */         d1 -= paramRandomSource.nextDouble() * paramDouble / 2.0D;
/*     */       } else {
/* 118 */         d1 += paramRandomSource.nextDouble() * paramDouble / 2.0D;
/*     */       } 
/*     */       
/* 121 */       if (paramPathfinderMob.getZ() > blockPos.getZ()) {
/* 122 */         d2 -= paramRandomSource.nextDouble() * paramDouble / 2.0D;
/*     */       } else {
/* 124 */         d2 += paramRandomSource.nextDouble() * paramDouble / 2.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return BlockPos.containing(d1 + paramPathfinderMob.getX(), paramBlockPos.getY() + paramPathfinderMob.getY(), d2 + paramPathfinderMob.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\a\\util\RandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */