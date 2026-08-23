/*    */ package net.minecraft.server.permissions;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSet;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class PermissionSetUnion implements PermissionSet {
/*  8 */   private final ReferenceSet<PermissionSet> permissions = (ReferenceSet<PermissionSet>)new ReferenceArraySet();
/*    */   
/*    */   PermissionSetUnion(PermissionSet paramPermissionSet1, PermissionSet paramPermissionSet2) {
/* 11 */     this.permissions.add(paramPermissionSet1);
/* 12 */     this.permissions.add(paramPermissionSet2);
/* 13 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> paramReferenceSet, PermissionSet paramPermissionSet) {
/* 17 */     this.permissions.addAll((Collection)paramReferenceSet);
/* 18 */     this.permissions.add(paramPermissionSet);
/* 19 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> paramReferenceSet1, ReferenceSet<PermissionSet> paramReferenceSet2) {
/* 23 */     this.permissions.addAll((Collection)paramReferenceSet1);
/* 24 */     this.permissions.addAll((Collection)paramReferenceSet2);
/* 25 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPermission(Permission paramPermission) {
/* 30 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet permissionSet = objectIterator.next();
/* 31 */       if (permissionSet.hasPermission(paramPermission)) {
/* 32 */         return true;
/*    */       } }
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public PermissionSet union(PermissionSet paramPermissionSet) {
/* 40 */     if (paramPermissionSet instanceof PermissionSetUnion) { PermissionSetUnion permissionSetUnion = (PermissionSetUnion)paramPermissionSet;
/* 41 */       return new PermissionSetUnion(this.permissions, permissionSetUnion.permissions); }
/*    */     
/* 43 */     return new PermissionSetUnion(this.permissions, paramPermissionSet);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public ReferenceSet<PermissionSet> getPermissions() {
/* 48 */     return (ReferenceSet<PermissionSet>)new ReferenceArraySet(this.permissions);
/*    */   }
/*    */   
/*    */   private void ensureNoUnionsWithinUnions() {
/* 52 */     for (ObjectIterator<PermissionSet> objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet permissionSet = objectIterator.next();
/* 53 */       if (permissionSet instanceof PermissionSetUnion)
/* 54 */         throw new IllegalArgumentException("Cannot have PermissionSetUnion within another PermissionSetUnion");  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\PermissionSetUnion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */