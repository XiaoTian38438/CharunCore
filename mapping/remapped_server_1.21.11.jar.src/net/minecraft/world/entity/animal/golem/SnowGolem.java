/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SnowGolem extends AbstractGolem implements Shearable, RangedAttackMob {
/*  47 */   private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final byte PUMPKIN_FLAG = 16;
/*     */   private static final boolean DEFAULT_PUMPKIN = true;
/*     */   
/*     */   public SnowGolem(EntityType<? extends SnowGolem> paramEntityType, Level paramLevel) {
/*  53 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  58 */     this.goalSelector.addGoal(1, (Goal)new RangedAttackGoal(this, 1.25D, 20, 10.0F));
/*  59 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
/*  60 */     this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  61 */     this.goalSelector.addGoal(4, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  63 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 10, true, false, (paramLivingEntity, paramServerLevel) -> paramLivingEntity instanceof net.minecraft.world.entity.monster.Enemy));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  67 */     return Mob.createMobAttributes()
/*  68 */       .add(Attributes.MAX_HEALTH, 4.0D)
/*  69 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  74 */     super.defineSynchedData(paramBuilder);
/*  75 */     paramBuilder.define(DATA_PUMPKIN_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  80 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  82 */     paramValueOutput.putBoolean("Pumpkin", hasPumpkin());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  87 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/*  89 */     setPumpkin(paramValueInput.getBooleanOr("Pumpkin", true));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  99 */     super.aiStep();
/*     */     
/* 101 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 102 */       if (((Boolean)serverLevel.environmentAttributes().getValue(EnvironmentAttributes.SNOW_GOLEM_MELTS, position())).booleanValue()) {
/* 103 */         hurtServer(serverLevel, damageSources().onFire(), 1.0F);
/*     */       }
/*     */       
/* 106 */       if (!((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 111 */       BlockState blockState = Blocks.SNOW.defaultBlockState();
/* 112 */       for (byte b = 0; b < 4; b++) {
/* 113 */         int i = Mth.floor(getX() + ((b % 2 * 2 - 1) * 0.25F));
/* 114 */         int j = Mth.floor(getY());
/* 115 */         int k = Mth.floor(getZ() + ((b / 2 % 2 * 2 - 1) * 0.25F));
/* 116 */         BlockPos blockPos = new BlockPos(i, j, k);
/* 117 */         if (level().getBlockState(blockPos).isAir() && blockState.canSurvive((LevelReader)level(), blockPos)) {
/* 118 */           level().setBlockAndUpdate(blockPos, blockState);
/* 119 */           level().gameEvent((Holder)GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of((Entity)this, blockState));
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 127 */     double d1 = paramLivingEntity.getX() - getX();
/* 128 */     double d2 = paramLivingEntity.getEyeY() - 1.100000023841858D;
/* 129 */     double d3 = paramLivingEntity.getZ() - getZ();
/* 130 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D;
/*     */     
/* 132 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 133 */       ItemStack itemStack = new ItemStack((ItemLike)Items.SNOWBALL);
/* 134 */       Projectile.spawnProjectile((Projectile)new Snowball((Level)serverLevel, (LivingEntity)this, itemStack), serverLevel, itemStack, paramSnowball -> paramSnowball.shoot(paramDouble1, paramDouble2 + paramDouble3 - paramSnowball.getY(), paramDouble4, 1.6F, 12.0F)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 146 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 147 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 148 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 149 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 150 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)paramPlayer);
/* 151 */         itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot()); }
/*     */       
/* 153 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 155 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack) {
/* 160 */     paramServerLevel.playSound(null, (Entity)this, SoundEvents.SNOW_GOLEM_SHEAR, paramSoundSource, 1.0F, 1.0F);
/*     */     
/* 162 */     setPumpkin(false);
/* 163 */     dropFromShearingLootTable(paramServerLevel, BuiltInLootTables.SHEAR_SNOW_GOLEM, paramItemStack, (paramServerLevel, paramItemStack) -> spawnAtLocation(paramServerLevel, paramItemStack, getEyeHeight()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 168 */     return (isAlive() && hasPumpkin());
/*     */   }
/*     */   
/*     */   public boolean hasPumpkin() {
/* 172 */     return ((((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   public void setPumpkin(boolean paramBoolean) {
/* 176 */     byte b = ((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue();
/* 177 */     if (paramBoolean) {
/* 178 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(b | 0x10)));
/*     */     } else {
/* 180 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(b & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 186 */     return SoundEvents.SNOW_GOLEM_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 191 */     return SoundEvents.SNOW_GOLEM_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 196 */     return SoundEvents.SNOW_GOLEM_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 201 */     return new Vec3(0.0D, (0.75F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\golem\SnowGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */