/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SugarCaneBlock extends Block {
/*  22 */   public static final MapCodec<SugarCaneBlock> CODEC = simpleCodec(SugarCaneBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SugarCaneBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */   
/*  29 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*  31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 16.0D);
/*     */   
/*     */   protected SugarCaneBlock(BlockBehaviour.Properties paramProperties) {
/*  34 */     super(paramProperties);
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  40 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  45 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  46 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  52 */     if (paramServerLevel.isEmptyBlock(paramBlockPos.above())) {
/*  53 */       byte b = 1;
/*  54 */       while (paramServerLevel.getBlockState(paramBlockPos.below(b)).is(this)) {
/*  55 */         b++;
/*     */       }
/*  57 */       if (b < 3) {
/*  58 */         int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  59 */         if (i == 15) {
/*  60 */           paramServerLevel.setBlockAndUpdate(paramBlockPos.above(), defaultBlockState());
/*  61 */           paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(0)), 260);
/*     */         } else {
/*  63 */           paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1)), 260);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  71 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  72 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*     */     
/*  75 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  80 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*  81 */     if (blockState.is(this)) {
/*  82 */       return true;
/*     */     }
/*     */     
/*  85 */     if (blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.SAND)) {
/*  86 */       BlockPos blockPos = paramBlockPos.below();
/*  87 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  88 */         BlockState blockState1 = paramLevelReader.getBlockState(blockPos.relative(direction));
/*  89 */         FluidState fluidState = paramLevelReader.getFluidState(blockPos.relative(direction));
/*  90 */         if (fluidState.is(FluidTags.WATER) || blockState1.is(Blocks.FROSTED_ICE)) {
/*  91 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 101 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SugarCaneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */