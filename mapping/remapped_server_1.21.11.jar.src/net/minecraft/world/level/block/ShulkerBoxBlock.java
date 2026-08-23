/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShulkerBoxBlock extends BaseEntityBlock {
/*     */   public static final MapCodec<ShulkerBoxBlock> CODEC;
/*     */   
/*     */   static {
/*  43 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.optionalFieldOf("color").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<ShulkerBoxBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final Map<Direction, VoxelShape> SHAPES_OPEN_SUPPORT = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*     */   
/*  57 */   public static final Identifier CONTENTS = Identifier.withDefaultNamespace("contents");
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/*  62 */     super(paramProperties);
/*  63 */     this.color = paramDyeColor;
/*  64 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  69 */     return (BlockEntity)new ShulkerBoxBlockEntity(this.color, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  74 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::tick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  79 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity; if (canOpen(paramBlockState, paramLevel, paramBlockPos, shulkerBoxBlockEntity))
/*  80 */         { paramPlayer.openMenu((MenuProvider)shulkerBoxBlockEntity);
/*  81 */           paramPlayer.awardStat(Stats.OPEN_SHULKER_BOX);
/*  82 */           PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }  }
/*     */        }
/*  84 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean canOpen(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, ShulkerBoxBlockEntity paramShulkerBoxBlockEntity) {
/*  88 */     if (paramShulkerBoxBlockEntity.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
/*  89 */       return true;
/*     */     }
/*     */     
/*  92 */     AABB aABB = Shulker.getProgressDeltaAabb(1.0F, (Direction)paramBlockState.getValue((Property)FACING), 0.0F, 0.5F, paramBlockPos.getBottomCenter()).deflate(1.0E-6D);
/*  93 */     return paramLevel.noCollision(aABB);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  98 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getClickedFace());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 103 */     paramBuilder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 108 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 109 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 110 */       if (!paramLevel.isClientSide() && paramPlayer.preventsBlockDrops() && !shulkerBoxBlockEntity.isEmpty()) {
/*     */         
/* 112 */         ItemStack itemStack = getColoredItemStack(getColor());
/* 113 */         itemStack.applyComponents(blockEntity.collectComponents());
/*     */         
/* 115 */         ItemEntity itemEntity = new ItemEntity(paramLevel, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, itemStack);
/* 116 */         itemEntity.setDefaultPickUpDelay();
/* 117 */         paramLevel.addFreshEntity((Entity)itemEntity);
/*     */       } else {
/* 119 */         shulkerBoxBlockEntity.unpackLootTable(paramPlayer);
/*     */       }  }
/*     */     
/* 122 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState paramBlockState, LootParams.Builder paramBuilder) {
/* 127 */     BlockEntity blockEntity = (BlockEntity)paramBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/*     */     
/* 129 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 130 */       paramBuilder = paramBuilder.withDynamicDrop(CONTENTS, paramConsumer -> {
/*     */             for (byte b = 0; b < paramShulkerBoxBlockEntity.getContainerSize(); b++) {
/*     */               paramConsumer.accept(paramShulkerBoxBlockEntity.getItem(b));
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 137 */     return super.getDrops(paramBlockState, paramBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 142 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 147 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 148 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity; if (!shulkerBoxBlockEntity.isClosed())
/* 149 */         return SHAPES_OPEN_SUPPORT.get(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite());  }
/*     */     
/* 151 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 156 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 157 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 158 */       return Shapes.create(shulkerBoxBlockEntity.getBoundingBox(paramBlockState)); }
/*     */     
/* 160 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 175 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*     */   }
/*     */   
/*     */   public static Block getBlockByColor(DyeColor paramDyeColor) {
/* 179 */     if (paramDyeColor == null) {
/* 180 */       return Blocks.SHULKER_BOX;
/*     */     }
/* 182 */     switch (paramDyeColor) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case BLUE: case BROWN: case GREEN: case RED: case BLACK: case PURPLE: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       Blocks.PURPLE_SHULKER_BOX;
/*     */   }
/*     */ 
/*     */   
/*     */   public DyeColor getColor() {
/* 203 */     return this.color;
/*     */   }
/*     */   
/*     */   public static ItemStack getColoredItemStack(DyeColor paramDyeColor) {
/* 207 */     return new ItemStack(getBlockByColor(paramDyeColor));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 212 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 217 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ShulkerBoxBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */