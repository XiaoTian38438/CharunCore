/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class Builder
/*    */ {
/* 44 */   private final Map<ContextKey<?>, Object> params = new IdentityHashMap<>();
/*    */   
/*    */   public <T> Builder withParameter(ContextKey<T> paramContextKey, T paramT) {
/* 47 */     this.params.put(paramContextKey, paramT);
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public <T> Builder withOptionalParameter(ContextKey<T> paramContextKey, T paramT) {
/* 52 */     if (paramT == null) {
/* 53 */       this.params.remove(paramContextKey);
/*    */     } else {
/* 55 */       this.params.put(paramContextKey, paramT);
/*    */     } 
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public <T> T getParameter(ContextKey<T> paramContextKey) {
/* 61 */     Object object = this.params.get(paramContextKey);
/* 62 */     if (object == null) {
/* 63 */       throw new NoSuchElementException(paramContextKey.name().toString());
/*    */     }
/*    */     
/* 66 */     return (T)object;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getOptionalParameter(ContextKey<T> paramContextKey) {
/* 71 */     return (T)this.params.get(paramContextKey);
/*    */   }
/*    */   
/*    */   public ContextMap create(ContextKeySet paramContextKeySet) {
/* 75 */     Sets.SetView setView1 = Sets.difference(this.params.keySet(), paramContextKeySet.allowed());
/* 76 */     if (!setView1.isEmpty()) {
/* 77 */       throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + String.valueOf(setView1));
/*    */     }
/*    */     
/* 80 */     Sets.SetView setView2 = Sets.difference(paramContextKeySet.required(), this.params.keySet());
/* 81 */     if (!setView2.isEmpty()) {
/* 82 */       throw new IllegalArgumentException("Missing required parameters: " + String.valueOf(setView2));
/*    */     }
/*    */     
/* 85 */     return new ContextMap(this.params);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\context\ContextMap$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */