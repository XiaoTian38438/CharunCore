/*    */ package net.minecraft.core.dispenser;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class EquipmentDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 14 */   public static final EquipmentDispenseItemBehavior INSTANCE = new EquipmentDispenseItemBehavior();
/*    */ 
/*    */   
/*    */   protected ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 18 */     return dispenseEquipment(paramBlockSource, paramItemStack) ? paramItemStack : super.execute(paramBlockSource, paramItemStack);
/*    */   }
/*    */   
/*    */   public static boolean dispenseEquipment(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 22 */     BlockPos blockPos = paramBlockSource.pos().relative((Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING));
/*    */     
/* 24 */     List<LivingEntity> list = paramBlockSource.level().getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), paramLivingEntity -> paramLivingEntity.canEquipWithDispenser(paramItemStack));
/* 25 */     if (list.isEmpty()) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     LivingEntity livingEntity = list.getFirst();
/* 30 */     EquipmentSlot equipmentSlot = livingEntity.getEquipmentSlotForItem(paramItemStack);
/*    */     
/* 32 */     ItemStack itemStack = paramItemStack.split(1);
/* 33 */     livingEntity.setItemSlot(equipmentSlot, itemStack);
/* 34 */     if (livingEntity instanceof Mob) { Mob mob = (Mob)livingEntity;
/* 35 */       mob.setGuaranteedDrop(equipmentSlot);
/* 36 */       mob.setPersistenceRequired(); }
/*    */     
/* 38 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\EquipmentDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */