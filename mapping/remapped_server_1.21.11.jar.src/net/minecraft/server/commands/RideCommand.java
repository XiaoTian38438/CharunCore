/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class RideCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_RIDING;
/*    */   
/*    */   static {
/* 20 */     ERROR_NOT_RIDING = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.ride.not_riding", new Object[] { paramObject }));
/* 21 */     ERROR_ALREADY_RIDING = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.ride.already_riding", new Object[] { paramObject1, paramObject2 }));
/* 22 */     ERROR_MOUNT_FAILED = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.ride.mount.failure.generic", new Object[] { paramObject1, paramObject2 }));
/* 23 */   } private static final Dynamic2CommandExceptionType ERROR_ALREADY_RIDING; private static final Dynamic2CommandExceptionType ERROR_MOUNT_FAILED; private static final SimpleCommandExceptionType ERROR_MOUNTING_PLAYER = new SimpleCommandExceptionType((Message)Component.translatable("commands.ride.mount.failure.cant_ride_players"));
/* 24 */   private static final SimpleCommandExceptionType ERROR_MOUNTING_LOOP = new SimpleCommandExceptionType((Message)Component.translatable("commands.ride.mount.failure.loop"));
/* 25 */   private static final SimpleCommandExceptionType ERROR_WRONG_DIMENSION = new SimpleCommandExceptionType((Message)Component.translatable("commands.ride.mount.failure.wrong_dimension"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 28 */     paramCommandDispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ride")
/* 30 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 33 */           .then(
/* 34 */             Commands.literal("mount")
/* 35 */             .then(
/* 36 */               Commands.argument("vehicle", (ArgumentType)EntityArgument.entity())
/* 37 */               .executes(paramCommandContext -> mount((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), EntityArgument.getEntity(paramCommandContext, "vehicle"))))))
/*    */ 
/*    */           
/* 40 */           .then(
/* 41 */             Commands.literal("dismount")
/* 42 */             .executes(paramCommandContext -> dismount((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int mount(CommandSourceStack paramCommandSourceStack, Entity paramEntity1, Entity paramEntity2) throws CommandSyntaxException {
/* 49 */     Entity entity = paramEntity1.getVehicle();
/* 50 */     if (entity != null) {
/* 51 */       throw ERROR_ALREADY_RIDING.create(paramEntity1.getDisplayName(), entity.getDisplayName());
/*    */     }
/* 53 */     if (paramEntity2.getType() == EntityType.PLAYER) {
/* 54 */       throw ERROR_MOUNTING_PLAYER.create();
/*    */     }
/* 56 */     if (paramEntity1.getSelfAndPassengers().anyMatch(paramEntity2 -> (paramEntity2 == paramEntity1))) {
/* 57 */       throw ERROR_MOUNTING_LOOP.create();
/*    */     }
/* 59 */     if (paramEntity1.level() != paramEntity2.level()) {
/* 60 */       throw ERROR_WRONG_DIMENSION.create();
/*    */     }
/* 62 */     if (!paramEntity1.startRiding(paramEntity2, true, true)) {
/* 63 */       throw ERROR_MOUNT_FAILED.create(paramEntity1.getDisplayName(), paramEntity2.getDisplayName());
/*    */     }
/* 65 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.ride.mount.success", new Object[] { paramEntity1.getDisplayName(), paramEntity2.getDisplayName() }), true);
/* 66 */     return 1;
/*    */   }
/*    */   
/*    */   private static int dismount(CommandSourceStack paramCommandSourceStack, Entity paramEntity) throws CommandSyntaxException {
/* 70 */     Entity entity = paramEntity.getVehicle();
/* 71 */     if (entity == null) {
/* 72 */       throw ERROR_NOT_RIDING.create(paramEntity.getDisplayName());
/*    */     }
/*    */     
/* 75 */     paramEntity.stopRiding();
/* 76 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.ride.dismount.success", new Object[] { paramEntity1.getDisplayName(), paramEntity2.getDisplayName() }), true);
/* 77 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\RideCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */