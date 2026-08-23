/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ 
/*    */ public class LevelResource
/*    */ {
/*  6 */   public static final LevelResource PLAYER_ADVANCEMENTS_DIR = new LevelResource("advancements");
/*  7 */   public static final LevelResource PLAYER_STATS_DIR = new LevelResource("stats");
/*  8 */   public static final LevelResource PLAYER_DATA_DIR = new LevelResource("playerdata");
/*  9 */   public static final LevelResource PLAYER_OLD_DATA_DIR = new LevelResource("players");
/* 10 */   public static final LevelResource LEVEL_DATA_FILE = new LevelResource("level.dat");
/* 11 */   public static final LevelResource OLD_LEVEL_DATA_FILE = new LevelResource("level.dat_old");
/* 12 */   public static final LevelResource ICON_FILE = new LevelResource("icon.png");
/* 13 */   public static final LevelResource LOCK_FILE = new LevelResource("session.lock");
/* 14 */   public static final LevelResource GENERATED_DIR = new LevelResource("generated");
/* 15 */   public static final LevelResource DATAPACK_DIR = new LevelResource("datapacks");
/* 16 */   public static final LevelResource MAP_RESOURCE_FILE = new LevelResource("resources.zip");
/* 17 */   public static final LevelResource ROOT = new LevelResource(".");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private LevelResource(String paramString) {
/* 22 */     this.id = paramString;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 26 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return "/" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\LevelResource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */