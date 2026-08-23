/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.Spawner;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpawnEggItem extends Item {
/*  42 */   private static final Map<EntityType<?>, SpawnEggItem> BY_ID = Maps.newIdentityHashMap();
/*     */   
/*     */   public SpawnEggItem(Item.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*     */     
/*  47 */     TypedEntityData typedEntityData = (TypedEntityData)components().get(DataComponents.ENTITY_DATA);
/*  48 */     if (typedEntityData != null)
/*  49 */       BY_ID.put((EntityType)typedEntityData.type(), this); 
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/*     */     ServerLevel serverLevel;
/*     */     BlockPos blockPos2;
/*  55 */     Level level = paramUseOnContext.getLevel();
/*  56 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  57 */     else { return (InteractionResult)InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/*  60 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/*  61 */     BlockPos blockPos1 = paramUseOnContext.getClickedPos();
/*  62 */     Direction direction = paramUseOnContext.getClickedFace();
/*     */     
/*  64 */     BlockState blockState = level.getBlockState(blockPos1);
/*  65 */     BlockEntity blockEntity = level.getBlockEntity(blockPos1); if (blockEntity instanceof Spawner) { Spawner spawner = (Spawner)blockEntity;
/*  66 */       EntityType<?> entityType = getType(itemStack);
/*     */       
/*  68 */       if (entityType == null) {
/*  69 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       }
/*     */       
/*  72 */       if (!serverLevel.isSpawnerBlockEnabled()) {
/*  73 */         Player player = paramUseOnContext.getPlayer(); if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/*  74 */           serverPlayer.sendSystemMessage((Component)Component.translatable("advMode.notEnabled.spawner")); }
/*     */         
/*  76 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       } 
/*     */       
/*  79 */       spawner.setEntityId(entityType, level.getRandom());
/*  80 */       level.sendBlockUpdated(blockPos1, blockState, blockState, 3);
/*  81 */       level.gameEvent((Entity)paramUseOnContext.getPlayer(), (Holder)GameEvent.BLOCK_CHANGE, blockPos1);
/*  82 */       itemStack.shrink(1);
/*  83 */       return (InteractionResult)InteractionResult.SUCCESS; }
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (blockState.getCollisionShape((BlockGetter)level, blockPos1).isEmpty()) {
/*  88 */       blockPos2 = blockPos1;
/*     */     } else {
/*  90 */       blockPos2 = blockPos1.relative(direction);
/*     */     } 
/*     */     
/*  93 */     return spawnMob((LivingEntity)paramUseOnContext.getPlayer(), itemStack, level, blockPos2, true, (!Objects.equals(blockPos1, blockPos2) && direction == Direction.UP));
/*     */   }
/*     */   
/*     */   private InteractionResult spawnMob(LivingEntity paramLivingEntity, ItemStack paramItemStack, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean1, boolean paramBoolean2) {
/*  97 */     EntityType<?> entityType = getType(paramItemStack);
/*  98 */     if (entityType == null) {
/*  99 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 101 */     if (!entityType.isAllowedInPeaceful() && paramLevel.getDifficulty() == Difficulty.PEACEFUL) {
/* 102 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 104 */     if (entityType.spawn((ServerLevel)paramLevel, paramItemStack, paramLivingEntity, paramBlockPos, EntitySpawnReason.SPAWN_ITEM_USE, paramBoolean1, paramBoolean2) != null) {
/* 105 */       paramItemStack.consume(1, paramLivingEntity);
/* 106 */       paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.ENTITY_PLACE, paramBlockPos);
/*     */     } 
/*     */     
/* 109 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 115 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*     */     
/* 117 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(paramLevel, paramPlayer, ClipContext.Fluid.SOURCE_ONLY);
/* 118 */     if (blockHitResult.getType() != HitResult.Type.BLOCK) {
/* 119 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 122 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; }
/* 123 */     else { return (InteractionResult)InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/* 126 */     BlockPos blockPos = blockHitResult.getBlockPos();
/* 127 */     if (!(paramLevel.getBlockState(blockPos).getBlock() instanceof net.minecraft.world.level.block.LiquidBlock)) {
/* 128 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 130 */     if (!paramLevel.mayInteract((Entity)paramPlayer, blockPos) || !paramPlayer.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
/* 131 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 133 */     InteractionResult interactionResult = spawnMob((LivingEntity)paramPlayer, itemStack, paramLevel, blockPos, false, false);
/* 134 */     if (interactionResult == InteractionResult.SUCCESS) {
/* 135 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*     */     }
/* 137 */     return interactionResult;
/*     */   }
/*     */   
/*     */   public boolean spawnsEntity(ItemStack paramItemStack, EntityType<?> paramEntityType) {
/* 141 */     return Objects.equals(getType(paramItemStack), paramEntityType);
/*     */   }
/*     */   
/*     */   public static SpawnEggItem byId(EntityType<?> paramEntityType) {
/* 145 */     return BY_ID.get(paramEntityType);
/*     */   }
/*     */   
/*     */   public static Iterable<SpawnEggItem> eggs() {
/* 149 */     return Iterables.unmodifiableIterable(BY_ID.values());
/*     */   }
/*     */   
/*     */   public EntityType<?> getType(ItemStack paramItemStack) {
/* 153 */     TypedEntityData typedEntityData = (TypedEntityData)paramItemStack.get(DataComponents.ENTITY_DATA);
/* 154 */     if (typedEntityData != null) {
/* 155 */       return (EntityType)typedEntityData.type();
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 162 */     return Optional.<TypedEntityData>ofNullable((TypedEntityData)components().get(DataComponents.ENTITY_DATA)).map(TypedEntityData::type).map(EntityType::requiredFeatures).orElseGet(FeatureFlagSet::of);
/*     */   }
/*     */   public Optional<Mob> spawnOffspringFromSpawnEgg(Player paramPlayer, Mob paramMob, EntityType<? extends Mob> paramEntityType, ServerLevel paramServerLevel, Vec3 paramVec3, ItemStack paramItemStack) {
/*     */     Mob mob;
/* 166 */     if (!spawnsEntity(paramItemStack, paramEntityType)) {
/* 167 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 171 */     if (paramMob instanceof AgeableMob) {
/* 172 */       AgeableMob ageableMob = ((AgeableMob)paramMob).getBreedOffspring(paramServerLevel, (AgeableMob)paramMob);
/*     */     } else {
/* 174 */       mob = (Mob)paramEntityType.create((Level)paramServerLevel, EntitySpawnReason.SPAWN_ITEM_USE);
/*     */     } 
/* 176 */     if (mob == null) {
/* 177 */       return Optional.empty();
/*     */     }
/*     */     
/* 180 */     mob.setBaby(true);
/* 181 */     if (!mob.isBaby()) {
/* 182 */       return Optional.empty();
/*     */     }
/*     */     
/* 185 */     mob.snapTo(paramVec3.x(), paramVec3.y(), paramVec3.z(), 0.0F, 0.0F);
/* 186 */     mob.applyComponentsFromItemStack(paramItemStack);
/*     */     
/* 188 */     paramServerLevel.addFreshEntityWithPassengers((Entity)mob);
/* 189 */     paramItemStack.consume(1, (LivingEntity)paramPlayer);
/* 190 */     return Optional.of(mob);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack paramItemStack, Player paramPlayer) {
/* 195 */     if (paramPlayer != null && paramPlayer.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 196 */       TypedEntityData typedEntityData = (TypedEntityData)paramItemStack.get(DataComponents.ENTITY_DATA);
/* 197 */       if (typedEntityData != null) {
/* 198 */         return ((EntityType)typedEntityData.type()).onlyOpCanSetNbt();
/*     */       }
/*     */     } 
/*     */     
/* 202 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SpawnEggItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */