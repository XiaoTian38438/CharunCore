/*    */ package net.minecraft.world.entity.animal.golem;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractGolem
/*    */   extends PathfinderMob {
/*    */   protected AbstractGolem(EntityType<? extends AbstractGolem> paramEntityType, Level paramLevel) {
/* 12 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 17 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 22 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 32 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeWhenFarAway(double paramDouble) {
/* 37 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\golem\AbstractGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */