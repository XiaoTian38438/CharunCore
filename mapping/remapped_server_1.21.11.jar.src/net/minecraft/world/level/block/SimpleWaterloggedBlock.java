/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public interface SimpleWaterloggedBlock extends BucketPickup, LiquidBlockContainer {
/*    */   default boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 22 */     return (paramFluid == Fluids.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 27 */     if (!((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && paramFluidState.getType() == Fluids.WATER) {
/* 28 */       if (!paramLevelAccessor.isClientSide()) {
/* 29 */         paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
/* 30 */         paramLevelAccessor.scheduleTick(paramBlockPos, paramFluidState.getType(), paramFluidState.getType().getTickDelay((LevelReader)paramLevelAccessor));
/*    */       } 
/* 32 */       return true;
/*    */     } 
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack pickupBlock(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 39 */     if (((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 40 */       paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
/* 41 */       if (!paramBlockState.canSurvive((LevelReader)paramLevelAccessor, paramBlockPos)) {
/* 42 */         paramLevelAccessor.destroyBlock(paramBlockPos, true);
/*    */       }
/* 44 */       return new ItemStack((ItemLike)Items.WATER_BUCKET);
/*    */     } 
/* 46 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<SoundEvent> getPickupSound() {
/* 51 */     return Fluids.WATER.getPickupSound();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SimpleWaterloggedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */