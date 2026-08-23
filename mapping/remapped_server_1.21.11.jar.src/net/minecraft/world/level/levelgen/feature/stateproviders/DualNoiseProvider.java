/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class DualNoiseProvider
/*    */   extends NoiseProvider {
/*    */   public static final MapCodec<DualNoiseProvider> CODEC;
/*    */   private final InclusiveRange<Integer> variety;
/*    */   
/*    */   static {
/* 28 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)InclusiveRange.codec((Codec)Codec.INT, Integer.valueOf(1), Integer.valueOf(64)).fieldOf("variety").forGetter(()), (App)NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("slow_noise").forGetter(()), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter(())).and(noiseProviderCodec(paramInstance)).apply((Applicative)paramInstance, DualNoiseProvider::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final NormalNoise.NoiseParameters slowNoiseParameters;
/*    */ 
/*    */   
/*    */   private final float slowScale;
/*    */ 
/*    */   
/*    */   private final NormalNoise slowNoise;
/*    */ 
/*    */   
/*    */   public DualNoiseProvider(InclusiveRange<Integer> paramInclusiveRange, NormalNoise.NoiseParameters paramNoiseParameters1, float paramFloat1, long paramLong, NormalNoise.NoiseParameters paramNoiseParameters2, float paramFloat2, List<BlockState> paramList) {
/* 43 */     super(paramLong, paramNoiseParameters2, paramFloat2, paramList);
/* 44 */     this.variety = paramInclusiveRange;
/* 45 */     this.slowNoiseParameters = paramNoiseParameters1;
/* 46 */     this.slowScale = paramFloat1;
/* 47 */     this.slowNoise = NormalNoise.create((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(paramLong)), paramNoiseParameters1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 52 */     return BlockStateProviderType.DUAL_NOISE_PROVIDER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 58 */     double d = getSlowNoiseValue(paramBlockPos);
/* 59 */     int i = (int)Mth.clampedMap(d, -1.0D, 1.0D, ((Integer)this.variety.minInclusive()).intValue(), (((Integer)this.variety.maxInclusive()).intValue() + 1));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     ArrayList<BlockState> arrayList = Lists.newArrayListWithCapacity(i);
/* 65 */     for (byte b = 0; b < i; b++)
/*    */     {
/* 67 */       arrayList.add(getRandomState(this.states, getSlowNoiseValue(paramBlockPos.offset(b * 54545, 0, b * 34234))));
/*    */     }
/*    */     
/* 70 */     return getRandomState(arrayList, paramBlockPos, this.scale);
/*    */   }
/*    */   
/*    */   protected double getSlowNoiseValue(BlockPos paramBlockPos) {
/* 74 */     return this.slowNoise.getValue((paramBlockPos.getX() * this.slowScale), (paramBlockPos.getY() * this.slowScale), (paramBlockPos.getZ() * this.slowScale));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\DualNoiseProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */