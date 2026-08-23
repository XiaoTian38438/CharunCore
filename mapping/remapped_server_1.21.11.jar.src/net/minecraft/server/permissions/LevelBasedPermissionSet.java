/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelBasedPermissionSet
/*    */   extends PermissionSet
/*    */ {
/*    */   @Deprecated
/* 14 */   public static final LevelBasedPermissionSet ALL = create(PermissionLevel.ALL);
/* 15 */   public static final LevelBasedPermissionSet MODERATOR = create(PermissionLevel.MODERATORS);
/* 16 */   public static final LevelBasedPermissionSet GAMEMASTER = create(PermissionLevel.GAMEMASTERS);
/* 17 */   public static final LevelBasedPermissionSet ADMIN = create(PermissionLevel.ADMINS);
/* 18 */   public static final LevelBasedPermissionSet OWNER = create(PermissionLevel.OWNERS);
/*    */ 
/*    */   
/*    */   PermissionLevel level();
/*    */   
/*    */   default boolean hasPermission(Permission paramPermission) {
/* 24 */     if (paramPermission instanceof Permission.HasCommandLevel) { Permission.HasCommandLevel hasCommandLevel = (Permission.HasCommandLevel)paramPermission;
/* 25 */       return level().isEqualOrHigherThan(hasCommandLevel.level()); }
/*    */ 
/*    */     
/* 28 */     if (paramPermission.equals(Permissions.COMMANDS_ENTITY_SELECTORS)) {
/* 29 */       return level().isEqualOrHigherThan(PermissionLevel.GAMEMASTERS);
/*    */     }
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default PermissionSet union(PermissionSet paramPermissionSet) {
/* 36 */     if (paramPermissionSet instanceof LevelBasedPermissionSet) { LevelBasedPermissionSet levelBasedPermissionSet = (LevelBasedPermissionSet)paramPermissionSet;
/* 37 */       if (level().isEqualOrHigherThan(levelBasedPermissionSet.level())) {
/* 38 */         return levelBasedPermissionSet;
/*    */       }
/* 40 */       return this; }
/*    */ 
/*    */     
/* 43 */     return super.union(paramPermissionSet);
/*    */   }
/*    */   
/*    */   static LevelBasedPermissionSet forLevel(PermissionLevel paramPermissionLevel) {
/* 47 */     switch (paramPermissionLevel) { default: throw new MatchException(null, null);case ALL: case MODERATORS: case GAMEMASTERS: case ADMINS: case OWNERS: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 52 */       OWNER;
/*    */   }
/*    */ 
/*    */   
/*    */   private static LevelBasedPermissionSet create(final PermissionLevel level) {
/* 57 */     return new LevelBasedPermissionSet()
/*    */       {
/*    */         public PermissionLevel level() {
/* 60 */           return level;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 65 */           return "permission level: " + level.name();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\LevelBasedPermissionSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */