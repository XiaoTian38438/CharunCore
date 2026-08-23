/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class PktUtils {
/*    */   public static final int MAX_PACKET_SIZE = 1460;
/*  7 */   public static final char[] HEX_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*    */ 
/*    */ 
/*    */   
/*    */   public static String stringFromByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 12 */     int i = paramInt2 - 1;
/* 13 */     int j = (paramInt1 > i) ? i : paramInt1;
/* 14 */     while (0 != paramArrayOfbyte[j] && j < i) {
/* 15 */       j++;
/*    */     }
/*    */     
/* 18 */     return new String(paramArrayOfbyte, paramInt1, j - paramInt1, StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 22 */     return intFromByteArray(paramArrayOfbyte, paramInt, paramArrayOfbyte.length);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 26 */     if (0 > paramInt2 - paramInt1 - 4)
/*    */     {
/*    */       
/* 29 */       return 0;
/*    */     }
/* 31 */     return paramArrayOfbyte[paramInt1 + 3] << 24 | (paramArrayOfbyte[paramInt1 + 2] & 0xFF) << 16 | (paramArrayOfbyte[paramInt1 + 1] & 0xFF) << 8 | paramArrayOfbyte[paramInt1] & 0xFF;
/*    */   }
/*    */   
/*    */   public static int intFromNetworkByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 35 */     if (0 > paramInt2 - paramInt1 - 4)
/*    */     {
/*    */       
/* 38 */       return 0;
/*    */     }
/* 40 */     return paramArrayOfbyte[paramInt1] << 24 | (paramArrayOfbyte[paramInt1 + 1] & 0xFF) << 16 | (paramArrayOfbyte[paramInt1 + 2] & 0xFF) << 8 | paramArrayOfbyte[paramInt1 + 3] & 0xFF;
/*    */   }
/*    */   
/*    */   public static String toHexString(byte paramByte) {
/* 44 */     return "" + HEX_CHAR[(paramByte & 0xF0) >>> 4] + HEX_CHAR[(paramByte & 0xF0) >>> 4];
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\PktUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */