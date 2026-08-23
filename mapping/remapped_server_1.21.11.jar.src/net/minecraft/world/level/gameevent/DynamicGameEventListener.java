/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicGameEventListener<T extends GameEventListener>
/*    */ {
/*    */   private final T listener;
/*    */   private SectionPos lastSection;
/*    */   
/*    */   public DynamicGameEventListener(T paramT) {
/* 21 */     this.listener = paramT;
/*    */   }
/*    */   
/*    */   public void add(ServerLevel paramServerLevel) {
/* 25 */     move(paramServerLevel);
/*    */   }
/*    */   
/*    */   public T getListener() {
/* 29 */     return this.listener;
/*    */   }
/*    */   
/*    */   public void remove(ServerLevel paramServerLevel) {
/* 33 */     ifChunkExists((LevelReader)paramServerLevel, this.lastSection, paramGameEventListenerRegistry -> paramGameEventListenerRegistry.unregister((GameEventListener)this.listener));
/*    */   }
/*    */   
/*    */   public void move(ServerLevel paramServerLevel) {
/* 37 */     this.listener.getListenerSource().getPosition((Level)paramServerLevel)
/* 38 */       .map(SectionPos::of)
/* 39 */       .ifPresent(paramSectionPos -> {
/*    */           if (this.lastSection == null || !this.lastSection.equals(paramSectionPos)) {
/*    */             ifChunkExists((LevelReader)paramServerLevel, this.lastSection, ());
/*    */             this.lastSection = paramSectionPos;
/*    */             ifChunkExists((LevelReader)paramServerLevel, this.lastSection, ());
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void ifChunkExists(LevelReader paramLevelReader, SectionPos paramSectionPos, Consumer<GameEventListenerRegistry> paramConsumer) {
/* 49 */     if (paramSectionPos == null) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     ChunkAccess chunkAccess = paramLevelReader.getChunk(paramSectionPos.x(), paramSectionPos.z(), ChunkStatus.FULL, false);
/*    */     
/* 55 */     if (chunkAccess != null)
/* 56 */       paramConsumer.accept(chunkAccess.getListenerRegistry(paramSectionPos.y())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\DynamicGameEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */