/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Abilities;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerPlayerGameMode {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final double FLIGHT_DISABLE_RANGE = 1.0D;
/*     */   protected ServerLevel level;
/*     */   protected final ServerPlayer player;
/*  43 */   private GameType gameModeForPlayer = GameType.DEFAULT_MODE;
/*     */   
/*     */   private GameType previousGameModeForPlayer;
/*     */   private boolean isDestroyingBlock;
/*     */   private int destroyProgressStart;
/*  48 */   private BlockPos destroyPos = BlockPos.ZERO;
/*     */   
/*     */   private int gameTicks;
/*     */   private boolean hasDelayedDestroy;
/*  52 */   private BlockPos delayedDestroyPos = BlockPos.ZERO;
/*     */   private int delayedTickStart;
/*  54 */   private int lastSentState = -1;
/*     */   
/*     */   public ServerPlayerGameMode(ServerPlayer paramServerPlayer) {
/*  57 */     this.player = paramServerPlayer;
/*  58 */     this.level = paramServerPlayer.level();
/*     */   }
/*     */   
/*     */   public boolean changeGameModeForPlayer(GameType paramGameType) {
/*  62 */     if (paramGameType == this.gameModeForPlayer) {
/*  63 */       return false;
/*     */     }
/*     */     
/*  66 */     Abilities abilities = this.player.getAbilities();
/*  67 */     setGameModeForPlayer(paramGameType, this.gameModeForPlayer);
/*     */     
/*  69 */     if (abilities.flying && paramGameType != GameType.SPECTATOR && isInRangeOfGround()) {
/*  70 */       abilities.flying = false;
/*     */     }
/*     */     
/*  73 */     this.player.onUpdateAbilities();
/*  74 */     this.level.getServer().getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, this.player));
/*  75 */     this.level.updateSleepingPlayerList();
/*     */     
/*  77 */     if (paramGameType == GameType.CREATIVE) {
/*  78 */       this.player.resetCurrentImpulseContext();
/*     */     }
/*     */     
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   protected void setGameModeForPlayer(GameType paramGameType1, GameType paramGameType2) {
/*  85 */     this.previousGameModeForPlayer = paramGameType2;
/*  86 */     this.gameModeForPlayer = paramGameType1;
/*     */     
/*  88 */     Abilities abilities = this.player.getAbilities();
/*  89 */     paramGameType1.updatePlayerAbilities(abilities);
/*     */   }
/*     */   
/*     */   private boolean isInRangeOfGround() {
/*  93 */     List list = Entity.collectAllColliders((Entity)this.player, this.level, this.player.getBoundingBox());
/*  94 */     return (list.isEmpty() && this.player.getAvailableSpaceBelow(1.0D) < 1.0D);
/*     */   }
/*     */   
/*     */   public GameType getGameModeForPlayer() {
/*  98 */     return this.gameModeForPlayer;
/*     */   }
/*     */   
/*     */   public GameType getPreviousGameModeForPlayer() {
/* 102 */     return this.previousGameModeForPlayer;
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/* 106 */     return this.gameModeForPlayer.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean isCreative() {
/* 110 */     return this.gameModeForPlayer.isCreative();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 114 */     this.gameTicks++;
/*     */     
/* 116 */     if (this.hasDelayedDestroy) {
/* 117 */       BlockState blockState = this.level.getBlockState(this.delayedDestroyPos);
/* 118 */       if (blockState.isAir()) {
/* 119 */         this.hasDelayedDestroy = false;
/*     */       } else {
/* 121 */         float f = incrementDestroyProgress(blockState, this.delayedDestroyPos, this.delayedTickStart);
/*     */         
/* 123 */         if (f >= 1.0F) {
/* 124 */           this.hasDelayedDestroy = false;
/* 125 */           destroyBlock(this.delayedDestroyPos);
/*     */         } 
/*     */       } 
/* 128 */     } else if (this.isDestroyingBlock) {
/* 129 */       BlockState blockState = this.level.getBlockState(this.destroyPos);
/*     */       
/* 131 */       if (blockState.isAir()) {
/* 132 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 133 */         this.lastSentState = -1;
/* 134 */         this.isDestroyingBlock = false;
/*     */       } else {
/* 136 */         incrementDestroyProgress(blockState, this.destroyPos, this.destroyProgressStart);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float incrementDestroyProgress(BlockState paramBlockState, BlockPos paramBlockPos, int paramInt) {
/* 142 */     int i = this.gameTicks - paramInt;
/* 143 */     float f = paramBlockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), paramBlockPos) * (i + 1);
/* 144 */     int j = (int)(f * 10.0F);
/*     */     
/* 146 */     if (j != this.lastSentState) {
/* 147 */       this.level.destroyBlockProgress(this.player.getId(), paramBlockPos, j);
/* 148 */       this.lastSentState = j;
/*     */     } 
/* 150 */     return f;
/*     */   }
/*     */   
/*     */   private void debugLogging(BlockPos paramBlockPos, boolean paramBoolean, int paramInt, String paramString) {
/* 154 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 155 */       LOGGER.debug("Server ACK {} {} {} {}", new Object[] { Integer.valueOf(paramInt), paramBlockPos, Boolean.valueOf(paramBoolean), paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos paramBlockPos, ServerboundPlayerActionPacket.Action paramAction, Direction paramDirection, int paramInt1, int paramInt2) {
/* 161 */     if (!this.player.isWithinBlockInteractionRange(paramBlockPos, 1.0D)) {
/* 162 */       debugLogging(paramBlockPos, false, paramInt2, "too far");
/*     */       return;
/*     */     } 
/* 165 */     if (paramBlockPos.getY() > paramInt1) {
/* 166 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(paramBlockPos, this.level.getBlockState(paramBlockPos)));
/* 167 */       debugLogging(paramBlockPos, false, paramInt2, "too high");
/*     */       
/*     */       return;
/*     */     } 
/* 171 */     if (paramAction == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
/* 172 */       if (!this.level.mayInteract((Entity)this.player, paramBlockPos)) {
/* 173 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(paramBlockPos, this.level.getBlockState(paramBlockPos)));
/* 174 */         debugLogging(paramBlockPos, false, paramInt2, "may not interact");
/*     */         return;
/*     */       } 
/* 177 */       if ((this.player.getAbilities()).instabuild) {
/* 178 */         destroyAndAck(paramBlockPos, paramInt2, "creative destroy");
/*     */         
/*     */         return;
/*     */       } 
/* 182 */       if (this.player.blockActionRestricted(this.level, paramBlockPos, this.gameModeForPlayer)) {
/* 183 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(paramBlockPos, this.level.getBlockState(paramBlockPos)));
/* 184 */         debugLogging(paramBlockPos, false, paramInt2, "block action restricted");
/*     */         
/*     */         return;
/*     */       } 
/* 188 */       this.destroyProgressStart = this.gameTicks;
/* 189 */       float f = 1.0F;
/* 190 */       BlockState blockState = this.level.getBlockState(paramBlockPos);
/* 191 */       if (!blockState.isAir()) {
/* 192 */         EnchantmentHelper.onHitBlock(this.level, this.player.getMainHandItem(), (LivingEntity)this.player, (Entity)this.player, EquipmentSlot.MAINHAND, Vec3.atCenterOf((Vec3i)paramBlockPos), blockState, paramItem -> this.player.onEquippedItemBroken(paramItem, EquipmentSlot.MAINHAND));
/* 193 */         blockState.attack(this.level, paramBlockPos, this.player);
/* 194 */         f = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), paramBlockPos);
/*     */       } 
/*     */       
/* 197 */       if (!blockState.isAir() && f >= 1.0F) {
/* 198 */         destroyAndAck(paramBlockPos, paramInt2, "insta mine");
/*     */       } else {
/* 200 */         if (this.isDestroyingBlock) {
/* 201 */           this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(this.destroyPos, this.level.getBlockState(this.destroyPos)));
/* 202 */           debugLogging(paramBlockPos, false, paramInt2, "abort destroying since another started (client insta mine, server disagreed)");
/*     */         } 
/* 204 */         this.isDestroyingBlock = true;
/* 205 */         this.destroyPos = paramBlockPos.immutable();
/*     */         
/* 207 */         int i = (int)(f * 10.0F);
/* 208 */         this.level.destroyBlockProgress(this.player.getId(), paramBlockPos, i);
/* 209 */         debugLogging(paramBlockPos, true, paramInt2, "actual start of destroying");
/* 210 */         this.lastSentState = i;
/*     */       } 
/* 212 */     } else if (paramAction == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
/*     */       
/* 214 */       if (paramBlockPos.equals(this.destroyPos)) {
/* 215 */         int i = this.gameTicks - this.destroyProgressStart;
/*     */         
/* 217 */         BlockState blockState = this.level.getBlockState(paramBlockPos);
/* 218 */         if (!blockState.isAir()) {
/* 219 */           float f = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), paramBlockPos) * (i + 1);
/* 220 */           if (f >= 0.7F) {
/* 221 */             this.isDestroyingBlock = false;
/* 222 */             this.level.destroyBlockProgress(this.player.getId(), paramBlockPos, -1);
/* 223 */             destroyAndAck(paramBlockPos, paramInt2, "destroyed"); return;
/*     */           } 
/* 225 */           if (!this.hasDelayedDestroy) {
/* 226 */             this.isDestroyingBlock = false;
/* 227 */             this.hasDelayedDestroy = true;
/* 228 */             this.delayedDestroyPos = paramBlockPos;
/* 229 */             this.delayedTickStart = this.destroyProgressStart;
/*     */           } 
/*     */         } 
/*     */       } 
/* 233 */       debugLogging(paramBlockPos, true, paramInt2, "stopped destroying");
/* 234 */     } else if (paramAction == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
/* 235 */       this.isDestroyingBlock = false;
/* 236 */       if (!Objects.equals(this.destroyPos, paramBlockPos)) {
/* 237 */         LOGGER.warn("Mismatch in destroy block pos: {} {}", this.destroyPos, paramBlockPos);
/* 238 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 239 */         debugLogging(paramBlockPos, true, paramInt2, "aborted mismatched destroying");
/*     */       } 
/*     */       
/* 242 */       this.level.destroyBlockProgress(this.player.getId(), paramBlockPos, -1);
/* 243 */       debugLogging(paramBlockPos, true, paramInt2, "aborted destroying");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyAndAck(BlockPos paramBlockPos, int paramInt, String paramString) {
/* 248 */     if (destroyBlock(paramBlockPos)) {
/* 249 */       debugLogging(paramBlockPos, true, paramInt, paramString);
/*     */     } else {
/* 251 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(paramBlockPos, this.level.getBlockState(paramBlockPos)));
/* 252 */       debugLogging(paramBlockPos, false, paramInt, paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos paramBlockPos) {
/* 261 */     BlockState blockState2 = this.level.getBlockState(paramBlockPos);
/* 262 */     if (!this.player.getMainHandItem().canDestroyBlock(blockState2, this.level, paramBlockPos, this.player)) {
/* 263 */       return false;
/*     */     }
/*     */     
/* 266 */     BlockEntity blockEntity = this.level.getBlockEntity(paramBlockPos);
/* 267 */     Block block = blockState2.getBlock();
/*     */ 
/*     */     
/* 270 */     if (block instanceof net.minecraft.world.level.block.GameMasterBlock && !this.player.canUseGameMasterBlocks()) {
/* 271 */       this.level.sendBlockUpdated(paramBlockPos, blockState2, blockState2, 3);
/* 272 */       return false;
/*     */     } 
/*     */     
/* 275 */     if (this.player.blockActionRestricted(this.level, paramBlockPos, this.gameModeForPlayer)) {
/* 276 */       return false;
/*     */     }
/*     */     
/* 279 */     BlockState blockState1 = block.playerWillDestroy(this.level, paramBlockPos, blockState2, this.player);
/*     */ 
/*     */     
/* 282 */     boolean bool1 = this.level.removeBlock(paramBlockPos, false);
/* 283 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 284 */       LOGGER.info("server broke {} {} -> {}", new Object[] { paramBlockPos, blockState1, this.level.getBlockState(paramBlockPos) });
/*     */     }
/* 286 */     if (bool1) {
/* 287 */       block.destroy((LevelAccessor)this.level, paramBlockPos, blockState1);
/*     */     }
/*     */     
/* 290 */     if (this.player.preventsBlockDrops()) {
/* 291 */       return true;
/*     */     }
/*     */     
/* 294 */     ItemStack itemStack1 = this.player.getMainHandItem();
/*     */     
/* 296 */     ItemStack itemStack2 = itemStack1.copy();
/* 297 */     boolean bool2 = this.player.hasCorrectToolForDrops(blockState1);
/* 298 */     itemStack1.mineBlock(this.level, blockState1, paramBlockPos, this.player);
/* 299 */     if (bool1 && bool2) {
/* 300 */       block.playerDestroy(this.level, this.player, paramBlockPos, blockState1, blockEntity, itemStack2);
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */   public InteractionResult useItem(ServerPlayer paramServerPlayer, Level paramLevel, ItemStack paramItemStack, InteractionHand paramInteractionHand) {
/*     */     ItemStack itemStack;
/* 306 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 307 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 309 */     if (paramServerPlayer.getCooldowns().isOnCooldown(paramItemStack)) {
/* 310 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 313 */     int i = paramItemStack.getCount();
/* 314 */     int j = paramItemStack.getDamageValue();
/* 315 */     InteractionResult interactionResult = paramItemStack.use(paramLevel, paramServerPlayer, paramInteractionHand);
/*     */ 
/*     */     
/* 318 */     if (interactionResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)interactionResult;
/* 319 */       itemStack = Objects.<ItemStack>requireNonNullElse(success.heldItemTransformedTo(), paramServerPlayer.getItemInHand(paramInteractionHand)); }
/*     */     else
/* 321 */     { itemStack = paramServerPlayer.getItemInHand(paramInteractionHand); }
/*     */ 
/*     */     
/* 324 */     if (itemStack == paramItemStack && itemStack.getCount() == i && itemStack.getUseDuration((LivingEntity)paramServerPlayer) <= 0 && itemStack.getDamageValue() == j) {
/* 325 */       return interactionResult;
/*     */     }
/*     */     
/* 328 */     if (interactionResult instanceof InteractionResult.Fail && itemStack.getUseDuration((LivingEntity)paramServerPlayer) > 0 && !paramServerPlayer.isUsingItem()) {
/* 329 */       return interactionResult;
/*     */     }
/*     */ 
/*     */     
/* 333 */     if (paramItemStack != itemStack) {
/* 334 */       paramServerPlayer.setItemInHand(paramInteractionHand, itemStack);
/*     */     }
/* 336 */     if (itemStack.isEmpty()) {
/* 337 */       paramServerPlayer.setItemInHand(paramInteractionHand, ItemStack.EMPTY);
/*     */     }
/* 339 */     if (!paramServerPlayer.isUsingItem()) {
/* 340 */       paramServerPlayer.inventoryMenu.sendAllDataToRemote();
/*     */     }
/* 342 */     return interactionResult;
/*     */   }
/*     */   public InteractionResult useItemOn(ServerPlayer paramServerPlayer, Level paramLevel, ItemStack paramItemStack, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     InteractionResult interactionResult;
/* 346 */     BlockPos blockPos = paramBlockHitResult.getBlockPos();
/*     */     
/* 348 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 349 */     if (!blockState.getBlock().isEnabled(paramLevel.enabledFeatures())) {
/* 350 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/* 353 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 354 */       MenuProvider menuProvider = blockState.getMenuProvider(paramLevel, blockPos);
/* 355 */       if (menuProvider != null) {
/* 356 */         paramServerPlayer.openMenu(menuProvider);
/*     */         
/* 358 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       } 
/* 360 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/*     */     
/* 363 */     boolean bool1 = (!paramServerPlayer.getMainHandItem().isEmpty() || !paramServerPlayer.getOffhandItem().isEmpty()) ? true : false;
/* 364 */     boolean bool2 = (paramServerPlayer.isSecondaryUseActive() && bool1) ? true : false;
/* 365 */     ItemStack itemStack = paramItemStack.copy();
/*     */     
/* 367 */     if (!bool2) {
/* 368 */       InteractionResult interactionResult1 = blockState.useItemOn(paramServerPlayer.getItemInHand(paramInteractionHand), paramLevel, paramServerPlayer, paramInteractionHand, paramBlockHitResult);
/* 369 */       if (interactionResult1.consumesAction()) {
/* 370 */         CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(paramServerPlayer, blockPos, itemStack);
/* 371 */         return interactionResult1;
/*     */       } 
/*     */       
/* 374 */       if (interactionResult1 instanceof InteractionResult.TryEmptyHandInteraction && paramInteractionHand == InteractionHand.MAIN_HAND) {
/* 375 */         interactionResult = blockState.useWithoutItem(paramLevel, paramServerPlayer, paramBlockHitResult);
/* 376 */         if (interactionResult.consumesAction()) {
/* 377 */           CriteriaTriggers.DEFAULT_BLOCK_USE.trigger(paramServerPlayer, blockPos);
/* 378 */           return interactionResult;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 383 */     if (paramItemStack.isEmpty() || paramServerPlayer.getCooldowns().isOnCooldown(paramItemStack)) {
/* 384 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 387 */     UseOnContext useOnContext = new UseOnContext(paramServerPlayer, paramInteractionHand, paramBlockHitResult);
/*     */     
/* 389 */     if (paramServerPlayer.hasInfiniteMaterials()) {
/*     */       
/* 391 */       int i = paramItemStack.getCount();
/* 392 */       interactionResult = paramItemStack.useOn(useOnContext);
/* 393 */       paramItemStack.setCount(i);
/*     */     } else {
/* 395 */       interactionResult = paramItemStack.useOn(useOnContext);
/*     */     } 
/* 397 */     if (interactionResult.consumesAction()) {
/* 398 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(paramServerPlayer, blockPos, itemStack);
/*     */     }
/* 400 */     return interactionResult;
/*     */   }
/*     */   
/*     */   public void setLevel(ServerLevel paramServerLevel) {
/* 404 */     this.level = paramServerLevel;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ServerPlayerGameMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */