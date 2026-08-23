/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ExperienceBottleItem extends Item implements ProjectileItem {
/*    */   public ExperienceBottleItem(Item.Properties paramProperties) {
/* 18 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 23 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 24 */     paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/* 25 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 26 */       Projectile.spawnProjectileFromRotation(ThrownExperienceBottle::new, serverLevel, itemStack, (LivingEntity)paramPlayer, -20.0F, 0.7F, 1.0F); }
/*    */     
/* 28 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 29 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/* 30 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 35 */     return (Projectile)new ThrownExperienceBottle(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 40 */     return ProjectileItem.DispenseConfig.builder()
/* 41 */       .uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5F)
/* 42 */       .power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25F)
/* 43 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ExperienceBottleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */