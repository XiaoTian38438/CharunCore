/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public class DragonSittingAttackingPhase
/*    */   extends AbstractDragonSittingPhase
/*    */ {
/*    */   private static final int ROAR_DURATION = 40;
/*    */   private int attackingTicks;
/*    */   
/*    */   public DragonSittingAttackingPhase(EnderDragon paramEnderDragon) {
/* 14 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doClientTick() {
/* 19 */     this.dragon.level().playLocalSound(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.dragon.getSoundSource(), 2.5F, 0.8F + this.dragon.getRandom().nextFloat() * 0.3F, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 24 */     if (this.attackingTicks++ >= 40) {
/* 25 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_FLAMING);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 31 */     this.attackingTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonSittingAttackingPhase> getPhase() {
/* 36 */     return EnderDragonPhase.SITTING_ATTACKING;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingAttackingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */