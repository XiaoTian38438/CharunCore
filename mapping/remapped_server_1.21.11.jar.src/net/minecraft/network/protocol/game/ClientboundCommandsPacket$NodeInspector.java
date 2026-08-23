package net.minecraft.network.protocol.game;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.resources.Identifier;

public interface NodeInspector<S> {
  Identifier suggestionId(ArgumentCommandNode<S, ?> paramArgumentCommandNode);
  
  boolean isExecutable(CommandNode<S> paramCommandNode);
  
  boolean isRestricted(CommandNode<S> paramCommandNode);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeInspector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */