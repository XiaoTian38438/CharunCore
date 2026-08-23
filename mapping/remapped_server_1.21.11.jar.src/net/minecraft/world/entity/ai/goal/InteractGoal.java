/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class InteractGoal
/*    */   extends LookAtPlayerGoal {
/*    */   public InteractGoal(Mob paramMob, Class<? extends LivingEntity> paramClass, float paramFloat) {
/* 10 */     super(paramMob, paramClass, paramFloat);
/* 11 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */   
/*    */   public InteractGoal(Mob paramMob, Class<? extends LivingEntity> paramClass, float paramFloat1, float paramFloat2) {
/* 15 */     super(paramMob, paramClass, paramFloat1, paramFloat2);
/* 16 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\InteractGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */