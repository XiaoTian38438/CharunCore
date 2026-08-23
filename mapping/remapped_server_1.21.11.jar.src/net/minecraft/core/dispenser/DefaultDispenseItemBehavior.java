/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultDispenseItemBehavior
/*    */   implements DispenseItemBehavior {
/*    */   private static final int DEFAULT_ACCURACY = 6;
/*    */   
/*    */   public final ItemStack dispense(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 17 */     ItemStack itemStack = execute(paramBlockSource, paramItemStack);
/*    */     
/* 19 */     playSound(paramBlockSource);
/* 20 */     playAnimation(paramBlockSource, (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING));
/*    */     
/* 22 */     return itemStack;
/*    */   }
/*    */   
/*    */   protected ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 26 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 27 */     Position position = DispenserBlock.getDispensePosition(paramBlockSource);
/*    */     
/* 29 */     ItemStack itemStack = paramItemStack.split(1);
/*    */     
/* 31 */     spawnItem((Level)paramBlockSource.level(), itemStack, 6, direction, position);
/*    */     
/* 33 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static void spawnItem(Level paramLevel, ItemStack paramItemStack, int paramInt, Direction paramDirection, Position paramPosition) {
/* 37 */     double d1 = paramPosition.x();
/* 38 */     double d2 = paramPosition.y();
/* 39 */     double d3 = paramPosition.z();
/*    */     
/* 41 */     if (paramDirection.getAxis() == Direction.Axis.Y) {
/*    */       
/* 43 */       d2 -= 0.125D;
/*    */     } else {
/*    */       
/* 46 */       d2 -= 0.15625D;
/*    */     } 
/*    */     
/* 49 */     ItemEntity itemEntity = new ItemEntity(paramLevel, d1, d2, d3, paramItemStack);
/*    */     
/* 51 */     double d4 = paramLevel.random.nextDouble() * 0.1D + 0.2D;
/* 52 */     itemEntity.setDeltaMovement(paramLevel.random
/* 53 */         .triangle(paramDirection.getStepX() * d4, 0.0172275D * paramInt), paramLevel.random
/* 54 */         .triangle(0.2D, 0.0172275D * paramInt), paramLevel.random
/* 55 */         .triangle(paramDirection.getStepZ() * d4, 0.0172275D * paramInt));
/*    */ 
/*    */     
/* 58 */     paramLevel.addFreshEntity((Entity)itemEntity);
/*    */   }
/*    */   
/*    */   protected void playSound(BlockSource paramBlockSource) {
/* 62 */     playDefaultSound(paramBlockSource);
/*    */   }
/*    */   
/*    */   protected void playAnimation(BlockSource paramBlockSource, Direction paramDirection) {
/* 66 */     playDefaultAnimation(paramBlockSource, paramDirection);
/*    */   }
/*    */   
/*    */   private static void playDefaultSound(BlockSource paramBlockSource) {
/* 70 */     paramBlockSource.level().levelEvent(1000, paramBlockSource.pos(), 0);
/*    */   }
/*    */   
/*    */   private static void playDefaultAnimation(BlockSource paramBlockSource, Direction paramDirection) {
/* 74 */     paramBlockSource.level().levelEvent(2000, paramBlockSource.pos(), paramDirection.get3DDataValue());
/*    */   }
/*    */   
/*    */   protected ItemStack consumeWithRemainder(BlockSource paramBlockSource, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 78 */     paramItemStack1.shrink(1);
/* 79 */     if (paramItemStack1.isEmpty())
/*    */     {
/* 81 */       return paramItemStack2;
/*    */     }
/* 83 */     addToInventoryOrDispense(paramBlockSource, paramItemStack2);
/* 84 */     return paramItemStack1;
/*    */   }
/*    */   
/*    */   private void addToInventoryOrDispense(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 88 */     ItemStack itemStack = paramBlockSource.blockEntity().insertItem(paramItemStack);
/* 89 */     if (itemStack.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 93 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 94 */     spawnItem((Level)paramBlockSource.level(), itemStack, 6, direction, DispenserBlock.getDispensePosition(paramBlockSource));
/*    */     
/* 96 */     playDefaultSound(paramBlockSource);
/* 97 */     playDefaultAnimation(paramBlockSource, direction);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\DefaultDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */