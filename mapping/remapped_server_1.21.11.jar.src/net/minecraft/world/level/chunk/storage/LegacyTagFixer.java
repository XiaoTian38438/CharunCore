/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface LegacyTagFixer
/*    */ {
/*    */   public static final Supplier<LegacyTagFixer> EMPTY = () -> ();
/*    */   
/*    */   default void markChunkDone(ChunkPos paramChunkPos) {}
/*    */   
/*    */   default int targetDataVersion() {
/* 18 */     return -1;
/*    */   }
/*    */   
/*    */   CompoundTag applyFix(CompoundTag paramCompoundTag);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\LegacyTagFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */