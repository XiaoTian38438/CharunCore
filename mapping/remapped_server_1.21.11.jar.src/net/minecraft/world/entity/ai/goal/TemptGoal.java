/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TemptGoal extends Goal {
/*  17 */   private static final TargetingConditions TEMPT_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
/*     */   
/*     */   private static final double DEFAULT_STOP_DISTANCE = 2.5D;
/*     */   private final TargetingConditions targetingConditions;
/*     */   protected final Mob mob;
/*     */   protected final double speedModifier;
/*     */   private double px;
/*     */   private double py;
/*     */   private double pz;
/*     */   private double pRotX;
/*     */   private double pRotY;
/*     */   protected Player player;
/*     */   private int calmDown;
/*     */   private boolean isRunning;
/*     */   private final Predicate<ItemStack> items;
/*     */   private final boolean canScare;
/*     */   private final double stopDistance;
/*     */   
/*     */   public TemptGoal(PathfinderMob paramPathfinderMob, double paramDouble, Predicate<ItemStack> paramPredicate, boolean paramBoolean) {
/*  36 */     this((Mob)paramPathfinderMob, paramDouble, paramPredicate, paramBoolean, 2.5D);
/*     */   }
/*     */   
/*     */   public TemptGoal(PathfinderMob paramPathfinderMob, double paramDouble1, Predicate<ItemStack> paramPredicate, boolean paramBoolean, double paramDouble2) {
/*  40 */     this((Mob)paramPathfinderMob, paramDouble1, paramPredicate, paramBoolean, paramDouble2);
/*     */   }
/*     */   
/*     */   TemptGoal(Mob paramMob, double paramDouble1, Predicate<ItemStack> paramPredicate, boolean paramBoolean, double paramDouble2) {
/*  44 */     this.mob = paramMob;
/*  45 */     this.speedModifier = paramDouble1;
/*  46 */     this.items = paramPredicate;
/*  47 */     this.canScare = paramBoolean;
/*  48 */     this.stopDistance = paramDouble2;
/*  49 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  50 */     this.targetingConditions = TEMPT_TARGETING.copy().selector((paramLivingEntity, paramServerLevel) -> shouldFollow(paramLivingEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  55 */     if (this.calmDown > 0) {
/*  56 */       this.calmDown--;
/*  57 */       return false;
/*     */     } 
/*  59 */     this.player = getServerLevel((Entity)this.mob).getNearestPlayer(this.targetingConditions.range(this.mob.getAttributeValue(Attributes.TEMPT_RANGE)), (LivingEntity)this.mob);
/*  60 */     return (this.player != null);
/*     */   }
/*     */   
/*     */   private boolean shouldFollow(LivingEntity paramLivingEntity) {
/*  64 */     return (this.items.test(paramLivingEntity.getMainHandItem()) || this.items.test(paramLivingEntity.getOffhandItem()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  69 */     if (canScare()) {
/*  70 */       if (this.mob.distanceToSqr((Entity)this.player) < 36.0D) {
/*  71 */         if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D) {
/*  72 */           return false;
/*     */         }
/*  74 */         if (Math.abs(this.player.getXRot() - this.pRotX) > 5.0D || Math.abs(this.player.getYRot() - this.pRotY) > 5.0D) {
/*  75 */           return false;
/*     */         }
/*     */       } else {
/*  78 */         this.px = this.player.getX();
/*  79 */         this.py = this.player.getY();
/*  80 */         this.pz = this.player.getZ();
/*     */       } 
/*  82 */       this.pRotX = this.player.getXRot();
/*  83 */       this.pRotY = this.player.getYRot();
/*     */     } 
/*  85 */     return canUse();
/*     */   }
/*     */   
/*     */   protected boolean canScare() {
/*  89 */     return this.canScare;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  94 */     this.px = this.player.getX();
/*  95 */     this.py = this.player.getY();
/*  96 */     this.pz = this.player.getZ();
/*  97 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 102 */     this.player = null;
/* 103 */     stopNavigation();
/* 104 */     this.calmDown = reducedTickDelay(100);
/* 105 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 110 */     this.mob.getLookControl().setLookAt((Entity)this.player, (this.mob.getMaxHeadYRot() + 20), this.mob.getMaxHeadXRot());
/* 111 */     if (this.mob.distanceToSqr((Entity)this.player) < this.stopDistance * this.stopDistance) {
/* 112 */       stopNavigation();
/*     */     } else {
/* 114 */       navigateTowards(this.player);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void stopNavigation() {
/* 119 */     this.mob.getNavigation().stop();
/*     */   }
/*     */   
/*     */   protected void navigateTowards(Player paramPlayer) {
/* 123 */     this.mob.getNavigation().moveTo((Entity)paramPlayer, this.speedModifier);
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/* 127 */     return this.isRunning;
/*     */   }
/*     */   
/*     */   public static class ForNonPathfinders extends TemptGoal {
/*     */     public ForNonPathfinders(Mob param1Mob, double param1Double1, Predicate<ItemStack> param1Predicate, boolean param1Boolean, double param1Double2) {
/* 132 */       super(param1Mob, param1Double1, param1Predicate, param1Boolean, param1Double2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void stopNavigation() {
/* 137 */       this.mob.getMoveControl().setWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void navigateTowards(Player param1Player) {
/* 142 */       Vec3 vec3 = param1Player.getEyePosition().subtract(this.mob.position()).scale(this.mob.getRandom().nextDouble()).add(this.mob.position());
/* 143 */       this.mob.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\TemptGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */