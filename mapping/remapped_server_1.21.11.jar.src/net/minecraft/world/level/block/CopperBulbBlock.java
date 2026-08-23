/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class CopperBulbBlock extends Block {
/* 19 */   public static final MapCodec<CopperBulbBlock> CODEC = simpleCodec(CopperBulbBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends CopperBulbBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   public CopperBulbBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/* 31 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 36 */     if (paramBlockState2.getBlock() != paramBlockState1.getBlock() && paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 37 */       checkAndFlip(paramBlockState1, serverLevel, paramBlockPos); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 43 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 44 */       checkAndFlip(paramBlockState, serverLevel, paramBlockPos); }
/*    */   
/*    */   }
/*    */   
/*    */   public void checkAndFlip(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 49 */     boolean bool = paramServerLevel.hasNeighborSignal(paramBlockPos);
/*    */     
/* 51 */     if (bool == ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     BlockState blockState = paramBlockState;
/* 56 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 57 */       blockState = (BlockState)blockState.cycle((Property)LIT);
/* 58 */       paramServerLevel.playSound(null, paramBlockPos, ((Boolean)blockState.getValue((Property)LIT)).booleanValue() ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
/*    */     } 
/* 60 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)blockState.setValue((Property)POWERED, Boolean.valueOf(bool)), 3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 65 */     paramBuilder.add(new Property[] { (Property)LIT, (Property)POWERED });
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 75 */     return ((Boolean)paramLevel.getBlockState(paramBlockPos).getValue((Property)LIT)).booleanValue() ? 15 : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CopperBulbBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */