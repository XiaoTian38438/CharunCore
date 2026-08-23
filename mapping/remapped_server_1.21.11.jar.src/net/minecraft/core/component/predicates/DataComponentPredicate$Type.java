/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Type<T extends DataComponentPredicate>
/*    */ {
/* 42 */   public static final Codec<Type<?>> CODEC = Codec.either(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE
/* 43 */       .byNameCodec(), BuiltInRegistries.DATA_COMPONENT_TYPE
/* 44 */       .byNameCodec())
/* 45 */     .xmap(Type::copyOrCreateType, Type::unpackType);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static final StreamCodec<RegistryFriendlyByteBuf, Type<?>> STREAM_CODEC = ByteBufCodecs.either(
/* 51 */       ByteBufCodecs.registry(Registries.DATA_COMPONENT_PREDICATE_TYPE), 
/* 52 */       ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE))
/* 53 */     .map(Type::copyOrCreateType, Type::unpackType);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T extends Type<?>> Either<T, DataComponentType<?>> unpackType(T paramT) {
/* 59 */     DataComponentPredicate.AnyValueType anyValueType = (DataComponentPredicate.AnyValueType)paramT; return (paramT instanceof DataComponentPredicate.AnyValueType) ? Either.right(anyValueType.componentType()) : Either.left(paramT);
/*    */   }
/*    */   
/*    */   private static Type<?> copyOrCreateType(Either<Type<?>, DataComponentType<?>> paramEither) {
/* 63 */     return (Type)paramEither.map(paramType -> paramType, DataComponentPredicate.AnyValueType::create);
/*    */   }
/*    */   
/*    */   Codec<T> codec();
/*    */   
/*    */   MapCodec<DataComponentPredicate.Single<T>> wrappedCodec();
/*    */   
/*    */   StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */