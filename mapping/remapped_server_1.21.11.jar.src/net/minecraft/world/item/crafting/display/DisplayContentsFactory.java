/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface DisplayContentsFactory<T> {
/*    */   public static interface ForRemainders<T>
/*    */     extends DisplayContentsFactory<T> {
/*    */     T addRemainder(T param1T, List<T> param1List);
/*    */   }
/*    */   
/*    */   public static interface ForStacks<T> extends DisplayContentsFactory<T> {
/*    */     default T forStack(Holder<Item> param1Holder) {
/* 17 */       return forStack(new ItemStack(param1Holder));
/*    */     }
/*    */     
/*    */     default T forStack(Item param1Item) {
/* 21 */       return forStack(new ItemStack((ItemLike)param1Item));
/*    */     }
/*    */     
/*    */     T forStack(ItemStack param1ItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\DisplayContentsFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */