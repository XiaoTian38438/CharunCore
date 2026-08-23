/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ class SimpleType<T>
/*     */   implements DataComponentType<T>
/*     */ {
/*     */   private final Codec<T> codec;
/*     */   private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   private final boolean ignoreSwapAnimation;
/*     */   
/*     */   SimpleType(Codec<T> paramCodec, StreamCodec<? super RegistryFriendlyByteBuf, T> paramStreamCodec, boolean paramBoolean) {
/*  96 */     this.codec = paramCodec;
/*  97 */     this.streamCodec = paramStreamCodec;
/*  98 */     this.ignoreSwapAnimation = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreSwapAnimation() {
/* 103 */     return this.ignoreSwapAnimation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Codec<T> codec() {
/* 108 */     return this.codec;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 113 */     return this.streamCodec;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentType$Builder$SimpleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */