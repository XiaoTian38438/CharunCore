/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnvironmentScanPlacement
/*    */   extends PlacementModifier
/*    */ {
/*    */   private final Direction directionOfSearch;
/*    */   private final BlockPredicate targetCondition;
/*    */   private final BlockPredicate allowedSearchCondition;
/*    */   private final int maxSteps;
/*    */   public static final MapCodec<EnvironmentScanPlacement> CODEC;
/*    */   
/*    */   static {
/* 29 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Direction.VERTICAL_CODEC.fieldOf("direction_of_search").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("target_condition").forGetter(()), (App)BlockPredicate.CODEC.optionalFieldOf("allowed_search_condition", BlockPredicate.alwaysTrue()).forGetter(()), (App)Codec.intRange(1, 32).fieldOf("max_steps").forGetter(())).apply((Applicative)paramInstance, EnvironmentScanPlacement::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private EnvironmentScanPlacement(Direction paramDirection, BlockPredicate paramBlockPredicate1, BlockPredicate paramBlockPredicate2, int paramInt) {
/* 37 */     this.directionOfSearch = paramDirection;
/* 38 */     this.targetCondition = paramBlockPredicate1;
/* 39 */     this.allowedSearchCondition = paramBlockPredicate2;
/* 40 */     this.maxSteps = paramInt;
/*    */   }
/*    */   
/*    */   public static EnvironmentScanPlacement scanningFor(Direction paramDirection, BlockPredicate paramBlockPredicate1, BlockPredicate paramBlockPredicate2, int paramInt) {
/* 44 */     return new EnvironmentScanPlacement(paramDirection, paramBlockPredicate1, paramBlockPredicate2, paramInt);
/*    */   }
/*    */   
/*    */   public static EnvironmentScanPlacement scanningFor(Direction paramDirection, BlockPredicate paramBlockPredicate, int paramInt) {
/* 48 */     return scanningFor(paramDirection, paramBlockPredicate, BlockPredicate.alwaysTrue(), paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 53 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 54 */     WorldGenLevel worldGenLevel = paramPlacementContext.getLevel();
/* 55 */     if (!this.allowedSearchCondition.test(worldGenLevel, mutableBlockPos)) {
/* 56 */       return Stream.of(new BlockPos[0]);
/*    */     }
/*    */     
/* 59 */     for (byte b = 0; b < this.maxSteps; b++) {
/* 60 */       if (this.targetCondition.test(worldGenLevel, mutableBlockPos)) {
/* 61 */         return (Stream)Stream.of(mutableBlockPos);
/*    */       }
/* 63 */       mutableBlockPos.move(this.directionOfSearch);
/* 64 */       if (worldGenLevel.isOutsideBuildHeight(mutableBlockPos.getY())) {
/* 65 */         return Stream.of(new BlockPos[0]);
/*    */       }
/* 67 */       if (!this.allowedSearchCondition.test(worldGenLevel, mutableBlockPos)) {
/*    */         break;
/*    */       }
/*    */     } 
/* 71 */     if (this.targetCondition.test(worldGenLevel, mutableBlockPos)) {
/* 72 */       return (Stream)Stream.of(mutableBlockPos);
/*    */     }
/* 74 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 79 */     return PlacementModifierType.ENVIRONMENT_SCAN;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\EnvironmentScanPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */