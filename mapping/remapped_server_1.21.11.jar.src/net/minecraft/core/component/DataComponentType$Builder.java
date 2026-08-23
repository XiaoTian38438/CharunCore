/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<T>
/*     */ {
/*     */   private Codec<T> codec;
/*     */   private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   private boolean cacheEncoding;
/*     */   private boolean ignoreSwapAnimation;
/*     */   
/*     */   public Builder<T> persistent(Codec<T> paramCodec) {
/*  58 */     this.codec = paramCodec;
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> paramStreamCodec) {
/*  66 */     this.streamCodec = paramStreamCodec;
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> cacheEncoding() {
/*  71 */     this.cacheEncoding = true;
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentType<T> build() {
/*  76 */     StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = Objects.<StreamCodec>requireNonNullElseGet(this.streamCodec, () -> ByteBufCodecs.fromCodecWithRegistries(Objects.<Codec>requireNonNull(this.codec, "Missing Codec for component")));
/*     */ 
/*     */ 
/*     */     
/*  80 */     Codec<T> codec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  81 */     return new SimpleType<>(codec, streamCodec, this.ignoreSwapAnimation);
/*     */   }
/*     */   
/*     */   public Builder<T> ignoreSwapAnimation() {
/*  85 */     this.ignoreSwapAnimation = true;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   private static class SimpleType<T>
/*     */     implements DataComponentType<T> {
/*     */     private final Codec<T> codec;
/*     */     private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private final boolean ignoreSwapAnimation;
/*     */     
/*     */     SimpleType(Codec<T> param2Codec, StreamCodec<? super RegistryFriendlyByteBuf, T> param2StreamCodec, boolean param2Boolean) {
/*  96 */       this.codec = param2Codec;
/*  97 */       this.streamCodec = param2StreamCodec;
/*  98 */       this.ignoreSwapAnimation = param2Boolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean ignoreSwapAnimation() {
/* 103 */       return this.ignoreSwapAnimation;
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<T> codec() {
/* 108 */       return this.codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 113 */       return this.streamCodec;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 118 */       return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentType$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */