/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.FileUtil;
/*    */ 
/*    */ public interface CachedOutput {
/*    */   static {
/* 11 */     NO_CACHE = ((paramPath, paramArrayOfbyte, paramHashCode) -> {
/*    */         FileUtil.createDirectoriesSafe(paramPath.getParent());
/*    */         Files.write(paramPath, paramArrayOfbyte, new java.nio.file.OpenOption[0]);
/*    */       });
/*    */   }
/*    */   
/*    */   public static final CachedOutput NO_CACHE;
/*    */   
/*    */   void writeIfNeeded(Path paramPath, byte[] paramArrayOfbyte, HashCode paramHashCode) throws IOException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\CachedOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */