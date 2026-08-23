/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.AllowlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.DiscoveryService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.PlayerService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerSettingsService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ 
/*     */ public class IncomingRpcMethods
/*     */ {
/*     */   public static IncomingRpcMethod<?, ?> bootstrap(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/*  19 */     registerAllowListService(paramRegistry);
/*     */     
/*  21 */     registerBanlistService(paramRegistry);
/*     */     
/*  23 */     registerIpBanlistService(paramRegistry);
/*     */     
/*  25 */     registerPlayerService(paramRegistry);
/*     */     
/*  27 */     registerOperatorService(paramRegistry);
/*     */     
/*  29 */     registerServerStateService(paramRegistry);
/*     */     
/*  31 */     registerServerSettingsService(paramRegistry);
/*     */     
/*  33 */     registerGameRuleService(paramRegistry);
/*     */     
/*  35 */     return IncomingRpcMethod.method(paramMinecraftApi -> DiscoveryService.discover(Schema.getSchemaRegistry()))
/*  36 */       .undiscoverable()
/*  37 */       .notOnMainThread()
/*  38 */       .response("result", Schema.DISCOVERY_SCHEMA)
/*  39 */       .register(paramRegistry, "rpc.discover");
/*     */   }
/*     */   
/*     */   private static void registerAllowListService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/*  43 */     IncomingRpcMethod.method(AllowlistService::get)
/*  44 */       .description("Get the allowlist")
/*  45 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  46 */       .register(paramRegistry, "allowlist");
/*     */     
/*  48 */     IncomingRpcMethod.method(AllowlistService::set)
/*  49 */       .description("Set the allowlist")
/*  50 */       .param("players", Schema.PLAYER_SCHEMA.asArray())
/*  51 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  52 */       .register(paramRegistry, "allowlist/set");
/*     */     
/*  54 */     IncomingRpcMethod.method(AllowlistService::add)
/*  55 */       .description("Add players to allowlist")
/*  56 */       .param("add", Schema.PLAYER_SCHEMA.asArray())
/*  57 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  58 */       .register(paramRegistry, "allowlist/add");
/*     */     
/*  60 */     IncomingRpcMethod.method(AllowlistService::remove)
/*  61 */       .description("Remove players from allowlist")
/*  62 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/*  63 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  64 */       .register(paramRegistry, "allowlist/remove");
/*     */     
/*  66 */     IncomingRpcMethod.method(AllowlistService::clear)
/*  67 */       .description("Clear all players in allowlist")
/*  68 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  69 */       .register(paramRegistry, "allowlist/clear");
/*     */   }
/*     */   
/*     */   private static void registerBanlistService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/*  73 */     IncomingRpcMethod.method(BanlistService::get)
/*  74 */       .description("Get the ban list")
/*  75 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  76 */       .register(paramRegistry, "bans");
/*     */     
/*  78 */     IncomingRpcMethod.method(BanlistService::set)
/*  79 */       .description("Set the banlist")
/*  80 */       .param("bans", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  81 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  82 */       .register(paramRegistry, "bans/set");
/*     */     
/*  84 */     IncomingRpcMethod.method(BanlistService::add)
/*  85 */       .description("Add players to ban list")
/*  86 */       .param("add", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  87 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  88 */       .register(paramRegistry, "bans/add");
/*     */     
/*  90 */     IncomingRpcMethod.method(BanlistService::remove)
/*  91 */       .description("Remove players from ban list")
/*  92 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/*  93 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  94 */       .register(paramRegistry, "bans/remove");
/*     */     
/*  96 */     IncomingRpcMethod.method(BanlistService::clear)
/*  97 */       .description("Clear all players in ban list")
/*  98 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  99 */       .register(paramRegistry, "bans/clear");
/*     */   }
/*     */   
/*     */   private static void registerIpBanlistService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 103 */     IncomingRpcMethod.method(IpBanlistService::get)
/* 104 */       .description("Get the ip ban list")
/* 105 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 106 */       .register(paramRegistry, "ip_bans");
/*     */     
/* 108 */     IncomingRpcMethod.method(IpBanlistService::set)
/* 109 */       .description("Set the ip banlist")
/* 110 */       .param("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 111 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 112 */       .register(paramRegistry, "ip_bans/set");
/*     */     
/* 114 */     IncomingRpcMethod.method(IpBanlistService::add)
/* 115 */       .description("Add ip to ban list")
/* 116 */       .param("add", Schema.INCOMING_IP_BAN_SCHEMA.asArray())
/* 117 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 118 */       .register(paramRegistry, "ip_bans/add");
/*     */     
/* 120 */     IncomingRpcMethod.method(IpBanlistService::remove)
/* 121 */       .description("Remove ip from ban list")
/* 122 */       .param("ip", Schema.STRING_SCHEMA.asArray())
/* 123 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 124 */       .register(paramRegistry, "ip_bans/remove");
/*     */     
/* 126 */     IncomingRpcMethod.method(IpBanlistService::clear)
/* 127 */       .description("Clear all ips in ban list")
/* 128 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 129 */       .register(paramRegistry, "ip_bans/clear");
/*     */   }
/*     */   
/*     */   private static void registerPlayerService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 133 */     IncomingRpcMethod.method(PlayerService::get)
/* 134 */       .description("Get all connected players")
/* 135 */       .response("players", Schema.PLAYER_SCHEMA.asArray())
/* 136 */       .register(paramRegistry, "players");
/*     */     
/* 138 */     IncomingRpcMethod.method(PlayerService::kick)
/* 139 */       .description("Kick players")
/* 140 */       .param("kick", Schema.KICK_PLAYER_SCHEMA.asArray())
/* 141 */       .response("kicked", Schema.PLAYER_SCHEMA.asArray())
/* 142 */       .register(paramRegistry, "players/kick");
/*     */   }
/*     */   
/*     */   private static void registerOperatorService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 146 */     IncomingRpcMethod.method(OperatorService::get)
/* 147 */       .description("Get all oped players")
/* 148 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 149 */       .register(paramRegistry, "operators");
/*     */     
/* 151 */     IncomingRpcMethod.method(OperatorService::set)
/* 152 */       .description("Set all oped players")
/* 153 */       .param("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 154 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 155 */       .register(paramRegistry, "operators/set");
/*     */     
/* 157 */     IncomingRpcMethod.method(OperatorService::add)
/* 158 */       .description("Op players")
/* 159 */       .param("add", Schema.OPERATOR_SCHEMA.asArray())
/* 160 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 161 */       .register(paramRegistry, "operators/add");
/*     */     
/* 163 */     IncomingRpcMethod.method(OperatorService::remove)
/* 164 */       .description("Deop players")
/* 165 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/* 166 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 167 */       .register(paramRegistry, "operators/remove");
/*     */     
/* 169 */     IncomingRpcMethod.method(OperatorService::clear)
/* 170 */       .description("Deop all players")
/* 171 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 172 */       .register(paramRegistry, "operators/clear");
/*     */   }
/*     */   
/*     */   private static void registerServerStateService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 176 */     IncomingRpcMethod.method(ServerStateService::status)
/* 177 */       .description("Get server status")
/* 178 */       .response("status", Schema.SERVER_STATE_SCHEMA.asRef())
/* 179 */       .register(paramRegistry, "server/status");
/*     */     
/* 181 */     IncomingRpcMethod.method(ServerStateService::save)
/* 182 */       .description("Save server state")
/* 183 */       .param("flush", Schema.BOOL_SCHEMA)
/* 184 */       .response("saving", Schema.BOOL_SCHEMA)
/* 185 */       .register(paramRegistry, "server/save");
/*     */     
/* 187 */     IncomingRpcMethod.method(ServerStateService::stop)
/* 188 */       .description("Stop server")
/* 189 */       .response("stopping", Schema.BOOL_SCHEMA)
/* 190 */       .register(paramRegistry, "server/stop");
/*     */     
/* 192 */     IncomingRpcMethod.method(ServerStateService::systemMessage)
/* 193 */       .description("Send a system message")
/* 194 */       .param("message", Schema.SYSTEM_MESSAGE_SCHEMA.asRef())
/* 195 */       .response("sent", Schema.BOOL_SCHEMA)
/* 196 */       .register(paramRegistry, "server/system_message");
/*     */   }
/*     */   
/*     */   private static void registerServerSettingsService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 200 */     IncomingRpcMethod.method(ServerSettingsService::autosave)
/* 201 */       .description("Get whether automatic world saving is enabled on the server")
/* 202 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 203 */       .register(paramRegistry, "serversettings/autosave");
/*     */     
/* 205 */     IncomingRpcMethod.method(ServerSettingsService::setAutosave)
/* 206 */       .description("Enable or disable automatic world saving on the server")
/* 207 */       .param("enable", Schema.BOOL_SCHEMA)
/* 208 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 209 */       .register(paramRegistry, "serversettings/autosave/set");
/*     */     
/* 211 */     IncomingRpcMethod.method(ServerSettingsService::difficulty)
/* 212 */       .description("Get the current difficulty level of the server")
/* 213 */       .response("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 214 */       .register(paramRegistry, "serversettings/difficulty");
/*     */     
/* 216 */     IncomingRpcMethod.method(ServerSettingsService::setDifficulty)
/* 217 */       .description("Set the difficulty level of the server")
/* 218 */       .param("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 219 */       .response("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 220 */       .register(paramRegistry, "serversettings/difficulty/set");
/*     */     
/* 222 */     IncomingRpcMethod.method(ServerSettingsService::enforceAllowlist)
/* 223 */       .description("Get whether allowlist enforcement is enabled (kicks players immediately when removed from allowlist)")
/* 224 */       .response("enforced", Schema.BOOL_SCHEMA)
/* 225 */       .register(paramRegistry, "serversettings/enforce_allowlist");
/*     */     
/* 227 */     IncomingRpcMethod.method(ServerSettingsService::setEnforceAllowlist)
/* 228 */       .description("Enable or disable allowlist enforcement (when enabled, players are kicked immediately upon removal from allowlist)")
/* 229 */       .param("enforce", Schema.BOOL_SCHEMA)
/* 230 */       .response("enforced", Schema.BOOL_SCHEMA)
/* 231 */       .register(paramRegistry, "serversettings/enforce_allowlist/set");
/*     */     
/* 233 */     IncomingRpcMethod.method(ServerSettingsService::usingAllowlist)
/* 234 */       .description("Get whether the allowlist is enabled on the server")
/* 235 */       .response("used", Schema.BOOL_SCHEMA)
/* 236 */       .register(paramRegistry, "serversettings/use_allowlist");
/*     */     
/* 238 */     IncomingRpcMethod.method(ServerSettingsService::setUsingAllowlist)
/* 239 */       .description("Enable or disable the allowlist on the server (controls whether only allowlisted players can join)")
/* 240 */       .param("use", Schema.BOOL_SCHEMA)
/* 241 */       .response("used", Schema.BOOL_SCHEMA)
/* 242 */       .register(paramRegistry, "serversettings/use_allowlist/set");
/*     */     
/* 244 */     IncomingRpcMethod.method(ServerSettingsService::maxPlayers)
/* 245 */       .description("Get the maximum number of players allowed to connect to the server")
/* 246 */       .response("max", Schema.INT_SCHEMA)
/* 247 */       .register(paramRegistry, "serversettings/max_players");
/*     */     
/* 249 */     IncomingRpcMethod.method(ServerSettingsService::setMaxPlayers)
/* 250 */       .description("Set the maximum number of players allowed to connect to the server")
/* 251 */       .param("max", Schema.INT_SCHEMA)
/* 252 */       .response("max", Schema.INT_SCHEMA)
/* 253 */       .register(paramRegistry, "serversettings/max_players/set");
/*     */     
/* 255 */     IncomingRpcMethod.method(ServerSettingsService::pauseWhenEmpty)
/* 256 */       .description("Get the number of seconds before the game is automatically paused when no players are online")
/* 257 */       .response("seconds", Schema.INT_SCHEMA)
/* 258 */       .register(paramRegistry, "serversettings/pause_when_empty_seconds");
/*     */     
/* 260 */     IncomingRpcMethod.method(ServerSettingsService::setPauseWhenEmpty)
/* 261 */       .description("Set the number of seconds before the game is automatically paused when no players are online")
/* 262 */       .param("seconds", Schema.INT_SCHEMA)
/* 263 */       .response("seconds", Schema.INT_SCHEMA)
/* 264 */       .register(paramRegistry, "serversettings/pause_when_empty_seconds/set");
/*     */     
/* 266 */     IncomingRpcMethod.method(ServerSettingsService::playerIdleTimeout)
/* 267 */       .description("Get the number of seconds before idle players are automatically kicked from the server")
/* 268 */       .response("seconds", Schema.INT_SCHEMA)
/* 269 */       .register(paramRegistry, "serversettings/player_idle_timeout");
/*     */     
/* 271 */     IncomingRpcMethod.method(ServerSettingsService::setPlayerIdleTimeout)
/* 272 */       .description("Set the number of seconds before idle players are automatically kicked from the server")
/* 273 */       .param("seconds", Schema.INT_SCHEMA)
/* 274 */       .response("seconds", Schema.INT_SCHEMA)
/* 275 */       .register(paramRegistry, "serversettings/player_idle_timeout/set");
/*     */     
/* 277 */     IncomingRpcMethod.method(ServerSettingsService::allowFlight)
/* 278 */       .description("Get whether flight is allowed for players in Survival mode")
/* 279 */       .response("allowed", Schema.BOOL_SCHEMA)
/* 280 */       .register(paramRegistry, "serversettings/allow_flight");
/*     */     
/* 282 */     IncomingRpcMethod.method(ServerSettingsService::setAllowFlight)
/* 283 */       .description("Allow or disallow flight for players in Survival mode")
/* 284 */       .param("allow", Schema.BOOL_SCHEMA)
/* 285 */       .response("allowed", Schema.BOOL_SCHEMA)
/* 286 */       .register(paramRegistry, "serversettings/allow_flight/set");
/*     */     
/* 288 */     IncomingRpcMethod.method(ServerSettingsService::motd)
/* 289 */       .description("Get the server's message of the day displayed to players")
/* 290 */       .response("message", Schema.STRING_SCHEMA)
/* 291 */       .register(paramRegistry, "serversettings/motd");
/*     */     
/* 293 */     IncomingRpcMethod.method(ServerSettingsService::setMotd)
/* 294 */       .description("Set the server's message of the day displayed to players")
/* 295 */       .param("message", Schema.STRING_SCHEMA)
/* 296 */       .response("message", Schema.STRING_SCHEMA)
/* 297 */       .register(paramRegistry, "serversettings/motd/set");
/*     */     
/* 299 */     IncomingRpcMethod.method(ServerSettingsService::spawnProtection)
/* 300 */       .description("Get the spawn protection radius in blocks (only operators can edit within this area)")
/* 301 */       .response("radius", Schema.INT_SCHEMA)
/* 302 */       .register(paramRegistry, "serversettings/spawn_protection_radius");
/*     */     
/* 304 */     IncomingRpcMethod.method(ServerSettingsService::setSpawnProtection)
/* 305 */       .description("Set the spawn protection radius in blocks (only operators can edit within this area)")
/* 306 */       .param("radius", Schema.INT_SCHEMA)
/* 307 */       .response("radius", Schema.INT_SCHEMA)
/* 308 */       .register(paramRegistry, "serversettings/spawn_protection_radius/set");
/*     */     
/* 310 */     IncomingRpcMethod.method(ServerSettingsService::forceGameMode)
/* 311 */       .description("Get whether players are forced to use the server's default game mode")
/* 312 */       .response("forced", Schema.BOOL_SCHEMA)
/* 313 */       .register(paramRegistry, "serversettings/force_game_mode");
/*     */     
/* 315 */     IncomingRpcMethod.method(ServerSettingsService::setForceGameMode)
/* 316 */       .description("Enable or disable forcing players to use the server's default game mode")
/* 317 */       .param("force", Schema.BOOL_SCHEMA)
/* 318 */       .response("forced", Schema.BOOL_SCHEMA)
/* 319 */       .register(paramRegistry, "serversettings/force_game_mode/set");
/*     */     
/* 321 */     IncomingRpcMethod.method(ServerSettingsService::gameMode)
/* 322 */       .description("Get the server's default game mode")
/* 323 */       .response("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 324 */       .register(paramRegistry, "serversettings/game_mode");
/*     */     
/* 326 */     IncomingRpcMethod.method(ServerSettingsService::setGameMode)
/* 327 */       .description("Set the server's default game mode")
/* 328 */       .param("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 329 */       .response("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 330 */       .register(paramRegistry, "serversettings/game_mode/set");
/*     */     
/* 332 */     IncomingRpcMethod.method(ServerSettingsService::viewDistance)
/* 333 */       .description("Get the server's view distance in chunks")
/* 334 */       .response("distance", Schema.INT_SCHEMA)
/* 335 */       .register(paramRegistry, "serversettings/view_distance");
/*     */     
/* 337 */     IncomingRpcMethod.method(ServerSettingsService::setViewDistance)
/* 338 */       .description("Set the server's view distance in chunks")
/* 339 */       .param("distance", Schema.INT_SCHEMA)
/* 340 */       .response("distance", Schema.INT_SCHEMA)
/* 341 */       .register(paramRegistry, "serversettings/view_distance/set");
/*     */     
/* 343 */     IncomingRpcMethod.method(ServerSettingsService::simulationDistance)
/* 344 */       .description("Get the server's simulation distance in chunks")
/* 345 */       .response("distance", Schema.INT_SCHEMA)
/* 346 */       .register(paramRegistry, "serversettings/simulation_distance");
/*     */     
/* 348 */     IncomingRpcMethod.method(ServerSettingsService::setSimulationDistance)
/* 349 */       .description("Set the server's simulation distance in chunks")
/* 350 */       .param("distance", Schema.INT_SCHEMA)
/* 351 */       .response("distance", Schema.INT_SCHEMA)
/* 352 */       .register(paramRegistry, "serversettings/simulation_distance/set");
/*     */     
/* 354 */     IncomingRpcMethod.method(ServerSettingsService::acceptTransfers)
/* 355 */       .description("Get whether the server accepts player transfers from other servers")
/* 356 */       .response("accepted", Schema.BOOL_SCHEMA)
/* 357 */       .register(paramRegistry, "serversettings/accept_transfers");
/*     */     
/* 359 */     IncomingRpcMethod.method(ServerSettingsService::setAcceptTransfers)
/* 360 */       .description("Enable or disable accepting player transfers from other servers")
/* 361 */       .param("accept", Schema.BOOL_SCHEMA)
/* 362 */       .response("accepted", Schema.BOOL_SCHEMA)
/* 363 */       .register(paramRegistry, "serversettings/accept_transfers/set");
/*     */     
/* 365 */     IncomingRpcMethod.method(ServerSettingsService::statusHeartbeatInterval)
/* 366 */       .description("Get the interval in seconds between server status heartbeats")
/* 367 */       .response("seconds", Schema.INT_SCHEMA)
/* 368 */       .register(paramRegistry, "serversettings/status_heartbeat_interval");
/*     */     
/* 370 */     IncomingRpcMethod.method(ServerSettingsService::setStatusHeartbeatInterval)
/* 371 */       .description("Set the interval in seconds between server status heartbeats")
/* 372 */       .param("seconds", Schema.INT_SCHEMA)
/* 373 */       .response("seconds", Schema.INT_SCHEMA)
/* 374 */       .register(paramRegistry, "serversettings/status_heartbeat_interval/set");
/*     */     
/* 376 */     IncomingRpcMethod.method(ServerSettingsService::operatorUserPermissionLevel)
/* 377 */       .description("Get default operator permission level")
/* 378 */       .response("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 379 */       .register(paramRegistry, "serversettings/operator_user_permission_level");
/*     */     
/* 381 */     IncomingRpcMethod.method(ServerSettingsService::setOperatorUserPermissionLevel)
/* 382 */       .description("Set default operator permission level")
/* 383 */       .param("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 384 */       .response("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 385 */       .register(paramRegistry, "serversettings/operator_user_permission_level/set");
/*     */     
/* 387 */     IncomingRpcMethod.method(ServerSettingsService::hidesOnlinePlayers)
/* 388 */       .description("Get whether the server hides online player information from status queries")
/* 389 */       .response("hidden", Schema.BOOL_SCHEMA)
/* 390 */       .register(paramRegistry, "serversettings/hide_online_players");
/*     */     
/* 392 */     IncomingRpcMethod.method(ServerSettingsService::setHidesOnlinePlayers)
/* 393 */       .description("Enable or disable hiding online player information from status queries")
/* 394 */       .param("hide", Schema.BOOL_SCHEMA)
/* 395 */       .response("hidden", Schema.BOOL_SCHEMA)
/* 396 */       .register(paramRegistry, "serversettings/hide_online_players/set");
/*     */     
/* 398 */     IncomingRpcMethod.method(ServerSettingsService::repliesToStatus)
/* 399 */       .description("Get whether the server responds to connection status requests")
/* 400 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 401 */       .register(paramRegistry, "serversettings/status_replies");
/*     */     
/* 403 */     IncomingRpcMethod.method(ServerSettingsService::setRepliesToStatus)
/* 404 */       .description("Enable or disable the server responding to connection status requests")
/* 405 */       .param("enable", Schema.BOOL_SCHEMA)
/* 406 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 407 */       .register(paramRegistry, "serversettings/status_replies/set");
/*     */     
/* 409 */     IncomingRpcMethod.method(ServerSettingsService::entityBroadcastRangePercentage)
/* 410 */       .description("Get the entity broadcast range as a percentage")
/* 411 */       .response("percentage_points", Schema.INT_SCHEMA)
/* 412 */       .register(paramRegistry, "serversettings/entity_broadcast_range");
/*     */     
/* 414 */     IncomingRpcMethod.method(ServerSettingsService::setEntityBroadcastRangePercentage)
/* 415 */       .description("Set the entity broadcast range as a percentage")
/* 416 */       .param("percentage_points", Schema.INT_SCHEMA)
/* 417 */       .response("percentage_points", Schema.INT_SCHEMA)
/* 418 */       .register(paramRegistry, "serversettings/entity_broadcast_range/set");
/*     */   }
/*     */   
/*     */   private static void registerGameRuleService(Registry<IncomingRpcMethod<?, ?>> paramRegistry) {
/* 422 */     IncomingRpcMethod.method(GameRulesService::get)
/* 423 */       .description("Get the available game rule keys and their current values")
/* 424 */       .response("gamerules", Schema.TYPED_GAME_RULE_SCHEMA.asRef().asArray())
/* 425 */       .register(paramRegistry, "gamerules");
/*     */     
/* 427 */     IncomingRpcMethod.method(GameRulesService::update)
/* 428 */       .description("Update game rule value")
/* 429 */       .param("gamerule", Schema.UNTYPED_GAME_RULE_SCHEMA.asRef())
/* 430 */       .response("gamerule", Schema.TYPED_GAME_RULE_SCHEMA.asRef())
/* 431 */       .register(paramRegistry, "gamerules/update");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethods.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */