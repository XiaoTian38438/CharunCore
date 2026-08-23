/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InSquarePlacement
/*    */   extends PlacementModifier
/*    */ {
/* 16 */   private static final InSquarePlacement INSTANCE = new InSquarePlacement();
/*    */   
/* 18 */   public static final MapCodec<InSquarePlacement> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public static InSquarePlacement spread() {
/* 21 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 26 */     int i = paramRandomSource.nextInt(16) + paramBlockPos.getX();
/* 27 */     int j = paramRandomSource.nextInt(16) + paramBlockPos.getZ();
/*    */     
/* 29 */     return Stream.of(new BlockPos(i, paramBlockPos.getY(), j));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 34 */     return PlacementModifierType.IN_SQUARE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\InSquarePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */