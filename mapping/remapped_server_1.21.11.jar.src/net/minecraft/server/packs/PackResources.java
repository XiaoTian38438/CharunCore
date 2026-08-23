/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ 
/*    */ public interface PackResources
/*    */   extends AutoCloseable
/*    */ {
/*    */   public static final String METADATA_EXTENSION = ".mcmeta";
/*    */   public static final String PACK_META = "pack.mcmeta";
/*    */   
/*    */   IoSupplier<InputStream> getRootResource(String... paramVarArgs);
/*    */   
/*    */   IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier);
/*    */   
/*    */   void listResources(PackType paramPackType, String paramString1, String paramString2, ResourceOutput paramResourceOutput);
/*    */   
/*    */   Set<String> getNamespaces(PackType paramPackType);
/*    */   
/*    */   <T> T getMetadataSection(MetadataSectionType<T> paramMetadataSectionType) throws IOException;
/*    */   
/*    */   PackLocationInfo location();
/*    */   
/*    */   default String packId() {
/* 32 */     return location().id();
/*    */   }
/*    */   
/*    */   default Optional<KnownPack> knownPackInfo() {
/* 36 */     return location().knownPackInfo();
/*    */   }
/*    */   
/*    */   void close();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\PackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */