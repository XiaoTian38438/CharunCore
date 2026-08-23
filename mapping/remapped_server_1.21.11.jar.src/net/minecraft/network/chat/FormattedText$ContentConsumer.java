package net.minecraft.network.chat;

import java.util.Optional;

public interface ContentConsumer<T> {
  Optional<T> accept(String paramString);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\FormattedText$ContentConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */