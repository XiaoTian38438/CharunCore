/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceRelativeThresholdFilter extends PlacementFilter {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(()), (App)Codec.INT.optionalFieldOf("min_inclusive", Integer.valueOf(-2147483648)).forGetter(()), (App)Codec.INT.optionalFieldOf("max_inclusive", Integer.valueOf(2147483647)).forGetter(())).apply((Applicative)paramInstance, SurfaceRelativeThresholdFilter::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SurfaceRelativeThresholdFilter> CODEC;
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   private SurfaceRelativeThresholdFilter(Heightmap.Types paramTypes, int paramInt1, int paramInt2) {
/* 26 */     this.heightmap = paramTypes;
/* 27 */     this.minInclusive = paramInt1;
/* 28 */     this.maxInclusive = paramInt2;
/*    */   }
/*    */   
/*    */   public static SurfaceRelativeThresholdFilter of(Heightmap.Types paramTypes, int paramInt1, int paramInt2) {
/* 32 */     return new SurfaceRelativeThresholdFilter(paramTypes, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 37 */     long l1 = paramPlacementContext.getHeight(this.heightmap, paramBlockPos.getX(), paramBlockPos.getZ());
/*    */     
/* 39 */     long l2 = l1 + this.minInclusive;
/* 40 */     long l3 = l1 + this.maxInclusive;
/*    */     
/* 42 */     return (l2 <= paramBlockPos.getY() && paramBlockPos.getY() <= l3);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 47 */     return PlacementModifierType.SURFACE_RELATIVE_THRESHOLD_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\SurfaceRelativeThresholdFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */