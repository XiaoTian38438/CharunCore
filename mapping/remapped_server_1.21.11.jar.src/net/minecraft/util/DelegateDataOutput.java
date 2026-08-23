/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.SuppressForbidden;
/*    */ 
/*    */ public class DelegateDataOutput
/*    */   implements DataOutput {
/*    */   private final DataOutput parent;
/*    */   
/*    */   public DelegateDataOutput(DataOutput paramDataOutput) {
/* 12 */     this.parent = paramDataOutput;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int paramInt) throws IOException {
/* 17 */     this.parent.write(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] paramArrayOfbyte) throws IOException {
/* 22 */     this.parent.write(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/* 27 */     this.parent.write(paramArrayOfbyte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeBoolean(boolean paramBoolean) throws IOException {
/* 32 */     this.parent.writeBoolean(paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeByte(int paramInt) throws IOException {
/* 37 */     this.parent.writeByte(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeShort(int paramInt) throws IOException {
/* 42 */     this.parent.writeShort(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeChar(int paramInt) throws IOException {
/* 47 */     this.parent.writeChar(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInt(int paramInt) throws IOException {
/* 52 */     this.parent.writeInt(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeLong(long paramLong) throws IOException {
/* 57 */     this.parent.writeLong(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeFloat(float paramFloat) throws IOException {
/* 62 */     this.parent.writeFloat(paramFloat);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeDouble(double paramDouble) throws IOException {
/* 67 */     this.parent.writeDouble(paramDouble);
/*    */   }
/*    */ 
/*    */   
/*    */   @SuppressForbidden(a = "Delegation is not use")
/*    */   public void writeBytes(String paramString) throws IOException {
/* 73 */     this.parent.writeBytes(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeChars(String paramString) throws IOException {
/* 78 */     this.parent.writeChars(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeUTF(String paramString) throws IOException {
/* 83 */     this.parent.writeUTF(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\DelegateDataOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */