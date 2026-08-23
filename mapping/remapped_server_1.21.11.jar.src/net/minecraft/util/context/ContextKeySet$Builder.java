/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
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
/* 32 */   private final Set<ContextKey<?>> required = Sets.newIdentityHashSet();
/* 33 */   private final Set<ContextKey<?>> optional = Sets.newIdentityHashSet();
/*    */   
/*    */   public Builder required(ContextKey<?> paramContextKey) {
/* 36 */     if (this.optional.contains(paramContextKey)) {
/* 37 */       throw new IllegalArgumentException("Parameter " + String.valueOf(paramContextKey.name()) + " is already optional");
/*    */     }
/* 39 */     this.required.add(paramContextKey);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public Builder optional(ContextKey<?> paramContextKey) {
/* 44 */     if (this.required.contains(paramContextKey)) {
/* 45 */       throw new IllegalArgumentException("Parameter " + String.valueOf(paramContextKey.name()) + " is already required");
/*    */     }
/* 47 */     this.optional.add(paramContextKey);
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public ContextKeySet build() {
/* 52 */     return new ContextKeySet(this.required, this.optional);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\context\ContextKeySet$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */