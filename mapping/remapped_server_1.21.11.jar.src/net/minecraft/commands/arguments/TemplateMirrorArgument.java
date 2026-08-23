/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ 
/*    */ public class TemplateMirrorArgument extends StringRepresentableArgument<Mirror> {
/*    */   private TemplateMirrorArgument() {
/*  9 */     super(Mirror.CODEC, Mirror::values);
/*    */   }
/*    */   
/*    */   public static StringRepresentableArgument<Mirror> templateMirror() {
/* 13 */     return new TemplateMirrorArgument();
/*    */   }
/*    */   
/*    */   public static Mirror getMirror(CommandContext<CommandSourceStack> paramCommandContext, String paramString) {
/* 17 */     return (Mirror)paramCommandContext.getArgument(paramString, Mirror.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\TemplateMirrorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */