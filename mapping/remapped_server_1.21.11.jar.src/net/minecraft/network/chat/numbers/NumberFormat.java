package net.minecraft.network.chat.numbers;

import net.minecraft.network.chat.MutableComponent;

public interface NumberFormat {
  MutableComponent format(int paramInt);
  
  NumberFormatType<? extends NumberFormat> type();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\NumberFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */