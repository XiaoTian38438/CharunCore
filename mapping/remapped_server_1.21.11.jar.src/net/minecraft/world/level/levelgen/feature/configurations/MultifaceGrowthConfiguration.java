/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*    */ 
/*    */ public class MultifaceGrowthConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<MultifaceGrowthConfiguration> CODEC;
/*    */   
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").flatXmap(MultifaceGrowthConfiguration::apply, DataResult::success).orElse(Blocks.GLOW_LICHEN).forGetter(()), (App)Codec.intRange(1, 64).fieldOf("search_range").orElse(Integer.valueOf(10)).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_floor").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("can_place_on_wall").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spreading").orElse(Float.valueOf(0.5F)).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_be_placed_on").forGetter(())).apply((Applicative)paramInstance, MultifaceGrowthConfiguration::new));
/*    */   }
/*    */   public final MultifaceSpreadeableBlock placeBlock; public final int searchRange;
/*    */   public final boolean canPlaceOnFloor;
/*    */   public final boolean canPlaceOnCeiling;
/*    */   public final boolean canPlaceOnWall;
/*    */   public final float chanceOfSpreading;
/*    */   public final HolderSet<Block> canBePlacedOn;
/*    */   private final ObjectArrayList<Direction> validDirections;
/*    */   
/*    */   private static DataResult<MultifaceSpreadeableBlock> apply(Block paramBlock) {
/* 32 */     MultifaceSpreadeableBlock multifaceSpreadeableBlock = (MultifaceSpreadeableBlock)paramBlock; return (paramBlock instanceof MultifaceSpreadeableBlock) ? 
/* 33 */       DataResult.success(multifaceSpreadeableBlock) : 
/* 34 */       DataResult.error(() -> "Growth block should be a multiface spreadeable block");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MultifaceGrowthConfiguration(MultifaceSpreadeableBlock paramMultifaceSpreadeableBlock, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, float paramFloat, HolderSet<Block> paramHolderSet) {
/* 49 */     this.placeBlock = paramMultifaceSpreadeableBlock;
/* 50 */     this.searchRange = paramInt;
/* 51 */     this.canPlaceOnFloor = paramBoolean1;
/* 52 */     this.canPlaceOnCeiling = paramBoolean2;
/* 53 */     this.canPlaceOnWall = paramBoolean3;
/* 54 */     this.chanceOfSpreading = paramFloat;
/* 55 */     this.canBePlacedOn = paramHolderSet;
/*    */     
/* 57 */     this.validDirections = new ObjectArrayList(6);
/* 58 */     if (paramBoolean2) {
/* 59 */       this.validDirections.add(Direction.UP);
/*    */     }
/* 61 */     if (paramBoolean1) {
/* 62 */       this.validDirections.add(Direction.DOWN);
/*    */     }
/* 64 */     if (paramBoolean3) {
/* 65 */       Objects.requireNonNull(this.validDirections); Direction.Plane.HORIZONTAL.forEach(this.validDirections::add);
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<Direction> getShuffledDirectionsExcept(RandomSource paramRandomSource, Direction paramDirection) {
/* 70 */     return Util.toShuffledList(this.validDirections.stream().filter(paramDirection2 -> (paramDirection2 != paramDirection1)), paramRandomSource);
/*    */   }
/*    */   
/*    */   public List<Direction> getShuffledDirections(RandomSource paramRandomSource) {
/* 74 */     return Util.shuffledCopy(this.validDirections, paramRandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\MultifaceGrowthConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */