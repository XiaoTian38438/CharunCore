package net.minecraft.world.item.slot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface SlotSource extends LootContextUser {
  MapCodec<? extends SlotSource> codec();
  
  SlotCollection provide(LootContext paramLootContext);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\SlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */