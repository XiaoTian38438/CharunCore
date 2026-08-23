/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class ChunkTracker extends DynamicGraphMinFixedPoint {
/*    */   protected ChunkTracker(int paramInt1, int paramInt2, int paramInt3) {
/*  8 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSource(long paramLong) {
/* 13 */     return (paramLong == ChunkPos.INVALID_CHUNK_POS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long paramLong, int paramInt, boolean paramBoolean) {
/* 18 */     if (paramBoolean && paramInt >= this.levelCount - 2) {
/*    */       return;
/*    */     }
/*    */     
/* 22 */     ChunkPos chunkPos = new ChunkPos(paramLong);
/* 23 */     int i = chunkPos.x;
/* 24 */     int j = chunkPos.z;
/* 25 */     for (byte b = -1; b <= 1; b++) {
/* 26 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 27 */         long l = ChunkPos.asLong(i + b, j + b1);
/* 28 */         if (l != paramLong)
/*    */         {
/*    */           
/* 31 */           checkNeighbor(paramLong, l, paramInt, paramBoolean);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long paramLong1, long paramLong2, int paramInt) {
/* 38 */     int i = paramInt;
/* 39 */     ChunkPos chunkPos = new ChunkPos(paramLong1);
/* 40 */     int j = chunkPos.x;
/* 41 */     int k = chunkPos.z;
/* 42 */     for (byte b = -1; b <= 1; b++) {
/* 43 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 44 */         long l = ChunkPos.asLong(j + b, k + b1);
/* 45 */         if (l == paramLong1) {
/* 46 */           l = ChunkPos.INVALID_CHUNK_POS;
/*    */         }
/* 48 */         if (l != paramLong2) {
/* 49 */           int m = computeLevelFromNeighbor(l, paramLong1, getLevel(l));
/* 50 */           if (i > m) {
/* 51 */             i = m;
/*    */           }
/* 53 */           if (i == 0) {
/* 54 */             return i;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 59 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long paramLong1, long paramLong2, int paramInt) {
/* 64 */     if (paramLong1 == ChunkPos.INVALID_CHUNK_POS) {
/* 65 */       return getLevelFromSource(paramLong2);
/*    */     }
/* 67 */     return paramInt + 1;
/*    */   }
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/*    */   public void update(long paramLong, int paramInt, boolean paramBoolean) {
/* 73 */     checkEdge(ChunkPos.INVALID_CHUNK_POS, paramLong, paramInt, paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */