/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class NbtProviders {
/* 11 */   private static final Codec<NbtProvider> TYPED_CODEC = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.byNameCodec().dispatch(NbtProvider::getType, LootNbtProviderType::codec);
/*    */   
/* 13 */   public static final Codec<NbtProvider> CODEC = Codec.lazyInitialized(() -> Codec.either(ContextNbtProvider.INLINE_CODEC, TYPED_CODEC).xmap(Either::unwrap, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final LootNbtProviderType STORAGE = register("storage", (MapCodec)StorageNbtProvider.CODEC);
/* 21 */   public static final LootNbtProviderType CONTEXT = register("context", (MapCodec)ContextNbtProvider.MAP_CODEC);
/*    */   
/*    */   private static LootNbtProviderType register(String paramString, MapCodec<? extends NbtProvider> paramMapCodec) {
/* 24 */     return (LootNbtProviderType)Registry.register(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, Identifier.withDefaultNamespace(paramString), new LootNbtProviderType(paramMapCodec));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\providers\nbt\NbtProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */