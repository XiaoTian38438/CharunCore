/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class PlacementFilter
/*    */   extends PlacementModifier
/*    */ {
/*    */   public final Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 11 */     if (shouldPlace(paramPlacementContext, paramRandomSource, paramBlockPos)) {
/* 12 */       return Stream.of(paramBlockPos);
/*    */     }
/* 14 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\PlacementFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */