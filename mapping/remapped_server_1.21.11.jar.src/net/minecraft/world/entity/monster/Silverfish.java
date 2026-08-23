/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class Silverfish
/*     */   extends Monster {
/*     */   private SilverfishWakeUpFriendsGoal friendsGoal;
/*     */   
/*     */   public Silverfish(EntityType<? extends Silverfish> paramEntityType, Level paramLevel) {
/*  39 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  44 */     this.friendsGoal = new SilverfishWakeUpFriendsGoal(this);
/*     */     
/*  46 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  47 */     this.goalSelector.addGoal(1, (Goal)new ClimbOnTopOfPowderSnowGoal((Mob)this, level()));
/*     */     
/*  49 */     this.goalSelector.addGoal(3, this.friendsGoal);
/*     */     
/*  51 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  52 */     this.goalSelector.addGoal(5, (Goal)new SilverfishMergeWithStoneGoal(this));
/*     */     
/*  54 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  55 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  59 */     return Monster.createMonsterAttributes()
/*  60 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  61 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  62 */       .add(Attributes.ATTACK_DAMAGE, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  67 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  72 */     return SoundEvents.SILVERFISH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  77 */     return SoundEvents.SILVERFISH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  82 */     return SoundEvents.SILVERFISH_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  87 */     playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  92 */     if (isInvulnerableTo(paramServerLevel, paramDamageSource)) {
/*  93 */       return false;
/*     */     }
/*  95 */     if ((paramDamageSource.getEntity() != null || paramDamageSource.is(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)) && this.friendsGoal != null) {
/*  96 */       this.friendsGoal.notifyHurt();
/*     */     }
/*  98 */     return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     this.yBodyRot = getYRot();
/*     */     
/* 106 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float paramFloat) {
/* 111 */     setYRot(paramFloat);
/* 112 */     super.setYBodyRot(paramFloat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 118 */     if (InfestedBlock.isCompatibleHostBlock(paramLevelReader.getBlockState(paramBlockPos.below()))) {
/* 119 */       return 10.0F;
/*     */     }
/* 121 */     return super.getWalkTargetValue(paramBlockPos, paramLevelReader);
/*     */   }
/*     */   
/*     */   public static boolean checkSilverfishSpawnRules(EntityType<Silverfish> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 125 */     if (!checkAnyLightMonsterSpawnRules((EntityType)paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource)) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     if (EntitySpawnReason.isSpawner(paramEntitySpawnReason)) {
/* 130 */       return true;
/*     */     }
/*     */     
/* 133 */     Player player = paramLevelAccessor.getNearestPlayer(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, 5.0D, true);
/* 134 */     return (player == null);
/*     */   }
/*     */   
/*     */   private static class SilverfishWakeUpFriendsGoal extends Goal {
/*     */     private final Silverfish silverfish;
/*     */     private int lookForFriends;
/*     */     
/*     */     public SilverfishWakeUpFriendsGoal(Silverfish param1Silverfish) {
/* 142 */       this.silverfish = param1Silverfish;
/*     */     }
/*     */     
/*     */     public void notifyHurt() {
/* 146 */       if (this.lookForFriends == 0) {
/* 147 */         this.lookForFriends = adjustedTickDelay(20);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 153 */       return (this.lookForFriends > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 158 */       this.lookForFriends--;
/* 159 */       if (this.lookForFriends <= 0) {
/* 160 */         Level level = this.silverfish.level();
/* 161 */         RandomSource randomSource = this.silverfish.getRandom();
/*     */ 
/*     */         
/* 164 */         BlockPos blockPos = this.silverfish.blockPosition();
/*     */         
/*     */         int i;
/* 167 */         for (i = 0; i <= 5 && i >= -5; i = ((i <= 0) ? 1 : 0) - i) {
/* 168 */           int j; for (j = 0; j <= 10 && j >= -10; j = ((j <= 0) ? 1 : 0) - j) {
/* 169 */             int k; for (k = 0; k <= 10 && k >= -10; k = ((k <= 0) ? 1 : 0) - k) {
/* 170 */               BlockPos blockPos1 = blockPos.offset(j, i, k);
/* 171 */               BlockState blockState = level.getBlockState(blockPos1);
/*     */               
/* 173 */               Block block = blockState.getBlock();
/* 174 */               if (block instanceof InfestedBlock) {
/* 175 */                 if (((Boolean)getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 176 */                   level.destroyBlock(blockPos1, true, (Entity)this.silverfish);
/*     */                 } else {
/* 178 */                   level.setBlock(blockPos1, ((InfestedBlock)block).hostStateByInfested(level.getBlockState(blockPos1)), 3);
/*     */                 } 
/* 180 */                 if (randomSource.nextBoolean())
/*     */                   // Byte code: goto -> 251 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SilverfishMergeWithStoneGoal
/*     */     extends RandomStrollGoal {
/*     */     private Direction selectedDirection;
/*     */     private boolean doMerge;
/*     */     
/*     */     public SilverfishMergeWithStoneGoal(Silverfish param1Silverfish) {
/* 196 */       super(param1Silverfish, 1.0D, 10);
/*     */       
/* 198 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 203 */       if (this.mob.getTarget() != null) {
/* 204 */         return false;
/*     */       }
/* 206 */       if (!this.mob.getNavigation().isDone()) {
/* 207 */         return false;
/*     */       }
/*     */       
/* 210 */       RandomSource randomSource = this.mob.getRandom();
/* 211 */       if (((Boolean)getServerLevel((Entity)this.mob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && randomSource.nextInt(reducedTickDelay(10)) == 0) {
/* 212 */         this.selectedDirection = Direction.getRandom(randomSource);
/*     */         
/* 214 */         BlockPos blockPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 215 */         BlockState blockState = this.mob.level().getBlockState(blockPos);
/* 216 */         if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 217 */           this.doMerge = true;
/* 218 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 222 */       this.doMerge = false;
/* 223 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 228 */       if (this.doMerge) {
/* 229 */         return false;
/*     */       }
/* 231 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 236 */       if (!this.doMerge) {
/* 237 */         super.start();
/*     */         
/*     */         return;
/*     */       } 
/* 241 */       Level level = this.mob.level();
/* 242 */       BlockPos blockPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 243 */       BlockState blockState = level.getBlockState(blockPos);
/*     */       
/* 245 */       if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 246 */         level.setBlock(blockPos, InfestedBlock.infestedStateByHost(blockState), 3);
/* 247 */         this.mob.spawnAnim();
/* 248 */         this.mob.discard();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Silverfish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */