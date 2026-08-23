/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public interface WorldGenLevel
/*    */   extends ServerLevelAccessor
/*    */ {
/*    */   long getSeed();
/*    */   
/*    */   default boolean ensureCanWrite(BlockPos paramBlockPos) {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */   default void setCurrentlyGenerating(Supplier<String> paramSupplier) {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\WorldGenLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */