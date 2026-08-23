/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FoliagePlacerType<P extends FoliagePlacer> {
/*  8 */   public static final FoliagePlacerType<BlobFoliagePlacer> BLOB_FOLIAGE_PLACER = register("blob_foliage_placer", BlobFoliagePlacer.CODEC);
/*  9 */   public static final FoliagePlacerType<SpruceFoliagePlacer> SPRUCE_FOLIAGE_PLACER = register("spruce_foliage_placer", SpruceFoliagePlacer.CODEC);
/* 10 */   public static final FoliagePlacerType<PineFoliagePlacer> PINE_FOLIAGE_PLACER = register("pine_foliage_placer", PineFoliagePlacer.CODEC);
/* 11 */   public static final FoliagePlacerType<AcaciaFoliagePlacer> ACACIA_FOLIAGE_PLACER = register("acacia_foliage_placer", AcaciaFoliagePlacer.CODEC);
/* 12 */   public static final FoliagePlacerType<BushFoliagePlacer> BUSH_FOLIAGE_PLACER = register("bush_foliage_placer", BushFoliagePlacer.CODEC);
/* 13 */   public static final FoliagePlacerType<FancyFoliagePlacer> FANCY_FOLIAGE_PLACER = register("fancy_foliage_placer", FancyFoliagePlacer.CODEC);
/* 14 */   public static final FoliagePlacerType<MegaJungleFoliagePlacer> MEGA_JUNGLE_FOLIAGE_PLACER = register("jungle_foliage_placer", MegaJungleFoliagePlacer.CODEC);
/* 15 */   public static final FoliagePlacerType<MegaPineFoliagePlacer> MEGA_PINE_FOLIAGE_PLACER = register("mega_pine_foliage_placer", MegaPineFoliagePlacer.CODEC);
/* 16 */   public static final FoliagePlacerType<DarkOakFoliagePlacer> DARK_OAK_FOLIAGE_PLACER = register("dark_oak_foliage_placer", DarkOakFoliagePlacer.CODEC);
/* 17 */   public static final FoliagePlacerType<RandomSpreadFoliagePlacer> RANDOM_SPREAD_FOLIAGE_PLACER = register("random_spread_foliage_placer", RandomSpreadFoliagePlacer.CODEC);
/* 18 */   public static final FoliagePlacerType<CherryFoliagePlacer> CHERRY_FOLIAGE_PLACER = register("cherry_foliage_placer", CherryFoliagePlacer.CODEC);
/*    */   
/*    */   private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 21 */     return (FoliagePlacerType<P>)Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, paramString, new FoliagePlacerType<>(paramMapCodec));
/*    */   }
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private FoliagePlacerType(MapCodec<P> paramMapCodec) {
/* 27 */     this.codec = paramMapCodec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 31 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */