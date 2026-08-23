/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeamCommand
/*     */ {
/*  38 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.add.duplicate"));
/*  39 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EMPTY = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.empty.unchanged"));
/*  40 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_NAME = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.name.unchanged"));
/*  41 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_COLOR = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.color.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.friendlyfire.alreadyEnabled"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.friendlyfire.alreadyDisabled"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyEnabled"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyDisabled"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.nametagVisibility.unchanged"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.deathMessageVisibility.unchanged"));
/*  48 */   private static final SimpleCommandExceptionType ERROR_TEAM_COLLISION_UNCHANGED = new SimpleCommandExceptionType((Message)Component.translatable("commands.team.option.collisionRule.unchanged"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  51 */     paramCommandDispatcher.register(
/*  52 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("team")
/*  53 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  54 */         .then((
/*  55 */           (LiteralArgumentBuilder)Commands.literal("list")
/*  56 */           .executes(paramCommandContext -> listTeams((CommandSourceStack)paramCommandContext.getSource())))
/*  57 */           .then(
/*  58 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  59 */             .executes(paramCommandContext -> listMembers((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"))))))
/*     */ 
/*     */         
/*  62 */         .then(
/*  63 */           Commands.literal("add")
/*  64 */           .then((
/*  65 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)StringArgumentType.word())
/*  66 */             .executes(paramCommandContext -> createTeam((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "team"))))
/*  67 */             .then(
/*  68 */               Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  69 */               .executes(paramCommandContext -> createTeam((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "team"), ComponentArgument.getResolvedComponent(paramCommandContext, "displayName")))))))
/*     */ 
/*     */ 
/*     */         
/*  73 */         .then(
/*  74 */           Commands.literal("remove")
/*  75 */           .then(
/*  76 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  77 */             .executes(paramCommandContext -> deleteTeam((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"))))))
/*     */ 
/*     */         
/*  80 */         .then(
/*  81 */           Commands.literal("empty")
/*  82 */           .then(
/*  83 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  84 */             .executes(paramCommandContext -> emptyTeam((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"))))))
/*     */ 
/*     */         
/*  87 */         .then(
/*  88 */           Commands.literal("join")
/*  89 */           .then((
/*  90 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  91 */             .executes(paramCommandContext -> joinTeam((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), (Collection)Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getEntityOrException()))))
/*  92 */             .then(
/*  93 */               Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/*  94 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  95 */               .executes(paramCommandContext -> joinTeam((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "members")))))))
/*     */ 
/*     */ 
/*     */         
/*  99 */         .then(
/* 100 */           Commands.literal("leave")
/* 101 */           .then(
/* 102 */             Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 103 */             .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 104 */             .executes(paramCommandContext -> leaveTeam((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "members"))))))
/*     */ 
/*     */         
/* 107 */         .then(
/* 108 */           Commands.literal("modify")
/* 109 */           .then((
/* 110 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/* 111 */             .then(
/* 112 */               Commands.literal("displayName")
/* 113 */               .then(
/* 114 */                 Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 115 */                 .executes(paramCommandContext -> setDisplayName((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), ComponentArgument.getResolvedComponent(paramCommandContext, "displayName"))))))
/*     */ 
/*     */             
/* 118 */             .then(
/* 119 */               Commands.literal("color")
/* 120 */               .then(
/* 121 */                 Commands.argument("value", (ArgumentType)ColorArgument.color())
/* 122 */                 .executes(paramCommandContext -> setColor((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), ColorArgument.getColor(paramCommandContext, "value"))))))
/*     */ 
/*     */             
/* 125 */             .then(
/* 126 */               Commands.literal("friendlyFire")
/* 127 */               .then(
/* 128 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 129 */                 .executes(paramCommandContext -> setFriendlyFire((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), BoolArgumentType.getBool(paramCommandContext, "allowed"))))))
/*     */ 
/*     */             
/* 132 */             .then(
/* 133 */               Commands.literal("seeFriendlyInvisibles")
/* 134 */               .then(
/* 135 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 136 */                 .executes(paramCommandContext -> setFriendlySight((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), BoolArgumentType.getBool(paramCommandContext, "allowed"))))))
/*     */ 
/*     */             
/* 139 */             .then((
/* 140 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("nametagVisibility")
/* 141 */               .then(Commands.literal("never").executes(paramCommandContext -> setNametagVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.NEVER))))
/* 142 */               .then(Commands.literal("hideForOtherTeams").executes(paramCommandContext -> setNametagVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 143 */               .then(Commands.literal("hideForOwnTeam").executes(paramCommandContext -> setNametagVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 144 */               .then(Commands.literal("always").executes(paramCommandContext -> setNametagVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 146 */             .then((
/* 147 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deathMessageVisibility")
/* 148 */               .then(Commands.literal("never").executes(paramCommandContext -> setDeathMessageVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.NEVER))))
/* 149 */               .then(Commands.literal("hideForOtherTeams").executes(paramCommandContext -> setDeathMessageVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 150 */               .then(Commands.literal("hideForOwnTeam").executes(paramCommandContext -> setDeathMessageVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 151 */               .then(Commands.literal("always").executes(paramCommandContext -> setDeathMessageVisibility((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 153 */             .then((
/* 154 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("collisionRule")
/* 155 */               .then(Commands.literal("never").executes(paramCommandContext -> setCollision((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.CollisionRule.NEVER))))
/* 156 */               .then(Commands.literal("pushOwnTeam").executes(paramCommandContext -> setCollision((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.CollisionRule.PUSH_OWN_TEAM))))
/* 157 */               .then(Commands.literal("pushOtherTeams").executes(paramCommandContext -> setCollision((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.CollisionRule.PUSH_OTHER_TEAMS))))
/* 158 */               .then(Commands.literal("always").executes(paramCommandContext -> setCollision((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), Team.CollisionRule.ALWAYS)))))
/*     */             
/* 160 */             .then(
/* 161 */               Commands.literal("prefix")
/* 162 */               .then(
/* 163 */                 Commands.argument("prefix", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 164 */                 .executes(paramCommandContext -> setPrefix((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), ComponentArgument.getResolvedComponent(paramCommandContext, "prefix"))))))
/*     */ 
/*     */             
/* 167 */             .then(
/* 168 */               Commands.literal("suffix")
/* 169 */               .then(
/* 170 */                 Commands.argument("suffix", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 171 */                 .executes(paramCommandContext -> setSuffix((CommandSourceStack)paramCommandContext.getSource(), TeamArgument.getTeam(paramCommandContext, "team"), ComponentArgument.getResolvedComponent(paramCommandContext, "suffix"))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Component getFirstMemberName(Collection<ScoreHolder> paramCollection) {
/* 180 */     return ((ScoreHolder)paramCollection.iterator().next()).getFeedbackDisplayName();
/*     */   }
/*     */   
/*     */   private static int leaveTeam(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection) {
/* 184 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 186 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 187 */       serverScoreboard.removePlayerFromTeam(scoreHolder.getScoreboardName());
/*     */     }
/*     */     
/* 190 */     if (paramCollection.size() == 1) {
/* 191 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.leave.success.single", new Object[] { getFirstMemberName(paramCollection) }), true);
/*     */     } else {
/* 193 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.leave.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 196 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int joinTeam(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Collection<ScoreHolder> paramCollection) {
/* 200 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 202 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 203 */       serverScoreboard.addPlayerToTeam(scoreHolder.getScoreboardName(), paramPlayerTeam);
/*     */     }
/*     */     
/* 206 */     if (paramCollection.size() == 1) {
/* 207 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.join.success.single", new Object[] { getFirstMemberName(paramCollection), paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     } else {
/* 209 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.join.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 212 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int setNametagVisibility(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Team.Visibility paramVisibility) throws CommandSyntaxException {
/* 216 */     if (paramPlayerTeam.getNameTagVisibility() == paramVisibility) {
/* 217 */       throw ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 219 */     paramPlayerTeam.setNameTagVisibility(paramVisibility);
/* 220 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.nametagVisibility.success", new Object[] { paramPlayerTeam.getFormattedDisplayName(), paramVisibility.getDisplayName() }), true);
/* 221 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDeathMessageVisibility(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Team.Visibility paramVisibility) throws CommandSyntaxException {
/* 225 */     if (paramPlayerTeam.getDeathMessageVisibility() == paramVisibility) {
/* 226 */       throw ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 228 */     paramPlayerTeam.setDeathMessageVisibility(paramVisibility);
/* 229 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.deathMessageVisibility.success", new Object[] { paramPlayerTeam.getFormattedDisplayName(), paramVisibility.getDisplayName() }), true);
/* 230 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setCollision(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Team.CollisionRule paramCollisionRule) throws CommandSyntaxException {
/* 234 */     if (paramPlayerTeam.getCollisionRule() == paramCollisionRule) {
/* 235 */       throw ERROR_TEAM_COLLISION_UNCHANGED.create();
/*     */     }
/* 237 */     paramPlayerTeam.setCollisionRule(paramCollisionRule);
/* 238 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.collisionRule.success", new Object[] { paramPlayerTeam.getFormattedDisplayName(), paramCollisionRule.getDisplayName() }), true);
/* 239 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlySight(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, boolean paramBoolean) throws CommandSyntaxException {
/* 243 */     if (paramPlayerTeam.canSeeFriendlyInvisibles() == paramBoolean) {
/* 244 */       if (paramBoolean) {
/* 245 */         throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED.create();
/*     */       }
/* 247 */       throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 251 */     paramPlayerTeam.setSeeFriendlyInvisibles(paramBoolean);
/* 252 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.seeFriendlyInvisibles." + (paramBoolean ? "enabled" : "disabled"), new Object[] { paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     
/* 254 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlyFire(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, boolean paramBoolean) throws CommandSyntaxException {
/* 258 */     if (paramPlayerTeam.isAllowFriendlyFire() == paramBoolean) {
/* 259 */       if (paramBoolean) {
/* 260 */         throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED.create();
/*     */       }
/* 262 */       throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 266 */     paramPlayerTeam.setAllowFriendlyFire(paramBoolean);
/* 267 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.friendlyfire." + (paramBoolean ? "enabled" : "disabled"), new Object[] { paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     
/* 269 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Component paramComponent) throws CommandSyntaxException {
/* 273 */     if (paramPlayerTeam.getDisplayName().equals(paramComponent)) {
/* 274 */       throw ERROR_TEAM_ALREADY_NAME.create();
/*     */     }
/*     */     
/* 277 */     paramPlayerTeam.setDisplayName(paramComponent);
/* 278 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.name.success", new Object[] { paramPlayerTeam.getFormattedDisplayName() }), true);
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, ChatFormatting paramChatFormatting) throws CommandSyntaxException {
/* 283 */     if (paramPlayerTeam.getColor() == paramChatFormatting) {
/* 284 */       throw ERROR_TEAM_ALREADY_COLOR.create();
/*     */     }
/* 286 */     paramPlayerTeam.setColor(paramChatFormatting);
/* 287 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.color.success", new Object[] { paramPlayerTeam.getFormattedDisplayName(), paramChatFormatting.getName() }), true);
/* 288 */     return 0;
/*     */   }
/*     */   
/*     */   private static int emptyTeam(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam) throws CommandSyntaxException {
/* 292 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 293 */     ArrayList arrayList = Lists.newArrayList(paramPlayerTeam.getPlayers());
/*     */     
/* 295 */     if (arrayList.isEmpty()) {
/* 296 */       throw ERROR_TEAM_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 299 */     for (String str : arrayList) {
/* 300 */       serverScoreboard.removePlayerFromTeam(str, paramPlayerTeam);
/*     */     }
/*     */     
/* 303 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.empty.success", new Object[] { Integer.valueOf(paramCollection.size()), paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     
/* 305 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static int deleteTeam(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam) {
/* 309 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 310 */     serverScoreboard.removePlayerTeam(paramPlayerTeam);
/* 311 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.remove.success", new Object[] { paramPlayerTeam.getFormattedDisplayName() }), true);
/* 312 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack paramCommandSourceStack, String paramString) throws CommandSyntaxException {
/* 316 */     return createTeam(paramCommandSourceStack, paramString, (Component)Component.literal(paramString));
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack paramCommandSourceStack, String paramString, Component paramComponent) throws CommandSyntaxException {
/* 320 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 321 */     if (serverScoreboard.getPlayerTeam(paramString) != null) {
/* 322 */       throw ERROR_TEAM_ALREADY_EXISTS.create();
/*     */     }
/*     */     
/* 325 */     PlayerTeam playerTeam = serverScoreboard.addPlayerTeam(paramString);
/* 326 */     playerTeam.setDisplayName(paramComponent);
/*     */     
/* 328 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.add.success", new Object[] { paramPlayerTeam.getFormattedDisplayName() }), true);
/*     */     
/* 330 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int listMembers(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam) {
/* 334 */     Collection collection = paramPlayerTeam.getPlayers();
/* 335 */     if (collection.isEmpty()) {
/* 336 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.list.members.empty", new Object[] { paramPlayerTeam.getFormattedDisplayName() }), false);
/*     */     } else {
/* 338 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.list.members.success", new Object[] { paramPlayerTeam.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection) }), false);
/*     */     } 
/* 340 */     return collection.size();
/*     */   }
/*     */   
/*     */   private static int listTeams(CommandSourceStack paramCommandSourceStack) {
/* 344 */     Collection collection = paramCommandSourceStack.getServer().getScoreboard().getPlayerTeams();
/* 345 */     if (collection.isEmpty()) {
/* 346 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.list.teams.empty"), false);
/*     */     } else {
/* 348 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.list.teams.success", new Object[] { Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, PlayerTeam::getFormattedDisplayName) }), false);
/*     */     } 
/* 350 */     return collection.size();
/*     */   }
/*     */   
/*     */   private static int setPrefix(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Component paramComponent) {
/* 354 */     paramPlayerTeam.setPlayerPrefix(paramComponent);
/* 355 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.prefix.success", new Object[] { paramComponent }), false);
/* 356 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setSuffix(CommandSourceStack paramCommandSourceStack, PlayerTeam paramPlayerTeam, Component paramComponent) {
/* 360 */     paramPlayerTeam.setPlayerSuffix(paramComponent);
/* 361 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.team.option.suffix.success", new Object[] { paramComponent }), false);
/* 362 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TeamCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */