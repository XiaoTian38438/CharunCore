/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
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
/*    */ public class Builder<B extends ByteBuf, V, T>
/*    */ {
/* 70 */   private final List<IdDispatchCodec.Entry<B, V, T>> entries = new ArrayList<>();
/*    */   private final Function<V, ? extends T> typeGetter;
/*    */   
/*    */   Builder(Function<V, ? extends T> paramFunction) {
/* 74 */     this.typeGetter = paramFunction;
/*    */   }
/*    */   
/*    */   public Builder<B, V, T> add(T paramT, StreamCodec<? super B, ? extends V> paramStreamCodec) {
/* 78 */     this.entries.add(new IdDispatchCodec.Entry<>(paramStreamCodec, paramT));
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public IdDispatchCodec<B, V, T> build() {
/* 83 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 84 */     object2IntOpenHashMap.defaultReturnValue(-2);
/*    */     
/* 86 */     for (IdDispatchCodec.Entry<B, V, T> entry : this.entries) {
/* 87 */       int i = object2IntOpenHashMap.size();
/* 88 */       int j = object2IntOpenHashMap.putIfAbsent(entry.type, i);
/* 89 */       if (j != -2) {
/* 90 */         throw new IllegalStateException("Duplicate registration for type " + String.valueOf(entry.type));
/*    */       }
/*    */     } 
/*    */     
/* 94 */     return new IdDispatchCodec<>(this.typeGetter, List.copyOf(this.entries), (Object2IntMap<T>)object2IntOpenHashMap);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\IdDispatchCodec$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */