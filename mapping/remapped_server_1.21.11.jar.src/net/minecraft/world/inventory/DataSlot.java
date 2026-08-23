/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public abstract class DataSlot {
/*    */   public static DataSlot forContainer(final ContainerData container, final int dataId) {
/*  5 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/*  8 */           return container.get(dataId);
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int param1Int) {
/* 13 */           container.set(dataId, param1Int);
/*    */         }
/*    */       };
/*    */   }
/*    */   private int prevValue;
/*    */   public static DataSlot shared(final int[] storage, final int index) {
/* 19 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/* 22 */           return storage[index];
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int param1Int) {
/* 27 */           storage[index] = param1Int;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static DataSlot standalone() {
/* 33 */     return new DataSlot()
/*    */       {
/*    */         private int value;
/*    */         
/*    */         public int get() {
/* 38 */           return this.value;
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int param1Int) {
/* 43 */           this.value = param1Int;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract int get();
/*    */ 
/*    */   
/*    */   public abstract void set(int paramInt);
/*    */   
/*    */   public boolean checkAndClearUpdateFlag() {
/* 55 */     int i = get();
/* 56 */     boolean bool = (i != this.prevValue) ? true : false;
/* 57 */     this.prevValue = i;
/* 58 */     return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\DataSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */