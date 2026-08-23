/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ 
/*    */ public class References {
/*    */   public static DSL.TypeReference reference(final String id) {
/*  7 */     return new DSL.TypeReference()
/*    */       {
/*    */         public String typeName() {
/* 10 */           return id;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 15 */           return "@" + id;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 21 */   public static final DSL.TypeReference LEVEL = reference("level");
/*    */   
/* 23 */   public static final DSL.TypeReference LIGHTWEIGHT_LEVEL = reference("lightweight_level");
/* 24 */   public static final DSL.TypeReference PLAYER = reference("player");
/* 25 */   public static final DSL.TypeReference CHUNK = reference("chunk");
/* 26 */   public static final DSL.TypeReference HOTBAR = reference("hotbar");
/* 27 */   public static final DSL.TypeReference OPTIONS = reference("options");
/* 28 */   public static final DSL.TypeReference STRUCTURE = reference("structure");
/* 29 */   public static final DSL.TypeReference STATS = reference("stats");
/* 30 */   public static final DSL.TypeReference SAVED_DATA_COMMAND_STORAGE = reference("saved_data/command_storage");
/* 31 */   public static final DSL.TypeReference SAVED_DATA_TICKETS = reference("saved_data/tickets");
/* 32 */   public static final DSL.TypeReference SAVED_DATA_MAP_DATA = reference("saved_data/map_data");
/* 33 */   public static final DSL.TypeReference SAVED_DATA_MAP_INDEX = reference("saved_data/idcounts");
/* 34 */   public static final DSL.TypeReference SAVED_DATA_RAIDS = reference("saved_data/raids");
/* 35 */   public static final DSL.TypeReference SAVED_DATA_RANDOM_SEQUENCES = reference("saved_data/random_sequences");
/* 36 */   public static final DSL.TypeReference SAVED_DATA_SCOREBOARD = reference("saved_data/scoreboard");
/* 37 */   public static final DSL.TypeReference SAVED_DATA_STOPWATCHES = reference("saved_data/stopwatches");
/* 38 */   public static final DSL.TypeReference SAVED_DATA_STRUCTURE_FEATURE_INDICES = reference("saved_data/structure_feature_indices");
/* 39 */   public static final DSL.TypeReference SAVED_DATA_WORLD_BORDER = reference("saved_data/world_border");
/* 40 */   public static final DSL.TypeReference ADVANCEMENTS = reference("advancements");
/* 41 */   public static final DSL.TypeReference POI_CHUNK = reference("poi_chunk");
/* 42 */   public static final DSL.TypeReference ENTITY_CHUNK = reference("entity_chunk");
/* 43 */   public static final DSL.TypeReference DEBUG_PROFILE = reference("debug_profile");
/*    */ 
/*    */   
/* 46 */   public static final DSL.TypeReference BLOCK_ENTITY = reference("block_entity");
/* 47 */   public static final DSL.TypeReference ITEM_STACK = reference("item_stack");
/* 48 */   public static final DSL.TypeReference BLOCK_STATE = reference("block_state");
/*    */ 
/*    */   
/* 51 */   public static final DSL.TypeReference FLAT_BLOCK_STATE = reference("flat_block_state");
/* 52 */   public static final DSL.TypeReference DATA_COMPONENTS = reference("data_components");
/* 53 */   public static final DSL.TypeReference VILLAGER_TRADE = reference("villager_trade");
/* 54 */   public static final DSL.TypeReference PARTICLE = reference("particle");
/* 55 */   public static final DSL.TypeReference TEXT_COMPONENT = reference("text_component");
/* 56 */   public static final DSL.TypeReference ENTITY_EQUIPMENT = reference("entity_equipment");
/*    */ 
/*    */   
/* 59 */   public static final DSL.TypeReference ENTITY_NAME = reference("entity_name");
/*    */ 
/*    */   
/* 62 */   public static final DSL.TypeReference ENTITY_TREE = reference("entity_tree");
/*    */   
/* 64 */   public static final DSL.TypeReference ENTITY = reference("entity");
/*    */ 
/*    */   
/* 67 */   public static final DSL.TypeReference BLOCK_NAME = reference("block_name");
/* 68 */   public static final DSL.TypeReference ITEM_NAME = reference("item_name");
/* 69 */   public static final DSL.TypeReference GAME_EVENT_NAME = reference("game_event_name");
/*    */ 
/*    */   
/* 72 */   public static final DSL.TypeReference UNTAGGED_SPAWNER = reference("untagged_spawner");
/*    */ 
/*    */   
/* 75 */   public static final DSL.TypeReference STRUCTURE_FEATURE = reference("structure_feature");
/*    */ 
/*    */   
/* 78 */   public static final DSL.TypeReference OBJECTIVE = reference("objective");
/*    */ 
/*    */   
/* 81 */   public static final DSL.TypeReference TEAM = reference("team");
/*    */ 
/*    */   
/* 84 */   public static final DSL.TypeReference RECIPE = reference("recipe");
/*    */ 
/*    */   
/* 87 */   public static final DSL.TypeReference BIOME = reference("biome");
/*    */ 
/*    */   
/* 90 */   public static final DSL.TypeReference MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST = reference("multi_noise_biome_source_parameter_list");
/*    */ 
/*    */   
/* 93 */   public static final DSL.TypeReference WORLD_GEN_SETTINGS = reference("world_gen_settings");
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\References.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */