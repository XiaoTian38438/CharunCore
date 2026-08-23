/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.HashCommon;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathTypeCache
/*    */ {
/*    */   private static final int SIZE = 4096;
/*    */   private static final int MASK = 4095;
/* 13 */   private final long[] positions = new long[4096];
/* 14 */   private final PathType[] pathTypes = new PathType[4096];
/*    */   
/*    */   public PathType getOrCompute(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 17 */     long l = paramBlockPos.asLong();
/* 18 */     int i = index(l);
/* 19 */     PathType pathType = get(i, l);
/* 20 */     if (pathType != null) {
/* 21 */       return pathType;
/*    */     }
/* 23 */     return compute(paramBlockGetter, paramBlockPos, i, l);
/*    */   }
/*    */   
/*    */   private PathType get(int paramInt, long paramLong) {
/* 27 */     if (this.positions[paramInt] == paramLong) {
/* 28 */       return this.pathTypes[paramInt];
/*    */     }
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   private PathType compute(BlockGetter paramBlockGetter, BlockPos paramBlockPos, int paramInt, long paramLong) {
/* 34 */     PathType pathType = WalkNodeEvaluator.getPathTypeFromState(paramBlockGetter, paramBlockPos);
/* 35 */     this.positions[paramInt] = paramLong;
/* 36 */     this.pathTypes[paramInt] = pathType;
/* 37 */     return pathType;
/*    */   }
/*    */   
/*    */   public void invalidate(BlockPos paramBlockPos) {
/* 41 */     long l = paramBlockPos.asLong();
/* 42 */     int i = index(l);
/* 43 */     if (this.positions[i] == l) {
/* 44 */       this.pathTypes[i] = null;
/*    */     }
/*    */   }
/*    */   
/*    */   private static int index(long paramLong) {
/* 49 */     return (int)HashCommon.mix(paramLong) & 0xFFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\PathTypeCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */