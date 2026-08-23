/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldBorderCommand
/*     */ {
/*  30 */   private static final SimpleCommandExceptionType ERROR_SAME_CENTER = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.center.failed"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_SAME_SIZE = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.set.failed.nochange"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_TOO_SMALL = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.set.failed.small"));
/*  33 */   private static final SimpleCommandExceptionType ERROR_TOO_BIG = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.set.failed.big", new Object[] { Double.valueOf(5.9999968E7D) }));
/*  34 */   private static final SimpleCommandExceptionType ERROR_TOO_FAR_OUT = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.set.failed.far", new Object[] { Double.valueOf(2.9999984E7D) }));
/*  35 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.warning.time.failed"));
/*  36 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.warning.distance.failed"));
/*  37 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.damage.buffer.failed"));
/*  38 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType((Message)Component.translatable("commands.worldborder.damage.amount.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  41 */     paramCommandDispatcher.register(
/*  42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("worldborder")
/*  43 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  44 */         .then(
/*  45 */           Commands.literal("add")
/*  46 */           .then((
/*  47 */             (RequiredArgumentBuilder)Commands.argument("distance", (ArgumentType)DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
/*  48 */             .executes(paramCommandContext -> setSize((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(paramCommandContext, "distance"), 0L)))
/*  49 */             .then(
/*  50 */               Commands.argument("time", (ArgumentType)TimeArgument.time(0))
/*  51 */               .executes(paramCommandContext -> setSize((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(paramCommandContext, "distance"), ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getWorldBorder().getLerpTime() + IntegerArgumentType.getInteger(paramCommandContext, "time")))))))
/*     */ 
/*     */ 
/*     */         
/*  55 */         .then(
/*  56 */           Commands.literal("set")
/*  57 */           .then((
/*  58 */             (RequiredArgumentBuilder)Commands.argument("distance", (ArgumentType)DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
/*  59 */             .executes(paramCommandContext -> setSize((CommandSourceStack)paramCommandContext.getSource(), DoubleArgumentType.getDouble(paramCommandContext, "distance"), 0L)))
/*  60 */             .then(
/*  61 */               Commands.argument("time", (ArgumentType)TimeArgument.time(0))
/*  62 */               .executes(paramCommandContext -> setSize((CommandSourceStack)paramCommandContext.getSource(), DoubleArgumentType.getDouble(paramCommandContext, "distance"), IntegerArgumentType.getInteger(paramCommandContext, "time")))))))
/*     */ 
/*     */ 
/*     */         
/*  66 */         .then(
/*  67 */           Commands.literal("center")
/*  68 */           .then(
/*  69 */             Commands.argument("pos", (ArgumentType)Vec2Argument.vec2())
/*  70 */             .executes(paramCommandContext -> setCenter((CommandSourceStack)paramCommandContext.getSource(), Vec2Argument.getVec2(paramCommandContext, "pos"))))))
/*     */ 
/*     */         
/*  73 */         .then((
/*  74 */           (LiteralArgumentBuilder)Commands.literal("damage")
/*  75 */           .then(
/*  76 */             Commands.literal("amount")
/*  77 */             .then(
/*  78 */               Commands.argument("damagePerBlock", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  79 */               .executes(paramCommandContext -> setDamageAmount((CommandSourceStack)paramCommandContext.getSource(), FloatArgumentType.getFloat(paramCommandContext, "damagePerBlock"))))))
/*     */ 
/*     */           
/*  82 */           .then(
/*  83 */             Commands.literal("buffer")
/*  84 */             .then(
/*  85 */               Commands.argument("distance", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  86 */               .executes(paramCommandContext -> setDamageBuffer((CommandSourceStack)paramCommandContext.getSource(), FloatArgumentType.getFloat(paramCommandContext, "distance")))))))
/*     */ 
/*     */ 
/*     */         
/*  90 */         .then(
/*  91 */           Commands.literal("get")
/*  92 */           .executes(paramCommandContext -> getSize((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  94 */         .then((
/*  95 */           (LiteralArgumentBuilder)Commands.literal("warning")
/*  96 */           .then(
/*  97 */             Commands.literal("distance")
/*  98 */             .then(
/*  99 */               Commands.argument("distance", (ArgumentType)IntegerArgumentType.integer(0))
/* 100 */               .executes(paramCommandContext -> setWarningDistance((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "distance"))))))
/*     */ 
/*     */           
/* 103 */           .then(
/* 104 */             Commands.literal("time")
/* 105 */             .then(
/* 106 */               Commands.argument("time", (ArgumentType)TimeArgument.time(0))
/* 107 */               .executes(paramCommandContext -> setWarningTime((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "time")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setDamageBuffer(CommandSourceStack paramCommandSourceStack, float paramFloat) throws CommandSyntaxException {
/* 115 */     WorldBorder worldBorder = paramCommandSourceStack.getLevel().getWorldBorder();
/* 116 */     if (worldBorder.getSafeZone() == paramFloat) {
/* 117 */       throw ERROR_SAME_DAMAGE_BUFFER.create();
/*     */     }
/* 119 */     worldBorder.setSafeZone(paramFloat);
/* 120 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.damage.buffer.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(paramFloat) }) }), true);
/* 121 */     return (int)paramFloat;
/*     */   }
/*     */   
/*     */   private static int setDamageAmount(CommandSourceStack paramCommandSourceStack, float paramFloat) throws CommandSyntaxException {
/* 125 */     WorldBorder worldBorder = paramCommandSourceStack.getLevel().getWorldBorder();
/* 126 */     if (worldBorder.getDamagePerBlock() == paramFloat) {
/* 127 */       throw ERROR_SAME_DAMAGE_AMOUNT.create();
/*     */     }
/* 129 */     worldBorder.setDamagePerBlock(paramFloat);
/* 130 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.damage.amount.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(paramFloat) }) }), true);
/* 131 */     return (int)paramFloat;
/*     */   }
/*     */   
/*     */   private static int setWarningTime(CommandSourceStack paramCommandSourceStack, int paramInt) throws CommandSyntaxException {
/* 135 */     WorldBorder worldBorder = paramCommandSourceStack.getLevel().getWorldBorder();
/* 136 */     if (worldBorder.getWarningTime() == paramInt) {
/* 137 */       throw ERROR_SAME_WARNING_TIME.create();
/*     */     }
/* 139 */     worldBorder.setWarningTime(paramInt);
/* 140 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.warning.time.success", new Object[] { formatTicksToSeconds(paramInt) }), true);
/* 141 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int setWarningDistance(CommandSourceStack paramCommandSourceStack, int paramInt) throws CommandSyntaxException {
/* 145 */     WorldBorder worldBorder = paramCommandSourceStack.getLevel().getWorldBorder();
/* 146 */     if (worldBorder.getWarningBlocks() == paramInt) {
/* 147 */       throw ERROR_SAME_WARNING_DISTANCE.create();
/*     */     }
/* 149 */     worldBorder.setWarningBlocks(paramInt);
/* 150 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(paramInt) }), true);
/* 151 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int getSize(CommandSourceStack paramCommandSourceStack) {
/* 155 */     double d = paramCommandSourceStack.getLevel().getWorldBorder().getSize();
/* 156 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.get", new Object[] { String.format(Locale.ROOT, "%.0f", new Object[] { Double.valueOf(paramDouble) }) }), false);
/* 157 */     return Mth.floor(d + 0.5D);
/*     */   }
/*     */   
/*     */   private static int setCenter(CommandSourceStack paramCommandSourceStack, Vec2 paramVec2) throws CommandSyntaxException {
/* 161 */     WorldBorder worldBorder = paramCommandSourceStack.getLevel().getWorldBorder();
/* 162 */     if (worldBorder.getCenterX() == paramVec2.x && worldBorder.getCenterZ() == paramVec2.y) {
/* 163 */       throw ERROR_SAME_CENTER.create();
/*     */     }
/*     */     
/* 166 */     if (Math.abs(paramVec2.x) > 2.9999984E7D || Math.abs(paramVec2.y) > 2.9999984E7D) {
/* 167 */       throw ERROR_TOO_FAR_OUT.create();
/*     */     }
/*     */     
/* 170 */     worldBorder.setCenter(paramVec2.x, paramVec2.y);
/* 171 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.center.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(paramVec2.x) }), String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(paramVec2.y) }) }), true);
/*     */     
/* 173 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setSize(CommandSourceStack paramCommandSourceStack, double paramDouble, long paramLong) throws CommandSyntaxException {
/* 177 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 178 */     WorldBorder worldBorder = serverLevel.getWorldBorder();
/* 179 */     double d = worldBorder.getSize();
/*     */     
/* 181 */     if (d == paramDouble) {
/* 182 */       throw ERROR_SAME_SIZE.create();
/*     */     }
/* 184 */     if (paramDouble < 1.0D) {
/* 185 */       throw ERROR_TOO_SMALL.create();
/*     */     }
/* 187 */     if (paramDouble > 5.9999968E7D) {
/* 188 */       throw ERROR_TOO_BIG.create();
/*     */     }
/*     */     
/* 191 */     String str = String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(paramDouble) });
/* 192 */     if (paramLong > 0L) {
/* 193 */       worldBorder.lerpSizeBetween(d, paramDouble, paramLong, serverLevel.getGameTime());
/* 194 */       if (paramDouble > d) {
/* 195 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.grow", new Object[] { paramString, formatTicksToSeconds(paramLong) }), true);
/*     */       } else {
/* 197 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.shrink", new Object[] { paramString, formatTicksToSeconds(paramLong) }), true);
/*     */       } 
/*     */     } else {
/* 200 */       worldBorder.setSize(paramDouble);
/* 201 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.immediate", new Object[] { paramString }), true);
/*     */     } 
/*     */     
/* 204 */     return (int)(paramDouble - d);
/*     */   }
/*     */   
/*     */   private static String formatTicksToSeconds(long paramLong) {
/* 208 */     return String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(paramLong / 20.0D) });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\WorldBorderCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */