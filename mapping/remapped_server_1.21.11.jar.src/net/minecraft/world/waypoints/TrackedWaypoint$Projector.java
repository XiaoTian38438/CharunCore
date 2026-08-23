package net.minecraft.world.waypoints;

import net.minecraft.world.phys.Vec3;

public interface Projector {
  Vec3 projectPointToScreen(Vec3 paramVec3);
  
  double projectHorizonToScreen();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Projector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */