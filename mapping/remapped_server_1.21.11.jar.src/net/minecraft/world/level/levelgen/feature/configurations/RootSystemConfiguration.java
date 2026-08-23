/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RootSystemConfiguration implements FeatureConfiguration {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("root_radius").forGetter(()), (App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("root_replaceable").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("root_state_provider").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(()), (App)Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(()), (App)Codec.intRange(1, 16).fieldOf("hanging_roots_vertical_span").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("hanging_root_state_provider").forGetter(()), (App)Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("allowed_tree_position").forGetter(())).apply((Applicative)paramInstance, RootSystemConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RootSystemConfiguration> CODEC;
/*    */   
/*    */   public final Holder<PlacedFeature> treeFeature;
/*    */   
/*    */   public final int requiredVerticalSpaceForTree;
/*    */   
/*    */   public final int rootRadius;
/*    */   
/*    */   public final TagKey<Block> rootReplaceable;
/*    */   
/*    */   public final BlockStateProvider rootStateProvider;
/*    */   
/*    */   public final int rootPlacementAttempts;
/*    */   
/*    */   public final int rootColumnMaxHeight;
/*    */   
/*    */   public final int hangingRootRadius;
/*    */   
/*    */   public final int hangingRootsVerticalSpan;
/*    */   
/*    */   public final BlockStateProvider hangingRootStateProvider;
/*    */   
/*    */   public final int hangingRootPlacementAttempts;
/*    */   public final int allowedVerticalWaterForTree;
/*    */   public final BlockPredicate allowedTreePosition;
/*    */   
/*    */   public RootSystemConfiguration(Holder<PlacedFeature> paramHolder, int paramInt1, int paramInt2, TagKey<Block> paramTagKey, BlockStateProvider paramBlockStateProvider1, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BlockStateProvider paramBlockStateProvider2, int paramInt7, int paramInt8, BlockPredicate paramBlockPredicate) {
/* 45 */     this.treeFeature = paramHolder;
/* 46 */     this.requiredVerticalSpaceForTree = paramInt1;
/* 47 */     this.rootRadius = paramInt2;
/* 48 */     this.rootReplaceable = paramTagKey;
/* 49 */     this.rootStateProvider = paramBlockStateProvider1;
/* 50 */     this.rootPlacementAttempts = paramInt3;
/* 51 */     this.rootColumnMaxHeight = paramInt4;
/* 52 */     this.hangingRootRadius = paramInt5;
/* 53 */     this.hangingRootsVerticalSpan = paramInt6;
/* 54 */     this.hangingRootStateProvider = paramBlockStateProvider2;
/* 55 */     this.hangingRootPlacementAttempts = paramInt7;
/* 56 */     this.allowedVerticalWaterForTree = paramInt8;
/* 57 */     this.allowedTreePosition = paramBlockPredicate;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\RootSystemConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */