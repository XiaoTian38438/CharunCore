/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ContextKeySet
/*    */ {
/*    */   private final Set<ContextKey<?>> required;
/*    */   private final Set<ContextKey<?>> allowed;
/*    */   
/*    */   ContextKeySet(Set<ContextKey<?>> paramSet1, Set<ContextKey<?>> paramSet2) {
/* 14 */     this.required = Set.copyOf(paramSet1);
/* 15 */     this.allowed = Set.copyOf((Collection<? extends ContextKey<?>>)Sets.union(paramSet1, paramSet2));
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> required() {
/* 19 */     return this.required;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> allowed() {
/* 23 */     return this.allowed;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return "[" + Joiner.on(", ").join(this.allowed.stream().map(paramContextKey -> (this.required.contains(paramContextKey) ? "!" : "") + (this.required.contains(paramContextKey) ? "!" : "")).iterator()) + "]";
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 32 */     private final Set<ContextKey<?>> required = Sets.newIdentityHashSet();
/* 33 */     private final Set<ContextKey<?>> optional = Sets.newIdentityHashSet();
/*    */     
/*    */     public Builder required(ContextKey<?> param1ContextKey) {
/* 36 */       if (this.optional.contains(param1ContextKey)) {
/* 37 */         throw new IllegalArgumentException("Parameter " + String.valueOf(param1ContextKey.name()) + " is already optional");
/*    */       }
/* 39 */       this.required.add(param1ContextKey);
/* 40 */       return this;
/*    */     }
/*    */     
/*    */     public Builder optional(ContextKey<?> param1ContextKey) {
/* 44 */       if (this.required.contains(param1ContextKey)) {
/* 45 */         throw new IllegalArgumentException("Parameter " + String.valueOf(param1ContextKey.name()) + " is already required");
/*    */       }
/* 47 */       this.optional.add(param1ContextKey);
/* 48 */       return this;
/*    */     }
/*    */     
/*    */     public ContextKeySet build() {
/* 52 */       return new ContextKeySet(this.required, this.optional);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\context\ContextKeySet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */