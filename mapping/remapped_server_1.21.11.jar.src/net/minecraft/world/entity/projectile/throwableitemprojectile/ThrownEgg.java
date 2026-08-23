/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*    */ import net.minecraft.world.item.EitherHolder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownEgg extends ThrowableItemProjectile {
/* 23 */   private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);
/*    */   
/*    */   public ThrownEgg(EntityType<? extends ThrownEgg> paramEntityType, Level paramLevel) {
/* 26 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 30 */     super(EntityType.EGG, paramLivingEntity, paramLevel, paramItemStack);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 34 */     super(EntityType.EGG, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleEntityEvent(byte paramByte) {
/* 39 */     if (paramByte == 3) {
/* 40 */       double d = 0.08D;
/* 41 */       for (byte b = 0; b < 8; b++) {
/* 42 */         level().addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/* 49 */     super.onHitEntity(paramEntityHitResult);
/* 50 */     paramEntityHitResult.getEntity().hurt(damageSources().thrown((Entity)this, getOwner()), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 55 */     super.onHit(paramHitResult);
/*    */     
/* 57 */     if (!level().isClientSide()) {
/* 58 */       if (this.random.nextInt(8) == 0) {
/* 59 */         byte b1 = 1;
/* 60 */         if (this.random.nextInt(32) == 0) {
/* 61 */           b1 = 4;
/*    */         }
/* 63 */         for (byte b2 = 0; b2 < b1; b2++) {
/* 64 */           Chicken chicken = (Chicken)EntityType.CHICKEN.create(level(), EntitySpawnReason.TRIGGERED);
/* 65 */           if (chicken != null) {
/* 66 */             chicken.setAge(-24000);
/* 67 */             chicken.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/*    */ 
/*    */ 
/*    */             
/* 71 */             Objects.requireNonNull(chicken); Optional.<EitherHolder>ofNullable((EitherHolder)getItem().get(DataComponents.CHICKEN_VARIANT)).flatMap(paramEitherHolder -> paramEitherHolder.unwrap((HolderLookup.Provider)registryAccess())).ifPresent(chicken::setVariant);
/*    */             
/* 73 */             if (!chicken.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
/*    */               break;
/*    */             }
/* 76 */             level().addFreshEntity((Entity)chicken);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 81 */       level().broadcastEntityEvent((Entity)this, (byte)3);
/* 82 */       discard();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 88 */     return Items.EGG;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEgg.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */