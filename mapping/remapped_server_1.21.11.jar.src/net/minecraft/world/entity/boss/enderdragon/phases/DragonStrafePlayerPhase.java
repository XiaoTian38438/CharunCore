/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DragonStrafePlayerPhase extends AbstractDragonPhaseInstance {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int FIREBALL_CHARGE_AMOUNT = 5;
/*     */   private int fireballCharge;
/*     */   private Path currentPath;
/*     */   private Vec3 targetLocation;
/*     */   private LivingEntity attackTarget;
/*     */   private boolean holdingPatternClockwise;
/*     */   
/*     */   public DragonStrafePlayerPhase(EnderDragon paramEnderDragon) {
/*  28 */     super(paramEnderDragon);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel paramServerLevel) {
/*  33 */     if (this.attackTarget == null) {
/*  34 */       LOGGER.warn("Skipping player strafe phase because no player was found");
/*  35 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */       
/*     */       return;
/*     */     } 
/*  39 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  40 */       double d3 = this.attackTarget.getX();
/*  41 */       double d4 = this.attackTarget.getZ();
/*     */       
/*  43 */       double d5 = d3 - this.dragon.getX();
/*  44 */       double d6 = d4 - this.dragon.getZ();
/*  45 */       double d7 = Math.sqrt(d5 * d5 + d6 * d6);
/*  46 */       double d8 = Math.min(0.4000000059604645D + d7 / 80.0D - 1.0D, 10.0D);
/*     */       
/*  48 */       this.targetLocation = new Vec3(d3, this.attackTarget.getY() + d8, d4);
/*     */     } 
/*     */     
/*  51 */     double d1 = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  52 */     if (d1 < 100.0D || d1 > 22500.0D) {
/*  53 */       findNewTarget();
/*     */     }
/*     */     
/*  56 */     double d2 = 64.0D;
/*  57 */     if (this.attackTarget.distanceToSqr((Entity)this.dragon) < 4096.0D) {
/*  58 */       if (this.dragon.hasLineOfSight((Entity)this.attackTarget)) {
/*  59 */         this.fireballCharge++;
/*  60 */         Vec3 vec31 = (new Vec3(this.attackTarget.getX() - this.dragon.getX(), 0.0D, this.attackTarget.getZ() - this.dragon.getZ())).normalize();
/*  61 */         Vec3 vec32 = (new Vec3(Mth.sin((this.dragon.getYRot() * 0.017453292F)), 0.0D, -Mth.cos((this.dragon.getYRot() * 0.017453292F)))).normalize();
/*  62 */         float f1 = (float)vec32.dot(vec31);
/*  63 */         float f2 = (float)(Math.acos(f1) * 57.2957763671875D);
/*  64 */         f2 += 0.5F;
/*     */         
/*  66 */         if (this.fireballCharge >= 5 && f2 >= 0.0F && f2 < 10.0F) {
/*  67 */           double d3 = 1.0D;
/*  68 */           Vec3 vec33 = this.dragon.getViewVector(1.0F);
/*  69 */           double d4 = this.dragon.head.getX() - vec33.x * 1.0D;
/*  70 */           double d5 = this.dragon.head.getY(0.5D) + 0.5D;
/*  71 */           double d6 = this.dragon.head.getZ() - vec33.z * 1.0D;
/*     */           
/*  73 */           double d7 = this.attackTarget.getX() - d4;
/*  74 */           double d8 = this.attackTarget.getY(0.5D) - d5;
/*  75 */           double d9 = this.attackTarget.getZ() - d6;
/*  76 */           Vec3 vec34 = new Vec3(d7, d8, d9);
/*     */           
/*  78 */           if (!this.dragon.isSilent()) {
/*  79 */             paramServerLevel.levelEvent(null, 1017, this.dragon.blockPosition(), 0);
/*     */           }
/*  81 */           DragonFireball dragonFireball = new DragonFireball((Level)paramServerLevel, (LivingEntity)this.dragon, vec34.normalize());
/*  82 */           dragonFireball.snapTo(d4, d5, d6, 0.0F, 0.0F);
/*  83 */           paramServerLevel.addFreshEntity((Entity)dragonFireball);
/*  84 */           this.fireballCharge = 0;
/*     */           
/*  86 */           if (this.currentPath != null) {
/*  87 */             while (!this.currentPath.isDone()) {
/*  88 */               this.currentPath.advance();
/*     */             }
/*     */           }
/*     */           
/*  92 */           this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */         }
/*     */       
/*  95 */       } else if (this.fireballCharge > 0) {
/*  96 */         this.fireballCharge--;
/*     */       }
/*     */     
/*     */     }
/* 100 */     else if (this.fireballCharge > 0) {
/* 101 */       this.fireballCharge--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNewTarget() {
/* 107 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 108 */       int i = this.dragon.findClosestNode();
/* 109 */       int j = i;
/*     */       
/* 111 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/* 112 */         this.holdingPatternClockwise = !this.holdingPatternClockwise;
/* 113 */         j += 6;
/*     */       } 
/*     */       
/* 116 */       if (this.holdingPatternClockwise) {
/* 117 */         j++;
/*     */       } else {
/* 119 */         j--;
/*     */       } 
/*     */       
/* 122 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*     */         
/* 124 */         j -= 12;
/* 125 */         j &= 0x7;
/* 126 */         j += 12;
/*     */       } else {
/*     */         
/* 129 */         j %= 12;
/* 130 */         if (j < 0) {
/* 131 */           j += 12;
/*     */         }
/*     */       } 
/*     */       
/* 135 */       this.currentPath = this.dragon.findPath(i, j, null);
/*     */       
/* 137 */       if (this.currentPath != null) {
/* 138 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 142 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 146 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 147 */       double d2; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 149 */       this.currentPath.advance();
/* 150 */       double d1 = blockPos.getX();
/*     */       
/* 152 */       double d3 = blockPos.getZ();
/*     */       
/*     */       do {
/* 155 */         d2 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 156 */       } while (d2 < blockPos.getY());
/*     */       
/* 158 */       this.targetLocation = new Vec3(d1, d2, d3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 164 */     this.fireballCharge = 0;
/* 165 */     this.targetLocation = null;
/* 166 */     this.currentPath = null;
/* 167 */     this.attackTarget = null;
/*     */   }
/*     */   
/*     */   public void setTarget(LivingEntity paramLivingEntity) {
/* 171 */     this.attackTarget = paramLivingEntity;
/*     */     
/* 173 */     int i = this.dragon.findClosestNode();
/* 174 */     int j = this.dragon.findClosestNode(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
/*     */     
/* 176 */     int k = this.attackTarget.getBlockX();
/* 177 */     int m = this.attackTarget.getBlockZ();
/*     */     
/* 179 */     double d1 = k - this.dragon.getX();
/* 180 */     double d2 = m - this.dragon.getZ();
/* 181 */     double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/* 182 */     double d4 = Math.min(0.4000000059604645D + d3 / 80.0D - 1.0D, 10.0D);
/* 183 */     int n = Mth.floor(this.attackTarget.getY() + d4);
/*     */     
/* 185 */     Node node = new Node(k, n, m);
/*     */     
/* 187 */     this.currentPath = this.dragon.findPath(i, j, node);
/*     */     
/* 189 */     if (this.currentPath != null) {
/* 190 */       this.currentPath.advance();
/*     */       
/* 192 */       navigateToNextPathNode();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getFlyTargetLocation() {
/* 198 */     return this.targetLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonStrafePlayerPhase> getPhase() {
/* 203 */     return EnderDragonPhase.STRAFE_PLAYER;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonStrafePlayerPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */