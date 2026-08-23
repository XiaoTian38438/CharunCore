/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class LayerConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)paramInstance, LayerConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<LayerConfiguration> CODEC;
/*    */   public final int height;
/*    */   public final BlockState state;
/*    */   
/*    */   public LayerConfiguration(int paramInt, BlockState paramBlockState) {
/* 18 */     this.height = paramInt;
/* 19 */     this.state = paramBlockState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\LayerConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */