/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class GamePacketTypes
/*     */ {
/*   9 */   public static final PacketType<ClientboundBundlePacket> CLIENTBOUND_BUNDLE = createClientbound("bundle");
/*  10 */   public static final PacketType<ClientboundBundleDelimiterPacket> CLIENTBOUND_BUNDLE_DELIMITER = createClientbound("bundle_delimiter");
/*     */   
/*  12 */   public static final PacketType<ClientboundAddEntityPacket> CLIENTBOUND_ADD_ENTITY = createClientbound("add_entity");
/*  13 */   public static final PacketType<ClientboundAnimatePacket> CLIENTBOUND_ANIMATE = createClientbound("animate");
/*  14 */   public static final PacketType<ClientboundAwardStatsPacket> CLIENTBOUND_AWARD_STATS = createClientbound("award_stats");
/*  15 */   public static final PacketType<ClientboundBlockChangedAckPacket> CLIENTBOUND_BLOCK_CHANGED_ACK = createClientbound("block_changed_ack");
/*  16 */   public static final PacketType<ClientboundBlockDestructionPacket> CLIENTBOUND_BLOCK_DESTRUCTION = createClientbound("block_destruction");
/*  17 */   public static final PacketType<ClientboundBlockEntityDataPacket> CLIENTBOUND_BLOCK_ENTITY_DATA = createClientbound("block_entity_data");
/*  18 */   public static final PacketType<ClientboundBlockEventPacket> CLIENTBOUND_BLOCK_EVENT = createClientbound("block_event");
/*  19 */   public static final PacketType<ClientboundBlockUpdatePacket> CLIENTBOUND_BLOCK_UPDATE = createClientbound("block_update");
/*  20 */   public static final PacketType<ClientboundBossEventPacket> CLIENTBOUND_BOSS_EVENT = createClientbound("boss_event");
/*  21 */   public static final PacketType<ClientboundChangeDifficultyPacket> CLIENTBOUND_CHANGE_DIFFICULTY = createClientbound("change_difficulty");
/*  22 */   public static final PacketType<ClientboundChunkBatchFinishedPacket> CLIENTBOUND_CHUNK_BATCH_FINISHED = createClientbound("chunk_batch_finished");
/*  23 */   public static final PacketType<ClientboundChunkBatchStartPacket> CLIENTBOUND_CHUNK_BATCH_START = createClientbound("chunk_batch_start");
/*  24 */   public static final PacketType<ClientboundChunksBiomesPacket> CLIENTBOUND_CHUNKS_BIOMES = createClientbound("chunks_biomes");
/*  25 */   public static final PacketType<ClientboundClearTitlesPacket> CLIENTBOUND_CLEAR_TITLES = createClientbound("clear_titles");
/*  26 */   public static final PacketType<ClientboundCommandSuggestionsPacket> CLIENTBOUND_COMMAND_SUGGESTIONS = createClientbound("command_suggestions");
/*  27 */   public static final PacketType<ClientboundCommandsPacket> CLIENTBOUND_COMMANDS = createClientbound("commands");
/*  28 */   public static final PacketType<ClientboundContainerClosePacket> CLIENTBOUND_CONTAINER_CLOSE = createClientbound("container_close");
/*  29 */   public static final PacketType<ClientboundContainerSetContentPacket> CLIENTBOUND_CONTAINER_SET_CONTENT = createClientbound("container_set_content");
/*  30 */   public static final PacketType<ClientboundContainerSetDataPacket> CLIENTBOUND_CONTAINER_SET_DATA = createClientbound("container_set_data");
/*  31 */   public static final PacketType<ClientboundContainerSetSlotPacket> CLIENTBOUND_CONTAINER_SET_SLOT = createClientbound("container_set_slot");
/*  32 */   public static final PacketType<ClientboundCooldownPacket> CLIENTBOUND_COOLDOWN = createClientbound("cooldown");
/*  33 */   public static final PacketType<ClientboundCustomChatCompletionsPacket> CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS = createClientbound("custom_chat_completions");
/*  34 */   public static final PacketType<ClientboundDamageEventPacket> CLIENTBOUND_DAMAGE_EVENT = createClientbound("damage_event");
/*  35 */   public static final PacketType<ClientboundDebugBlockValuePacket> CLIENTBOUND_DEBUG_BLOCK_VALUE = createClientbound("debug/block_value");
/*  36 */   public static final PacketType<ClientboundDebugChunkValuePacket> CLIENTBOUND_DEBUG_CHUNK_VALUE = createClientbound("debug/chunk_value");
/*  37 */   public static final PacketType<ClientboundDebugEntityValuePacket> CLIENTBOUND_DEBUG_ENTITY_VALUE = createClientbound("debug/entity_value");
/*  38 */   public static final PacketType<ClientboundDebugEventPacket> CLIENTBOUND_DEBUG_EVENT = createClientbound("debug/event");
/*  39 */   public static final PacketType<ClientboundDebugSamplePacket> CLIENTBOUND_DEBUG_SAMPLE = createClientbound("debug_sample");
/*  40 */   public static final PacketType<ClientboundDeleteChatPacket> CLIENTBOUND_DELETE_CHAT = createClientbound("delete_chat");
/*  41 */   public static final PacketType<ClientboundDisguisedChatPacket> CLIENTBOUND_DISGUISED_CHAT = createClientbound("disguised_chat");
/*  42 */   public static final PacketType<ClientboundEntityEventPacket> CLIENTBOUND_ENTITY_EVENT = createClientbound("entity_event");
/*  43 */   public static final PacketType<ClientboundEntityPositionSyncPacket> CLIENTBOUND_ENTITY_POSITION_SYNC = createClientbound("entity_position_sync");
/*  44 */   public static final PacketType<ClientboundExplodePacket> CLIENTBOUND_EXPLODE = createClientbound("explode");
/*  45 */   public static final PacketType<ClientboundForgetLevelChunkPacket> CLIENTBOUND_FORGET_LEVEL_CHUNK = createClientbound("forget_level_chunk");
/*  46 */   public static final PacketType<ClientboundGameEventPacket> CLIENTBOUND_GAME_EVENT = createClientbound("game_event");
/*  47 */   public static final PacketType<ClientboundGameTestHighlightPosPacket> CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS = createClientbound("game_test_highlight_pos");
/*  48 */   public static final PacketType<ClientboundMountScreenOpenPacket> CLIENTBOUND_MOUNT_SCREEN_OPEN = createClientbound("mount_screen_open");
/*  49 */   public static final PacketType<ClientboundHurtAnimationPacket> CLIENTBOUND_HURT_ANIMATION = createClientbound("hurt_animation");
/*  50 */   public static final PacketType<ClientboundInitializeBorderPacket> CLIENTBOUND_INITIALIZE_BORDER = createClientbound("initialize_border");
/*  51 */   public static final PacketType<ClientboundLevelChunkWithLightPacket> CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT = createClientbound("level_chunk_with_light");
/*  52 */   public static final PacketType<ClientboundLevelEventPacket> CLIENTBOUND_LEVEL_EVENT = createClientbound("level_event");
/*  53 */   public static final PacketType<ClientboundLevelParticlesPacket> CLIENTBOUND_LEVEL_PARTICLES = createClientbound("level_particles");
/*  54 */   public static final PacketType<ClientboundLightUpdatePacket> CLIENTBOUND_LIGHT_UPDATE = createClientbound("light_update");
/*  55 */   public static final PacketType<ClientboundLoginPacket> CLIENTBOUND_LOGIN = createClientbound("login");
/*  56 */   public static final PacketType<ClientboundMapItemDataPacket> CLIENTBOUND_MAP_ITEM_DATA = createClientbound("map_item_data");
/*  57 */   public static final PacketType<ClientboundMerchantOffersPacket> CLIENTBOUND_MERCHANT_OFFERS = createClientbound("merchant_offers");
/*  58 */   public static final PacketType<ClientboundMoveEntityPacket.Pos> CLIENTBOUND_MOVE_ENTITY_POS = createClientbound("move_entity_pos");
/*  59 */   public static final PacketType<ClientboundMoveEntityPacket.PosRot> CLIENTBOUND_MOVE_ENTITY_POS_ROT = createClientbound("move_entity_pos_rot");
/*  60 */   public static final PacketType<ClientboundMoveMinecartPacket> CLIENTBOUND_MOVE_MINECART_ALONG_TRACK = createClientbound("move_minecart_along_track");
/*  61 */   public static final PacketType<ClientboundMoveEntityPacket.Rot> CLIENTBOUND_MOVE_ENTITY_ROT = createClientbound("move_entity_rot");
/*  62 */   public static final PacketType<ClientboundMoveVehiclePacket> CLIENTBOUND_MOVE_VEHICLE = createClientbound("move_vehicle");
/*  63 */   public static final PacketType<ClientboundOpenBookPacket> CLIENTBOUND_OPEN_BOOK = createClientbound("open_book");
/*  64 */   public static final PacketType<ClientboundOpenScreenPacket> CLIENTBOUND_OPEN_SCREEN = createClientbound("open_screen");
/*  65 */   public static final PacketType<ClientboundOpenSignEditorPacket> CLIENTBOUND_OPEN_SIGN_EDITOR = createClientbound("open_sign_editor");
/*  66 */   public static final PacketType<ClientboundPlaceGhostRecipePacket> CLIENTBOUND_PLACE_GHOST_RECIPE = createClientbound("place_ghost_recipe");
/*  67 */   public static final PacketType<ClientboundPlayerAbilitiesPacket> CLIENTBOUND_PLAYER_ABILITIES = createClientbound("player_abilities");
/*  68 */   public static final PacketType<ClientboundPlayerChatPacket> CLIENTBOUND_PLAYER_CHAT = createClientbound("player_chat");
/*  69 */   public static final PacketType<ClientboundPlayerCombatEndPacket> CLIENTBOUND_PLAYER_COMBAT_END = createClientbound("player_combat_end");
/*  70 */   public static final PacketType<ClientboundPlayerCombatEnterPacket> CLIENTBOUND_PLAYER_COMBAT_ENTER = createClientbound("player_combat_enter");
/*  71 */   public static final PacketType<ClientboundPlayerCombatKillPacket> CLIENTBOUND_PLAYER_COMBAT_KILL = createClientbound("player_combat_kill");
/*  72 */   public static final PacketType<ClientboundPlayerInfoRemovePacket> CLIENTBOUND_PLAYER_INFO_REMOVE = createClientbound("player_info_remove");
/*  73 */   public static final PacketType<ClientboundPlayerInfoUpdatePacket> CLIENTBOUND_PLAYER_INFO_UPDATE = createClientbound("player_info_update");
/*  74 */   public static final PacketType<ClientboundPlayerLookAtPacket> CLIENTBOUND_PLAYER_LOOK_AT = createClientbound("player_look_at");
/*  75 */   public static final PacketType<ClientboundPlayerPositionPacket> CLIENTBOUND_PLAYER_POSITION = createClientbound("player_position");
/*  76 */   public static final PacketType<ClientboundPlayerRotationPacket> CLIENTBOUND_PLAYER_ROTATION = createClientbound("player_rotation");
/*  77 */   public static final PacketType<ClientboundRecipeBookAddPacket> CLIENTBOUND_RECIPE_BOOK_ADD = createClientbound("recipe_book_add");
/*  78 */   public static final PacketType<ClientboundRecipeBookRemovePacket> CLIENTBOUND_RECIPE_BOOK_REMOVE = createClientbound("recipe_book_remove");
/*  79 */   public static final PacketType<ClientboundRecipeBookSettingsPacket> CLIENTBOUND_RECIPE_BOOK_SETTINGS = createClientbound("recipe_book_settings");
/*  80 */   public static final PacketType<ClientboundRemoveEntitiesPacket> CLIENTBOUND_REMOVE_ENTITIES = createClientbound("remove_entities");
/*  81 */   public static final PacketType<ClientboundRemoveMobEffectPacket> CLIENTBOUND_REMOVE_MOB_EFFECT = createClientbound("remove_mob_effect");
/*  82 */   public static final PacketType<ClientboundRespawnPacket> CLIENTBOUND_RESPAWN = createClientbound("respawn");
/*  83 */   public static final PacketType<ClientboundRotateHeadPacket> CLIENTBOUND_ROTATE_HEAD = createClientbound("rotate_head");
/*  84 */   public static final PacketType<ClientboundSectionBlocksUpdatePacket> CLIENTBOUND_SECTION_BLOCKS_UPDATE = createClientbound("section_blocks_update");
/*  85 */   public static final PacketType<ClientboundSelectAdvancementsTabPacket> CLIENTBOUND_SELECT_ADVANCEMENTS_TAB = createClientbound("select_advancements_tab");
/*  86 */   public static final PacketType<ClientboundServerDataPacket> CLIENTBOUND_SERVER_DATA = createClientbound("server_data");
/*  87 */   public static final PacketType<ClientboundSetActionBarTextPacket> CLIENTBOUND_SET_ACTION_BAR_TEXT = createClientbound("set_action_bar_text");
/*  88 */   public static final PacketType<ClientboundSetBorderCenterPacket> CLIENTBOUND_SET_BORDER_CENTER = createClientbound("set_border_center");
/*  89 */   public static final PacketType<ClientboundSetBorderLerpSizePacket> CLIENTBOUND_SET_BORDER_LERP_SIZE = createClientbound("set_border_lerp_size");
/*  90 */   public static final PacketType<ClientboundSetBorderSizePacket> CLIENTBOUND_SET_BORDER_SIZE = createClientbound("set_border_size");
/*  91 */   public static final PacketType<ClientboundSetBorderWarningDelayPacket> CLIENTBOUND_SET_BORDER_WARNING_DELAY = createClientbound("set_border_warning_delay");
/*  92 */   public static final PacketType<ClientboundSetBorderWarningDistancePacket> CLIENTBOUND_SET_BORDER_WARNING_DISTANCE = createClientbound("set_border_warning_distance");
/*  93 */   public static final PacketType<ClientboundSetCameraPacket> CLIENTBOUND_SET_CAMERA = createClientbound("set_camera");
/*  94 */   public static final PacketType<ClientboundSetChunkCacheCenterPacket> CLIENTBOUND_SET_CHUNK_CACHE_CENTER = createClientbound("set_chunk_cache_center");
/*  95 */   public static final PacketType<ClientboundSetChunkCacheRadiusPacket> CLIENTBOUND_SET_CHUNK_CACHE_RADIUS = createClientbound("set_chunk_cache_radius");
/*  96 */   public static final PacketType<ClientboundSetDefaultSpawnPositionPacket> CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION = createClientbound("set_default_spawn_position");
/*  97 */   public static final PacketType<ClientboundSetDisplayObjectivePacket> CLIENTBOUND_SET_DISPLAY_OBJECTIVE = createClientbound("set_display_objective");
/*  98 */   public static final PacketType<ClientboundSetEntityDataPacket> CLIENTBOUND_SET_ENTITY_DATA = createClientbound("set_entity_data");
/*  99 */   public static final PacketType<ClientboundSetEntityLinkPacket> CLIENTBOUND_SET_ENTITY_LINK = createClientbound("set_entity_link");
/* 100 */   public static final PacketType<ClientboundSetEntityMotionPacket> CLIENTBOUND_SET_ENTITY_MOTION = createClientbound("set_entity_motion");
/* 101 */   public static final PacketType<ClientboundSetEquipmentPacket> CLIENTBOUND_SET_EQUIPMENT = createClientbound("set_equipment");
/* 102 */   public static final PacketType<ClientboundSetExperiencePacket> CLIENTBOUND_SET_EXPERIENCE = createClientbound("set_experience");
/* 103 */   public static final PacketType<ClientboundSetHealthPacket> CLIENTBOUND_SET_HEALTH = createClientbound("set_health");
/* 104 */   public static final PacketType<ClientboundSetHeldSlotPacket> CLIENTBOUND_SET_HELD_SLOT = createClientbound("set_held_slot");
/* 105 */   public static final PacketType<ClientboundSetObjectivePacket> CLIENTBOUND_SET_OBJECTIVE = createClientbound("set_objective");
/* 106 */   public static final PacketType<ClientboundSetPassengersPacket> CLIENTBOUND_SET_PASSENGERS = createClientbound("set_passengers");
/* 107 */   public static final PacketType<ClientboundSetPlayerTeamPacket> CLIENTBOUND_SET_PLAYER_TEAM = createClientbound("set_player_team");
/* 108 */   public static final PacketType<ClientboundSetScorePacket> CLIENTBOUND_SET_SCORE = createClientbound("set_score");
/* 109 */   public static final PacketType<ClientboundSetSimulationDistancePacket> CLIENTBOUND_SET_SIMULATION_DISTANCE = createClientbound("set_simulation_distance");
/* 110 */   public static final PacketType<ClientboundSetSubtitleTextPacket> CLIENTBOUND_SET_SUBTITLE_TEXT = createClientbound("set_subtitle_text");
/* 111 */   public static final PacketType<ClientboundSetTimePacket> CLIENTBOUND_SET_TIME = createClientbound("set_time");
/* 112 */   public static final PacketType<ClientboundSetTitleTextPacket> CLIENTBOUND_SET_TITLE_TEXT = createClientbound("set_title_text");
/* 113 */   public static final PacketType<ClientboundSetTitlesAnimationPacket> CLIENTBOUND_SET_TITLES_ANIMATION = createClientbound("set_titles_animation");
/* 114 */   public static final PacketType<ClientboundSoundEntityPacket> CLIENTBOUND_SOUND_ENTITY = createClientbound("sound_entity");
/* 115 */   public static final PacketType<ClientboundSoundPacket> CLIENTBOUND_SOUND = createClientbound("sound");
/* 116 */   public static final PacketType<ClientboundStartConfigurationPacket> CLIENTBOUND_START_CONFIGURATION = createClientbound("start_configuration");
/* 117 */   public static final PacketType<ClientboundStopSoundPacket> CLIENTBOUND_STOP_SOUND = createClientbound("stop_sound");
/* 118 */   public static final PacketType<ClientboundSystemChatPacket> CLIENTBOUND_SYSTEM_CHAT = createClientbound("system_chat");
/* 119 */   public static final PacketType<ClientboundTabListPacket> CLIENTBOUND_TAB_LIST = createClientbound("tab_list");
/* 120 */   public static final PacketType<ClientboundTagQueryPacket> CLIENTBOUND_TAG_QUERY = createClientbound("tag_query");
/* 121 */   public static final PacketType<ClientboundTakeItemEntityPacket> CLIENTBOUND_TAKE_ITEM_ENTITY = createClientbound("take_item_entity");
/* 122 */   public static final PacketType<ClientboundTeleportEntityPacket> CLIENTBOUND_TELEPORT_ENTITY = createClientbound("teleport_entity");
/* 123 */   public static final PacketType<ClientboundTestInstanceBlockStatus> CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS = createClientbound("test_instance_block_status");
/* 124 */   public static final PacketType<ClientboundUpdateAdvancementsPacket> CLIENTBOUND_UPDATE_ADVANCEMENTS = createClientbound("update_advancements");
/* 125 */   public static final PacketType<ClientboundUpdateAttributesPacket> CLIENTBOUND_UPDATE_ATTRIBUTES = createClientbound("update_attributes");
/* 126 */   public static final PacketType<ClientboundUpdateMobEffectPacket> CLIENTBOUND_UPDATE_MOB_EFFECT = createClientbound("update_mob_effect");
/* 127 */   public static final PacketType<ClientboundUpdateRecipesPacket> CLIENTBOUND_UPDATE_RECIPES = createClientbound("update_recipes");
/* 128 */   public static final PacketType<ClientboundProjectilePowerPacket> CLIENTBOUND_PROJECTILE_POWER = createClientbound("projectile_power");
/* 129 */   public static final PacketType<ClientboundTrackedWaypointPacket> CLIENTBOUND_WAYPOINT = createClientbound("waypoint");
/*     */   
/* 131 */   public static final PacketType<ServerboundAcceptTeleportationPacket> SERVERBOUND_ACCEPT_TELEPORTATION = createServerbound("accept_teleportation");
/* 132 */   public static final PacketType<ServerboundBlockEntityTagQueryPacket> SERVERBOUND_BLOCK_ENTITY_TAG_QUERY = createServerbound("block_entity_tag_query");
/* 133 */   public static final PacketType<ServerboundSelectBundleItemPacket> SERVERBOUND_BUNDLE_ITEM_SELECTED = createServerbound("bundle_item_selected");
/* 134 */   public static final PacketType<ServerboundChangeDifficultyPacket> SERVERBOUND_CHANGE_DIFFICULTY = createServerbound("change_difficulty");
/* 135 */   public static final PacketType<ServerboundChangeGameModePacket> SERVERBOUND_CHANGE_GAME_MODE = createServerbound("change_game_mode");
/* 136 */   public static final PacketType<ServerboundChatAckPacket> SERVERBOUND_CHAT_ACK = createServerbound("chat_ack");
/* 137 */   public static final PacketType<ServerboundChatCommandPacket> SERVERBOUND_CHAT_COMMAND = createServerbound("chat_command");
/* 138 */   public static final PacketType<ServerboundChatCommandSignedPacket> SERVERBOUND_CHAT_COMMAND_SIGNED = createServerbound("chat_command_signed");
/* 139 */   public static final PacketType<ServerboundChatPacket> SERVERBOUND_CHAT = createServerbound("chat");
/* 140 */   public static final PacketType<ServerboundChatSessionUpdatePacket> SERVERBOUND_CHAT_SESSION_UPDATE = createServerbound("chat_session_update");
/* 141 */   public static final PacketType<ServerboundChunkBatchReceivedPacket> SERVERBOUND_CHUNK_BATCH_RECEIVED = createServerbound("chunk_batch_received");
/* 142 */   public static final PacketType<ServerboundClientCommandPacket> SERVERBOUND_CLIENT_COMMAND = createServerbound("client_command");
/* 143 */   public static final PacketType<ServerboundClientTickEndPacket> SERVERBOUND_CLIENT_TICK_END = createServerbound("client_tick_end");
/* 144 */   public static final PacketType<ServerboundCommandSuggestionPacket> SERVERBOUND_COMMAND_SUGGESTION = createServerbound("command_suggestion");
/* 145 */   public static final PacketType<ServerboundConfigurationAcknowledgedPacket> SERVERBOUND_CONFIGURATION_ACKNOWLEDGED = createServerbound("configuration_acknowledged");
/* 146 */   public static final PacketType<ServerboundContainerButtonClickPacket> SERVERBOUND_CONTAINER_BUTTON_CLICK = createServerbound("container_button_click");
/* 147 */   public static final PacketType<ServerboundContainerClickPacket> SERVERBOUND_CONTAINER_CLICK = createServerbound("container_click");
/* 148 */   public static final PacketType<ServerboundContainerClosePacket> SERVERBOUND_CONTAINER_CLOSE = createServerbound("container_close");
/* 149 */   public static final PacketType<ServerboundContainerSlotStateChangedPacket> SERVERBOUND_CONTAINER_SLOT_STATE_CHANGED = createServerbound("container_slot_state_changed");
/* 150 */   public static final PacketType<ServerboundDebugSubscriptionRequestPacket> SERVERBOUND_DEBUG_SUBSCRIPTION_REQUEST = createServerbound("debug_subscription_request");
/* 151 */   public static final PacketType<ServerboundEditBookPacket> SERVERBOUND_EDIT_BOOK = createServerbound("edit_book");
/* 152 */   public static final PacketType<ServerboundEntityTagQueryPacket> SERVERBOUND_ENTITY_TAG_QUERY = createServerbound("entity_tag_query");
/* 153 */   public static final PacketType<ServerboundInteractPacket> SERVERBOUND_INTERACT = createServerbound("interact");
/* 154 */   public static final PacketType<ServerboundJigsawGeneratePacket> SERVERBOUND_JIGSAW_GENERATE = createServerbound("jigsaw_generate");
/* 155 */   public static final PacketType<ServerboundLockDifficultyPacket> SERVERBOUND_LOCK_DIFFICULTY = createServerbound("lock_difficulty");
/* 156 */   public static final PacketType<ServerboundMovePlayerPacket.Pos> SERVERBOUND_MOVE_PLAYER_POS = createServerbound("move_player_pos");
/* 157 */   public static final PacketType<ServerboundMovePlayerPacket.PosRot> SERVERBOUND_MOVE_PLAYER_POS_ROT = createServerbound("move_player_pos_rot");
/* 158 */   public static final PacketType<ServerboundMovePlayerPacket.Rot> SERVERBOUND_MOVE_PLAYER_ROT = createServerbound("move_player_rot");
/* 159 */   public static final PacketType<ServerboundMovePlayerPacket.StatusOnly> SERVERBOUND_MOVE_PLAYER_STATUS_ONLY = createServerbound("move_player_status_only");
/* 160 */   public static final PacketType<ServerboundMoveVehiclePacket> SERVERBOUND_MOVE_VEHICLE = createServerbound("move_vehicle");
/* 161 */   public static final PacketType<ServerboundPaddleBoatPacket> SERVERBOUND_PADDLE_BOAT = createServerbound("paddle_boat");
/* 162 */   public static final PacketType<ServerboundPickItemFromBlockPacket> SERVERBOUND_PICK_ITEM_FROM_BLOCK = createServerbound("pick_item_from_block");
/* 163 */   public static final PacketType<ServerboundPickItemFromEntityPacket> SERVERBOUND_PICK_ITEM_FROM_ENTITY = createServerbound("pick_item_from_entity");
/* 164 */   public static final PacketType<ServerboundPlaceRecipePacket> SERVERBOUND_PLACE_RECIPE = createServerbound("place_recipe");
/* 165 */   public static final PacketType<ServerboundPlayerAbilitiesPacket> SERVERBOUND_PLAYER_ABILITIES = createServerbound("player_abilities");
/* 166 */   public static final PacketType<ServerboundPlayerActionPacket> SERVERBOUND_PLAYER_ACTION = createServerbound("player_action");
/* 167 */   public static final PacketType<ServerboundPlayerCommandPacket> SERVERBOUND_PLAYER_COMMAND = createServerbound("player_command");
/* 168 */   public static final PacketType<ServerboundPlayerInputPacket> SERVERBOUND_PLAYER_INPUT = createServerbound("player_input");
/* 169 */   public static final PacketType<ServerboundPlayerLoadedPacket> SERVERBOUND_PLAYER_LOADED = createServerbound("player_loaded");
/* 170 */   public static final PacketType<ServerboundRecipeBookChangeSettingsPacket> SERVERBOUND_RECIPE_BOOK_CHANGE_SETTINGS = createServerbound("recipe_book_change_settings");
/* 171 */   public static final PacketType<ServerboundRecipeBookSeenRecipePacket> SERVERBOUND_RECIPE_BOOK_SEEN_RECIPE = createServerbound("recipe_book_seen_recipe");
/* 172 */   public static final PacketType<ServerboundRenameItemPacket> SERVERBOUND_RENAME_ITEM = createServerbound("rename_item");
/* 173 */   public static final PacketType<ServerboundSeenAdvancementsPacket> SERVERBOUND_SEEN_ADVANCEMENTS = createServerbound("seen_advancements");
/* 174 */   public static final PacketType<ServerboundSelectTradePacket> SERVERBOUND_SELECT_TRADE = createServerbound("select_trade");
/* 175 */   public static final PacketType<ServerboundSetBeaconPacket> SERVERBOUND_SET_BEACON = createServerbound("set_beacon");
/* 176 */   public static final PacketType<ServerboundSetCarriedItemPacket> SERVERBOUND_SET_CARRIED_ITEM = createServerbound("set_carried_item");
/* 177 */   public static final PacketType<ServerboundSetCommandBlockPacket> SERVERBOUND_SET_COMMAND_BLOCK = createServerbound("set_command_block");
/* 178 */   public static final PacketType<ServerboundSetCommandMinecartPacket> SERVERBOUND_SET_COMMAND_MINECART = createServerbound("set_command_minecart");
/* 179 */   public static final PacketType<ServerboundSetCreativeModeSlotPacket> SERVERBOUND_SET_CREATIVE_MODE_SLOT = createServerbound("set_creative_mode_slot");
/* 180 */   public static final PacketType<ServerboundSetJigsawBlockPacket> SERVERBOUND_SET_JIGSAW_BLOCK = createServerbound("set_jigsaw_block");
/* 181 */   public static final PacketType<ServerboundSetStructureBlockPacket> SERVERBOUND_SET_STRUCTURE_BLOCK = createServerbound("set_structure_block");
/* 182 */   public static final PacketType<ServerboundSetTestBlockPacket> SERVERBOUND_SET_TEST_BLOCK = createServerbound("set_test_block");
/* 183 */   public static final PacketType<ServerboundTestInstanceBlockActionPacket> SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION = createServerbound("test_instance_block_action");
/* 184 */   public static final PacketType<ServerboundSignUpdatePacket> SERVERBOUND_SIGN_UPDATE = createServerbound("sign_update");
/* 185 */   public static final PacketType<ServerboundSwingPacket> SERVERBOUND_SWING = createServerbound("swing");
/* 186 */   public static final PacketType<ServerboundTeleportToEntityPacket> SERVERBOUND_TELEPORT_TO_ENTITY = createServerbound("teleport_to_entity");
/* 187 */   public static final PacketType<ServerboundUseItemOnPacket> SERVERBOUND_USE_ITEM_ON = createServerbound("use_item_on");
/* 188 */   public static final PacketType<ServerboundUseItemPacket> SERVERBOUND_USE_ITEM = createServerbound("use_item");
/*     */   
/* 190 */   public static final PacketType<ClientboundResetScorePacket> CLIENTBOUND_RESET_SCORE = createClientbound("reset_score");
/* 191 */   public static final PacketType<ClientboundTickingStatePacket> CLIENTBOUND_TICKING_STATE = createClientbound("ticking_state");
/* 192 */   public static final PacketType<ClientboundTickingStepPacket> CLIENTBOUND_TICKING_STEP = createClientbound("ticking_step");
/* 193 */   public static final PacketType<ClientboundSetCursorItemPacket> CLIENTBOUND_SET_CURSOR_ITEM = createClientbound("set_cursor_item");
/* 194 */   public static final PacketType<ClientboundSetPlayerInventoryPacket> CLIENTBOUND_SET_PLAYER_INVENTORY = createClientbound("set_player_inventory");
/*     */   
/*     */   private static <T extends net.minecraft.network.protocol.Packet<ClientGamePacketListener>> PacketType<T> createClientbound(String paramString) {
/* 197 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.network.protocol.Packet<ServerGamePacketListener>> PacketType<T> createServerbound(String paramString) {
/* 201 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\GamePacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */