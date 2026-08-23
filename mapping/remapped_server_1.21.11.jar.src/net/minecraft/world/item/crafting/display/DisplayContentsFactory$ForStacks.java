/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ForStacks<T>
/*    */   extends DisplayContentsFactory<T>
/*    */ {
/*    */   default T forStack(Holder<Item> paramHolder) {
/* 17 */     return forStack(new ItemStack(paramHolder));
/*    */   }
/*    */   
/*    */   default T forStack(Item paramItem) {
/* 21 */     return forStack(new ItemStack((ItemLike)paramItem));
/*    */   }
/*    */   
/*    */   T forStack(ItemStack paramItemStack);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\DisplayContentsFactory$ForStacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */