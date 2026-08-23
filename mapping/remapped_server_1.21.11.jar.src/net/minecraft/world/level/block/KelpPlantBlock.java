/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
/* 17 */   public static final MapCodec<KelpPlantBlock> CODEC = simpleCodec(KelpPlantBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<KelpPlantBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected KelpPlantBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties, Direction.UP, Shapes.block(), true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 30 */     return (GrowingPlantHeadBlock)Blocks.KELP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 35 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canAttachTo(BlockState paramBlockState) {
/* 40 */     return getHeadBlock().canAttachTo(paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\KelpPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */