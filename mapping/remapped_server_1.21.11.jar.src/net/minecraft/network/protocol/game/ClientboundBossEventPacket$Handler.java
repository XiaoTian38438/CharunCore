package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;

public interface Handler {
  default void add(UUID paramUUID, Component paramComponent, float paramFloat, BossEvent.BossBarColor paramBossBarColor, BossEvent.BossBarOverlay paramBossBarOverlay, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {}
  
  default void remove(UUID paramUUID) {}
  
  default void updateProgress(UUID paramUUID, float paramFloat) {}
  
  default void updateName(UUID paramUUID, Component paramComponent) {}
  
  default void updateStyle(UUID paramUUID, BossEvent.BossBarColor paramBossBarColor, BossEvent.BossBarOverlay paramBossBarOverlay) {}
  
  default void updateProperties(UUID paramUUID, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Handler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */