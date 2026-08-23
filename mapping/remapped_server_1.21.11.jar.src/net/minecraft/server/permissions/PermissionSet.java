/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PermissionSet
/*    */ {
/*    */   public static final PermissionSet NO_PERMISSIONS = paramPermission -> false;
/*    */   public static final PermissionSet ALL_PERMISSIONS = paramPermission -> true;
/*    */   
/*    */   default PermissionSet union(PermissionSet paramPermissionSet) {
/* 11 */     if (paramPermissionSet instanceof PermissionSetUnion) {
/* 12 */       return paramPermissionSet.union(this);
/*    */     }
/* 14 */     return new PermissionSetUnion(this, paramPermissionSet);
/*    */   }
/*    */   
/*    */   boolean hasPermission(Permission paramPermission);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\PermissionSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */