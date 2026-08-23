/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Vindicator extends AbstractIllager {
/*     */   private static final String TAG_JOHNNY = "Johnny";
/*     */   
/*     */   static {
/*  53 */     DOOR_BREAKING_PREDICATE = (paramDifficulty -> (paramDifficulty == Difficulty.NORMAL || paramDifficulty == Difficulty.HARD));
/*     */   }
/*     */   static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE; private static final boolean DEFAULT_JOHNNY = false;
/*     */   boolean isJohnny = false;
/*     */   
/*     */   public Vindicator(EntityType<? extends Vindicator> paramEntityType, Level paramLevel) {
/*  59 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*     */     
/*  66 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  67 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Creaking.class, 8.0F, 1.0D, 1.2D));
/*  68 */     this.goalSelector.addGoal(2, (Goal)new VindicatorBreakDoorGoal((Mob)this));
/*  69 */     this.goalSelector.addGoal(3, (Goal)new AbstractIllager.RaiderOpenDoorGoal(this, this));
/*  70 */     this.goalSelector.addGoal(4, (Goal)new Raider.HoldGroundAttackGoal(this, 10.0F));
/*  71 */     this.goalSelector.addGoal(5, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0D, false));
/*  72 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  73 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  74 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, true));
/*  75 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*  76 */     this.targetSelector.addGoal(4, (Goal)new VindicatorJohnnyAttackGoal(this));
/*  77 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  78 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  79 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/*  84 */     if (!isNoAi() && GoalUtils.hasGroundPathNavigation((Mob)this)) {
/*  85 */       boolean bool = paramServerLevel.isRaided(blockPosition());
/*  86 */       getNavigation().setCanOpenDoors(bool);
/*     */     } 
/*     */     
/*  89 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  93 */     return Monster.createMonsterAttributes()
/*  94 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  95 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/*  96 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  97 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 102 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 104 */     if (this.isJohnny) {
/* 105 */       paramValueOutput.putBoolean("Johnny", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 111 */     if (isAggressive())
/* 112 */       return AbstractIllager.IllagerArmPose.ATTACKING; 
/* 113 */     if (isCelebrating()) {
/* 114 */       return AbstractIllager.IllagerArmPose.CELEBRATING;
/*     */     }
/* 116 */     return AbstractIllager.IllagerArmPose.CROSSED;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 121 */     super.readAdditionalSaveData(paramValueInput);
/* 122 */     this.isJohnny = paramValueInput.getBooleanOr("Johnny", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 127 */     return SoundEvents.VINDICATOR_CELEBRATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 132 */     SpawnGroupData spawnGroupData = super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */     
/* 134 */     getNavigation().setCanOpenDoors(true);
/*     */     
/* 136 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 137 */     populateDefaultEquipmentSlots(randomSource, paramDifficultyInstance);
/* 138 */     populateDefaultEquipmentEnchantments(paramServerLevelAccessor, randomSource, paramDifficultyInstance);
/*     */     
/* 140 */     return spawnGroupData;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 145 */     if (getCurrentRaid() == null) {
/* 146 */       setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_AXE));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(Component paramComponent) {
/* 152 */     super.setCustomName(paramComponent);
/* 153 */     if (!this.isJohnny && paramComponent != null && paramComponent.getString().equals("Johnny")) {
/* 154 */       this.isJohnny = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     return SoundEvents.VINDICATOR_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 165 */     return SoundEvents.VINDICATOR_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 170 */     return SoundEvents.VINDICATOR_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean) {
/* 175 */     ItemStack itemStack = new ItemStack((ItemLike)Items.IRON_AXE);
/* 176 */     Raid raid = getCurrentRaid();
/*     */     
/* 178 */     boolean bool = (this.random.nextFloat() <= raid.getEnchantOdds()) ? true : false;
/* 179 */     if (bool) {
/*     */ 
/*     */       
/* 182 */       ResourceKey resourceKey = (paramInt > raid.getNumGroups(Difficulty.NORMAL)) ? VanillaEnchantmentProviders.RAID_VINDICATOR_POST_WAVE_5 : VanillaEnchantmentProviders.RAID_VINDICATOR;
/* 183 */       EnchantmentHelper.enchantItemFromProvider(itemStack, paramServerLevel.registryAccess(), resourceKey, paramServerLevel.getCurrentDifficultyAt(blockPosition()), this.random);
/*     */     } 
/*     */     
/* 186 */     setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/*     */   }
/*     */   
/*     */   private static class VindicatorBreakDoorGoal extends BreakDoorGoal {
/*     */     public VindicatorBreakDoorGoal(Mob param1Mob) {
/* 191 */       super(param1Mob, 6, Vindicator.DOOR_BREAKING_PREDICATE);
/* 192 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 197 */       Vindicator vindicator = (Vindicator)this.mob;
/* 198 */       return (vindicator.hasActiveRaid() && super.canContinueToUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 203 */       Vindicator vindicator = (Vindicator)this.mob;
/* 204 */       return (vindicator.hasActiveRaid() && vindicator.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 209 */       super.start();
/* 210 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */     public VindicatorJohnnyAttackGoal(Vindicator param1Vindicator) {
/* 216 */       super((Mob)param1Vindicator, LivingEntity.class, 0, true, true, (param1LivingEntity, param1ServerLevel) -> param1LivingEntity.attackable());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 221 */       return (((Vindicator)this.mob).isJohnny && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 226 */       super.start();
/* 227 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\illager\Vindicator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */