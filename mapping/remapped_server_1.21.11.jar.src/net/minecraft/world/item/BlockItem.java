/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public class BlockItem extends Item {
/*     */   public BlockItem(Block paramBlock, Item.Properties paramProperties) {
/*  38 */     super(paramProperties);
/*  39 */     this.block = paramBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*  44 */     InteractionResult interactionResult = place(new BlockPlaceContext(paramUseOnContext));
/*     */     
/*  46 */     if (!interactionResult.consumesAction() && paramUseOnContext.getItemInHand().has(DataComponents.CONSUMABLE)) {
/*  47 */       return use(paramUseOnContext.getLevel(), paramUseOnContext.getPlayer(), paramUseOnContext.getHand());
/*     */     }
/*  49 */     return interactionResult;
/*     */   } @Deprecated
/*     */   private final Block block;
/*     */   public InteractionResult place(BlockPlaceContext paramBlockPlaceContext) {
/*  53 */     if (!getBlock().isEnabled(paramBlockPlaceContext.getLevel().enabledFeatures())) {
/*  54 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/*  57 */     if (!paramBlockPlaceContext.canPlace()) {
/*  58 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/*  61 */     BlockPlaceContext blockPlaceContext = updatePlacementContext(paramBlockPlaceContext);
/*  62 */     if (blockPlaceContext == null) {
/*  63 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/*  66 */     BlockState blockState1 = getPlacementState(blockPlaceContext);
/*  67 */     if (blockState1 == null) {
/*  68 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/*  71 */     if (!placeBlock(blockPlaceContext, blockState1)) {
/*  72 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/*  75 */     BlockPos blockPos = blockPlaceContext.getClickedPos();
/*  76 */     Level level = blockPlaceContext.getLevel();
/*  77 */     Player player = blockPlaceContext.getPlayer();
/*  78 */     ItemStack itemStack = blockPlaceContext.getItemInHand();
/*     */ 
/*     */     
/*  81 */     BlockState blockState2 = level.getBlockState(blockPos);
/*  82 */     if (blockState2.is(blockState1.getBlock())) {
/*  83 */       blockState2 = updateBlockStateFromTag(blockPos, level, itemStack, blockState2);
/*  84 */       updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState2);
/*  85 */       updateBlockEntityComponents(level, blockPos, itemStack);
/*  86 */       blockState2.getBlock().setPlacedBy(level, blockPos, blockState2, (LivingEntity)player, itemStack);
/*  87 */       if (player instanceof ServerPlayer) {
/*  88 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
/*     */       }
/*     */     } 
/*  91 */     SoundType soundType = blockState2.getSoundType();
/*  92 */     level.playSound((Entity)player, blockPos, getPlaceSound(blockState2), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
/*  93 */     level.gameEvent((Holder)GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of((Entity)player, blockState2));
/*  94 */     itemStack.consume(1, (LivingEntity)player);
/*  95 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected SoundEvent getPlaceSound(BlockState paramBlockState) {
/*  99 */     return paramBlockState.getSoundType().getPlaceSound();
/*     */   }
/*     */   
/*     */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext paramBlockPlaceContext) {
/* 103 */     return paramBlockPlaceContext;
/*     */   }
/*     */   
/*     */   private static void updateBlockEntityComponents(Level paramLevel, BlockPos paramBlockPos, ItemStack paramItemStack) {
/* 107 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 108 */     if (blockEntity != null) {
/* 109 */       blockEntity.applyComponentsFromItemStack(paramItemStack);
/* 110 */       blockEntity.setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean updateCustomBlockEntityTag(BlockPos paramBlockPos, Level paramLevel, Player paramPlayer, ItemStack paramItemStack, BlockState paramBlockState) {
/* 115 */     return updateCustomBlockEntityTag(paramLevel, paramPlayer, paramBlockPos, paramItemStack);
/*     */   }
/*     */   
/*     */   protected BlockState getPlacementState(BlockPlaceContext paramBlockPlaceContext) {
/* 119 */     BlockState blockState = getBlock().getStateForPlacement(paramBlockPlaceContext);
/* 120 */     return (blockState != null && canPlace(paramBlockPlaceContext, blockState)) ? blockState : null;
/*     */   }
/*     */   
/*     */   private BlockState updateBlockStateFromTag(BlockPos paramBlockPos, Level paramLevel, ItemStack paramItemStack, BlockState paramBlockState) {
/* 124 */     BlockItemStateProperties blockItemStateProperties = (BlockItemStateProperties)paramItemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/* 125 */     if (blockItemStateProperties.isEmpty()) {
/* 126 */       return paramBlockState;
/*     */     }
/* 128 */     BlockState blockState = blockItemStateProperties.apply(paramBlockState);
/* 129 */     if (blockState != paramBlockState) {
/* 130 */       paramLevel.setBlock(paramBlockPos, blockState, 2);
/*     */     }
/* 132 */     return blockState;
/*     */   }
/*     */   
/*     */   protected boolean canPlace(BlockPlaceContext paramBlockPlaceContext, BlockState paramBlockState) {
/* 136 */     Player player = paramBlockPlaceContext.getPlayer();
/* 137 */     return ((!mustSurvive() || paramBlockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) && paramBlockPlaceContext
/* 138 */       .getLevel().isUnobstructed(paramBlockState, paramBlockPlaceContext.getClickedPos(), CollisionContext.placementContext(player)));
/*     */   }
/*     */   
/*     */   protected boolean mustSurvive() {
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean placeBlock(BlockPlaceContext paramBlockPlaceContext, BlockState paramBlockState) {
/* 146 */     return paramBlockPlaceContext.getLevel().setBlock(paramBlockPlaceContext.getClickedPos(), paramBlockState, 11);
/*     */   }
/*     */   
/*     */   public static boolean updateCustomBlockEntityTag(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, ItemStack paramItemStack) {
/* 150 */     if (paramLevel.isClientSide()) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     TypedEntityData typedEntityData = (TypedEntityData)paramItemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 155 */     if (typedEntityData != null) {
/* 156 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*     */       
/* 158 */       if (blockEntity != null) {
/* 159 */         BlockEntityType blockEntityType = blockEntity.getType();
/* 160 */         if (blockEntityType != typedEntityData.type()) {
/* 161 */           return false;
/*     */         }
/* 163 */         if (blockEntityType.onlyOpCanSetNbt() && (paramPlayer == null || !paramPlayer.canUseGameMasterBlocks())) {
/* 164 */           return false;
/*     */         }
/* 166 */         return typedEntityData.loadInto(blockEntity, (HolderLookup.Provider)paramLevel.registryAccess());
/*     */       } 
/*     */     } 
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack paramItemStack, Player paramPlayer) {
/* 174 */     if (paramPlayer != null && paramPlayer.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 175 */       TypedEntityData typedEntityData = (TypedEntityData)paramItemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 176 */       if (typedEntityData != null) {
/* 177 */         return ((BlockEntityType)typedEntityData.type()).onlyOpCanSetNbt();
/*     */       }
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   public Block getBlock() {
/* 185 */     return this.block;
/*     */   }
/*     */   
/*     */   public void registerBlocks(Map<Block, Item> paramMap, Item paramItem) {
/* 189 */     paramMap.put(getBlock(), paramItem);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canFitInsideContainerItems() {
/* 195 */     return !(getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity paramItemEntity) {
/* 200 */     ItemContainerContents itemContainerContents = paramItemEntity.getItem().<ItemContainerContents>set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
/* 201 */     if (itemContainerContents != null) {
/* 202 */       ItemUtils.onContainerDestroyed(paramItemEntity, itemContainerContents.nonEmptyItemsCopy());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setBlockEntityData(ItemStack paramItemStack, BlockEntityType<?> paramBlockEntityType, TagValueOutput paramTagValueOutput) {
/* 207 */     paramTagValueOutput.discard("id");
/* 208 */     if (paramTagValueOutput.isEmpty()) {
/* 209 */       paramItemStack.remove(DataComponents.BLOCK_ENTITY_DATA);
/*     */     } else {
/* 211 */       BlockEntity.addEntityType((ValueOutput)paramTagValueOutput, paramBlockEntityType);
/* 212 */       paramItemStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(paramBlockEntityType, paramTagValueOutput.buildResult()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 218 */     return getBlock().requiredFeatures();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */