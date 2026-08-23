/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ 
/*    */ public class FlyingMoveControl extends MoveControl {
/*    */   private final int maxTurn;
/*    */   private final boolean hoversInPlace;
/*    */   
/*    */   public FlyingMoveControl(Mob paramMob, int paramInt, boolean paramBoolean) {
/* 12 */     super(paramMob);
/* 13 */     this.maxTurn = paramInt;
/* 14 */     this.hoversInPlace = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 19 */     if (this.operation == MoveControl.Operation.MOVE_TO) {
/* 20 */       float f2; this.operation = MoveControl.Operation.WAIT;
/*    */       
/* 22 */       this.mob.setNoGravity(true);
/*    */       
/* 24 */       double d1 = this.wantedX - this.mob.getX();
/* 25 */       double d2 = this.wantedY - this.mob.getY();
/* 26 */       double d3 = this.wantedZ - this.mob.getZ();
/* 27 */       double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/* 28 */       if (d4 < 2.500000277905201E-7D) {
/* 29 */         this.mob.setYya(0.0F);
/* 30 */         this.mob.setZza(0.0F);
/*    */         return;
/*    */       } 
/* 33 */       float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/* 34 */       this.mob.setYRot(rotlerp(this.mob.getYRot(), f1, 90.0F));
/*    */       
/* 36 */       if (this.mob.onGround()) {
/* 37 */         f2 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
/*    */       } else {
/* 39 */         f2 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
/*    */       } 
/* 41 */       this.mob.setSpeed(f2);
/*    */       
/* 43 */       double d5 = Math.sqrt(d1 * d1 + d3 * d3);
/* 44 */       if (Math.abs(d2) > 9.999999747378752E-6D || Math.abs(d5) > 9.999999747378752E-6D) {
/* 45 */         float f = (float)-(Mth.atan2(d2, d5) * 57.2957763671875D);
/* 46 */         this.mob.setXRot(rotlerp(this.mob.getXRot(), f, this.maxTurn));
/* 47 */         this.mob.setYya((d2 > 0.0D) ? f2 : -f2);
/*    */       } 
/*    */     } else {
/* 50 */       if (!this.hoversInPlace) {
/* 51 */         this.mob.setNoGravity(false);
/*    */       }
/*    */       
/* 54 */       this.mob.setYya(0.0F);
/* 55 */       this.mob.setZza(0.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\FlyingMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */