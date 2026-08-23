/*   */ package net.minecraft.server.permissions;
/*   */ 
/*   */ public class Permissions {
/* 4 */   public static final Permission COMMANDS_MODERATOR = new Permission.HasCommandLevel(PermissionLevel.MODERATORS);
/* 5 */   public static final Permission COMMANDS_GAMEMASTER = new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS);
/* 6 */   public static final Permission COMMANDS_ADMIN = new Permission.HasCommandLevel(PermissionLevel.ADMINS);
/* 7 */   public static final Permission COMMANDS_OWNER = new Permission.HasCommandLevel(PermissionLevel.OWNERS);
/*   */   
/* 9 */   public static final Permission COMMANDS_ENTITY_SELECTORS = Permission.Atom.create("commands/entity_selectors");
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\Permissions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */