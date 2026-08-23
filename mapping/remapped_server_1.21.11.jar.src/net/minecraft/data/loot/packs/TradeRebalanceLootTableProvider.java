/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.loot.LootTableProvider;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class TradeRebalanceLootTableProvider {
/*    */   public static LootTableProvider create(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     return new LootTableProvider(paramPackOutput, 
/*    */         
/* 16 */         Set.of(), 
/* 17 */         List.of(new LootTableProvider.SubProviderEntry(TradeRebalanceChestLoot::new, LootContextParamSets.CHEST)), paramCompletableFuture);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\packs\TradeRebalanceLootTableProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */