/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldPresetTags
/*    */ {
/* 12 */   public static final TagKey<WorldPreset> NORMAL = create("normal");
/*    */   
/* 14 */   public static final TagKey<WorldPreset> EXTENDED = create("extended");
/*    */   
/*    */   private static TagKey<WorldPreset> create(String paramString) {
/* 17 */     return TagKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\WorldPresetTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */