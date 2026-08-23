package net.minecraft.world.level.entity;

import java.util.UUID;

public interface UUIDLookup<IdentifiedType extends UniquelyIdentifyable> {
  IdentifiedType lookup(UUID paramUUID);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\UUIDLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */