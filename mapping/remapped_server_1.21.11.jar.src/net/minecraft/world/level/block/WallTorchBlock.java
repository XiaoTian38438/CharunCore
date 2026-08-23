/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallTorchBlock extends TorchBlock {
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)PARTICLE_OPTIONS_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, WallTorchBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<WallTorchBlock> CODEC;
/*     */   
/*     */   public MapCodec<WallTorchBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*  36 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  38 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(5.0D, 3.0D, 13.0D, 11.0D, 16.0D));
/*     */   
/*     */   protected WallTorchBlock(SimpleParticleType paramSimpleParticleType, BlockBehaviour.Properties paramProperties) {
/*  41 */     super(paramSimpleParticleType, paramProperties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  47 */     return getShape(paramBlockState);
/*     */   }
/*     */   
/*     */   public static VoxelShape getShape(BlockState paramBlockState) {
/*  51 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  56 */     return canSurvive(paramLevelReader, paramBlockPos, (Direction)paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   public static boolean canSurvive(LevelReader paramLevelReader, BlockPos paramBlockPos, Direction paramDirection) {
/*  60 */     BlockPos blockPos = paramBlockPos.relative(paramDirection.getOpposite());
/*  61 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*  62 */     return blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  67 */     BlockState blockState = defaultBlockState();
/*     */     
/*  69 */     Level level = paramBlockPlaceContext.getLevel();
/*  70 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  72 */     Direction[] arrayOfDirection = paramBlockPlaceContext.getNearestLookingDirections();
/*  73 */     for (Direction direction : arrayOfDirection) {
/*  74 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  78 */         Direction direction1 = direction.getOpposite();
/*     */         
/*  80 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction1);
/*  81 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  82 */           return blockState;
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  91 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  92 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  94 */     return paramBlockState1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  99 */     Direction direction1 = (Direction)paramBlockState.getValue((Property)FACING);
/* 100 */     double d1 = paramBlockPos.getX() + 0.5D;
/* 101 */     double d2 = paramBlockPos.getY() + 0.7D;
/* 102 */     double d3 = paramBlockPos.getZ() + 0.5D;
/* 103 */     double d4 = 0.22D;
/* 104 */     double d5 = 0.27D;
/*     */     
/* 106 */     Direction direction2 = direction1.getOpposite();
/* 107 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1 + 0.27D * direction2.getStepX(), d2 + 0.22D, d3 + 0.27D * direction2.getStepZ(), 0.0D, 0.0D, 0.0D);
/* 108 */     paramLevel.addParticle((ParticleOptions)this.flameParticle, d1 + 0.27D * direction2.getStepX(), d2 + 0.22D, d3 + 0.27D * direction2.getStepZ(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 113 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 118 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 123 */     paramBuilder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */