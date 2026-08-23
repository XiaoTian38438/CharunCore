/*    */ package net.minecraft.data.registries;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.enchantment.providers.TradeRebalanceEnchantmentProviders;
/*    */ 
/*    */ public class TradeRebalanceRegistries
/*    */ {
/* 11 */   private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
/* 12 */     .add(Registries.ENCHANTMENT_PROVIDER, TradeRebalanceEnchantmentProviders::bootstrap);
/*    */   
/*    */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 15 */     return RegistryPatchGenerator.createLookup(paramCompletableFuture, BUILDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\registries\TradeRebalanceRegistries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */