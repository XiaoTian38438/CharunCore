package net.minecraft.world.level.block.state;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.state.properties.Property;

public interface Factory<O, S> {
  S create(O paramO, Reference2ObjectArrayMap<Property<?>, Comparable<?>> paramReference2ObjectArrayMap, MapCodec<S> paramMapCodec);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\StateDefinition$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */