/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeLayerSettings {
/*  7 */   private static final Codec<Double> LAYER_RANGE = Codec.doubleRange(0.01D, 50.0D); public static final Codec<GeodeLayerSettings> CODEC; static {
/*  8 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)LAYER_RANGE.fieldOf("filling").orElse(Double.valueOf(1.7D)).forGetter(()), (App)LAYER_RANGE.fieldOf("inner_layer").orElse(Double.valueOf(2.2D)).forGetter(()), (App)LAYER_RANGE.fieldOf("middle_layer").orElse(Double.valueOf(3.2D)).forGetter(()), (App)LAYER_RANGE.fieldOf("outer_layer").orElse(Double.valueOf(4.2D)).forGetter(())).apply((Applicative)paramInstance, GeodeLayerSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public final double filling;
/*    */   
/*    */   public final double innerLayer;
/*    */   
/*    */   public final double middleLayer;
/*    */   
/*    */   public final double outerLayer;
/*    */   
/*    */   public GeodeLayerSettings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 21 */     this.filling = paramDouble1;
/* 22 */     this.innerLayer = paramDouble2;
/* 23 */     this.middleLayer = paramDouble3;
/* 24 */     this.outerLayer = paramDouble4;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\GeodeLayerSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */