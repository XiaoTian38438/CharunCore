/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.advancements.AdvancementProvider;
/*    */ 
/*    */ public class VanillaAdvancementProvider
/*    */ {
/*    */   public static AdvancementProvider create(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 12 */     return new AdvancementProvider(paramPackOutput, paramCompletableFuture, 
/*    */ 
/*    */         
/* 15 */         List.of(new VanillaTheEndAdvancements(), new VanillaHusbandryAdvancements(), new VanillaAdventureAdvancements(), new VanillaNetherAdvancements(), new VanillaStoryAdvancements()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\advancements\packs\VanillaAdvancementProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */