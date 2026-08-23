/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DeltaFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockState.CODEC.fieldOf("contents").forGetter(()), (App)BlockState.CODEC.fieldOf("rim").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("size").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("rim_size").forGetter(())).apply((Applicative)paramInstance, DeltaFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DeltaFeatureConfiguration> CODEC;
/*    */   
/*    */   private final BlockState contents;
/*    */   
/*    */   private final BlockState rim;
/*    */   private final IntProvider size;
/*    */   private final IntProvider rimSize;
/*    */   
/*    */   public DeltaFeatureConfiguration(BlockState paramBlockState1, BlockState paramBlockState2, IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 22 */     this.contents = paramBlockState1;
/* 23 */     this.rim = paramBlockState2;
/* 24 */     this.size = paramIntProvider1;
/* 25 */     this.rimSize = paramIntProvider2;
/*    */   }
/*    */   
/*    */   public BlockState contents() {
/* 29 */     return this.contents;
/*    */   }
/*    */   
/*    */   public BlockState rim() {
/* 33 */     return this.rim;
/*    */   }
/*    */   
/*    */   public IntProvider size() {
/* 37 */     return this.size;
/*    */   }
/*    */   
/*    */   public IntProvider rimSize() {
/* 41 */     return this.rimSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\DeltaFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */