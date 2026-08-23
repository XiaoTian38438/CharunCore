/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathProvider
/*    */ {
/*    */   private final Path root;
/*    */   private final String kind;
/*    */   
/*    */   PathProvider(PackOutput paramPackOutput, PackOutput.Target paramTarget, String paramString) {
/* 43 */     this.root = paramPackOutput.getOutputFolder(paramTarget);
/* 44 */     this.kind = paramString;
/*    */   }
/*    */   
/*    */   public Path file(Identifier paramIdentifier, String paramString) {
/* 48 */     return this.root.resolve(paramIdentifier.getNamespace()).resolve(this.kind).resolve(paramIdentifier.getPath() + "." + paramIdentifier.getPath());
/*    */   }
/*    */   
/*    */   public Path json(Identifier paramIdentifier) {
/* 52 */     return this.root.resolve(paramIdentifier.getNamespace()).resolve(this.kind).resolve(paramIdentifier.getPath() + ".json");
/*    */   }
/*    */   
/*    */   public Path json(ResourceKey<?> paramResourceKey) {
/* 56 */     return this.root.resolve(paramResourceKey.identifier().getNamespace()).resolve(this.kind).resolve(paramResourceKey.identifier().getPath() + ".json");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\PackOutput$PathProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */