/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.material.MapColor;
/*    */ 
/*    */ public class MapDecorationTypes
/*    */ {
/*    */   private static final int COPPER_COLOR = 12741452;
/* 14 */   public static final Holder<MapDecorationType> PLAYER = register("player", "player", false, true);
/* 15 */   public static final Holder<MapDecorationType> FRAME = register("frame", "frame", true, true);
/* 16 */   public static final Holder<MapDecorationType> RED_MARKER = register("red_marker", "red_marker", false, true);
/* 17 */   public static final Holder<MapDecorationType> BLUE_MARKER = register("blue_marker", "blue_marker", false, true);
/* 18 */   public static final Holder<MapDecorationType> TARGET_X = register("target_x", "target_x", true, false);
/* 19 */   public static final Holder<MapDecorationType> TARGET_POINT = register("target_point", "target_point", true, false);
/* 20 */   public static final Holder<MapDecorationType> PLAYER_OFF_MAP = register("player_off_map", "player_off_map", false, true);
/* 21 */   public static final Holder<MapDecorationType> PLAYER_OFF_LIMITS = register("player_off_limits", "player_off_limits", false, true);
/* 22 */   public static final Holder<MapDecorationType> WOODLAND_MANSION = register("mansion", "woodland_mansion", true, 5393476, false, true);
/* 23 */   public static final Holder<MapDecorationType> OCEAN_MONUMENT = register("monument", "ocean_monument", true, 3830373, false, true);
/* 24 */   public static final Holder<MapDecorationType> WHITE_BANNER = register("banner_white", "white_banner", true, true);
/* 25 */   public static final Holder<MapDecorationType> ORANGE_BANNER = register("banner_orange", "orange_banner", true, true);
/* 26 */   public static final Holder<MapDecorationType> MAGENTA_BANNER = register("banner_magenta", "magenta_banner", true, true);
/* 27 */   public static final Holder<MapDecorationType> LIGHT_BLUE_BANNER = register("banner_light_blue", "light_blue_banner", true, true);
/* 28 */   public static final Holder<MapDecorationType> YELLOW_BANNER = register("banner_yellow", "yellow_banner", true, true);
/* 29 */   public static final Holder<MapDecorationType> LIME_BANNER = register("banner_lime", "lime_banner", true, true);
/* 30 */   public static final Holder<MapDecorationType> PINK_BANNER = register("banner_pink", "pink_banner", true, true);
/* 31 */   public static final Holder<MapDecorationType> GRAY_BANNER = register("banner_gray", "gray_banner", true, true);
/* 32 */   public static final Holder<MapDecorationType> LIGHT_GRAY_BANNER = register("banner_light_gray", "light_gray_banner", true, true);
/* 33 */   public static final Holder<MapDecorationType> CYAN_BANNER = register("banner_cyan", "cyan_banner", true, true);
/* 34 */   public static final Holder<MapDecorationType> PURPLE_BANNER = register("banner_purple", "purple_banner", true, true);
/* 35 */   public static final Holder<MapDecorationType> BLUE_BANNER = register("banner_blue", "blue_banner", true, true);
/* 36 */   public static final Holder<MapDecorationType> BROWN_BANNER = register("banner_brown", "brown_banner", true, true);
/* 37 */   public static final Holder<MapDecorationType> GREEN_BANNER = register("banner_green", "green_banner", true, true);
/* 38 */   public static final Holder<MapDecorationType> RED_BANNER = register("banner_red", "red_banner", true, true);
/* 39 */   public static final Holder<MapDecorationType> BLACK_BANNER = register("banner_black", "black_banner", true, true);
/* 40 */   public static final Holder<MapDecorationType> RED_X = register("red_x", "red_x", true, false);
/* 41 */   public static final Holder<MapDecorationType> DESERT_VILLAGE = register("village_desert", "desert_village", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 42 */   public static final Holder<MapDecorationType> PLAINS_VILLAGE = register("village_plains", "plains_village", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 43 */   public static final Holder<MapDecorationType> SAVANNA_VILLAGE = register("village_savanna", "savanna_village", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 44 */   public static final Holder<MapDecorationType> SNOWY_VILLAGE = register("village_snowy", "snowy_village", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 45 */   public static final Holder<MapDecorationType> TAIGA_VILLAGE = register("village_taiga", "taiga_village", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 46 */   public static final Holder<MapDecorationType> JUNGLE_TEMPLE = register("jungle_temple", "jungle_temple", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 47 */   public static final Holder<MapDecorationType> SWAMP_HUT = register("swamp_hut", "swamp_hut", true, MapColor.COLOR_LIGHT_GRAY.col, false, true);
/* 48 */   public static final Holder<MapDecorationType> TRIAL_CHAMBERS = register("trial_chambers", "trial_chambers", true, 12741452, false, true);
/*    */   
/*    */   public static Holder<MapDecorationType> bootstrap(Registry<MapDecorationType> paramRegistry) {
/* 51 */     return PLAYER;
/*    */   }
/*    */   
/*    */   private static Holder<MapDecorationType> register(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
/* 55 */     return register(paramString1, paramString2, paramBoolean1, -1, paramBoolean2, false);
/*    */   }
/*    */   
/*    */   private static Holder<MapDecorationType> register(String paramString1, String paramString2, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3) {
/* 59 */     ResourceKey resourceKey = ResourceKey.create(Registries.MAP_DECORATION_TYPE, Identifier.withDefaultNamespace(paramString1));
/* 60 */     MapDecorationType mapDecorationType = new MapDecorationType(Identifier.withDefaultNamespace(paramString2), paramBoolean1, paramInt, paramBoolean3, paramBoolean2);
/* 61 */     return (Holder<MapDecorationType>)Registry.registerForHolder(BuiltInRegistries.MAP_DECORATION_TYPE, resourceKey, mapDecorationType);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\saveddata\maps\MapDecorationTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */