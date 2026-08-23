/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DecoratedPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  51 */   public static final MapCodec<DecoratedPotBlock> CODEC = simpleCodec(DecoratedPotBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<DecoratedPotBlock> codec() {
/*  55 */     return CODEC;
/*     */   }
/*     */   
/*  58 */   public static final Identifier SHERDS_DYNAMIC_DROP_ID = Identifier.withDefaultNamespace("sherds");
/*     */   
/*  60 */   public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  61 */   public static final BooleanProperty CRACKED = BlockStateProperties.CRACKED;
/*  62 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  64 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*     */   protected DecoratedPotBlock(BlockBehaviour.Properties paramProperties) {
/*  67 */     super(paramProperties);
/*  68 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  69 */         .setValue((Property)HORIZONTAL_FACING, (Comparable)Direction.NORTH))
/*  70 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)))
/*  71 */         .setValue((Property)CRACKED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  76 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  77 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  79 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  84 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  85 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/*  86 */       .setValue((Property)HORIZONTAL_FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection()))
/*  87 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER))))
/*  88 */       .setValue((Property)CRACKED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     DecoratedPotBlockEntity decoratedPotBlockEntity;
/*  93 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof DecoratedPotBlockEntity) { decoratedPotBlockEntity = (DecoratedPotBlockEntity)blockEntity; }
/*  94 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/*  97 */     if (paramLevel.isClientSide()) {
/*  98 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 101 */     ItemStack itemStack = decoratedPotBlockEntity.getTheItem();
/* 102 */     if (!paramItemStack.isEmpty() && (itemStack
/* 103 */       .isEmpty() || (ItemStack.isSameItemSameComponents(itemStack, paramItemStack) && itemStack.getCount() < itemStack.getMaxStackSize()))) {
/*     */       float f;
/* 105 */       decoratedPotBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
/* 106 */       paramPlayer.awardStat(Stats.ITEM_USED.get(paramItemStack.getItem()));
/* 107 */       ItemStack itemStack1 = paramItemStack.consumeAndReturn(1, (LivingEntity)paramPlayer);
/*     */       
/* 109 */       if (decoratedPotBlockEntity.isEmpty()) {
/* 110 */         decoratedPotBlockEntity.setTheItem(itemStack1);
/* 111 */         f = itemStack1.getCount() / itemStack1.getMaxStackSize();
/*     */       } else {
/* 113 */         itemStack.grow(1);
/* 114 */         f = itemStack.getCount() / itemStack.getMaxStackSize();
/*     */       } 
/*     */       
/* 117 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * f);
/* 118 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 119 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.DUST_PLUME, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 1.2D, paramBlockPos.getZ() + 0.5D, 7, 0.0D, 0.0D, 0.0D, 0.0D); }
/*     */       
/* 121 */       decoratedPotBlockEntity.setChanged();
/* 122 */       paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*     */       
/* 124 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 127 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*     */     DecoratedPotBlockEntity decoratedPotBlockEntity;
/* 132 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof DecoratedPotBlockEntity) { decoratedPotBlockEntity = (DecoratedPotBlockEntity)blockEntity; }
/* 133 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/* 136 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 137 */     decoratedPotBlockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
/* 138 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/* 139 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 149 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 154 */     paramBuilder.add(new Property[] { (Property)HORIZONTAL_FACING, (Property)WATERLOGGED, (Property)CRACKED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 159 */     return (BlockEntity)new DecoratedPotBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 164 */     Containers.updateNeighboursAfterDestroy(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState paramBlockState, LootParams.Builder paramBuilder) {
/* 169 */     BlockEntity blockEntity = (BlockEntity)paramBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/*     */     
/* 171 */     if (blockEntity instanceof DecoratedPotBlockEntity) { DecoratedPotBlockEntity decoratedPotBlockEntity = (DecoratedPotBlockEntity)blockEntity;
/* 172 */       paramBuilder.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, paramConsumer -> {
/*     */             for (Item item : paramDecoratedPotBlockEntity.getDecorations().ordered()) {
/*     */               paramConsumer.accept(item.getDefaultInstance());
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 179 */     return super.getDrops(paramBlockState, paramBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 184 */     ItemStack itemStack = paramPlayer.getMainHandItem();
/* 185 */     BlockState blockState = paramBlockState;
/* 186 */     if (itemStack.is(ItemTags.BREAKS_DECORATED_POTS) && !EnchantmentHelper.hasTag(itemStack, EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING)) {
/* 187 */       blockState = (BlockState)paramBlockState.setValue((Property)CRACKED, Boolean.valueOf(true));
/* 188 */       paramLevel.setBlock(paramBlockPos, blockState, 260);
/*     */     } 
/* 190 */     return super.playerWillDestroy(paramLevel, paramBlockPos, blockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 195 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 196 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 198 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundType getSoundType(BlockState paramBlockState) {
/* 203 */     if (((Boolean)paramBlockState.getValue((Property)CRACKED)).booleanValue()) {
/* 204 */       return SoundType.DECORATED_POT_CRACKED;
/*     */     }
/* 206 */     return SoundType.DECORATED_POT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 211 */     BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 212 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramProjectile.mayInteract(serverLevel, blockPos) && paramProjectile.mayBreak(serverLevel)) {
/* 213 */         paramLevel.setBlock(blockPos, (BlockState)paramBlockState.setValue((Property)CRACKED, Boolean.valueOf(true)), 260);
/* 214 */         paramLevel.destroyBlock(blockPos, true, (Entity)paramProjectile);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 220 */     BlockEntity blockEntity = paramLevelReader.getBlockEntity(paramBlockPos); if (blockEntity instanceof DecoratedPotBlockEntity) { DecoratedPotBlockEntity decoratedPotBlockEntity = (DecoratedPotBlockEntity)blockEntity;
/* 221 */       PotDecorations potDecorations = decoratedPotBlockEntity.getDecorations();
/* 222 */       return DecoratedPotBlockEntity.createDecoratedPotItem(potDecorations); }
/*     */ 
/*     */     
/* 225 */     return super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 230 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 235 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(paramLevel.getBlockEntity(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 240 */     return (BlockState)paramBlockState.setValue((Property)HORIZONTAL_FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)HORIZONTAL_FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 245 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)HORIZONTAL_FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DecoratedPotBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */