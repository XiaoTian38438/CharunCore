package net.minecraft.world.level.storage.loot.providers.score;

import java.util.Set;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.scores.ScoreHolder;

public interface ScoreboardNameProvider {
  ScoreHolder getScoreHolder(LootContext paramLootContext);
  
  LootScoreProviderType getType();
  
  Set<ContextKey<?>> getReferencedContextParams();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\providers\score\ScoreboardNameProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */