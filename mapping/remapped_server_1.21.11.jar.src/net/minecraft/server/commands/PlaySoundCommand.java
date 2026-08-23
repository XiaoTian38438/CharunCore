/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlaySoundCommand
/*     */ {
/*  39 */   private static final SimpleCommandExceptionType ERROR_TOO_FAR = new SimpleCommandExceptionType((Message)Component.translatable("commands.playsound.failed"));
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  44 */     RequiredArgumentBuilder requiredArgumentBuilder = (RequiredArgumentBuilder)Commands.argument("sound", (ArgumentType)IdentifierArgument.id()).suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS)).executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), getCallingPlayerAsCollection(((CommandSourceStack)paramCommandContext.getSource()).getPlayer()), IdentifierArgument.getId(paramCommandContext, "sound"), SoundSource.MASTER, ((CommandSourceStack)paramCommandContext.getSource()).getPosition(), 1.0F, 1.0F, 0.0F));
/*     */     
/*  46 */     for (SoundSource soundSource : SoundSource.values()) {
/*  47 */       requiredArgumentBuilder.then((ArgumentBuilder)source(soundSource));
/*     */     }
/*     */     
/*  50 */     paramCommandDispatcher.register(
/*  51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("playsound")
/*  52 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */         .then((ArgumentBuilder)requiredArgumentBuilder));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> source(SoundSource paramSoundSource) {
/*  58 */     return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(paramSoundSource.getName())
/*  59 */       .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), getCallingPlayerAsCollection(((CommandSourceStack)paramCommandContext.getSource()).getPlayer()), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, ((CommandSourceStack)paramCommandContext.getSource()).getPosition(), 1.0F, 1.0F, 0.0F)))
/*  60 */       .then((
/*  61 */         (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  62 */         .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, ((CommandSourceStack)paramCommandContext.getSource()).getPosition(), 1.0F, 1.0F, 0.0F)))
/*  63 */         .then((
/*  64 */           (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  65 */           .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, Vec3Argument.getVec3(paramCommandContext, "pos"), 1.0F, 1.0F, 0.0F)))
/*  66 */           .then((
/*  67 */             (RequiredArgumentBuilder)Commands.argument("volume", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  68 */             .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, Vec3Argument.getVec3(paramCommandContext, "pos"), ((Float)paramCommandContext.getArgument("volume", Float.class)).floatValue(), 1.0F, 0.0F)))
/*  69 */             .then((
/*  70 */               (RequiredArgumentBuilder)Commands.argument("pitch", (ArgumentType)FloatArgumentType.floatArg(0.0F, 2.0F))
/*  71 */               .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, Vec3Argument.getVec3(paramCommandContext, "pos"), ((Float)paramCommandContext.getArgument("volume", Float.class)).floatValue(), ((Float)paramCommandContext.getArgument("pitch", Float.class)).floatValue(), 0.0F)))
/*  72 */               .then(
/*  73 */                 Commands.argument("minVolume", (ArgumentType)FloatArgumentType.floatArg(0.0F, 1.0F))
/*  74 */                 .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IdentifierArgument.getId(paramCommandContext, "sound"), paramSoundSource, Vec3Argument.getVec3(paramCommandContext, "pos"), ((Float)paramCommandContext.getArgument("volume", Float.class)).floatValue(), ((Float)paramCommandContext.getArgument("pitch", Float.class)).floatValue(), ((Float)paramCommandContext.getArgument("minVolume", Float.class)).floatValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<ServerPlayer> getCallingPlayerAsCollection(ServerPlayer paramServerPlayer) {
/*  83 */     return (paramServerPlayer != null) ? List.<ServerPlayer>of(paramServerPlayer) : List.<ServerPlayer>of();
/*     */   }
/*     */   
/*     */   private static int playSound(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Identifier paramIdentifier, SoundSource paramSoundSource, Vec3 paramVec3, float paramFloat1, float paramFloat2, float paramFloat3) throws CommandSyntaxException {
/*  87 */     Holder holder = Holder.direct(SoundEvent.createVariableRangeEvent(paramIdentifier));
/*  88 */     double d = Mth.square(((SoundEvent)holder.value()).getRange(paramFloat1));
/*  89 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*  90 */     long l = serverLevel.getRandom().nextLong();
/*     */     
/*  92 */     ArrayList<ServerPlayer> arrayList = new ArrayList();
/*     */     
/*  94 */     for (ServerPlayer serverPlayer : paramCollection) {
/*  95 */       if (serverPlayer.level() != serverLevel) {
/*     */         continue;
/*     */       }
/*  98 */       double d1 = paramVec3.x - serverPlayer.getX();
/*  99 */       double d2 = paramVec3.y - serverPlayer.getY();
/* 100 */       double d3 = paramVec3.z - serverPlayer.getZ();
/* 101 */       double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/* 102 */       Vec3 vec3 = paramVec3;
/* 103 */       float f = paramFloat1;
/*     */       
/* 105 */       if (d4 > d) {
/* 106 */         if (paramFloat3 <= 0.0F) {
/*     */           continue;
/*     */         }
/*     */         
/* 110 */         double d5 = Math.sqrt(d4);
/* 111 */         vec3 = new Vec3(serverPlayer.getX() + d1 / d5 * 2.0D, serverPlayer.getY() + d2 / d5 * 2.0D, serverPlayer.getZ() + d3 / d5 * 2.0D);
/* 112 */         f = paramFloat3;
/*     */       } 
/*     */       
/* 115 */       serverPlayer.connection.send((Packet)new ClientboundSoundPacket(holder, paramSoundSource, vec3.x(), vec3.y(), vec3.z(), f, paramFloat2, l));
/* 116 */       arrayList.add(serverPlayer);
/*     */     } 
/*     */     
/* 119 */     int i = arrayList.size();
/* 120 */     if (i == 0)
/* 121 */       throw ERROR_TOO_FAR.create(); 
/* 122 */     if (i == 1) {
/* 123 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.playsound.success.single", new Object[] { Component.translationArg(paramIdentifier), ((ServerPlayer)paramList.getFirst()).getDisplayName() }), true);
/*     */     } else {
/* 125 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.playsound.success.multiple", new Object[] { Component.translationArg(paramIdentifier), Integer.valueOf(paramInt) }), true);
/*     */     } 
/*     */     
/* 128 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PlaySoundCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */