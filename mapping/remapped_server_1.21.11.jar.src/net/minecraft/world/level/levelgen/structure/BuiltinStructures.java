/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface BuiltinStructures {
/*  8 */   public static final ResourceKey<Structure> PILLAGER_OUTPOST = createKey("pillager_outpost");
/*  9 */   public static final ResourceKey<Structure> MINESHAFT = createKey("mineshaft");
/* 10 */   public static final ResourceKey<Structure> MINESHAFT_MESA = createKey("mineshaft_mesa");
/* 11 */   public static final ResourceKey<Structure> WOODLAND_MANSION = createKey("mansion");
/* 12 */   public static final ResourceKey<Structure> JUNGLE_TEMPLE = createKey("jungle_pyramid");
/* 13 */   public static final ResourceKey<Structure> DESERT_PYRAMID = createKey("desert_pyramid");
/* 14 */   public static final ResourceKey<Structure> IGLOO = createKey("igloo");
/* 15 */   public static final ResourceKey<Structure> SHIPWRECK = createKey("shipwreck");
/* 16 */   public static final ResourceKey<Structure> SHIPWRECK_BEACHED = createKey("shipwreck_beached");
/* 17 */   public static final ResourceKey<Structure> SWAMP_HUT = createKey("swamp_hut");
/* 18 */   public static final ResourceKey<Structure> STRONGHOLD = createKey("stronghold");
/* 19 */   public static final ResourceKey<Structure> OCEAN_MONUMENT = createKey("monument");
/* 20 */   public static final ResourceKey<Structure> OCEAN_RUIN_COLD = createKey("ocean_ruin_cold");
/* 21 */   public static final ResourceKey<Structure> OCEAN_RUIN_WARM = createKey("ocean_ruin_warm");
/* 22 */   public static final ResourceKey<Structure> FORTRESS = createKey("fortress");
/* 23 */   public static final ResourceKey<Structure> NETHER_FOSSIL = createKey("nether_fossil");
/* 24 */   public static final ResourceKey<Structure> END_CITY = createKey("end_city");
/* 25 */   public static final ResourceKey<Structure> BURIED_TREASURE = createKey("buried_treasure");
/* 26 */   public static final ResourceKey<Structure> BASTION_REMNANT = createKey("bastion_remnant");
/*    */   
/* 28 */   public static final ResourceKey<Structure> VILLAGE_PLAINS = createKey("village_plains");
/* 29 */   public static final ResourceKey<Structure> VILLAGE_DESERT = createKey("village_desert");
/* 30 */   public static final ResourceKey<Structure> VILLAGE_SAVANNA = createKey("village_savanna");
/* 31 */   public static final ResourceKey<Structure> VILLAGE_SNOWY = createKey("village_snowy");
/* 32 */   public static final ResourceKey<Structure> VILLAGE_TAIGA = createKey("village_taiga");
/*    */   
/* 34 */   public static final ResourceKey<Structure> RUINED_PORTAL_STANDARD = createKey("ruined_portal");
/* 35 */   public static final ResourceKey<Structure> RUINED_PORTAL_DESERT = createKey("ruined_portal_desert");
/* 36 */   public static final ResourceKey<Structure> RUINED_PORTAL_JUNGLE = createKey("ruined_portal_jungle");
/* 37 */   public static final ResourceKey<Structure> RUINED_PORTAL_SWAMP = createKey("ruined_portal_swamp");
/* 38 */   public static final ResourceKey<Structure> RUINED_PORTAL_MOUNTAIN = createKey("ruined_portal_mountain");
/* 39 */   public static final ResourceKey<Structure> RUINED_PORTAL_OCEAN = createKey("ruined_portal_ocean");
/* 40 */   public static final ResourceKey<Structure> RUINED_PORTAL_NETHER = createKey("ruined_portal_nether");
/*    */   
/* 42 */   public static final ResourceKey<Structure> ANCIENT_CITY = createKey("ancient_city");
/* 43 */   public static final ResourceKey<Structure> TRAIL_RUINS = createKey("trail_ruins");
/* 44 */   public static final ResourceKey<Structure> TRIAL_CHAMBERS = createKey("trial_chambers");
/*    */   
/*    */   private static ResourceKey<Structure> createKey(String paramString) {
/* 47 */     return ResourceKey.create(Registries.STRUCTURE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\BuiltinStructures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */