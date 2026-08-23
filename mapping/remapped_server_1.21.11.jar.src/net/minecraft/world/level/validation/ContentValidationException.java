/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ContentValidationException extends Exception {
/*    */   private final Path directory;
/*    */   private final List<ForbiddenSymlinkInfo> entries;
/*    */   
/*    */   public ContentValidationException(Path paramPath, List<ForbiddenSymlinkInfo> paramList) {
/* 12 */     this.directory = paramPath;
/* 13 */     this.entries = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 18 */     return getMessage(this.directory, this.entries);
/*    */   }
/*    */   
/*    */   public static String getMessage(Path paramPath, List<ForbiddenSymlinkInfo> paramList) {
/* 22 */     return "Failed to validate '" + String.valueOf(paramPath) + "'. Found forbidden symlinks: " + (String)paramList.stream().map(paramForbiddenSymlinkInfo -> String.valueOf(paramForbiddenSymlinkInfo.link()) + "->" + String.valueOf(paramForbiddenSymlinkInfo.link())).collect(Collectors.joining(", "));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\validation\ContentValidationException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */