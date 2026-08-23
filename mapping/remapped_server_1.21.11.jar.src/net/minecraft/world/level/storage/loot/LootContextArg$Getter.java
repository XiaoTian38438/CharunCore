/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKey;
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
/*    */ public interface Getter<T, R>
/*    */   extends LootContextArg<R>
/*    */ {
/*    */   R get(T paramT);
/*    */   
/*    */   ContextKey<? extends T> contextParam();
/*    */   
/*    */   default R get(LootContext paramLootContext) {
/* 43 */     T t = (T)paramLootContext.getOptionalParameter((ContextKey)contextParam());
/* 44 */     return (t != null) ? get(t) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootContextArg$Getter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */