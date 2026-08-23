/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class VegetationPatchConfiguration implements FeatureConfiguration {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(()), (App)CaveSurface.CODEC.fieldOf("surface").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("depth").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(()), (App)IntProvider.CODEC.fieldOf("xz_radius").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(())).apply((Applicative)paramInstance, VegetationPatchConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<VegetationPatchConfiguration> CODEC;
/*    */   
/*    */   public final TagKey<Block> replaceable;
/*    */   
/*    */   public final BlockStateProvider groundState;
/*    */   
/*    */   public final Holder<PlacedFeature> vegetationFeature;
/*    */   
/*    */   public final CaveSurface surface;
/*    */   
/*    */   public final IntProvider depth;
/*    */   
/*    */   public final float extraBottomBlockChance;
/*    */   
/*    */   public final int verticalRange;
/*    */   
/*    */   public final float vegetationChance;
/*    */   
/*    */   public final IntProvider xzRadius;
/*    */   public final float extraEdgeColumnChance;
/*    */   
/*    */   public VegetationPatchConfiguration(TagKey<Block> paramTagKey, BlockStateProvider paramBlockStateProvider, Holder<PlacedFeature> paramHolder, CaveSurface paramCaveSurface, IntProvider paramIntProvider1, float paramFloat1, int paramInt, float paramFloat2, IntProvider paramIntProvider2, float paramFloat3) {
/* 42 */     this.replaceable = paramTagKey;
/* 43 */     this.groundState = paramBlockStateProvider;
/* 44 */     this.vegetationFeature = paramHolder;
/* 45 */     this.surface = paramCaveSurface;
/* 46 */     this.depth = paramIntProvider1;
/* 47 */     this.extraBottomBlockChance = paramFloat1;
/* 48 */     this.verticalRange = paramInt;
/* 49 */     this.vegetationChance = paramFloat2;
/* 50 */     this.xzRadius = paramIntProvider2;
/* 51 */     this.extraEdgeColumnChance = paramFloat3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\VegetationPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */