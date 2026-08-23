/*    */ package com.mojang.serialization;
/*    */ 
/*    */ 
/*    */ public class Lifecycle
/*    */ {
/*  6 */   private static final Lifecycle STABLE = new Lifecycle()
/*    */     {
/*    */       public String toString() {
/*  9 */         return "Stable";
/*    */       }
/*    */     };
/* 12 */   private static final Lifecycle EXPERIMENTAL = new Lifecycle()
/*    */     {
/*    */       public String toString() {
/* 15 */         return "Experimental";
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public static final class Deprecated
/*    */     extends Lifecycle
/*    */   {
/*    */     private final int since;
/*    */     
/*    */     public Deprecated(int param1Int) {
/* 26 */       this.since = param1Int;
/*    */     }
/*    */     
/*    */     public int since() {
/* 30 */       return this.since;
/*    */     }
/*    */   }
/*    */   
/*    */   public static Lifecycle experimental() {
/* 35 */     return EXPERIMENTAL;
/*    */   }
/*    */   
/*    */   public static Lifecycle stable() {
/* 39 */     return STABLE;
/*    */   }
/*    */   
/*    */   public static Lifecycle deprecated(int paramInt) {
/* 43 */     return new Deprecated(paramInt);
/*    */   }
/*    */   
/*    */   public Lifecycle add(Lifecycle paramLifecycle) {
/* 47 */     if (this == EXPERIMENTAL || paramLifecycle == EXPERIMENTAL) {
/* 48 */       return EXPERIMENTAL;
/*    */     }
/* 50 */     if (this instanceof Deprecated) {
/* 51 */       if (paramLifecycle instanceof Deprecated && ((Deprecated)paramLifecycle).since < ((Deprecated)this).since) {
/* 52 */         return paramLifecycle;
/*    */       }
/* 54 */       return this;
/*    */     } 
/* 56 */     if (paramLifecycle instanceof Deprecated) {
/* 57 */       return paramLifecycle;
/*    */     }
/* 59 */     return STABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Lifecycle.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */