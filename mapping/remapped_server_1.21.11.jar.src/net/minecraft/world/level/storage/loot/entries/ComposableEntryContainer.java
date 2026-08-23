/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface ComposableEntryContainer
/*    */ {
/*    */   public static final ComposableEntryContainer ALWAYS_FALSE = (paramLootContext, paramConsumer) -> false;
/*    */   public static final ComposableEntryContainer ALWAYS_TRUE = (paramLootContext, paramConsumer) -> true;
/*    */   
/*    */   default ComposableEntryContainer and(ComposableEntryContainer paramComposableEntryContainer) {
/* 16 */     Objects.requireNonNull(paramComposableEntryContainer);
/* 17 */     return (paramLootContext, paramConsumer) -> (expand(paramLootContext, paramConsumer) && paramComposableEntryContainer.expand(paramLootContext, paramConsumer));
/*    */   }
/*    */   
/*    */   default ComposableEntryContainer or(ComposableEntryContainer paramComposableEntryContainer) {
/* 21 */     Objects.requireNonNull(paramComposableEntryContainer);
/* 22 */     return (paramLootContext, paramConsumer) -> (expand(paramLootContext, paramConsumer) || paramComposableEntryContainer.expand(paramLootContext, paramConsumer));
/*    */   }
/*    */   
/*    */   boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\ComposableEntryContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */