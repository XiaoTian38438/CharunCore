/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public final class ListCodec<E> extends Record implements Codec<List<E>> {
/*    */   private final Codec<E> elementCodec;
/*    */   private final int minSize;
/*    */   private final int maxSize;
/*    */   
/* 17 */   public ListCodec(Codec<E> paramCodec, int paramInt1, int paramInt2) { this.elementCodec = paramCodec; this.minSize = paramInt1; this.maxSize = paramInt2; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/serialization/codecs/ListCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 17 */     //   #17	-> 0 } public Codec<E> elementCodec() { return this.elementCodec; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/serialization/codecs/ListCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 17 */     //   #17	-> 0 } public int minSize() { return this.minSize; } public int maxSize() { return this.maxSize; }
/*    */    private <R> DataResult<R> createTooShortError(int paramInt) {
/* 19 */     return DataResult.error(() -> "List is too short: " + paramInt + ", expected range [" + this.minSize + "-" + this.maxSize + "]");
/*    */   }
/*    */   
/*    */   private <R> DataResult<R> createTooLongError(int paramInt) {
/* 23 */     return DataResult.error(() -> "List is too long: " + paramInt + ", expected range [" + this.minSize + "-" + this.maxSize + "]");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(List<E> paramList, DynamicOps<T> paramDynamicOps, T paramT) {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: invokeinterface size : ()I
/*    */     //   6: aload_0
/*    */     //   7: getfield minSize : I
/*    */     //   10: if_icmpge -> 24
/*    */     //   13: aload_0
/*    */     //   14: aload_1
/*    */     //   15: invokeinterface size : ()I
/*    */     //   20: invokevirtual createTooShortError : (I)Lcom/mojang/serialization/DataResult;
/*    */     //   23: areturn
/*    */     //   24: aload_1
/*    */     //   25: invokeinterface size : ()I
/*    */     //   30: aload_0
/*    */     //   31: getfield maxSize : I
/*    */     //   34: if_icmple -> 48
/*    */     //   37: aload_0
/*    */     //   38: aload_1
/*    */     //   39: invokeinterface size : ()I
/*    */     //   44: invokevirtual createTooLongError : (I)Lcom/mojang/serialization/DataResult;
/*    */     //   47: areturn
/*    */     //   48: aload_2
/*    */     //   49: invokeinterface listBuilder : ()Lcom/mojang/serialization/ListBuilder;
/*    */     //   54: astore #4
/*    */     //   56: aload_1
/*    */     //   57: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   62: astore #5
/*    */     //   64: aload #5
/*    */     //   66: invokeinterface hasNext : ()Z
/*    */     //   71: ifeq -> 106
/*    */     //   74: aload #5
/*    */     //   76: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   81: astore #6
/*    */     //   83: aload #4
/*    */     //   85: aload_0
/*    */     //   86: getfield elementCodec : Lcom/mojang/serialization/Codec;
/*    */     //   89: aload_2
/*    */     //   90: aload #6
/*    */     //   92: invokeinterface encodeStart : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*    */     //   97: invokeinterface add : (Lcom/mojang/serialization/DataResult;)Lcom/mojang/serialization/ListBuilder;
/*    */     //   102: pop
/*    */     //   103: goto -> 64
/*    */     //   106: aload #4
/*    */     //   108: aload_3
/*    */     //   109: invokeinterface build : (Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*    */     //   114: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     //   #29	-> 13
/*    */     //   #31	-> 24
/*    */     //   #32	-> 37
/*    */     //   #34	-> 48
/*    */     //   #35	-> 56
/*    */     //   #36	-> 83
/*    */     //   #37	-> 103
/*    */     //   #38	-> 106
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<List<E>, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 43 */     return paramDynamicOps.getList(paramT).setLifecycle(Lifecycle.stable()).flatMap(paramConsumer -> {
/*    */           DecoderState decoderState = new DecoderState(paramDynamicOps);
/*    */           Objects.requireNonNull(decoderState);
/*    */           paramConsumer.accept(decoderState::accept);
/*    */           return decoderState.build();
/*    */         });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 52 */     return "ListCodec[" + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */   
/*    */   private class DecoderState<T> {
/* 56 */     private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());
/*    */     
/*    */     private final DynamicOps<T> ops;
/* 59 */     private final List<E> elements = new ArrayList<>();
/* 60 */     private final Stream.Builder<T> failed = Stream.builder();
/* 61 */     private DataResult<Unit> result = INITIAL_RESULT;
/*    */     private int totalCount;
/*    */     
/*    */     private DecoderState(DynamicOps<T> param1DynamicOps) {
/* 65 */       this.ops = param1DynamicOps;
/*    */     }
/*    */     
/*    */     public void accept(T param1T) {
/* 69 */       this.totalCount++;
/* 70 */       if (this.elements.size() >= ListCodec.this.maxSize) {
/* 71 */         this.failed.add(param1T);
/*    */         return;
/*    */       } 
/* 74 */       DataResult dataResult = ListCodec.this.elementCodec.decode(this.ops, param1T);
/* 75 */       dataResult.error().ifPresent(param1Error -> this.failed.add((T)param1Object));
/* 76 */       dataResult.resultOrPartial().ifPresent(param1Pair -> this.elements.add((E)param1Pair.getFirst()));
/* 77 */       this.result = this.result.apply2stable((param1Unit, param1Pair) -> param1Unit, dataResult);
/*    */     }
/*    */     
/*    */     public DataResult<Pair<List<E>, T>> build() {
/* 81 */       if (this.elements.size() < ListCodec.this.minSize) {
/* 82 */         return ListCodec.this.createTooShortError(this.elements.size());
/*    */       }
/* 84 */       Object object = this.ops.createList(this.failed.build());
/* 85 */       Pair pair = Pair.of(List.copyOf(this.elements), object);
/* 86 */       if (this.totalCount > ListCodec.this.maxSize) {
/* 87 */         this.result = ListCodec.this.createTooLongError(this.totalCount);
/*    */       }
/* 89 */       return this.result.map(param1Unit -> param1Pair).setPartial(pair);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\ListCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */