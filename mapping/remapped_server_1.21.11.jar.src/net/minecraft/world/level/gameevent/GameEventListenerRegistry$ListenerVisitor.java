package net.minecraft.world.level.gameevent;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface ListenerVisitor {
  void visit(GameEventListener paramGameEventListener, Vec3 paramVec3);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry$ListenerVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */