/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DragonChargePlayerPhase
/*    */   extends AbstractDragonPhaseInstance {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final int CHARGE_RECOVERY_TIME = 10;
/*    */   private Vec3 targetLocation;
/*    */   private int timeSinceCharge;
/*    */   
/*    */   public DragonChargePlayerPhase(EnderDragon paramEnderDragon) {
/* 18 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 23 */     if (this.targetLocation == null) {
/* 24 */       LOGGER.warn("Aborting charge player as no target was set.");
/* 25 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (this.timeSinceCharge > 0 && 
/* 30 */       this.timeSinceCharge++ >= 10) {
/* 31 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 36 */     double d = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 37 */     if (d < 100.0D || d > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 38 */       this.timeSinceCharge++;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 44 */     this.targetLocation = null;
/* 45 */     this.timeSinceCharge = 0;
/*    */   }
/*    */   
/*    */   public void setTarget(Vec3 paramVec3) {
/* 49 */     this.targetLocation = paramVec3;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 54 */     return 3.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 59 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonChargePlayerPhase> getPhase() {
/* 64 */     return EnderDragonPhase.CHARGING_PLAYER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonChargePlayerPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */