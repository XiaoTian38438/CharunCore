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
/*    */ public class CaveVinesPlantBlock extends GrowingPlantBodyBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesPlantBlock> CODEC = simpleCodec(CaveVinesPlantBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<CaveVinesPlantBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CaveVinesPlantBlock(BlockBehaviour.Properties paramProperties) {
/* 27 */     super(paramProperties, Direction.DOWN, SHAPE, false);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BERRIES, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 33 */     return (GrowingPlantHeadBlock)Blocks.CAVE_VINES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateHeadAfterConvertedFromBody(BlockState paramBlockState1, BlockState paramBlockState2) {
/* 38 */     return (BlockState)paramBlockState2.setValue((Property)BERRIES, paramBlockState1.getValue((Property)BERRIES));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 43 */     return new ItemStack((ItemLike)Items.GLOW_BERRIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 48 */     return CaveVines.use((Entity)paramPlayer, paramBlockState, paramLevel, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 53 */     paramBuilder.add(new Property[] { (Property)BERRIES });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 58 */     return !((Boolean)paramBlockState.getValue((Property)BERRIES)).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 68 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BERRIES, Boolean.valueOf(true)), 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CaveVinesPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */