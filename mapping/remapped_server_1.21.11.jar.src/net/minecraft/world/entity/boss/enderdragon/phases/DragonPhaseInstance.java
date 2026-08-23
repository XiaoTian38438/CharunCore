package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface DragonPhaseInstance {
  boolean isSitting();
  
  void doClientTick();
  
  void doServerTick(ServerLevel paramServerLevel);
  
  void onCrystalDestroyed(EndCrystal paramEndCrystal, BlockPos paramBlockPos, DamageSource paramDamageSource, Player paramPlayer);
  
  void begin();
  
  void end();
  
  float getFlySpeed();
  
  float getTurnSpeed();
  
  EnderDragonPhase<? extends DragonPhaseInstance> getPhase();
  
  Vec3 getFlyTargetLocation();
  
  float onHurt(DamageSource paramDamageSource, float paramFloat);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonPhaseInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */