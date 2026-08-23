/*   */ package net.minecraft.world.entity.ai.goal;
/*   */ 
/*   */ import java.util.EnumSet;
/*   */ 
/*   */ public abstract class JumpGoal extends Goal {
/*   */   public JumpGoal() {
/* 7 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\JumpGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */