/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
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
/*     */   extends OptionalDispenseItemBehavior
/*     */ {
/*     */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 137 */     BlockPos blockPos = paramBlockSource.pos().relative((Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING));
/* 138 */     List list = paramBlockSource.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(blockPos), paramAbstractChestedHorse -> (paramAbstractChestedHorse.isAlive() && !paramAbstractChestedHorse.hasChest()));
/*     */     
/* 140 */     for (AbstractChestedHorse abstractChestedHorse : list) {
/* 141 */       if (abstractChestedHorse.isTamed()) {
/* 142 */         SlotAccess slotAccess = abstractChestedHorse.getSlot(499);
/* 143 */         if (slotAccess != null && slotAccess.set(paramItemStack)) {
/* 144 */           paramItemStack.shrink(1);
/* 145 */           setSuccess(true);
/* 146 */           return paramItemStack;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     return super.execute(paramBlockSource, paramItemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */