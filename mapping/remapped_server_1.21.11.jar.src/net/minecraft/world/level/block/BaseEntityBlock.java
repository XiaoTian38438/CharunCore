/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BaseEntityBlock extends Block implements EntityBlock {
/*    */   protected BaseEntityBlock(BlockBehaviour.Properties paramProperties) {
/* 15 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends BaseEntityBlock> codec();
/*    */ 
/*    */   
/*    */   protected boolean triggerEvent(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 23 */     super.triggerEvent(paramBlockState, paramLevel, paramBlockPos, paramInt1, paramInt2);
/*    */     
/* 25 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 26 */     if (blockEntity == null) {
/* 27 */       return false;
/*    */     }
/* 29 */     return blockEntity.triggerEvent(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 34 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 35 */     return (blockEntity instanceof MenuProvider) ? (MenuProvider)blockEntity : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> paramBlockEntityType, BlockEntityType<E> paramBlockEntityType1, BlockEntityTicker<? super E> paramBlockEntityTicker) {
/* 40 */     return (paramBlockEntityType1 == paramBlockEntityType) ? (BlockEntityTicker)paramBlockEntityTicker : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseEntityBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */