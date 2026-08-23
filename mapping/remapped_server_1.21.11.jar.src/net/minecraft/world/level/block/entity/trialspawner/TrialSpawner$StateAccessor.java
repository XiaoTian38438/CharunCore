package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.world.level.Level;

public interface StateAccessor {
  void setState(Level paramLevel, TrialSpawnerState paramTrialSpawnerState);
  
  TrialSpawnerState getState();
  
  void markUpdated();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner$StateAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */