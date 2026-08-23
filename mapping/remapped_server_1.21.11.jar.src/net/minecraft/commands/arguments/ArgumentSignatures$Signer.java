package net.minecraft.commands.arguments;

import net.minecraft.network.chat.MessageSignature;

@FunctionalInterface
public interface Signer {
  MessageSignature sign(String paramString);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\ArgumentSignatures$Signer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */