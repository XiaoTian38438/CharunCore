/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClearInventoryCommands {
/*    */   private static final DynamicCommandExceptionType ERROR_SINGLE;
/*    */   
/*    */   static {
/* 27 */     ERROR_SINGLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("clear.failed.single", new Object[] { paramObject }));
/* 28 */     ERROR_MULTIPLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("clear.failed.multiple", new Object[] { paramObject }));
/*    */   } private static final DynamicCommandExceptionType ERROR_MULTIPLE;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 31 */     paramCommandDispatcher.register(
/* 32 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clear")
/* 33 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 34 */         .executes(paramCommandContext -> clearUnlimited((CommandSourceStack)paramCommandContext.getSource(), Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()), ())))
/* 35 */         .then((
/* 36 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 37 */           .executes(paramCommandContext -> clearUnlimited((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ())))
/* 38 */           .then((
/* 39 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemPredicateArgument.itemPredicate(paramCommandBuildContext))
/* 40 */             .executes(paramCommandContext -> clearUnlimited((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(paramCommandContext, "item"))))
/* 41 */             .then(
/* 42 */               Commands.argument("maxCount", (ArgumentType)IntegerArgumentType.integer(0))
/* 43 */               .executes(paramCommandContext -> clearInventory((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), (Predicate<ItemStack>)ItemPredicateArgument.getItemPredicate(paramCommandContext, "item"), IntegerArgumentType.getInteger(paramCommandContext, "maxCount")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int clearUnlimited(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Predicate<ItemStack> paramPredicate) throws CommandSyntaxException {
/* 51 */     return clearInventory(paramCommandSourceStack, paramCollection, paramPredicate, -1);
/*    */   }
/*    */   
/*    */   private static int clearInventory(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Predicate<ItemStack> paramPredicate, int paramInt) throws CommandSyntaxException {
/* 55 */     int i = 0;
/*    */     
/* 57 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 58 */       i += serverPlayer.getInventory().clearOrCountMatchingItems(paramPredicate, paramInt, (Container)serverPlayer.inventoryMenu.getCraftSlots());
/*    */       
/* 60 */       serverPlayer.containerMenu.broadcastChanges();
/*    */ 
/*    */       
/* 63 */       serverPlayer.inventoryMenu.slotsChanged((Container)serverPlayer.getInventory());
/*    */     } 
/*    */     
/* 66 */     if (i == 0) {
/* 67 */       if (paramCollection.size() == 1) {
/* 68 */         throw ERROR_SINGLE.create(((ServerPlayer)paramCollection.iterator().next()).getName());
/*    */       }
/* 70 */       throw ERROR_MULTIPLE.create(Integer.valueOf(paramCollection.size()));
/*    */     } 
/*    */ 
/*    */     
/* 74 */     int j = i;
/* 75 */     if (paramInt == 0) {
/* 76 */       if (paramCollection.size() == 1) {
/* 77 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.clear.test.single", new Object[] { Integer.valueOf(paramInt), ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */       } else {
/* 79 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.clear.test.multiple", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(paramCollection.size()) }), true);
/*    */       }
/*    */     
/* 82 */     } else if (paramCollection.size() == 1) {
/* 83 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.clear.success.single", new Object[] { Integer.valueOf(paramInt), ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 85 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.clear.success.multiple", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */ 
/*    */     
/* 89 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ClearInventoryCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */