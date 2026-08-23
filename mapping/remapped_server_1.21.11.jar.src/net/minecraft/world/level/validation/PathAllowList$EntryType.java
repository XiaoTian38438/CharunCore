/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
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
/*    */ @FunctionalInterface
/*    */ public interface EntryType
/*    */ {
/* 21 */   public static final EntryType FILESYSTEM = FileSystem::getPathMatcher;
/*    */   public static final EntryType PREFIX = (paramFileSystem, paramString) -> ();
/*    */   
/*    */   PathMatcher compile(FileSystem paramFileSystem, String paramString);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\validation\PathAllowList$EntryType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */