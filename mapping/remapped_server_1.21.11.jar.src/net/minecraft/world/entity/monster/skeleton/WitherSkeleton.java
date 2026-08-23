/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
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
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ 
/*     */ public class WitherSkeleton extends AbstractSkeleton {
/*     */   public WitherSkeleton(EntityType<? extends WitherSkeleton> paramEntityType, Level paramLevel) {
/*  34 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  36 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  41 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractPiglin.class, true));
/*  42 */     super.registerGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  47 */     return SoundEvents.WITHER_SKELETON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  52 */     return SoundEvents.WITHER_SKELETON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  57 */     return SoundEvents.WITHER_SKELETON_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   SoundEvent getStepSound() {
/*  62 */     return SoundEvents.WITHER_SKELETON_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHoldItem(ItemStack paramItemStack) {
/*  72 */     return (!paramItemStack.is(ItemTags.WITHER_SKELETON_DISLIKED_WEAPONS) && super.canHoldItem(paramItemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/*  77 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.STONE_SWORD));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {}
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*  86 */     SpawnGroupData spawnGroupData = super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */     
/*  88 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
/*     */     
/*  90 */     reassessWeaponGoal();
/*     */     
/*  92 */     return spawnGroupData;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/*  97 */     if (!super.doHurtTarget(paramServerLevel, paramEntity)) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     if (paramEntity instanceof LivingEntity) {
/* 102 */       ((LivingEntity)paramEntity).addEffect(new MobEffectInstance(MobEffects.WITHER, 200), (Entity)this);
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack paramItemStack1, float paramFloat, ItemStack paramItemStack2) {
/* 109 */     AbstractArrow abstractArrow = super.getArrow(paramItemStack1, paramFloat, paramItemStack2);
/* 110 */     abstractArrow.igniteForSeconds(100.0F);
/* 111 */     return abstractArrow;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance paramMobEffectInstance) {
/* 116 */     if (paramMobEffectInstance.is(MobEffects.WITHER)) {
/* 117 */       return false;
/*     */     }
/* 119 */     return super.canBeAffected(paramMobEffectInstance);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\skeleton\WitherSkeleton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */