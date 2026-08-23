/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEventListener;
/*    */ 
/*    */ public interface EntityBlock
/*    */ {
/*    */   BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 17 */     return null;
/*    */   }
/*    */   
/*    */   default <T extends BlockEntity> GameEventListener getListener(ServerLevel paramServerLevel, T paramT) {
/* 21 */     if (paramT instanceof GameEventListener.Provider) { GameEventListener.Provider provider = (GameEventListener.Provider)paramT;
/* 22 */       return provider.getListener(); }
/*    */ 
/*    */     
/* 25 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EntityBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */