/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ParticleArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleCommand
/*    */ {
/* 31 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.particle.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 34 */     paramCommandDispatcher.register(
/* 35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("particle")
/* 36 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 37 */         .then((
/* 38 */           (RequiredArgumentBuilder)Commands.argument("name", (ArgumentType)ParticleArgument.particle(paramCommandBuildContext))
/* 39 */           .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), ((CommandSourceStack)paramCommandContext.getSource()).getPosition(), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getPlayers())))
/* 40 */           .then((
/* 41 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 42 */             .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getPlayers())))
/* 43 */             .then(
/* 44 */               Commands.argument("delta", (ArgumentType)Vec3Argument.vec3(false))
/* 45 */               .then(
/* 46 */                 Commands.argument("speed", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/* 47 */                 .then((
/* 48 */                   (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0))
/* 49 */                   .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3Argument.getVec3(paramCommandContext, "delta"), FloatArgumentType.getFloat(paramCommandContext, "speed"), IntegerArgumentType.getInteger(paramCommandContext, "count"), false, ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getPlayers())))
/* 50 */                   .then((
/* 51 */                     (LiteralArgumentBuilder)Commands.literal("force")
/* 52 */                     .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3Argument.getVec3(paramCommandContext, "delta"), FloatArgumentType.getFloat(paramCommandContext, "speed"), IntegerArgumentType.getInteger(paramCommandContext, "count"), true, ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getPlayers())))
/* 53 */                     .then(
/* 54 */                       Commands.argument("viewers", (ArgumentType)EntityArgument.players())
/* 55 */                       .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3Argument.getVec3(paramCommandContext, "delta"), FloatArgumentType.getFloat(paramCommandContext, "speed"), IntegerArgumentType.getInteger(paramCommandContext, "count"), true, EntityArgument.getPlayers(paramCommandContext, "viewers"))))))
/*    */ 
/*    */                   
/* 58 */                   .then((
/* 59 */                     (LiteralArgumentBuilder)Commands.literal("normal")
/* 60 */                     .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3Argument.getVec3(paramCommandContext, "delta"), FloatArgumentType.getFloat(paramCommandContext, "speed"), IntegerArgumentType.getInteger(paramCommandContext, "count"), false, ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getPlayers())))
/* 61 */                     .then(
/* 62 */                       Commands.argument("viewers", (ArgumentType)EntityArgument.players())
/* 63 */                       .executes(paramCommandContext -> sendParticles((CommandSourceStack)paramCommandContext.getSource(), ParticleArgument.getParticle(paramCommandContext, "name"), Vec3Argument.getVec3(paramCommandContext, "pos"), Vec3Argument.getVec3(paramCommandContext, "delta"), FloatArgumentType.getFloat(paramCommandContext, "speed"), IntegerArgumentType.getInteger(paramCommandContext, "count"), false, EntityArgument.getPlayers(paramCommandContext, "viewers")))))))))));
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
/*    */   private static int sendParticles(CommandSourceStack paramCommandSourceStack, ParticleOptions paramParticleOptions, Vec3 paramVec31, Vec3 paramVec32, float paramFloat, int paramInt, boolean paramBoolean, Collection<ServerPlayer> paramCollection) throws CommandSyntaxException {
/* 75 */     byte b = 0;
/*    */     
/* 77 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 78 */       if (paramCommandSourceStack.getLevel().sendParticles(serverPlayer, paramParticleOptions, paramBoolean, false, paramVec31.x, paramVec31.y, paramVec31.z, paramInt, paramVec32.x, paramVec32.y, paramVec32.z, paramFloat)) {
/* 79 */         b++;
/*    */       }
/*    */     } 
/*    */     
/* 83 */     if (b == 0) {
/* 84 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 87 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.particle.success", new Object[] { BuiltInRegistries.PARTICLE_TYPE.getKey(paramParticleOptions.getType()).toString() }), true);
/*    */     
/* 89 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ParticleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */