/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class SectionTracker extends DynamicGraphMinFixedPoint {
/*    */   protected SectionTracker(int paramInt1, int paramInt2, int paramInt3) {
/*  8 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long paramLong, int paramInt, boolean paramBoolean) {
/* 13 */     if (paramBoolean && paramInt >= this.levelCount - 2) {
/*    */       return;
/*    */     }
/*    */     
/* 17 */     for (byte b = -1; b <= 1; b++) {
/* 18 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 19 */         for (byte b2 = -1; b2 <= 1; b2++) {
/* 20 */           long l = SectionPos.offset(paramLong, b, b1, b2);
/* 21 */           if (l != paramLong)
/*    */           {
/*    */             
/* 24 */             checkNeighbor(paramLong, l, paramInt, paramBoolean);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long paramLong1, long paramLong2, int paramInt) {
/* 32 */     int i = paramInt;
/* 33 */     for (byte b = -1; b <= 1; b++) {
/* 34 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 35 */         for (byte b2 = -1; b2 <= 1; b2++) {
/* 36 */           long l = SectionPos.offset(paramLong1, b, b1, b2);
/* 37 */           if (l == paramLong1) {
/* 38 */             l = Long.MAX_VALUE;
/*    */           }
/* 40 */           if (l != paramLong2) {
/* 41 */             int j = computeLevelFromNeighbor(l, paramLong1, getLevel(l));
/* 42 */             if (i > j) {
/* 43 */               i = j;
/*    */             }
/* 45 */             if (i == 0) {
/* 46 */               return i;
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 52 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long paramLong1, long paramLong2, int paramInt) {
/* 57 */     if (isSource(paramLong1)) {
/* 58 */       return getLevelFromSource(paramLong2);
/*    */     }
/* 60 */     return paramInt + 1;
/*    */   }
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/*    */   public void update(long paramLong, int paramInt, boolean paramBoolean) {
/* 66 */     checkEdge(Long.MAX_VALUE, paramLong, paramInt, paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\SectionTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */