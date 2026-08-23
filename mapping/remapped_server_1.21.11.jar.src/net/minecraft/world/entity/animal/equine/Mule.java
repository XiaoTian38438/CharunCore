/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Mule
/*    */   extends AbstractChestedHorse {
/*    */   public Mule(EntityType<? extends Mule> paramEntityType, Level paramLevel) {
/* 15 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 20 */     return SoundEvents.MULE_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 25 */     return SoundEvents.MULE_ANGRY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 30 */     return SoundEvents.MULE_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getEatingSound() {
/* 35 */     return SoundEvents.MULE_EAT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 40 */     return SoundEvents.MULE_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playJumpSound() {
/* 45 */     playSound(SoundEvents.MULE_JUMP, 0.4F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playChestEquipsSound() {
/* 50 */     playSound(SoundEvents.MULE_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 55 */     return (AgeableMob)EntityType.MULE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\Mule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */