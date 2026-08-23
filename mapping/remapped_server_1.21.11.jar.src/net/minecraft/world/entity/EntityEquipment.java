/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class EntityEquipment {
/*    */   static {
/* 11 */     CODEC = Codec.unboundedMap((Codec)EquipmentSlot.CODEC, ItemStack.CODEC).xmap(paramMap -> {
/*    */           EnumMap<EquipmentSlot, Object> enumMap = new EnumMap<>(EquipmentSlot.class);
/*    */           enumMap.putAll(paramMap);
/*    */           return new EntityEquipment((EnumMap)enumMap);
/*    */         }paramEntityEquipment -> {
/*    */           EnumMap<EquipmentSlot, ItemStack> enumMap = new EnumMap<>(paramEntityEquipment.items);
/*    */           enumMap.values().removeIf(ItemStack::isEmpty);
/*    */           return enumMap;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<EntityEquipment> CODEC;
/*    */   private final EnumMap<EquipmentSlot, ItemStack> items;
/*    */   
/*    */   private EntityEquipment(EnumMap<EquipmentSlot, ItemStack> paramEnumMap) {
/* 27 */     this.items = paramEnumMap;
/*    */   }
/*    */   
/*    */   public EntityEquipment() {
/* 31 */     this(new EnumMap<>(EquipmentSlot.class));
/*    */   }
/*    */   
/*    */   public ItemStack set(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack) {
/* 35 */     return Objects.<ItemStack>requireNonNullElse(this.items.put(paramEquipmentSlot, paramItemStack), ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public ItemStack get(EquipmentSlot paramEquipmentSlot) {
/* 39 */     return this.items.getOrDefault(paramEquipmentSlot, ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 43 */     for (ItemStack itemStack : this.items.values()) {
/* 44 */       if (!itemStack.isEmpty()) {
/* 45 */         return false;
/*    */       }
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void tick(Entity paramEntity) {
/* 52 */     for (Map.Entry<EquipmentSlot, ItemStack> entry : this.items.entrySet()) {
/* 53 */       ItemStack itemStack = (ItemStack)entry.getValue();
/* 54 */       if (!itemStack.isEmpty()) {
/* 55 */         itemStack.inventoryTick(paramEntity.level(), paramEntity, (EquipmentSlot)entry.getKey());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAll(EntityEquipment paramEntityEquipment) {
/* 61 */     this.items.clear();
/* 62 */     this.items.putAll(paramEntityEquipment.items);
/*    */   }
/*    */   
/*    */   public void dropAll(LivingEntity paramLivingEntity) {
/* 66 */     for (ItemStack itemStack : this.items.values()) {
/* 67 */       paramLivingEntity.drop(itemStack, true, false);
/*    */     }
/* 69 */     clear();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 73 */     this.items.replaceAll((paramEquipmentSlot, paramItemStack) -> ItemStack.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityEquipment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */