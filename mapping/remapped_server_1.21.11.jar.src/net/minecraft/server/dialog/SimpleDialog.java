package net.minecraft.server.dialog;

import com.mojang.serialization.MapCodec;
import java.util.List;

public interface SimpleDialog extends Dialog {
  MapCodec<? extends SimpleDialog> codec();
  
  List<ActionButton> mainActions();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\SimpleDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */