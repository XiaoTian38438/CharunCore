/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class NetworkDataOutputStream {
/*    */   private final ByteArrayOutputStream outputStream;
/*    */   private final DataOutputStream dataOutputStream;
/*    */   
/*    */   public NetworkDataOutputStream(int paramInt) {
/* 13 */     this.outputStream = new ByteArrayOutputStream(paramInt);
/* 14 */     this.dataOutputStream = new DataOutputStream(this.outputStream);
/*    */   }
/*    */   
/*    */   public void writeBytes(byte[] paramArrayOfbyte) throws IOException {
/* 18 */     this.dataOutputStream.write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*    */   }
/*    */   
/*    */   public void writeString(String paramString) throws IOException {
/* 22 */     this.dataOutputStream.write(paramString.getBytes(StandardCharsets.UTF_8));
/* 23 */     this.dataOutputStream.write(0);
/*    */   }
/*    */   
/*    */   public void write(int paramInt) throws IOException {
/* 27 */     this.dataOutputStream.write(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeShort(short paramShort) throws IOException {
/* 32 */     this.dataOutputStream.writeShort(Short.reverseBytes(paramShort));
/*    */   }
/*    */   
/*    */   public void writeInt(int paramInt) throws IOException {
/* 36 */     this.dataOutputStream.writeInt(Integer.reverseBytes(paramInt));
/*    */   }
/*    */   
/*    */   public void writeFloat(float paramFloat) throws IOException {
/* 40 */     this.dataOutputStream.writeInt(Integer.reverseBytes(Float.floatToIntBits(paramFloat)));
/*    */   }
/*    */   
/*    */   public byte[] toByteArray() {
/* 44 */     return this.outputStream.toByteArray();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 48 */     this.outputStream.reset();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\NetworkDataOutputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */