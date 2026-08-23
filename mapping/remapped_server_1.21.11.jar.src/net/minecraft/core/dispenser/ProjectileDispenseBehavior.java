/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ProjectileItem;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
/*    */   private final ProjectileItem projectileItem;
/*    */   
/*    */   public ProjectileDispenseBehavior(Item paramItem) {
/*    */     ProjectileItem projectileItem;
/* 19 */     if (paramItem instanceof ProjectileItem) { projectileItem = (ProjectileItem)paramItem; }
/* 20 */     else { throw new IllegalArgumentException(String.valueOf(paramItem) + " not instance of " + String.valueOf(paramItem)); }
/*    */     
/* 22 */     this.projectileItem = projectileItem;
/* 23 */     this.dispenseConfig = projectileItem.createDispenseConfig();
/*    */   }
/*    */   private final ProjectileItem.DispenseConfig dispenseConfig;
/*    */   
/*    */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 28 */     ServerLevel serverLevel = paramBlockSource.level();
/* 29 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 30 */     Position position = this.dispenseConfig.positionFunction().getDispensePosition(paramBlockSource, direction);
/*    */     
/* 32 */     Projectile.spawnProjectileUsingShoot(this.projectileItem
/* 33 */         .asProjectile((Level)serverLevel, position, paramItemStack, direction), serverLevel, paramItemStack, direction
/*    */         
/* 35 */         .getStepX(), direction.getStepY(), direction.getStepZ(), this.dispenseConfig
/* 36 */         .power(), this.dispenseConfig.uncertainty());
/*    */     
/* 38 */     paramItemStack.shrink(1);
/*    */     
/* 40 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource paramBlockSource) {
/* 45 */     paramBlockSource.level().levelEvent(this.dispenseConfig.overrideDispenseEvent().orElse(1002), paramBlockSource.pos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\ProjectileDispenseBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */