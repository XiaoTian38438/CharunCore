/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class RarityFilter extends PlacementFilter {
/*    */   public static final MapCodec<RarityFilter> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, paramRarityFilter -> Integer.valueOf(paramRarityFilter.chance));
/*    */   }
/*    */   private final int chance;
/*    */   
/*    */   private RarityFilter(int paramInt) {
/* 17 */     this.chance = paramInt;
/*    */   }
/*    */   
/*    */   public static RarityFilter onAverageOnceEvery(int paramInt) {
/* 21 */     return new RarityFilter(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 26 */     return (paramRandomSource.nextFloat() < 1.0F / this.chance);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 31 */     return PlacementModifierType.RARITY_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\RarityFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */