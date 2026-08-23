/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.util.SignatureValidator;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface SignedMessageValidator
/*    */ {
/* 12 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final SignedMessageValidator ACCEPT_UNSIGNED = PlayerChatMessage::removeSignature;
/*    */   static {
/* 18 */     REJECT_ALL = (paramPlayerChatMessage -> {
/*    */         LOGGER.error("Received chat message from {}, but they have no chat session initialized and secure chat is enforced", paramPlayerChatMessage.sender());
/*    */         return null;
/*    */       });
/*    */   }
/*    */   
/*    */   public static final SignedMessageValidator REJECT_ALL;
/*    */   
/*    */   PlayerChatMessage updateAndValidate(PlayerChatMessage paramPlayerChatMessage);
/*    */   
/*    */   public static class KeyBased implements SignedMessageValidator { private final SignatureValidator validator;
/*    */     private final BooleanSupplier expired;
/*    */     private PlayerChatMessage lastMessage;
/*    */     private boolean isChainValid = true;
/*    */     
/*    */     public KeyBased(SignatureValidator param1SignatureValidator, BooleanSupplier param1BooleanSupplier) {
/* 34 */       this.validator = param1SignatureValidator;
/* 35 */       this.expired = param1BooleanSupplier;
/*    */     }
/*    */ 
/*    */     
/*    */     private boolean validateChain(PlayerChatMessage param1PlayerChatMessage) {
/* 40 */       if (param1PlayerChatMessage.equals(this.lastMessage)) {
/* 41 */         return true;
/*    */       }
/*    */       
/* 44 */       if (this.lastMessage != null && !param1PlayerChatMessage.link().isDescendantOf(this.lastMessage.link())) {
/* 45 */         LOGGER.error("Received out-of-order chat message from {}: expected index > {} for session {}, but was {} for session {}", new Object[] { param1PlayerChatMessage.sender(), Integer.valueOf(this.lastMessage.link().index()), this.lastMessage.link().sessionId(), Integer.valueOf(param1PlayerChatMessage.link().index()), param1PlayerChatMessage.link().sessionId() });
/* 46 */         return false;
/*    */       } 
/*    */       
/* 49 */       return true;
/*    */     }
/*    */     
/*    */     private boolean validate(PlayerChatMessage param1PlayerChatMessage) {
/* 53 */       if (this.expired.getAsBoolean()) {
/* 54 */         LOGGER.error("Received message with expired profile public key from {} with session {}", param1PlayerChatMessage.sender(), param1PlayerChatMessage.link().sessionId());
/* 55 */         return false;
/*    */       } 
/* 57 */       if (!param1PlayerChatMessage.verify(this.validator)) {
/* 58 */         LOGGER.error("Received message with invalid signature (is the session wrong, or signature cache out of sync?): {}", PlayerChatMessage.describeSigned(param1PlayerChatMessage));
/* 59 */         return false;
/*    */       } 
/* 61 */       return validateChain(param1PlayerChatMessage);
/*    */     }
/*    */ 
/*    */     
/*    */     public PlayerChatMessage updateAndValidate(PlayerChatMessage param1PlayerChatMessage) {
/* 66 */       this.isChainValid = (this.isChainValid && validate(param1PlayerChatMessage));
/* 67 */       if (!this.isChainValid) {
/* 68 */         return null;
/*    */       }
/* 70 */       this.lastMessage = param1PlayerChatMessage;
/* 71 */       return param1PlayerChatMessage;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\SignedMessageValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */