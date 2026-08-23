/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ActionTypes {
/*    */   public static MapCodec<? extends Action> bootstrap(Registry<MapCodec<? extends Action>> paramRegistry) {
/*  9 */     StaticAction.WRAPPED_CODECS.forEach((paramAction, paramMapCodec) -> Registry.register(paramRegistry, Identifier.withDefaultNamespace(paramAction.getSerializedName()), paramMapCodec));
/*    */ 
/*    */     
/* 12 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("dynamic/run_command"), CommandTemplate.MAP_CODEC);
/* 13 */     return (MapCodec<? extends Action>)Registry.register(paramRegistry, Identifier.withDefaultNamespace("dynamic/custom"), CustomAll.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\action\ActionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */