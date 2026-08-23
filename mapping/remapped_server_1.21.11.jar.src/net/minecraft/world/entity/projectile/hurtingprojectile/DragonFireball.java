/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.PowerParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonFireball
/*    */   extends AbstractHurtingProjectile
/*    */ {
/*    */   public static final float SPLASH_RANGE = 4.0F;
/*    */   
/*    */   public DragonFireball(EntityType<? extends DragonFireball> paramEntityType, Level paramLevel) {
/* 24 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public DragonFireball(Level paramLevel, LivingEntity paramLivingEntity, Vec3 paramVec3) {
/* 28 */     super(EntityType.DRAGON_FIREBALL, paramLivingEntity, paramVec3, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 33 */     super.onHit(paramHitResult);
/* 34 */     if (paramHitResult.getType() == HitResult.Type.ENTITY && ownedBy(((EntityHitResult)paramHitResult).getEntity())) {
/*    */       return;
/*    */     }
/* 37 */     if (!level().isClientSide()) {
/* 38 */       List list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
/*    */       
/* 40 */       AreaEffectCloud areaEffectCloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 41 */       Entity entity = getOwner();
/* 42 */       if (entity instanceof LivingEntity) {
/* 43 */         areaEffectCloud.setOwner((LivingEntity)entity);
/*    */       }
/* 45 */       areaEffectCloud.setCustomParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
/* 46 */       areaEffectCloud.setRadius(3.0F);
/* 47 */       areaEffectCloud.setDuration(600);
/* 48 */       areaEffectCloud.setRadiusPerTick((7.0F - areaEffectCloud.getRadius()) / areaEffectCloud.getDuration());
/* 49 */       areaEffectCloud.setPotionDurationScale(0.25F);
/* 50 */       areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
/*    */       
/* 52 */       if (!list.isEmpty()) {
/* 53 */         for (LivingEntity livingEntity : list) {
/* 54 */           double d = distanceToSqr((Entity)livingEntity);
/* 55 */           if (d < 16.0D) {
/* 56 */             areaEffectCloud.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
/*    */             
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       }
/* 62 */       level().levelEvent(2006, blockPosition(), isSilent() ? -1 : 1);
/* 63 */       level().addFreshEntity((Entity)areaEffectCloud);
/*    */       
/* 65 */       discard();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ParticleOptions getTrailParticle() {
/* 71 */     return (ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldBurn() {
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\DragonFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */