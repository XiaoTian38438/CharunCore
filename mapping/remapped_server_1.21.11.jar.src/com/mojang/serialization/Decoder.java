/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.codecs.FieldDecoder;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Decoder<A>
/*     */ {
/*     */   default <T> DataResult<A> parse(DynamicOps<T> paramDynamicOps, T paramT) {
/*  18 */     return decode(paramDynamicOps, paramT).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   default <T> DataResult<Pair<A, T>> decode(Dynamic<T> paramDynamic) {
/*  22 */     return decode(paramDynamic.getOps(), paramDynamic.getValue());
/*     */   }
/*     */   
/*     */   default <T> DataResult<A> parse(Dynamic<T> paramDynamic) {
/*  26 */     return decode(paramDynamic).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   default Terminal<A> terminal() {
/*  30 */     return this::parse;
/*     */   }
/*     */   
/*     */   default Boxed<A> boxed() {
/*  34 */     return this::decode;
/*     */   }
/*     */   
/*     */   default Simple<A> simple() {
/*  38 */     return this::parse;
/*     */   }
/*     */   
/*     */   default MapDecoder<A> fieldOf(String paramString) {
/*  42 */     return (MapDecoder<A>)new FieldDecoder(paramString, this);
/*     */   }
/*     */   
/*     */   default <B> Decoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
/*  46 */     return new Decoder<B>()
/*     */       {
/*     */         public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  49 */           return Decoder.this.decode(param1DynamicOps, param1T).flatMap(param1Pair -> ((DataResult)param1Function.apply(param1Pair.getFirst())).map(()));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  54 */           return Decoder.this.toString() + "[flatMapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> Decoder<B> map(final Function<? super A, ? extends B> function) {
/*  60 */     return new Decoder<B>()
/*     */       {
/*     */         public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  63 */           return Decoder.this.decode(param1DynamicOps, param1T).map(param1Pair -> param1Pair.mapFirst(param1Function));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  68 */           return Decoder.this.toString() + "[mapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Decoder<A> promotePartial(final Consumer<String> onError) {
/*  74 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  77 */           return Decoder.this.<T>decode(param1DynamicOps, param1T).promotePartial(onError);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  82 */           return Decoder.this.toString() + "[promotePartial]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Decoder<A> withLifecycle(final Lifecycle lifecycle) {
/*  88 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*  91 */           return Decoder.this.<T>decode(param1DynamicOps, param1T).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  96 */           return Decoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofTerminal(Terminal<? extends A> paramTerminal) {
/* 102 */     return paramTerminal.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofBoxed(Boxed<? extends A> paramBoxed) {
/* 106 */     return paramBoxed.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofSimple(Simple<? extends A> paramSimple) {
/* 110 */     return paramSimple.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> MapDecoder<A> unit(A paramA) {
/* 114 */     return unit(() -> paramObject);
/*     */   }
/*     */   
/*     */   static <A> MapDecoder<A> unit(final Supplier<A> instance) {
/* 118 */     return new MapDecoder.Implementation<A>()
/*     */       {
/*     */         public <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike) {
/* 121 */           return DataResult.success(instance.get());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 126 */           return Stream.empty();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 131 */           return "UnitDecoder[" + String.valueOf(instance.get()) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> error(final String error) {
/* 137 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/* 140 */           return DataResult.error(() -> param1String);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 145 */           return "ErrorDecoder[" + error + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT);
/*     */   
/*     */   public static interface Terminal<A> { <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, T param1T);
/*     */     
/* 154 */     default Decoder<A> decoder() { return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 157 */             return Decoder.Terminal.this.decode(param2DynamicOps, param2T).map(param2Object -> Pair.of(param2Object, param2DynamicOps.empty()));
/*     */           }
/*     */           
/*     */           public String toString()
/*     */           {
/* 162 */             return "TerminalDecoder[" + String.valueOf(Decoder.Terminal.this) + "]"; } }; } } class null implements Decoder<A> { public String toString() { return "TerminalDecoder[" + String.valueOf(this.this$0) + "]"; }
/*     */ 
/*     */     
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.decode(param1DynamicOps, param1T).map(param1Object -> Pair.of(param1Object, param1DynamicOps.empty()));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface Boxed<A> { <T> DataResult<Pair<A, T>> decode(Dynamic<T> param1Dynamic);
/*     */     
/* 172 */     default Decoder<A> decoder() { return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 175 */             return Decoder.Boxed.this.decode(new Dynamic<>(param2DynamicOps, param2T));
/*     */           }
/*     */           
/*     */           public String toString()
/*     */           {
/* 180 */             return "BoxedDecoder[" + String.valueOf(Decoder.Boxed.this) + "]"; } }; } } class null implements Decoder<A> { public String toString() { return "BoxedDecoder[" + String.valueOf(this.this$0) + "]"; }
/*     */ 
/*     */     
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.decode(new Dynamic<>(param1DynamicOps, param1T));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface Simple<A> { <T> DataResult<A> decode(Dynamic<T> param1Dynamic);
/*     */     
/* 190 */     default Decoder<A> decoder() { return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 193 */             return Decoder.Simple.this.decode(new Dynamic<>(param2DynamicOps, param2T)).map(param2Object -> Pair.of(param2Object, param2DynamicOps.empty()));
/*     */           }
/*     */           
/*     */           public String toString()
/*     */           {
/* 198 */             return "SimpleDecoder[" + String.valueOf(Decoder.Simple.this) + "]"; } }; } } class null implements Decoder<A> { public String toString() { return "SimpleDecoder[" + String.valueOf(this.this$0) + "]"; }
/*     */ 
/*     */     
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.decode(new Dynamic<>(param1DynamicOps, param1T)).map(param1Object -> Pair.of(param1Object, param1DynamicOps.empty()));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Decoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */