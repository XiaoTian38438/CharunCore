/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.redstone.Orientation;
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
/*     */ public class DebugSubscriptions<T>
/*     */ {
/*  80 */   public static final DebugSubscription<?> DEDICATED_SERVER_TICK_TIME = registerSimple("dedicated_server_tick_time");
/*     */ 
/*     */   
/*  83 */   public static final DebugSubscription<DebugBeeInfo> BEES = registerWithValue("bees", (StreamCodec)DebugBeeInfo.STREAM_CODEC);
/*  84 */   public static final DebugSubscription<DebugBrainDump> BRAINS = registerWithValue("brains", (StreamCodec)DebugBrainDump.STREAM_CODEC);
/*  85 */   public static final DebugSubscription<DebugBreezeInfo> BREEZES = registerWithValue("breezes", (StreamCodec)DebugBreezeInfo.STREAM_CODEC);
/*  86 */   public static final DebugSubscription<DebugGoalInfo> GOAL_SELECTORS = registerWithValue("goal_selectors", (StreamCodec)DebugGoalInfo.STREAM_CODEC);
/*  87 */   public static final DebugSubscription<DebugPathInfo> ENTITY_PATHS = registerWithValue("entity_paths", (StreamCodec)DebugPathInfo.STREAM_CODEC);
/*     */ 
/*     */   
/*  90 */   public static final DebugSubscription<DebugEntityBlockIntersection> ENTITY_BLOCK_INTERSECTIONS = registerTemporaryValue("entity_block_intersections", (StreamCodec)DebugEntityBlockIntersection.STREAM_CODEC, 100);
/*  91 */   public static final DebugSubscription<DebugHiveInfo> BEE_HIVES = registerWithValue("bee_hives", DebugHiveInfo.STREAM_CODEC);
/*  92 */   public static final DebugSubscription<DebugPoiInfo> POIS = registerWithValue("pois", DebugPoiInfo.STREAM_CODEC);
/*  93 */   public static final DebugSubscription<Orientation> REDSTONE_WIRE_ORIENTATIONS = registerTemporaryValue("redstone_wire_orientations", Orientation.STREAM_CODEC, 200);
/*  94 */   public static final DebugSubscription<Unit> VILLAGE_SECTIONS = registerWithValue("village_sections", Unit.STREAM_CODEC);
/*     */ 
/*     */   
/*  97 */   public static final DebugSubscription<List<BlockPos>> RAIDS = registerWithValue("raids", BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*  98 */   public static final DebugSubscription<List<DebugStructureInfo>> STRUCTURES = registerWithValue("structures", DebugStructureInfo.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */ 
/*     */   
/* 101 */   public static final DebugSubscription<DebugGameEventListenerInfo> GAME_EVENT_LISTENERS = registerWithValue("game_event_listeners", DebugGameEventListenerInfo.STREAM_CODEC);
/*     */ 
/*     */   
/* 104 */   public static final DebugSubscription<BlockPos> NEIGHBOR_UPDATES = registerTemporaryValue("neighbor_updates", BlockPos.STREAM_CODEC, 200);
/* 105 */   public static final DebugSubscription<DebugGameEventInfo> GAME_EVENTS = registerTemporaryValue("game_events", DebugGameEventInfo.STREAM_CODEC, 60);
/*     */   
/*     */   public static DebugSubscription<?> bootstrap(Registry<DebugSubscription<?>> paramRegistry) {
/* 108 */     return DEDICATED_SERVER_TICK_TIME;
/*     */   }
/*     */   
/*     */   private static DebugSubscription<?> registerSimple(String paramString) {
/* 112 */     return (DebugSubscription)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(paramString), new DebugSubscription(null));
/*     */   }
/*     */   
/*     */   private static <T> DebugSubscription<T> registerWithValue(String paramString, StreamCodec<? super RegistryFriendlyByteBuf, T> paramStreamCodec) {
/* 116 */     return (DebugSubscription<T>)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(paramString), new DebugSubscription<>(paramStreamCodec));
/*     */   }
/*     */   
/*     */   private static <T> DebugSubscription<T> registerTemporaryValue(String paramString, StreamCodec<? super RegistryFriendlyByteBuf, T> paramStreamCodec, int paramInt) {
/* 120 */     return (DebugSubscription<T>)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(paramString), new DebugSubscription<>(paramStreamCodec, paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\DebugSubscriptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */