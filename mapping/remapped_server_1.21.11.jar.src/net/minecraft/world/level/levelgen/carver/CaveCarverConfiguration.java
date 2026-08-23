/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CaveCarverConfiguration extends CarverConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)CarverConfiguration.CODEC.forGetter(()), (App)FloatProvider.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(()), (App)FloatProvider.CODEC.fieldOf("vertical_radius_multiplier").forGetter(()), (App)FloatProvider.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(())).apply((Applicative)paramInstance, CaveCarverConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<CaveCarverConfiguration> CODEC;
/*    */   
/*    */   public final FloatProvider horizontalRadiusMultiplier;
/*    */   
/*    */   public final FloatProvider verticalRadiusMultiplier;
/*    */   
/*    */   final FloatProvider floorLevel;
/*    */ 
/*    */   
/*    */   public CaveCarverConfiguration(float paramFloat, HeightProvider paramHeightProvider, FloatProvider paramFloatProvider1, VerticalAnchor paramVerticalAnchor, CarverDebugSettings paramCarverDebugSettings, HolderSet<Block> paramHolderSet, FloatProvider paramFloatProvider2, FloatProvider paramFloatProvider3, FloatProvider paramFloatProvider4) {
/* 26 */     super(paramFloat, paramHeightProvider, paramFloatProvider1, paramVerticalAnchor, paramCarverDebugSettings, paramHolderSet);
/* 27 */     this.horizontalRadiusMultiplier = paramFloatProvider2;
/* 28 */     this.verticalRadiusMultiplier = paramFloatProvider3;
/* 29 */     this.floorLevel = paramFloatProvider4;
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(float paramFloat, HeightProvider paramHeightProvider, FloatProvider paramFloatProvider1, VerticalAnchor paramVerticalAnchor, HolderSet<Block> paramHolderSet, FloatProvider paramFloatProvider2, FloatProvider paramFloatProvider3, FloatProvider paramFloatProvider4) {
/* 33 */     this(paramFloat, paramHeightProvider, paramFloatProvider1, paramVerticalAnchor, CarverDebugSettings.DEFAULT, paramHolderSet, paramFloatProvider2, paramFloatProvider3, paramFloatProvider4);
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(CarverConfiguration paramCarverConfiguration, FloatProvider paramFloatProvider1, FloatProvider paramFloatProvider2, FloatProvider paramFloatProvider3) {
/* 37 */     this(paramCarverConfiguration.probability, paramCarverConfiguration.y, paramCarverConfiguration.yScale, paramCarverConfiguration.lavaLevel, paramCarverConfiguration.debugSettings, paramCarverConfiguration.replaceable, paramFloatProvider1, paramFloatProvider2, paramFloatProvider3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CaveCarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */