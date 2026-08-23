/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Parched
/*    */   extends AbstractSkeleton
/*    */ {
/*    */   public Parched(EntityType<? extends AbstractSkeleton> paramEntityType, Level paramLevel) {
/* 20 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack paramItemStack1, float paramFloat, ItemStack paramItemStack2) {
/* 25 */     AbstractArrow abstractArrow = super.getArrow(paramItemStack1, paramFloat, paramItemStack2);
/* 26 */     if (abstractArrow instanceof Arrow) {
/* 27 */       ((Arrow)abstractArrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
/*    */     }
/* 29 */     return abstractArrow;
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 33 */     return AbstractSkeleton.createAttributes()
/* 34 */       .add(Attributes.MAX_HEALTH, 16.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 39 */     return SoundEvents.PARCHED_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 44 */     return SoundEvents.PARCHED_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 49 */     return SoundEvents.PARCHED_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 54 */     return SoundEvents.PARCHED_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getHardAttackInterval() {
/* 59 */     return 50;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAttackInterval() {
/* 64 */     return 70;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeAffected(MobEffectInstance paramMobEffectInstance) {
/* 69 */     if (paramMobEffectInstance.getEffect() == MobEffects.WEAKNESS) {
/* 70 */       return false;
/*    */     }
/* 72 */     return super.canBeAffected(paramMobEffectInstance);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\skeleton\Parched.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */