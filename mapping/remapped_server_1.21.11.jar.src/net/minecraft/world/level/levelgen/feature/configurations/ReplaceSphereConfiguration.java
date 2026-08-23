/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceSphereConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockState.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)IntProvider.codec(0, 12).fieldOf("radius").forGetter(())).apply((Applicative)paramInstance, ReplaceSphereConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ReplaceSphereConfiguration> CODEC;
/*    */   
/*    */   public final BlockState targetState;
/*    */   
/*    */   public final BlockState replaceState;
/*    */   private final IntProvider radius;
/*    */   
/*    */   public ReplaceSphereConfiguration(BlockState paramBlockState1, BlockState paramBlockState2, IntProvider paramIntProvider) {
/* 21 */     this.targetState = paramBlockState1;
/* 22 */     this.replaceState = paramBlockState2;
/* 23 */     this.radius = paramIntProvider;
/*    */   }
/*    */   
/*    */   public IntProvider radius() {
/* 27 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceSphereConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */