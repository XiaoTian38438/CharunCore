/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Node;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonLandingApproachPhase
/*    */   extends AbstractDragonPhaseInstance {
/* 17 */   private static final TargetingConditions NEAR_EGG_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
/*    */   
/*    */   private Path currentPath;
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonLandingApproachPhase(EnderDragon paramEnderDragon) {
/* 23 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonLandingApproachPhase> getPhase() {
/* 28 */     return EnderDragonPhase.LANDING_APPROACH;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 33 */     this.currentPath = null;
/* 34 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 39 */     double d = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 40 */     if (d < 100.0D || d > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 41 */       findNewTarget(paramServerLevel);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 47 */     return this.targetLocation;
/*    */   }
/*    */   
/*    */   private void findNewTarget(ServerLevel paramServerLevel) {
/* 51 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 52 */       int j, i = this.dragon.findClosestNode();
/* 53 */       BlockPos blockPos = paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/* 54 */       Player player = paramServerLevel.getNearestPlayer(NEAR_EGG_TARGETING, (LivingEntity)this.dragon, blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*    */ 
/*    */       
/* 57 */       if (player != null) {
/* 58 */         Vec3 vec3 = (new Vec3(player.getX(), 0.0D, player.getZ())).normalize();
/* 59 */         j = this.dragon.findClosestNode(-vec3.x * 40.0D, 105.0D, -vec3.z * 40.0D);
/*    */       } else {
/* 61 */         j = this.dragon.findClosestNode(40.0D, blockPos.getY(), 0.0D);
/*    */       } 
/*    */       
/* 64 */       Node node = new Node(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*    */       
/* 66 */       this.currentPath = this.dragon.findPath(i, j, node);
/*    */       
/* 68 */       if (this.currentPath != null) {
/* 69 */         this.currentPath.advance();
/*    */       }
/*    */     } 
/*    */     
/* 73 */     navigateToNextPathNode();
/*    */     
/* 75 */     if (this.currentPath != null && this.currentPath.isDone()) {
/* 76 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
/*    */     }
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 81 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 82 */       double d3; BlockPos blockPos = this.currentPath.getNextNodePos();
/*    */       
/* 84 */       this.currentPath.advance();
/* 85 */       double d1 = blockPos.getX();
/* 86 */       double d2 = blockPos.getZ();
/*    */ 
/*    */       
/*    */       do {
/* 90 */         d3 = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 91 */       } while (d3 < blockPos.getY());
/*    */       
/* 93 */       this.targetLocation = new Vec3(d1, d3, d2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingApproachPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */