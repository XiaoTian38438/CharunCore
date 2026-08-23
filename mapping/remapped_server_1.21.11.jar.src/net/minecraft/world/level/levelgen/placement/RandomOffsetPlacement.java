/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class RandomOffsetPlacement extends PlacementModifier {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)IntProvider.codec(-16, 16).fieldOf("xz_spread").forGetter(()), (App)IntProvider.codec(-16, 16).fieldOf("y_spread").forGetter(())).apply((Applicative)paramInstance, RandomOffsetPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RandomOffsetPlacement> CODEC;
/*    */   private final IntProvider xzSpread;
/*    */   private final IntProvider ySpread;
/*    */   
/*    */   public static RandomOffsetPlacement of(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 25 */     return new RandomOffsetPlacement(paramIntProvider1, paramIntProvider2);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement vertical(IntProvider paramIntProvider) {
/* 29 */     return new RandomOffsetPlacement((IntProvider)ConstantInt.of(0), paramIntProvider);
/*    */   }
/*    */   
/*    */   public static RandomOffsetPlacement horizontal(IntProvider paramIntProvider) {
/* 33 */     return new RandomOffsetPlacement(paramIntProvider, (IntProvider)ConstantInt.of(0));
/*    */   }
/*    */   
/*    */   private RandomOffsetPlacement(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 37 */     this.xzSpread = paramIntProvider1;
/* 38 */     this.ySpread = paramIntProvider2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 43 */     int i = paramBlockPos.getX() + this.xzSpread.sample(paramRandomSource);
/* 44 */     int j = paramBlockPos.getY() + this.ySpread.sample(paramRandomSource);
/* 45 */     int k = paramBlockPos.getZ() + this.xzSpread.sample(paramRandomSource);
/* 46 */     return Stream.of(new BlockPos(i, j, k));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 51 */     return PlacementModifierType.RANDOM_OFFSET;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\RandomOffsetPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */