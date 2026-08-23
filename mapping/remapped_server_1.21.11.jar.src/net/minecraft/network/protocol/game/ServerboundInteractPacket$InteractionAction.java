/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.InteractionHand;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InteractionAction
/*     */   implements ServerboundInteractPacket.Action
/*     */ {
/*     */   private final InteractionHand hand;
/*     */   
/*     */   InteractionAction(InteractionHand paramInteractionHand) {
/* 119 */     this.hand = paramInteractionHand;
/*     */   }
/*     */   
/*     */   private InteractionAction(FriendlyByteBuf paramFriendlyByteBuf) {
/* 123 */     this.hand = (InteractionHand)paramFriendlyByteBuf.readEnum(InteractionHand.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerboundInteractPacket.ActionType getType() {
/* 128 */     return ServerboundInteractPacket.ActionType.INTERACT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(ServerboundInteractPacket.Handler paramHandler) {
/* 133 */     paramHandler.onInteraction(this.hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf paramFriendlyByteBuf) {
/* 138 */     paramFriendlyByteBuf.writeEnum((Enum)this.hand);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$InteractionAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */