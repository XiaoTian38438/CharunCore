/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.FlatLevelGeneratorPresetTags;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*    */ 
/*    */ public class FlatLevelGeneratorPresetTagsProvider
/*    */   extends KeyTagProvider<FlatLevelGeneratorPreset> {
/*    */   public FlatLevelGeneratorPresetTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 14 */     super(paramPackOutput, Registries.FLAT_LEVEL_GENERATOR_PRESET, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(FlatLevelGeneratorPresetTags.VISIBLE)
/* 20 */       .add(FlatLevelGeneratorPresets.CLASSIC_FLAT)
/* 21 */       .add(FlatLevelGeneratorPresets.TUNNELERS_DREAM)
/* 22 */       .add(FlatLevelGeneratorPresets.WATER_WORLD)
/* 23 */       .add(FlatLevelGeneratorPresets.OVERWORLD)
/* 24 */       .add(FlatLevelGeneratorPresets.SNOWY_KINGDOM)
/* 25 */       .add(FlatLevelGeneratorPresets.BOTTOMLESS_PIT)
/* 26 */       .add(FlatLevelGeneratorPresets.DESERT)
/* 27 */       .add(FlatLevelGeneratorPresets.REDSTONE_READY)
/* 28 */       .add(FlatLevelGeneratorPresets.THE_VOID);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\FlatLevelGeneratorPresetTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */