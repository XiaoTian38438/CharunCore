/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OpenDoorGoal extends DoorInteractGoal {
/*    */   private final boolean closeDoor;
/*    */   private int forgetTime;
/*    */   
/*    */   public OpenDoorGoal(Mob paramMob, boolean paramBoolean) {
/* 10 */     super(paramMob);
/* 11 */     this.mob = paramMob;
/* 12 */     this.closeDoor = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 17 */     return (this.closeDoor && this.forgetTime > 0 && super.canContinueToUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     this.forgetTime = 20;
/* 23 */     setOpen(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 28 */     setOpen(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.forgetTime--;
/* 34 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\OpenDoorGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */