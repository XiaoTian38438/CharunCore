/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CarverConfiguration extends ProbabilityFeatureConfiguration {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)HeightProvider.CODEC.fieldOf("y").forGetter(()), (App)FloatProvider.CODEC.fieldOf("yScale").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(()), (App)CarverDebugSettings.CODEC.optionalFieldOf("debug_settings", CarverDebugSettings.DEFAULT).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(())).apply((Applicative)paramInstance, CarverConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<CarverConfiguration> CODEC;
/*    */   
/*    */   public final HeightProvider y;
/*    */   
/*    */   public final FloatProvider yScale;
/*    */   
/*    */   public final VerticalAnchor lavaLevel;
/*    */   
/*    */   public final CarverDebugSettings debugSettings;
/*    */   
/*    */   public final HolderSet<Block> replaceable;
/*    */   
/*    */   public CarverConfiguration(float paramFloat, HeightProvider paramHeightProvider, FloatProvider paramFloatProvider, VerticalAnchor paramVerticalAnchor, CarverDebugSettings paramCarverDebugSettings, HolderSet<Block> paramHolderSet) {
/* 33 */     super(paramFloat);
/* 34 */     this.y = paramHeightProvider;
/* 35 */     this.yScale = paramFloatProvider;
/* 36 */     this.lavaLevel = paramVerticalAnchor;
/* 37 */     this.debugSettings = paramCarverDebugSettings;
/* 38 */     this.replaceable = paramHolderSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */