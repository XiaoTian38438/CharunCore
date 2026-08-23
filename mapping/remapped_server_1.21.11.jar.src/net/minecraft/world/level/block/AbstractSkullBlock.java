/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public abstract class AbstractSkullBlock extends BaseEntityBlock {
/* 21 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   private final SkullBlock.Type type;
/*    */   
/*    */   public AbstractSkullBlock(SkullBlock.Type paramType, BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/* 26 */     this.type = paramType;
/* 27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractSkullBlock> codec();
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     return (BlockEntity)new SkullBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 40 */     if (paramLevel.isClientSide()) {
/*    */ 
/*    */ 
/*    */       
/* 44 */       boolean bool = (paramBlockState.is(Blocks.DRAGON_HEAD) || paramBlockState.is(Blocks.DRAGON_WALL_HEAD) || paramBlockState.is(Blocks.PIGLIN_HEAD) || paramBlockState.is(Blocks.PIGLIN_WALL_HEAD)) ? true : false;
/*    */       
/* 46 */       if (bool) {
/* 47 */         return createTickerHelper(paramBlockEntityType, BlockEntityType.SKULL, SkullBlockEntity::animation);
/*    */       }
/*    */     } 
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   public SkullBlock.Type getType() {
/* 54 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 64 */     paramBuilder.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 69 */     return (BlockState)defaultBlockState()
/* 70 */       .setValue((Property)POWERED, Boolean.valueOf(paramBlockPlaceContext.getLevel().hasNeighborSignal(paramBlockPlaceContext.getClickedPos())));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 75 */     if (paramLevel.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 79 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/* 80 */     if (bool != ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue())
/* 81 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool)), 2); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */