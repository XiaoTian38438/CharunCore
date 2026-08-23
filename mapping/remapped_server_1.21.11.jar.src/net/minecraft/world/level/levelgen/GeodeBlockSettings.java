/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class GeodeBlockSettings
/*    */ {
/*    */   public final BlockStateProvider fillingProvider;
/*    */   public final BlockStateProvider innerLayerProvider;
/*    */   public final BlockStateProvider alternateInnerLayerProvider;
/*    */   public final BlockStateProvider middleLayerProvider;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("filling_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(()), (App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter(()), (App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(())).apply((Applicative)paramInstance, GeodeBlockSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public final BlockStateProvider outerLayerProvider;
/*    */   
/*    */   public final List<BlockState> innerPlacements;
/*    */   
/*    */   public final TagKey<Block> cannotReplace;
/*    */   public final TagKey<Block> invalidBlocks;
/*    */   public static final Codec<GeodeBlockSettings> CODEC;
/*    */   
/*    */   public GeodeBlockSettings(BlockStateProvider paramBlockStateProvider1, BlockStateProvider paramBlockStateProvider2, BlockStateProvider paramBlockStateProvider3, BlockStateProvider paramBlockStateProvider4, BlockStateProvider paramBlockStateProvider5, List<BlockState> paramList, TagKey<Block> paramTagKey1, TagKey<Block> paramTagKey2) {
/* 37 */     this.fillingProvider = paramBlockStateProvider1;
/* 38 */     this.innerLayerProvider = paramBlockStateProvider2;
/* 39 */     this.alternateInnerLayerProvider = paramBlockStateProvider3;
/* 40 */     this.middleLayerProvider = paramBlockStateProvider4;
/* 41 */     this.outerLayerProvider = paramBlockStateProvider5;
/* 42 */     this.innerPlacements = paramList;
/* 43 */     this.cannotReplace = paramTagKey1;
/* 44 */     this.invalidBlocks = paramTagKey2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\GeodeBlockSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */