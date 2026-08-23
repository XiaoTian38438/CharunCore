/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class RepeatingPlacement
/*    */   extends PlacementModifier
/*    */ {
/*    */   protected abstract int count(RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 14 */     return IntStream.range(0, count(paramRandomSource, paramBlockPos)).mapToObj(paramInt -> paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\RepeatingPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */