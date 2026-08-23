/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasureStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.EndCityStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.IglooStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.JungleTempleStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.NetherFossilStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.ShipwreckStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.SwampHutStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
/*    */ 
/*    */ public interface StructureType<S extends Structure> {
/* 24 */   public static final StructureType<BuriedTreasureStructure> BURIED_TREASURE = register("buried_treasure", BuriedTreasureStructure.CODEC);
/* 25 */   public static final StructureType<DesertPyramidStructure> DESERT_PYRAMID = register("desert_pyramid", DesertPyramidStructure.CODEC);
/* 26 */   public static final StructureType<EndCityStructure> END_CITY = register("end_city", EndCityStructure.CODEC);
/* 27 */   public static final StructureType<NetherFortressStructure> FORTRESS = register("fortress", NetherFortressStructure.CODEC);
/* 28 */   public static final StructureType<IglooStructure> IGLOO = register("igloo", IglooStructure.CODEC);
/* 29 */   public static final StructureType<JigsawStructure> JIGSAW = register("jigsaw", JigsawStructure.CODEC);
/* 30 */   public static final StructureType<JungleTempleStructure> JUNGLE_TEMPLE = register("jungle_temple", JungleTempleStructure.CODEC);
/* 31 */   public static final StructureType<MineshaftStructure> MINESHAFT = register("mineshaft", MineshaftStructure.CODEC);
/* 32 */   public static final StructureType<NetherFossilStructure> NETHER_FOSSIL = register("nether_fossil", NetherFossilStructure.CODEC);
/* 33 */   public static final StructureType<OceanMonumentStructure> OCEAN_MONUMENT = register("ocean_monument", OceanMonumentStructure.CODEC);
/* 34 */   public static final StructureType<OceanRuinStructure> OCEAN_RUIN = register("ocean_ruin", OceanRuinStructure.CODEC);
/* 35 */   public static final StructureType<RuinedPortalStructure> RUINED_PORTAL = register("ruined_portal", RuinedPortalStructure.CODEC);
/* 36 */   public static final StructureType<ShipwreckStructure> SHIPWRECK = register("shipwreck", ShipwreckStructure.CODEC);
/* 37 */   public static final StructureType<StrongholdStructure> STRONGHOLD = register("stronghold", StrongholdStructure.CODEC);
/* 38 */   public static final StructureType<SwampHutStructure> SWAMP_HUT = register("swamp_hut", SwampHutStructure.CODEC);
/* 39 */   public static final StructureType<WoodlandMansionStructure> WOODLAND_MANSION = register("woodland_mansion", WoodlandMansionStructure.CODEC);
/*    */ 
/*    */   
/*    */   private static <S extends Structure> StructureType<S> register(String paramString, MapCodec<S> paramMapCodec) {
/* 43 */     return (StructureType<S>)Registry.register(BuiltInRegistries.STRUCTURE_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */   
/*    */   MapCodec<S> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructureType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */