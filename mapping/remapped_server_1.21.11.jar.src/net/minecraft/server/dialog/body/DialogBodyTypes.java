/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DialogBodyTypes {
/*    */   public static MapCodec<? extends DialogBody> bootstrap(Registry<MapCodec<? extends DialogBody>> paramRegistry) {
/*  9 */     Registry.register(paramRegistry, Identifier.withDefaultNamespace("item"), ItemBody.MAP_CODEC);
/* 10 */     return (MapCodec<? extends DialogBody>)Registry.register(paramRegistry, Identifier.withDefaultNamespace("plain_message"), PlainMessage.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\body\DialogBodyTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */