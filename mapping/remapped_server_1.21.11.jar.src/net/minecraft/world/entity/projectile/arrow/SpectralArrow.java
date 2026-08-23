/*    */ package net.minecraft.world.entity.projectile.arrow;
/*    */ 
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SpellParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class SpectralArrow
/*    */   extends AbstractArrow
/*    */ {
/*    */   private static final int DEFAULT_DURATION = 200;
/* 21 */   private int duration = 200;
/*    */   
/*    */   public SpectralArrow(EntityType<? extends SpectralArrow> paramEntityType, Level paramLevel) {
/* 24 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 28 */     super(EntityType.SPECTRAL_ARROW, paramLivingEntity, paramLevel, paramItemStack1, paramItemStack2);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 32 */     super(EntityType.SPECTRAL_ARROW, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack1, paramItemStack2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 37 */     super.tick();
/*    */     
/* 39 */     if (level().isClientSide() && !isInGround()) {
/* 40 */       level().addParticle((ParticleOptions)SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doPostHurtEffects(LivingEntity paramLivingEntity) {
/* 46 */     super.doPostHurtEffects(paramLivingEntity);
/*    */     
/* 48 */     MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
/* 49 */     paramLivingEntity.addEffect(mobEffectInstance, getEffectSource());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 54 */     super.readAdditionalSaveData(paramValueInput);
/* 55 */     this.duration = paramValueInput.getIntOr("Duration", 200);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 60 */     super.addAdditionalSaveData(paramValueOutput);
/* 61 */     paramValueOutput.putInt("Duration", this.duration);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getDefaultPickupItem() {
/* 66 */     return new ItemStack((ItemLike)Items.SPECTRAL_ARROW);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\arrow\SpectralArrow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */