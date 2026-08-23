/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.api.Schema;
/*    */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*    */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*    */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*    */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*    */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*    */ 
/*    */ 
/*    */ public class OutgoingRpcMethods
/*    */ {
/* 15 */   public static final Holder.Reference<OutgoingRpcMethod<Void, Void>> SERVER_STARTED = OutgoingRpcMethod.notification()
/* 16 */     .description("Server started")
/* 17 */     .register("server/started");
/* 18 */   public static final Holder.Reference<OutgoingRpcMethod<Void, Void>> SERVER_SHUTTING_DOWN = OutgoingRpcMethod.notification()
/* 19 */     .description("Server shutting down")
/* 20 */     .register("server/stopping");
/* 21 */   public static final Holder.Reference<OutgoingRpcMethod<Void, Void>> SERVER_SAVE_STARTED = OutgoingRpcMethod.notification()
/* 22 */     .description("Server save started")
/* 23 */     .register("server/saving");
/* 24 */   public static final Holder.Reference<OutgoingRpcMethod<Void, Void>> SERVER_SAVE_COMPLETED = OutgoingRpcMethod.notification()
/* 25 */     .description("Server save completed")
/* 26 */     .register("server/saved");
/* 27 */   public static final Holder.Reference<OutgoingRpcMethod<Void, Void>> SERVER_ACTIVITY_OCCURRED = OutgoingRpcMethod.notification()
/* 28 */     .description("Server activity occurred. Rate limited to 1 notification per 30 seconds")
/* 29 */     .register("server/activity");
/*    */   
/* 31 */   public static final Holder.Reference<OutgoingRpcMethod<PlayerDto, Void>> PLAYER_JOINED = OutgoingRpcMethod.<PlayerDto>notificationWithParams()
/* 32 */     .param("player", Schema.PLAYER_SCHEMA.asRef())
/* 33 */     .description("Player joined")
/* 34 */     .register("players/joined");
/*    */   
/* 36 */   public static final Holder.Reference<OutgoingRpcMethod<PlayerDto, Void>> PLAYER_LEFT = OutgoingRpcMethod.<PlayerDto>notificationWithParams()
/* 37 */     .param("player", Schema.PLAYER_SCHEMA.asRef())
/* 38 */     .description("Player left")
/* 39 */     .register("players/left");
/*    */   
/* 41 */   public static final Holder.Reference<OutgoingRpcMethod<OperatorService.OperatorDto, Void>> PLAYER_OPED = OutgoingRpcMethod.<OperatorService.OperatorDto>notificationWithParams()
/* 42 */     .param("player", Schema.OPERATOR_SCHEMA.asRef())
/* 43 */     .description("Player was oped")
/* 44 */     .register("operators/added");
/*    */   
/* 46 */   public static final Holder.Reference<OutgoingRpcMethod<OperatorService.OperatorDto, Void>> PLAYER_DEOPED = OutgoingRpcMethod.<OperatorService.OperatorDto>notificationWithParams()
/* 47 */     .param("player", Schema.OPERATOR_SCHEMA.asRef())
/* 48 */     .description("Player was deoped")
/* 49 */     .register("operators/removed");
/*    */   
/* 51 */   public static final Holder.Reference<OutgoingRpcMethod<PlayerDto, Void>> PLAYER_ADDED_TO_ALLOWLIST = OutgoingRpcMethod.<PlayerDto>notificationWithParams()
/* 52 */     .param("player", Schema.PLAYER_SCHEMA.asRef())
/* 53 */     .description("Player was added to allowlist")
/* 54 */     .register("allowlist/added");
/*    */   
/* 56 */   public static final Holder.Reference<OutgoingRpcMethod<PlayerDto, Void>> PLAYER_REMOVED_FROM_ALLOWLIST = OutgoingRpcMethod.<PlayerDto>notificationWithParams()
/* 57 */     .param("player", Schema.PLAYER_SCHEMA.asRef())
/* 58 */     .description("Player was removed from allowlist")
/* 59 */     .register("allowlist/removed");
/*    */   
/* 61 */   public static final Holder.Reference<OutgoingRpcMethod<IpBanlistService.IpBanDto, Void>> IP_BANNED = OutgoingRpcMethod.<IpBanlistService.IpBanDto>notificationWithParams()
/* 62 */     .param("player", Schema.IP_BAN_SCHEMA.asRef())
/* 63 */     .description("Ip was added to ip ban list")
/* 64 */     .register("ip_bans/added");
/*    */   
/* 66 */   public static final Holder.Reference<OutgoingRpcMethod<String, Void>> IP_UNBANNED = OutgoingRpcMethod.<String>notificationWithParams()
/* 67 */     .param("player", Schema.STRING_SCHEMA)
/* 68 */     .description("Ip was removed from ip ban list")
/* 69 */     .register("ip_bans/removed");
/*    */   
/* 71 */   public static final Holder.Reference<OutgoingRpcMethod<BanlistService.UserBanDto, Void>> PLAYER_BANNED = OutgoingRpcMethod.<BanlistService.UserBanDto>notificationWithParams()
/* 72 */     .param("player", Schema.PLAYER_BAN_SCHEMA.asRef())
/* 73 */     .description("Player was added to ban list")
/* 74 */     .register("bans/added");
/*    */   
/* 76 */   public static final Holder.Reference<OutgoingRpcMethod<PlayerDto, Void>> PLAYER_UNBANNED = OutgoingRpcMethod.<PlayerDto>notificationWithParams()
/* 77 */     .param("player", Schema.PLAYER_SCHEMA.asRef())
/* 78 */     .description("Player was removed from ban list")
/* 79 */     .register("bans/removed");
/*    */   
/* 81 */   public static final Holder.Reference<OutgoingRpcMethod<GameRulesService.GameRuleUpdate<?>, Void>> GAMERULE_CHANGED = OutgoingRpcMethod.<GameRulesService.GameRuleUpdate<?>>notificationWithParams()
/* 82 */     .param("gamerule", Schema.TYPED_GAME_RULE_SCHEMA.asRef())
/* 83 */     .description("Gamerule was changed")
/* 84 */     .register("gamerules/updated");
/*    */   
/* 86 */   public static final Holder.Reference<OutgoingRpcMethod<ServerStateService.ServerState, Void>> STATUS_HEARTBEAT = OutgoingRpcMethod.<ServerStateService.ServerState>notificationWithParams()
/* 87 */     .param("status", Schema.SERVER_STATE_SCHEMA.asRef())
/* 88 */     .description("Server status heartbeat")
/* 89 */     .register("server/status");
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethods.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */