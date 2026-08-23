/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.validation.DirectoryValidator;
/*    */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PackDetector<T>
/*    */ {
/*    */   private final DirectoryValidator validator;
/*    */   
/*    */   protected PackDetector(DirectoryValidator paramDirectoryValidator) {
/* 20 */     this.validator = paramDirectoryValidator;
/*    */   }
/*    */   public T detectPackResources(Path paramPath, List<ForbiddenSymlinkInfo> paramList) throws IOException {
/*    */     BasicFileAttributes basicFileAttributes;
/* 24 */     Path path = paramPath;
/*    */     
/*    */     try {
/* 27 */       basicFileAttributes = Files.readAttributes(paramPath, (Class)BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 28 */     } catch (NoSuchFileException noSuchFileException) {
/* 29 */       return null;
/*    */     } 
/*    */     
/* 32 */     if (basicFileAttributes.isSymbolicLink()) {
/* 33 */       this.validator.validateSymlink(paramPath, paramList);
/* 34 */       if (!paramList.isEmpty()) {
/* 35 */         return null;
/*    */       }
/* 37 */       path = Files.readSymbolicLink(paramPath);
/* 38 */       basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*    */     } 
/*    */     
/* 41 */     if (basicFileAttributes.isDirectory()) {
/* 42 */       this.validator.validateKnownDirectory(path, paramList);
/* 43 */       if (!paramList.isEmpty()) {
/* 44 */         return null;
/*    */       }
/* 46 */       if (!Files.isRegularFile(path.resolve("pack.mcmeta"), new LinkOption[0])) {
/* 47 */         return null;
/*    */       }
/* 49 */       return createDirectoryPack(path);
/* 50 */     }  if (basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".zip")) {
/* 51 */       return createZipPack(path);
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract T createZipPack(Path paramPath) throws IOException;
/*    */   
/*    */   protected abstract T createDirectoryPack(Path paramPath) throws IOException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\PackDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */