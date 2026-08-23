/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WaterlilyBlock extends VegetationBlock {
/* 18 */   public static final MapCodec<WaterlilyBlock> CODEC = simpleCodec(WaterlilyBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WaterlilyBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 1.5D);
/*    */   
/*    */   protected WaterlilyBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 33 */     super.entityInside(paramBlockState, paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier, paramBoolean);
/*    */     
/* 35 */     if (paramLevel instanceof net.minecraft.server.level.ServerLevel && paramEntity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 36 */       paramLevel.destroyBlock(new BlockPos((Vec3i)paramBlockPos), true, paramEntity);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 42 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 47 */     FluidState fluidState1 = paramBlockGetter.getFluidState(paramBlockPos);
/* 48 */     FluidState fluidState2 = paramBlockGetter.getFluidState(paramBlockPos.above());
/* 49 */     return ((fluidState1.getType() == Fluids.WATER || paramBlockState.getBlock() instanceof IceBlock) && fluidState2.getType() == Fluids.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WaterlilyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */