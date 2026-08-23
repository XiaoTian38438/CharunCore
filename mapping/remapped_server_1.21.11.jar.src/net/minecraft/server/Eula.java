/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Properties;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class Eula
/*    */ {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path file;
/*    */   private final boolean agreed;
/*    */   
/*    */   public Eula(Path paramPath) {
/* 21 */     this.file = paramPath;
/* 22 */     this.agreed = (SharedConstants.IS_RUNNING_IN_IDE || readFile());
/*    */   }
/*    */   private boolean readFile() {
/*    */     
/* 26 */     try { InputStream inputStream = Files.newInputStream(this.file, new java.nio.file.OpenOption[0]); 
/* 27 */       try { Properties properties = new Properties();
/* 28 */         properties.load(inputStream);
/* 29 */         boolean bool = Boolean.parseBoolean(properties.getProperty("eula", "false"));
/* 30 */         if (inputStream != null) inputStream.close();  return bool; } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/* 31 */     { LOGGER.warn("Failed to load {}", this.file);
/* 32 */       saveDefaults();
/*    */       
/* 34 */       return false; }
/*    */   
/*    */   }
/*    */   public boolean hasAgreedToEULA() {
/* 38 */     return this.agreed;
/*    */   }
/*    */   
/*    */   private void saveDefaults() {
/* 42 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*    */       return; 
/*    */     
/* 45 */     try { OutputStream outputStream = Files.newOutputStream(this.file, new java.nio.file.OpenOption[0]); 
/* 46 */       try { Properties properties = new Properties();
/* 47 */         properties.setProperty("eula", "false");
/* 48 */         properties.store(outputStream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (" + String.valueOf(CommonLinks.EULA) + ").");
/* 49 */         if (outputStream != null) outputStream.close();  } catch (Throwable throwable) { if (outputStream != null) try { outputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/* 50 */     { LOGGER.warn("Failed to save {}", this.file, exception); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\Eula.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */