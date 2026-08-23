/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.WorldVersion;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ 
/*    */ public class VersionCommand {
/* 16 */   private static final Component HEADER = (Component)Component.translatable("commands.version.header");
/* 17 */   private static final Component STABLE = (Component)Component.translatable("commands.version.stable.yes");
/* 18 */   private static final Component UNSTABLE = (Component)Component.translatable("commands.version.stable.no");
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, boolean paramBoolean) {
/* 21 */     paramCommandDispatcher.register(
/* 22 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("version")
/*    */         
/* 24 */         .requires((Predicate)Commands.hasPermission(paramBoolean ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL)))
/* 25 */         .executes(paramCommandContext -> {
/*    */             CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*    */             commandSourceStack.sendSystemMessage(HEADER);
/*    */             Objects.requireNonNull(commandSourceStack);
/*    */             dumpVersion(commandSourceStack::sendSystemMessage);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */   
/*    */   public static void dumpVersion(Consumer<Component> paramConsumer) {
/* 35 */     WorldVersion worldVersion = SharedConstants.getCurrentVersion();
/* 36 */     paramConsumer.accept(Component.translatable("commands.version.id", new Object[] { worldVersion.id() }));
/* 37 */     paramConsumer.accept(Component.translatable("commands.version.name", new Object[] { worldVersion.name() }));
/* 38 */     paramConsumer.accept(Component.translatable("commands.version.data", new Object[] { Integer.valueOf(worldVersion.dataVersion().version()) }));
/* 39 */     paramConsumer.accept(Component.translatable("commands.version.series", new Object[] { worldVersion.dataVersion().series() }));
/* 40 */     paramConsumer.accept(Component.translatable("commands.version.protocol", new Object[] { Integer.valueOf(worldVersion.protocolVersion()), "0x" + Integer.toHexString(worldVersion.protocolVersion()) }));
/* 41 */     paramConsumer.accept(Component.translatable("commands.version.build_time", new Object[] { Component.translationArg(worldVersion.buildTime()) }));
/* 42 */     paramConsumer.accept(Component.translatable("commands.version.pack.resource", new Object[] { worldVersion.packVersion(PackType.CLIENT_RESOURCES).toString() }));
/* 43 */     paramConsumer.accept(Component.translatable("commands.version.pack.data", new Object[] { worldVersion.packVersion(PackType.SERVER_DATA).toString() }));
/* 44 */     paramConsumer.accept(worldVersion.stable() ? STABLE : UNSTABLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\VersionCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */