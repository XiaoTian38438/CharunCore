/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ class StrictEither<T>
/*     */   extends MapCodec<T>
/*     */ {
/*     */   private final String typeFieldName;
/*     */   private final MapCodec<T> typed;
/*     */   private final MapCodec<T> fuzzy;
/*     */   
/*     */   public StrictEither(String paramString, MapCodec<T> paramMapCodec1, MapCodec<T> paramMapCodec2) {
/*  98 */     this.typeFieldName = paramString;
/*  99 */     this.typed = paramMapCodec1;
/* 100 */     this.fuzzy = paramMapCodec2;
/*     */   }
/*     */ 
/*     */   
/*     */   public <O> DataResult<T> decode(DynamicOps<O> paramDynamicOps, MapLike<O> paramMapLike) {
/* 105 */     if (paramMapLike.get(this.typeFieldName) != null) {
/* 106 */       return this.typed.decode(paramDynamicOps, paramMapLike);
/*     */     }
/* 108 */     return this.fuzzy.decode(paramDynamicOps, paramMapLike);
/*     */   }
/*     */ 
/*     */   
/*     */   public <O> RecordBuilder<O> encode(T paramT, DynamicOps<O> paramDynamicOps, RecordBuilder<O> paramRecordBuilder) {
/* 113 */     return this.fuzzy.encode(paramT, paramDynamicOps, paramRecordBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T1> Stream<T1> keys(DynamicOps<T1> paramDynamicOps) {
/* 118 */     return Stream.<T1>concat(this.typed.keys(paramDynamicOps), this.fuzzy.keys(paramDynamicOps)).distinct();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ComponentSerialization$StrictEither.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */