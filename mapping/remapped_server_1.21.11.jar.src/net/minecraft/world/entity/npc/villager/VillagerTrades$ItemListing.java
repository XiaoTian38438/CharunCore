package net.minecraft.world.entity.npc.villager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;

public interface ItemListing {
  MerchantOffer getOffer(ServerLevel paramServerLevel, Entity paramEntity, RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\villager\VillagerTrades$ItemListing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */