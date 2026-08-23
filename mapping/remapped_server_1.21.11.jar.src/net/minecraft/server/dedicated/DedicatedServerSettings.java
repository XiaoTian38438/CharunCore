/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class DedicatedServerSettings {
/*    */   private final Path source;
/*    */   private DedicatedServerProperties properties;
/*    */   
/*    */   public DedicatedServerSettings(Path paramPath) {
/* 11 */     this.source = paramPath;
/* 12 */     this.properties = DedicatedServerProperties.fromFile(paramPath);
/*    */   }
/*    */   
/*    */   public DedicatedServerProperties getProperties() {
/* 16 */     return this.properties;
/*    */   }
/*    */   
/*    */   public void forceSave() {
/* 20 */     this.properties.store(this.source);
/*    */   }
/*    */   
/*    */   public DedicatedServerSettings update(UnaryOperator<DedicatedServerProperties> paramUnaryOperator) {
/* 24 */     (this.properties = paramUnaryOperator.apply(this.properties)).store(this.source);
/* 25 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dedicated\DedicatedServerSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */