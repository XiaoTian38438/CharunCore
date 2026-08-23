/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketEncoderException extends EncoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*    */   public SkipPacketEncoderException(String paramString) {
/*  8 */     super(paramString);
/*    */   }
/*    */   
/*    */   public SkipPacketEncoderException(Throwable paramThrowable) {
/* 12 */     super(paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\SkipPacketEncoderException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */