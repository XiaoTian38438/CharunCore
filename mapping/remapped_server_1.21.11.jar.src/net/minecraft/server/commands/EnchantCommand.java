/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ public class EnchantCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
/*    */   private static final DynamicCommandExceptionType ERROR_NO_ITEM;
/*    */   
/*    */   static {
/* 32 */     ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.enchant.failed.entity", new Object[] { paramObject }));
/* 33 */     ERROR_NO_ITEM = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.enchant.failed.itemless", new Object[] { paramObject }));
/* 34 */     ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.enchant.failed.incompatible", new Object[] { paramObject }));
/* 35 */     ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.enchant.failed.level", new Object[] { paramObject1, paramObject2 }));
/* 36 */   } private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE; private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH; private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType((Message)Component.translatable("commands.enchant.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 39 */     paramCommandDispatcher.register(
/* 40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("enchant")
/* 41 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 42 */         .then(
/* 43 */           Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 44 */           .then((
/* 45 */             (RequiredArgumentBuilder)Commands.argument("enchantment", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.ENCHANTMENT))
/* 46 */             .executes(paramCommandContext -> enchant((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<Enchantment>)ResourceArgument.getEnchantment(paramCommandContext, "enchantment"), 1)))
/* 47 */             .then(
/* 48 */               Commands.argument("level", (ArgumentType)IntegerArgumentType.integer(0))
/* 49 */               .executes(paramCommandContext -> enchant((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<Enchantment>)ResourceArgument.getEnchantment(paramCommandContext, "enchantment"), IntegerArgumentType.getInteger(paramCommandContext, "level")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int enchant(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, Holder<Enchantment> paramHolder, int paramInt) throws CommandSyntaxException {
/* 57 */     Enchantment enchantment = (Enchantment)paramHolder.value();
/* 58 */     if (paramInt > enchantment.getMaxLevel()) {
/* 59 */       throw ERROR_LEVEL_TOO_HIGH.create(Integer.valueOf(paramInt), Integer.valueOf(enchantment.getMaxLevel()));
/*    */     }
/*    */     
/* 62 */     byte b = 0;
/*    */     
/* 64 */     for (Entity entity : paramCollection) {
/* 65 */       if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 66 */         ItemStack itemStack = livingEntity.getMainHandItem();
/* 67 */         if (!itemStack.isEmpty()) {
/* 68 */           if (enchantment.canEnchant(itemStack) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantmentsForCrafting(itemStack).keySet(), paramHolder)) {
/* 69 */             itemStack.enchant(paramHolder, paramInt);
/* 70 */             b++; continue;
/* 71 */           }  if (paramCollection.size() == 1)
/* 72 */             throw ERROR_INCOMPATIBLE.create(itemStack.getHoverName().getString());  continue;
/*    */         } 
/* 74 */         if (paramCollection.size() == 1)
/* 75 */           throw ERROR_NO_ITEM.create(livingEntity.getName().getString());  continue; }
/*    */       
/* 77 */       if (paramCollection.size() == 1) {
/* 78 */         throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
/*    */       }
/*    */     } 
/*    */     
/* 82 */     if (b == 0)
/* 83 */       throw ERROR_NOTHING_HAPPENED.create(); 
/* 84 */     if (paramCollection.size() == 1) {
/* 85 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.enchant.success.single", new Object[] { Enchantment.getFullname(paramHolder, paramInt), ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 87 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.enchant.success.multiple", new Object[] { Enchantment.getFullname(paramHolder, paramInt), Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 90 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\EnchantCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */