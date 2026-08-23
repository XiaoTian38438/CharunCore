/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class NumberProviders {
/* 11 */   private static final Codec<NumberProvider> TYPED_CODEC = BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.byNameCodec().dispatch(NumberProvider::getType, LootNumberProviderType::codec);
/*    */   static {
/* 13 */     CODEC = Codec.lazyInitialized(() -> {
/*    */           Codec codec = Codec.withAlternative(TYPED_CODEC, UniformGenerator.CODEC.codec());
/*    */           return Codec.either(ConstantValue.INLINE_CODEC, codec).xmap(Either::unwrap, ());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<NumberProvider> CODEC;
/*    */   
/* 22 */   public static final LootNumberProviderType CONSTANT = register("constant", (MapCodec)ConstantValue.CODEC);
/* 23 */   public static final LootNumberProviderType UNIFORM = register("uniform", (MapCodec)UniformGenerator.CODEC);
/* 24 */   public static final LootNumberProviderType BINOMIAL = register("binomial", (MapCodec)BinomialDistributionGenerator.CODEC);
/* 25 */   public static final LootNumberProviderType SCORE = register("score", (MapCodec)ScoreboardValue.CODEC);
/* 26 */   public static final LootNumberProviderType STORAGE = register("storage", (MapCodec)StorageValue.CODEC);
/* 27 */   public static final LootNumberProviderType ENCHANTMENT_LEVEL = register("enchantment_level", (MapCodec)EnchantmentLevelProvider.CODEC);
/*    */   
/*    */   private static LootNumberProviderType register(String paramString, MapCodec<? extends NumberProvider> paramMapCodec) {
/* 30 */     return (LootNumberProviderType)Registry.register(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, Identifier.withDefaultNamespace(paramString), new LootNumberProviderType(paramMapCodec));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */