/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountPlacement extends RepeatingPlacement {
/*    */   public static final MapCodec<CountPlacement> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountPlacement::new, paramCountPlacement -> paramCountPlacement.count);
/*    */   }
/*    */   private final IntProvider count;
/*    */   
/*    */   private CountPlacement(IntProvider paramIntProvider) {
/* 19 */     this.count = paramIntProvider;
/*    */   }
/*    */   
/*    */   public static CountPlacement of(IntProvider paramIntProvider) {
/* 23 */     return new CountPlacement(paramIntProvider);
/*    */   }
/*    */   
/*    */   public static CountPlacement of(int paramInt) {
/* 27 */     return of((IntProvider)ConstantInt.of(paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int count(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 32 */     return this.count.sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 37 */     return PlacementModifierType.COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\CountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */