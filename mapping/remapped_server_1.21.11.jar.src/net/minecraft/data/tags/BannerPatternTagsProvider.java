/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BannerPatternTags;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatterns;
/*    */ 
/*    */ public class BannerPatternTagsProvider extends KeyTagProvider<BannerPattern> {
/*    */   public BannerPatternTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     super(paramPackOutput, Registries.BANNER_PATTERN, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(BannerPatternTags.NO_ITEM_REQUIRED)
/* 20 */       .add((ResourceKey<BannerPattern>[])new ResourceKey[] { 
/*    */           BannerPatterns.SQUARE_BOTTOM_LEFT, BannerPatterns.SQUARE_BOTTOM_RIGHT, BannerPatterns.SQUARE_TOP_LEFT, BannerPatterns.SQUARE_TOP_RIGHT, BannerPatterns.STRIPE_BOTTOM, BannerPatterns.STRIPE_TOP, BannerPatterns.STRIPE_LEFT, BannerPatterns.STRIPE_RIGHT, BannerPatterns.STRIPE_CENTER, BannerPatterns.STRIPE_MIDDLE, 
/*    */           BannerPatterns.STRIPE_DOWNRIGHT, BannerPatterns.STRIPE_DOWNLEFT, BannerPatterns.STRIPE_SMALL, BannerPatterns.CROSS, BannerPatterns.STRAIGHT_CROSS, BannerPatterns.TRIANGLE_BOTTOM, BannerPatterns.TRIANGLE_TOP, BannerPatterns.TRIANGLES_BOTTOM, BannerPatterns.TRIANGLES_TOP, BannerPatterns.DIAGONAL_LEFT, 
/*    */           BannerPatterns.DIAGONAL_RIGHT, BannerPatterns.DIAGONAL_LEFT_MIRROR, BannerPatterns.DIAGONAL_RIGHT_MIRROR, BannerPatterns.CIRCLE_MIDDLE, BannerPatterns.RHOMBUS_MIDDLE, BannerPatterns.HALF_VERTICAL, BannerPatterns.HALF_HORIZONTAL, BannerPatterns.HALF_VERTICAL_MIRROR, BannerPatterns.HALF_HORIZONTAL_MIRROR, BannerPatterns.BORDER, 
/*    */           BannerPatterns.GRADIENT, BannerPatterns.GRADIENT_UP });
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
/* 55 */     tag(BannerPatternTags.PATTERN_ITEM_FLOWER)
/* 56 */       .add(BannerPatterns.FLOWER);
/*    */     
/* 58 */     tag(BannerPatternTags.PATTERN_ITEM_CREEPER)
/* 59 */       .add(BannerPatterns.CREEPER);
/*    */     
/* 61 */     tag(BannerPatternTags.PATTERN_ITEM_SKULL)
/* 62 */       .add(BannerPatterns.SKULL);
/*    */     
/* 64 */     tag(BannerPatternTags.PATTERN_ITEM_MOJANG)
/* 65 */       .add(BannerPatterns.MOJANG);
/*    */     
/* 67 */     tag(BannerPatternTags.PATTERN_ITEM_GLOBE)
/* 68 */       .add(BannerPatterns.GLOBE);
/*    */     
/* 70 */     tag(BannerPatternTags.PATTERN_ITEM_PIGLIN)
/* 71 */       .add(BannerPatterns.PIGLIN);
/*    */     
/* 73 */     tag(BannerPatternTags.PATTERN_ITEM_FLOW)
/* 74 */       .add(BannerPatterns.FLOW);
/*    */     
/* 76 */     tag(BannerPatternTags.PATTERN_ITEM_GUSTER)
/* 77 */       .add(BannerPatterns.GUSTER);
/*    */     
/* 79 */     tag(BannerPatternTags.PATTERN_ITEM_FIELD_MASONED)
/* 80 */       .add(BannerPatterns.BRICKS);
/*    */     
/* 82 */     tag(BannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED)
/* 83 */       .add(BannerPatterns.CURLY_BORDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\BannerPatternTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */