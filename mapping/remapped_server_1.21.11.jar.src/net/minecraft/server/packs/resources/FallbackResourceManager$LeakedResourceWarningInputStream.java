/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LeakedResourceWarningInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Supplier<String> message;
/*     */   private boolean closed;
/*     */   
/*     */   public LeakedResourceWarningInputStream(InputStream paramInputStream, Identifier paramIdentifier, String paramString) {
/* 105 */     super(paramInputStream);
/* 106 */     Exception exception = new Exception("Stacktrace");
/* 107 */     this.message = (() -> {
/*     */         StringWriter stringWriter = new StringWriter();
/*     */         paramException.printStackTrace(new PrintWriter(stringWriter));
/*     */         return "Leaked resource: '" + String.valueOf(paramIdentifier) + "' loaded from pack: '" + paramString + "'\n" + String.valueOf(stringWriter);
/*     */       });
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     super.close();
/* 117 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 122 */     if (!this.closed) {
/* 123 */       FallbackResourceManager.LOGGER.warn("{}", this.message.get());
/*     */     }
/*     */     
/* 126 */     super.finalize();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\FallbackResourceManager$LeakedResourceWarningInputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */