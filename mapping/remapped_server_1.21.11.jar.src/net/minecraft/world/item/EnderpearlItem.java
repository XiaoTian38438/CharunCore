/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EnderpearlItem
/*    */   extends Item {
/* 16 */   public static float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */   
/*    */   public EnderpearlItem(Item.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 24 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 26 */     paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/* 27 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 28 */       Projectile.spawnProjectileFromRotation(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl::new, serverLevel, itemStack, (LivingEntity)paramPlayer, 0.0F, PROJECTILE_SHOOT_POWER, 1.0F); }
/*    */     
/* 30 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 31 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/* 32 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\EnderpearlItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */