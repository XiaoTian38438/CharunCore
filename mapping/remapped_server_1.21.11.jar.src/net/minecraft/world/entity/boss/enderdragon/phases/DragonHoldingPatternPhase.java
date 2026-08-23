/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DragonHoldingPatternPhase extends AbstractDragonPhaseInstance {
/*  18 */   private static final TargetingConditions NEW_TARGET_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
/*     */   
/*     */   private Path currentPath;
/*     */   private Vec3 targetLocation;
/*     */   private boolean clockwise;
/*     */   
/*     */   public DragonHoldingPatternPhase(EnderDragon paramEnderDragon) {
/*  25 */     super(paramEnderDragon);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonHoldingPatternPhase> getPhase() {
/*  30 */     return EnderDragonPhase.HOLDING_PATTERN;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel paramServerLevel) {
/*  35 */     double d = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  36 */     if (d < 100.0D || d > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/*  37 */       findNewTarget(paramServerLevel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  43 */     this.currentPath = null;
/*  44 */     this.targetLocation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getFlyTargetLocation() {
/*  49 */     return this.targetLocation;
/*     */   }
/*     */   
/*     */   private void findNewTarget(ServerLevel paramServerLevel) {
/*  53 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  54 */       double d; BlockPos blockPos = paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/*     */ 
/*     */ 
/*     */       
/*  58 */       byte b = (this.dragon.getDragonFight() == null) ? 0 : this.dragon.getDragonFight().getCrystalsAlive();
/*     */       
/*  60 */       if (this.dragon.getRandom().nextInt(b + 3) == 0) {
/*  61 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING_APPROACH);
/*     */         
/*     */         return;
/*     */       } 
/*  65 */       Player player = paramServerLevel.getNearestPlayer(NEW_TARGET_TARGETING, (LivingEntity)this.dragon, blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*  66 */       if (player != null) {
/*  67 */         d = blockPos.distToCenterSqr((Position)player.position()) / 512.0D;
/*     */       } else {
/*  69 */         d = 64.0D;
/*     */       } 
/*  71 */       if (player != null && (this.dragon.getRandom().nextInt((int)(d + 2.0D)) == 0 || this.dragon.getRandom().nextInt(b + 2) == 0)) {
/*     */         
/*  73 */         strafePlayer(player);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*  79 */     if (this.currentPath == null || this.currentPath.isDone()) {
/*  80 */       int i = this.dragon.findClosestNode();
/*  81 */       int j = i;
/*     */       
/*  83 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/*  84 */         this.clockwise = !this.clockwise;
/*  85 */         j += 6;
/*     */       } 
/*     */       
/*  88 */       if (this.clockwise) {
/*  89 */         j++;
/*     */       } else {
/*  91 */         j--;
/*     */       } 
/*     */       
/*  94 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() < 0) {
/*     */         
/*  96 */         j -= 12;
/*  97 */         j &= 0x7;
/*  98 */         j += 12;
/*     */       } else {
/*     */         
/* 101 */         j %= 12;
/* 102 */         if (j < 0) {
/* 103 */           j += 12;
/*     */         }
/*     */       } 
/*     */       
/* 107 */       this.currentPath = this.dragon.findPath(i, j, null);
/* 108 */       if (this.currentPath != null) {
/* 109 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 113 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void strafePlayer(Player paramPlayer) {
/* 117 */     this.dragon.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
/* 118 */     ((DragonStrafePlayerPhase)this.dragon.getPhaseManager().<DragonStrafePlayerPhase>getPhase(EnderDragonPhase.STRAFE_PLAYER)).setTarget((LivingEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 122 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 123 */       double d3; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 125 */       this.currentPath.advance();
/* 126 */       double d1 = blockPos.getX();
/* 127 */       double d2 = blockPos.getZ();
/*     */ 
/*     */       
/*     */       do {
/* 131 */         d3 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 132 */       } while (d3 < blockPos.getY());
/*     */       
/* 134 */       this.targetLocation = new Vec3(d1, d3, d2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal paramEndCrystal, BlockPos paramBlockPos, DamageSource paramDamageSource, Player paramPlayer) {
/* 140 */     if (paramPlayer != null && this.dragon.canAttack((LivingEntity)paramPlayer))
/* 141 */       strafePlayer(paramPlayer); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonHoldingPatternPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */