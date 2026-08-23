/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ProjectileDeflection {
/*    */   public static final ProjectileDeflection NONE = (paramProjectile, paramEntity, paramRandomSource) -> {
/*    */     
/*    */     };
/*    */   public static final ProjectileDeflection REVERSE;
/*    */   
/*    */   static {
/* 15 */     REVERSE = ((paramProjectile, paramEntity, paramRandomSource) -> {
/*    */         float f = 170.0F + paramRandomSource.nextFloat() * 20.0F;
/*    */         
/*    */         paramProjectile.setDeltaMovement(paramProjectile.getDeltaMovement().scale(-0.5D));
/*    */         paramProjectile.setYRot(paramProjectile.getYRot() + f);
/*    */         paramProjectile.yRotO += f;
/*    */         paramProjectile.needsSync = true;
/*    */       });
/* 23 */     AIM_DEFLECT = ((paramProjectile, paramEntity, paramRandomSource) -> {
/*    */         if (paramEntity != null) {
/*    */           Vec3 vec3 = paramEntity.getLookAngle();
/*    */           
/*    */           paramProjectile.setDeltaMovement(vec3);
/*    */           paramProjectile.needsSync = true;
/*    */         } 
/*    */       });
/* 31 */     MOMENTUM_DEFLECT = ((paramProjectile, paramEntity, paramRandomSource) -> {
/*    */         if (paramEntity != null) {
/*    */           Vec3 vec3 = paramEntity.getDeltaMovement().normalize();
/*    */           paramProjectile.setDeltaMovement(vec3);
/*    */           paramProjectile.needsSync = true;
/*    */         } 
/*    */       });
/*    */   }
/*    */   
/*    */   public static final ProjectileDeflection AIM_DEFLECT;
/*    */   public static final ProjectileDeflection MOMENTUM_DEFLECT;
/*    */   
/*    */   void deflect(Projectile paramProjectile, Entity paramEntity, RandomSource paramRandomSource);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\ProjectileDeflection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */