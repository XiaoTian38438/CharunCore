/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionCheckTypes {
/*    */   public static MapCodec<? extends PermissionCheck> bootstrap(Registry<MapCodec<? extends PermissionCheck>> paramRegistry) {
/*  9 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("always_pass"), PermissionCheck.AlwaysPass.MAP_CODEC);
/* 10 */     return (MapCodec<? extends PermissionCheck>)Registry.register(paramRegistry, Identifier.withDefaultNamespace("require"), PermissionCheck.Require.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\PermissionCheckTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */