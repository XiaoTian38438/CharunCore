/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ class InteractionAtLocationAction
/*     */   implements ServerboundInteractPacket.Action
/*     */ {
/*     */   private final InteractionHand hand;
/*     */   private final Vec3 location;
/*     */   
/*     */   InteractionAtLocationAction(InteractionHand paramInteractionHand, Vec3 paramVec3) {
/* 147 */     this.hand = paramInteractionHand;
/* 148 */     this.location = paramVec3;
/*     */   }
/*     */   
/*     */   private InteractionAtLocationAction(FriendlyByteBuf paramFriendlyByteBuf) {
/* 152 */     this.location = new Vec3(paramFriendlyByteBuf.readFloat(), paramFriendlyByteBuf.readFloat(), paramFriendlyByteBuf.readFloat());
/* 153 */     this.hand = (InteractionHand)paramFriendlyByteBuf.readEnum(InteractionHand.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerboundInteractPacket.ActionType getType() {
/* 158 */     return ServerboundInteractPacket.ActionType.INTERACT_AT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(ServerboundInteractPacket.Handler paramHandler) {
/* 163 */     paramHandler.onInteraction(this.hand, this.location);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf paramFriendlyByteBuf) {
/* 168 */     paramFriendlyByteBuf.writeFloat((float)this.location.x);
/* 169 */     paramFriendlyByteBuf.writeFloat((float)this.location.y);
/* 170 */     paramFriendlyByteBuf.writeFloat((float)this.location.z);
/* 171 */     paramFriendlyByteBuf.writeEnum((Enum)this.hand);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$InteractionAtLocationAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */