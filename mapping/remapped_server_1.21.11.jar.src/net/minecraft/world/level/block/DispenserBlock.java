/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.dispenser.BlockSource;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DispenserBlock extends BaseEntityBlock {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  48 */   public static final MapCodec<DispenserBlock> CODEC = simpleCodec(DispenserBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends DispenserBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*  56 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*     */   
/*  58 */   private static final DefaultDispenseItemBehavior DEFAULT_BEHAVIOR = new DefaultDispenseItemBehavior();
/*  59 */   public static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY = new IdentityHashMap<>();
/*     */   private static final int TRIGGER_DURATION = 4;
/*     */   
/*     */   public static void registerBehavior(ItemLike paramItemLike, DispenseItemBehavior paramDispenseItemBehavior) {
/*  63 */     DISPENSER_REGISTRY.put(paramItemLike.asItem(), paramDispenseItemBehavior);
/*     */   }
/*     */   
/*     */   public static void registerProjectileBehavior(ItemLike paramItemLike) {
/*  67 */     DISPENSER_REGISTRY.put(paramItemLike.asItem(), new ProjectileDispenseBehavior(paramItemLike.asItem()));
/*     */   }
/*     */   
/*     */   protected DispenserBlock(BlockBehaviour.Properties paramProperties) {
/*  71 */     super(paramProperties);
/*  72 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  77 */     if (!paramLevel.isClientSide()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof DispenserBlockEntity) { DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity)blockEntity;
/*  78 */         paramPlayer.openMenu((MenuProvider)dispenserBlockEntity);
/*  79 */         paramPlayer.awardStat((dispenserBlockEntity instanceof net.minecraft.world.level.block.entity.DropperBlockEntity) ? Stats.INSPECT_DROPPER : Stats.INSPECT_DISPENSER); }
/*     */        }
/*  81 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void dispenseFrom(ServerLevel paramServerLevel, BlockState paramBlockState, BlockPos paramBlockPos) {
/*  85 */     DispenserBlockEntity dispenserBlockEntity = paramServerLevel.getBlockEntity(paramBlockPos, BlockEntityType.DISPENSER).orElse(null);
/*  86 */     if (dispenserBlockEntity == null) {
/*  87 */       LOGGER.warn("Ignoring dispensing attempt for Dispenser without matching block entity at {}", paramBlockPos);
/*     */       return;
/*     */     } 
/*  90 */     BlockSource blockSource = new BlockSource(paramServerLevel, paramBlockPos, paramBlockState, dispenserBlockEntity);
/*     */     
/*  92 */     int i = dispenserBlockEntity.getRandomSlot(paramServerLevel.random);
/*  93 */     if (i < 0) {
/*  94 */       paramServerLevel.levelEvent(1001, paramBlockPos, 0);
/*  95 */       paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_ACTIVATE, paramBlockPos, GameEvent.Context.of(dispenserBlockEntity.getBlockState()));
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     ItemStack itemStack = dispenserBlockEntity.getItem(i);
/* 100 */     DispenseItemBehavior dispenseItemBehavior = getDispenseMethod((Level)paramServerLevel, itemStack);
/*     */     
/* 102 */     if (dispenseItemBehavior != DispenseItemBehavior.NOOP) {
/* 103 */       dispenserBlockEntity.setItem(i, dispenseItemBehavior.dispense(blockSource, itemStack));
/*     */     }
/*     */   }
/*     */   
/*     */   protected DispenseItemBehavior getDispenseMethod(Level paramLevel, ItemStack paramItemStack) {
/* 108 */     if (!paramItemStack.isItemEnabled(paramLevel.enabledFeatures())) {
/* 109 */       return (DispenseItemBehavior)DEFAULT_BEHAVIOR;
/*     */     }
/* 111 */     DispenseItemBehavior dispenseItemBehavior = DISPENSER_REGISTRY.get(paramItemStack.getItem());
/* 112 */     if (dispenseItemBehavior != null) {
/* 113 */       return dispenseItemBehavior;
/*     */     }
/* 115 */     return getDefaultDispenseMethod(paramItemStack);
/*     */   }
/*     */   
/*     */   private static DispenseItemBehavior getDefaultDispenseMethod(ItemStack paramItemStack) {
/* 119 */     if (paramItemStack.has(DataComponents.EQUIPPABLE)) {
/* 120 */       return (DispenseItemBehavior)EquipmentDispenseItemBehavior.INSTANCE;
/*     */     }
/* 122 */     return (DispenseItemBehavior)DEFAULT_BEHAVIOR;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 127 */     boolean bool = (paramLevel.hasNeighborSignal(paramBlockPos) || paramLevel.hasNeighborSignal(paramBlockPos.above())) ? true : false;
/* 128 */     boolean bool1 = ((Boolean)paramBlockState.getValue((Property)TRIGGERED)).booleanValue();
/*     */     
/* 130 */     if (bool && !bool1) {
/* 131 */       paramLevel.scheduleTick(paramBlockPos, this, 4);
/* 132 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)TRIGGERED, Boolean.valueOf(true)), 2);
/* 133 */     } else if (!bool && bool1) {
/* 134 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)TRIGGERED, Boolean.valueOf(false)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 140 */     dispenseFrom(paramServerLevel, paramBlockState, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 145 */     return (BlockEntity)new DispenserBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 150 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 155 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static Position getDispensePosition(BlockSource paramBlockSource) {
/* 159 */     return getDispensePosition(paramBlockSource, 0.7D, Vec3.ZERO);
/*     */   }
/*     */   
/*     */   public static Position getDispensePosition(BlockSource paramBlockSource, double paramDouble, Vec3 paramVec3) {
/* 163 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)FACING);
/*     */     
/* 165 */     return (Position)paramBlockSource.center().add(paramDouble * direction
/* 166 */         .getStepX() + paramVec3.x(), paramDouble * direction
/* 167 */         .getStepY() + paramVec3.y(), paramDouble * direction
/* 168 */         .getStepZ() + paramVec3.z());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 174 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 179 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 184 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 189 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 194 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)TRIGGERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DispenserBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */