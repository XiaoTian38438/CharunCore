/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class DamageCommand {
/* 26 */   private static final SimpleCommandExceptionType ERROR_INVULNERABLE = new SimpleCommandExceptionType((Message)Component.translatable("commands.damage.invulnerable"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 29 */     paramCommandDispatcher.register(
/* 30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("damage")
/* 31 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 32 */         .then(
/* 33 */           Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 34 */           .then((
/* 35 */             (RequiredArgumentBuilder)Commands.argument("amount", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/* 36 */             .executes(paramCommandContext -> damage((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), FloatArgumentType.getFloat(paramCommandContext, "amount"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel().damageSources().generic())))
/* 37 */             .then((
/* 38 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("damageType", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.DAMAGE_TYPE))
/* 39 */               .executes(paramCommandContext -> damage((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), FloatArgumentType.getFloat(paramCommandContext, "amount"), new DamageSource((Holder)ResourceArgument.getResource(paramCommandContext, "damageType", Registries.DAMAGE_TYPE)))))
/* 40 */               .then(
/* 41 */                 Commands.literal("at")
/* 42 */                 .then(
/* 43 */                   Commands.argument("location", (ArgumentType)Vec3Argument.vec3())
/* 44 */                   .executes(paramCommandContext -> damage((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), FloatArgumentType.getFloat(paramCommandContext, "amount"), new DamageSource((Holder)ResourceArgument.getResource(paramCommandContext, "damageType", Registries.DAMAGE_TYPE), Vec3Argument.getVec3(paramCommandContext, "location")))))))
/*    */ 
/*    */               
/* 47 */               .then(
/* 48 */                 Commands.literal("by")
/* 49 */                 .then((
/* 50 */                   (RequiredArgumentBuilder)Commands.argument("entity", (ArgumentType)EntityArgument.entity())
/* 51 */                   .executes(paramCommandContext -> damage((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), FloatArgumentType.getFloat(paramCommandContext, "amount"), new DamageSource((Holder)ResourceArgument.getResource(paramCommandContext, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(paramCommandContext, "entity")))))
/* 52 */                   .then(
/* 53 */                     Commands.literal("from")
/* 54 */                     .then(
/* 55 */                       Commands.argument("cause", (ArgumentType)EntityArgument.entity())
/* 56 */                       .executes(paramCommandContext -> damage((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), FloatArgumentType.getFloat(paramCommandContext, "amount"), new DamageSource((Holder)ResourceArgument.getResource(paramCommandContext, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(paramCommandContext, "entity"), EntityArgument.getEntity(paramCommandContext, "cause"))))))))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int damage(CommandSourceStack paramCommandSourceStack, Entity paramEntity, float paramFloat, DamageSource paramDamageSource) throws CommandSyntaxException {
/* 68 */     if (paramEntity.hurtServer(paramCommandSourceStack.getLevel(), paramDamageSource, paramFloat)) {
/* 69 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.damage.success", new Object[] { Float.valueOf(paramFloat), paramEntity.getDisplayName() }), true);
/* 70 */       return 1;
/*    */     } 
/*    */     
/* 73 */     throw ERROR_INVULNERABLE.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DamageCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */