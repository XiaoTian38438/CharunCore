/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DaylightDetectorBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<DaylightDetectorBlock> CODEC = simpleCodec(DaylightDetectorBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<DaylightDetectorBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*  39 */   public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 6.0D);
/*     */   
/*     */   public DaylightDetectorBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*     */     
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0))).setValue((Property)INVERTED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  51 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  61 */     return ((Integer)paramBlockState.getValue((Property)POWER)).intValue();
/*     */   }
/*     */   
/*     */   private static void updateSignalStrength(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  65 */     int i = paramLevel.getBrightness(LightLayer.SKY, paramBlockPos) - paramLevel.getSkyDarken();
/*  66 */     float f = ((Float)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, paramBlockPos)).floatValue() * 0.017453292F;
/*     */     
/*  68 */     boolean bool = ((Boolean)paramBlockState.getValue((Property)INVERTED)).booleanValue();
/*  69 */     if (bool) {
/*  70 */       i = 15 - i;
/*  71 */     } else if (i > 0) {
/*     */       
/*  73 */       float f1 = (f < 3.1415927F) ? 0.0F : 6.2831855F;
/*  74 */       f += (f1 - f) * 0.2F;
/*     */       
/*  76 */       i = Math.round(i * Mth.cos(f));
/*     */     } 
/*  78 */     i = Mth.clamp(i, 0, 15);
/*     */     
/*  80 */     if (((Integer)paramBlockState.getValue((Property)POWER)).intValue() != i) {
/*  81 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWER, Integer.valueOf(i)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  87 */     if (!paramPlayer.mayBuild()) {
/*  88 */       return super.useWithoutItem(paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramBlockHitResult);
/*     */     }
/*  90 */     if (!paramLevel.isClientSide()) {
/*  91 */       BlockState blockState = (BlockState)paramBlockState.cycle((Property)INVERTED);
/*  92 */       paramLevel.setBlock(paramBlockPos, blockState, 2);
/*  93 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of((Entity)paramPlayer, blockState));
/*  94 */       updateSignalStrength(blockState, paramLevel, paramBlockPos);
/*     */     } 
/*  96 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 106 */     return (BlockEntity)new DaylightDetectorBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 111 */     if (!paramLevel.isClientSide() && paramLevel.dimensionType().hasSkyLight()) {
/* 112 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.DAYLIGHT_DETECTOR, DaylightDetectorBlock::tickEntity);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   private static void tickEntity(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, DaylightDetectorBlockEntity paramDaylightDetectorBlockEntity) {
/* 118 */     if (paramLevel.getGameTime() % 20L == 0L) {
/* 119 */       updateSignalStrength(paramBlockState, paramLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 125 */     paramBuilder.add(new Property[] { (Property)POWER, (Property)INVERTED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DaylightDetectorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */