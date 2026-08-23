/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeCrackSettings {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)GeodeConfiguration.CHANCE_RANGE.fieldOf("generate_crack_chance").orElse(Double.valueOf(1.0D)).forGetter(()), (App)Codec.doubleRange(0.0D, 5.0D).fieldOf("base_crack_size").orElse(Double.valueOf(2.0D)).forGetter(()), (App)Codec.intRange(0, 10).fieldOf("crack_point_offset").orElse(Integer.valueOf(2)).forGetter(())).apply((Applicative)paramInstance, GeodeCrackSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<GeodeCrackSettings> CODEC;
/*    */   
/*    */   public final double generateCrackChance;
/*    */   public final double baseCrackSize;
/*    */   public final int crackPointOffset;
/*    */   
/*    */   public GeodeCrackSettings(double paramDouble1, double paramDouble2, int paramInt) {
/* 19 */     this.generateCrackChance = paramDouble1;
/* 20 */     this.baseCrackSize = paramDouble2;
/* 21 */     this.crackPointOffset = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\GeodeCrackSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */