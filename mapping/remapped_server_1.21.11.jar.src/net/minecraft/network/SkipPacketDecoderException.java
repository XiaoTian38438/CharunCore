/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketDecoderException extends DecoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*    */   public SkipPacketDecoderException(String paramString) {
/*  8 */     super(paramString);
/*    */   }
/*    */   
/*    */   public SkipPacketDecoderException(Throwable paramThrowable) {
/* 12 */     super(paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\SkipPacketDecoderException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */