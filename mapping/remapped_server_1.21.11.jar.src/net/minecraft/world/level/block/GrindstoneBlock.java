/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.GrindstoneMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class GrindstoneBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  31 */   public static final MapCodec<GrindstoneBlock> CODEC = simpleCodec(GrindstoneBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<GrindstoneBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.grindstone_title");
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected GrindstoneBlock(BlockBehaviour.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*     */     
/*  46 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  50 */     VoxelShape voxelShape1 = Shapes.or(
/*  51 */         Block.box(2.0D, 6.0D, 7.0D, 4.0D, 10.0D, 16.0D), 
/*  52 */         Block.box(2.0D, 5.0D, 3.0D, 4.0D, 11.0D, 9.0D));
/*     */     
/*  54 */     VoxelShape voxelShape2 = Shapes.rotate(voxelShape1, OctahedralGroup.INVERT_X);
/*     */     
/*  56 */     VoxelShape voxelShape3 = Shapes.or(
/*  57 */         Block.boxZ(8.0D, 2.0D, 14.0D, 0.0D, 12.0D), new VoxelShape[] { voxelShape1, voxelShape2 });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     Map map = Shapes.rotateAttachFace(voxelShape3);
/*     */     
/*  64 */     return getShapeForEachState(paramBlockState -> (VoxelShape)((Map)paramMap.get(paramBlockState.getValue((Property)FACE))).get(paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState paramBlockState) {
/*  68 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  73 */     return getVoxelShape(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  78 */     return getVoxelShape(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  88 */     if (!paramLevel.isClientSide()) {
/*  89 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/*  90 */       paramPlayer.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
/*     */     } 
/*  92 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  97 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new GrindstoneMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 102 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 107 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 112 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)FACE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 117 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GrindstoneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */