/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeCache;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CrafterBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CrafterBlock extends BaseEntityBlock {
/*  43 */   public static final MapCodec<CrafterBlock> CODEC = simpleCodec(CrafterBlock::new);
/*  44 */   public static final BooleanProperty CRAFTING = BlockStateProperties.CRAFTING;
/*  45 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*  46 */   private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*     */   private static final int MAX_CRAFTING_TICKS = 6;
/*     */   private static final int CRAFTING_TICK_DELAY = 4;
/*  49 */   private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);
/*     */   
/*     */   private static final int CRAFTER_ADVANCEMENT_DIAMETER = 17;
/*     */   
/*     */   public CrafterBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP))
/*  56 */         .setValue((Property)TRIGGERED, Boolean.valueOf(false)))
/*  57 */         .setValue((Property)CRAFTING, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<CrafterBlock> codec() {
/*  62 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  72 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  73 */     if (blockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/*  74 */       return crafterBlockEntity.getRedstoneSignal(); }
/*     */     
/*  76 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  81 */     boolean bool1 = paramLevel.hasNeighborSignal(paramBlockPos);
/*  82 */     boolean bool2 = ((Boolean)paramBlockState.getValue((Property)TRIGGERED)).booleanValue();
/*  83 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*     */     
/*  85 */     if (bool1 && !bool2) {
/*  86 */       paramLevel.scheduleTick(paramBlockPos, this, 4);
/*  87 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)TRIGGERED, Boolean.valueOf(true)), 2);
/*  88 */       setBlockEntityTriggered(blockEntity, true);
/*  89 */     } else if (!bool1 && bool2) {
/*  90 */       paramLevel.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)TRIGGERED, Boolean.valueOf(false))).setValue((Property)CRAFTING, Boolean.valueOf(false)), 2);
/*  91 */       setBlockEntityTriggered(blockEntity, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  97 */     dispenseFrom(paramBlockState, paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 102 */     return paramLevel.isClientSide() ? null : createTickerHelper(paramBlockEntityType, BlockEntityType.CRAFTER, CrafterBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   private void setBlockEntityTriggered(BlockEntity paramBlockEntity, boolean paramBoolean) {
/* 106 */     if (paramBlockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)paramBlockEntity;
/* 107 */       crafterBlockEntity.setTriggered(paramBoolean); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 113 */     CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(paramBlockPos, paramBlockState);
/* 114 */     crafterBlockEntity.setTriggered((paramBlockState.hasProperty((Property)TRIGGERED) && ((Boolean)paramBlockState.getValue((Property)TRIGGERED)).booleanValue()));
/* 115 */     return (BlockEntity)crafterBlockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 120 */     Direction direction1 = paramBlockPlaceContext.getNearestLookingDirection().getOpposite();
/* 121 */     switch (direction1) { default: throw new MatchException(null, null);
/*     */       case DOWN: 
/*     */       case UP: 
/* 124 */       case NORTH: case SOUTH: case WEST: case EAST: break; }  Direction direction2 = Direction.UP;
/*     */ 
/*     */     
/* 127 */     return (BlockState)((BlockState)defaultBlockState()
/* 128 */       .setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(direction1, direction2)))
/* 129 */       .setValue((Property)TRIGGERED, Boolean.valueOf(paramBlockPlaceContext.getLevel().hasNeighborSignal(paramBlockPlaceContext.getClickedPos())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 134 */     if (((Boolean)paramBlockState.getValue((Property)TRIGGERED)).booleanValue()) {
/* 135 */       paramLevel.scheduleTick(paramBlockPos, this, 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 141 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 146 */     if (!paramLevel.isClientSide()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/* 147 */         paramPlayer.openMenu((MenuProvider)crafterBlockEntity); }
/*     */        }
/* 149 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   protected void dispenseFrom(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/*     */     CrafterBlockEntity crafterBlockEntity;
/* 153 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CrafterBlockEntity) { crafterBlockEntity = (CrafterBlockEntity)blockEntity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 157 */     CraftingInput craftingInput = crafterBlockEntity.asCraftInput();
/* 158 */     Optional<RecipeHolder<CraftingRecipe>> optional = getPotentialResults(paramServerLevel, craftingInput);
/*     */     
/* 160 */     if (optional.isEmpty()) {
/* 161 */       paramServerLevel.levelEvent(1050, paramBlockPos, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 165 */     RecipeHolder<?> recipeHolder = optional.get();
/*     */     
/* 167 */     ItemStack itemStack = ((CraftingRecipe)recipeHolder.value()).assemble((RecipeInput)craftingInput, (HolderLookup.Provider)paramServerLevel.registryAccess());
/*     */     
/* 169 */     if (itemStack.isEmpty()) {
/* 170 */       paramServerLevel.levelEvent(1050, paramBlockPos, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     crafterBlockEntity.setCraftingTicksRemaining(6);
/* 175 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)CRAFTING, Boolean.valueOf(true)), 2);
/* 176 */     itemStack.onCraftedBySystem((Level)paramServerLevel);
/*     */ 
/*     */     
/* 179 */     dispenseItem(paramServerLevel, paramBlockPos, crafterBlockEntity, itemStack, paramBlockState, recipeHolder);
/*     */ 
/*     */     
/* 182 */     for (ItemStack itemStack1 : ((CraftingRecipe)recipeHolder.value()).getRemainingItems(craftingInput)) {
/* 183 */       if (!itemStack1.isEmpty()) {
/* 184 */         dispenseItem(paramServerLevel, paramBlockPos, crafterBlockEntity, itemStack1, paramBlockState, recipeHolder);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 189 */     crafterBlockEntity.getItems().forEach(paramItemStack -> {
/*     */           if (paramItemStack.isEmpty()) {
/*     */             return;
/*     */           }
/*     */           paramItemStack.shrink(1);
/*     */         });
/* 195 */     crafterBlockEntity.setChanged();
/*     */   }
/*     */   
/*     */   public static Optional<RecipeHolder<CraftingRecipe>> getPotentialResults(ServerLevel paramServerLevel, CraftingInput paramCraftingInput) {
/* 199 */     return RECIPE_CACHE.get(paramServerLevel, paramCraftingInput);
/*     */   }
/*     */   
/*     */   private void dispenseItem(ServerLevel paramServerLevel, BlockPos paramBlockPos, CrafterBlockEntity paramCrafterBlockEntity, ItemStack paramItemStack, BlockState paramBlockState, RecipeHolder<?> paramRecipeHolder) {
/* 203 */     Direction direction = ((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)).front();
/* 204 */     Container container = HopperBlockEntity.getContainerAt((Level)paramServerLevel, paramBlockPos.relative(direction));
/* 205 */     ItemStack itemStack = paramItemStack.copy();
/*     */     
/* 207 */     if (container != null && (container instanceof CrafterBlockEntity || paramItemStack.getCount() > container.getMaxStackSize(paramItemStack))) {
/*     */       
/* 209 */       while (!itemStack.isEmpty()) {
/* 210 */         ItemStack itemStack1 = itemStack.copyWithCount(1);
/*     */         
/* 212 */         ItemStack itemStack2 = HopperBlockEntity.addItem((Container)paramCrafterBlockEntity, container, itemStack1, direction.getOpposite());
/*     */         
/* 214 */         if (!itemStack2.isEmpty()) {
/*     */           break;
/*     */         }
/* 217 */         itemStack.shrink(1);
/*     */       } 
/* 219 */     } else if (container != null) {
/*     */       
/* 221 */       while (!itemStack.isEmpty()) {
/* 222 */         int i = itemStack.getCount();
/* 223 */         itemStack = HopperBlockEntity.addItem((Container)paramCrafterBlockEntity, container, itemStack, direction.getOpposite());
/*     */         
/* 225 */         if (i == itemStack.getCount()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 232 */     if (!itemStack.isEmpty()) {
/* 233 */       Vec3 vec31 = Vec3.atCenterOf((Vec3i)paramBlockPos);
/* 234 */       Vec3 vec32 = vec31.relative(direction, 0.7D);
/* 235 */       DefaultDispenseItemBehavior.spawnItem((Level)paramServerLevel, itemStack, 6, direction, (Position)vec32);
/*     */       
/* 237 */       for (ServerPlayer serverPlayer : paramServerLevel.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(vec31, 17.0D, 17.0D, 17.0D))) {
/* 238 */         CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(serverPlayer, paramRecipeHolder.id(), (List)paramCrafterBlockEntity.getItems());
/*     */       }
/*     */       
/* 241 */       paramServerLevel.levelEvent(1049, paramBlockPos, 0);
/* 242 */       paramServerLevel.levelEvent(2010, paramBlockPos, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 248 */     return (BlockState)paramBlockState.setValue((Property)ORIENTATION, (Comparable)paramRotation.rotation().rotate((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 253 */     return (BlockState)paramBlockState.setValue((Property)ORIENTATION, (Comparable)paramMirror.rotation().rotate((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 258 */     paramBuilder.add(new Property[] { (Property)ORIENTATION, (Property)TRIGGERED, (Property)CRAFTING });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CrafterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */