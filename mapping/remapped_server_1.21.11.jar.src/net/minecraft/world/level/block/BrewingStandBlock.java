/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BrewingStandBlock extends BaseEntityBlock {
/*  32 */   public static final MapCodec<BrewingStandBlock> CODEC = simpleCodec(BrewingStandBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BrewingStandBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */   
/*  39 */   public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[] { BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final VoxelShape SHAPE = Shapes.or(
/*  46 */       Block.column(2.0D, 2.0D, 14.0D), 
/*  47 */       Block.column(14.0D, 0.0D, 2.0D));
/*     */ 
/*     */   
/*     */   public BrewingStandBlock(BlockBehaviour.Properties paramProperties) {
/*  51 */     super(paramProperties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_BOTTLE[0], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[1], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[2], Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  57 */     return (BlockEntity)new BrewingStandBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  62 */     return paramLevel.isClientSide() ? null : createTickerHelper(paramBlockEntityType, BlockEntityType.BREWING_STAND, BrewingStandBlockEntity::serverTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  67 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  72 */     if (!paramLevel.isClientSide()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof BrewingStandBlockEntity) { BrewingStandBlockEntity brewingStandBlockEntity = (BrewingStandBlockEntity)blockEntity;
/*  73 */         paramPlayer.openMenu((MenuProvider)brewingStandBlockEntity);
/*  74 */         paramPlayer.awardStat(Stats.INTERACT_WITH_BREWINGSTAND); }
/*     */        }
/*  76 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  81 */     double d1 = paramBlockPos.getX() + 0.4D + paramRandomSource.nextFloat() * 0.2D;
/*  82 */     double d2 = paramBlockPos.getY() + 0.7D + paramRandomSource.nextFloat() * 0.3D;
/*  83 */     double d3 = paramBlockPos.getZ() + 0.4D + paramRandomSource.nextFloat() * 0.2D;
/*     */     
/*  85 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  90 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 100 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 105 */     paramBuilder.add(new Property[] { (Property)HAS_BOTTLE[0], (Property)HAS_BOTTLE[1], (Property)HAS_BOTTLE[2] });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 110 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BrewingStandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */