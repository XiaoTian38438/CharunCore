/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class TranslatableFormatException extends IllegalArgumentException {
/*    */   public TranslatableFormatException(TranslatableContents paramTranslatableContents, String paramString) {
/*  7 */     super(String.format(Locale.ROOT, "Error parsing: %s: %s", new Object[] { paramTranslatableContents, paramString }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableContents paramTranslatableContents, int paramInt) {
/* 11 */     super(String.format(Locale.ROOT, "Invalid index %d requested for %s", new Object[] { Integer.valueOf(paramInt), paramTranslatableContents }));
/*    */   }
/*    */   
/*    */   public TranslatableFormatException(TranslatableContents paramTranslatableContents, Throwable paramThrowable) {
/* 15 */     super(String.format(Locale.ROOT, "Error while parsing: %s", new Object[] { paramTranslatableContents }), paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\TranslatableFormatException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */