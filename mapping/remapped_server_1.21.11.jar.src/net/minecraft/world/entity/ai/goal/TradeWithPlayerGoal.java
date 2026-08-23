/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class TradeWithPlayerGoal extends Goal {
/*    */   private final AbstractVillager mob;
/*    */   
/*    */   public TradeWithPlayerGoal(AbstractVillager paramAbstractVillager) {
/* 12 */     this.mob = paramAbstractVillager;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 18 */     if (!this.mob.isAlive()) {
/* 19 */       return false;
/*    */     }
/* 21 */     if (this.mob.isInWater()) {
/* 22 */       return false;
/*    */     }
/* 24 */     if (!this.mob.onGround()) {
/* 25 */       return false;
/*    */     }
/* 27 */     if (this.mob.hurtMarked) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     Player player = this.mob.getTradingPlayer();
/* 32 */     if (player == null)
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (this.mob.distanceToSqr((Entity)player) > 16.0D)
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 47 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 52 */     this.mob.setTradingPlayer(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\TradeWithPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */