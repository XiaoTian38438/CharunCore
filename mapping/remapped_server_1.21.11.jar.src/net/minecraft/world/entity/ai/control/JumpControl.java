/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class JumpControl implements Control {
/*    */   private final Mob mob;
/*    */   protected boolean jump;
/*    */   
/*    */   public JumpControl(Mob paramMob) {
/* 10 */     this.mob = paramMob;
/*    */   }
/*    */   
/*    */   public void jump() {
/* 14 */     this.jump = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     this.mob.setJumping(this.jump);
/* 19 */     this.jump = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\JumpControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */