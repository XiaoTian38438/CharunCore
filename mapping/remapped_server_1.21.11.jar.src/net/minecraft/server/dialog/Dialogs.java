/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.DialogTags;
/*    */ 
/*    */ public class Dialogs {
/* 16 */   public static final ResourceKey<Dialog> SERVER_LINKS = create("server_links");
/* 17 */   public static final ResourceKey<Dialog> CUSTOM_OPTIONS = create("custom_options");
/* 18 */   public static final ResourceKey<Dialog> QUICK_ACTIONS = create("quick_actions");
/*    */   
/*    */   public static final int BIG_BUTTON_WIDTH = 310;
/* 21 */   private static final ActionButton DEFAULT_BACK_BUTTON = new ActionButton(new CommonButtonData(CommonComponents.GUI_BACK, 200), 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 26 */       Optional.empty());
/*    */ 
/*    */   
/*    */   private static ResourceKey<Dialog> create(String paramString) {
/* 30 */     return ResourceKey.create(Registries.DIALOG, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<Dialog> paramBootstrapContext) {
/* 34 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.DIALOG);
/*    */     
/* 36 */     paramBootstrapContext.register(SERVER_LINKS, new ServerLinksDialog(new CommonDialogData(
/*    */             
/* 38 */             (Component)Component.translatable("menu.server_links.title"), 
/* 39 */             (Optional)Optional.of(Component.translatable("menu.server_links")), true, true, DialogAction.CLOSE, 
/*    */ 
/*    */ 
/*    */             
/* 43 */             List.of(), 
/* 44 */             List.of()), 
/*    */           
/* 46 */           Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     paramBootstrapContext.register(CUSTOM_OPTIONS, new DialogListDialog(new CommonDialogData(
/*    */             
/* 53 */             (Component)Component.translatable("menu.custom_options.title"), 
/* 54 */             (Optional)Optional.of(Component.translatable("menu.custom_options")), true, true, DialogAction.CLOSE, 
/*    */ 
/*    */ 
/*    */             
/* 58 */             List.of(), 
/* 59 */             List.of()), (HolderSet<Dialog>)holderGetter
/*    */           
/* 61 */           .getOrThrow(DialogTags.PAUSE_SCREEN_ADDITIONS), 
/* 62 */           Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     paramBootstrapContext.register(QUICK_ACTIONS, new DialogListDialog(new CommonDialogData(
/*    */             
/* 69 */             (Component)Component.translatable("menu.quick_actions.title"), 
/* 70 */             (Optional)Optional.of(Component.translatable("menu.quick_actions")), true, true, DialogAction.CLOSE, 
/*    */ 
/*    */ 
/*    */             
/* 74 */             List.of(), 
/* 75 */             List.of()), (HolderSet<Dialog>)holderGetter
/*    */           
/* 77 */           .getOrThrow(DialogTags.QUICK_ACTIONS), 
/* 78 */           Optional.of(DEFAULT_BACK_BUTTON), 1, 310));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\Dialogs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */