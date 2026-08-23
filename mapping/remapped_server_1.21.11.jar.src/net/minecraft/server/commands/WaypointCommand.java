/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.HexFormat;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.HexColorArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.WaypointArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.waypoints.Waypoint;
/*     */ import net.minecraft.world.waypoints.WaypointStyleAsset;
/*     */ import net.minecraft.world.waypoints.WaypointStyleAssets;
/*     */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*     */ 
/*     */ public class WaypointCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  42 */     paramCommandDispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("waypoint")
/*  44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  45 */         .then(
/*  46 */           Commands.literal("list")
/*  47 */           .executes(paramCommandContext -> listWaypoints((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  49 */         .then(
/*  50 */           Commands.literal("modify")
/*  51 */           .then((
/*  52 */             (RequiredArgumentBuilder)Commands.argument("waypoint", (ArgumentType)EntityArgument.entity())
/*  53 */             .then((
/*  54 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  55 */               .then(
/*  56 */                 Commands.argument("color", (ArgumentType)ColorArgument.color())
/*  57 */                 .executes(paramCommandContext -> setWaypointColor((CommandSourceStack)paramCommandContext.getSource(), WaypointArgument.getWaypoint(paramCommandContext, "waypoint"), ColorArgument.getColor(paramCommandContext, "color")))))
/*     */               
/*  59 */               .then(
/*  60 */                 Commands.literal("hex").then(
/*  61 */                   Commands.argument("color", (ArgumentType)HexColorArgument.hexColor())
/*  62 */                   .executes(paramCommandContext -> setWaypointColor((CommandSourceStack)paramCommandContext.getSource(), WaypointArgument.getWaypoint(paramCommandContext, "waypoint"), HexColorArgument.getHexColor(paramCommandContext, "color"))))))
/*     */ 
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("reset")
/*  67 */                 .executes(paramCommandContext -> resetWaypointColor((CommandSourceStack)paramCommandContext.getSource(), WaypointArgument.getWaypoint(paramCommandContext, "waypoint"))))))
/*     */ 
/*     */             
/*  70 */             .then((
/*  71 */               (LiteralArgumentBuilder)Commands.literal("style")
/*  72 */               .then(
/*  73 */                 Commands.literal("reset")
/*  74 */                 .executes(paramCommandContext -> setWaypointStyle((CommandSourceStack)paramCommandContext.getSource(), WaypointArgument.getWaypoint(paramCommandContext, "waypoint"), WaypointStyleAssets.DEFAULT))))
/*     */               
/*  76 */               .then(
/*  77 */                 Commands.literal("set")
/*  78 */                 .then(
/*  79 */                   Commands.argument("style", (ArgumentType)IdentifierArgument.id())
/*  80 */                   .executes(paramCommandContext -> setWaypointStyle((CommandSourceStack)paramCommandContext.getSource(), WaypointArgument.getWaypoint(paramCommandContext, "waypoint"), ResourceKey.create(WaypointStyleAssets.ROOT_ID, IdentifierArgument.getId(paramCommandContext, "style"))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setWaypointStyle(CommandSourceStack paramCommandSourceStack, WaypointTransmitter paramWaypointTransmitter, ResourceKey<WaypointStyleAsset> paramResourceKey) {
/*  90 */     mutateIcon(paramCommandSourceStack, paramWaypointTransmitter, paramIcon -> paramIcon.style = paramResourceKey);
/*  91 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.modify.style"), false);
/*  92 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setWaypointColor(CommandSourceStack paramCommandSourceStack, WaypointTransmitter paramWaypointTransmitter, ChatFormatting paramChatFormatting) {
/*  96 */     mutateIcon(paramCommandSourceStack, paramWaypointTransmitter, paramIcon -> paramIcon.color = Optional.of(paramChatFormatting.getColor()));
/*  97 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", new Object[] { Component.literal(paramChatFormatting.getName()).withStyle(paramChatFormatting) }), false);
/*  98 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setWaypointColor(CommandSourceStack paramCommandSourceStack, WaypointTransmitter paramWaypointTransmitter, Integer paramInteger) {
/* 102 */     mutateIcon(paramCommandSourceStack, paramWaypointTransmitter, paramIcon -> paramIcon.color = Optional.of(paramInteger));
/* 103 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", new Object[] { Component.literal(HexFormat.of().withUpperCase().toHexDigits(ARGB.color(0, paramInteger.intValue()), 6)).withColor(paramInteger.intValue()) }), false);
/* 104 */     return 0;
/*     */   }
/*     */   
/*     */   private static int resetWaypointColor(CommandSourceStack paramCommandSourceStack, WaypointTransmitter paramWaypointTransmitter) {
/* 108 */     mutateIcon(paramCommandSourceStack, paramWaypointTransmitter, paramIcon -> paramIcon.color = Optional.empty());
/* 109 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color.reset"), false);
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   private static int listWaypoints(CommandSourceStack paramCommandSourceStack) {
/* 114 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 115 */     Set set = serverLevel.getWaypointManager().transmitters();
/* 116 */     String str = serverLevel.dimension().identifier().toString();
/*     */     
/* 118 */     if (set.isEmpty()) {
/* 119 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.list.empty", new Object[] { paramString }), false);
/* 120 */       return 0;
/*     */     } 
/*     */     
/* 123 */     Component component = ComponentUtils.formatList(set.stream().map(paramWaypointTransmitter -> {
/*     */             if (paramWaypointTransmitter instanceof LivingEntity) {
/*     */               LivingEntity livingEntity = (LivingEntity)paramWaypointTransmitter;
/*     */ 
/*     */               
/*     */               BlockPos blockPos = livingEntity.blockPosition();
/*     */               
/*     */               return livingEntity.getFeedbackDisplayName().copy().withStyle(());
/*     */             } 
/*     */             
/*     */             return Component.literal(paramWaypointTransmitter.toString());
/* 134 */           }).toList(), Function.identity());
/*     */     
/* 136 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.waypoint.list.success", new Object[] { Integer.valueOf(paramSet.size()), paramString, paramComponent }), false);
/*     */     
/* 138 */     return set.size();
/*     */   }
/*     */   
/*     */   private static void mutateIcon(CommandSourceStack paramCommandSourceStack, WaypointTransmitter paramWaypointTransmitter, Consumer<Waypoint.Icon> paramConsumer) {
/* 142 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 143 */     serverLevel.getWaypointManager().untrackWaypoint(paramWaypointTransmitter);
/*     */     
/* 145 */     paramConsumer.accept(paramWaypointTransmitter.waypointIcon());
/*     */     
/* 147 */     serverLevel.getWaypointManager().trackWaypoint(paramWaypointTransmitter);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\WaypointCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */