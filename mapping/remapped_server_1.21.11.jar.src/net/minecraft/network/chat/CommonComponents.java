/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class CommonComponents {
/*  7 */   public static final Component EMPTY = Component.empty();
/*    */   
/*  9 */   public static final Component OPTION_ON = Component.translatable("options.on");
/* 10 */   public static final Component OPTION_OFF = Component.translatable("options.off");
/*    */   
/* 12 */   public static final Component GUI_DONE = Component.translatable("gui.done");
/* 13 */   public static final Component GUI_CANCEL = Component.translatable("gui.cancel");
/* 14 */   public static final Component GUI_YES = Component.translatable("gui.yes");
/* 15 */   public static final Component GUI_NO = Component.translatable("gui.no");
/* 16 */   public static final Component GUI_OK = Component.translatable("gui.ok");
/* 17 */   public static final Component GUI_PROCEED = Component.translatable("gui.proceed");
/* 18 */   public static final Component GUI_CONTINUE = Component.translatable("gui.continue");
/* 19 */   public static final Component GUI_BACK = Component.translatable("gui.back");
/* 20 */   public static final Component GUI_TO_TITLE = Component.translatable("gui.toTitle");
/* 21 */   public static final Component GUI_ACKNOWLEDGE = Component.translatable("gui.acknowledge");
/* 22 */   public static final Component GUI_OPEN_IN_BROWSER = Component.translatable("chat.link.open");
/* 23 */   public static final Component GUI_COPY_TO_CLIPBOARD = Component.translatable("chat.copy");
/* 24 */   public static final Component GUI_COPY_LINK_TO_CLIPBOARD = Component.translatable("gui.copy_link_to_clipboard");
/* 25 */   public static final Component GUI_DISCONNECT = Component.translatable("menu.disconnect");
/* 26 */   public static final Component GUI_RETURN_TO_MENU = Component.translatable("menu.returnToMenu");
/*    */   
/* 28 */   public static final Component TRANSFER_CONNECT_FAILED = Component.translatable("connect.failed.transfer");
/* 29 */   public static final Component CONNECT_FAILED = Component.translatable("connect.failed");
/*    */   
/* 31 */   public static final Component NEW_LINE = Component.literal("\n");
/* 32 */   public static final Component NARRATION_SEPARATOR = Component.literal(". ");
/*    */   
/* 34 */   public static final Component ELLIPSIS = Component.literal("...");
/* 35 */   public static final Component SPACE = space();
/*    */   
/*    */   public static MutableComponent space() {
/* 38 */     return Component.literal(" ");
/*    */   }
/*    */   
/*    */   public static MutableComponent days(long paramLong) {
/* 42 */     return Component.translatable("gui.days", new Object[] { Long.valueOf(paramLong) });
/*    */   }
/*    */   
/*    */   public static MutableComponent hours(long paramLong) {
/* 46 */     return Component.translatable("gui.hours", new Object[] { Long.valueOf(paramLong) });
/*    */   }
/*    */   
/*    */   public static MutableComponent minutes(long paramLong) {
/* 50 */     return Component.translatable("gui.minutes", new Object[] { Long.valueOf(paramLong) });
/*    */   }
/*    */   
/*    */   public static Component optionStatus(boolean paramBoolean) {
/* 54 */     return paramBoolean ? OPTION_ON : OPTION_OFF;
/*    */   }
/*    */   
/*    */   public static Component disconnectButtonLabel(boolean paramBoolean) {
/* 58 */     return paramBoolean ? GUI_RETURN_TO_MENU : GUI_DISCONNECT;
/*    */   }
/*    */   
/*    */   public static MutableComponent optionStatus(Component paramComponent, boolean paramBoolean) {
/* 62 */     return Component.translatable(paramBoolean ? "options.on.composed" : "options.off.composed", new Object[] { paramComponent });
/*    */   }
/*    */   
/*    */   public static MutableComponent optionNameValue(Component paramComponent1, Component paramComponent2) {
/* 66 */     return Component.translatable("options.generic_value", new Object[] { paramComponent1, paramComponent2 });
/*    */   }
/*    */   
/*    */   public static MutableComponent joinForNarration(Component... paramVarArgs) {
/* 70 */     MutableComponent mutableComponent = Component.empty();
/* 71 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 72 */       mutableComponent.append(paramVarArgs[b]);
/* 73 */       if (b != paramVarArgs.length - 1) {
/* 74 */         mutableComponent.append(NARRATION_SEPARATOR);
/*    */       }
/*    */     } 
/* 77 */     return mutableComponent;
/*    */   }
/*    */   
/*    */   public static Component joinLines(Component... paramVarArgs) {
/* 81 */     return joinLines(Arrays.asList(paramVarArgs));
/*    */   }
/*    */   
/*    */   public static Component joinLines(Collection<? extends Component> paramCollection) {
/* 85 */     return ComponentUtils.formatList(paramCollection, NEW_LINE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\CommonComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */