/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecodeException
/*     */   extends ThrowingComponent
/*     */ {
/* 108 */   static final Component MISSING_PROFILE_KEY = Component.translatable("chat.disabled.missingProfileKey");
/* 109 */   static final Component CHAIN_BROKEN = Component.translatable("chat.disabled.chain_broken");
/* 110 */   static final Component EXPIRED_PROFILE_KEY = Component.translatable("chat.disabled.expiredProfileKey");
/* 111 */   static final Component INVALID_SIGNATURE = Component.translatable("chat.disabled.invalid_signature");
/* 112 */   static final Component OUT_OF_ORDER_CHAT = Component.translatable("chat.disabled.out_of_order_chat");
/*     */   
/*     */   public DecodeException(Component paramComponent) {
/* 115 */     super(paramComponent);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\SignedMessageChain$DecodeException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */