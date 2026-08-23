/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class ThrowablePotionItem
/*    */   extends PotionItem implements ProjectileItem {
/* 17 */   public static float PROJECTILE_SHOOT_POWER = 0.5F;
/*    */   
/*    */   public ThrowablePotionItem(Item.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 25 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 26 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 27 */       Projectile.spawnProjectileFromRotation(this::createPotion, serverLevel, itemStack, (LivingEntity)paramPlayer, -20.0F, PROJECTILE_SHOOT_POWER, 1.0F); }
/*    */     
/* 29 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 30 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/* 31 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract AbstractThrownPotion createPotion(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack);
/*    */   
/*    */   protected abstract AbstractThrownPotion createPotion(Level paramLevel, Position paramPosition, ItemStack paramItemStack);
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 40 */     return (Projectile)createPotion(paramLevel, paramPosition, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 45 */     return ProjectileItem.DispenseConfig.builder()
/* 46 */       .uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5F)
/* 47 */       .power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25F)
/* 48 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ThrowablePotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */