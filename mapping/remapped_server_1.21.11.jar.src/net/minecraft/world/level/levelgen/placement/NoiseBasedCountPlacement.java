/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class NoiseBasedCountPlacement
/*    */   extends RepeatingPlacement {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_offset").orElse(Double.valueOf(0.0D)).forGetter(())).apply((Applicative)paramInstance, NoiseBasedCountPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NoiseBasedCountPlacement> CODEC;
/*    */   
/*    */   private final int noiseToCountRatio;
/*    */   
/*    */   private final double noiseFactor;
/*    */   
/*    */   private final double noiseOffset;
/*    */   
/*    */   private NoiseBasedCountPlacement(int paramInt, double paramDouble1, double paramDouble2) {
/* 29 */     this.noiseToCountRatio = paramInt;
/* 30 */     this.noiseFactor = paramDouble1;
/* 31 */     this.noiseOffset = paramDouble2;
/*    */   }
/*    */   
/*    */   public static NoiseBasedCountPlacement of(int paramInt, double paramDouble1, double paramDouble2) {
/* 35 */     return new NoiseBasedCountPlacement(paramInt, paramDouble1, paramDouble2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int count(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 40 */     double d = Biome.BIOME_INFO_NOISE.getValue(paramBlockPos.getX() / this.noiseFactor, paramBlockPos.getZ() / this.noiseFactor, false);
/* 41 */     return (int)Math.ceil((d + this.noiseOffset) * this.noiseToCountRatio);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 46 */     return PlacementModifierType.NOISE_BASED_COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\NoiseBasedCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */