/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class MultiNoiseBiomeSourceParameterLists {
/* 10 */   public static final ResourceKey<MultiNoiseBiomeSourceParameterList> NETHER = register("nether");
/* 11 */   public static final ResourceKey<MultiNoiseBiomeSourceParameterList> OVERWORLD = register("overworld");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<MultiNoiseBiomeSourceParameterList> paramBootstrapContext) {
/* 14 */     HolderGetter<Biome> holderGetter = paramBootstrapContext.lookup(Registries.BIOME);
/* 15 */     paramBootstrapContext.register(NETHER, new MultiNoiseBiomeSourceParameterList(MultiNoiseBiomeSourceParameterList.Preset.NETHER, holderGetter));
/* 16 */     paramBootstrapContext.register(OVERWORLD, new MultiNoiseBiomeSourceParameterList(MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD, holderGetter));
/*    */   }
/*    */   
/*    */   private static ResourceKey<MultiNoiseBiomeSourceParameterList> register(String paramString) {
/* 20 */     return ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterLists.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */