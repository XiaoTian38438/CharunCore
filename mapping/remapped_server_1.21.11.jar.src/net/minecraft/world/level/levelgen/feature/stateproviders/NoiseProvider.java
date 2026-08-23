/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class NoiseProvider extends NoiseBasedStateProvider {
/*    */   public static final MapCodec<NoiseProvider> CODEC;
/*    */   protected final List<BlockState> states;
/*    */   
/*    */   protected static <P extends NoiseProvider> Products.P4<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float, List<BlockState>> noiseProviderCodec(RecordCodecBuilder.Instance<P> paramInstance) {
/* 22 */     return noiseCodec((RecordCodecBuilder.Instance)paramInstance).and(
/* 23 */         (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("states").forGetter(paramNoiseProvider -> paramNoiseProvider.states));
/*    */   }
/*    */   
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> noiseProviderCodec(paramInstance).apply((Applicative)paramInstance, NoiseProvider::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public NoiseProvider(long paramLong, NormalNoise.NoiseParameters paramNoiseParameters, float paramFloat, List<BlockState> paramList) {
/* 32 */     super(paramLong, paramNoiseParameters, paramFloat);
/* 33 */     this.states = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 38 */     return BlockStateProviderType.NOISE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 43 */     return getRandomState(this.states, paramBlockPos, this.scale);
/*    */   }
/*    */   
/*    */   protected BlockState getRandomState(List<BlockState> paramList, BlockPos paramBlockPos, double paramDouble) {
/* 47 */     double d = getNoiseValue(paramBlockPos, paramDouble);
/* 48 */     return getRandomState(paramList, d);
/*    */   }
/*    */   
/*    */   protected BlockState getRandomState(List<BlockState> paramList, double paramDouble) {
/* 52 */     double d = Mth.clamp((1.0D + paramDouble) / 2.0D, 0.0D, 0.9999D);
/* 53 */     return paramList.get((int)(d * paramList.size()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */