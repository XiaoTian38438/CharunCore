/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueueEntry
/*     */ {
/*     */   private static final int FROM_LEVEL_BITS = 4;
/*     */   private static final int DIRECTION_BITS = 6;
/*     */   private static final long LEVEL_MASK = 15L;
/*     */   private static final long DIRECTIONS_MASK = 1008L;
/*     */   private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
/*     */   private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;
/*     */   
/*     */   public static long decreaseSkipOneDirection(int paramInt, Direction paramDirection) {
/* 258 */     long l = withoutDirection(1008L, paramDirection);
/* 259 */     return withLevel(l, paramInt);
/*     */   }
/*     */   
/*     */   public static long decreaseAllDirections(int paramInt) {
/* 263 */     return withLevel(1008L, paramInt);
/*     */   }
/*     */   
/*     */   public static long increaseLightFromEmission(int paramInt, boolean paramBoolean) {
/* 267 */     long l = 1008L;
/* 268 */     l |= 0x800L;
/* 269 */     if (paramBoolean) {
/* 270 */       l |= 0x400L;
/*     */     }
/* 272 */     return withLevel(l, paramInt);
/*     */   }
/*     */   
/*     */   public static long increaseSkipOneDirection(int paramInt, boolean paramBoolean, Direction paramDirection) {
/* 276 */     long l = withoutDirection(1008L, paramDirection);
/* 277 */     if (paramBoolean) {
/* 278 */       l |= 0x400L;
/*     */     }
/* 280 */     return withLevel(l, paramInt);
/*     */   }
/*     */   
/*     */   public static long increaseOnlyOneDirection(int paramInt, boolean paramBoolean, Direction paramDirection) {
/* 284 */     long l = 0L;
/* 285 */     if (paramBoolean) {
/* 286 */       l |= 0x400L;
/*     */     }
/* 288 */     l = withDirection(l, paramDirection);
/* 289 */     return withLevel(l, paramInt);
/*     */   }
/*     */   
/*     */   public static long increaseSkySourceInDirections(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5) {
/* 293 */     long l = withLevel(0L, 15);
/* 294 */     if (paramBoolean1) {
/* 295 */       l = withDirection(l, Direction.DOWN);
/*     */     }
/* 297 */     if (paramBoolean2) {
/* 298 */       l = withDirection(l, Direction.NORTH);
/*     */     }
/* 300 */     if (paramBoolean3) {
/* 301 */       l = withDirection(l, Direction.SOUTH);
/*     */     }
/* 303 */     if (paramBoolean4) {
/* 304 */       l = withDirection(l, Direction.WEST);
/*     */     }
/* 306 */     if (paramBoolean5) {
/* 307 */       l = withDirection(l, Direction.EAST);
/*     */     }
/* 309 */     return l;
/*     */   }
/*     */   
/*     */   public static int getFromLevel(long paramLong) {
/* 313 */     return (int)(paramLong & 0xFL);
/*     */   }
/*     */   
/*     */   public static boolean isFromEmptyShape(long paramLong) {
/* 317 */     return ((paramLong & 0x400L) != 0L);
/*     */   }
/*     */   
/*     */   public static boolean isIncreaseFromEmission(long paramLong) {
/* 321 */     return ((paramLong & 0x800L) != 0L);
/*     */   }
/*     */   
/*     */   public static boolean shouldPropagateInDirection(long paramLong, Direction paramDirection) {
/* 325 */     return ((paramLong & 1L << paramDirection.ordinal() + 4) != 0L);
/*     */   }
/*     */   
/*     */   private static long withLevel(long paramLong, int paramInt) {
/* 329 */     return paramLong & 0xFFFFFFFFFFFFFFF0L | paramInt & 0xFL;
/*     */   }
/*     */   
/*     */   private static long withDirection(long paramLong, Direction paramDirection) {
/* 333 */     return paramLong | 1L << paramDirection.ordinal() + 4;
/*     */   }
/*     */   
/*     */   private static long withoutDirection(long paramLong, Direction paramDirection) {
/* 337 */     return paramLong & (1L << paramDirection.ordinal() + 4 ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\LightEngine$QueueEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */