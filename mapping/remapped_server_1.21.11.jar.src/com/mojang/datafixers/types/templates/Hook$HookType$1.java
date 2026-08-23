/*    */ package com.mojang.datafixers.types.templates;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
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
/*    */ class null
/*    */   implements Codec<A>
/*    */ {
/*    */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT) {
/* 90 */     return Hook.HookType.this.delegate.codec().decode(paramDynamicOps, Hook.HookType.this.preRead.apply(paramDynamicOps, paramT)).setLifecycle(Lifecycle.experimental());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT) {
/* 95 */     return Hook.HookType.this.delegate.codec().encode(paramA, paramDynamicOps, paramT).map(paramObject -> Hook.HookType.this.postWrite.apply(paramDynamicOps, paramObject)).setLifecycle(Lifecycle.experimental());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Hook$HookType$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */