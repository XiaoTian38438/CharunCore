/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ShelfBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
/*  51 */   public static final MapCodec<ShelfBlock> CODEC = simpleCodec(ShelfBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ShelfBlock> codec() {
/*  55 */     return CODEC;
/*     */   }
/*     */   
/*  58 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  59 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  60 */   public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = BlockStateProperties.SIDE_CHAIN_PART;
/*  61 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  63 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Shapes.or(
/*  64 */         Block.box(0.0D, 12.0D, 11.0D, 16.0D, 16.0D, 13.0D), new VoxelShape[] {
/*  65 */           Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D), 
/*  66 */           Block.box(0.0D, 0.0D, 11.0D, 16.0D, 4.0D, 13.0D) }));
/*     */   
/*     */   public ShelfBlock(BlockBehaviour.Properties paramProperties) {
/*  69 */     super(paramProperties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  71 */         .setValue((Property)FACING, (Comparable)Direction.NORTH))
/*  72 */         .setValue((Property)POWERED, Boolean.valueOf(false)))
/*  73 */         .setValue((Property)SIDE_CHAIN_PART, (Comparable)SideChainPart.UNCONNECTED))
/*  74 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  80 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  90 */     return (paramPathComputationType == PathComputationType.WATER && paramBlockState.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  95 */     return (BlockEntity)new ShelfBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 100 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)SIDE_CHAIN_PART, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 106 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/* 107 */     updateNeighborsAfterPoweringDown((LevelAccessor)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 112 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/* 117 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() != bool) {
/*     */       
/* 119 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool));
/* 120 */       if (!bool) {
/* 121 */         blockState = (BlockState)blockState.setValue((Property)SIDE_CHAIN_PART, (Comparable)SideChainPart.UNCONNECTED);
/*     */       }
/*     */       
/* 124 */       paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 125 */       playSound((LevelAccessor)paramLevel, paramBlockPos, bool ? SoundEvents.SHELF_ACTIVATE : SoundEvents.SHELF_DEACTIVATE);
/* 126 */       paramLevel.gameEvent(bool ? (Holder)GameEvent.BLOCK_ACTIVATE : (Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos, GameEvent.Context.of(blockState));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 132 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 133 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/* 134 */       .setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite()))
/* 135 */       .setValue((Property)POWERED, Boolean.valueOf(paramBlockPlaceContext.getLevel().hasNeighborSignal(paramBlockPlaceContext.getClickedPos()))))
/* 136 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 141 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 146 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRows() {
/* 151 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumns() {
/* 156 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 163 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ShelfBlockEntity) { ShelfBlockEntity shelfBlockEntity = (ShelfBlockEntity)blockEntity; if (!paramInteractionHand.equals(InteractionHand.OFF_HAND)) {
/*     */ 
/*     */ 
/*     */         
/* 167 */         OptionalInt optionalInt = getHitSlot(paramBlockHitResult, (Direction)paramBlockState.getValue((Property)FACING));
/* 168 */         if (optionalInt.isEmpty()) {
/* 169 */           return (InteractionResult)InteractionResult.PASS;
/*     */         }
/*     */         
/* 172 */         Inventory inventory = paramPlayer.getInventory();
/* 173 */         if (paramLevel.isClientSide()) {
/* 174 */           return inventory.getSelectedItem().isEmpty() ? (InteractionResult)InteractionResult.PASS : (InteractionResult)InteractionResult.SUCCESS;
/*     */         }
/*     */         
/* 177 */         if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 178 */           boolean bool1 = swapSingleItem(paramItemStack, paramPlayer, shelfBlockEntity, optionalInt.getAsInt(), inventory);
/*     */           
/* 180 */           if (bool1) {
/* 181 */             playSound((LevelAccessor)paramLevel, paramBlockPos, paramItemStack.isEmpty() ? SoundEvents.SHELF_TAKE_ITEM : SoundEvents.SHELF_SINGLE_SWAP);
/* 182 */           } else if (!paramItemStack.isEmpty()) {
/* 183 */             playSound((LevelAccessor)paramLevel, paramBlockPos, SoundEvents.SHELF_PLACE_ITEM);
/*     */           } else {
/* 185 */             return (InteractionResult)InteractionResult.PASS;
/*     */           } 
/*     */           
/* 188 */           return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(paramItemStack);
/*     */         } 
/* 190 */         ItemStack itemStack = inventory.getSelectedItem();
/* 191 */         boolean bool = swapHotbar(paramLevel, paramBlockPos, inventory);
/* 192 */         if (!bool) {
/* 193 */           return (InteractionResult)InteractionResult.CONSUME;
/*     */         }
/* 195 */         playSound((LevelAccessor)paramLevel, paramBlockPos, SoundEvents.SHELF_MULTI_SWAP);
/* 196 */         if (itemStack == inventory.getSelectedItem()) {
/* 197 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         }
/* 199 */         return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(inventory.getSelectedItem());
/*     */       }  }
/*     */     
/*     */     return (InteractionResult)InteractionResult.PASS;
/*     */   } private static boolean swapSingleItem(ItemStack paramItemStack, Player paramPlayer, ShelfBlockEntity paramShelfBlockEntity, int paramInt, Inventory paramInventory) {
/* 204 */     ItemStack itemStack1 = paramShelfBlockEntity.swapItemNoUpdate(paramInt, paramItemStack);
/* 205 */     ItemStack itemStack2 = (paramPlayer.hasInfiniteMaterials() && itemStack1.isEmpty()) ? paramItemStack.copy() : itemStack1;
/*     */     
/* 207 */     paramInventory.setItem(paramInventory.getSelectedSlot(), itemStack2);
/*     */     
/* 209 */     paramInventory.setChanged();
/* 210 */     paramShelfBlockEntity.setChanged((itemStack2.has(DataComponents.USE_EFFECTS) && !((UseEffects)itemStack2.get(DataComponents.USE_EFFECTS)).interactVibrations()) ? null : GameEvent.ITEM_INTERACT_FINISH);
/*     */     
/* 212 */     return !itemStack1.isEmpty();
/*     */   }
/*     */   
/*     */   private boolean swapHotbar(Level paramLevel, BlockPos paramBlockPos, Inventory paramInventory) {
/* 216 */     List<BlockPos> list = getAllBlocksConnectedTo((LevelAccessor)paramLevel, paramBlockPos);
/* 217 */     if (list.isEmpty()) {
/* 218 */       return false;
/*     */     }
/*     */     
/* 221 */     boolean bool = false;
/* 222 */     for (byte b = 0; b < list.size(); b++) {
/* 223 */       ShelfBlockEntity shelfBlockEntity = (ShelfBlockEntity)paramLevel.getBlockEntity(list.get(b));
/* 224 */       if (shelfBlockEntity != null) {
/*     */ 
/*     */ 
/*     */         
/* 228 */         for (byte b1 = 0; b1 < shelfBlockEntity.getContainerSize(); b1++) {
/* 229 */           int i = 9 - (list.size() - b) * shelfBlockEntity.getContainerSize() + b1;
/* 230 */           if (i >= 0 && i <= paramInventory.getContainerSize()) {
/*     */ 
/*     */ 
/*     */             
/* 234 */             ItemStack itemStack1 = paramInventory.removeItemNoUpdate(i);
/* 235 */             ItemStack itemStack2 = shelfBlockEntity.swapItemNoUpdate(b1, itemStack1);
/* 236 */             if (!itemStack1.isEmpty() || !itemStack2.isEmpty()) {
/* 237 */               paramInventory.setItem(i, itemStack2);
/* 238 */               bool = true;
/*     */             } 
/*     */           } 
/*     */         } 
/* 242 */         paramInventory.setChanged();
/* 243 */         shelfBlockEntity.setChanged(GameEvent.ENTITY_INTERACT);
/*     */       } 
/* 245 */     }  return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public SideChainPart getSideChainPart(BlockState paramBlockState) {
/* 250 */     return (SideChainPart)paramBlockState.getValue((Property)SIDE_CHAIN_PART);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setSideChainPart(BlockState paramBlockState, SideChainPart paramSideChainPart) {
/* 255 */     return (BlockState)paramBlockState.setValue((Property)SIDE_CHAIN_PART, (Comparable)paramSideChainPart);
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getFacing(BlockState paramBlockState) {
/* 260 */     return (Direction)paramBlockState.getValue((Property)FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectable(BlockState paramBlockState) {
/* 265 */     return (paramBlockState.is(BlockTags.WOODEN_SHELVES) && paramBlockState.hasProperty((Property)POWERED) && ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxChainLength() {
/* 270 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 275 */     if (((Boolean)paramBlockState1.getValue((Property)POWERED)).booleanValue()) {
/* 276 */       updateSelfAndNeighborsOnPoweringUp((LevelAccessor)paramLevel, paramBlockPos, paramBlockState1, paramBlockState2);
/*     */     } else {
/* 278 */       updateNeighborsAfterPoweringDown((LevelAccessor)paramLevel, paramBlockPos, paramBlockState1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playSound(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, SoundEvent paramSoundEvent) {
/* 283 */     paramLevelAccessor.playSound(null, paramBlockPos, paramSoundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 288 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 289 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 291 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 296 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 297 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 299 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 304 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 309 */     if (paramLevel.isClientSide())
/*     */     {
/* 311 */       return 0;
/*     */     }
/*     */     
/* 314 */     if (paramDirection != ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite())
/*     */     {
/* 316 */       return 0;
/*     */     }
/*     */     
/* 319 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ShelfBlockEntity) { ShelfBlockEntity shelfBlockEntity = (ShelfBlockEntity)blockEntity;
/* 320 */       boolean bool = shelfBlockEntity.getItem(0).isEmpty() ? false : true;
/* 321 */       byte b1 = shelfBlockEntity.getItem(1).isEmpty() ? 0 : 1;
/* 322 */       byte b2 = shelfBlockEntity.getItem(2).isEmpty() ? 0 : 1;
/* 323 */       return bool | b1 << 1 | b2 << 2; }
/*     */ 
/*     */     
/* 326 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ShelfBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */