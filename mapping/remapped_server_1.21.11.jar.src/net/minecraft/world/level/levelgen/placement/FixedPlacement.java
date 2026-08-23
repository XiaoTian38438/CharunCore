/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FixedPlacement extends PlacementModifier {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockPos.CODEC.listOf().fieldOf("positions").forGetter(())).apply((Applicative)paramInstance, FixedPlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<FixedPlacement> CODEC;
/*    */   private final List<BlockPos> positions;
/*    */   
/*    */   public static FixedPlacement of(BlockPos... paramVarArgs) {
/* 23 */     return new FixedPlacement(List.of(paramVarArgs));
/*    */   }
/*    */   
/*    */   private FixedPlacement(List<BlockPos> paramList) {
/* 27 */     this.positions = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 32 */     int i = SectionPos.blockToSectionCoord(paramBlockPos.getX());
/* 33 */     int j = SectionPos.blockToSectionCoord(paramBlockPos.getZ());
/* 34 */     boolean bool = false;
/* 35 */     for (BlockPos blockPos : this.positions) {
/* 36 */       if (isSameChunk(i, j, blockPos)) {
/* 37 */         bool = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 41 */     if (!bool) {
/* 42 */       return Stream.empty();
/*    */     }
/* 44 */     return this.positions.stream().filter(paramBlockPos -> isSameChunk(paramInt1, paramInt2, paramBlockPos));
/*    */   }
/*    */   
/*    */   private static boolean isSameChunk(int paramInt1, int paramInt2, BlockPos paramBlockPos) {
/* 48 */     return (paramInt1 == SectionPos.blockToSectionCoord(paramBlockPos.getX()) && paramInt2 == SectionPos.blockToSectionCoord(paramBlockPos.getZ()));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 53 */     return PlacementModifierType.FIXED_PLACEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\FixedPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */