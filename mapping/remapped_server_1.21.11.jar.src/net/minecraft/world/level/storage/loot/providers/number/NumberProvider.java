/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface NumberProvider extends LootContextUser {
/*    */   float getFloat(LootContext paramLootContext);
/*    */   
/*    */   default int getInt(LootContext paramLootContext) {
/* 10 */     return Math.round(getFloat(paramLootContext));
/*    */   }
/*    */   
/*    */   LootNumberProviderType getType();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */