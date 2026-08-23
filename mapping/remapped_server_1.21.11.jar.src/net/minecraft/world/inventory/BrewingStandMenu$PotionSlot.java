/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
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
/*     */ class PotionSlot
/*     */   extends Slot
/*     */ {
/*     */   public PotionSlot(Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/* 134 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mayPlace(ItemStack paramItemStack) {
/* 139 */     return mayPlaceItem(paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/* 144 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {
/* 149 */     Optional<Holder> optional = ((PotionContents)paramItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 150 */     if (optional.isPresent() && paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/* 151 */       CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, optional.get()); }
/*     */     
/* 153 */     super.onTake(paramPlayer, paramItemStack);
/*     */   }
/*     */   
/*     */   public static boolean mayPlaceItem(ItemStack paramItemStack) {
/* 157 */     return (paramItemStack.is(Items.POTION) || paramItemStack.is(Items.SPLASH_POTION) || paramItemStack.is(Items.LINGERING_POTION) || paramItemStack.is(Items.GLASS_BOTTLE));
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getNoItemIcon() {
/* 162 */     return BrewingStandMenu.EMPTY_SLOT_POTION;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\BrewingStandMenu$PotionSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */