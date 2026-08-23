/*    */ package net.minecraft.data.info;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.BlockTypes;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockListReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public BlockListReport(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 28 */     this.output = paramPackOutput;
/* 29 */     this.registries = paramCompletableFuture;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 34 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("blocks.json");
/*    */     
/* 36 */     return this.registries.thenCompose(paramProvider -> {
/*    */           JsonObject jsonObject = new JsonObject();
/*    */           RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           paramProvider.lookupOrThrow(Registries.BLOCK).listElements().forEach(());
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 89 */     return "Block List";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\BlockListReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */