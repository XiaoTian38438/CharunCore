/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public interface ServicesKeySet {
/*    */   public static final ServicesKeySet EMPTY = paramServicesKeyType -> List.of();
/*    */   
/*    */   static ServicesKeySet lazy(Supplier<ServicesKeySet> paramSupplier) {
/* 11 */     return paramServicesKeyType -> ((ServicesKeySet)paramSupplier.get()).keys(paramServicesKeyType);
/*    */   }
/*    */   
/*    */   Collection<ServicesKeyInfo> keys(ServicesKeyType paramServicesKeyType);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\ServicesKeySet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */