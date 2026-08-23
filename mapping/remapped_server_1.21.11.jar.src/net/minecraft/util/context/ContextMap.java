/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContextMap
/*    */ {
/*    */   private final Map<ContextKey<?>, Object> params;
/*    */   
/*    */   ContextMap(Map<ContextKey<?>, Object> paramMap) {
/* 16 */     this.params = paramMap;
/*    */   }
/*    */   
/*    */   public boolean has(ContextKey<?> paramContextKey) {
/* 20 */     return this.params.containsKey(paramContextKey);
/*    */   }
/*    */   
/*    */   public <T> T getOrThrow(ContextKey<T> paramContextKey) {
/* 24 */     Object object = this.params.get(paramContextKey);
/* 25 */     if (object == null) {
/* 26 */       throw new NoSuchElementException(paramContextKey.name().toString());
/*    */     }
/*    */     
/* 29 */     return (T)object;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getOptional(ContextKey<T> paramContextKey) {
/* 34 */     return (T)this.params.get(paramContextKey);
/*    */   }
/*    */ 
/*    */   
/*    */   @Contract("_,!null->!null; _,_->_")
/*    */   public <T> T getOrDefault(ContextKey<T> paramContextKey, T paramT) {
/* 40 */     return (T)this.params.getOrDefault(paramContextKey, paramT);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 44 */     private final Map<ContextKey<?>, Object> params = new IdentityHashMap<>();
/*    */     
/*    */     public <T> Builder withParameter(ContextKey<T> param1ContextKey, T param1T) {
/* 47 */       this.params.put(param1ContextKey, param1T);
/* 48 */       return this;
/*    */     }
/*    */     
/*    */     public <T> Builder withOptionalParameter(ContextKey<T> param1ContextKey, T param1T) {
/* 52 */       if (param1T == null) {
/* 53 */         this.params.remove(param1ContextKey);
/*    */       } else {
/* 55 */         this.params.put(param1ContextKey, param1T);
/*    */       } 
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public <T> T getParameter(ContextKey<T> param1ContextKey) {
/* 61 */       Object object = this.params.get(param1ContextKey);
/* 62 */       if (object == null) {
/* 63 */         throw new NoSuchElementException(param1ContextKey.name().toString());
/*    */       }
/*    */       
/* 66 */       return (T)object;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> T getOptionalParameter(ContextKey<T> param1ContextKey) {
/* 71 */       return (T)this.params.get(param1ContextKey);
/*    */     }
/*    */     
/*    */     public ContextMap create(ContextKeySet param1ContextKeySet) {
/* 75 */       Sets.SetView setView1 = Sets.difference(this.params.keySet(), param1ContextKeySet.allowed());
/* 76 */       if (!setView1.isEmpty()) {
/* 77 */         throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + String.valueOf(setView1));
/*    */       }
/*    */       
/* 80 */       Sets.SetView setView2 = Sets.difference(param1ContextKeySet.required(), this.params.keySet());
/* 81 */       if (!setView2.isEmpty()) {
/* 82 */         throw new IllegalArgumentException("Missing required parameters: " + String.valueOf(setView2));
/*    */       }
/*    */       
/* 85 */       return new ContextMap(this.params);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\context\ContextMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */