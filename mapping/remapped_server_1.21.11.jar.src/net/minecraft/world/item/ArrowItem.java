/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ArrowItem
/*    */   extends Item implements ProjectileItem {
/*    */   public ArrowItem(Item.Properties paramProperties) {
/* 14 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   public AbstractArrow createArrow(Level paramLevel, ItemStack paramItemStack1, LivingEntity paramLivingEntity, ItemStack paramItemStack2) {
/* 18 */     return (AbstractArrow)new Arrow(paramLevel, paramLivingEntity, paramItemStack1.copyWithCount(1), paramItemStack2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 23 */     Arrow arrow = new Arrow(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack.copyWithCount(1), null);
/* 24 */     arrow.pickup = AbstractArrow.Pickup.ALLOWED;
/*    */     
/* 26 */     return (Projectile)arrow;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ArrowItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */