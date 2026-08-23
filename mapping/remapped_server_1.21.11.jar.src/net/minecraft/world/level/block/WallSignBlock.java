/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallSignBlock extends SignBlock {
/*     */   static {
/*  27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)paramInstance, WallSignBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<WallSignBlock> CODEC;
/*     */   
/*     */   public MapCodec<WallSignBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  39 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 4.5D, 12.5D, 14.0D, 16.0D));
/*     */   
/*     */   public WallSignBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/*  42 */     super(paramWoodType, paramProperties.sound(paramWoodType.soundType()));
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  48 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  53 */     return paramLevelReader.getBlockState(paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite())).isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  58 */     BlockState blockState = defaultBlockState();
/*  59 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/*  61 */     Level level = paramBlockPlaceContext.getLevel();
/*  62 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  64 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/*  65 */     for (Direction direction : arrayOfDirection) {
/*  66 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  70 */         Direction direction1 = direction.getOpposite();
/*     */         
/*  72 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/*  73 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  74 */           return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  83 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  84 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  86 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYRotationDegrees(BlockState paramBlockState) {
/*  91 */     return ((Direction)paramBlockState.getValue((Property)FACING)).toYRot();
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getSignHitboxCenterPosition(BlockState paramBlockState) {
/*  96 */     return ((VoxelShape)SHAPES.get(paramBlockState.getValue((Property)FACING))).bounds().getCenter();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 101 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 106 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 111 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */