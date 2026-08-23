/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class Snowball
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public Snowball(EntityType<? extends Snowball> paramEntityType, Level paramLevel) {
/* 20 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public Snowball(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 24 */     super(EntityType.SNOWBALL, paramLivingEntity, paramLevel, paramItemStack);
/*    */   }
/*    */   
/*    */   public Snowball(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 28 */     super(EntityType.SNOWBALL, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 33 */     return Items.SNOWBALL;
/*    */   }
/*    */   
/*    */   private ParticleOptions getParticle() {
/* 37 */     ItemStack itemStack = getItem();
/* 38 */     return itemStack.isEmpty() ? (ParticleOptions)ParticleTypes.ITEM_SNOWBALL : (ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleEntityEvent(byte paramByte) {
/* 43 */     if (paramByte == 3) {
/* 44 */       ParticleOptions particleOptions = getParticle();
/* 45 */       for (byte b = 0; b < 8; b++) {
/* 46 */         level().addParticle(particleOptions, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/* 53 */     super.onHitEntity(paramEntityHitResult);
/* 54 */     Entity entity = paramEntityHitResult.getEntity();
/* 55 */     boolean bool = (entity instanceof net.minecraft.world.entity.monster.Blaze) ? true : false;
/*    */     
/* 57 */     entity.hurt(damageSources().thrown((Entity)this, getOwner()), bool);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 62 */     super.onHit(paramHitResult);
/*    */     
/* 64 */     if (!level().isClientSide()) {
/* 65 */       level().broadcastEntityEvent((Entity)this, (byte)3);
/* 66 */       discard();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\Snowball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */