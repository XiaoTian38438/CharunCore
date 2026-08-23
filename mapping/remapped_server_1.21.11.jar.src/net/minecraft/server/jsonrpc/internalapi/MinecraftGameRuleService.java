package net.minecraft.server.jsonrpc.internalapi;

import java.util.stream.Stream;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.GameRulesService;
import net.minecraft.world.level.gamerules.GameRule;

public interface MinecraftGameRuleService {
  <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> paramGameRuleUpdate, ClientInfo paramClientInfo);
  
  <T> T getRuleValue(GameRule<T> paramGameRule);
  
  <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> paramGameRule, T paramT);
  
  Stream<GameRule<?>> getAvailableGameRules();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftGameRuleService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */