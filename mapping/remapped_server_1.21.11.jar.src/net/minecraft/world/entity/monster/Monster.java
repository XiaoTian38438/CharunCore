/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class Monster extends PathfinderMob implements Enemy {
/*     */   protected Monster(EntityType<? extends Monster> paramEntityType, Level paramLevel) {
/*  33 */     super(paramEntityType, paramLevel);
/*  34 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  39 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  44 */     updateSwingTime();
/*  45 */     updateNoActionTime();
/*  46 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateNoActionTime() {
/*  50 */     float f = getLightLevelDependentMagicValue();
/*  51 */     if (f > 0.5F) {
/*  52 */       this.noActionTime += 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  58 */     return SoundEvents.HOSTILE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/*  63 */     return SoundEvents.HOSTILE_SPLASH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  68 */     return SoundEvents.HOSTILE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  73 */     return SoundEvents.HOSTILE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity.Fallsounds getFallSounds() {
/*  78 */     return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/*  83 */     return -paramLevelReader.getPathfindingCostFromLightLevels(paramBlockPos);
/*     */   }
/*     */   
/*     */   public static boolean isDarkEnoughToSpawn(ServerLevelAccessor paramServerLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  87 */     if (paramServerLevelAccessor.getBrightness(LightLayer.SKY, paramBlockPos) > paramRandomSource.nextInt(32)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     DimensionType dimensionType = paramServerLevelAccessor.dimensionType();
/*  92 */     int i = dimensionType.monsterSpawnBlockLightLimit();
/*  93 */     if (i < 15 && paramServerLevelAccessor.getBrightness(LightLayer.BLOCK, paramBlockPos) > i) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     int j = paramServerLevelAccessor.getLevel().isThundering() ? paramServerLevelAccessor.getMaxLocalRawBrightness(paramBlockPos, 10) : paramServerLevelAccessor.getMaxLocalRawBrightness(paramBlockPos);
/*  98 */     return (j <= dimensionType.monsterSpawnLightTest().sample(paramRandomSource));
/*     */   }
/*     */   
/*     */   public static boolean checkMonsterSpawnRules(EntityType<? extends Mob> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 102 */     return (paramServerLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && (
/* 103 */       EntitySpawnReason.ignoresLightRequirements(paramEntitySpawnReason) || isDarkEnoughToSpawn(paramServerLevelAccessor, paramBlockPos, paramRandomSource)) && 
/* 104 */       checkMobSpawnRules(paramEntityType, (LevelAccessor)paramServerLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource));
/*     */   }
/*     */   
/*     */   public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 108 */     return (paramLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && 
/* 109 */       checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource));
/*     */   }
/*     */   
/*     */   public static boolean checkSurfaceMonstersSpawnRules(EntityType<? extends Mob> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 113 */     return (checkMonsterSpawnRules(paramEntityType, paramServerLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource) && (
/* 114 */       EntitySpawnReason.isSpawner(paramEntitySpawnReason) || paramServerLevelAccessor.canSeeSky(paramBlockPos)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createMonsterAttributes() {
/* 118 */     return Mob.createMobAttributes()
/* 119 */       .add(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDropLoot(ServerLevel paramServerLevel) {
/* 129 */     return ((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isPreventingPlayerRest(ServerLevel paramServerLevel, Player paramPlayer) {
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getProjectile(ItemStack paramItemStack) {
/* 138 */     if (paramItemStack.getItem() instanceof ProjectileWeaponItem) {
/* 139 */       Predicate predicate = ((ProjectileWeaponItem)paramItemStack.getItem()).getSupportedHeldProjectiles();
/* 140 */       ItemStack itemStack = ProjectileWeaponItem.getHeldProjectile((LivingEntity)this, predicate);
/* 141 */       return itemStack.isEmpty() ? new ItemStack((ItemLike)Items.ARROW) : itemStack;
/*     */     } 
/* 143 */     return ItemStack.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Monster.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */