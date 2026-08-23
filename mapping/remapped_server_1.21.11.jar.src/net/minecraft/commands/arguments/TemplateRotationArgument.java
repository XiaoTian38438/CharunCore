/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public class TemplateRotationArgument extends StringRepresentableArgument<Rotation> {
/*    */   private TemplateRotationArgument() {
/*  9 */     super(Rotation.CODEC, Rotation::values);
/*    */   }
/*    */   
/*    */   public static TemplateRotationArgument templateRotation() {
/* 13 */     return new TemplateRotationArgument();
/*    */   }
/*    */   
/*    */   public static Rotation getRotation(CommandContext<CommandSourceStack> paramCommandContext, String paramString) {
/* 17 */     return (Rotation)paramCommandContext.getArgument(paramString, Rotation.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\TemplateRotationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */