/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractCauldronBlock extends Block {
/*     */   protected static final int FLOOR_LEVEL = 4;
/*  28 */   private static final VoxelShape SHAPE_INSIDE = Block.column(12.0D, 4.0D, 16.0D);
/*  29 */   protected static final VoxelShape SHAPE = (VoxelShape)Util.make(() -> {
/*     */         byte b1 = 4;
/*     */         byte b2 = 3;
/*     */         byte b3 = 2;
/*     */         return Shapes.join(Shapes.block(), Shapes.or(Block.column(16.0D, 8.0D, 0.0D, 3.0D), new VoxelShape[] { Block.column(8.0D, 16.0D, 0.0D, 3.0D), Block.column(12.0D, 0.0D, 3.0D), SHAPE_INSIDE }), BooleanOp.ONLY_FIRST);
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final CauldronInteraction.InteractionMap interactions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCauldronBlock(BlockBehaviour.Properties paramProperties, CauldronInteraction.InteractionMap paramInteractionMap) {
/*  52 */     super(paramProperties);
/*  53 */     this.interactions = paramInteractionMap;
/*     */   }
/*     */   
/*     */   protected double getContentHeight(BlockState paramBlockState) {
/*  57 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  62 */     CauldronInteraction cauldronInteraction = (CauldronInteraction)this.interactions.map().get(paramItemStack.getItem());
/*  63 */     return cauldronInteraction.interact(paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  68 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getInteractionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  73 */     return SHAPE_INSIDE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  90 */     BlockPos blockPos = PointedDripstoneBlock.findStalactiteTipAboveCauldron((Level)paramServerLevel, paramBlockPos);
/*  91 */     if (blockPos == null) {
/*     */       return;
/*     */     }
/*  94 */     Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(paramServerLevel, blockPos);
/*  95 */     if (fluid != Fluids.EMPTY && canReceiveStalactiteDrip(fluid)) {
/*  96 */       receiveStalactiteDrip(paramBlockState, (Level)paramServerLevel, paramBlockPos, fluid);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canReceiveStalactiteDrip(Fluid paramFluid) {
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   protected void receiveStalactiteDrip(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Fluid paramFluid) {}
/*     */   
/*     */   protected abstract MapCodec<? extends AbstractCauldronBlock> codec();
/*     */   
/*     */   public abstract boolean isFull(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */