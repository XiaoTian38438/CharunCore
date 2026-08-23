/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.DefaultedRegistry;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class RegistryDumpReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public RegistryDumpReport(PackOutput paramPackOutput) {
/* 20 */     this.output = paramPackOutput;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 25 */     JsonObject jsonObject = new JsonObject();
/*    */     
/* 27 */     BuiltInRegistries.REGISTRY.listElements().forEach(paramReference -> paramJsonObject.add(paramReference.key().identifier().toString(), dumpRegistry((Registry)paramReference.value())));
/*    */     
/* 29 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("registries.json");
/* 30 */     return DataProvider.saveStable(paramCachedOutput, (JsonElement)jsonObject, path);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> JsonElement dumpRegistry(Registry<T> paramRegistry) {
/* 35 */     JsonObject jsonObject1 = new JsonObject();
/*    */     
/* 37 */     if (paramRegistry instanceof DefaultedRegistry) {
/* 38 */       Identifier identifier = ((DefaultedRegistry)paramRegistry).getDefaultKey();
/* 39 */       jsonObject1.addProperty("default", identifier.toString());
/*    */     } 
/*    */     
/* 42 */     int i = BuiltInRegistries.REGISTRY.getId(paramRegistry);
/* 43 */     jsonObject1.addProperty("protocol_id", Integer.valueOf(i));
/*    */     
/* 45 */     JsonObject jsonObject2 = new JsonObject();
/* 46 */     paramRegistry.listElements().forEach(paramReference -> {
/*    */           Object object = paramReference.value();
/*    */           
/*    */           int i = paramRegistry.getId(object);
/*    */           
/*    */           JsonObject jsonObject = new JsonObject();
/*    */           jsonObject.addProperty("protocol_id", Integer.valueOf(i));
/*    */           paramJsonObject.add(paramReference.key().identifier().toString(), (JsonElement)jsonObject);
/*    */         });
/* 55 */     jsonObject1.add("entries", (JsonElement)jsonObject2);
/* 56 */     return (JsonElement)jsonObject1;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 61 */     return "Registry Dump";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\RegistryDumpReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */