/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.util.SignatureValidator;
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
/*    */ public class KeyBased
/*    */   implements SignedMessageValidator
/*    */ {
/*    */   private final SignatureValidator validator;
/*    */   private final BooleanSupplier expired;
/*    */   private PlayerChatMessage lastMessage;
/*    */   private boolean isChainValid = true;
/*    */   
/*    */   public KeyBased(SignatureValidator paramSignatureValidator, BooleanSupplier paramBooleanSupplier) {
/* 34 */     this.validator = paramSignatureValidator;
/* 35 */     this.expired = paramBooleanSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean validateChain(PlayerChatMessage paramPlayerChatMessage) {
/* 40 */     if (paramPlayerChatMessage.equals(this.lastMessage)) {
/* 41 */       return true;
/*    */     }
/*    */     
/* 44 */     if (this.lastMessage != null && !paramPlayerChatMessage.link().isDescendantOf(this.lastMessage.link())) {
/* 45 */       LOGGER.error("Received out-of-order chat message from {}: expected index > {} for session {}, but was {} for session {}", new Object[] { paramPlayerChatMessage.sender(), Integer.valueOf(this.lastMessage.link().index()), this.lastMessage.link().sessionId(), Integer.valueOf(paramPlayerChatMessage.link().index()), paramPlayerChatMessage.link().sessionId() });
/* 46 */       return false;
/*    */     } 
/*    */     
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   private boolean validate(PlayerChatMessage paramPlayerChatMessage) {
/* 53 */     if (this.expired.getAsBoolean()) {
/* 54 */       LOGGER.error("Received message with expired profile public key from {} with session {}", paramPlayerChatMessage.sender(), paramPlayerChatMessage.link().sessionId());
/* 55 */       return false;
/*    */     } 
/* 57 */     if (!paramPlayerChatMessage.verify(this.validator)) {
/* 58 */       LOGGER.error("Received message with invalid signature (is the session wrong, or signature cache out of sync?): {}", PlayerChatMessage.describeSigned(paramPlayerChatMessage));
/* 59 */       return false;
/*    */     } 
/* 61 */     return validateChain(paramPlayerChatMessage);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerChatMessage updateAndValidate(PlayerChatMessage paramPlayerChatMessage) {
/* 66 */     this.isChainValid = (this.isChainValid && validate(paramPlayerChatMessage));
/* 67 */     if (!this.isChainValid) {
/* 68 */       return null;
/*    */     }
/* 70 */     this.lastMessage = paramPlayerChatMessage;
/* 71 */     return paramPlayerChatMessage;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\SignedMessageValidator$KeyBased.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */