/*    */ package net.minecraft.world.level.chunk.status;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ChunkDependencies
/*    */ {
/*    */   private final ImmutableList<ChunkStatus> dependencyByRadius;
/*    */   private final int[] radiusByDependency;
/*    */   
/*    */   public ChunkDependencies(ImmutableList<ChunkStatus> paramImmutableList) {
/* 22 */     this.dependencyByRadius = paramImmutableList;
/* 23 */     boolean bool = paramImmutableList.isEmpty() ? false : (((ChunkStatus)paramImmutableList.getFirst()).getIndex() + 1);
/* 24 */     this.radiusByDependency = new int[bool];
/* 25 */     for (byte b = 0; b < paramImmutableList.size(); b++) {
/* 26 */       ChunkStatus chunkStatus = (ChunkStatus)paramImmutableList.get(b);
/* 27 */       int i = chunkStatus.getIndex();
/* 28 */       for (byte b1 = 0; b1 <= i; b1++) {
/* 29 */         this.radiusByDependency[b1] = b;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public ImmutableList<ChunkStatus> asList() {
/* 36 */     return this.dependencyByRadius;
/*    */   }
/*    */   
/*    */   public int size() {
/* 40 */     return this.dependencyByRadius.size();
/*    */   }
/*    */   
/*    */   public int getRadiusOf(ChunkStatus paramChunkStatus) {
/* 44 */     int i = paramChunkStatus.getIndex();
/* 45 */     if (i >= this.radiusByDependency.length) {
/* 46 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Requesting a ChunkStatus(%s) outside of dependency range(%s)", new Object[] { paramChunkStatus, this.dependencyByRadius }));
/*    */     }
/* 48 */     return this.radiusByDependency[i];
/*    */   }
/*    */   
/*    */   public int getRadius() {
/* 52 */     return Math.max(0, this.dependencyByRadius.size() - 1);
/*    */   }
/*    */   
/*    */   public ChunkStatus get(int paramInt) {
/* 56 */     return (ChunkStatus)this.dependencyByRadius.get(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return this.dependencyByRadius.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkDependencies.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */