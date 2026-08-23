/*     */ package net.minecraft;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import java.time.Duration;
/*     */ import net.minecraft.commands.BrigadierExceptions;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SuppressForbidden(a = "System.out needed before bootstrap")
/*     */ public class SharedConstants
/*     */ {
/*     */   @Deprecated
/*     */   public static final boolean SNAPSHOT = false;
/*     */   @Deprecated
/*     */   public static final int WORLD_VERSION = 4671;
/*     */   @Deprecated
/*     */   public static final String SERIES = "main";
/*     */   @Deprecated
/*     */   public static final int RELEASE_NETWORK_PROTOCOL_VERSION = 774;
/*     */   @Deprecated
/*     */   public static final int SNAPSHOT_NETWORK_PROTOCOL_VERSION = 286;
/*     */   public static final int SNBT_NAG_VERSION = 4650;
/*     */   private static final int SNAPSHOT_PROTOCOL_BIT = 30;
/*     */   public static final boolean CRASH_EAGERLY = false;
/*     */   @Deprecated
/*     */   public static final int RESOURCE_PACK_FORMAT_MAJOR = 75;
/*     */   @Deprecated
/*     */   public static final int RESOURCE_PACK_FORMAT_MINOR = 0;
/*     */   @Deprecated
/*     */   public static final int DATA_PACK_FORMAT_MAJOR = 94;
/*     */   @Deprecated
/*     */   public static final int DATA_PACK_FORMAT_MINOR = 1;
/*     */   public static final String RPC_MANAGEMENT_SERVER_API_VERSION = "2.0.0";
/*     */   @Deprecated
/*     */   public static final int LANGUAGE_FORMAT = 1;
/*     */   public static final int REPORT_FORMAT_VERSION = 1;
/*     */   public static final String DATA_VERSION_TAG = "DataVersion";
/*     */   public static final String DEBUG_FLAG_PREFIX = "MC_DEBUG_";
/*     */   
/*     */   private static String prefixDebugFlagName(String paramString) {
/*  76 */     return "MC_DEBUG_" + paramString;
/*     */   }
/*     */   
/*     */   private static boolean booleanProperty(String paramString) {
/*  80 */     String str = System.getProperty(paramString);
/*     */     
/*  82 */     return (str != null && (str.isEmpty() || Boolean.parseBoolean(str)));
/*     */   }
/*     */   
/*  85 */   public static final boolean DEBUG_ENABLED = booleanProperty(prefixDebugFlagName("ENABLED"));
/*  86 */   private static final boolean DEBUG_PRINT_PROPERTIES = booleanProperty(prefixDebugFlagName("PRINT_PROPERTIES"));
/*     */   public static final boolean FIX_TNT_DUPE = false;
/*     */   
/*     */   private static boolean debugFlag(String paramString) {
/*  90 */     if (!DEBUG_ENABLED) {
/*  91 */       return false;
/*     */     }
/*  93 */     String str = prefixDebugFlagName(paramString);
/*  94 */     if (DEBUG_PRINT_PROPERTIES) {
/*  95 */       System.out.println("Debug property available: " + str + ": bool");
/*     */     }
/*  97 */     return booleanProperty(str);
/*     */   }
/*     */   public static final boolean FIX_SAND_DUPE = false;
/*     */   
/*     */   private static int debugIntValue(String paramString) {
/* 102 */     if (!DEBUG_ENABLED) {
/* 103 */       return 0;
/*     */     }
/* 105 */     String str = prefixDebugFlagName(paramString);
/* 106 */     if (DEBUG_PRINT_PROPERTIES) {
/* 107 */       System.out.println("Debug property available: " + str + ": int");
/*     */     }
/* 109 */     return Integer.parseInt(System.getProperty(str, "0"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static final boolean DEBUG_OPEN_INCOMPATIBLE_WORLDS = debugFlag("OPEN_INCOMPATIBLE_WORLDS");
/* 115 */   public static final boolean DEBUG_ALLOW_LOW_SIM_DISTANCE = debugFlag("ALLOW_LOW_SIM_DISTANCE");
/* 116 */   public static final boolean DEBUG_HOTKEYS = debugFlag("HOTKEYS");
/* 117 */   public static final boolean DEBUG_UI_NARRATION = debugFlag("UI_NARRATION");
/* 118 */   public static final boolean DEBUG_SHUFFLE_UI_RENDERING_ORDER = debugFlag("SHUFFLE_UI_RENDERING_ORDER");
/* 119 */   public static final boolean DEBUG_SHUFFLE_MODELS = debugFlag("SHUFFLE_MODELS");
/* 120 */   public static final boolean DEBUG_RENDER_UI_LAYERING_RECTANGLES = debugFlag("RENDER_UI_LAYERING_RECTANGLES");
/* 121 */   public static final boolean DEBUG_PATHFINDING = debugFlag("PATHFINDING");
/* 122 */   public static final boolean DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES = debugFlag("SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES");
/* 123 */   public static final boolean DEBUG_SHAPES = debugFlag("SHAPES");
/* 124 */   public static final boolean DEBUG_NEIGHBORSUPDATE = debugFlag("NEIGHBORSUPDATE");
/* 125 */   public static final boolean DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER = debugFlag("EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER");
/* 126 */   public static final boolean DEBUG_STRUCTURES = debugFlag("STRUCTURES");
/* 127 */   public static final boolean DEBUG_GAME_EVENT_LISTENERS = debugFlag("GAME_EVENT_LISTENERS");
/* 128 */   public static final boolean DEBUG_DUMP_TEXTURE_ATLAS = debugFlag("DUMP_TEXTURE_ATLAS");
/* 129 */   public static final boolean DEBUG_DUMP_INTERPOLATED_TEXTURE_FRAMES = debugFlag("DUMP_INTERPOLATED_TEXTURE_FRAMES");
/* 130 */   public static final boolean DEBUG_STRUCTURE_EDIT_MODE = debugFlag("STRUCTURE_EDIT_MODE");
/* 131 */   public static final boolean DEBUG_SAVE_STRUCTURES_AS_SNBT = debugFlag("SAVE_STRUCTURES_AS_SNBT");
/* 132 */   public static final boolean DEBUG_SYNCHRONOUS_GL_LOGS = debugFlag("SYNCHRONOUS_GL_LOGS");
/* 133 */   public static final boolean DEBUG_VERBOSE_SERVER_EVENTS = debugFlag("VERBOSE_SERVER_EVENTS");
/* 134 */   public static final boolean DEBUG_NAMED_RUNNABLES = debugFlag("NAMED_RUNNABLES");
/* 135 */   public static final boolean DEBUG_GOAL_SELECTOR = debugFlag("GOAL_SELECTOR");
/* 136 */   public static final boolean DEBUG_VILLAGE_SECTIONS = debugFlag("VILLAGE_SECTIONS");
/* 137 */   public static final boolean DEBUG_BRAIN = debugFlag("BRAIN");
/* 138 */   public static final boolean DEBUG_POI = debugFlag("POI");
/* 139 */   public static final boolean DEBUG_BEES = debugFlag("BEES");
/* 140 */   public static final boolean DEBUG_RAIDS = debugFlag("RAIDS");
/* 141 */   public static final boolean DEBUG_BLOCK_BREAK = debugFlag("BLOCK_BREAK");
/* 142 */   public static final boolean DEBUG_MONITOR_TICK_TIMES = debugFlag("MONITOR_TICK_TIMES");
/* 143 */   public static final boolean DEBUG_KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN = debugFlag("KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN");
/* 144 */   public static final boolean DEBUG_DONT_SAVE_WORLD = debugFlag("DONT_SAVE_WORLD");
/* 145 */   public static final boolean DEBUG_LARGE_DRIPSTONE = debugFlag("LARGE_DRIPSTONE");
/* 146 */   public static final boolean DEBUG_CARVERS = debugFlag("CARVERS");
/* 147 */   public static final boolean DEBUG_ORE_VEINS = debugFlag("ORE_VEINS");
/* 148 */   public static final boolean DEBUG_SCULK_CATALYST = debugFlag("SCULK_CATALYST");
/* 149 */   public static final boolean DEBUG_BYPASS_REALMS_VERSION_CHECK = debugFlag("BYPASS_REALMS_VERSION_CHECK");
/* 150 */   public static final boolean DEBUG_SOCIAL_INTERACTIONS = debugFlag("SOCIAL_INTERACTIONS");
/* 151 */   public static final boolean DEBUG_VALIDATE_RESOURCE_PATH_CASE = debugFlag("VALIDATE_RESOURCE_PATH_CASE");
/* 152 */   public static final boolean DEBUG_UNLOCK_ALL_TRADES = debugFlag("UNLOCK_ALL_TRADES");
/* 153 */   public static final boolean DEBUG_BREEZE_MOB = debugFlag("BREEZE_MOB");
/* 154 */   public static final boolean DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS = debugFlag("TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS");
/* 155 */   public static final boolean DEBUG_VAULT_DETECTS_SHEEP_AS_PLAYERS = debugFlag("VAULT_DETECTS_SHEEP_AS_PLAYERS");
/* 156 */   public static final boolean DEBUG_FORCE_ONBOARDING_SCREEN = debugFlag("FORCE_ONBOARDING_SCREEN");
/* 157 */   public static final boolean DEBUG_CURSOR_POS = debugFlag("CURSOR_POS");
/* 158 */   public static final boolean DEBUG_DEFAULT_SKIN_OVERRIDE = debugFlag("DEFAULT_SKIN_OVERRIDE");
/* 159 */   public static final boolean DEBUG_PANORAMA_SCREENSHOT = debugFlag("PANORAMA_SCREENSHOT");
/* 160 */   public static final boolean DEBUG_CHASE_COMMAND = debugFlag("CHASE_COMMAND");
/* 161 */   public static final boolean DEBUG_VERBOSE_COMMAND_ERRORS = debugFlag("VERBOSE_COMMAND_ERRORS");
/* 162 */   public static final boolean DEBUG_DEV_COMMANDS = debugFlag("DEV_COMMANDS");
/* 163 */   public static final boolean DEBUG_ACTIVE_TEXT_AREAS = debugFlag("ACTIVE_TEXT_AREAS");
/*     */   
/* 165 */   public static final boolean DEBUG_IGNORE_LOCAL_MOB_CAP = debugFlag("IGNORE_LOCAL_MOB_CAP");
/*     */   
/* 167 */   public static final boolean DEBUG_DISABLE_LIQUID_SPREADING = debugFlag("DISABLE_LIQUID_SPREADING");
/* 168 */   public static final boolean DEBUG_AQUIFERS = debugFlag("AQUIFERS");
/*     */   
/* 170 */   public static final boolean DEBUG_JFR_PROFILING_ENABLE_LEVEL_LOADING = debugFlag("JFR_PROFILING_ENABLE_LEVEL_LOADING");
/* 171 */   public static final boolean DEBUG_ENTITY_BLOCK_INTERSECTION = debugFlag("ENTITY_BLOCK_INTERSECTION");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static boolean debugGenerateSquareTerrainWithoutNoise = debugFlag("GENERATE_SQUARE_TERRAIN_WITHOUT_NOISE");
/*     */ 
/*     */ 
/*     */   
/* 181 */   public static final boolean DEBUG_ONLY_GENERATE_HALF_THE_WORLD = debugFlag("ONLY_GENERATE_HALF_THE_WORLD");
/* 182 */   public static final boolean DEBUG_DISABLE_FLUID_GENERATION = debugFlag("DISABLE_FLUID_GENERATION");
/* 183 */   public static final boolean DEBUG_DISABLE_AQUIFERS = debugFlag("DISABLE_AQUIFERS");
/* 184 */   public static final boolean DEBUG_DISABLE_SURFACE = debugFlag("DISABLE_SURFACE");
/* 185 */   public static final boolean DEBUG_DISABLE_CARVERS = debugFlag("DISABLE_CARVERS");
/* 186 */   public static final boolean DEBUG_DISABLE_STRUCTURES = debugFlag("DISABLE_STRUCTURES");
/* 187 */   public static final boolean DEBUG_DISABLE_FEATURES = debugFlag("DISABLE_FEATURES");
/* 188 */   public static final boolean DEBUG_DISABLE_ORE_VEINS = debugFlag("DISABLE_ORE_VEINS");
/* 189 */   public static final boolean DEBUG_DISABLE_BLENDING = debugFlag("DISABLE_BLENDING");
/* 190 */   public static final boolean DEBUG_DISABLE_BELOW_ZERO_RETROGENERATION = debugFlag("DISABLE_BELOW_ZERO_RETROGENERATION");
/*     */   
/*     */   public static final int DEFAULT_MINECRAFT_PORT = 25565;
/* 193 */   public static final boolean DEBUG_SUBTITLES = debugFlag("SUBTITLES");
/* 194 */   public static final int DEBUG_FAKE_LATENCY_MS = debugIntValue("FAKE_LATENCY_MS");
/* 195 */   public static final int DEBUG_FAKE_JITTER_MS = debugIntValue("FAKE_JITTER_MS");
/*     */   
/* 197 */   public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
/* 198 */   public static final boolean COMMAND_STACK_TRACES = debugFlag("COMMAND_STACK_TRACES");
/* 199 */   public static final boolean DEBUG_WORLD_RECREATE = debugFlag("WORLD_RECREATE");
/* 200 */   public static final boolean DEBUG_SHOW_SERVER_DEBUG_VALUES = debugFlag("SHOW_SERVER_DEBUG_VALUES");
/*     */   
/* 202 */   public static final boolean DEBUG_FEATURE_COUNT = debugFlag("FEATURE_COUNT");
/*     */   
/* 204 */   public static final boolean DEBUG_FORCE_TELEMETRY = debugFlag("FORCE_TELEMETRY");
/* 205 */   public static final boolean DEBUG_DONT_SEND_TELEMETRY_TO_BACKEND = debugFlag("DONT_SEND_TELEMETRY_TO_BACKEND");
/*     */   
/* 207 */   public static final long MAXIMUM_TICK_TIME_NANOS = Duration.ofMillis(300L).toNanos();
/*     */   
/*     */   public static final float MAXIMUM_BLOCK_EXPLOSION_RESISTANCE = 3600000.0F;
/*     */   
/*     */   public static final boolean USE_WORKFLOWS_HOOKS = false;
/*     */   
/*     */   public static final boolean USE_DEVONLY = false;
/*     */   
/*     */   public static boolean CHECK_DATA_FIXER_SCHEMA = true;
/*     */   
/*     */   public static boolean IS_RUNNING_IN_IDE;
/*     */   
/*     */   public static final int WORLD_RESOLUTION = 16;
/*     */   public static final int MAX_CHAT_LENGTH = 256;
/*     */   public static final int MAX_USER_INPUT_COMMAND_LENGTH = 32500;
/*     */   public static final int MAX_FUNCTION_COMMAND_LENGTH = 2000000;
/*     */   public static final int MAX_PLAYER_NAME_LENGTH = 16;
/*     */   public static final int MAX_CHAINED_NEIGHBOR_UPDATES = 1000000;
/*     */   public static final int MAX_RENDER_DISTANCE = 32;
/*     */   public static final int MAX_CLOUD_DISTANCE = 128;
/* 227 */   public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', Character.MIN_VALUE, '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
/*     */   
/*     */   public static final int TICKS_PER_SECOND = 20;
/*     */   
/*     */   public static final int MILLIS_PER_TICK = 50;
/*     */   
/*     */   public static final int TICKS_PER_MINUTE = 1200;
/*     */   
/*     */   public static final int TICKS_PER_GAME_DAY = 24000;
/*     */   
/*     */   public static final int DEFAULT_RANDOM_TICK_SPEED = 3;
/*     */   
/*     */   public static final float AVERAGE_GAME_TICKS_PER_RANDOM_TICK_PER_BLOCK = 1365.3334F;
/*     */   public static final float AVERAGE_RANDOM_TICKS_PER_BLOCK_PER_MINUTE = 0.87890625F;
/*     */   public static final float AVERAGE_RANDOM_TICKS_PER_BLOCK_PER_GAME_DAY = 17.578125F;
/*     */   public static final int WORLD_ICON_SIZE = 64;
/*     */   private static WorldVersion CURRENT_VERSION;
/*     */   
/*     */   public static void setVersion(WorldVersion paramWorldVersion) {
/* 246 */     if (CURRENT_VERSION == null) {
/* 247 */       CURRENT_VERSION = paramWorldVersion;
/* 248 */     } else if (paramWorldVersion != CURRENT_VERSION) {
/* 249 */       throw new IllegalStateException("Cannot override the current game version!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryDetectVersion() {
/* 254 */     if (CURRENT_VERSION == null) {
/* 255 */       CURRENT_VERSION = DetectedVersion.tryDetectVersion();
/*     */     }
/*     */   }
/*     */   
/*     */   public static WorldVersion getCurrentVersion() {
/* 260 */     if (CURRENT_VERSION == null) {
/* 261 */       throw new IllegalStateException("Game version not set");
/*     */     }
/* 263 */     return CURRENT_VERSION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getProtocolVersion() {
/* 270 */     return 774;
/*     */   }
/*     */   
/*     */   static {
/* 274 */     ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
/* 275 */     CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES = COMMAND_STACK_TRACES;
/* 276 */     CommandSyntaxException.BUILT_IN_EXCEPTIONS = (BuiltInExceptionProvider)new BrigadierExceptions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean debugVoidTerrain(ChunkPos paramChunkPos) {
/* 283 */     int i = paramChunkPos.getMinBlockX();
/* 284 */     int j = paramChunkPos.getMinBlockZ();
/* 285 */     if (DEBUG_ONLY_GENERATE_HALF_THE_WORLD) {
/* 286 */       return (j < 0);
/*     */     }
/*     */     
/* 289 */     if (debugGenerateSquareTerrainWithoutNoise) {
/* 290 */       return (i > 8192 || i < 0 || j > 1024 || j < 0);
/*     */     }
/* 292 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\SharedConstants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */