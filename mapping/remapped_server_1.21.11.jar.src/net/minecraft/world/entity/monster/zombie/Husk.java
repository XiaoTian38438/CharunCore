/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.animal.camel.CamelHusk;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public class Husk
/*     */   extends Zombie
/*     */ {
/*     */   public Husk(EntityType<? extends Husk> paramEntityType, Level paramLevel) {
/*  30 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSunSensitive() {
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  40 */     return SoundEvents.HUSK_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  45 */     return SoundEvents.HUSK_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  50 */     return SoundEvents.HUSK_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/*  55 */     return SoundEvents.HUSK_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/*  60 */     boolean bool = super.doHurtTarget(paramServerLevel, paramEntity);
/*  61 */     if (bool && getMainHandItem().isEmpty() && paramEntity instanceof LivingEntity) {
/*  62 */       float f = paramServerLevel.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/*  63 */       ((LivingEntity)paramEntity).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)f), (Entity)this);
/*     */     } 
/*     */     
/*  66 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doUnderWaterConversion(ServerLevel paramServerLevel) {
/*  76 */     convertToZombieType(paramServerLevel, EntityType.ZOMBIE);
/*  77 */     if (!isSilent()) {
/*  78 */       paramServerLevel.levelEvent(null, 1041, blockPosition(), 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*  84 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/*     */     
/*  86 */     paramSpawnGroupData = super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*  87 */     float f = paramDifficultyInstance.getSpecialMultiplier();
/*     */     
/*  89 */     if (paramEntitySpawnReason != EntitySpawnReason.CONVERSION) {
/*  90 */       setCanPickUpLoot((randomSource.nextFloat() < 0.55F * f));
/*     */     }
/*     */     
/*  93 */     if (paramSpawnGroupData != null) {
/*  94 */       paramSpawnGroupData = new HuskGroupData((Zombie.ZombieGroupData)paramSpawnGroupData);
/*  95 */       ((HuskGroupData)paramSpawnGroupData).triedToSpawnCamelHusk = (paramEntitySpawnReason != EntitySpawnReason.NATURAL);
/*     */     } 
/*  97 */     if (paramSpawnGroupData instanceof HuskGroupData) { HuskGroupData huskGroupData = (HuskGroupData)paramSpawnGroupData;
/*  98 */       if (!huskGroupData.triedToSpawnCamelHusk) {
/*     */         
/* 100 */         BlockPos blockPos = blockPosition();
/* 101 */         if (paramServerLevelAccessor.noCollision(EntityType.CAMEL_HUSK.getSpawnAABB(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D))) {
/*     */           
/* 103 */           huskGroupData.triedToSpawnCamelHusk = true;
/* 104 */           if (randomSource.nextFloat() < 0.1F) {
/*     */             
/* 106 */             setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SPEAR));
/*     */             
/* 108 */             CamelHusk camelHusk = (CamelHusk)EntityType.CAMEL_HUSK.create(level(), EntitySpawnReason.NATURAL);
/* 109 */             if (camelHusk != null) {
/* 110 */               camelHusk.setPos(getX(), getY(), getZ());
/* 111 */               camelHusk.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, null);
/* 112 */               startRiding((Entity)camelHusk, true, true);
/* 113 */               paramServerLevelAccessor.addFreshEntity((Entity)camelHusk);
/*     */               
/* 115 */               Parched parched = (Parched)EntityType.PARCHED.create(level(), EntitySpawnReason.NATURAL);
/* 116 */               if (parched != null) {
/* 117 */                 parched.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 118 */                 parched.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, null);
/* 119 */                 parched.startRiding((Entity)camelHusk, false, false);
/* 120 */                 paramServerLevelAccessor.addFreshEntityWithPassengers((Entity)parched);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */     
/* 127 */     return paramSpawnGroupData;
/*     */   }
/*     */   
/*     */   public static class HuskGroupData extends Zombie.ZombieGroupData {
/*     */     public boolean triedToSpawnCamelHusk = false;
/*     */     
/*     */     public HuskGroupData(Zombie.ZombieGroupData param1ZombieGroupData) {
/* 134 */       super(param1ZombieGroupData.isBaby, param1ZombieGroupData.canSpawnJockey);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\zombie\Husk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */