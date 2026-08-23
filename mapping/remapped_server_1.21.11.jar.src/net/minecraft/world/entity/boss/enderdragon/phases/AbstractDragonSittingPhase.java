/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public abstract class AbstractDragonSittingPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/*    */   public AbstractDragonSittingPhase(EnderDragon paramEnderDragon) {
/* 10 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 15 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public float onHurt(DamageSource paramDamageSource, float paramFloat) {
/* 20 */     if (paramDamageSource.getDirectEntity() instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow || paramDamageSource.getDirectEntity() instanceof net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge) {
/* 21 */       paramDamageSource.getDirectEntity().igniteForSeconds(1.0F);
/* 22 */       return 0.0F;
/*    */     } 
/* 24 */     return super.onHurt(paramDamageSource, paramFloat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonSittingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */