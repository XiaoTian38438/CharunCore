/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Pufferfish extends AbstractFish {
/*  33 */   private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);
/*     */   int inflateCounter;
/*     */   int deflateTimer;
/*     */   private static final TargetingConditions.Selector SCARY_MOB;
/*     */   
/*  38 */   static { SCARY_MOB = ((paramLivingEntity, paramServerLevel) -> {
/*     */         if (paramLivingEntity instanceof Player) {
/*     */           Player player = (Player)paramLivingEntity;
/*     */           if (player.isCreative())
/*     */             return false; 
/*     */         } 
/*     */         return !paramLivingEntity.getType().is(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH);
/*  45 */       }); } static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);
/*     */   
/*     */   public static final int STATE_SMALL = 0;
/*     */   public static final int STATE_MID = 1;
/*     */   public static final int STATE_FULL = 2;
/*     */   private static final int DEFAULT_PUFF_STATE = 0;
/*     */   
/*     */   public Pufferfish(EntityType<? extends Pufferfish> paramEntityType, Level paramLevel) {
/*  53 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  55 */     refreshDimensions();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  60 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  62 */     paramBuilder.define(PUFF_STATE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public int getPuffState() {
/*  66 */     return ((Integer)this.entityData.get(PUFF_STATE)).intValue();
/*     */   }
/*     */   
/*     */   public void setPuffState(int paramInt) {
/*  70 */     this.entityData.set(PUFF_STATE, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/*  75 */     if (PUFF_STATE.equals(paramEntityDataAccessor)) {
/*  76 */       refreshDimensions();
/*     */     }
/*     */     
/*  79 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  84 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  86 */     paramValueOutput.putInt("PuffState", getPuffState());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  91 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/*  93 */     setPuffState(Math.min(paramValueInput.getIntOr("PuffState", 0), 2));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/*  98 */     return new ItemStack((ItemLike)Items.PUFFERFISH_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 103 */     super.registerGoals();
/*     */     
/* 105 */     this.goalSelector.addGoal(1, new PufferfishPuffGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 110 */     if (!level().isClientSide() && isAlive() && isEffectiveAi()) {
/* 111 */       if (this.inflateCounter > 0) {
/*     */         
/* 113 */         if (getPuffState() == 0) {
/* 114 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 115 */           setPuffState(1);
/*     */         }
/* 117 */         else if (this.inflateCounter > 40 && getPuffState() == 1) {
/* 118 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 119 */           setPuffState(2);
/*     */         } 
/*     */ 
/*     */         
/* 123 */         this.inflateCounter++;
/* 124 */       } else if (getPuffState() != 0) {
/*     */         
/* 126 */         if (this.deflateTimer > 60 && getPuffState() == 2) {
/* 127 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 128 */           setPuffState(1);
/* 129 */         } else if (this.deflateTimer > 100 && getPuffState() == 1) {
/* 130 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 131 */           setPuffState(0);
/*     */         } 
/*     */         
/* 134 */         this.deflateTimer++;
/*     */       } 
/*     */     }
/* 137 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 142 */     super.aiStep();
/*     */     
/* 144 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (isAlive() && getPuffState() > 0) {
/* 145 */         List list = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(0.3D), paramMob -> TARGETING_CONDITIONS.test(paramServerLevel, (LivingEntity)this, (LivingEntity)paramMob));
/* 146 */         for (Mob mob : list) {
/* 147 */           if (mob.isAlive())
/* 148 */             touch(serverLevel, mob); 
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void touch(ServerLevel paramServerLevel, Mob paramMob) {
/* 155 */     int i = getPuffState();
/* 156 */     if (paramMob.hurtServer(paramServerLevel, damageSources().mobAttack((LivingEntity)this), (1 + i))) {
/* 157 */       paramMob.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), (Entity)this);
/* 158 */       playSound(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player paramPlayer) {
/* 164 */     int i = getPuffState();
/* 165 */     if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer; if (i > 0 && 
/* 166 */         paramPlayer.hurtServer(serverPlayer.level(), damageSources().mobAttack((LivingEntity)this), (1 + i))) {
/* 167 */         if (!isSilent()) {
/* 168 */           serverPlayer.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
/*     */         }
/* 170 */         paramPlayer.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), (Entity)this);
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 177 */     return SoundEvents.PUFFER_FISH_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 182 */     return SoundEvents.PUFFER_FISH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 187 */     return SoundEvents.PUFFER_FISH_FLOP;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 192 */     return super.getDefaultDimensions(paramPose).scale(getScale(getPuffState()));
/*     */   }
/*     */   
/*     */   private static float getScale(int paramInt) {
/* 196 */     switch (paramInt) {
/*     */       case 1:
/* 198 */         return 0.7F;
/*     */       case 0:
/* 200 */         return 0.5F;
/*     */     } 
/* 202 */     return 1.0F;
/*     */   }
/*     */   
/*     */   private static class PufferfishPuffGoal
/*     */     extends Goal {
/*     */     private final Pufferfish fish;
/*     */     
/*     */     public PufferfishPuffGoal(Pufferfish param1Pufferfish) {
/* 210 */       this.fish = param1Pufferfish;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 215 */       List list = this.fish.level().getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), param1LivingEntity -> Pufferfish.TARGETING_CONDITIONS.test(getServerLevel((Entity)this.fish), (LivingEntity)this.fish, param1LivingEntity));
/*     */       
/* 217 */       return !list.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 222 */       this.fish.inflateCounter = 1;
/* 223 */       this.fish.deflateTimer = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 228 */       this.fish.inflateCounter = 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\Pufferfish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */