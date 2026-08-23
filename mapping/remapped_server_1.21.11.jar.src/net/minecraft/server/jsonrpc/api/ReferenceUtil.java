/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ReferenceUtil {
/*    */   static {
/* 10 */     REFERENCE_CODEC = Codec.STRING.comapFlatMap(paramString -> {
/*    */           
/*    */           try {
/*    */             return DataResult.success(new URI(paramString));
/* 14 */           } catch (URISyntaxException uRISyntaxException) {
/*    */             Objects.requireNonNull(uRISyntaxException);
/*    */             return DataResult.error(uRISyntaxException::getMessage);
/*    */           } 
/*    */         }URI::toString);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<URI> REFERENCE_CODEC;
/*    */ 
/*    */   
/*    */   public static URI createLocalReference(String paramString) {
/* 27 */     return URI.create("#/components/schemas/" + paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\api\ReferenceUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */