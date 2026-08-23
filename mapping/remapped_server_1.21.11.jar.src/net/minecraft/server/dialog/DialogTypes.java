/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class DialogTypes {
/*    */   public static MapCodec<? extends Dialog> bootstrap(Registry<MapCodec<? extends Dialog>> paramRegistry) {
/*  8 */     Registry.register(paramRegistry, "notice", NoticeDialog.MAP_CODEC);
/*  9 */     Registry.register(paramRegistry, "server_links", ServerLinksDialog.MAP_CODEC);
/* 10 */     Registry.register(paramRegistry, "dialog_list", DialogListDialog.MAP_CODEC);
/* 11 */     Registry.register(paramRegistry, "multi_action", MultiActionDialog.MAP_CODEC);
/* 12 */     return (MapCodec<? extends Dialog>)Registry.register(paramRegistry, "confirmation", ConfirmationDialog.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\DialogTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */