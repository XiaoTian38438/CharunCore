/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ class ArmorSlot
/*    */   extends Slot {
/*    */   private final LivingEntity owner;
/*    */   private final EquipmentSlot slot;
/*    */   private final Identifier emptyIcon;
/*    */   
/*    */   public ArmorSlot(Container paramContainer, LivingEntity paramLivingEntity, EquipmentSlot paramEquipmentSlot, int paramInt1, int paramInt2, int paramInt3, Identifier paramIdentifier) {
/* 19 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/* 20 */     this.owner = paramLivingEntity;
/* 21 */     this.slot = paramEquipmentSlot;
/* 22 */     this.emptyIcon = paramIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setByPlayer(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 27 */     this.owner.onEquipItem(this.slot, paramItemStack2, paramItemStack1);
/* 28 */     super.setByPlayer(paramItemStack1, paramItemStack2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxStackSize() {
/* 33 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 38 */     return this.owner.isEquippableInSlot(paramItemStack, this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isActive() {
/* 43 */     return this.owner.canUseSlot(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPickup(Player paramPlayer) {
/* 48 */     ItemStack itemStack = getItem();
/* 49 */     if (!itemStack.isEmpty() && !paramPlayer.isCreative() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
/* 50 */       return false;
/*    */     }
/* 52 */     return super.mayPickup(paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getNoItemIcon() {
/* 57 */     return this.emptyIcon;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ArmorSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */