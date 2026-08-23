package net.minecraft.world.waypoints;

public interface WaypointManager<T extends Waypoint> {
  void trackWaypoint(T paramT);
  
  void updateWaypoint(T paramT);
  
  void untrackWaypoint(T paramT);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */