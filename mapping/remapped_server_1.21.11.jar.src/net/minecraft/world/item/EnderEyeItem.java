/*     */ package net.minecraft.world.item;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EyeOfEnder;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EnderEyeItem extends Item {
/*     */   public EnderEyeItem(Item.Properties paramProperties) {
/*  33 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*  38 */     Level level = paramUseOnContext.getLevel();
/*  39 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*     */     
/*  41 */     BlockState blockState1 = level.getBlockState(blockPos);
/*     */     
/*  43 */     if (!blockState1.is(Blocks.END_PORTAL_FRAME) || ((Boolean)blockState1.getValue((Property)EndPortalFrameBlock.HAS_EYE)).booleanValue()) {
/*  44 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  47 */     if (level.isClientSide()) {
/*  48 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  51 */     BlockState blockState2 = (BlockState)blockState1.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(true));
/*  52 */     Block.pushEntitiesUp(blockState1, blockState2, (LevelAccessor)level, blockPos);
/*  53 */     level.setBlock(blockPos, blockState2, 2);
/*  54 */     level.updateNeighbourForOutputSignal(blockPos, Blocks.END_PORTAL_FRAME);
/*  55 */     paramUseOnContext.getItemInHand().shrink(1);
/*     */     
/*  57 */     level.levelEvent(1503, blockPos, 0);
/*     */ 
/*     */     
/*  60 */     BlockPattern.BlockPatternMatch blockPatternMatch = EndPortalFrameBlock.getOrCreatePortalShape().find((LevelReader)level, blockPos);
/*  61 */     if (blockPatternMatch != null) {
/*  62 */       BlockPos blockPos1 = blockPatternMatch.getFrontTopLeft().offset(-3, 0, -3);
/*  63 */       for (byte b = 0; b < 3; b++) {
/*  64 */         for (byte b1 = 0; b1 < 3; b1++) {
/*  65 */           BlockPos blockPos2 = blockPos1.offset(b, 0, b1);
/*  66 */           level.destroyBlock(blockPos2, true, null);
/*  67 */           level.setBlock(blockPos2, Blocks.END_PORTAL.defaultBlockState(), 2);
/*     */         } 
/*     */       } 
/*  70 */       level.globalLevelEvent(1038, blockPos1.offset(1, 0, 1), 0);
/*     */     } 
/*     */     
/*  73 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/*  80 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/*  85 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  86 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(paramLevel, paramPlayer, ClipContext.Fluid.NONE);
/*  87 */     if (blockHitResult.getType() == HitResult.Type.BLOCK && 
/*  88 */       paramLevel.getBlockState(blockHitResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
/*  89 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     paramPlayer.startUsingItem(paramInteractionHand);
/*  97 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  98 */       BlockPos blockPos = serverLevel.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, paramPlayer.blockPosition(), 100, false);
/*  99 */       if (blockPos == null)
/*     */       {
/* 101 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       }
/* 103 */       EyeOfEnder eyeOfEnder = new EyeOfEnder(paramLevel, paramPlayer.getX(), paramPlayer.getY(0.5D), paramPlayer.getZ());
/* 104 */       eyeOfEnder.setItem(itemStack);
/* 105 */       eyeOfEnder.signalTo(Vec3.atLowerCornerOf((Vec3i)blockPos));
/* 106 */       paramLevel.gameEvent((Holder)GameEvent.PROJECTILE_SHOOT, eyeOfEnder.position(), GameEvent.Context.of((Entity)paramPlayer));
/* 107 */       paramLevel.addFreshEntity((Entity)eyeOfEnder);
/*     */       
/* 109 */       if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/* 110 */         CriteriaTriggers.USED_ENDER_EYE.trigger(serverPlayer, blockPos); }
/*     */ 
/*     */       
/* 113 */       float f = Mth.lerp(paramLevel.random.nextFloat(), 0.33F, 0.5F);
/* 114 */       paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, f);
/* 115 */       itemStack.consume(1, (LivingEntity)paramPlayer);
/* 116 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this)); }
/*     */     
/* 118 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\EnderEyeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */