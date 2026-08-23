/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class FileToIdConverter
/*    */ {
/*    */   private final String prefix;
/*    */   private final String extension;
/*    */   
/*    */   public FileToIdConverter(String paramString1, String paramString2) {
/* 16 */     this.prefix = paramString1;
/* 17 */     this.extension = paramString2;
/*    */   }
/*    */   
/*    */   public static FileToIdConverter json(String paramString) {
/* 21 */     return new FileToIdConverter(paramString, ".json");
/*    */   }
/*    */   
/*    */   public static FileToIdConverter registry(ResourceKey<? extends Registry<?>> paramResourceKey) {
/* 25 */     return json(Registries.elementsDirPath(paramResourceKey));
/*    */   }
/*    */   
/*    */   public Identifier idToFile(Identifier paramIdentifier) {
/* 29 */     return paramIdentifier.withPath(this.prefix + "/" + this.prefix + paramIdentifier.getPath());
/*    */   }
/*    */   
/*    */   public Identifier fileToId(Identifier paramIdentifier) {
/* 33 */     String str = paramIdentifier.getPath();
/* 34 */     return paramIdentifier.withPath(str.substring(this.prefix.length() + 1, str.length() - this.extension.length()));
/*    */   }
/*    */   
/*    */   public Map<Identifier, Resource> listMatchingResources(ResourceManager paramResourceManager) {
/* 38 */     return paramResourceManager.listResources(this.prefix, paramIdentifier -> paramIdentifier.getPath().endsWith(this.extension));
/*    */   }
/*    */   
/*    */   public Map<Identifier, List<Resource>> listMatchingResourceStacks(ResourceManager paramResourceManager) {
/* 42 */     return paramResourceManager.listResourceStacks(this.prefix, paramIdentifier -> paramIdentifier.getPath().endsWith(this.extension));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\resources\FileToIdConverter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */