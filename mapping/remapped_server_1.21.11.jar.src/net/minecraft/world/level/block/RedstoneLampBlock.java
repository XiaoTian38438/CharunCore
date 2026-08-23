/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class RedstoneLampBlock extends Block {
/* 16 */   public static final MapCodec<RedstoneLampBlock> CODEC = simpleCodec(RedstoneLampBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<RedstoneLampBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/* 23 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*    */   
/*    */   public RedstoneLampBlock(BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramProperties);
/* 27 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 32 */     return (BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(paramBlockPlaceContext.getLevel().hasNeighborSignal(paramBlockPlaceContext.getClickedPos())));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 37 */     if (paramLevel.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 41 */     boolean bool = ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue();
/* 42 */     if (bool != paramLevel.hasNeighborSignal(paramBlockPos)) {
/* 43 */       if (bool) {
/* 44 */         paramLevel.scheduleTick(paramBlockPos, this, 4);
/*    */       } else {
/* 46 */         paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)LIT), 2);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 53 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && !paramServerLevel.hasNeighborSignal(paramBlockPos)) {
/* 54 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)LIT), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 60 */     paramBuilder.add(new Property[] { (Property)LIT });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RedstoneLampBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */