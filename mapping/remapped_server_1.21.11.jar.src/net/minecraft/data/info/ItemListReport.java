/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public class ItemListReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public ItemListReport(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 22 */     this.output = paramPackOutput;
/* 23 */     this.registries = paramCompletableFuture;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 28 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("items.json");
/*    */     
/* 30 */     return this.registries.thenCompose(paramProvider -> {
/*    */           JsonObject jsonObject = new JsonObject();
/*    */           RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           paramProvider.lookupOrThrow(Registries.ITEM).listElements().forEach(());
/*    */           return DataProvider.saveStable(paramCachedOutput, (JsonElement)jsonObject, paramPath);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 47 */     return "Item List";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\ItemListReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */