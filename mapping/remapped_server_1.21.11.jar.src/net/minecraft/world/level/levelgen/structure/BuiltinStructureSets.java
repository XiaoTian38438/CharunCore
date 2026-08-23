/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface BuiltinStructureSets {
/*  8 */   public static final ResourceKey<StructureSet> VILLAGES = register("villages");
/*  9 */   public static final ResourceKey<StructureSet> DESERT_PYRAMIDS = register("desert_pyramids");
/* 10 */   public static final ResourceKey<StructureSet> IGLOOS = register("igloos");
/* 11 */   public static final ResourceKey<StructureSet> JUNGLE_TEMPLES = register("jungle_temples");
/* 12 */   public static final ResourceKey<StructureSet> SWAMP_HUTS = register("swamp_huts");
/* 13 */   public static final ResourceKey<StructureSet> PILLAGER_OUTPOSTS = register("pillager_outposts");
/* 14 */   public static final ResourceKey<StructureSet> OCEAN_MONUMENTS = register("ocean_monuments");
/* 15 */   public static final ResourceKey<StructureSet> WOODLAND_MANSIONS = register("woodland_mansions");
/* 16 */   public static final ResourceKey<StructureSet> BURIED_TREASURES = register("buried_treasures");
/* 17 */   public static final ResourceKey<StructureSet> MINESHAFTS = register("mineshafts");
/* 18 */   public static final ResourceKey<StructureSet> RUINED_PORTALS = register("ruined_portals");
/* 19 */   public static final ResourceKey<StructureSet> SHIPWRECKS = register("shipwrecks");
/* 20 */   public static final ResourceKey<StructureSet> OCEAN_RUINS = register("ocean_ruins");
/* 21 */   public static final ResourceKey<StructureSet> NETHER_COMPLEXES = register("nether_complexes");
/* 22 */   public static final ResourceKey<StructureSet> NETHER_FOSSILS = register("nether_fossils");
/* 23 */   public static final ResourceKey<StructureSet> END_CITIES = register("end_cities");
/* 24 */   public static final ResourceKey<StructureSet> ANCIENT_CITIES = register("ancient_cities");
/* 25 */   public static final ResourceKey<StructureSet> STRONGHOLDS = register("strongholds");
/* 26 */   public static final ResourceKey<StructureSet> TRAIL_RUINS = register("trail_ruins");
/* 27 */   public static final ResourceKey<StructureSet> TRIAL_CHAMBERS = register("trial_chambers");
/*    */   
/*    */   private static ResourceKey<StructureSet> register(String paramString) {
/* 30 */     return ResourceKey.create(Registries.STRUCTURE_SET, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\BuiltinStructureSets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */