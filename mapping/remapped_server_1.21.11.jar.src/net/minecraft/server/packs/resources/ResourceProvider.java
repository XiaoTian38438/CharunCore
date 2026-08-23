/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ResourceProvider
/*    */ {
/*    */   public static final ResourceProvider EMPTY = paramIdentifier -> Optional.empty();
/*    */   
/*    */   default Resource getResourceOrThrow(Identifier paramIdentifier) throws FileNotFoundException {
/* 23 */     return getResource(paramIdentifier).<Throwable>orElseThrow(() -> new FileNotFoundException(paramIdentifier.toString()));
/*    */   }
/*    */   
/*    */   default InputStream open(Identifier paramIdentifier) throws IOException {
/* 27 */     return getResourceOrThrow(paramIdentifier).open();
/*    */   }
/*    */   
/*    */   default BufferedReader openAsReader(Identifier paramIdentifier) throws IOException {
/* 31 */     return getResourceOrThrow(paramIdentifier).openAsReader();
/*    */   }
/*    */   
/*    */   static ResourceProvider fromMap(Map<Identifier, Resource> paramMap) {
/* 35 */     return paramIdentifier -> Optional.ofNullable((Resource)paramMap.get(paramIdentifier));
/*    */   }
/*    */   
/*    */   Optional<Resource> getResource(Identifier paramIdentifier);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ResourceProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */