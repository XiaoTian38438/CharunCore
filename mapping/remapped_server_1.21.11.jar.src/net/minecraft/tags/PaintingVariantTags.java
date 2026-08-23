/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PaintingVariantTags
/*    */ {
/* 11 */   public static final TagKey<PaintingVariant> PLACEABLE = create("placeable");
/*    */   
/*    */   private static TagKey<PaintingVariant> create(String paramString) {
/* 14 */     return TagKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\PaintingVariantTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */