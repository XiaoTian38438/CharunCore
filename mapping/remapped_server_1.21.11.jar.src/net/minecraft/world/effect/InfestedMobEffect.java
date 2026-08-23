/*    */ package net.minecraft.world.effect;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ class InfestedMobEffect extends MobEffect {
/*    */   private final float chanceToSpawn;
/*    */   
/*    */   protected InfestedMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt, float paramFloat, ToIntFunction<RandomSource> paramToIntFunction) {
/* 23 */     super(paramMobEffectCategory, paramInt, (ParticleOptions)ParticleTypes.INFESTED);
/* 24 */     this.chanceToSpawn = paramFloat;
/* 25 */     this.spawnedCount = paramToIntFunction;
/*    */   }
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   public void onMobHurt(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt, DamageSource paramDamageSource, float paramFloat) {
/* 30 */     if (paramLivingEntity.getRandom().nextFloat() <= this.chanceToSpawn) {
/* 31 */       int i = this.spawnedCount.applyAsInt(paramLivingEntity.getRandom());
/* 32 */       for (byte b = 0; b < i; b++) {
/* 33 */         spawnSilverfish(paramServerLevel, paramLivingEntity, paramLivingEntity.getX(), paramLivingEntity.getY() + paramLivingEntity.getBbHeight() / 2.0D, paramLivingEntity.getZ());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnSilverfish(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 39 */     Silverfish silverfish = (Silverfish)EntityType.SILVERFISH.create((Level)paramServerLevel, EntitySpawnReason.TRIGGERED);
/*    */     
/* 41 */     if (silverfish == null) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     RandomSource randomSource = paramLivingEntity.getRandom();
/* 46 */     float f1 = 1.5707964F;
/* 47 */     float f2 = Mth.randomBetween(randomSource, -1.5707964F, 1.5707964F);
/* 48 */     Vector3f vector3f = paramLivingEntity.getLookAngle().toVector3f().mul(0.3F).mul(1.0F, 1.5F, 1.0F).rotateY(f2);
/*    */     
/* 50 */     silverfish.snapTo(paramDouble1, paramDouble2, paramDouble3, paramServerLevel.getRandom().nextFloat() * 360.0F, 0.0F);
/* 51 */     silverfish.setDeltaMovement(new Vec3((Vector3fc)vector3f));
/* 52 */     paramServerLevel.addFreshEntity((Entity)silverfish);
/* 53 */     silverfish.playSound(SoundEvents.SILVERFISH_HURT);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\InfestedMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */