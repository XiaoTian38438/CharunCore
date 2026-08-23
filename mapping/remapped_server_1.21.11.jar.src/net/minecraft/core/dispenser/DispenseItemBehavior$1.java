/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SpawnEggItem;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends DefaultDispenseItemBehavior
/*     */ {
/*     */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/*  97 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/*     */     
/*  99 */     EntityType entityType = ((SpawnEggItem)paramItemStack.getItem()).getType(paramItemStack);
/* 100 */     if (entityType == null) {
/* 101 */       return paramItemStack;
/*     */     }
/*     */     try {
/* 104 */       entityType.spawn(paramBlockSource.level(), paramItemStack, null, paramBlockSource.pos().relative(direction), EntitySpawnReason.DISPENSER, (direction != Direction.UP), false);
/* 105 */     } catch (Exception exception) {
/* 106 */       LOGGER.error("Error while dispensing spawn egg from dispenser at {}", paramBlockSource.pos(), exception);
/* 107 */       return ItemStack.EMPTY;
/*     */     } 
/* 109 */     paramItemStack.shrink(1);
/* 110 */     paramBlockSource.level().gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, paramBlockSource.pos());
/* 111 */     return paramItemStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */