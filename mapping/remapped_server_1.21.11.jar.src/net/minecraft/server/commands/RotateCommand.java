/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ 
/*    */ public class RotateCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 25 */     paramCommandDispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("rotate")
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .then((
/* 29 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 30 */           .then(
/* 31 */             Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/* 32 */             .executes(paramCommandContext -> rotate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), RotationArgument.getRotation(paramCommandContext, "rotation")))))
/*    */           
/* 34 */           .then((
/* 35 */             (LiteralArgumentBuilder)Commands.literal("facing")
/* 36 */             .then(
/* 37 */               Commands.literal("entity")
/* 38 */               .then((
/* 39 */                 (RequiredArgumentBuilder)Commands.argument("facingEntity", (ArgumentType)EntityArgument.entity())
/* 40 */                 .executes(paramCommandContext -> rotate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), new LookAt.LookAtEntity(EntityArgument.getEntity(paramCommandContext, "facingEntity"), EntityAnchorArgument.Anchor.FEET))))
/* 41 */                 .then(
/* 42 */                   Commands.argument("facingAnchor", (ArgumentType)EntityAnchorArgument.anchor())
/* 43 */                   .executes(paramCommandContext -> rotate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), new LookAt.LookAtEntity(EntityArgument.getEntity(paramCommandContext, "facingEntity"), EntityAnchorArgument.getAnchor(paramCommandContext, "facingAnchor"))))))))
/*    */ 
/*    */ 
/*    */             
/* 47 */             .then(
/* 48 */               Commands.argument("facingLocation", (ArgumentType)Vec3Argument.vec3())
/* 49 */               .executes(paramCommandContext -> rotate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), new LookAt.LookAtPosition(Vec3Argument.getVec3(paramCommandContext, "facingLocation"))))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int rotate(CommandSourceStack paramCommandSourceStack, Entity paramEntity, Coordinates paramCoordinates) {
/* 57 */     Vec2 vec2 = paramCoordinates.getRotation(paramCommandSourceStack);
/* 58 */     float f1 = paramCoordinates.isYRelative() ? (vec2.y - paramEntity.getYRot()) : vec2.y;
/* 59 */     float f2 = paramCoordinates.isXRelative() ? (vec2.x - paramEntity.getXRot()) : vec2.x;
/* 60 */     paramEntity.forceSetRotation(f1, paramCoordinates.isYRelative(), f2, paramCoordinates.isXRelative());
/* 61 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.rotate.success", new Object[] { paramEntity.getDisplayName() }), true);
/* 62 */     return 1;
/*    */   }
/*    */   
/*    */   private static int rotate(CommandSourceStack paramCommandSourceStack, Entity paramEntity, LookAt paramLookAt) {
/* 66 */     paramLookAt.perform(paramCommandSourceStack, paramEntity);
/* 67 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.rotate.success", new Object[] { paramEntity.getDisplayName() }), true);
/* 68 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\RotateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */