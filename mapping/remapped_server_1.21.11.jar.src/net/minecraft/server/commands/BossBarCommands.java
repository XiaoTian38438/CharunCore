/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.bossevents.CustomBossEvent;
/*     */ import net.minecraft.server.bossevents.CustomBossEvents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class BossBarCommands {
/*     */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
/*     */   
/*     */   static {
/*  39 */     ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.bossbar.create.failed", new Object[] { paramObject }));
/*  40 */     ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.bossbar.unknown", new Object[] { paramObject }));
/*  41 */   } private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST; private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.players.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.name.unchanged"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.color.unchanged"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.style.unchanged"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.value.unchanged"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.max.unchanged"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.visibility.unchanged.hidden"));
/*  48 */   private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType((Message)Component.translatable("commands.bossbar.set.visibility.unchanged.visible")); public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR; static {
/*  49 */     SUGGEST_BOSS_BAR = ((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)paramCommandContext.getSource()).getServer().getCustomBossEvents().getIds(), paramSuggestionsBuilder));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  52 */     paramCommandDispatcher.register(
/*  53 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bossbar")
/*  54 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  55 */         .then(
/*  56 */           Commands.literal("add")
/*  57 */           .then(
/*  58 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  59 */             .then(
/*  60 */               Commands.argument("name", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  61 */               .executes(paramCommandContext -> createBar((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"), ComponentArgument.getResolvedComponent(paramCommandContext, "name")))))))
/*     */ 
/*     */ 
/*     */         
/*  65 */         .then(
/*  66 */           Commands.literal("remove")
/*  67 */           .then(
/*  68 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  69 */             .suggests(SUGGEST_BOSS_BAR)
/*  70 */             .executes(paramCommandContext -> removeBar((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext))))))
/*     */ 
/*     */         
/*  73 */         .then(
/*  74 */           Commands.literal("list")
/*  75 */           .executes(paramCommandContext -> listBars((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  77 */         .then(
/*  78 */           Commands.literal("set")
/*  79 */           .then((
/*  80 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  81 */             .suggests(SUGGEST_BOSS_BAR)
/*  82 */             .then(
/*  83 */               Commands.literal("name")
/*  84 */               .then(
/*  85 */                 Commands.argument("name", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  86 */                 .executes(paramCommandContext -> setName((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), ComponentArgument.getResolvedComponent(paramCommandContext, "name"))))))
/*     */ 
/*     */             
/*  89 */             .then((
/*  90 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  91 */               .then(
/*  92 */                 Commands.literal("pink")
/*  93 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.PINK))))
/*     */               
/*  95 */               .then(
/*  96 */                 Commands.literal("blue")
/*  97 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.BLUE))))
/*     */               
/*  99 */               .then(
/* 100 */                 Commands.literal("red")
/* 101 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.RED))))
/*     */               
/* 103 */               .then(
/* 104 */                 Commands.literal("green")
/* 105 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.GREEN))))
/*     */               
/* 107 */               .then(
/* 108 */                 Commands.literal("yellow")
/* 109 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.YELLOW))))
/*     */               
/* 111 */               .then(
/* 112 */                 Commands.literal("purple")
/* 113 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.PURPLE))))
/*     */               
/* 115 */               .then(
/* 116 */                 Commands.literal("white")
/* 117 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarColor.WHITE)))))
/*     */ 
/*     */             
/* 120 */             .then((
/* 121 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("style")
/* 122 */               .then(
/* 123 */                 Commands.literal("progress")
/* 124 */                 .executes(paramCommandContext -> setStyle((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarOverlay.PROGRESS))))
/*     */               
/* 126 */               .then(
/* 127 */                 Commands.literal("notched_6")
/* 128 */                 .executes(paramCommandContext -> setStyle((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarOverlay.NOTCHED_6))))
/*     */               
/* 130 */               .then(
/* 131 */                 Commands.literal("notched_10")
/* 132 */                 .executes(paramCommandContext -> setStyle((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarOverlay.NOTCHED_10))))
/*     */               
/* 134 */               .then(
/* 135 */                 Commands.literal("notched_12")
/* 136 */                 .executes(paramCommandContext -> setStyle((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarOverlay.NOTCHED_12))))
/*     */               
/* 138 */               .then(
/* 139 */                 Commands.literal("notched_20")
/* 140 */                 .executes(paramCommandContext -> setStyle((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BossEvent.BossBarOverlay.NOTCHED_20)))))
/*     */ 
/*     */             
/* 143 */             .then(
/* 144 */               Commands.literal("value")
/* 145 */               .then(
/* 146 */                 Commands.argument("value", (ArgumentType)IntegerArgumentType.integer(0))
/* 147 */                 .executes(paramCommandContext -> setValue((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), IntegerArgumentType.getInteger(paramCommandContext, "value"))))))
/*     */ 
/*     */             
/* 150 */             .then(
/* 151 */               Commands.literal("max")
/* 152 */               .then(
/* 153 */                 Commands.argument("max", (ArgumentType)IntegerArgumentType.integer(1))
/* 154 */                 .executes(paramCommandContext -> setMax((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), IntegerArgumentType.getInteger(paramCommandContext, "max"))))))
/*     */ 
/*     */             
/* 157 */             .then(
/* 158 */               Commands.literal("visible")
/* 159 */               .then(
/* 160 */                 Commands.argument("visible", (ArgumentType)BoolArgumentType.bool())
/* 161 */                 .executes(paramCommandContext -> setVisible((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), BoolArgumentType.getBool(paramCommandContext, "visible"))))))
/*     */ 
/*     */             
/* 164 */             .then((
/* 165 */               (LiteralArgumentBuilder)Commands.literal("players")
/* 166 */               .executes(paramCommandContext -> setPlayers((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), Collections.emptyList())))
/* 167 */               .then(
/* 168 */                 Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 169 */                 .executes(paramCommandContext -> setPlayers((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext), EntityArgument.getOptionalPlayers(paramCommandContext, "targets"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         .then(
/* 175 */           Commands.literal("get")
/* 176 */           .then((
/* 177 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/* 178 */             .suggests(SUGGEST_BOSS_BAR)
/* 179 */             .then(
/* 180 */               Commands.literal("value")
/* 181 */               .executes(paramCommandContext -> getValue((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext)))))
/*     */             
/* 183 */             .then(
/* 184 */               Commands.literal("max")
/* 185 */               .executes(paramCommandContext -> getMax((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext)))))
/*     */             
/* 187 */             .then(
/* 188 */               Commands.literal("visible")
/* 189 */               .executes(paramCommandContext -> getVisible((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext)))))
/*     */             
/* 191 */             .then(
/* 192 */               Commands.literal("players")
/* 193 */               .executes(paramCommandContext -> getPlayers((CommandSourceStack)paramCommandContext.getSource(), getBossBar(paramCommandContext)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getValue(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent) {
/* 201 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.value", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramCustomBossEvent.getValue()) }), true);
/* 202 */     return paramCustomBossEvent.getValue();
/*     */   }
/*     */   
/*     */   private static int getMax(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent) {
/* 206 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.max", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramCustomBossEvent.getMax()) }), true);
/* 207 */     return paramCustomBossEvent.getMax();
/*     */   }
/*     */   
/*     */   private static int getVisible(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent) {
/* 211 */     if (paramCustomBossEvent.isVisible()) {
/* 212 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.visible", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 213 */       return 1;
/*     */     } 
/* 215 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.hidden", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getPlayers(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent) {
/* 221 */     if (paramCustomBossEvent.getPlayers().isEmpty()) {
/* 222 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.none", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/*     */     } else {
/* 224 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.some", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramCustomBossEvent.getPlayers().size()), ComponentUtils.formatList(paramCustomBossEvent.getPlayers(), Player::getDisplayName) }), true);
/*     */     } 
/* 226 */     return paramCustomBossEvent.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int setVisible(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, boolean paramBoolean) throws CommandSyntaxException {
/* 230 */     if (paramCustomBossEvent.isVisible() == paramBoolean) {
/* 231 */       if (paramBoolean) {
/* 232 */         throw ERROR_ALREADY_VISIBLE.create();
/*     */       }
/* 234 */       throw ERROR_ALREADY_HIDDEN.create();
/*     */     } 
/*     */     
/* 237 */     paramCustomBossEvent.setVisible(paramBoolean);
/* 238 */     if (paramBoolean) {
/* 239 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.visible", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/*     */     } else {
/* 241 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.hidden", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/*     */     } 
/* 243 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, int paramInt) throws CommandSyntaxException {
/* 247 */     if (paramCustomBossEvent.getValue() == paramInt) {
/* 248 */       throw ERROR_NO_VALUE_CHANGE.create();
/*     */     }
/* 250 */     paramCustomBossEvent.setValue(paramInt);
/* 251 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.value.success", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramInt) }), true);
/* 252 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int setMax(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, int paramInt) throws CommandSyntaxException {
/* 256 */     if (paramCustomBossEvent.getMax() == paramInt) {
/* 257 */       throw ERROR_NO_MAX_CHANGE.create();
/*     */     }
/* 259 */     paramCustomBossEvent.setMax(paramInt);
/* 260 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.max.success", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramInt) }), true);
/* 261 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, BossEvent.BossBarColor paramBossBarColor) throws CommandSyntaxException {
/* 265 */     if (paramCustomBossEvent.getColor().equals(paramBossBarColor)) {
/* 266 */       throw ERROR_NO_COLOR_CHANGE.create();
/*     */     }
/* 268 */     paramCustomBossEvent.setColor(paramBossBarColor);
/* 269 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.color.success", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setStyle(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, BossEvent.BossBarOverlay paramBossBarOverlay) throws CommandSyntaxException {
/* 274 */     if (paramCustomBossEvent.getOverlay().equals(paramBossBarOverlay)) {
/* 275 */       throw ERROR_NO_STYLE_CHANGE.create();
/*     */     }
/* 277 */     paramCustomBossEvent.setOverlay(paramBossBarOverlay);
/* 278 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.style.success", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setName(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, Component paramComponent) throws CommandSyntaxException {
/* 283 */     MutableComponent mutableComponent = ComponentUtils.updateForEntity(paramCommandSourceStack, paramComponent, null, 0);
/* 284 */     if (paramCustomBossEvent.getName().equals(mutableComponent)) {
/* 285 */       throw ERROR_NO_NAME_CHANGE.create();
/*     */     }
/* 287 */     paramCustomBossEvent.setName((Component)mutableComponent);
/* 288 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.name.success", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 289 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setPlayers(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent, Collection<ServerPlayer> paramCollection) throws CommandSyntaxException {
/* 293 */     boolean bool = paramCustomBossEvent.setPlayers(paramCollection);
/* 294 */     if (!bool) {
/* 295 */       throw ERROR_NO_PLAYER_CHANGE.create();
/*     */     }
/* 297 */     if (paramCustomBossEvent.getPlayers().isEmpty()) {
/* 298 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.none", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/*     */     } else {
/* 300 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.some", new Object[] { paramCustomBossEvent.getDisplayName(), Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, Player::getDisplayName) }), true);
/*     */     } 
/* 302 */     return paramCustomBossEvent.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int listBars(CommandSourceStack paramCommandSourceStack) {
/* 306 */     Collection collection = paramCommandSourceStack.getServer().getCustomBossEvents().getEvents();
/* 307 */     if (collection.isEmpty()) {
/* 308 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.none"), false);
/*     */     } else {
/* 310 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.some", new Object[] { Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, CustomBossEvent::getDisplayName) }), false);
/*     */     } 
/* 312 */     return collection.size();
/*     */   }
/*     */   
/*     */   private static int createBar(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier, Component paramComponent) throws CommandSyntaxException {
/* 316 */     CustomBossEvents customBossEvents = paramCommandSourceStack.getServer().getCustomBossEvents();
/* 317 */     if (customBossEvents.get(paramIdentifier) != null) {
/* 318 */       throw ERROR_ALREADY_EXISTS.create(paramIdentifier.toString());
/*     */     }
/* 320 */     CustomBossEvent customBossEvent = customBossEvents.create(paramIdentifier, (Component)ComponentUtils.updateForEntity(paramCommandSourceStack, paramComponent, null, 0));
/* 321 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.create.success", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 322 */     return customBossEvents.getEvents().size();
/*     */   }
/*     */   
/*     */   private static int removeBar(CommandSourceStack paramCommandSourceStack, CustomBossEvent paramCustomBossEvent) {
/* 326 */     CustomBossEvents customBossEvents = paramCommandSourceStack.getServer().getCustomBossEvents();
/* 327 */     paramCustomBossEvent.removeAllPlayers();
/* 328 */     customBossEvents.remove(paramCustomBossEvent);
/* 329 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.bossbar.remove.success", new Object[] { paramCustomBossEvent.getDisplayName() }), true);
/* 330 */     return customBossEvents.getEvents().size();
/*     */   }
/*     */   
/*     */   public static CustomBossEvent getBossBar(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException {
/* 334 */     Identifier identifier = IdentifierArgument.getId(paramCommandContext, "id");
/* 335 */     CustomBossEvent customBossEvent = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getCustomBossEvents().get(identifier);
/* 336 */     if (customBossEvent == null) {
/* 337 */       throw ERROR_DOESNT_EXIST.create(identifier.toString());
/*     */     }
/* 339 */     return customBossEvent;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\BossBarCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */