/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.Equippable;
/*    */ import net.minecraft.world.level.storage.loot.LootParams;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public interface EquipmentUser
/*    */ {
/*    */   void setItemSlot(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack);
/*    */   
/*    */   ItemStack getItemBySlot(EquipmentSlot paramEquipmentSlot);
/*    */   
/*    */   void setDropChance(EquipmentSlot paramEquipmentSlot, float paramFloat);
/*    */   
/*    */   default void equip(EquipmentTable paramEquipmentTable, LootParams paramLootParams) {
/* 23 */     equip(paramEquipmentTable.lootTable(), paramLootParams, paramEquipmentTable.slotDropChances());
/*    */   }
/*    */   
/*    */   default void equip(ResourceKey<LootTable> paramResourceKey, LootParams paramLootParams, Map<EquipmentSlot, Float> paramMap) {
/* 27 */     equip(paramResourceKey, paramLootParams, 0L, paramMap);
/*    */   }
/*    */   
/*    */   default void equip(ResourceKey<LootTable> paramResourceKey, LootParams paramLootParams, long paramLong, Map<EquipmentSlot, Float> paramMap) {
/* 31 */     LootTable lootTable = paramLootParams.getLevel().getServer().reloadableRegistries().getLootTable(paramResourceKey);
/* 32 */     if (lootTable == LootTable.EMPTY) {
/*    */       return;
/*    */     }
/*    */     
/* 36 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(paramLootParams, paramLong);
/*    */     
/* 38 */     ArrayList<EquipmentSlot> arrayList = new ArrayList();
/* 39 */     for (ItemStack itemStack : objectArrayList) {
/* 40 */       EquipmentSlot equipmentSlot = resolveSlot(itemStack, arrayList);
/*    */       
/* 42 */       if (equipmentSlot != null) {
/* 43 */         ItemStack itemStack1 = equipmentSlot.limit(itemStack);
/* 44 */         setItemSlot(equipmentSlot, itemStack1);
/* 45 */         Float float_ = paramMap.get(equipmentSlot);
/* 46 */         if (float_ != null) {
/* 47 */           setDropChance(equipmentSlot, float_.floatValue());
/*    */         }
/* 49 */         arrayList.add(equipmentSlot);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default EquipmentSlot resolveSlot(ItemStack paramItemStack, List<EquipmentSlot> paramList) {
/* 55 */     if (paramItemStack.isEmpty()) {
/* 56 */       return null;
/*    */     }
/*    */     
/* 59 */     Equippable equippable = (Equippable)paramItemStack.get(DataComponents.EQUIPPABLE);
/*    */     
/* 61 */     if (equippable != null) {
/* 62 */       EquipmentSlot equipmentSlot = equippable.slot();
/* 63 */       if (!paramList.contains(equipmentSlot)) {
/* 64 */         return equipmentSlot;
/*    */       }
/* 66 */     } else if (!paramList.contains(EquipmentSlot.MAINHAND)) {
/* 67 */       return EquipmentSlot.MAINHAND;
/*    */     } 
/*    */     
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EquipmentUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */