/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperStairBlock extends StairBlock implements WeatheringCopper {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), (App)BlockState.CODEC.fieldOf("base_state").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperStairBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeatheringCopperStairBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public MapCodec<WeatheringCopperStairBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WeatheringCopperStairBlock(WeatheringCopper.WeatherState paramWeatherState, BlockState paramBlockState, BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramBlockState, paramProperties);
/* 26 */     this.weatherState = paramWeatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 31 */     changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 36 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 41 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperStairBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */