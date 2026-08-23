/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BarrelBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BarrelBlock extends BaseEntityBlock {
/* 27 */   public static final MapCodec<BarrelBlock> CODEC = simpleCodec(BarrelBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BarrelBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */   
/* 34 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/* 35 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*    */   
/*    */   public BarrelBlock(BlockBehaviour.Properties paramProperties) {
/* 38 */     super(paramProperties);
/* 39 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 44 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof BarrelBlockEntity) { BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity)blockEntity;
/* 45 */         paramPlayer.openMenu((MenuProvider)barrelBlockEntity);
/* 46 */         paramPlayer.awardStat(Stats.OPEN_BARREL);
/* 47 */         PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }
/*    */        }
/* 49 */      return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 54 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 59 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos);
/*    */     
/* 61 */     if (blockEntity instanceof BarrelBlockEntity) {
/* 62 */       ((BarrelBlockEntity)blockEntity).recheckOpen();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 68 */     return (BlockEntity)new BarrelBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 78 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 83 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 88 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 93 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)OPEN });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 98 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getNearestLookingDirection().getOpposite());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BarrelBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */