/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
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
/*    */ class DecoderState<T>
/*    */ {
/* 56 */   private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());
/*    */   
/*    */   private final DynamicOps<T> ops;
/* 59 */   private final List<E> elements = new ArrayList<>();
/* 60 */   private final Stream.Builder<T> failed = Stream.builder();
/* 61 */   private DataResult<Unit> result = INITIAL_RESULT;
/*    */   private int totalCount;
/*    */   
/*    */   private DecoderState(DynamicOps<T> paramDynamicOps) {
/* 65 */     this.ops = paramDynamicOps;
/*    */   }
/*    */   
/*    */   public void accept(T paramT) {
/* 69 */     this.totalCount++;
/* 70 */     if (this.elements.size() >= ListCodec.this.maxSize) {
/* 71 */       this.failed.add(paramT);
/*    */       return;
/*    */     } 
/* 74 */     DataResult dataResult = ListCodec.this.elementCodec.decode(this.ops, paramT);
/* 75 */     dataResult.error().ifPresent(paramError -> this.failed.add((T)paramObject));
/* 76 */     dataResult.resultOrPartial().ifPresent(paramPair -> this.elements.add((E)paramPair.getFirst()));
/* 77 */     this.result = this.result.apply2stable((paramUnit, paramPair) -> paramUnit, dataResult);
/*    */   }
/*    */   
/*    */   public DataResult<Pair<List<E>, T>> build() {
/* 81 */     if (this.elements.size() < ListCodec.this.minSize) {
/* 82 */       return ListCodec.this.createTooShortError(this.elements.size());
/*    */     }
/* 84 */     Object object = this.ops.createList(this.failed.build());
/* 85 */     Pair pair = Pair.of(List.copyOf(this.elements), object);
/* 86 */     if (this.totalCount > ListCodec.this.maxSize) {
/* 87 */       this.result = ListCodec.this.createTooLongError(this.totalCount);
/*    */     }
/* 89 */     return this.result.map(paramUnit -> paramPair).setPartial(pair);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\ListCodec$DecoderState.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */