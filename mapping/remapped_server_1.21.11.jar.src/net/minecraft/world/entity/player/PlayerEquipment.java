/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityEquipment;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class PlayerEquipment extends EntityEquipment {
/*    */   private final Player player;
/*    */   
/*    */   public PlayerEquipment(Player paramPlayer) {
/* 11 */     this.player = paramPlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack set(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack) {
/* 16 */     if (paramEquipmentSlot == EquipmentSlot.MAINHAND) {
/* 17 */       return this.player.getInventory().setSelectedItem(paramItemStack);
/*    */     }
/* 19 */     return super.set(paramEquipmentSlot, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack get(EquipmentSlot paramEquipmentSlot) {
/* 24 */     if (paramEquipmentSlot == EquipmentSlot.MAINHAND) {
/* 25 */       return this.player.getInventory().getSelectedItem();
/*    */     }
/* 27 */     return super.get(paramEquipmentSlot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 32 */     return (this.player.getInventory().getSelectedItem().isEmpty() && super.isEmpty());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\player\PlayerEquipment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */