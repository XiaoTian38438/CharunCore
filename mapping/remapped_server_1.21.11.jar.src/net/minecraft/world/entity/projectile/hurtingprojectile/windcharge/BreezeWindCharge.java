/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreezeWindCharge extends AbstractWindCharge {
/*    */   public BreezeWindCharge(EntityType<? extends AbstractWindCharge> paramEntityType, Level paramLevel) {
/* 16 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   private static final float RADIUS = 3.0F;
/*    */   public BreezeWindCharge(Breeze paramBreeze, Level paramLevel) {
/* 20 */     super(EntityType.BREEZE_WIND_CHARGE, paramLevel, (Entity)paramBreeze, paramBreeze.getX(), paramBreeze.getFiringYPosition(), paramBreeze.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void explode(Vec3 paramVec3) {
/* 25 */     level().explode((Entity)this, null, EXPLOSION_DAMAGE_CALCULATOR, paramVec3.x(), paramVec3.y(), paramVec3.z(), 3.0F, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\BreezeWindCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */