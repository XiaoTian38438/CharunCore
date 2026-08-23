/*     */ package net.minecraft.world.entity.ai.memory;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearAttack;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MemoryModuleType<U>
/*     */ {
/*  34 */   public static final MemoryModuleType<Void> DUMMY = register("dummy");
/*  35 */   public static final MemoryModuleType<GlobalPos> HOME = register("home", GlobalPos.CODEC);
/*  36 */   public static final MemoryModuleType<GlobalPos> JOB_SITE = register("job_site", GlobalPos.CODEC);
/*  37 */   public static final MemoryModuleType<GlobalPos> POTENTIAL_JOB_SITE = register("potential_job_site", GlobalPos.CODEC);
/*  38 */   public static final MemoryModuleType<GlobalPos> MEETING_POINT = register("meeting_point", GlobalPos.CODEC);
/*  39 */   public static final MemoryModuleType<List<GlobalPos>> SECONDARY_JOB_SITE = register("secondary_job_site");
/*  40 */   public static final MemoryModuleType<List<LivingEntity>> NEAREST_LIVING_ENTITIES = register("mobs");
/*  41 */   public static final MemoryModuleType<NearestVisibleLivingEntities> NEAREST_VISIBLE_LIVING_ENTITIES = register("visible_mobs");
/*  42 */   public static final MemoryModuleType<List<LivingEntity>> VISIBLE_VILLAGER_BABIES = register("visible_villager_babies");
/*  43 */   public static final MemoryModuleType<List<Player>> NEAREST_PLAYERS = register("nearest_players");
/*  44 */   public static final MemoryModuleType<Player> NEAREST_VISIBLE_PLAYER = register("nearest_visible_player");
/*  45 */   public static final MemoryModuleType<Player> NEAREST_VISIBLE_ATTACKABLE_PLAYER = register("nearest_visible_targetable_player");
/*  46 */   public static final MemoryModuleType<List<Player>> NEAREST_VISIBLE_ATTACKABLE_PLAYERS = register("nearest_visible_targetable_players");
/*  47 */   public static final MemoryModuleType<WalkTarget> WALK_TARGET = register("walk_target");
/*  48 */   public static final MemoryModuleType<PositionTracker> LOOK_TARGET = register("look_target");
/*  49 */   public static final MemoryModuleType<LivingEntity> ATTACK_TARGET = register("attack_target");
/*  50 */   public static final MemoryModuleType<Boolean> ATTACK_COOLING_DOWN = register("attack_cooling_down");
/*  51 */   public static final MemoryModuleType<LivingEntity> INTERACTION_TARGET = register("interaction_target");
/*  52 */   public static final MemoryModuleType<AgeableMob> BREED_TARGET = register("breed_target");
/*  53 */   public static final MemoryModuleType<Entity> RIDE_TARGET = register("ride_target");
/*  54 */   public static final MemoryModuleType<Path> PATH = register("path");
/*  55 */   public static final MemoryModuleType<List<GlobalPos>> INTERACTABLE_DOORS = register("interactable_doors");
/*  56 */   public static final MemoryModuleType<Set<GlobalPos>> DOORS_TO_CLOSE = register("doors_to_close");
/*  57 */   public static final MemoryModuleType<BlockPos> NEAREST_BED = register("nearest_bed");
/*  58 */   public static final MemoryModuleType<DamageSource> HURT_BY = register("hurt_by");
/*  59 */   public static final MemoryModuleType<LivingEntity> HURT_BY_ENTITY = register("hurt_by_entity");
/*  60 */   public static final MemoryModuleType<LivingEntity> AVOID_TARGET = register("avoid_target");
/*  61 */   public static final MemoryModuleType<LivingEntity> NEAREST_HOSTILE = register("nearest_hostile");
/*  62 */   public static final MemoryModuleType<LivingEntity> NEAREST_ATTACKABLE = register("nearest_attackable");
/*  63 */   public static final MemoryModuleType<GlobalPos> HIDING_PLACE = register("hiding_place");
/*  64 */   public static final MemoryModuleType<Long> HEARD_BELL_TIME = register("heard_bell_time");
/*  65 */   public static final MemoryModuleType<Long> CANT_REACH_WALK_TARGET_SINCE = register("cant_reach_walk_target_since");
/*  66 */   public static final MemoryModuleType<Boolean> GOLEM_DETECTED_RECENTLY = register("golem_detected_recently", (Codec<Boolean>)Codec.BOOL);
/*  67 */   public static final MemoryModuleType<Boolean> DANGER_DETECTED_RECENTLY = register("danger_detected_recently", (Codec<Boolean>)Codec.BOOL);
/*  68 */   public static final MemoryModuleType<Long> LAST_SLEPT = register("last_slept", (Codec<Long>)Codec.LONG);
/*  69 */   public static final MemoryModuleType<Long> LAST_WOKEN = register("last_woken", (Codec<Long>)Codec.LONG);
/*  70 */   public static final MemoryModuleType<Long> LAST_WORKED_AT_POI = register("last_worked_at_poi", (Codec<Long>)Codec.LONG);
/*  71 */   public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_ADULT = register("nearest_visible_adult");
/*  72 */   public static final MemoryModuleType<ItemEntity> NEAREST_VISIBLE_WANTED_ITEM = register("nearest_visible_wanted_item");
/*  73 */   public static final MemoryModuleType<Mob> NEAREST_VISIBLE_NEMESIS = register("nearest_visible_nemesis");
/*  74 */   public static final MemoryModuleType<Integer> PLAY_DEAD_TICKS = register("play_dead_ticks", (Codec<Integer>)Codec.INT);
/*  75 */   public static final MemoryModuleType<Player> TEMPTING_PLAYER = register("tempting_player");
/*  76 */   public static final MemoryModuleType<Integer> TEMPTATION_COOLDOWN_TICKS = register("temptation_cooldown_ticks", (Codec<Integer>)Codec.INT);
/*  77 */   public static final MemoryModuleType<Integer> GAZE_COOLDOWN_TICKS = register("gaze_cooldown_ticks", (Codec<Integer>)Codec.INT);
/*  78 */   public static final MemoryModuleType<Boolean> IS_TEMPTED = register("is_tempted", (Codec<Boolean>)Codec.BOOL);
/*  79 */   public static final MemoryModuleType<Integer> LONG_JUMP_COOLDOWN_TICKS = register("long_jump_cooling_down", (Codec<Integer>)Codec.INT);
/*  80 */   public static final MemoryModuleType<Boolean> LONG_JUMP_MID_JUMP = register("long_jump_mid_jump");
/*  81 */   public static final MemoryModuleType<Boolean> HAS_HUNTING_COOLDOWN = register("has_hunting_cooldown", (Codec<Boolean>)Codec.BOOL);
/*  82 */   public static final MemoryModuleType<Integer> RAM_COOLDOWN_TICKS = register("ram_cooldown_ticks", (Codec<Integer>)Codec.INT);
/*  83 */   public static final MemoryModuleType<Vec3> RAM_TARGET = register("ram_target");
/*  84 */   public static final MemoryModuleType<Unit> IS_IN_WATER = register("is_in_water", Unit.CODEC);
/*  85 */   public static final MemoryModuleType<Unit> IS_PREGNANT = register("is_pregnant", Unit.CODEC);
/*  86 */   public static final MemoryModuleType<Boolean> IS_PANICKING = register("is_panicking", (Codec<Boolean>)Codec.BOOL);
/*  87 */   public static final MemoryModuleType<List<UUID>> UNREACHABLE_TONGUE_TARGETS = register("unreachable_tongue_targets");
/*  88 */   public static final MemoryModuleType<Set<GlobalPos>> VISITED_BLOCK_POSITIONS = register("visited_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList));
/*  89 */   public static final MemoryModuleType<Set<GlobalPos>> UNREACHABLE_TRANSPORT_BLOCK_POSITIONS = register("unreachable_transport_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList));
/*  90 */   public static final MemoryModuleType<Integer> TRANSPORT_ITEMS_COOLDOWN_TICKS = register("transport_items_cooldown_ticks");
/*  91 */   public static final MemoryModuleType<Integer> CHARGE_COOLDOWN_TICKS = register("charge_cooldown_ticks", (Codec<Integer>)Codec.INT);
/*  92 */   public static final MemoryModuleType<Integer> ATTACK_TARGET_COOLDOWN = register("attack_target_cooldown", (Codec<Integer>)Codec.INT);
/*  93 */   public static final MemoryModuleType<Integer> SPEAR_FLEEING_TIME = register("spear_fleeing_time");
/*  94 */   public static final MemoryModuleType<Vec3> SPEAR_FLEEING_POSITION = register("spear_fleeing_position");
/*  95 */   public static final MemoryModuleType<Vec3> SPEAR_CHARGE_POSITION = register("spear_charge_position");
/*  96 */   public static final MemoryModuleType<Integer> SPEAR_ENGAGE_TIME = register("spear_engage_time");
/*  97 */   public static final MemoryModuleType<SpearAttack.SpearStatus> SPEAR_STATUS = register("spear_status");
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
/* 108 */   public static final MemoryModuleType<UUID> ANGRY_AT = register("angry_at", UUIDUtil.CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final MemoryModuleType<Boolean> UNIVERSAL_ANGER = register("universal_anger", (Codec<Boolean>)Codec.BOOL);
/* 114 */   public static final MemoryModuleType<Boolean> ADMIRING_ITEM = register("admiring_item", (Codec<Boolean>)Codec.BOOL);
/* 115 */   public static final MemoryModuleType<Integer> TIME_TRYING_TO_REACH_ADMIRE_ITEM = register("time_trying_to_reach_admire_item");
/* 116 */   public static final MemoryModuleType<Boolean> DISABLE_WALK_TO_ADMIRE_ITEM = register("disable_walk_to_admire_item");
/* 117 */   public static final MemoryModuleType<Boolean> ADMIRING_DISABLED = register("admiring_disabled", (Codec<Boolean>)Codec.BOOL);
/* 118 */   public static final MemoryModuleType<Boolean> HUNTED_RECENTLY = register("hunted_recently", (Codec<Boolean>)Codec.BOOL);
/*     */   
/* 120 */   public static final MemoryModuleType<BlockPos> CELEBRATE_LOCATION = register("celebrate_location");
/* 121 */   public static final MemoryModuleType<Boolean> DANCING = register("dancing");
/* 122 */   public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_HUNTABLE_HOGLIN = register("nearest_visible_huntable_hoglin");
/* 123 */   public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_BABY_HOGLIN = register("nearest_visible_baby_hoglin");
/* 124 */   public static final MemoryModuleType<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD = register("nearest_targetable_player_not_wearing_gold");
/* 125 */   public static final MemoryModuleType<List<AbstractPiglin>> NEARBY_ADULT_PIGLINS = register("nearby_adult_piglins");
/* 126 */   public static final MemoryModuleType<List<AbstractPiglin>> NEAREST_VISIBLE_ADULT_PIGLINS = register("nearest_visible_adult_piglins");
/* 127 */   public static final MemoryModuleType<List<Hoglin>> NEAREST_VISIBLE_ADULT_HOGLINS = register("nearest_visible_adult_hoglins");
/*     */   
/* 129 */   public static final MemoryModuleType<AbstractPiglin> NEAREST_VISIBLE_ADULT_PIGLIN = register("nearest_visible_adult_piglin");
/* 130 */   public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_ZOMBIFIED = register("nearest_visible_zombified");
/* 131 */   public static final MemoryModuleType<Integer> VISIBLE_ADULT_PIGLIN_COUNT = register("visible_adult_piglin_count");
/* 132 */   public static final MemoryModuleType<Integer> VISIBLE_ADULT_HOGLIN_COUNT = register("visible_adult_hoglin_count");
/* 133 */   public static final MemoryModuleType<Player> NEAREST_PLAYER_HOLDING_WANTED_ITEM = register("nearest_player_holding_wanted_item");
/* 134 */   public static final MemoryModuleType<Boolean> ATE_RECENTLY = register("ate_recently");
/* 135 */   public static final MemoryModuleType<BlockPos> NEAREST_REPELLENT = register("nearest_repellent");
/* 136 */   public static final MemoryModuleType<Boolean> PACIFIED = register("pacified");
/*     */ 
/*     */   
/* 139 */   public static final MemoryModuleType<LivingEntity> ROAR_TARGET = register("roar_target");
/* 140 */   public static final MemoryModuleType<BlockPos> DISTURBANCE_LOCATION = register("disturbance_location");
/* 141 */   public static final MemoryModuleType<Unit> RECENT_PROJECTILE = register("recent_projectile", Unit.CODEC);
/* 142 */   public static final MemoryModuleType<Unit> IS_SNIFFING = register("is_sniffing", Unit.CODEC);
/* 143 */   public static final MemoryModuleType<Unit> IS_EMERGING = register("is_emerging", Unit.CODEC);
/* 144 */   public static final MemoryModuleType<Unit> ROAR_SOUND_DELAY = register("roar_sound_delay", Unit.CODEC);
/* 145 */   public static final MemoryModuleType<Unit> DIG_COOLDOWN = register("dig_cooldown", Unit.CODEC);
/* 146 */   public static final MemoryModuleType<Unit> ROAR_SOUND_COOLDOWN = register("roar_sound_cooldown", Unit.CODEC);
/* 147 */   public static final MemoryModuleType<Unit> SNIFF_COOLDOWN = register("sniff_cooldown", Unit.CODEC);
/* 148 */   public static final MemoryModuleType<Unit> TOUCH_COOLDOWN = register("touch_cooldown", Unit.CODEC);
/* 149 */   public static final MemoryModuleType<Unit> VIBRATION_COOLDOWN = register("vibration_cooldown", Unit.CODEC);
/* 150 */   public static final MemoryModuleType<Unit> SONIC_BOOM_COOLDOWN = register("sonic_boom_cooldown", Unit.CODEC);
/* 151 */   public static final MemoryModuleType<Unit> SONIC_BOOM_SOUND_COOLDOWN = register("sonic_boom_sound_cooldown", Unit.CODEC);
/* 152 */   public static final MemoryModuleType<Unit> SONIC_BOOM_SOUND_DELAY = register("sonic_boom_sound_delay", Unit.CODEC);
/*     */   
/* 154 */   public static final MemoryModuleType<UUID> LIKED_PLAYER = register("liked_player", UUIDUtil.CODEC);
/* 155 */   public static final MemoryModuleType<GlobalPos> LIKED_NOTEBLOCK_POSITION = register("liked_noteblock", GlobalPos.CODEC);
/* 156 */   public static final MemoryModuleType<Integer> LIKED_NOTEBLOCK_COOLDOWN_TICKS = register("liked_noteblock_cooldown_ticks", (Codec<Integer>)Codec.INT);
/* 157 */   public static final MemoryModuleType<Integer> ITEM_PICKUP_COOLDOWN_TICKS = register("item_pickup_cooldown_ticks", (Codec<Integer>)Codec.INT);
/*     */ 
/*     */   
/* 160 */   public static final MemoryModuleType<List<GlobalPos>> SNIFFER_EXPLORED_POSITIONS = register("sniffer_explored_positions", Codec.list(GlobalPos.CODEC));
/* 161 */   public static final MemoryModuleType<BlockPos> SNIFFER_SNIFFING_TARGET = register("sniffer_sniffing_target");
/* 162 */   public static final MemoryModuleType<Boolean> SNIFFER_DIGGING = register("sniffer_digging");
/* 163 */   public static final MemoryModuleType<Boolean> SNIFFER_HAPPY = register("sniffer_happy");
/*     */ 
/*     */   
/* 166 */   public static final MemoryModuleType<Unit> BREEZE_JUMP_COOLDOWN = register("breeze_jump_cooldown", Unit.CODEC);
/* 167 */   public static final MemoryModuleType<Unit> BREEZE_SHOOT = register("breeze_shoot", Unit.CODEC);
/* 168 */   public static final MemoryModuleType<Unit> BREEZE_SHOOT_CHARGING = register("breeze_shoot_charging", Unit.CODEC);
/* 169 */   public static final MemoryModuleType<Unit> BREEZE_SHOOT_RECOVERING = register("breeze_shoot_recover", Unit.CODEC);
/* 170 */   public static final MemoryModuleType<Unit> BREEZE_SHOOT_COOLDOWN = register("breeze_shoot_cooldown", Unit.CODEC);
/* 171 */   public static final MemoryModuleType<Unit> BREEZE_JUMP_INHALING = register("breeze_jump_inhaling", Unit.CODEC);
/* 172 */   public static final MemoryModuleType<BlockPos> BREEZE_JUMP_TARGET = register("breeze_jump_target", BlockPos.CODEC);
/* 173 */   public static final MemoryModuleType<Unit> BREEZE_LEAVING_WATER = register("breeze_leaving_water", Unit.CODEC);
/*     */   
/*     */   private final Optional<Codec<ExpirableValue<U>>> codec;
/*     */   
/*     */   @VisibleForTesting
/*     */   public MemoryModuleType(Optional<Codec<U>> paramOptional) {
/* 179 */     this.codec = paramOptional.map(ExpirableValue::codec);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(this).toString();
/*     */   }
/*     */   
/*     */   public Optional<Codec<ExpirableValue<U>>> getCodec() {
/* 188 */     return this.codec;
/*     */   }
/*     */   
/*     */   private static <U> MemoryModuleType<U> register(String paramString, Codec<U> paramCodec) {
/* 192 */     return (MemoryModuleType<U>)Registry.register((Registry)BuiltInRegistries.MEMORY_MODULE_TYPE, Identifier.withDefaultNamespace(paramString), new MemoryModuleType(Optional.of(paramCodec)));
/*     */   }
/*     */   
/*     */   private static <U> MemoryModuleType<U> register(String paramString) {
/* 196 */     return (MemoryModuleType<U>)Registry.register((Registry)BuiltInRegistries.MEMORY_MODULE_TYPE, Identifier.withDefaultNamespace(paramString), new MemoryModuleType(Optional.empty()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\memory\MemoryModuleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */