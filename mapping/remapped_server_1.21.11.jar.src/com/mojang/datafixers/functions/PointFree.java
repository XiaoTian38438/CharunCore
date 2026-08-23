/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PointFree<T>
/*    */ {
/*    */   private volatile boolean initialized;
/*    */   @Nullable
/*    */   private Function<DynamicOps<?>, T> value;
/*    */   
/*    */   public Function<DynamicOps<?>, T> evalCached() {
/* 19 */     if (!this.initialized) {
/* 20 */       synchronized (this) {
/* 21 */         if (!this.initialized) {
/* 22 */           this.value = eval();
/* 23 */           this.initialized = true;
/*    */         } 
/*    */       } 
/*    */     }
/* 27 */     return this.value;
/*    */   }
/*    */   
/*    */   public abstract Type<T> type();
/*    */   
/*    */   public abstract Function<DynamicOps<?>, T> eval();
/*    */   
/*    */   Optional<? extends PointFree<T>> all(PointFreeRule paramPointFreeRule) {
/* 35 */     return Optional.of(this);
/*    */   }
/*    */   
/*    */   Optional<? extends PointFree<T>> one(PointFreeRule paramPointFreeRule) {
/* 39 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 44 */     return toString(0);
/*    */   }
/*    */   
/*    */   public static String indent(int paramInt) {
/* 48 */     return " ".repeat(paramInt);
/*    */   }
/*    */   
/*    */   public abstract String toString(int paramInt);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFree.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */