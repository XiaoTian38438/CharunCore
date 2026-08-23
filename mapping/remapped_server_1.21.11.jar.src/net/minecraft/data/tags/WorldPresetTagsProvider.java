/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.WorldPresetTags;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*    */ 
/*    */ public class WorldPresetTagsProvider
/*    */   extends KeyTagProvider<WorldPreset> {
/*    */   public WorldPresetTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     super(paramPackOutput, Registries.WORLD_PRESET, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(WorldPresetTags.NORMAL)
/* 20 */       .add(WorldPresets.NORMAL)
/* 21 */       .add(WorldPresets.FLAT)
/* 22 */       .add(WorldPresets.LARGE_BIOMES)
/* 23 */       .add(WorldPresets.AMPLIFIED)
/* 24 */       .add(WorldPresets.SINGLE_BIOME_SURFACE);
/*    */ 
/*    */     
/* 27 */     tag(WorldPresetTags.EXTENDED)
/* 28 */       .addTag(WorldPresetTags.NORMAL)
/* 29 */       .add(WorldPresets.DEBUG);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\WorldPresetTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */