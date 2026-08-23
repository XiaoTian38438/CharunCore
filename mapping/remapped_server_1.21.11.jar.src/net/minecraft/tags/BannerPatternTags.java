/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BannerPatternTags
/*    */ {
/* 11 */   public static final TagKey<BannerPattern> NO_ITEM_REQUIRED = create("no_item_required");
/* 12 */   public static final TagKey<BannerPattern> PATTERN_ITEM_FLOWER = create("pattern_item/flower");
/* 13 */   public static final TagKey<BannerPattern> PATTERN_ITEM_CREEPER = create("pattern_item/creeper");
/* 14 */   public static final TagKey<BannerPattern> PATTERN_ITEM_SKULL = create("pattern_item/skull");
/* 15 */   public static final TagKey<BannerPattern> PATTERN_ITEM_MOJANG = create("pattern_item/mojang");
/* 16 */   public static final TagKey<BannerPattern> PATTERN_ITEM_GLOBE = create("pattern_item/globe");
/* 17 */   public static final TagKey<BannerPattern> PATTERN_ITEM_PIGLIN = create("pattern_item/piglin");
/* 18 */   public static final TagKey<BannerPattern> PATTERN_ITEM_FLOW = create("pattern_item/flow");
/* 19 */   public static final TagKey<BannerPattern> PATTERN_ITEM_GUSTER = create("pattern_item/guster");
/* 20 */   public static final TagKey<BannerPattern> PATTERN_ITEM_FIELD_MASONED = create("pattern_item/field_masoned");
/* 21 */   public static final TagKey<BannerPattern> PATTERN_ITEM_BORDURE_INDENTED = create("pattern_item/bordure_indented");
/*    */   
/*    */   private static TagKey<BannerPattern> create(String paramString) {
/* 24 */     return TagKey.create(Registries.BANNER_PATTERN, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\BannerPatternTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */