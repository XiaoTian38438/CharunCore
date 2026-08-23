/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ 
/*    */ public class RandomStandGoal extends Goal {
/*    */   private final AbstractHorse horse;
/*    */   private int nextStand;
/*    */   
/*    */   public RandomStandGoal(AbstractHorse paramAbstractHorse) {
/* 11 */     this.horse = paramAbstractHorse;
/* 12 */     resetStandInterval(paramAbstractHorse);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 17 */     this.horse.standIfPossible();
/* 18 */     playStandSound();
/*    */   }
/*    */   
/*    */   private void playStandSound() {
/* 22 */     SoundEvent soundEvent = this.horse.getAmbientStandSound();
/* 23 */     if (soundEvent != null) {
/* 24 */       this.horse.playSound(soundEvent);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 37 */     this.nextStand++;
/* 38 */     if (this.nextStand > 0 && this.horse.getRandom().nextInt(1000) < this.nextStand) {
/* 39 */       resetStandInterval(this.horse);
/* 40 */       return (!this.horse.isImmobile() && this.horse.getRandom().nextInt(10) == 0);
/*    */     } 
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   private void resetStandInterval(AbstractHorse paramAbstractHorse) {
/* 46 */     this.nextStand = -paramAbstractHorse.getAmbientStandInterval();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RandomStandGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */