/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class BlockPredicateFilter extends PlacementFilter {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockPredicate.CODEC.fieldOf("predicate").forGetter(())).apply((Applicative)paramInstance, BlockPredicateFilter::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<BlockPredicateFilter> CODEC;
/*    */   private final BlockPredicate predicate;
/*    */   
/*    */   private BlockPredicateFilter(BlockPredicate paramBlockPredicate) {
/* 21 */     this.predicate = paramBlockPredicate;
/*    */   }
/*    */   
/*    */   public static BlockPredicateFilter forPredicate(BlockPredicate paramBlockPredicate) {
/* 25 */     return new BlockPredicateFilter(paramBlockPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 30 */     return this.predicate.test(paramPlacementContext.getLevel(), paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 35 */     return PlacementModifierType.BLOCK_PREDICATE_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\BlockPredicateFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */