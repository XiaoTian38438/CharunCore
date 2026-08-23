/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SplashPotionItem extends ThrowablePotionItem {
/*    */   public SplashPotionItem(Item.Properties paramProperties) {
/* 17 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 22 */     paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/* 23 */     return super.use(paramLevel, paramPlayer, paramInteractionHand);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 28 */     return (AbstractThrownPotion)new ThrownSplashPotion((Level)paramServerLevel, paramLivingEntity, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractThrownPotion createPotion(Level paramLevel, Position paramPosition, ItemStack paramItemStack) {
/* 33 */     return (AbstractThrownPotion)new ThrownSplashPotion(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SplashPotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */