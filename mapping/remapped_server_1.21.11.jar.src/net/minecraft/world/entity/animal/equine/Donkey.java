/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Donkey
/*    */   extends AbstractChestedHorse {
/*    */   public Donkey(EntityType<? extends Donkey> paramEntityType, Level paramLevel) {
/* 16 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 21 */     return SoundEvents.DONKEY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 26 */     return SoundEvents.DONKEY_ANGRY;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 31 */     return SoundEvents.DONKEY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getEatingSound() {
/* 36 */     return SoundEvents.DONKEY_EAT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 41 */     return SoundEvents.DONKEY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canMate(Animal paramAnimal) {
/* 46 */     if (paramAnimal == this) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     if (paramAnimal instanceof Donkey || paramAnimal instanceof Horse) {
/* 51 */       return (canParent() && ((AbstractHorse)paramAnimal).canParent());
/*    */     }
/*    */     
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playJumpSound() {
/* 59 */     playSound(SoundEvents.DONKEY_JUMP, 0.4F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 64 */     EntityType entityType = (paramAgeableMob instanceof Horse) ? EntityType.MULE : EntityType.DONKEY;
/* 65 */     AbstractHorse abstractHorse = (AbstractHorse)entityType.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*    */     
/* 67 */     if (abstractHorse != null) {
/* 68 */       setOffspringAttributes(paramAgeableMob, abstractHorse);
/*    */     }
/*    */     
/* 71 */     return (AgeableMob)abstractHorse;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\Donkey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */