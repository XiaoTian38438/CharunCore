package net.minecraft.world.level.chunk;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;

public interface LightChunkGetter {
  LightChunk getChunkForLighting(int paramInt1, int paramInt2);
  
  default void onLightUpdate(LightLayer paramLightLayer, SectionPos paramSectionPos) {}
  
  BlockGetter getLevel();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\LightChunkGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */