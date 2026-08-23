/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.nbt.CompoundTag;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PendingStore
/*    */ {
/*    */   CompoundTag data;
/* 57 */   final CompletableFuture<Void> result = new CompletableFuture<>();
/*    */   
/*    */   public PendingStore(CompoundTag paramCompoundTag) {
/* 60 */     this.data = paramCompoundTag;
/*    */   }
/*    */   
/*    */   CompoundTag copyData() {
/* 64 */     CompoundTag compoundTag = this.data;
/* 65 */     return (compoundTag == null) ? null : compoundTag.copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\IOWorker$PendingStore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */