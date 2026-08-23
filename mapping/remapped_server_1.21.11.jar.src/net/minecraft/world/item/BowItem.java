/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BowItem
/*    */   extends ProjectileWeaponItem
/*    */ {
/*    */   public static final int MAX_DRAW_DURATION = 20;
/*    */   public static final int DEFAULT_RANGE = 15;
/*    */   
/*    */   public BowItem(Item.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean releaseUsing(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 29 */     if (!(paramLivingEntity instanceof Player)) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     Player player = (Player)paramLivingEntity;
/* 34 */     ItemStack itemStack = player.getProjectile(paramItemStack);
/*    */     
/* 36 */     if (itemStack.isEmpty()) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     int i = getUseDuration(paramItemStack, paramLivingEntity) - paramInt;
/* 41 */     float f = getPowerForTime(i);
/* 42 */     if (f < 0.1D) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     List<ItemStack> list = draw(paramItemStack, itemStack, (LivingEntity)player);
/* 47 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (!list.isEmpty()) {
/* 48 */         shoot(serverLevel, (LivingEntity)player, player.getUsedItemHand(), paramItemStack, list, f * 3.0F, 1.0F, (f == 1.0F), null);
/*    */       } }
/*    */     
/* 51 */     paramLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (paramLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
/* 52 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void shootProjectile(LivingEntity paramLivingEntity1, Projectile paramProjectile, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, LivingEntity paramLivingEntity2) {
/* 58 */     paramProjectile.shootFromRotation((Entity)paramLivingEntity1, paramLivingEntity1.getXRot(), paramLivingEntity1.getYRot() + paramFloat3, 0.0F, paramFloat1, paramFloat2);
/*    */   }
/*    */   
/*    */   public static float getPowerForTime(int paramInt) {
/* 62 */     float f = paramInt / 20.0F;
/* 63 */     f = (f * f + f * 2.0F) / 3.0F;
/* 64 */     if (f > 1.0F) {
/* 65 */       f = 1.0F;
/*    */     }
/* 67 */     return f;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 72 */     return 72000;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/* 77 */     return ItemUseAnimation.BOW;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 82 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 83 */     boolean bool = !paramPlayer.getProjectile(itemStack).isEmpty() ? true : false;
/* 84 */     if (paramPlayer.hasInfiniteMaterials() || bool) {
/* 85 */       paramPlayer.startUsingItem(paramInteractionHand);
/* 86 */       return (InteractionResult)InteractionResult.CONSUME;
/*    */     } 
/* 88 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */ 
/*    */   
/*    */   public Predicate<ItemStack> getAllSupportedProjectiles() {
/* 93 */     return ARROW_ONLY;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefaultProjectileRange() {
/* 98 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */