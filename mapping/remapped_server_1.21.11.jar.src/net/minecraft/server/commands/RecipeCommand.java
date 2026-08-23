/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ public class RecipeCommand {
/* 24 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.recipe.give.failed"));
/* 25 */   private static final SimpleCommandExceptionType ERROR_TAKE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.recipe.take.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 28 */     paramCommandDispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("recipe")
/* 30 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 31 */         .then(
/* 32 */           Commands.literal("give")
/* 33 */           .then((
/* 34 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 35 */             .then(
/* 36 */               Commands.argument("recipe", (ArgumentType)ResourceKeyArgument.key(Registries.RECIPE))
/* 37 */               .executes(paramCommandContext -> giveRecipes((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), Collections.singleton(ResourceKeyArgument.getRecipe(paramCommandContext, "recipe"))))))
/*    */             
/* 39 */             .then(
/* 40 */               Commands.literal("*")
/* 41 */               .executes(paramCommandContext -> giveRecipes((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getServer().getRecipeManager().getRecipes()))))))
/*    */ 
/*    */ 
/*    */         
/* 45 */         .then(
/* 46 */           Commands.literal("take")
/* 47 */           .then((
/* 48 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 49 */             .then(
/* 50 */               Commands.argument("recipe", (ArgumentType)ResourceKeyArgument.key(Registries.RECIPE))
/* 51 */               .executes(paramCommandContext -> takeRecipes((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), Collections.singleton(ResourceKeyArgument.getRecipe(paramCommandContext, "recipe"))))))
/*    */             
/* 53 */             .then(
/* 54 */               Commands.literal("*")
/* 55 */               .executes(paramCommandContext -> takeRecipes((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getServer().getRecipeManager().getRecipes()))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int giveRecipes(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Collection<RecipeHolder<?>> paramCollection1) throws CommandSyntaxException {
/* 63 */     int i = 0;
/*    */     
/* 65 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 66 */       i += serverPlayer.awardRecipes(paramCollection1);
/*    */     }
/*    */     
/* 69 */     if (i == 0) {
/* 70 */       throw ERROR_GIVE_FAILED.create();
/*    */     }
/*    */     
/* 73 */     if (paramCollection.size() == 1) {
/* 74 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.recipe.give.success.single", new Object[] { Integer.valueOf(paramCollection1.size()), ((ServerPlayer)paramCollection2.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 76 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.recipe.give.success.multiple", new Object[] { Integer.valueOf(paramCollection1.size()), Integer.valueOf(paramCollection2.size()) }), true);
/*    */     } 
/*    */     
/* 79 */     return i;
/*    */   }
/*    */   
/*    */   private static int takeRecipes(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Collection<RecipeHolder<?>> paramCollection1) throws CommandSyntaxException {
/* 83 */     int i = 0;
/*    */     
/* 85 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 86 */       i += serverPlayer.resetRecipes(paramCollection1);
/*    */     }
/*    */     
/* 89 */     if (i == 0) {
/* 90 */       throw ERROR_TAKE_FAILED.create();
/*    */     }
/*    */     
/* 93 */     if (paramCollection.size() == 1) {
/* 94 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.recipe.take.success.single", new Object[] { Integer.valueOf(paramCollection1.size()), ((ServerPlayer)paramCollection2.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 96 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.recipe.take.success.multiple", new Object[] { Integer.valueOf(paramCollection1.size()), Integer.valueOf(paramCollection2.size()) }), true);
/*    */     } 
/*    */     
/* 99 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\RecipeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */