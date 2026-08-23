/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ public interface StructureTags
/*    */ {
/*  9 */   public static final TagKey<Structure> EYE_OF_ENDER_LOCATED = create("eye_of_ender_located");
/* 10 */   public static final TagKey<Structure> DOLPHIN_LOCATED = create("dolphin_located");
/* 11 */   public static final TagKey<Structure> ON_WOODLAND_EXPLORER_MAPS = create("on_woodland_explorer_maps");
/* 12 */   public static final TagKey<Structure> ON_OCEAN_EXPLORER_MAPS = create("on_ocean_explorer_maps");
/* 13 */   public static final TagKey<Structure> ON_SAVANNA_VILLAGE_MAPS = create("on_savanna_village_maps");
/* 14 */   public static final TagKey<Structure> ON_DESERT_VILLAGE_MAPS = create("on_desert_village_maps");
/* 15 */   public static final TagKey<Structure> ON_PLAINS_VILLAGE_MAPS = create("on_plains_village_maps");
/* 16 */   public static final TagKey<Structure> ON_TAIGA_VILLAGE_MAPS = create("on_taiga_village_maps");
/* 17 */   public static final TagKey<Structure> ON_SNOWY_VILLAGE_MAPS = create("on_snowy_village_maps");
/*    */   
/* 19 */   public static final TagKey<Structure> ON_JUNGLE_EXPLORER_MAPS = create("on_jungle_explorer_maps");
/*    */   
/* 21 */   public static final TagKey<Structure> ON_SWAMP_EXPLORER_MAPS = create("on_swamp_explorer_maps");
/* 22 */   public static final TagKey<Structure> ON_TREASURE_MAPS = create("on_treasure_maps");
/* 23 */   public static final TagKey<Structure> ON_TRIAL_CHAMBERS_MAPS = create("on_trial_chambers_maps");
/*    */ 
/*    */   
/* 26 */   public static final TagKey<Structure> CATS_SPAWN_IN = create("cats_spawn_in");
/* 27 */   public static final TagKey<Structure> CATS_SPAWN_AS_BLACK = create("cats_spawn_as_black");
/*    */ 
/*    */   
/* 30 */   public static final TagKey<Structure> VILLAGE = create("village");
/* 31 */   public static final TagKey<Structure> MINESHAFT = create("mineshaft");
/* 32 */   public static final TagKey<Structure> SHIPWRECK = create("shipwreck");
/* 33 */   public static final TagKey<Structure> RUINED_PORTAL = create("ruined_portal");
/* 34 */   public static final TagKey<Structure> OCEAN_RUIN = create("ocean_ruin");
/*    */   
/*    */   private static TagKey<Structure> create(String paramString) {
/* 37 */     return TagKey.create(Registries.STRUCTURE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\StructureTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */