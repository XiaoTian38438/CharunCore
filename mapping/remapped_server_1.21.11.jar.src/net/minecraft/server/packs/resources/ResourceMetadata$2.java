/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
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
/*    */ class null
/*    */   implements ResourceMetadata
/*    */ {
/*    */   public <T> Optional<T> getSection(MetadataSectionType<T> paramMetadataSectionType) {
/* 35 */     String str = paramMetadataSectionType.name();
/* 36 */     if (metadata.has(str)) {
/* 37 */       Object object = paramMetadataSectionType.codec().parse((DynamicOps)JsonOps.INSTANCE, metadata.get(str)).getOrThrow(com.google.gson.JsonParseException::new);
/* 38 */       return Optional.of((T)object);
/*    */     } 
/* 40 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ResourceMetadata$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */