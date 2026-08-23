/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemInput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GiveCommand
/*    */ {
/*    */   public static final int MAX_ALLOWED_ITEMSTACKS = 100;
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 32 */     paramCommandDispatcher.register(
/* 33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("give")
/* 34 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 35 */         .then(
/* 36 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(paramCommandBuildContext))
/* 39 */             .executes(paramCommandContext -> giveItem((CommandSourceStack)paramCommandContext.getSource(), ItemArgument.getItem(paramCommandContext, "item"), EntityArgument.getPlayers(paramCommandContext, "targets"), 1)))
/* 40 */             .then(
/* 41 */               Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1))
/* 42 */               .executes(paramCommandContext -> giveItem((CommandSourceStack)paramCommandContext.getSource(), ItemArgument.getItem(paramCommandContext, "item"), EntityArgument.getPlayers(paramCommandContext, "targets"), IntegerArgumentType.getInteger(paramCommandContext, "count")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int giveItem(CommandSourceStack paramCommandSourceStack, ItemInput paramItemInput, Collection<ServerPlayer> paramCollection, int paramInt) throws CommandSyntaxException {
/* 50 */     ItemStack itemStack = paramItemInput.createItemStack(1, false);
/* 51 */     int i = itemStack.getMaxStackSize();
/* 52 */     int j = i * 100;
/* 53 */     if (paramInt > j) {
/* 54 */       paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.give.failed.toomanyitems", new Object[] { Integer.valueOf(j), itemStack.getDisplayName() }));
/* 55 */       return 0;
/*    */     } 
/* 57 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 58 */       int k = paramInt;
/* 59 */       while (k > 0) {
/* 60 */         int m = Math.min(i, k);
/* 61 */         k -= m;
/*    */         
/* 63 */         ItemStack itemStack1 = paramItemInput.createItemStack(m, false);
/* 64 */         boolean bool = serverPlayer.getInventory().add(itemStack1);
/*    */         
/* 66 */         if (!bool || !itemStack1.isEmpty()) {
/* 67 */           ItemEntity itemEntity1 = serverPlayer.drop(itemStack1, false);
/* 68 */           if (itemEntity1 != null) {
/* 69 */             itemEntity1.setNoPickUpDelay();
/* 70 */             itemEntity1.setTarget(serverPlayer.getUUID());
/*    */           } 
/*    */           continue;
/*    */         } 
/* 74 */         ItemEntity itemEntity = serverPlayer.drop(itemStack, false);
/* 75 */         if (itemEntity != null) {
/* 76 */           itemEntity.makeFakeItem();
/*    */         }
/* 78 */         serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 79 */         serverPlayer.containerMenu.broadcastChanges();
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 84 */     if (paramCollection.size() == 1) {
/* 85 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.give.success.single", new Object[] { Integer.valueOf(paramInt), paramItemStack.getDisplayName(), ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 87 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.give.success.single", new Object[] { Integer.valueOf(paramInt), paramItemStack.getDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 90 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\GiveCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */