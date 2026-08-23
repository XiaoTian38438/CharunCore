/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public abstract class EnchantmentTagsProvider
/*    */   extends KeyTagProvider<Enchantment> {
/*    */   public EnchantmentTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 18 */     super(paramPackOutput, Registries.ENCHANTMENT, paramCompletableFuture);
/*    */   }
/*    */   
/*    */   protected void tooltipOrder(HolderLookup.Provider paramProvider, ResourceKey<Enchantment>... paramVarArgs) {
/* 22 */     tag(EnchantmentTags.TOOLTIP_ORDER).add(paramVarArgs);
/* 23 */     Set<ResourceKey<Enchantment>> set = Set.of(paramVarArgs);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     List list = (List)paramProvider.lookupOrThrow(Registries.ENCHANTMENT).listElements().filter(paramReference -> !paramSet.contains(paramReference.unwrapKey().get())).map(Holder::getRegisteredName).collect(Collectors.toList());
/* 29 */     if (!list.isEmpty())
/* 30 */       throw new IllegalStateException("Not all enchantments were registered for tooltip ordering. Missing: " + String.join(", ", list)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\EnchantmentTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */