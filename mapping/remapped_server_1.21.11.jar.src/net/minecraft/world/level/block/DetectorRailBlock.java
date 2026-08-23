/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class DetectorRailBlock extends BaseRailBlock {
/*  32 */   public static final MapCodec<DetectorRailBlock> CODEC = simpleCodec(DetectorRailBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<DetectorRailBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */   
/*  39 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  40 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   private static final int PRESSED_CHECK_PERIOD = 20;
/*     */   
/*     */   public DetectorRailBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(true, paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  55 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     checkPressed(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  68 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     checkPressed((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  77 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  82 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  83 */       return 0;
/*     */     }
/*  85 */     return (paramDirection == Direction.UP) ? 15 : 0;
/*     */   }
/*     */   
/*     */   private void checkPressed(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  89 */     if (!canSurvive(paramBlockState, (LevelReader)paramLevel, paramBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     boolean bool = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/*  94 */     boolean bool1 = false;
/*     */     
/*  96 */     List<AbstractMinecart> list = getInteractingMinecartOfType(paramLevel, paramBlockPos, AbstractMinecart.class, paramEntity -> true);
/*  97 */     if (!list.isEmpty()) {
/*  98 */       bool1 = true;
/*     */     }
/*     */     
/* 101 */     if (bool1 && !bool) {
/* 102 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true));
/* 103 */       paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 104 */       updatePowerToConnected(paramLevel, paramBlockPos, blockState, true);
/* 105 */       paramLevel.updateNeighborsAt(paramBlockPos, this);
/* 106 */       paramLevel.updateNeighborsAt(paramBlockPos.below(), this);
/* 107 */       paramLevel.setBlocksDirty(paramBlockPos, paramBlockState, blockState);
/*     */     } 
/*     */     
/* 110 */     if (!bool1 && bool) {
/* 111 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false));
/* 112 */       paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 113 */       updatePowerToConnected(paramLevel, paramBlockPos, blockState, false);
/* 114 */       paramLevel.updateNeighborsAt(paramBlockPos, this);
/* 115 */       paramLevel.updateNeighborsAt(paramBlockPos.below(), this);
/* 116 */       paramLevel.setBlocksDirty(paramBlockPos, paramBlockState, blockState);
/*     */     } 
/*     */     
/* 119 */     if (bool1) {
/* 120 */       paramLevel.scheduleTick(paramBlockPos, this, 20);
/*     */     }
/*     */     
/* 123 */     paramLevel.updateNeighbourForOutputSignal(paramBlockPos, this);
/*     */   }
/*     */   
/*     */   protected void updatePowerToConnected(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 127 */     RailState railState = new RailState(paramLevel, paramBlockPos, paramBlockState);
/* 128 */     List<BlockPos> list = railState.getConnections();
/*     */     
/* 130 */     for (BlockPos blockPos : list) {
/* 131 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/* 132 */       paramLevel.neighborChanged(blockState, blockPos, blockState.getBlock(), null, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 138 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     BlockState blockState = updateState(paramBlockState1, paramLevel, paramBlockPos, paramBoolean);
/*     */ 
/*     */     
/* 150 */     checkPressed(paramLevel, paramBlockPos, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public Property<RailShape> getShapeProperty() {
/* 155 */     return (Property<RailShape>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 165 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 166 */       List<MinecartCommandBlock> list = getInteractingMinecartOfType(paramLevel, paramBlockPos, MinecartCommandBlock.class, paramEntity -> true);
/* 167 */       if (!list.isEmpty()) {
/* 168 */         return ((MinecartCommandBlock)list.get(0)).getCommandBlock().getSuccessCount();
/*     */       }
/*     */       
/* 171 */       List<AbstractMinecart> list1 = getInteractingMinecartOfType(paramLevel, paramBlockPos, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
/* 172 */       if (!list1.isEmpty()) {
/* 173 */         return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)list1.get(0));
/*     */       }
/*     */     } 
/*     */     
/* 177 */     return 0;
/*     */   }
/*     */   
/*     */   private <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level paramLevel, BlockPos paramBlockPos, Class<T> paramClass, Predicate<Entity> paramPredicate) {
/* 181 */     return paramLevel.getEntitiesOfClass(paramClass, getSearchBB(paramBlockPos), paramPredicate);
/*     */   }
/*     */   
/*     */   private AABB getSearchBB(BlockPos paramBlockPos) {
/* 185 */     double d = 0.2D;
/*     */     
/* 187 */     return new AABB(paramBlockPos.getX() + 0.2D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.2D, (paramBlockPos.getX() + 1) - 0.2D, (paramBlockPos.getY() + 1) - 0.2D, (paramBlockPos.getZ() + 1) - 0.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 192 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 193 */     RailShape railShape2 = rotate(railShape1, paramRotation);
/* 194 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 199 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 200 */     RailShape railShape2 = mirror(railShape1, paramMirror);
/* 201 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 206 */     paramBuilder.add(new Property[] { (Property)SHAPE, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DetectorRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */