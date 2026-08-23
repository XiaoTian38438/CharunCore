package net.minecraft.network.chat.contents.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;

public interface DataSource {
  Stream<CompoundTag> getData(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
  
  MapCodec<? extends DataSource> codec();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\data\DataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */