/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CaveVinesBlock extends GrowingPlantHeadBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesBlock> CODEC = simpleCodec(CaveVinesBlock::new);
/*    */   private static final float CHANCE_OF_BERRIES_ON_GROWTH = 0.11F;
/*    */   
/*    */   public MapCodec<CaveVinesBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CaveVinesBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties, Direction.DOWN, SHAPE, false, 0.1D);
/* 30 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)BERRIES, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource) {
/* 35 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState paramBlockState) {
/* 40 */     return paramBlockState.isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 45 */     return Blocks.CAVE_VINES_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateBodyAfterConvertedFromHead(BlockState paramBlockState1, BlockState paramBlockState2) {
/* 50 */     return (BlockState)paramBlockState2.setValue((Property)BERRIES, paramBlockState1.getValue((Property)BERRIES));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getGrowIntoState(BlockState paramBlockState, RandomSource paramRandomSource) {
/* 55 */     return (BlockState)super.getGrowIntoState(paramBlockState, paramRandomSource).setValue((Property)BERRIES, Boolean.valueOf((paramRandomSource.nextFloat() < 0.11F)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 60 */     return new ItemStack((ItemLike)Items.GLOW_BERRIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 65 */     return CaveVines.use((Entity)paramPlayer, paramBlockState, paramLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 70 */     super.createBlockStateDefinition(paramBuilder);
/* 71 */     paramBuilder.add(new Property[] { (Property)BERRIES });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 76 */     return !((Boolean)paramBlockState.getValue((Property)BERRIES)).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 86 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BERRIES, Boolean.valueOf(true)), 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CaveVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */