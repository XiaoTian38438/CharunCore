/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class AbstractPiglin extends Monster {
/*  25 */   protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   private static final boolean DEFAULT_PICK_UP_LOOT = true;
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*  30 */   protected int timeInOverworld = 0;
/*     */   
/*     */   public AbstractPiglin(EntityType<? extends AbstractPiglin> paramEntityType, Level paramLevel) {
/*  33 */     super(paramEntityType, paramLevel);
/*  34 */     setCanPickUpLoot(true);
/*  35 */     applyOpenDoorsAbility();
/*  36 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  37 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   private void applyOpenDoorsAbility() {
/*  41 */     if (GoalUtils.hasGroundPathNavigation((Mob)this)) {
/*  42 */       getNavigation().setCanOpenDoors(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImmuneToZombification(boolean paramBoolean) {
/*  49 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   protected boolean isImmuneToZombification() {
/*  53 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  58 */     super.defineSynchedData(paramBuilder);
/*  59 */     paramBuilder.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  64 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  66 */     paramValueOutput.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/*  67 */     paramValueOutput.putInt("TimeInOverworld", this.timeInOverworld);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  72 */     super.readAdditionalSaveData(paramValueInput);
/*     */ 
/*     */     
/*  75 */     setCanPickUpLoot(paramValueInput.getBooleanOr("CanPickUpLoot", true));
/*     */     
/*  77 */     setImmuneToZombification(paramValueInput.getBooleanOr("IsImmuneToZombification", false));
/*  78 */     this.timeInOverworld = paramValueInput.getIntOr("TimeInOverworld", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/*  83 */     super.customServerAiStep(paramServerLevel);
/*     */     
/*  85 */     if (isConverting()) {
/*  86 */       this.timeInOverworld++;
/*     */     } else {
/*  88 */       this.timeInOverworld = 0;
/*     */     } 
/*  90 */     if (this.timeInOverworld > 300) {
/*  91 */       playConvertedSound();
/*  92 */       finishConversion(paramServerLevel);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setTimeInOverworld(int paramInt) {
/*  98 */     this.timeInOverworld = paramInt;
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 102 */     return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue());
/*     */   }
/*     */   
/*     */   protected void finishConversion(ServerLevel paramServerLevel) {
/* 106 */     convertTo(EntityType.ZOMBIFIED_PIGLIN, 
/*     */         
/* 108 */         ConversionParams.single((Mob)this, true, true), paramZombifiedPiglin -> paramZombifiedPiglin.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdult() {
/* 114 */     return !isBaby();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LivingEntity getTarget() {
/* 121 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   protected boolean isHoldingMeleeWeapon() {
/* 125 */     return getMainHandItem().has(DataComponents.TOOL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 130 */     if (PiglinAi.isIdle(this))
/* 131 */       super.playAmbientSound(); 
/*     */   }
/*     */   
/*     */   protected abstract boolean canHunt();
/*     */   
/*     */   public abstract PiglinArmPose getArmPose();
/*     */   
/*     */   protected abstract void playConvertedSound();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\AbstractPiglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */