/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.DyeColor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 65 */   private final ImmutableList.Builder<BannerPatternLayers.Layer> layers = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public Builder addIfRegistered(HolderGetter<BannerPattern> paramHolderGetter, ResourceKey<BannerPattern> paramResourceKey, DyeColor paramDyeColor) {
/* 72 */     Optional<Holder<BannerPattern>> optional = paramHolderGetter.get(paramResourceKey);
/* 73 */     if (optional.isEmpty()) {
/* 74 */       BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", paramResourceKey.identifier());
/* 75 */       return this;
/*    */     } 
/* 77 */     return add(optional.get(), paramDyeColor);
/*    */   }
/*    */   
/*    */   public Builder add(Holder<BannerPattern> paramHolder, DyeColor paramDyeColor) {
/* 81 */     return add(new BannerPatternLayers.Layer(paramHolder, paramDyeColor));
/*    */   }
/*    */   
/*    */   public Builder add(BannerPatternLayers.Layer paramLayer) {
/* 85 */     this.layers.add(paramLayer);
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addAll(BannerPatternLayers paramBannerPatternLayers) {
/* 90 */     this.layers.addAll(paramBannerPatternLayers.layers);
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public BannerPatternLayers build() {
/* 95 */     return new BannerPatternLayers((List<BannerPatternLayers.Layer>)this.layers.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */