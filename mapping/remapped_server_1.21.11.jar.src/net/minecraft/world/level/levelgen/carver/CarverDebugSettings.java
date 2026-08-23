/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CarverDebugSettings {
/*  9 */   public static final CarverDebugSettings DEFAULT = new CarverDebugSettings(false, Blocks.ACACIA_BUTTON
/*    */       
/* 11 */       .defaultBlockState(), Blocks.CANDLE
/* 12 */       .defaultBlockState(), Blocks.ORANGE_STAINED_GLASS
/* 13 */       .defaultBlockState(), Blocks.GLASS
/* 14 */       .defaultBlockState()); public static final Codec<CarverDebugSettings> CODEC; private final boolean debugMode; private final BlockState airState;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.BOOL.optionalFieldOf("debug_mode", Boolean.valueOf(false)).forGetter(CarverDebugSettings::isDebugMode), (App)BlockState.CODEC.optionalFieldOf("air_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getAirState), (App)BlockState.CODEC.optionalFieldOf("water_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getWaterState), (App)BlockState.CODEC.optionalFieldOf("lava_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getLavaState), (App)BlockState.CODEC.optionalFieldOf("barrier_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getBarrierState)).apply((Applicative)paramInstance, CarverDebugSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final BlockState waterState;
/*    */ 
/*    */   
/*    */   private final BlockState lavaState;
/*    */ 
/*    */   
/*    */   private final BlockState barrierState;
/*    */ 
/*    */   
/*    */   public static CarverDebugSettings of(boolean paramBoolean, BlockState paramBlockState1, BlockState paramBlockState2, BlockState paramBlockState3, BlockState paramBlockState4) {
/* 32 */     return new CarverDebugSettings(paramBoolean, paramBlockState1, paramBlockState2, paramBlockState3, paramBlockState4);
/*    */   }
/*    */   
/*    */   public static CarverDebugSettings of(BlockState paramBlockState1, BlockState paramBlockState2, BlockState paramBlockState3, BlockState paramBlockState4) {
/* 36 */     return new CarverDebugSettings(false, paramBlockState1, paramBlockState2, paramBlockState3, paramBlockState4);
/*    */   }
/*    */   
/*    */   public static CarverDebugSettings of(boolean paramBoolean, BlockState paramBlockState) {
/* 40 */     return new CarverDebugSettings(paramBoolean, paramBlockState, DEFAULT.getWaterState(), DEFAULT.getLavaState(), DEFAULT.getBarrierState());
/*    */   }
/*    */   
/*    */   private CarverDebugSettings(boolean paramBoolean, BlockState paramBlockState1, BlockState paramBlockState2, BlockState paramBlockState3, BlockState paramBlockState4) {
/* 44 */     this.debugMode = paramBoolean;
/* 45 */     this.airState = paramBlockState1;
/* 46 */     this.waterState = paramBlockState2;
/* 47 */     this.lavaState = paramBlockState3;
/* 48 */     this.barrierState = paramBlockState4;
/*    */   }
/*    */   
/*    */   public boolean isDebugMode() {
/* 52 */     return this.debugMode;
/*    */   }
/*    */   
/*    */   public BlockState getAirState() {
/* 56 */     return this.airState;
/*    */   }
/*    */   
/*    */   public BlockState getWaterState() {
/* 60 */     return this.waterState;
/*    */   }
/*    */   
/*    */   public BlockState getLavaState() {
/* 64 */     return this.lavaState;
/*    */   }
/*    */   
/*    */   public BlockState getBarrierState() {
/* 68 */     return this.barrierState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CarverDebugSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */