/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeleportCommand
/*     */ {
/*  45 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)Component.translatable("commands.teleport.invalidPosition"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  48 */     LiteralCommandNode literalCommandNode = paramCommandDispatcher.register(
/*  49 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("teleport")
/*  50 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  51 */         .then(
/*  52 */           Commands.argument("location", (ArgumentType)Vec3Argument.vec3())
/*  53 */           .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getEntityOrException()), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), null, null))))
/*     */         
/*  55 */         .then(
/*  56 */           Commands.argument("destination", (ArgumentType)EntityArgument.entity())
/*  57 */           .executes(paramCommandContext -> teleportToEntity((CommandSourceStack)paramCommandContext.getSource(), Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getEntityOrException()), EntityArgument.getEntity(paramCommandContext, "destination")))))
/*     */         
/*  59 */         .then((
/*  60 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  61 */           .then((
/*  62 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("location", (ArgumentType)Vec3Argument.vec3())
/*  63 */             .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), null, null)))
/*  64 */             .then(
/*  65 */               Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/*  66 */               .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), RotationArgument.getRotation(paramCommandContext, "rotation"), null))))
/*     */             
/*  68 */             .then((
/*  69 */               (LiteralArgumentBuilder)Commands.literal("facing")
/*  70 */               .then(
/*  71 */                 Commands.literal("entity")
/*  72 */                 .then((
/*  73 */                   (RequiredArgumentBuilder)Commands.argument("facingEntity", (ArgumentType)EntityArgument.entity())
/*  74 */                   .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), null, new LookAt.LookAtEntity(EntityArgument.getEntity(paramCommandContext, "facingEntity"), EntityAnchorArgument.Anchor.FEET))))
/*  75 */                   .then(
/*  76 */                     Commands.argument("facingAnchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  77 */                     .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), null, new LookAt.LookAtEntity(EntityArgument.getEntity(paramCommandContext, "facingEntity"), EntityAnchorArgument.getAnchor(paramCommandContext, "facingAnchor"))))))))
/*     */ 
/*     */ 
/*     */               
/*  81 */               .then(
/*  82 */                 Commands.argument("facingLocation", (ArgumentType)Vec3Argument.vec3())
/*  83 */                 .executes(paramCommandContext -> teleportToPos((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(paramCommandContext, "location"), null, new LookAt.LookAtPosition(Vec3Argument.getVec3(paramCommandContext, "facingLocation"))))))))
/*     */ 
/*     */ 
/*     */           
/*  87 */           .then(
/*  88 */             Commands.argument("destination", (ArgumentType)EntityArgument.entity())
/*  89 */             .executes(paramCommandContext -> teleportToEntity((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), EntityArgument.getEntity(paramCommandContext, "destination"))))));
/*     */ 
/*     */ 
/*     */     
/*  93 */     paramCommandDispatcher.register(
/*  94 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tp")
/*  95 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  96 */         .redirect((CommandNode)literalCommandNode));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int teleportToEntity(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, Entity paramEntity) throws CommandSyntaxException {
/* 101 */     for (Entity entity : paramCollection)
/*     */     {
/*     */       
/* 104 */       performTeleport(paramCommandSourceStack, entity, (ServerLevel)paramEntity.level(), paramEntity.getX(), paramEntity.getY(), paramEntity.getZ(), EnumSet.noneOf(Relative.class), paramEntity.getYRot(), paramEntity.getXRot(), null);
/*     */     }
/*     */     
/* 107 */     if (paramCollection.size() == 1) {
/* 108 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.single", new Object[] { ((Entity)paramCollection.iterator().next()).getDisplayName(), paramEntity.getDisplayName() }), true);
/*     */     } else {
/* 110 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramEntity.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 113 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int teleportToPos(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, ServerLevel paramServerLevel, Coordinates paramCoordinates1, Coordinates paramCoordinates2, LookAt paramLookAt) throws CommandSyntaxException {
/* 117 */     Vec3 vec3 = paramCoordinates1.getPosition(paramCommandSourceStack);
/* 118 */     Vec2 vec2 = (paramCoordinates2 == null) ? null : paramCoordinates2.getRotation(paramCommandSourceStack);
/*     */     
/* 120 */     for (Entity entity : paramCollection) {
/* 121 */       Set<Relative> set = getRelatives(paramCoordinates1, paramCoordinates2, (entity.level().dimension() == paramServerLevel.dimension()));
/* 122 */       if (vec2 == null) {
/* 123 */         performTeleport(paramCommandSourceStack, entity, paramServerLevel, vec3.x, vec3.y, vec3.z, set, entity.getYRot(), entity.getXRot(), paramLookAt); continue;
/*     */       } 
/* 125 */       performTeleport(paramCommandSourceStack, entity, paramServerLevel, vec3.x, vec3.y, vec3.z, set, vec2.y, vec2.x, paramLookAt);
/*     */     } 
/*     */ 
/*     */     
/* 129 */     if (paramCollection.size() == 1) {
/* 130 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.teleport.success.location.single", new Object[] { ((Entity)paramCollection.iterator().next()).getDisplayName(), formatDouble(paramVec3.x), formatDouble(paramVec3.y), formatDouble(paramVec3.z) }), true);
/*     */     } else {
/* 132 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.teleport.success.location.multiple", new Object[] { Integer.valueOf(paramCollection.size()), formatDouble(paramVec3.x), formatDouble(paramVec3.y), formatDouble(paramVec3.z) }), true);
/*     */     } 
/*     */     
/* 135 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static Set<Relative> getRelatives(Coordinates paramCoordinates1, Coordinates paramCoordinates2, boolean paramBoolean) {
/* 139 */     Set set1 = Relative.direction(paramCoordinates1.isXRelative(), paramCoordinates1.isYRelative(), paramCoordinates1.isZRelative());
/* 140 */     Set set2 = paramBoolean ? Relative.position(paramCoordinates1.isXRelative(), paramCoordinates1.isYRelative(), paramCoordinates1.isZRelative()) : Set.of();
/* 141 */     Set set3 = (paramCoordinates2 == null) ? Relative.ROTATION : Relative.rotation(paramCoordinates2.isYRelative(), paramCoordinates2.isXRelative());
/* 142 */     return Relative.union(new Set[] { set1, set2, set3 });
/*     */   }
/*     */   
/*     */   private static String formatDouble(double paramDouble) {
/* 146 */     return String.format(Locale.ROOT, "%f", new Object[] { Double.valueOf(paramDouble) });
/*     */   }
/*     */   
/*     */   private static void performTeleport(CommandSourceStack paramCommandSourceStack, Entity paramEntity, ServerLevel paramServerLevel, double paramDouble1, double paramDouble2, double paramDouble3, Set<Relative> paramSet, float paramFloat1, float paramFloat2, LookAt paramLookAt) throws CommandSyntaxException {
/*     */     // Byte code:
/*     */     //   0: dload_3
/*     */     //   1: dload #5
/*     */     //   3: dload #7
/*     */     //   5: invokestatic containing : (DDD)Lnet/minecraft/core/BlockPos;
/*     */     //   8: astore #13
/*     */     //   10: aload #13
/*     */     //   12: invokestatic isInSpawnableBounds : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   15: ifne -> 25
/*     */     //   18: getstatic net/minecraft/server/commands/TeleportCommand.INVALID_POSITION : Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
/*     */     //   21: invokevirtual create : ()Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   24: athrow
/*     */     //   25: aload #9
/*     */     //   27: getstatic net/minecraft/world/entity/Relative.X : Lnet/minecraft/world/entity/Relative;
/*     */     //   30: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   35: ifeq -> 47
/*     */     //   38: dload_3
/*     */     //   39: aload_1
/*     */     //   40: invokevirtual getX : ()D
/*     */     //   43: dsub
/*     */     //   44: goto -> 48
/*     */     //   47: dload_3
/*     */     //   48: dstore #14
/*     */     //   50: aload #9
/*     */     //   52: getstatic net/minecraft/world/entity/Relative.Y : Lnet/minecraft/world/entity/Relative;
/*     */     //   55: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   60: ifeq -> 73
/*     */     //   63: dload #5
/*     */     //   65: aload_1
/*     */     //   66: invokevirtual getY : ()D
/*     */     //   69: dsub
/*     */     //   70: goto -> 75
/*     */     //   73: dload #5
/*     */     //   75: dstore #16
/*     */     //   77: aload #9
/*     */     //   79: getstatic net/minecraft/world/entity/Relative.Z : Lnet/minecraft/world/entity/Relative;
/*     */     //   82: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   87: ifeq -> 100
/*     */     //   90: dload #7
/*     */     //   92: aload_1
/*     */     //   93: invokevirtual getZ : ()D
/*     */     //   96: dsub
/*     */     //   97: goto -> 102
/*     */     //   100: dload #7
/*     */     //   102: dstore #18
/*     */     //   104: aload #9
/*     */     //   106: getstatic net/minecraft/world/entity/Relative.Y_ROT : Lnet/minecraft/world/entity/Relative;
/*     */     //   109: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   114: ifeq -> 127
/*     */     //   117: fload #10
/*     */     //   119: aload_1
/*     */     //   120: invokevirtual getYRot : ()F
/*     */     //   123: fsub
/*     */     //   124: goto -> 129
/*     */     //   127: fload #10
/*     */     //   129: fstore #20
/*     */     //   131: aload #9
/*     */     //   133: getstatic net/minecraft/world/entity/Relative.X_ROT : Lnet/minecraft/world/entity/Relative;
/*     */     //   136: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   141: ifeq -> 154
/*     */     //   144: fload #11
/*     */     //   146: aload_1
/*     */     //   147: invokevirtual getXRot : ()F
/*     */     //   150: fsub
/*     */     //   151: goto -> 156
/*     */     //   154: fload #11
/*     */     //   156: fstore #21
/*     */     //   158: fload #20
/*     */     //   160: invokestatic wrapDegrees : (F)F
/*     */     //   163: fstore #22
/*     */     //   165: fload #21
/*     */     //   167: invokestatic wrapDegrees : (F)F
/*     */     //   170: fstore #23
/*     */     //   172: aload_1
/*     */     //   173: aload_2
/*     */     //   174: dload #14
/*     */     //   176: dload #16
/*     */     //   178: dload #18
/*     */     //   180: aload #9
/*     */     //   182: fload #22
/*     */     //   184: fload #23
/*     */     //   186: iconst_1
/*     */     //   187: invokevirtual teleportTo : (Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z
/*     */     //   190: ifne -> 194
/*     */     //   193: return
/*     */     //   194: aload #12
/*     */     //   196: ifnull -> 208
/*     */     //   199: aload #12
/*     */     //   201: aload_0
/*     */     //   202: aload_1
/*     */     //   203: invokeinterface perform : (Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/entity/Entity;)V
/*     */     //   208: aload_1
/*     */     //   209: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   212: ifeq -> 229
/*     */     //   215: aload_1
/*     */     //   216: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   219: astore #24
/*     */     //   221: aload #24
/*     */     //   223: invokevirtual isFallFlying : ()Z
/*     */     //   226: ifne -> 248
/*     */     //   229: aload_1
/*     */     //   230: aload_1
/*     */     //   231: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   234: dconst_1
/*     */     //   235: dconst_0
/*     */     //   236: dconst_1
/*     */     //   237: invokevirtual multiply : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   240: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   243: aload_1
/*     */     //   244: iconst_1
/*     */     //   245: invokevirtual setOnGround : (Z)V
/*     */     //   248: aload_1
/*     */     //   249: instanceof net/minecraft/world/entity/PathfinderMob
/*     */     //   252: ifeq -> 269
/*     */     //   255: aload_1
/*     */     //   256: checkcast net/minecraft/world/entity/PathfinderMob
/*     */     //   259: astore #24
/*     */     //   261: aload #24
/*     */     //   263: invokevirtual getNavigation : ()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;
/*     */     //   266: invokevirtual stop : ()V
/*     */     //   269: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     //   #151	-> 10
/*     */     //   #152	-> 18
/*     */     //   #156	-> 25
/*     */     //   #157	-> 50
/*     */     //   #158	-> 77
/*     */     //   #159	-> 104
/*     */     //   #160	-> 131
/*     */     //   #162	-> 158
/*     */     //   #163	-> 165
/*     */     //   #165	-> 172
/*     */     //   #166	-> 193
/*     */     //   #169	-> 194
/*     */     //   #170	-> 199
/*     */     //   #173	-> 208
/*     */     //   #174	-> 229
/*     */     //   #175	-> 243
/*     */     //   #178	-> 248
/*     */     //   #179	-> 261
/*     */     //   #181	-> 269
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TeleportCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */