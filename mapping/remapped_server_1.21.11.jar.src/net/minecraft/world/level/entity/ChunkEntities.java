/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ public class ChunkEntities<T>
/*    */ {
/*    */   private final ChunkPos pos;
/*    */   private final List<T> entities;
/*    */   
/*    */   public ChunkEntities(ChunkPos paramChunkPos, List<T> paramList) {
/* 14 */     this.pos = paramChunkPos;
/* 15 */     this.entities = paramList;
/*    */   }
/*    */   
/*    */   public ChunkPos getPos() {
/* 19 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Stream<T> getEntities() {
/* 23 */     return this.entities.stream();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 27 */     return this.entities.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\ChunkEntities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */