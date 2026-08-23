/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ 
/*    */ public class ZombieAttackGoal extends MeleeAttackGoal {
/*    */   private final Zombie zombie;
/*    */   
/*    */   public ZombieAttackGoal(Zombie paramZombie, double paramDouble, boolean paramBoolean) {
/* 10 */     super((PathfinderMob)paramZombie, paramDouble, paramBoolean);
/* 11 */     this.zombie = paramZombie;
/*    */   }
/*    */   private int raiseArmTicks;
/*    */   
/*    */   public void start() {
/* 16 */     super.start();
/* 17 */     this.raiseArmTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 22 */     super.stop();
/* 23 */     this.zombie.setAggressive(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     super.tick();
/*    */     
/* 30 */     this.raiseArmTicks++;
/* 31 */     if (this.raiseArmTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2) {
/* 32 */       this.zombie.setAggressive(true);
/*    */     } else {
/* 34 */       this.zombie.setAggressive(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\ZombieAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */