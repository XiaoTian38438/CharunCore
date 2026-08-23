/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ 
/*     */ public class GameRules
/*     */ {
/*     */   public static Codec<GameRules> codec(FeatureFlagSet paramFeatureFlagSet) {
/*  22 */     return GameRuleMap.CODEC.xmap(paramGameRuleMap -> new GameRules(paramFeatureFlagSet, paramGameRuleMap), paramGameRules -> paramGameRules.rules);
/*     */   }
/*     */   
/*  25 */   public static final GameRule<Boolean> ADVANCE_TIME = registerBoolean("advance_time", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*  26 */   public static final GameRule<Boolean> ADVANCE_WEATHER = registerBoolean("advance_weather", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*  27 */   public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = registerBoolean("allow_entering_nether_using_portals", GameRuleCategory.MISC, true);
/*  28 */   public static final GameRule<Boolean> BLOCK_DROPS = registerBoolean("block_drops", GameRuleCategory.DROPS, true);
/*  29 */   public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = registerBoolean("block_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*  30 */   public static final GameRule<Boolean> COMMAND_BLOCKS_WORK = registerBoolean("command_blocks_work", GameRuleCategory.MISC, true);
/*  31 */   public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = registerBoolean("command_block_output", GameRuleCategory.CHAT, true);
/*  32 */   public static final GameRule<Boolean> DROWNING_DAMAGE = registerBoolean("drowning_damage", GameRuleCategory.PLAYER, true);
/*  33 */   public static final GameRule<Boolean> ELYTRA_MOVEMENT_CHECK = registerBoolean("elytra_movement_check", GameRuleCategory.PLAYER, true);
/*  34 */   public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = registerBoolean("ender_pearls_vanish_on_death", GameRuleCategory.PLAYER, true);
/*  35 */   public static final GameRule<Boolean> ENTITY_DROPS = registerBoolean("entity_drops", GameRuleCategory.DROPS, true);
/*  36 */   public static final GameRule<Boolean> FALL_DAMAGE = registerBoolean("fall_damage", GameRuleCategory.PLAYER, true);
/*  37 */   public static final GameRule<Boolean> FIRE_DAMAGE = registerBoolean("fire_damage", GameRuleCategory.PLAYER, true);
/*  38 */   public static final GameRule<Integer> FIRE_SPREAD_RADIUS_AROUND_PLAYER = registerInteger("fire_spread_radius_around_player", GameRuleCategory.UPDATES, 128, -1);
/*  39 */   public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = registerBoolean("forgive_dead_players", GameRuleCategory.MOBS, true);
/*  40 */   public static final GameRule<Boolean> FREEZE_DAMAGE = registerBoolean("freeze_damage", GameRuleCategory.PLAYER, true);
/*  41 */   public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = registerBoolean("global_sound_events", GameRuleCategory.MISC, true);
/*  42 */   public static final GameRule<Boolean> IMMEDIATE_RESPAWN = registerBoolean("immediate_respawn", GameRuleCategory.PLAYER, false);
/*  43 */   public static final GameRule<Boolean> KEEP_INVENTORY = registerBoolean("keep_inventory", GameRuleCategory.PLAYER, false);
/*  44 */   public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = registerBoolean("lava_source_conversion", GameRuleCategory.UPDATES, false);
/*  45 */   public static final GameRule<Boolean> LIMITED_CRAFTING = registerBoolean("limited_crafting", GameRuleCategory.PLAYER, false);
/*  46 */   public static final GameRule<Boolean> LOCATOR_BAR = registerBoolean("locator_bar", GameRuleCategory.PLAYER, true);
/*  47 */   public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = registerBoolean("log_admin_commands", GameRuleCategory.CHAT, true);
/*  48 */   public static final GameRule<Integer> MAX_BLOCK_MODIFICATIONS = registerInteger("max_block_modifications", GameRuleCategory.MISC, 32768, 1);
/*  49 */   public static final GameRule<Integer> MAX_COMMAND_FORKS = registerInteger("max_command_forks", GameRuleCategory.MISC, 65536, 0);
/*  50 */   public static final GameRule<Integer> MAX_COMMAND_SEQUENCE_LENGTH = registerInteger("max_command_sequence_length", GameRuleCategory.MISC, 65536, 0);
/*  51 */   public static final GameRule<Integer> MAX_ENTITY_CRAMMING = registerInteger("max_entity_cramming", GameRuleCategory.MOBS, 24, 0);
/*  52 */   public static final GameRule<Integer> MAX_MINECART_SPEED = registerInteger("max_minecart_speed", GameRuleCategory.MISC, 8, 1, 1000, FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS));
/*  53 */   public static final GameRule<Integer> MAX_SNOW_ACCUMULATION_HEIGHT = registerInteger("max_snow_accumulation_height", GameRuleCategory.UPDATES, 1, 0, 8);
/*  54 */   public static final GameRule<Boolean> MOB_DROPS = registerBoolean("mob_drops", GameRuleCategory.DROPS, true);
/*  55 */   public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = registerBoolean("mob_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*  56 */   public static final GameRule<Boolean> MOB_GRIEFING = registerBoolean("mob_griefing", GameRuleCategory.MOBS, true);
/*  57 */   public static final GameRule<Boolean> NATURAL_HEALTH_REGENERATION = registerBoolean("natural_health_regeneration", GameRuleCategory.PLAYER, true);
/*  58 */   public static final GameRule<Boolean> PLAYER_MOVEMENT_CHECK = registerBoolean("player_movement_check", GameRuleCategory.PLAYER, true);
/*  59 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = registerInteger("players_nether_portal_creative_delay", GameRuleCategory.PLAYER, 0, 0);
/*  60 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = registerInteger("players_nether_portal_default_delay", GameRuleCategory.PLAYER, 80, 0);
/*  61 */   public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = registerInteger("players_sleeping_percentage", GameRuleCategory.PLAYER, 100, 0);
/*  62 */   public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = registerBoolean("projectiles_can_break_blocks", GameRuleCategory.DROPS, true);
/*  63 */   public static final GameRule<Boolean> PVP = registerBoolean("pvp", GameRuleCategory.PLAYER, true);
/*  64 */   public static final GameRule<Boolean> RAIDS = registerBoolean("raids", GameRuleCategory.MOBS, true);
/*  65 */   public static final GameRule<Integer> RANDOM_TICK_SPEED = registerInteger("random_tick_speed", GameRuleCategory.UPDATES, 3, 0);
/*  66 */   public static final GameRule<Boolean> REDUCED_DEBUG_INFO = registerBoolean("reduced_debug_info", GameRuleCategory.MISC, false);
/*  67 */   public static final GameRule<Integer> RESPAWN_RADIUS = registerInteger("respawn_radius", GameRuleCategory.PLAYER, 10, 0);
/*  68 */   public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = registerBoolean("send_command_feedback", GameRuleCategory.CHAT, true);
/*  69 */   public static final GameRule<Boolean> SHOW_ADVANCEMENT_MESSAGES = registerBoolean("show_advancement_messages", GameRuleCategory.CHAT, true);
/*  70 */   public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = registerBoolean("show_death_messages", GameRuleCategory.CHAT, true);
/*  71 */   public static final GameRule<Boolean> SPAWNER_BLOCKS_WORK = registerBoolean("spawner_blocks_work", GameRuleCategory.MISC, true);
/*  72 */   public static final GameRule<Boolean> SPAWN_MOBS = registerBoolean("spawn_mobs", GameRuleCategory.SPAWNING, true);
/*  73 */   public static final GameRule<Boolean> SPAWN_MONSTERS = registerBoolean("spawn_monsters", GameRuleCategory.SPAWNING, true);
/*  74 */   public static final GameRule<Boolean> SPAWN_PATROLS = registerBoolean("spawn_patrols", GameRuleCategory.SPAWNING, true);
/*  75 */   public static final GameRule<Boolean> SPAWN_PHANTOMS = registerBoolean("spawn_phantoms", GameRuleCategory.SPAWNING, true);
/*  76 */   public static final GameRule<Boolean> SPAWN_WANDERING_TRADERS = registerBoolean("spawn_wandering_traders", GameRuleCategory.SPAWNING, true);
/*  77 */   public static final GameRule<Boolean> SPAWN_WARDENS = registerBoolean("spawn_wardens", GameRuleCategory.SPAWNING, true);
/*  78 */   public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = registerBoolean("spectators_generate_chunks", GameRuleCategory.PLAYER, true);
/*  79 */   public static final GameRule<Boolean> SPREAD_VINES = registerBoolean("spread_vines", GameRuleCategory.UPDATES, true);
/*  80 */   public static final GameRule<Boolean> TNT_EXPLODES = registerBoolean("tnt_explodes", GameRuleCategory.MISC, true);
/*  81 */   public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = registerBoolean("tnt_explosion_drop_decay", GameRuleCategory.DROPS, false);
/*  82 */   public static final GameRule<Boolean> UNIVERSAL_ANGER = registerBoolean("universal_anger", GameRuleCategory.MOBS, false);
/*  83 */   public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = registerBoolean("water_source_conversion", GameRuleCategory.UPDATES, true);
/*     */   
/*     */   private final GameRuleMap rules;
/*     */   
/*     */   public GameRules(FeatureFlagSet paramFeatureFlagSet, GameRuleMap paramGameRuleMap) {
/*  88 */     this(paramFeatureFlagSet);
/*  89 */     Objects.requireNonNull(this.rules); this.rules.setFromIf(paramGameRuleMap, this.rules::has);
/*     */   }
/*     */   
/*     */   public GameRules(FeatureFlagSet paramFeatureFlagSet) {
/*  93 */     this.rules = GameRuleMap.of(BuiltInRegistries.GAME_RULE.filterFeatures(paramFeatureFlagSet).listElements().map(Holder::value));
/*     */   }
/*     */   
/*     */   public Stream<GameRule<?>> availableRules() {
/*  97 */     return this.rules.keySet().stream();
/*     */   }
/*     */   
/*     */   public <T> T get(GameRule<T> paramGameRule) {
/* 101 */     T t = (T)this.rules.get((GameRule)paramGameRule);
/* 102 */     if (t == null) {
/* 103 */       throw new IllegalArgumentException("Tried to access invalid game rule");
/*     */     }
/* 105 */     return t;
/*     */   }
/*     */   
/*     */   public <T> void set(GameRule<T> paramGameRule, T paramT, MinecraftServer paramMinecraftServer) {
/* 109 */     if (!this.rules.has(paramGameRule)) {
/* 110 */       throw new IllegalArgumentException("Tried to set invalid game rule");
/*     */     }
/* 112 */     this.rules.set(paramGameRule, paramT);
/* 113 */     if (paramMinecraftServer != null) {
/* 114 */       paramMinecraftServer.onGameRuleChanged(paramGameRule, paramT);
/*     */     }
/*     */   }
/*     */   
/*     */   public GameRules copy(FeatureFlagSet paramFeatureFlagSet) {
/* 119 */     return new GameRules(paramFeatureFlagSet, this.rules);
/*     */   }
/*     */   
/*     */   public void setAll(GameRules paramGameRules, MinecraftServer paramMinecraftServer) {
/* 123 */     setAll(paramGameRules.rules, paramMinecraftServer);
/*     */   }
/*     */   
/*     */   public void setAll(GameRuleMap paramGameRuleMap, MinecraftServer paramMinecraftServer) {
/* 127 */     paramGameRuleMap.keySet().forEach(paramGameRule -> setFromOther(paramGameRuleMap, paramGameRule, paramMinecraftServer));
/*     */   }
/*     */   
/*     */   private <T> void setFromOther(GameRuleMap paramGameRuleMap, GameRule<T> paramGameRule, MinecraftServer paramMinecraftServer) {
/* 131 */     set(paramGameRule, Objects.requireNonNull(paramGameRuleMap.get(paramGameRule)), paramMinecraftServer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitGameRuleTypes(GameRuleTypeVisitor paramGameRuleTypeVisitor) {
/* 139 */     this.rules.keySet().forEach(paramGameRule -> {
/*     */           paramGameRuleTypeVisitor.visit(paramGameRule);
/*     */           paramGameRule.callVisitor(paramGameRuleTypeVisitor);
/*     */         });
/*     */   }
/*     */   
/*     */   private static GameRule<Boolean> registerBoolean(String paramString, GameRuleCategory paramGameRuleCategory, boolean paramBoolean) {
/* 146 */     return register(paramString, paramGameRuleCategory, GameRuleType.BOOL, 
/*     */ 
/*     */ 
/*     */         
/* 150 */         (ArgumentType<Boolean>)BoolArgumentType.bool(), (Codec<Boolean>)Codec.BOOL, 
/*     */         
/* 152 */         Boolean.valueOf(paramBoolean), 
/* 153 */         FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, paramBoolean -> paramBoolean.booleanValue() ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String paramString, GameRuleCategory paramGameRuleCategory, int paramInt1, int paramInt2) {
/* 160 */     return registerInteger(paramString, paramGameRuleCategory, paramInt1, paramInt2, 2147483647, FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String paramString, GameRuleCategory paramGameRuleCategory, int paramInt1, int paramInt2, int paramInt3) {
/* 164 */     return registerInteger(paramString, paramGameRuleCategory, paramInt1, paramInt2, paramInt3, FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String paramString, GameRuleCategory paramGameRuleCategory, int paramInt1, int paramInt2, int paramInt3, FeatureFlagSet paramFeatureFlagSet) {
/* 168 */     return register(paramString, paramGameRuleCategory, GameRuleType.INT, 
/*     */ 
/*     */ 
/*     */         
/* 172 */         (ArgumentType<Integer>)IntegerArgumentType.integer(paramInt2, paramInt3), 
/* 173 */         Codec.intRange(paramInt2, paramInt3), 
/* 174 */         Integer.valueOf(paramInt1), paramFeatureFlagSet, GameRuleTypeVisitor::visitInteger, paramInteger -> paramInteger.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> GameRule<T> register(String paramString, GameRuleCategory paramGameRuleCategory, GameRuleType paramGameRuleType, ArgumentType<T> paramArgumentType, Codec<T> paramCodec, T paramT, FeatureFlagSet paramFeatureFlagSet, VisitorCaller<T> paramVisitorCaller, ToIntFunction<T> paramToIntFunction) {
/* 192 */     return (GameRule<T>)Registry.register(BuiltInRegistries.GAME_RULE, paramString, new GameRule<>(paramGameRuleCategory, paramGameRuleType, paramArgumentType, paramVisitorCaller, paramCodec, paramToIntFunction, paramT, paramFeatureFlagSet));
/*     */   }
/*     */   
/*     */   public static GameRule<?> bootstrap(Registry<GameRule<?>> paramRegistry) {
/* 196 */     return ADVANCE_TIME;
/*     */   }
/*     */   
/*     */   public <T> String getAsString(GameRule<T> paramGameRule) {
/* 200 */     return paramGameRule.serialize(get(paramGameRule));
/*     */   }
/*     */   
/*     */   public static interface VisitorCaller<T> {
/*     */     void call(GameRuleTypeVisitor param1GameRuleTypeVisitor, GameRule<T> param1GameRule);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gamerules\GameRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */