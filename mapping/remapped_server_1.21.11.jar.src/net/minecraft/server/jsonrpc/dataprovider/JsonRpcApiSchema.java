/*    */ package net.minecraft.server.jsonrpc.dataprovider;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.server.jsonrpc.api.Schema;
/*    */ import net.minecraft.server.jsonrpc.methods.DiscoveryService;
/*    */ 
/*    */ public class JsonRpcApiSchema implements DataProvider {
/*    */   private final Path path;
/*    */   
/*    */   public JsonRpcApiSchema(PackOutput paramPackOutput) {
/* 18 */     this.path = paramPackOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("json-rpc-api-schema.json");
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 23 */     DiscoveryService.DiscoverResponse discoverResponse = DiscoveryService.discover(Schema.getSchemaRegistry());
/* 24 */     return DataProvider.saveStable(paramCachedOutput, (JsonElement)DiscoveryService.DiscoverResponse.CODEC.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, discoverResponse).getOrThrow(), this.path);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 29 */     return "Json RPC API schema";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\dataprovider\JsonRpcApiSchema.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */