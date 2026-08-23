package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

interface NodeStub {
  <S> ArgumentBuilder<S, ?> build(CommandBuildContext paramCommandBuildContext, ClientboundCommandsPacket.NodeBuilder<S> paramNodeBuilder);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeStub.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */