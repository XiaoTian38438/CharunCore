/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpectralArrowItem
/*    */   extends ArrowItem {
/*    */   public SpectralArrowItem(Item.Properties paramProperties) {
/* 14 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractArrow createArrow(Level paramLevel, ItemStack paramItemStack1, LivingEntity paramLivingEntity, ItemStack paramItemStack2) {
/* 19 */     return (AbstractArrow)new SpectralArrow(paramLevel, paramLivingEntity, paramItemStack1.copyWithCount(1), paramItemStack2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 24 */     SpectralArrow spectralArrow = new SpectralArrow(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack.copyWithCount(1), null);
/* 25 */     spectralArrow.pickup = AbstractArrow.Pickup.ALLOWED;
/* 26 */     return (Projectile)spectralArrow;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SpectralArrowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */