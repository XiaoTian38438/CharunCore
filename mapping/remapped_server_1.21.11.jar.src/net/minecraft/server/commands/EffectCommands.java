/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EffectCommands
/*     */ {
/*  34 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.effect.give.failed"));
/*  35 */   private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.effect.clear.everything.failed"));
/*  36 */   private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.effect.clear.specific.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  39 */     paramCommandDispatcher.register(
/*  40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("effect")
/*  41 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  42 */         .then((
/*  43 */           (LiteralArgumentBuilder)Commands.literal("clear")
/*  44 */           .executes(paramCommandContext -> clearEffects((CommandSourceStack)paramCommandContext.getSource(), (Collection<? extends Entity>)ImmutableList.of(((CommandSourceStack)paramCommandContext.getSource()).getEntityOrException()))))
/*  45 */           .then((
/*  46 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  47 */             .executes(paramCommandContext -> clearEffects((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"))))
/*  48 */             .then(
/*  49 */               Commands.argument("effect", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.MOB_EFFECT))
/*  50 */               .executes(paramCommandContext -> clearEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect")))))))
/*     */ 
/*     */ 
/*     */         
/*  54 */         .then(
/*  55 */           Commands.literal("give")
/*  56 */           .then(
/*  57 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  58 */             .then((
/*  59 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("effect", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.MOB_EFFECT))
/*  60 */               .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), null, 0, true)))
/*  61 */               .then((
/*  62 */                 (RequiredArgumentBuilder)Commands.argument("seconds", (ArgumentType)IntegerArgumentType.integer(1, 1000000))
/*  63 */                 .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(paramCommandContext, "seconds")), 0, true)))
/*  64 */                 .then((
/*  65 */                   (RequiredArgumentBuilder)Commands.argument("amplifier", (ArgumentType)IntegerArgumentType.integer(0, 255))
/*  66 */                   .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(paramCommandContext, "seconds")), IntegerArgumentType.getInteger(paramCommandContext, "amplifier"), true)))
/*  67 */                   .then(
/*  68 */                     Commands.argument("hideParticles", (ArgumentType)BoolArgumentType.bool())
/*  69 */                     .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(paramCommandContext, "seconds")), IntegerArgumentType.getInteger(paramCommandContext, "amplifier"), !BoolArgumentType.getBool(paramCommandContext, "hideParticles")))))))
/*     */ 
/*     */ 
/*     */               
/*  73 */               .then((
/*  74 */                 (LiteralArgumentBuilder)Commands.literal("infinite")
/*  75 */                 .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(-1), 0, true)))
/*  76 */                 .then((
/*  77 */                   (RequiredArgumentBuilder)Commands.argument("amplifier", (ArgumentType)IntegerArgumentType.integer(0, 255))
/*  78 */                   .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(-1), IntegerArgumentType.getInteger(paramCommandContext, "amplifier"), true)))
/*  79 */                   .then(
/*  80 */                     Commands.argument("hideParticles", (ArgumentType)BoolArgumentType.bool())
/*  81 */                     .executes(paramCommandContext -> giveEffect((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), (Holder<MobEffect>)ResourceArgument.getMobEffect(paramCommandContext, "effect"), Integer.valueOf(-1), IntegerArgumentType.getInteger(paramCommandContext, "amplifier"), !BoolArgumentType.getBool(paramCommandContext, "hideParticles"))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int giveEffect(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, Holder<MobEffect> paramHolder, Integer paramInteger, int paramInt, boolean paramBoolean) throws CommandSyntaxException {
/*     */     char c;
/*  92 */     MobEffect mobEffect = (MobEffect)paramHolder.value();
/*  93 */     byte b = 0;
/*     */ 
/*     */     
/*  96 */     if (paramInteger != null) {
/*  97 */       if (mobEffect.isInstantenous()) {
/*  98 */         c = paramInteger.intValue();
/*  99 */       } else if (paramInteger.intValue() == -1) {
/* 100 */         c = '￿';
/*     */       } else {
/* 102 */         c = paramInteger.intValue() * 20;
/*     */       }
/*     */     
/* 105 */     } else if (mobEffect.isInstantenous()) {
/* 106 */       c = '\001';
/*     */     } else {
/* 108 */       c = 'ɘ';
/*     */     } 
/*     */ 
/*     */     
/* 112 */     for (Entity entity : paramCollection) {
/* 113 */       if (entity instanceof LivingEntity) {
/* 114 */         MobEffectInstance mobEffectInstance = new MobEffectInstance(paramHolder, c, paramInt, false, paramBoolean);
/* 115 */         if (((LivingEntity)entity).addEffect(mobEffectInstance, paramCommandSourceStack.getEntity())) {
/* 116 */           b++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (b == 0) {
/* 122 */       throw ERROR_GIVE_FAILED.create();
/*     */     }
/*     */     
/* 125 */     if (paramCollection.size() == 1) {
/* 126 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.give.success.single", new Object[] { paramMobEffect.getDisplayName(), ((Entity)paramCollection.iterator().next()).getDisplayName(), Integer.valueOf(paramInt / 20) }), true);
/*     */     } else {
/* 128 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.give.success.multiple", new Object[] { paramMobEffect.getDisplayName(), Integer.valueOf(paramCollection.size()), Integer.valueOf(paramInt / 20) }), true);
/*     */     } 
/*     */     
/* 131 */     return b;
/*     */   }
/*     */   
/*     */   private static int clearEffects(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection) throws CommandSyntaxException {
/* 135 */     byte b = 0;
/*     */     
/* 137 */     for (Entity entity : paramCollection) {
/* 138 */       if (entity instanceof LivingEntity && (
/* 139 */         (LivingEntity)entity).removeAllEffects()) {
/* 140 */         b++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (b == 0) {
/* 146 */       throw ERROR_CLEAR_EVERYTHING_FAILED.create();
/*     */     }
/*     */     
/* 149 */     if (paramCollection.size() == 1) {
/* 150 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.single", new Object[] { ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 152 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 155 */     return b;
/*     */   }
/*     */   
/*     */   private static int clearEffect(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, Holder<MobEffect> paramHolder) throws CommandSyntaxException {
/* 159 */     MobEffect mobEffect = (MobEffect)paramHolder.value();
/* 160 */     byte b = 0;
/*     */     
/* 162 */     for (Entity entity : paramCollection) {
/* 163 */       if (entity instanceof LivingEntity && (
/* 164 */         (LivingEntity)entity).removeEffect(paramHolder)) {
/* 165 */         b++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 170 */     if (b == 0) {
/* 171 */       throw ERROR_CLEAR_SPECIFIC_FAILED.create();
/*     */     }
/*     */     
/* 174 */     if (paramCollection.size() == 1) {
/* 175 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.single", new Object[] { paramMobEffect.getDisplayName(), ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 177 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.multiple", new Object[] { paramMobEffect.getDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 180 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\EffectCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */