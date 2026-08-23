/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperTrapDoorBlock extends TrapDoorBlock implements WeatheringCopper {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(TrapDoorBlock::getType), (App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperTrapDoorBlock::getAge), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperTrapDoorBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeatheringCopperTrapDoorBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public MapCodec<WeatheringCopperTrapDoorBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperTrapDoorBlock(BlockSetType paramBlockSetType, WeatheringCopper.WeatherState paramWeatherState, BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramBlockSetType, paramProperties);
/* 27 */     this.weatherState = paramWeatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 32 */     changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 37 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 42 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperTrapDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */