/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.StonecutterMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class StonecutterBlock extends Block {
/* 27 */   public static final MapCodec<StonecutterBlock> CODEC = simpleCodec(StonecutterBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<StonecutterBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */   
/* 34 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.stonecutter");
/*    */   
/* 36 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 38 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 9.0D);
/*    */   
/*    */   public StonecutterBlock(BlockBehaviour.Properties paramProperties) {
/* 41 */     super(paramProperties);
/* 42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 47 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 52 */     if (!paramLevel.isClientSide()) {
/* 53 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/* 54 */       paramPlayer.awardStat(Stats.INTERACT_WITH_STONECUTTER);
/*    */     } 
/* 56 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 61 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new StonecutterMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 66 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 76 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 81 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 86 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StonecutterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */