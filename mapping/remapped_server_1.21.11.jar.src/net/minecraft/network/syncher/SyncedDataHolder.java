package net.minecraft.network.syncher;

import java.util.List;

public interface SyncedDataHolder {
  void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor);
  
  void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> paramList);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\syncher\SyncedDataHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */