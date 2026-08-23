/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class AbstractDragonPhaseInstance
/*    */   implements DragonPhaseInstance {
/*    */   protected final EnderDragon dragon;
/*    */   
/*    */   public AbstractDragonPhaseInstance(EnderDragon paramEnderDragon) {
/* 16 */     this.dragon = paramEnderDragon;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doClientTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onCrystalDestroyed(EndCrystal paramEndCrystal, BlockPos paramBlockPos, DamageSource paramDamageSource, Player paramPlayer) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void begin() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 46 */     return 0.6F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public float onHurt(DamageSource paramDamageSource, float paramFloat) {
/* 56 */     return paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 61 */     float f1 = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
/* 62 */     float f2 = Math.min(f1, 40.0F);
/*    */     
/* 64 */     return 0.7F / f2 / f1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonPhaseInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */