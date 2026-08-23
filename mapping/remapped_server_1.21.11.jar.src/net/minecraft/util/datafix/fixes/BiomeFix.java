/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class BiomeFix
/*    */ {
/*  8 */   public static final Map<String, String> BIOMES = (Map<String, String>)ImmutableMap.builder()
/*  9 */     .put("minecraft:extreme_hills", "minecraft:mountains")
/* 10 */     .put("minecraft:swampland", "minecraft:swamp")
/* 11 */     .put("minecraft:hell", "minecraft:nether_wastes")
/* 12 */     .put("minecraft:sky", "minecraft:the_end")
/* 13 */     .put("minecraft:ice_flats", "minecraft:snowy_tundra")
/* 14 */     .put("minecraft:ice_mountains", "minecraft:snowy_mountains")
/* 15 */     .put("minecraft:mushroom_island", "minecraft:mushroom_fields")
/* 16 */     .put("minecraft:mushroom_island_shore", "minecraft:mushroom_field_shore")
/* 17 */     .put("minecraft:beaches", "minecraft:beach")
/* 18 */     .put("minecraft:forest_hills", "minecraft:wooded_hills")
/* 19 */     .put("minecraft:smaller_extreme_hills", "minecraft:mountain_edge")
/* 20 */     .put("minecraft:stone_beach", "minecraft:stone_shore")
/* 21 */     .put("minecraft:cold_beach", "minecraft:snowy_beach")
/* 22 */     .put("minecraft:roofed_forest", "minecraft:dark_forest")
/* 23 */     .put("minecraft:taiga_cold", "minecraft:snowy_taiga")
/* 24 */     .put("minecraft:taiga_cold_hills", "minecraft:snowy_taiga_hills")
/* 25 */     .put("minecraft:redwood_taiga", "minecraft:giant_tree_taiga")
/* 26 */     .put("minecraft:redwood_taiga_hills", "minecraft:giant_tree_taiga_hills")
/* 27 */     .put("minecraft:extreme_hills_with_trees", "minecraft:wooded_mountains")
/* 28 */     .put("minecraft:savanna_rock", "minecraft:savanna_plateau")
/* 29 */     .put("minecraft:mesa", "minecraft:badlands")
/* 30 */     .put("minecraft:mesa_rock", "minecraft:wooded_badlands_plateau")
/* 31 */     .put("minecraft:mesa_clear_rock", "minecraft:badlands_plateau")
/* 32 */     .put("minecraft:sky_island_low", "minecraft:small_end_islands")
/* 33 */     .put("minecraft:sky_island_medium", "minecraft:end_midlands")
/* 34 */     .put("minecraft:sky_island_high", "minecraft:end_highlands")
/* 35 */     .put("minecraft:sky_island_barren", "minecraft:end_barrens")
/* 36 */     .put("minecraft:void", "minecraft:the_void")
/* 37 */     .put("minecraft:mutated_plains", "minecraft:sunflower_plains")
/* 38 */     .put("minecraft:mutated_desert", "minecraft:desert_lakes")
/* 39 */     .put("minecraft:mutated_extreme_hills", "minecraft:gravelly_mountains")
/* 40 */     .put("minecraft:mutated_forest", "minecraft:flower_forest")
/* 41 */     .put("minecraft:mutated_taiga", "minecraft:taiga_mountains")
/* 42 */     .put("minecraft:mutated_swampland", "minecraft:swamp_hills")
/* 43 */     .put("minecraft:mutated_ice_flats", "minecraft:ice_spikes")
/* 44 */     .put("minecraft:mutated_jungle", "minecraft:modified_jungle")
/* 45 */     .put("minecraft:mutated_jungle_edge", "minecraft:modified_jungle_edge")
/* 46 */     .put("minecraft:mutated_birch_forest", "minecraft:tall_birch_forest")
/* 47 */     .put("minecraft:mutated_birch_forest_hills", "minecraft:tall_birch_hills")
/* 48 */     .put("minecraft:mutated_roofed_forest", "minecraft:dark_forest_hills")
/* 49 */     .put("minecraft:mutated_taiga_cold", "minecraft:snowy_taiga_mountains")
/* 50 */     .put("minecraft:mutated_redwood_taiga", "minecraft:giant_spruce_taiga")
/* 51 */     .put("minecraft:mutated_redwood_taiga_hills", "minecraft:giant_spruce_taiga_hills")
/* 52 */     .put("minecraft:mutated_extreme_hills_with_trees", "minecraft:modified_gravelly_mountains")
/* 53 */     .put("minecraft:mutated_savanna", "minecraft:shattered_savanna")
/* 54 */     .put("minecraft:mutated_savanna_rock", "minecraft:shattered_savanna_plateau")
/* 55 */     .put("minecraft:mutated_mesa", "minecraft:eroded_badlands")
/* 56 */     .put("minecraft:mutated_mesa_rock", "minecraft:modified_wooded_badlands_plateau")
/* 57 */     .put("minecraft:mutated_mesa_clear_rock", "minecraft:modified_badlands_plateau")
/*    */     
/* 59 */     .put("minecraft:warm_deep_ocean", "minecraft:deep_warm_ocean")
/* 60 */     .put("minecraft:lukewarm_deep_ocean", "minecraft:deep_lukewarm_ocean")
/* 61 */     .put("minecraft:cold_deep_ocean", "minecraft:deep_cold_ocean")
/* 62 */     .put("minecraft:frozen_deep_ocean", "minecraft:deep_frozen_ocean")
/* 63 */     .build();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */