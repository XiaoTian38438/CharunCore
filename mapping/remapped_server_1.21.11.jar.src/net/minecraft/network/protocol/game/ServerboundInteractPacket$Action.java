package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;

interface Action {
  ServerboundInteractPacket.ActionType getType();
  
  void dispatch(ServerboundInteractPacket.Handler paramHandler);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */