/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Resource
/*    */ {
/*    */   private final PackResources source;
/*    */   private final IoSupplier<InputStream> streamSupplier;
/*    */   private final IoSupplier<ResourceMetadata> metadataSupplier;
/*    */   private ResourceMetadata cachedMetadata;
/*    */   
/*    */   public Resource(PackResources paramPackResources, IoSupplier<InputStream> paramIoSupplier, IoSupplier<ResourceMetadata> paramIoSupplier1) {
/* 22 */     this.source = paramPackResources;
/* 23 */     this.streamSupplier = paramIoSupplier;
/* 24 */     this.metadataSupplier = paramIoSupplier1;
/*    */   }
/*    */   
/*    */   public Resource(PackResources paramPackResources, IoSupplier<InputStream> paramIoSupplier) {
/* 28 */     this.source = paramPackResources;
/* 29 */     this.streamSupplier = paramIoSupplier;
/* 30 */     this.metadataSupplier = ResourceMetadata.EMPTY_SUPPLIER;
/* 31 */     this.cachedMetadata = ResourceMetadata.EMPTY;
/*    */   }
/*    */   
/*    */   public PackResources source() {
/* 35 */     return this.source;
/*    */   }
/*    */   
/*    */   public String sourcePackId() {
/* 39 */     return this.source.packId();
/*    */   }
/*    */   
/*    */   public Optional<KnownPack> knownPackInfo() {
/* 43 */     return this.source.knownPackInfo();
/*    */   }
/*    */   
/*    */   public InputStream open() throws IOException {
/* 47 */     return this.streamSupplier.get();
/*    */   }
/*    */   
/*    */   public BufferedReader openAsReader() throws IOException {
/* 51 */     return new BufferedReader(new InputStreamReader(open(), StandardCharsets.UTF_8));
/*    */   }
/*    */   
/*    */   public ResourceMetadata metadata() throws IOException {
/* 55 */     if (this.cachedMetadata == null) {
/* 56 */       this.cachedMetadata = this.metadataSupplier.get();
/*    */     }
/* 58 */     return this.cachedMetadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\Resource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */