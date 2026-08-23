/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.entity.raid.Raids;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RaidCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  37 */     paramCommandDispatcher.register(
/*  38 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("raid")
/*  39 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  40 */         .then(Commands.literal("start")
/*  41 */           .then(
/*  42 */             Commands.argument("omenlvl", (ArgumentType)IntegerArgumentType.integer(0))
/*  43 */             .executes(paramCommandContext -> start((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "omenlvl"))))))
/*     */ 
/*     */         
/*  46 */         .then(Commands.literal("stop").executes(paramCommandContext -> stop((CommandSourceStack)paramCommandContext.getSource()))))
/*  47 */         .then(Commands.literal("check").executes(paramCommandContext -> check((CommandSourceStack)paramCommandContext.getSource()))))
/*  48 */         .then(Commands.literal("sound")
/*  49 */           .then(
/*  50 */             Commands.argument("type", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  51 */             .executes(paramCommandContext -> playSound((CommandSourceStack)paramCommandContext.getSource(), ComponentArgument.getResolvedComponent(paramCommandContext, "type"))))))
/*     */         
/*  53 */         .then(Commands.literal("spawnleader").executes(paramCommandContext -> spawnLeader((CommandSourceStack)paramCommandContext.getSource()))))
/*  54 */         .then(Commands.literal("setomen").then(
/*  55 */             Commands.argument("level", (ArgumentType)IntegerArgumentType.integer(0))
/*  56 */             .executes(paramCommandContext -> setRaidOmenLevel((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "level"))))))
/*     */ 
/*     */         
/*  59 */         .then(Commands.literal("glow").executes(paramCommandContext -> glow((CommandSourceStack)paramCommandContext.getSource()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int glow(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/*  64 */     Raid raid = getRaid(paramCommandSourceStack.getPlayerOrException());
/*     */     
/*  66 */     if (raid != null) {
/*  67 */       Set set = raid.getAllRaiders();
/*  68 */       for (Raider raider : set) {
/*  69 */         raider.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000, 1));
/*     */       }
/*     */     } 
/*  72 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setRaidOmenLevel(CommandSourceStack paramCommandSourceStack, int paramInt) throws CommandSyntaxException {
/*  76 */     Raid raid = getRaid(paramCommandSourceStack.getPlayerOrException());
/*     */     
/*  78 */     if (raid != null) {
/*  79 */       int i = raid.getMaxRaidOmenLevel();
/*  80 */       if (paramInt > i) {
/*  81 */         paramCommandSourceStack.sendFailure((Component)Component.literal("Sorry, the max raid omen level you can set is " + i));
/*     */       } else {
/*  83 */         int j = raid.getRaidOmenLevel();
/*  84 */         raid.setRaidOmenLevel(paramInt);
/*  85 */         paramCommandSourceStack.sendSuccess(() -> Component.literal("Changed village's raid omen level from " + paramInt1 + " to " + paramInt2), false);
/*     */       } 
/*     */     } else {
/*  88 */       paramCommandSourceStack.sendFailure((Component)Component.literal("No raid found here"));
/*     */     } 
/*     */     
/*  91 */     return 1;
/*     */   }
/*     */   
/*     */   private static int spawnLeader(CommandSourceStack paramCommandSourceStack) {
/*  95 */     paramCommandSourceStack.sendSuccess(() -> Component.literal("Spawned a raid captain"), false);
/*     */     
/*  97 */     Raider raider = (Raider)EntityType.PILLAGER.create((Level)paramCommandSourceStack.getLevel(), EntitySpawnReason.COMMAND);
/*  98 */     if (raider == null) {
/*  99 */       paramCommandSourceStack.sendFailure((Component)Component.literal("Pillager failed to spawn"));
/* 100 */       return 0;
/*     */     } 
/* 102 */     raider.setPatrolLeader(true);
/* 103 */     raider.setItemSlot(EquipmentSlot.HEAD, Raid.getOminousBannerInstance((HolderGetter)paramCommandSourceStack.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/* 104 */     raider.setPos((paramCommandSourceStack.getPosition()).x, (paramCommandSourceStack.getPosition()).y, (paramCommandSourceStack.getPosition()).z);
/* 105 */     raider.finalizeSpawn((ServerLevelAccessor)paramCommandSourceStack.getLevel(), paramCommandSourceStack.getLevel().getCurrentDifficultyAt(BlockPos.containing((Position)paramCommandSourceStack.getPosition())), EntitySpawnReason.COMMAND, null);
/* 106 */     paramCommandSourceStack.getLevel().addFreshEntityWithPassengers((Entity)raider);
/*     */     
/* 108 */     return 1;
/*     */   }
/*     */   
/*     */   private static int playSound(CommandSourceStack paramCommandSourceStack, Component paramComponent) {
/* 112 */     if (paramComponent != null && paramComponent.getString().equals("local")) {
/* 113 */       ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 114 */       Vec3 vec3 = paramCommandSourceStack.getPosition().add(5.0D, 0.0D, 0.0D);
/* 115 */       serverLevel.playSeededSound(null, vec3.x, vec3.y, vec3.z, (Holder)SoundEvents.RAID_HORN, SoundSource.NEUTRAL, 2.0F, 1.0F, serverLevel.random.nextLong());
/*     */     } 
/* 117 */     return 1;
/*     */   }
/*     */   
/*     */   private static int start(CommandSourceStack paramCommandSourceStack, int paramInt) throws CommandSyntaxException {
/* 121 */     ServerPlayer serverPlayer = paramCommandSourceStack.getPlayerOrException();
/* 122 */     BlockPos blockPos = serverPlayer.blockPosition();
/*     */     
/* 124 */     if (serverPlayer.level().isRaided(blockPos)) {
/* 125 */       paramCommandSourceStack.sendFailure((Component)Component.literal("Raid already started close by"));
/* 126 */       return -1;
/*     */     } 
/*     */     
/* 129 */     Raids raids = serverPlayer.level().getRaids();
/* 130 */     Raid raid = raids.createOrExtendRaid(serverPlayer, serverPlayer.blockPosition());
/* 131 */     if (raid != null) {
/* 132 */       raid.setRaidOmenLevel(paramInt);
/* 133 */       raids.setDirty();
/* 134 */       paramCommandSourceStack.sendSuccess(() -> Component.literal("Created a raid in your local village"), false);
/*     */     } else {
/* 136 */       paramCommandSourceStack.sendFailure((Component)Component.literal("Failed to create a raid in your local village"));
/*     */     } 
/* 138 */     return 1;
/*     */   }
/*     */   
/*     */   private static int stop(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 142 */     ServerPlayer serverPlayer = paramCommandSourceStack.getPlayerOrException();
/* 143 */     BlockPos blockPos = serverPlayer.blockPosition();
/*     */     
/* 145 */     Raid raid = serverPlayer.level().getRaidAt(blockPos);
/*     */     
/* 147 */     if (raid != null) {
/* 148 */       raid.stop();
/* 149 */       paramCommandSourceStack.sendSuccess(() -> Component.literal("Stopped raid"), false);
/* 150 */       return 1;
/*     */     } 
/* 152 */     paramCommandSourceStack.sendFailure((Component)Component.literal("No raid here"));
/* 153 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int check(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 158 */     Raid raid = getRaid(paramCommandSourceStack.getPlayerOrException());
/*     */     
/* 160 */     if (raid != null) {
/* 161 */       StringBuilder stringBuilder1 = new StringBuilder();
/* 162 */       stringBuilder1.append("Found a started raid! ");
/* 163 */       paramCommandSourceStack.sendSuccess(() -> Component.literal(paramStringBuilder.toString()), false);
/* 164 */       StringBuilder stringBuilder2 = new StringBuilder();
/* 165 */       stringBuilder2.append("Num groups spawned: ");
/* 166 */       stringBuilder2.append(raid.getGroupsSpawned());
/* 167 */       stringBuilder2.append(" Raid omen level: ");
/* 168 */       stringBuilder2.append(raid.getRaidOmenLevel());
/* 169 */       stringBuilder2.append(" Num mobs: ");
/* 170 */       stringBuilder2.append(raid.getTotalRaidersAlive());
/* 171 */       stringBuilder2.append(" Raid health: ");
/* 172 */       stringBuilder2.append(raid.getHealthOfLivingRaiders());
/* 173 */       stringBuilder2.append(" / ");
/* 174 */       stringBuilder2.append(raid.getTotalHealth());
/* 175 */       paramCommandSourceStack.sendSuccess(() -> Component.literal(paramStringBuilder.toString()), false);
/* 176 */       return 1;
/*     */     } 
/* 178 */     paramCommandSourceStack.sendFailure((Component)Component.literal("Found no started raids"));
/* 179 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Raid getRaid(ServerPlayer paramServerPlayer) {
/* 184 */     return paramServerPlayer.level().getRaidAt(paramServerPlayer.blockPosition());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\RaidCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */