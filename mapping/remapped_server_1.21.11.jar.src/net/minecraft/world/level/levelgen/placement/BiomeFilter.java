/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BiomeFilter
/*    */   extends PlacementFilter
/*    */ {
/* 14 */   private static final BiomeFilter INSTANCE = new BiomeFilter();
/*    */   
/* 16 */   public static MapCodec<BiomeFilter> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BiomeFilter biome() {
/* 22 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 27 */     PlacedFeature placedFeature = paramPlacementContext.topFeature().<Throwable>orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
/* 28 */     Holder holder = paramPlacementContext.getLevel().getBiome(paramBlockPos);
/* 29 */     return paramPlacementContext.generator().getBiomeGenerationSettings(holder).hasFeature(placedFeature);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 34 */     return PlacementModifierType.BIOME_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\BiomeFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */