/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Containers;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public abstract class AbstractFurnaceBlock extends BaseEntityBlock {
/* 26 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   protected AbstractFurnaceBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/* 31 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)LIT, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractFurnaceBlock> codec();
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 39 */     if (!paramLevel.isClientSide()) {
/* 40 */       openContainer(paramLevel, paramBlockPos, paramPlayer);
/*    */     }
/* 42 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer);
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 49 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 54 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 64 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 69 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 74 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 79 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)LIT });
/*    */   }
/*    */   
/*    */   protected static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level paramLevel, BlockEntityType<T> paramBlockEntityType, BlockEntityType<? extends AbstractFurnaceBlockEntity> paramBlockEntityType1) {
/* 83 */     ServerLevel serverLevel = (ServerLevel)paramLevel; return (paramLevel instanceof ServerLevel) ? createTickerHelper(paramBlockEntityType, paramBlockEntityType1, (paramLevel, paramBlockPos, paramBlockState, paramAbstractFurnaceBlockEntity) -> AbstractFurnaceBlockEntity.serverTick(paramServerLevel, paramBlockPos, paramBlockState, paramAbstractFurnaceBlockEntity)) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractFurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */