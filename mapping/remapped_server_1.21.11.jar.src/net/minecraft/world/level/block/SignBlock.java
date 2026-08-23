/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SignApplicator;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class SignBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  44 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*     */   
/*     */   private final WoodType type;
/*     */   
/*     */   protected SignBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/*  51 */     super(paramProperties);
/*  52 */     this.type = paramWoodType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  60 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  61 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  64 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  69 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPossibleToRespawnInThis(BlockState paramBlockState) {
/*  74 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  79 */     return (BlockEntity)new SignBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     SignBlockEntity signBlockEntity;
/*     */     ServerLevel serverLevel;
/*  84 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SignBlockEntity) { signBlockEntity = (SignBlockEntity)blockEntity; }
/*  85 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/*  88 */     Item item = paramItemStack.getItem(); SignApplicator signApplicator2 = (SignApplicator)item, signApplicator1 = (item instanceof SignApplicator) ? signApplicator2 : null;
/*  89 */     boolean bool = (signApplicator1 != null && paramPlayer.mayBuild()) ? true : false;
/*     */     
/*  91 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/*  92 */     else { return (bool || signBlockEntity.isWaxed()) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.CONSUME; }
/*     */ 
/*     */     
/*  95 */     if (!bool || signBlockEntity.isWaxed() || otherPlayerIsEditingSign(paramPlayer, signBlockEntity)) {
/*  96 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  99 */     boolean bool1 = signBlockEntity.isFacingFrontText(paramPlayer);
/* 100 */     if (signApplicator1.canApplyToSign(signBlockEntity.getText(bool1), paramPlayer) && signApplicator1.tryApplyToSign((Level)serverLevel, signBlockEntity, bool1, paramPlayer)) {
/* 101 */       signBlockEntity.executeClickCommandsIfPresent(serverLevel, paramPlayer, paramBlockPos, bool1);
/* 102 */       paramPlayer.awardStat(Stats.ITEM_USED.get(paramItemStack.getItem()));
/* 103 */       serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, signBlockEntity.getBlockPos(), GameEvent.Context.of((Entity)paramPlayer, signBlockEntity.getBlockState()));
/* 104 */       paramItemStack.consume(1, (LivingEntity)paramPlayer);
/* 105 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 108 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*     */     SignBlockEntity signBlockEntity;
/*     */     ServerLevel serverLevel;
/* 113 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SignBlockEntity) { signBlockEntity = (SignBlockEntity)blockEntity; }
/* 114 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/* 117 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/* 118 */     else { Util.pauseInIde(new IllegalStateException("Expected to only call this on server"));
/* 119 */       return (InteractionResult)InteractionResult.CONSUME; }
/*     */ 
/*     */     
/* 122 */     boolean bool1 = signBlockEntity.isFacingFrontText(paramPlayer);
/*     */     
/* 124 */     boolean bool2 = signBlockEntity.executeClickCommandsIfPresent(serverLevel, paramPlayer, paramBlockPos, bool1);
/*     */     
/* 126 */     if (signBlockEntity.isWaxed()) {
/* 127 */       serverLevel.playSound(null, signBlockEntity.getBlockPos(), signBlockEntity.getSignInteractionFailedSoundEvent(), SoundSource.BLOCKS);
/* 128 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 131 */     if (bool2) {
/* 132 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     }
/*     */     
/* 135 */     if (!otherPlayerIsEditingSign(paramPlayer, signBlockEntity) && paramPlayer.mayBuild() && hasEditableText(paramPlayer, signBlockEntity, bool1)) {
/* 136 */       openTextEdit(paramPlayer, signBlockEntity, bool1);
/* 137 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 140 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private boolean hasEditableText(Player paramPlayer, SignBlockEntity paramSignBlockEntity, boolean paramBoolean) {
/* 144 */     SignText signText = paramSignBlockEntity.getText(paramBoolean);
/* 145 */     return Arrays.<Component>stream(signText.getMessages(paramPlayer.isTextFilteringEnabled()))
/* 146 */       .allMatch(paramComponent -> (paramComponent.equals(CommonComponents.EMPTY) || paramComponent.getContents() instanceof net.minecraft.network.chat.contents.PlainTextContents));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getSignHitboxCenterPosition(BlockState paramBlockState) {
/* 152 */     return new Vec3(0.5D, 0.5D, 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 157 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 158 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 160 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */   
/*     */   public WoodType type() {
/* 164 */     return this.type;
/*     */   }
/*     */   
/*     */   public static WoodType getWoodType(Block paramBlock) {
/*     */     WoodType woodType;
/* 169 */     if (paramBlock instanceof SignBlock) {
/* 170 */       woodType = ((SignBlock)paramBlock).type();
/*     */     } else {
/* 172 */       woodType = WoodType.OAK;
/*     */     } 
/* 174 */     return woodType;
/*     */   }
/*     */   
/*     */   public void openTextEdit(Player paramPlayer, SignBlockEntity paramSignBlockEntity, boolean paramBoolean) {
/* 178 */     paramSignBlockEntity.setAllowedPlayerEditor(paramPlayer.getUUID());
/* 179 */     paramPlayer.openTextEdit(paramSignBlockEntity, paramBoolean);
/*     */   }
/*     */   
/*     */   private boolean otherPlayerIsEditingSign(Player paramPlayer, SignBlockEntity paramSignBlockEntity) {
/* 183 */     UUID uUID = paramSignBlockEntity.getPlayerWhoMayEdit();
/* 184 */     return (uUID != null && !uUID.equals(paramPlayer.getUUID()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 189 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.SIGN, SignBlockEntity::tick);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends SignBlock> codec();
/*     */   
/*     */   public abstract float getYRotationDegrees(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */