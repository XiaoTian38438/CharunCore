/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.InterpolationHandler;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class MinecartBehavior
/*    */ {
/*    */   protected final AbstractMinecart minecart;
/*    */   
/*    */   protected MinecartBehavior(AbstractMinecart paramAbstractMinecart) {
/* 16 */     this.minecart = paramAbstractMinecart;
/*    */   }
/*    */   
/*    */   public InterpolationHandler getInterpolation() {
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public void lerpMotion(Vec3 paramVec3) {
/* 24 */     setDeltaMovement(paramVec3);
/*    */   }
/*    */   
/*    */   public abstract void tick();
/*    */   
/*    */   public Level level() {
/* 30 */     return this.minecart.level();
/*    */   }
/*    */   
/*    */   public abstract void moveAlongTrack(ServerLevel paramServerLevel);
/*    */   
/*    */   public abstract double stepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble);
/*    */   
/*    */   public abstract boolean pushAndPickupEntities();
/*    */   
/*    */   public Vec3 getDeltaMovement() {
/* 40 */     return this.minecart.getDeltaMovement();
/*    */   }
/*    */   
/*    */   public void setDeltaMovement(Vec3 paramVec3) {
/* 44 */     this.minecart.setDeltaMovement(paramVec3);
/*    */   }
/*    */   
/*    */   public void setDeltaMovement(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 48 */     this.minecart.setDeltaMovement(paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   public Vec3 position() {
/* 52 */     return this.minecart.position();
/*    */   }
/*    */   
/*    */   public double getX() {
/* 56 */     return this.minecart.getX();
/*    */   }
/*    */   
/*    */   public double getY() {
/* 60 */     return this.minecart.getY();
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 64 */     return this.minecart.getZ();
/*    */   }
/*    */   
/*    */   public void setPos(Vec3 paramVec3) {
/* 68 */     this.minecart.setPos(paramVec3);
/*    */   }
/*    */   
/*    */   public void setPos(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 72 */     this.minecart.setPos(paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   public float getXRot() {
/* 76 */     return this.minecart.getXRot();
/*    */   }
/*    */   
/*    */   public void setXRot(float paramFloat) {
/* 80 */     this.minecart.setXRot(paramFloat);
/*    */   }
/*    */   
/*    */   public float getYRot() {
/* 84 */     return this.minecart.getYRot();
/*    */   }
/*    */   
/*    */   public void setYRot(float paramFloat) {
/* 88 */     this.minecart.setYRot(paramFloat);
/*    */   }
/*    */   
/*    */   public Direction getMotionDirection() {
/* 92 */     return this.minecart.getDirection();
/*    */   }
/*    */   
/*    */   public Vec3 getKnownMovement(Vec3 paramVec3) {
/* 96 */     return paramVec3;
/*    */   }
/*    */   
/*    */   public abstract double getMaxSpeed(ServerLevel paramServerLevel);
/*    */   
/*    */   public abstract double getSlowdownFactor();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */