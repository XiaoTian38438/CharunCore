/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ZombieHorse
/*     */   extends AbstractHorse
/*     */ {
/*     */   private static final float SPEED_FACTOR = 42.16F;
/*     */   private static final double BASE_JUMP_STRENGTH = 0.5D;
/*  47 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ZOMBIE_HORSE.getDimensions()
/*  48 */     .withAttachments(EntityAttachments.builder()
/*  49 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.ZOMBIE_HORSE.getHeight() - 0.03125F, 0.0F))
/*     */     
/*  51 */     .scale(0.5F); private static final double PER_RANDOM_JUMP_STRENGTH = 0.06666666666666667D; private static final double BASE_SPEED = 9.0D; private static final double PER_RANDOM_SPEED = 1.0D;
/*     */   
/*     */   public ZombieHorse(EntityType<? extends ZombieHorse> paramEntityType, Level paramLevel) {
/*  54 */     super((EntityType)paramEntityType, paramLevel);
/*  55 */     setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
/*  56 */     setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  60 */     return createBaseHorseAttributes()
/*  61 */       .add(Attributes.MAX_HEALTH, 25.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  66 */     setPersistenceRequired();
/*  67 */     return super.interact(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMobControlled() {
/*  77 */     return getFirstPassenger() instanceof Mob;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes(RandomSource paramRandomSource) {
/*  82 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateZombieHorseJumpStrength(paramRandomSource::nextDouble));
/*  83 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateZombieHorseSpeed(paramRandomSource::nextDouble));
/*     */   }
/*     */   
/*     */   private static double generateZombieHorseJumpStrength(DoubleSupplier paramDoubleSupplier) {
/*  87 */     return 0.5D + paramDoubleSupplier.getAsDouble() * 0.06666666666666667D + paramDoubleSupplier.getAsDouble() * 0.06666666666666667D + paramDoubleSupplier.getAsDouble() * 0.06666666666666667D;
/*     */   }
/*     */   
/*     */   private static double generateZombieHorseSpeed(DoubleSupplier paramDoubleSupplier) {
/*  91 */     return (9.0D + paramDoubleSupplier.getAsDouble() * 1.0D + paramDoubleSupplier.getAsDouble() * 1.0D + paramDoubleSupplier.getAsDouble() * 1.0D) / 42.15999984741211D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  96 */     return SoundEvents.ZOMBIE_HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 101 */     return SoundEvents.ZOMBIE_HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 106 */     return SoundEvents.ZOMBIE_HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 111 */     return SoundEvents.ZOMBIE_HORSE_ANGRY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 116 */     return SoundEvents.ZOMBIE_HORSE_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/* 131 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 132 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, paramItemStack -> paramItemStack.is(ItemTags.ZOMBIE_HORSE_FOOD), false));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 137 */     if (paramEntitySpawnReason == EntitySpawnReason.NATURAL) {
/* 138 */       Zombie zombie = (Zombie)EntityType.ZOMBIE.create(level(), EntitySpawnReason.JOCKEY);
/* 139 */       if (zombie != null) {
/* 140 */         zombie.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 141 */         zombie.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, null);
/* 142 */         zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SPEAR));
/* 143 */         zombie.startRiding((Entity)this, false, false);
/*     */       } 
/*     */     } 
/* 146 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 151 */     boolean bool = (!isBaby() && isTamed() && paramPlayer.isSecondaryUseActive()) ? true : false;
/* 152 */     if (isVehicle() || bool) {
/* 153 */       return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */     }
/*     */     
/* 156 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*     */     
/* 158 */     if (!itemStack.isEmpty()) {
/* 159 */       if (isFood(itemStack)) {
/* 160 */         return fedFood(paramPlayer, itemStack);
/*     */       }
/*     */       
/* 163 */       if (!isTamed()) {
/* 164 */         makeMad();
/* 165 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 168 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot paramEquipmentSlot) {
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/* 178 */     return (isTamed() || !isMobControlled());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack paramItemStack) {
/* 183 */     return paramItemStack.is(ItemTags.ZOMBIE_HORSE_FOOD);
/*     */   }
/*     */ 
/*     */   
/*     */   protected EquipmentSlot sunProtectionSlot() {
/* 188 */     return EquipmentSlot.BODY;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3[] getQuadLeashOffsets() {
/* 193 */     return Leashable.createQuadLeashOffsets((Entity)this, 0.04D, 0.41D, 0.18D, 0.73D);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 198 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */ 
/*     */   
/*     */   public float chargeSpeedModifier() {
/* 203 */     return 1.4F;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\ZombieHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */