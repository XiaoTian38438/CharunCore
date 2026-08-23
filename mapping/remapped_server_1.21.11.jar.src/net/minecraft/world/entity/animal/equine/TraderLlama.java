/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class TraderLlama extends Llama {
/*  29 */   private int despawnDelay = 47999; private static final int DEFAULT_DESPAWN_DELAY = 47999;
/*     */   
/*     */   public TraderLlama(EntityType<? extends TraderLlama> paramEntityType, Level paramLevel) {
/*  32 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraderLlama() {
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Llama makeNewLlama() {
/*  42 */     return (Llama)EntityType.TRADER_LLAMA.create(level(), EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  47 */     super.addAdditionalSaveData(paramValueOutput);
/*  48 */     paramValueOutput.putInt("DespawnDelay", this.despawnDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  53 */     super.readAdditionalSaveData(paramValueInput);
/*  54 */     this.despawnDelay = paramValueInput.getIntOr("DespawnDelay", 47999);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  59 */     super.registerGoals();
/*     */     
/*  61 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*     */     
/*  63 */     this.targetSelector.addGoal(1, (Goal)new TraderLlamaDefendWanderingTraderGoal(this));
/*  64 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Zombie.class, true, (paramLivingEntity, paramServerLevel) -> (paramLivingEntity.getType() != EntityType.ZOMBIFIED_PIGLIN)));
/*  65 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractIllager.class, true));
/*     */   }
/*     */   
/*     */   public void setDespawnDelay(int paramInt) {
/*  69 */     this.despawnDelay = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPlayerRide(Player paramPlayer) {
/*  74 */     Entity entity = getLeashHolder();
/*  75 */     if (entity instanceof WanderingTrader) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     super.doPlayerRide(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  84 */     super.aiStep();
/*     */     
/*  86 */     if (!level().isClientSide()) {
/*  87 */       maybeDespawn();
/*     */     }
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/*  92 */     if (!canDespawn()) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     this.despawnDelay = isLeashedToWanderingTrader() ? (((WanderingTrader)getLeashHolder()).getDespawnDelay() - 1) : (this.despawnDelay - 1);
/*     */     
/*  98 */     if (this.despawnDelay <= 0) {
/*  99 */       removeLeash();
/* 100 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canDespawn() {
/* 105 */     return (!isTamed() && 
/* 106 */       !isLeashedToSomethingOtherThanTheWanderingTrader() && 
/* 107 */       !hasExactlyOnePlayerPassenger());
/*     */   }
/*     */   
/*     */   private boolean isLeashedToWanderingTrader() {
/* 111 */     return getLeashHolder() instanceof WanderingTrader;
/*     */   }
/*     */   
/*     */   private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
/* 115 */     return (isLeashed() && !isLeashedToWanderingTrader());
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 120 */     if (paramEntitySpawnReason == EntitySpawnReason.EVENT) {
/* 121 */       setAge(0);
/*     */     }
/*     */     
/* 124 */     if (paramSpawnGroupData == null) {
/* 125 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false);
/*     */     }
/*     */     
/* 128 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   protected static class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
/*     */     private final Llama llama;
/*     */     private LivingEntity ownerLastHurtBy;
/*     */     private int timestamp;
/*     */     
/*     */     public TraderLlamaDefendWanderingTraderGoal(Llama param1Llama) {
/* 137 */       super((Mob)param1Llama, false);
/* 138 */       this.llama = param1Llama;
/* 139 */       setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 144 */       if (!this.llama.isLeashed()) {
/* 145 */         return false;
/*     */       }
/* 147 */       Entity entity = this.llama.getLeashHolder();
/* 148 */       if (!(entity instanceof WanderingTrader)) {
/* 149 */         return false;
/*     */       }
/*     */       
/* 152 */       WanderingTrader wanderingTrader = (WanderingTrader)entity;
/* 153 */       this.ownerLastHurtBy = wanderingTrader.getLastHurtByMob();
/* 154 */       int i = wanderingTrader.getLastHurtByMobTimestamp();
/* 155 */       return (i != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 160 */       this.mob.setTarget(this.ownerLastHurtBy);
/*     */       
/* 162 */       Entity entity = this.llama.getLeashHolder();
/* 163 */       if (entity instanceof WanderingTrader) {
/* 164 */         this.timestamp = ((WanderingTrader)entity).getLastHurtByMobTimestamp();
/*     */       }
/*     */       
/* 167 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\TraderLlama.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */