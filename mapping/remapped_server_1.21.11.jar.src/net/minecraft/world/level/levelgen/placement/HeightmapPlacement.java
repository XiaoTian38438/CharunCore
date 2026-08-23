/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapPlacement extends PlacementModifier {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(())).apply((Applicative)paramInstance, HeightmapPlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<HeightmapPlacement> CODEC;
/*    */   private final Heightmap.Types heightmap;
/*    */   
/*    */   private HeightmapPlacement(Heightmap.Types paramTypes) {
/* 22 */     this.heightmap = paramTypes;
/*    */   }
/*    */   
/*    */   public static HeightmapPlacement onHeightmap(Heightmap.Types paramTypes) {
/* 26 */     return new HeightmapPlacement(paramTypes);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 31 */     int i = paramBlockPos.getX();
/* 32 */     int j = paramBlockPos.getZ();
/* 33 */     int k = paramPlacementContext.getHeight(this.heightmap, i, j);
/* 34 */     if (k > paramPlacementContext.getMinY()) {
/* 35 */       return Stream.of(new BlockPos(i, k, j));
/*    */     }
/* 37 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 42 */     return PlacementModifierType.HEIGHTMAP;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\HeightmapPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */