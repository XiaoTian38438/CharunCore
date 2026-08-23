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
/*    */ public class Synchronized
/*    */   implements RemoteSlot
/*    */ {
/*    */   private final HashedPatchMap.HashGenerator hasher;
/* 41 */   private ItemStack remoteStack = null;
/* 42 */   private HashedStack remoteHash = null;
/*    */   
/*    */   public Synchronized(HashedPatchMap.HashGenerator paramHashGenerator) {
/* 45 */     this.hasher = paramHashGenerator;
/*    */   }
/*    */ 
/*    */   
/*    */   public void force(ItemStack paramItemStack) {
/* 50 */     this.remoteStack = paramItemStack.copy();
/* 51 */     this.remoteHash = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void receive(HashedStack paramHashedStack) {
/* 56 */     this.remoteStack = null;
/* 57 */     this.remoteHash = paramHashedStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack paramItemStack) {
/* 62 */     if (this.remoteStack != null) {
/* 63 */       return ItemStack.matches(this.remoteStack, paramItemStack);
/*    */     }
/*    */     
/* 66 */     if (this.remoteHash != null && 
/* 67 */       this.remoteHash.matches(paramItemStack, this.hasher)) {
/*    */       
/* 69 */       this.remoteStack = paramItemStack.copy();
/* 70 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public void copyFrom(Synchronized paramSynchronized) {
/* 78 */     this.remoteStack = paramSynchronized.remoteStack;
/* 79 */     this.remoteHash = paramSynchronized.remoteHash;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\RemoteSlot$Synchronized.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */