/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class MinecraftGameRuleServiceImpl
/*    */   implements MinecraftGameRuleService {
/*    */   private final DedicatedServer server;
/*    */   private final GameRules gameRules;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftGameRuleServiceImpl(DedicatedServer paramDedicatedServer, JsonRpcLogger paramJsonRpcLogger) {
/* 19 */     this.server = paramDedicatedServer;
/* 20 */     this.gameRules = paramDedicatedServer.getWorldData().getGameRules();
/* 21 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> paramGameRuleUpdate, ClientInfo paramClientInfo) {
/* 26 */     GameRule gameRule = paramGameRuleUpdate.gameRule();
/* 27 */     Object object1 = this.gameRules.get(gameRule);
/* 28 */     Object object2 = paramGameRuleUpdate.value();
/* 29 */     this.gameRules.set(gameRule, object2, (MinecraftServer)this.server);
/* 30 */     this.jsonrpcLogger.log(paramClientInfo, "Game rule '{}' updated from '{}' to '{}'", new Object[] { gameRule.id(), gameRule.serialize(object1), gameRule.serialize(object2) });
/* 31 */     return paramGameRuleUpdate;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> paramGameRule, T paramT) {
/* 36 */     return new GameRulesService.GameRuleUpdate(paramGameRule, paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<GameRule<?>> getAvailableGameRules() {
/* 41 */     return this.gameRules.availableRules();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getRuleValue(GameRule<T> paramGameRule) {
/* 46 */     return (T)this.gameRules.get(paramGameRule);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftGameRuleServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */