/*    */ package net.minecraft.world.level.levelgen.presets;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.dimension.LevelStem;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ 
/*    */ public class WorldPreset {
/*    */   static {
/* 21 */     DIRECT_CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.unboundedMap(ResourceKey.codec(Registries.LEVEL_STEM), LevelStem.CODEC).fieldOf("dimensions").forGetter(())).apply((Applicative)paramInstance, WorldPreset::new)).validate(WorldPreset::requireOverworld);
/*    */   }
/* 23 */   public static final Codec<Holder<WorldPreset>> CODEC = (Codec<Holder<WorldPreset>>)RegistryFileCodec.create(Registries.WORLD_PRESET, DIRECT_CODEC);
/*    */   public static final Codec<WorldPreset> DIRECT_CODEC;
/*    */   private final Map<ResourceKey<LevelStem>, LevelStem> dimensions;
/*    */   
/*    */   public WorldPreset(Map<ResourceKey<LevelStem>, LevelStem> paramMap) {
/* 28 */     this.dimensions = paramMap;
/*    */   }
/*    */   
/*    */   private ImmutableMap<ResourceKey<LevelStem>, LevelStem> dimensionsInOrder() {
/* 32 */     ImmutableMap.Builder builder = ImmutableMap.builder();
/* 33 */     WorldDimensions.keysInOrder(this.dimensions.keySet().stream()).forEach(paramResourceKey -> {
/*    */           LevelStem levelStem = this.dimensions.get(paramResourceKey);
/*    */           if (levelStem != null) {
/*    */             paramBuilder.put(paramResourceKey, levelStem);
/*    */           }
/*    */         });
/* 39 */     return builder.build();
/*    */   }
/*    */   
/*    */   public WorldDimensions createWorldDimensions() {
/* 43 */     return new WorldDimensions((Map)dimensionsInOrder());
/*    */   }
/*    */   
/*    */   public Optional<LevelStem> overworld() {
/* 47 */     return Optional.ofNullable(this.dimensions.get(LevelStem.OVERWORLD));
/*    */   }
/*    */ 
/*    */   
/*    */   private static DataResult<WorldPreset> requireOverworld(WorldPreset paramWorldPreset) {
/* 52 */     if (paramWorldPreset.overworld().isEmpty()) {
/* 53 */       return DataResult.error(() -> "Missing overworld dimension");
/*    */     }
/* 55 */     return DataResult.success(paramWorldPreset, Lifecycle.stable());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\presets\WorldPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */