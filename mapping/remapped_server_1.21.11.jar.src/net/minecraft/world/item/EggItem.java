/*    */ package net.minecraft.world.item;
/*    */ 
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
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EggItem extends Item implements ProjectileItem {
/*    */   public static final float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */   
/*    */   public EggItem(Item.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 26 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 27 */     paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/* 28 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 29 */       Projectile.spawnProjectileFromRotation(ThrownEgg::new, serverLevel, itemStack, (LivingEntity)paramPlayer, 0.0F, 1.5F, 1.0F); }
/*    */     
/* 31 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 32 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/* 33 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 38 */     return (Projectile)new ThrownEgg(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\EggItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */