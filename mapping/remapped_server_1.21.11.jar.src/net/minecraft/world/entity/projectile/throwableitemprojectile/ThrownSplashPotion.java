/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownSplashPotion
/*    */   extends AbstractThrownPotion
/*    */ {
/*    */   public ThrownSplashPotion(EntityType<? extends ThrownSplashPotion> paramEntityType, Level paramLevel) {
/* 25 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public ThrownSplashPotion(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 29 */     super(EntityType.SPLASH_POTION, paramLevel, paramLivingEntity, paramItemStack);
/*    */   }
/*    */   
/*    */   public ThrownSplashPotion(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 33 */     super(EntityType.SPLASH_POTION, paramLevel, paramDouble1, paramDouble2, paramDouble3, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 38 */     return Items.SPLASH_POTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onHitAsPotion(ServerLevel paramServerLevel, ItemStack paramItemStack, HitResult paramHitResult) {
/* 43 */     PotionContents potionContents = (PotionContents)paramItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 44 */     float f1 = ((Float)paramItemStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue();
/*    */     
/* 46 */     Iterable iterable = potionContents.getAllEffects();
/* 47 */     AABB aABB1 = getBoundingBox().move(paramHitResult.getLocation().subtract(position()));
/* 48 */     AABB aABB2 = aABB1.inflate(4.0D, 2.0D, 4.0D);
/* 49 */     List list = level().getEntitiesOfClass(LivingEntity.class, aABB2);
/*    */     
/* 51 */     float f2 = ProjectileUtil.computeMargin((Entity)this);
/*    */     
/* 53 */     if (!list.isEmpty()) {
/* 54 */       Entity entity = getEffectSource();
/* 55 */       for (LivingEntity livingEntity : list) {
/* 56 */         if (!livingEntity.isAffectedByPotions()) {
/*    */           continue;
/*    */         }
/*    */         
/* 60 */         double d = aABB1.distanceToSqr(livingEntity.getBoundingBox().inflate(f2));
/* 61 */         if (d < 16.0D) {
/* 62 */           double d1 = 1.0D - Math.sqrt(d) / 4.0D;
/* 63 */           for (MobEffectInstance mobEffectInstance1 : iterable) {
/* 64 */             Holder holder = mobEffectInstance1.getEffect();
/* 65 */             if (((MobEffect)holder.value()).isInstantenous()) {
/* 66 */               ((MobEffect)holder.value()).applyInstantenousEffect(paramServerLevel, (Entity)this, getOwner(), livingEntity, mobEffectInstance1.getAmplifier(), d1); continue;
/*    */             } 
/* 68 */             int i = mobEffectInstance1.mapDuration(paramInt -> (int)(paramDouble * paramInt * paramFloat + 0.5D));
/* 69 */             MobEffectInstance mobEffectInstance2 = new MobEffectInstance(holder, i, mobEffectInstance1.getAmplifier(), mobEffectInstance1.isAmbient(), mobEffectInstance1.isVisible());
/* 70 */             if (!mobEffectInstance2.endsWithin(20))
/* 71 */               livingEntity.addEffect(mobEffectInstance2, entity); 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownSplashPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */