/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class CalibratedSculkSensorBlock extends SculkSensorBlock {
/* 21 */   public static final MapCodec<CalibratedSculkSensorBlock> CODEC = simpleCodec(CalibratedSculkSensorBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<CalibratedSculkSensorBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   public CalibratedSculkSensorBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/* 32 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 37 */     return (BlockEntity)new CalibratedSculkSensorBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 42 */     if (!paramLevel.isClientSide()) {
/* 43 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.CALIBRATED_SCULK_SENSOR, (paramLevel, paramBlockPos, paramBlockState, paramCalibratedSculkSensorBlockEntity) -> VibrationSystem.Ticker.tick(paramLevel, paramCalibratedSculkSensorBlockEntity.getVibrationData(), paramCalibratedSculkSensorBlockEntity.getVibrationUser()));
/*    */     }
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 51 */     return (BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 56 */     if (paramDirection != paramBlockState.getValue((Property)FACING)) {
/* 57 */       return super.getSignal(paramBlockState, paramBlockGetter, paramBlockPos, paramDirection);
/*    */     }
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 64 */     super.createBlockStateDefinition(paramBuilder);
/* 65 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 71 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 76 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getActiveTicks() {
/* 81 */     return 10;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CalibratedSculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */