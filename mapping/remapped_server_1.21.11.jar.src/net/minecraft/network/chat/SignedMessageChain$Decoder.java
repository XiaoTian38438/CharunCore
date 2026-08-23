/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.BooleanSupplier;
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
/*    */ @FunctionalInterface
/*    */ public interface Decoder
/*    */ {
/*    */   static Decoder unsigned(UUID paramUUID, BooleanSupplier paramBooleanSupplier) {
/* 93 */     return (paramMessageSignature, paramSignedMessageBody) -> {
/*    */         if (paramBooleanSupplier.getAsBoolean())
/*    */           throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY); 
/*    */         return PlayerChatMessage.unsigned(paramUUID, paramSignedMessageBody.content());
/*    */       };
/*    */   }
/*    */   
/*    */   default void setChainBroken() {}
/*    */   
/*    */   PlayerChatMessage unpack(MessageSignature paramMessageSignature, SignedMessageBody paramSignedMessageBody) throws SignedMessageChain.DecodeException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\SignedMessageChain$Decoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */