package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;

interface Inserter {
  void apply(List<Pack> paramList, Pack paramPack) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DataPackCommand$Inserter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */