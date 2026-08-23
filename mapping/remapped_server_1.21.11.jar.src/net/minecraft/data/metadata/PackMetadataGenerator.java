/*    */ package net.minecraft.data.metadata;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.DetectedVersion;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public class PackMetadataGenerator implements DataProvider {
/*    */   private final PackOutput output;
/* 25 */   private final Map<String, Supplier<JsonElement>> elements = new HashMap<>();
/*    */   
/*    */   public PackMetadataGenerator(PackOutput paramPackOutput) {
/* 28 */     this.output = paramPackOutput;
/*    */   }
/*    */   
/*    */   public <T> PackMetadataGenerator add(MetadataSectionType<T> paramMetadataSectionType, T paramT) {
/* 32 */     this.elements.put(paramMetadataSectionType.name(), () -> ((JsonElement)paramMetadataSectionType.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, paramObject).getOrThrow(IllegalArgumentException::new)).getAsJsonObject());
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 38 */     JsonObject jsonObject = new JsonObject();
/* 39 */     this.elements.forEach((paramString, paramSupplier) -> paramJsonObject.add(paramString, paramSupplier.get()));
/* 40 */     return DataProvider.saveStable(paramCachedOutput, (JsonElement)jsonObject, this.output.getOutputFolder().resolve("pack.mcmeta"));
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 45 */     return "Pack Metadata";
/*    */   }
/*    */   
/*    */   public static PackMetadataGenerator forFeaturePack(PackOutput paramPackOutput, Component paramComponent) {
/* 49 */     return (new PackMetadataGenerator(paramPackOutput))
/* 50 */       .add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(paramComponent, DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA).minorRange()));
/*    */   }
/*    */   
/*    */   public static PackMetadataGenerator forFeaturePack(PackOutput paramPackOutput, Component paramComponent, FeatureFlagSet paramFeatureFlagSet) {
/* 54 */     return forFeaturePack(paramPackOutput, paramComponent)
/* 55 */       .add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(paramFeatureFlagSet));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\metadata\PackMetadataGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */