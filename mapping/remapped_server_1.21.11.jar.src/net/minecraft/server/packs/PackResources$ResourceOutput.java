package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;

@FunctionalInterface
public interface ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\PackResources$ResourceOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */