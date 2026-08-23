/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceWaterDepthFilter
/*    */   extends PlacementFilter {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("max_water_depth").forGetter(())).apply((Applicative)paramInstance, SurfaceWaterDepthFilter::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SurfaceWaterDepthFilter> CODEC;
/*    */   private final int maxWaterDepth;
/*    */   
/*    */   private SurfaceWaterDepthFilter(int paramInt) {
/* 23 */     this.maxWaterDepth = paramInt;
/*    */   }
/*    */   
/*    */   public static SurfaceWaterDepthFilter forMaxDepth(int paramInt) {
/* 27 */     return new SurfaceWaterDepthFilter(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 32 */     int i = paramPlacementContext.getHeight(Heightmap.Types.OCEAN_FLOOR, paramBlockPos.getX(), paramBlockPos.getZ());
/* 33 */     int j = paramPlacementContext.getHeight(Heightmap.Types.WORLD_SURFACE, paramBlockPos.getX(), paramBlockPos.getZ());
/*    */     
/* 35 */     return (j - i <= this.maxWaterDepth);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 40 */     return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\SurfaceWaterDepthFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */