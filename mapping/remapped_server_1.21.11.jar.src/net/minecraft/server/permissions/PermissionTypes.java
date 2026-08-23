/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PermissionTypes {
/*    */   public static MapCodec<? extends Permission> bootstrap(Registry<MapCodec<? extends Permission>> paramRegistry) {
/*  9 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("atom"), Permission.Atom.MAP_CODEC);
/* 10 */     return (MapCodec<? extends Permission>)Registry.register(paramRegistry, Identifier.withDefaultNamespace("command_level"), Permission.HasCommandLevel.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\permissions\PermissionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */