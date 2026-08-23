/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class WardenSpawnTrackerCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 20 */     paramCommandDispatcher.register(
/* 21 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("warden_spawn_tracker")
/* 22 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 23 */         .then(
/* 24 */           Commands.literal("clear")
/* 25 */           .executes(paramCommandContext -> resetTracker((CommandSourceStack)paramCommandContext.getSource(), (Collection<? extends Player>)ImmutableList.of(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException())))))
/*    */         
/* 27 */         .then(
/* 28 */           Commands.literal("set")
/* 29 */           .then(
/* 30 */             Commands.argument("warning_level", (ArgumentType)IntegerArgumentType.integer(0, 4))
/* 31 */             .executes(paramCommandContext -> setWarningLevel((CommandSourceStack)paramCommandContext.getSource(), (Collection<? extends Player>)ImmutableList.of(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()), IntegerArgumentType.getInteger(paramCommandContext, "warning_level"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setWarningLevel(CommandSourceStack paramCommandSourceStack, Collection<? extends Player> paramCollection, int paramInt) {
/* 38 */     for (Player player : paramCollection) {
/* 39 */       player.getWardenSpawnTracker().ifPresent(paramWardenSpawnTracker -> paramWardenSpawnTracker.setWarningLevel(paramInt));
/*    */     }
/*    */     
/* 42 */     if (paramCollection.size() == 1) {
/* 43 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.single", new Object[] { ((Player)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 45 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 48 */     return paramCollection.size();
/*    */   }
/*    */   
/*    */   private static int resetTracker(CommandSourceStack paramCommandSourceStack, Collection<? extends Player> paramCollection) {
/* 52 */     for (Player player : paramCollection) {
/* 53 */       player.getWardenSpawnTracker().ifPresent(WardenSpawnTracker::reset);
/*    */     }
/*    */     
/* 56 */     if (paramCollection.size() == 1) {
/* 57 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.single", new Object[] { ((Player)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 59 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 62 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\WardenSpawnTrackerCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */