/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DataComponentType<T>
/*     */ {
/*  19 */   public static final Codec<DataComponentType<?>> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec());
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentType<?>> STREAM_CODEC = StreamCodec.recursive(paramStreamCodec -> ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE)); public static final Codec<DataComponentType<?>> PERSISTENT_CODEC;
/*     */   static {
/*  22 */     PERSISTENT_CODEC = CODEC.validate(paramDataComponentType -> paramDataComponentType.isTransient() ? DataResult.error(()) : DataResult.success(paramDataComponentType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static final Codec<Map<DataComponentType<?>, Object>> VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, DataComponentType::codecOrThrow);
/*     */   
/*     */   static <T> Builder<T> builder() {
/*  30 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Codec<T> codecOrThrow() {
/*  36 */     Codec<T> codec = codec();
/*  37 */     if (codec == null) {
/*  38 */       throw new IllegalStateException(String.valueOf(this) + " is not a persistent component");
/*     */     }
/*  40 */     return codec;
/*     */   }
/*     */   
/*     */   default boolean isTransient() {
/*  44 */     return (codec() == null);
/*     */   }
/*     */   Codec<T> codec();
/*     */   
/*     */   boolean ignoreSwapAnimation();
/*     */   
/*     */   StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*     */   
/*     */   public static class Builder<T> { private Codec<T> codec;
/*     */     private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private boolean cacheEncoding;
/*     */     private boolean ignoreSwapAnimation;
/*     */     
/*     */     public Builder<T> persistent(Codec<T> param1Codec) {
/*  58 */       this.codec = param1Codec;
/*  59 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> param1StreamCodec) {
/*  66 */       this.streamCodec = param1StreamCodec;
/*  67 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> cacheEncoding() {
/*  71 */       this.cacheEncoding = true;
/*  72 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentType<T> build() {
/*  76 */       StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = Objects.<StreamCodec>requireNonNullElseGet(this.streamCodec, () -> ByteBufCodecs.fromCodecWithRegistries(Objects.<Codec>requireNonNull(this.codec, "Missing Codec for component")));
/*     */ 
/*     */ 
/*     */       
/*  80 */       Codec<T> codec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  81 */       return new SimpleType<>(codec, streamCodec, this.ignoreSwapAnimation);
/*     */     }
/*     */     
/*     */     public Builder<T> ignoreSwapAnimation() {
/*  85 */       this.ignoreSwapAnimation = true;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     private static class SimpleType<T>
/*     */       implements DataComponentType<T> {
/*     */       private final Codec<T> codec;
/*     */       private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */       private final boolean ignoreSwapAnimation;
/*     */       
/*     */       SimpleType(Codec<T> param2Codec, StreamCodec<? super RegistryFriendlyByteBuf, T> param2StreamCodec, boolean param2Boolean) {
/*  96 */         this.codec = param2Codec;
/*  97 */         this.streamCodec = param2StreamCodec;
/*  98 */         this.ignoreSwapAnimation = param2Boolean;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean ignoreSwapAnimation() {
/* 103 */         return this.ignoreSwapAnimation;
/*     */       }
/*     */ 
/*     */       
/*     */       public Codec<T> codec() {
/* 108 */         return this.codec;
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 113 */         return this.streamCodec;
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 118 */         return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); } } } private static class SimpleType<T> implements DataComponentType<T> { public String toString() { return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); }
/*     */ 
/*     */     
/*     */     private final Codec<T> codec;
/*     */     private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private final boolean ignoreSwapAnimation;
/*     */     
/*     */     SimpleType(Codec<T> param1Codec, StreamCodec<? super RegistryFriendlyByteBuf, T> param1StreamCodec, boolean param1Boolean) {
/*     */       this.codec = param1Codec;
/*     */       this.streamCodec = param1StreamCodec;
/*     */       this.ignoreSwapAnimation = param1Boolean;
/*     */     }
/*     */     
/*     */     public boolean ignoreSwapAnimation() {
/*     */       return this.ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/*     */       return this.codec;
/*     */     }
/*     */     
/*     */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/*     */       return this.streamCodec;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */