package net.minecraft.world.waypoints;

public interface Connection {
  void connect();
  
  void disconnect();
  
  void update();
  
  boolean isBroken();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\waypoints\WaypointTransmitter$Connection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */