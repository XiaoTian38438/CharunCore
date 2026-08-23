/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonTakeoffPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   private boolean firstTick;
/*    */   private Path currentPath;
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonTakeoffPhase(EnderDragon paramEnderDragon) {
/* 19 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 24 */     if (this.firstTick || this.currentPath == null) {
/* 25 */       this.firstTick = false;
/* 26 */       findNewTarget();
/*    */     } else {
/* 28 */       BlockPos blockPos = paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/* 29 */       if (!blockPos.closerToCenterThan((Position)this.dragon.position(), 10.0D)) {
/* 30 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 37 */     this.firstTick = true;
/* 38 */     this.currentPath = null;
/* 39 */     this.targetLocation = null;
/*    */   }
/*    */   
/*    */   private void findNewTarget() {
/* 43 */     int i = this.dragon.findClosestNode();
/* 44 */     Vec3 vec3 = this.dragon.getHeadLookVector(1.0F);
/* 45 */     int j = this.dragon.findClosestNode(-vec3.x * 40.0D, 105.0D, -vec3.z * 40.0D);
/*    */     
/* 47 */     if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*    */       
/* 49 */       j -= 12;
/* 50 */       j &= 0x7;
/* 51 */       j += 12;
/*    */     } else {
/*    */       
/* 54 */       j %= 12;
/* 55 */       if (j < 0) {
/* 56 */         j += 12;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     this.currentPath = this.dragon.findPath(i, j, null);
/*    */     
/* 62 */     navigateToNextPathNode();
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 66 */     if (this.currentPath != null) {
/* 67 */       this.currentPath.advance();
/* 68 */       if (!this.currentPath.isDone()) {
/* 69 */         double d; BlockPos blockPos = this.currentPath.getNextNodePos();
/* 70 */         this.currentPath.advance();
/*    */ 
/*    */         
/*    */         do {
/* 74 */           d = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 75 */         } while (d < blockPos.getY());
/*    */         
/* 77 */         this.targetLocation = new Vec3(blockPos.getX(), d, blockPos.getZ());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 84 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonTakeoffPhase> getPhase() {
/* 89 */     return EnderDragonPhase.TAKEOFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonTakeoffPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */