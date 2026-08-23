/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityReference;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WindCharge
/*    */   extends AbstractWindCharge {
/* 25 */   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = (ExplosionDamageCalculator)new SimpleExplosionDamageCalculator(true, false, 
/*    */ 
/*    */       
/* 28 */       Optional.of(Float.valueOf(1.22F)), BuiltInRegistries.BLOCK
/* 29 */       .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
/*    */   
/*    */   private static final float RADIUS = 1.2F;
/*    */   
/* 33 */   private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
/*    */   
/* 35 */   private int noDeflectTicks = 5;
/*    */   
/*    */   public WindCharge(EntityType<? extends AbstractWindCharge> paramEntityType, Level paramLevel) {
/* 38 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public WindCharge(Player paramPlayer, Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 42 */     super(EntityType.WIND_CHARGE, paramLevel, (Entity)paramPlayer, paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   public WindCharge(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, Vec3 paramVec3) {
/* 46 */     super(EntityType.WIND_CHARGE, paramDouble1, paramDouble2, paramDouble3, paramVec3, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     super.tick();
/* 52 */     if (this.noDeflectTicks > 0) {
/* 53 */       this.noDeflectTicks--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean deflect(ProjectileDeflection paramProjectileDeflection, Entity paramEntity, EntityReference<Entity> paramEntityReference, boolean paramBoolean) {
/* 59 */     if (this.noDeflectTicks > 0) {
/* 60 */       return false;
/*    */     }
/* 62 */     return super.deflect(paramProjectileDeflection, paramEntity, paramEntityReference, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void explode(Vec3 paramVec3) {
/* 67 */     level().explode((Entity)this, null, EXPLOSION_DAMAGE_CALCULATOR, paramVec3.x(), paramVec3.y(), paramVec3.z(), 1.2F, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.WIND_CHARGE_BURST);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 72 */     if (this.tickCount < 2 && paramDouble < MIN_CAMERA_DISTANCE_SQUARED) {
/* 73 */       return false;
/*    */     }
/* 75 */     return super.shouldRenderAtSqrDistance(paramDouble);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\WindCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */