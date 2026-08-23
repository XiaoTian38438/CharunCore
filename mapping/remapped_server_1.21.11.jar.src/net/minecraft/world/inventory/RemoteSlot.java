/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.network.HashedPatchMap;
/*    */ import net.minecraft.network.HashedStack;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface RemoteSlot
/*    */ {
/* 24 */   public static final RemoteSlot PLACEHOLDER = new RemoteSlot()
/*    */     {
/*    */       public void receive(HashedStack param1HashedStack) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void force(ItemStack param1ItemStack) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean matches(ItemStack param1ItemStack) {
/* 35 */         return true;
/*    */       }
/*    */     };
/*    */   void force(ItemStack paramItemStack);
/*    */   void receive(HashedStack paramHashedStack);
/*    */   boolean matches(ItemStack paramItemStack);
/* 41 */   public static class Synchronized implements RemoteSlot { private ItemStack remoteStack = null; private final HashedPatchMap.HashGenerator hasher;
/* 42 */     private HashedStack remoteHash = null;
/*    */     
/*    */     public Synchronized(HashedPatchMap.HashGenerator param1HashGenerator) {
/* 45 */       this.hasher = param1HashGenerator;
/*    */     }
/*    */ 
/*    */     
/*    */     public void force(ItemStack param1ItemStack) {
/* 50 */       this.remoteStack = param1ItemStack.copy();
/* 51 */       this.remoteHash = null;
/*    */     }
/*    */ 
/*    */     
/*    */     public void receive(HashedStack param1HashedStack) {
/* 56 */       this.remoteStack = null;
/* 57 */       this.remoteHash = param1HashedStack;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(ItemStack param1ItemStack) {
/* 62 */       if (this.remoteStack != null) {
/* 63 */         return ItemStack.matches(this.remoteStack, param1ItemStack);
/*    */       }
/*    */       
/* 66 */       if (this.remoteHash != null && 
/* 67 */         this.remoteHash.matches(param1ItemStack, this.hasher)) {
/*    */         
/* 69 */         this.remoteStack = param1ItemStack.copy();
/* 70 */         return true;
/*    */       } 
/*    */ 
/*    */       
/* 74 */       return false;
/*    */     }
/*    */     
/*    */     public void copyFrom(Synchronized param1Synchronized) {
/* 78 */       this.remoteStack = param1Synchronized.remoteStack;
/* 79 */       this.remoteHash = param1Synchronized.remoteHash;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\RemoteSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */