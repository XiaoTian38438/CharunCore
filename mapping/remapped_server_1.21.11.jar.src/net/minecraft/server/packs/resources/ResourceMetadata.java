/*    */ package net.minecraft.server.packs.resources;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public interface ResourceMetadata {
/* 20 */   public static final ResourceMetadata EMPTY = new ResourceMetadata()
/*    */     {
/*    */       public <T> Optional<T> getSection(MetadataSectionType<T> param1MetadataSectionType) {
/* 23 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   public static final IoSupplier<ResourceMetadata> EMPTY_SUPPLIER = () -> EMPTY;
/*    */   
/* 29 */   static ResourceMetadata fromJsonStream(InputStream paramInputStream) throws IOException { BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, StandardCharsets.UTF_8)); 
/* 30 */     try { final JsonObject metadata = GsonHelper.parse(bufferedReader);
/*    */       
/* 32 */       ResourceMetadata resourceMetadata = new ResourceMetadata()
/*    */         {
/*    */           public <T> Optional<T> getSection(MetadataSectionType<T> param1MetadataSectionType) {
/* 35 */             String str = param1MetadataSectionType.name();
/* 36 */             if (metadata.has(str)) {
/* 37 */               Object object = param1MetadataSectionType.codec().parse((DynamicOps)JsonOps.INSTANCE, metadata.get(str)).getOrThrow(com.google.gson.JsonParseException::new);
/* 38 */               return Optional.of((T)object);
/*    */             } 
/* 40 */             return Optional.empty();
/*    */           }
/*    */         };
/*    */       
/* 44 */       bufferedReader.close(); return resourceMetadata; }
/*    */     catch (Throwable throwable) { try {
/*    */         bufferedReader.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       }  throw throwable; }
/* 50 */      } default <T> Optional<MetadataSectionType.WithValue<T>> getTypedSection(MetadataSectionType<T> paramMetadataSectionType) { Objects.requireNonNull(paramMetadataSectionType); return getSection(paramMetadataSectionType).map(paramMetadataSectionType::withValue); }
/*    */ 
/*    */   
/*    */   default List<MetadataSectionType.WithValue<?>> getTypedSections(Collection<MetadataSectionType<?>> paramCollection) {
/* 54 */     return (List<MetadataSectionType.WithValue<?>>)paramCollection.stream().map(this::getTypedSection).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/*    */   }
/*    */   
/*    */   <T> Optional<T> getSection(MetadataSectionType<T> paramMetadataSectionType);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ResourceMetadata.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */