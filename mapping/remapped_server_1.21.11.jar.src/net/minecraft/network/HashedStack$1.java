/*    */ package net.minecraft.network;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements HashedStack
/*    */ {
/*    */   public String toString() {
/* 17 */     return "<empty>";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack paramItemStack, HashedPatchMap.HashGenerator paramHashGenerator) {
/* 22 */     return paramItemStack.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\HashedStack$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */