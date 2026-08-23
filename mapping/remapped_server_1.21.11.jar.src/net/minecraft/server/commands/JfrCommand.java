/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.profiling.jfr.Environment;
/*    */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*    */ 
/*    */ public class JfrCommand {
/* 23 */   private static final SimpleCommandExceptionType START_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.jfr.start.failed")); private static final DynamicCommandExceptionType DUMP_FAILED; static {
/* 24 */     DUMP_FAILED = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.jfr.dump.failed", new Object[] { paramObject }));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 30 */     paramCommandDispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("jfr")
/* 32 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 33 */         .then(Commands.literal("start").executes(paramCommandContext -> startJfr((CommandSourceStack)paramCommandContext.getSource()))))
/* 34 */         .then(Commands.literal("stop").executes(paramCommandContext -> stopJfr((CommandSourceStack)paramCommandContext.getSource()))));
/*    */   }
/*    */ 
/*    */   
/*    */   private static int startJfr(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 39 */     Environment environment = Environment.from(paramCommandSourceStack.getServer());
/* 40 */     if (!JvmProfiler.INSTANCE.start(environment)) {
/* 41 */       throw START_FAILED.create();
/*    */     }
/* 43 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.jfr.started"), false);
/* 44 */     return 1;
/*    */   }
/*    */   
/*    */   private static int stopJfr(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/*    */     try {
/* 49 */       Path path1 = Paths.get(".", new String[0]).relativize(JvmProfiler.INSTANCE.stop().normalize());
/* 50 */       Path path2 = (!paramCommandSourceStack.getServer().isPublished() || SharedConstants.IS_RUNNING_IN_IDE) ? path1.toAbsolutePath() : path1;
/*    */ 
/*    */       
/* 53 */       MutableComponent mutableComponent = Component.literal(path1.toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(paramStyle -> paramStyle.withClickEvent((ClickEvent)new ClickEvent.CopyToClipboard(paramPath.toString())).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.copy.click"))));
/*    */ 
/*    */       
/* 56 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.jfr.stopped", new Object[] { paramComponent }), false);
/* 57 */       return 1;
/* 58 */     } catch (Throwable throwable) {
/* 59 */       throw DUMP_FAILED.create(throwable.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\JfrCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */