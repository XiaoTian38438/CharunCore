/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class EmptyFluid
/*    */   extends Fluid {
/*    */   public Item getBucket() {
/* 18 */     return Items.AIR;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection) {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlow(BlockGetter paramBlockGetter, BlockPos paramBlockPos, FluidState paramFluidState) {
/* 28 */     return Vec3.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTickDelay(LevelReader paramLevelReader) {
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEmpty() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getExplosionResistance() {
/* 43 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getHeight(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 48 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getOwnHeight(FluidState paramFluidState) {
/* 53 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createLegacyBlock(FluidState paramFluidState) {
/* 58 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSource(FluidState paramFluidState) {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmount(FluidState paramFluidState) {
/* 68 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 73 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\material\EmptyFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */