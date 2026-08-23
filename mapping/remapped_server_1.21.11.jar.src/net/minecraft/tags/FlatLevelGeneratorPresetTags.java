/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatLevelGeneratorPresetTags
/*    */ {
/* 12 */   public static final TagKey<FlatLevelGeneratorPreset> VISIBLE = create("visible");
/*    */   
/*    */   private static TagKey<FlatLevelGeneratorPreset> create(String paramString) {
/* 15 */     return TagKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\FlatLevelGeneratorPresetTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */