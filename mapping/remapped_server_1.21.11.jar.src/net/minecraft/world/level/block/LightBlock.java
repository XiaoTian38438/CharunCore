/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LightBlock extends Block implements SimpleWaterloggedBlock {
/*  32 */   public static final MapCodec<LightBlock> CODEC = simpleCodec(LightBlock::new);
/*     */   public static final int MAX_LEVEL = 15;
/*     */   
/*     */   public MapCodec<LightBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  40 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*  41 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; public static final ToIntFunction<BlockState> LIGHT_EMISSION; static {
/*  42 */     LIGHT_EMISSION = (paramBlockState -> ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue());
/*     */   }
/*     */   public LightBlock(BlockBehaviour.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(15))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  51 */     paramBuilder.add(new Property[] { (Property)LEVEL, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  56 */     if (!paramLevel.isClientSide() && paramPlayer.canUseGameMasterBlocks()) {
/*  57 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)LEVEL), 2);
/*  58 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*  60 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  65 */     return paramCollisionContext.isHoldingItem(Items.LIGHT) ? Shapes.block() : Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  70 */     return paramBlockState.getFluidState().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/*  75 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  80 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  85 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  86 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  88 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  93 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  94 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  96 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 101 */     return setLightOnStack(super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean), ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue());
/*     */   }
/*     */   
/*     */   public static ItemStack setLightOnStack(ItemStack paramItemStack, int paramInt) {
/* 105 */     paramItemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)LEVEL, Integer.valueOf(paramInt)));
/* 106 */     return paramItemStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LightBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */