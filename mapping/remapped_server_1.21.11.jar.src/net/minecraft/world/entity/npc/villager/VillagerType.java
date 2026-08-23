/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public final class VillagerType {
/* 21 */   public static final ResourceKey<VillagerType> DESERT = createKey("desert");
/* 22 */   public static final ResourceKey<VillagerType> JUNGLE = createKey("jungle");
/* 23 */   public static final ResourceKey<VillagerType> PLAINS = createKey("plains");
/* 24 */   public static final ResourceKey<VillagerType> SAVANNA = createKey("savanna");
/* 25 */   public static final ResourceKey<VillagerType> SNOW = createKey("snow");
/* 26 */   public static final ResourceKey<VillagerType> SWAMP = createKey("swamp");
/* 27 */   public static final ResourceKey<VillagerType> TAIGA = createKey("taiga");
/*    */   
/*    */   private static ResourceKey<VillagerType> createKey(String paramString) {
/* 30 */     return ResourceKey.create(Registries.VILLAGER_TYPE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/* 33 */   public static final Codec<Holder<VillagerType>> CODEC = (Codec<Holder<VillagerType>>)RegistryFixedCodec.create(Registries.VILLAGER_TYPE);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VillagerType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.VILLAGER_TYPE); private static final Map<ResourceKey<Biome>, ResourceKey<VillagerType>> BY_BIOME;
/*    */   
/*    */   private static VillagerType register(Registry<VillagerType> paramRegistry, ResourceKey<VillagerType> paramResourceKey) {
/* 37 */     return (VillagerType)Registry.register(paramRegistry, paramResourceKey, new VillagerType());
/*    */   }
/*    */   
/*    */   public static VillagerType bootstrap(Registry<VillagerType> paramRegistry) {
/* 41 */     register(paramRegistry, DESERT);
/* 42 */     register(paramRegistry, JUNGLE);
/* 43 */     register(paramRegistry, PLAINS);
/* 44 */     register(paramRegistry, SAVANNA);
/* 45 */     register(paramRegistry, SNOW);
/* 46 */     register(paramRegistry, SWAMP);
/* 47 */     return register(paramRegistry, TAIGA);
/*    */   }
/*    */   static {
/* 50 */     BY_BIOME = (Map<ResourceKey<Biome>, ResourceKey<VillagerType>>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*    */           paramHashMap.put(Biomes.BADLANDS, DESERT);
/*    */           paramHashMap.put(Biomes.DESERT, DESERT);
/*    */           paramHashMap.put(Biomes.ERODED_BADLANDS, DESERT);
/*    */           paramHashMap.put(Biomes.WOODED_BADLANDS, DESERT);
/*    */           paramHashMap.put(Biomes.BAMBOO_JUNGLE, JUNGLE);
/*    */           paramHashMap.put(Biomes.JUNGLE, JUNGLE);
/*    */           paramHashMap.put(Biomes.SPARSE_JUNGLE, JUNGLE);
/*    */           paramHashMap.put(Biomes.SAVANNA_PLATEAU, SAVANNA);
/*    */           paramHashMap.put(Biomes.SAVANNA, SAVANNA);
/*    */           paramHashMap.put(Biomes.WINDSWEPT_SAVANNA, SAVANNA);
/*    */           paramHashMap.put(Biomes.DEEP_FROZEN_OCEAN, SNOW);
/*    */           paramHashMap.put(Biomes.FROZEN_OCEAN, SNOW);
/*    */           paramHashMap.put(Biomes.FROZEN_RIVER, SNOW);
/*    */           paramHashMap.put(Biomes.ICE_SPIKES, SNOW);
/*    */           paramHashMap.put(Biomes.SNOWY_BEACH, SNOW);
/*    */           paramHashMap.put(Biomes.SNOWY_TAIGA, SNOW);
/*    */           paramHashMap.put(Biomes.SNOWY_PLAINS, SNOW);
/*    */           paramHashMap.put(Biomes.GROVE, SNOW);
/*    */           paramHashMap.put(Biomes.SNOWY_SLOPES, SNOW);
/*    */           paramHashMap.put(Biomes.FROZEN_PEAKS, SNOW);
/*    */           paramHashMap.put(Biomes.JAGGED_PEAKS, SNOW);
/*    */           paramHashMap.put(Biomes.SWAMP, SWAMP);
/*    */           paramHashMap.put(Biomes.MANGROVE_SWAMP, SWAMP);
/*    */           paramHashMap.put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, TAIGA);
/*    */           paramHashMap.put(Biomes.OLD_GROWTH_PINE_TAIGA, TAIGA);
/*    */           paramHashMap.put(Biomes.WINDSWEPT_GRAVELLY_HILLS, TAIGA);
/*    */           paramHashMap.put(Biomes.WINDSWEPT_HILLS, TAIGA);
/*    */           paramHashMap.put(Biomes.TAIGA, TAIGA);
/*    */           paramHashMap.put(Biomes.WINDSWEPT_FOREST, TAIGA);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResourceKey<VillagerType> byBiome(Holder<Biome> paramHolder) {
/* 90 */     Objects.requireNonNull(BY_BIOME); return paramHolder.unwrapKey().map(BY_BIOME::get).orElse(PLAINS);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\villager\VillagerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */